package com.novacapital.repository;

import com.novacapital.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Integer> {

    // Proyectos por cliente (creador)
    List<Proyecto> findByClienteIdCliente(Integer idCliente);

    // Proyectos activos
    List<Proyecto> findByEstado(Proyecto.EstadoProyecto estado);

    // Proyectos por categoría
    List<Proyecto> findByCategoria(String categoria);

    // Buscar por nombre (contiene texto, sin distinción mayúsculas)
    List<Proyecto> findByNombreContainingIgnoreCase(String nombre);

    // Proyectos con al menos una inversión del cliente
    @Query("SELECT DISTINCT i.proyecto FROM Inversion i WHERE i.cliente.idCliente = :idCliente")
    List<Proyecto> findProyectosInvertidosPorCliente(Integer idCliente);

    // Contar proyectos distintos en los que ha invertido un cliente
    @Query("SELECT COUNT(DISTINCT i.proyecto.idProyecto) FROM Inversion i WHERE i.cliente.idCliente = :idCliente")
    long countProyectosInvertidosPorCliente(Integer idCliente);
}