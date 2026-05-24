package com.novacapital.service;

import com.novacapital.dto.PuntuacionRequest;
import com.novacapital.dto.PuntuacionResponse;
import com.novacapital.entity.Cliente;
import com.novacapital.entity.Proyecto;
import com.novacapital.entity.Puntuacion;
import com.novacapital.exception.BadRequestException;
import com.novacapital.exception.ResourceNotFoundException;
import com.novacapital.repository.ProyectoRepository;
import com.novacapital.repository.PuntuacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PuntuacionService {

    private final PuntuacionRepository puntuacionRepo;
    private final ProyectoRepository   proyectoRepo;
    private final ClienteService       clienteService;

    public PuntuacionService(PuntuacionRepository puntuacionRepo,
                             ProyectoRepository proyectoRepo,
                             ClienteService clienteService) {
        this.puntuacionRepo = puntuacionRepo;
        this.proyectoRepo   = proyectoRepo;
        this.clienteService = clienteService;
    }

    @Transactional
    public PuntuacionResponse valorar(PuntuacionRequest request, String emailCliente) {
        Cliente cliente   = clienteService.obtenerPorEmail(emailCliente);
        Proyecto proyecto = proyectoRepo.findById(request.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proyecto no encontrado: " + request.getIdProyecto()));

        if (puntuacionRepo.existsByClienteIdClienteAndProyectoIdProyecto(
                cliente.getIdCliente(), proyecto.getIdProyecto())) {
            throw new BadRequestException("Ya has valorado este proyecto anteriormente");
        }

        Puntuacion p = new Puntuacion();
        p.setCliente(cliente);
        p.setProyecto(proyecto);
        p.setValor(request.getValor());
        p.setComentario(request.getComentario());
        return PuntuacionResponse.from(puntuacionRepo.save(p));
    }

    // Convierte la lista de entidades Puntuacion a lista de DTOs PuntuacionResponse
    public List<PuntuacionResponse> obtenerPorProyecto(Integer idProyecto) {
        List<Puntuacion> puntuaciones = puntuacionRepo.findByProyectoIdProyecto(idProyecto);

        List<PuntuacionResponse> resultado = new ArrayList<>();
        for (Puntuacion p : puntuaciones) {
            resultado.add(PuntuacionResponse.from(p));
        }
        return resultado;
    }

    // Si la BD no devuelve media (sin valoraciones aún), retorna 0.0 en lugar de null
    public Double obtenerMedia(Integer idProyecto) {
        Double media = puntuacionRepo.calcularMediaPorProyecto(idProyecto);
        if (media == null) {
            return 0.0;
        }
        return media;
    }
}