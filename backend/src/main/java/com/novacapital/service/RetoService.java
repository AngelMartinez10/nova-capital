package com.novacapital.service;

import com.novacapital.dto.ClienteRetoResponse;
import com.novacapital.entity.*;
import com.novacapital.exception.ResourceNotFoundException;
import com.novacapital.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RetoService {

    private final RetoRepository        retoRepo;
    private final ClienteRetoRepository clienteRetoRepo;
    private final AurusRepository       aurusRepo;
    private final TransaccionRepository transaccionRepo;
    private final NotificacionService   notificacionService;

    public RetoService(RetoRepository retoRepo,
                       ClienteRetoRepository clienteRetoRepo,
                       AurusRepository aurusRepo,
                       TransaccionRepository transaccionRepo,
                       NotificacionService notificacionService) {
        this.retoRepo            = retoRepo;
        this.clienteRetoRepo     = clienteRetoRepo;
        this.aurusRepo           = aurusRepo;
        this.transaccionRepo     = transaccionRepo;
        this.notificacionService = notificacionService;
    }

    // Convierte la lista de retos del cliente a lista de DTOs ClienteRetoResponse
    public List<ClienteRetoResponse> obtenerRetosDeCliente(Integer idCliente) {
        List<ClienteReto> retos = clienteRetoRepo.findByClienteIdCliente(idCliente);

        List<ClienteRetoResponse> resultado = new ArrayList<>();
        for (ClienteReto cr : retos) {
            resultado.add(ClienteRetoResponse.from(cr));
        }
        return resultado;
    }

    public List<Reto> obtenerRetosDisponibles() {
        return retoRepo.findByActivoTrue();
    }

    @Transactional
    public void comprobarRetoPrimeraInversion(Cliente cliente, long totalInversiones) {
        if (totalInversiones == 1) completarRetoSiPendiente(cliente, 1);
    }

    @Transactional
    public void comprobarRetoInversorDiversificado(Cliente cliente, long proyectosDistintos) {
        if (proyectosDistintos >= 3) completarRetoSiPendiente(cliente, 3);
    }

    @Transactional
    public void comprobarRetoProyectoFinanciado(Cliente cliente) {
        completarRetoSiPendiente(cliente, 4);
    }

    @Transactional
    public void comprobarRetoCreadorProyectos(Cliente cliente, long totalProyectos) {
        if (totalProyectos == 1) completarRetoSiPendiente(cliente, 2);
    }

    @Transactional
    public void comprobarRetoInversorVeterano(Cliente cliente, long totalInversiones) {
        if (totalInversiones >= 10) completarRetoSiPendiente(cliente, 5);
    }

    // Marca el reto como completado, suma la recompensa al saldo y crea la notificación
    @Transactional
    protected void completarRetoSiPendiente(Cliente cliente, Integer idReto) {
        Optional<ClienteReto> resultado = clienteRetoRepo
                .findByClienteIdClienteAndRetoIdReto(cliente.getIdCliente(), idReto);

        if (!resultado.isPresent()) {
            return;
        }

        ClienteReto cr = resultado.get();

        // Si ya está completado, no hacemos nada
        if (Boolean.TRUE.equals(cr.getCompletado())) {
            return;
        }

        // Marcar reto como completado
        cr.setCompletado(true);
        cr.setFechaCompletado(LocalDateTime.now());
        clienteRetoRepo.save(cr);

        // Sumar la recompensa al saldo Aurus del cliente
        Aurus aurus = aurusRepo.findByClienteIdCliente(cliente.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Saldo Aurus no encontrado"));

        BigDecimal saldoAnterior = aurus.getSaldo();
        BigDecimal recompensa    = cr.getReto().getRecompensa();
        BigDecimal saldoNuevo    = saldoAnterior.add(recompensa);
        aurus.setSaldo(saldoNuevo);
        aurusRepo.save(aurus);

        // Registrar la transacción de tipo recompensa
        Transaccion t = new Transaccion();
        t.setCliente(cliente);
        t.setTipo(Transaccion.TipoTransaccion.RECOMPENSA);
        t.setCantidad(recompensa);
        t.setSaldoAnterior(saldoAnterior);
        t.setSaldoPosterior(saldoNuevo);
        t.setDescripcion("Recompensa por reto: " + cr.getReto().getTitulo());
        transaccionRepo.save(t);

        // Notificar al cliente
        notificacionService.crearNotificacion(cliente,
                "Reto completado! \"" + cr.getReto().getTitulo()
                        + "\" - +" + recompensa + " Aurus");
    }
}