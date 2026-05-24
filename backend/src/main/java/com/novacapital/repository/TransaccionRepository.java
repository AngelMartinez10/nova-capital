package com.novacapital.repository;

import com.novacapital.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> {

    List<Transaccion> findByClienteIdClienteOrderByFechaDesc(Integer idCliente);
}