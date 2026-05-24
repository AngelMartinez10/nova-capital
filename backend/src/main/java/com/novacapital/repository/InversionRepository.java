package com.novacapital.repository;

import com.novacapital.entity.Inversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InversionRepository extends JpaRepository<Inversion, Integer> {

    List<Inversion> findByClienteIdClienteOrderByFechaInversionDesc(Integer idCliente);

    List<Inversion> findByProyectoIdProyecto(Integer idProyecto);

    long countByClienteIdCliente(Integer idCliente);
}