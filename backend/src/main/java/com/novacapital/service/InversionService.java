package com.novacapital.service;

import com.novacapital.dto.InversionRequest;
import com.novacapital.dto.InversionResponse;
import com.novacapital.entity.*;
import com.novacapital.exception.BadRequestException;
import com.novacapital.exception.ResourceNotFoundException;
import com.novacapital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de inversiones.
 *
 * Al realizar una inversión:
 *  1. Valida saldo suficiente y proyecto activo
 *  2. Descuenta Aurus del cliente
 *  3. Suma al proyecto y cambia estado si se financia
 *  4. Registra la inversión y la transacción
 *  5. Comprueba retos automáticamente
 *  6. Crea notificaciones
 */
@Service
public class InversionService {

    private final InversionRepository inversionRepo;
    private final ProyectoRepository proyectoRepo;
    private final AurusRepository aurusRepo;
    private final TransaccionRepository transaccionRepo;
    private final ClienteService clienteService;
    private final RetoService retoService;
    private final NotificacionService notificacionService;

    public InversionService(InversionRepository inversionRepo,
                            ProyectoRepository proyectoRepo,
                            AurusRepository aurusRepo,
                            TransaccionRepository transaccionRepo,
                            ClienteService clienteService,
                            RetoService retoService,
                            NotificacionService notificacionService) {
        this.inversionRepo       = inversionRepo;
        this.proyectoRepo        = proyectoRepo;
        this.aurusRepo           = aurusRepo;
        this.transaccionRepo     = transaccionRepo;
        this.clienteService      = clienteService;
        this.retoService         = retoService;
        this.notificacionService = notificacionService;
    }

    /**
     * Realiza una inversión completa con todas sus implicaciones.
     */
    @Transactional
    public InversionResponse invertir(InversionRequest request, String emailCliente) {

        // 1. Obtener cliente y proyecto
        Cliente cliente = clienteService.obtenerPorEmail(emailCliente);
        Proyecto proyecto = proyectoRepo.findById(request.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proyecto no encontrado: " + request.getIdProyecto()));

        // 2. Validaciones
        if (proyecto.getEstado() == Proyecto.EstadoProyecto.FINANCIADO) {
            throw new BadRequestException("Este proyecto ya está completamente financiado");
        }
        if (proyecto.getEstado() == Proyecto.EstadoProyecto.CANCELADO) {
            throw new BadRequestException("No se puede invertir en un proyecto cancelado");
        }

        Aurus aurus = aurusRepo.findByClienteIdCliente(cliente.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Saldo Aurus no encontrado"));

        BigDecimal cantidad = request.getCantidad();
        if (aurus.getSaldo().compareTo(cantidad) < 0) {
            throw new BadRequestException(
                    "Saldo insuficiente. Tienes " + aurus.getSaldo() + " Aurus y necesitas " + cantidad);
        }

        // 3. Descontar Aurus del cliente
        BigDecimal saldoAnterior = aurus.getSaldo();
        BigDecimal saldoNuevo    = saldoAnterior.subtract(cantidad);
        aurus.setSaldo(saldoNuevo);
        aurusRepo.save(aurus);

        // 4. Actualizar el proyecto
        proyecto.setCantidadActual(proyecto.getCantidadActual().add(cantidad));
        if (proyecto.getEstado() == Proyecto.EstadoProyecto.ACTIVO &&
                proyecto.getCantidadActual().compareTo(BigDecimal.ZERO) > 0) {
            proyecto.setEstado(Proyecto.EstadoProyecto.EN_PROGRESO);
        }
        boolean proyectoFinanciado = proyecto.getCantidadActual()
                .compareTo(proyecto.getObjetivoInversion()) >= 0;
        if (proyectoFinanciado) {
            proyecto.setEstado(Proyecto.EstadoProyecto.FINANCIADO);
        }
        proyectoRepo.save(proyecto);

        // 5. Guardar la inversión
        Inversion inversion = new Inversion();
        inversion.setCliente(cliente);
        inversion.setProyecto(proyecto);
        inversion.setCantidad(cantidad);
        inversionRepo.save(inversion);

        // 6. Registrar transacción
        Transaccion transaccion = new Transaccion();
        transaccion.setCliente(cliente);
        transaccion.setTipo(Transaccion.TipoTransaccion.INVERSION);
        transaccion.setCantidad(cantidad);
        transaccion.setSaldoAnterior(saldoAnterior);
        transaccion.setSaldoPosterior(saldoNuevo);
        transaccion.setDescripcion("Inversión en: " + proyecto.getNombre());
        transaccionRepo.save(transaccion);

        // 7. Notificación al inversor
        notificacionService.crearNotificacion(cliente,
                "💰 Inversión de " + cantidad + " Aurus en \"" + proyecto.getNombre() + "\" registrada");

        // 8. Si el proyecto se ha financiado, notificar también al creador
        if (proyectoFinanciado && proyecto.getCliente() != null) {
            notificacionService.crearNotificacion(proyecto.getCliente(),
                    "🎉 ¡Tu proyecto \"" + proyecto.getNombre() + "\" ha sido financiado completamente!");
            retoService.comprobarRetoProyectoFinanciado(cliente);
        }

        // 9. Comprobar retos del inversor
        long totalInversiones   = inversionRepo.countByClienteIdCliente(cliente.getIdCliente());
        long proyectosDistintos = proyectoRepo.countProyectosInvertidosPorCliente(cliente.getIdCliente());
        retoService.comprobarRetoPrimeraInversion(cliente, totalInversiones);
        retoService.comprobarRetoInversorDiversificado(cliente, proyectosDistintos);
        retoService.comprobarRetoInversorVeterano(cliente, totalInversiones);

        // 10. Construir respuesta
        InversionResponse response = InversionResponse.from(inversion);

        // Refrescar saldo por si se añadieron recompensas de retos
        BigDecimal saldoFinal = saldoNuevo;
        Optional<Aurus> aurusRefrescado = aurusRepo.findByClienteIdCliente(cliente.getIdCliente());
        if (aurusRefrescado.isPresent()) {
            saldoFinal = aurusRefrescado.get().getSaldo();
        }
        response.setNuevoSaldoAurus(saldoFinal);

        return response;
    }

    // Convierte la lista de entidades Inversion a lista de DTOs InversionResponse
    public List<InversionResponse> obtenerHistorial(Integer idCliente) {
        List<Inversion> inversiones = inversionRepo
                .findByClienteIdClienteOrderByFechaInversionDesc(idCliente);

        List<InversionResponse> resultado = new ArrayList<>();
        for (Inversion inv : inversiones) {
            resultado.add(InversionResponse.from(inv));
        }
        return resultado;
    }

    // Convierte la lista de entidades Inversion de un proyecto a lista de DTOs
    public List<InversionResponse> obtenerPorProyecto(Integer idProyecto) {
        List<Inversion> inversiones = inversionRepo.findByProyectoIdProyecto(idProyecto);

        List<InversionResponse> resultado = new ArrayList<>();
        for (Inversion inv : inversiones) {
            resultado.add(InversionResponse.from(inv));
        }
        return resultado;
    }
}