package com.novacapital.service;

import com.novacapital.dto.ProyectoRequest;
import com.novacapital.dto.ProyectoResponse;
import com.novacapital.entity.Cliente;
import com.novacapital.entity.Proyecto;
import com.novacapital.exception.BadRequestException;
import com.novacapital.exception.ResourceNotFoundException;
import com.novacapital.repository.ProyectoRepository;
import com.novacapital.repository.PuntuacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProyectoService {

    private final ProyectoRepository proyectoRepo;
    private final ClienteService clienteService;
    private final PuntuacionRepository puntuacionRepo;

    public ProyectoService(ProyectoRepository proyectoRepo,
                           ClienteService clienteService,
                           PuntuacionRepository puntuacionRepo) {
        this.proyectoRepo   = proyectoRepo;
        this.clienteService = clienteService;
        this.puntuacionRepo = puntuacionRepo;
    }

    public List<ProyectoResponse> listarTodos() {
        return proyectoRepo.findAll().stream()
                .map(this::toResponseConMedia)
                .collect(Collectors.toList());
    }

    public List<ProyectoResponse> listarPorEstado(String estado) {
        try {
            Proyecto.EstadoProyecto estadoEnum = Proyecto.EstadoProyecto.valueOf(estado.toUpperCase());
            return proyectoRepo.findByEstado(estadoEnum).stream()
                    .map(this::toResponseConMedia)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado inválido: " + estado +
                    ". Valores válidos: ACTIVO, EN_PROGRESO, FINANCIADO, CANCELADO");
        }
    }

    public List<ProyectoResponse> listarPorCliente(Integer idCliente) {
        return proyectoRepo.findByClienteIdCliente(idCliente).stream()
                .map(this::toResponseConMedia)
                .collect(Collectors.toList());
    }

    public ProyectoResponse obtenerPorId(Integer id) {
        Proyecto proyecto = proyectoRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + id));
        return toResponseConMedia(proyecto);
    }

    @Transactional
    public ProyectoResponse crearProyecto(ProyectoRequest request, String emailCliente) {
        Cliente cliente = clienteService.obtenerPorEmail(emailCliente);

        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(request.getNombre());
        proyecto.setDescripcion(request.getDescripcion());
        proyecto.setCategoria(request.getCategoria());
        proyecto.setObjetivoInversion(request.getObjetivoInversion());
        proyecto.setRendimientoMensual(request.getRendimientoMensual());
        proyecto.setCliente(cliente);

        return toResponseConMedia(proyectoRepo.save(proyecto));
    }

    @Transactional
    public ProyectoResponse cancelarProyecto(Integer idProyecto, String emailCliente) {
        Proyecto proyecto = proyectoRepo.findById(idProyecto)
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado: " + idProyecto));

        Cliente cliente = clienteService.obtenerPorEmail(emailCliente);
        if (!proyecto.getCliente().getIdCliente().equals(cliente.getIdCliente())) {
            throw new BadRequestException("No tienes permiso para cancelar este proyecto");
        }
        if (proyecto.getEstado() == Proyecto.EstadoProyecto.FINANCIADO) {
            throw new BadRequestException("No se puede cancelar un proyecto financiado");
        }

        proyecto.setEstado(Proyecto.EstadoProyecto.CANCELADO);
        return toResponseConMedia(proyectoRepo.save(proyecto));
    }

    public long contarProyectosPorCliente(Integer idCliente) {
        return proyectoRepo.findByClienteIdCliente(idCliente).size();
    }

    private ProyectoResponse toResponseConMedia(Proyecto p) {
        ProyectoResponse dto = ProyectoResponse.from(p);
        Double media = puntuacionRepo.calcularMediaPorProyecto(p.getIdProyecto());
        dto.setMediaValoraciones(media);
        return dto;
    }
}