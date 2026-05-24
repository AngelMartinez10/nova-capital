package com.novacapital.repository;

import com.novacapital.entity.Puntuacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PuntuacionRepository extends JpaRepository<Puntuacion, Integer> {

    List<Puntuacion> findByProyectoIdProyecto(Integer idProyecto);

    Optional<Puntuacion> findByClienteIdClienteAndProyectoIdProyecto(Integer idCliente, Integer idProyecto);

    boolean existsByClienteIdClienteAndProyectoIdProyecto(Integer idCliente, Integer idProyecto);

    @Query("SELECT AVG(p.valor) FROM Puntuacion p WHERE p.proyecto.idProyecto = :idProyecto")
    Double calcularMediaPorProyecto(Integer idProyecto);
}