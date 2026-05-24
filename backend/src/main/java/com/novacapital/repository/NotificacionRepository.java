package com.novacapital.repository;

import com.novacapital.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Integer> {

    List<Notificacion> findByClienteIdClienteOrderByFechaDesc(Integer idCliente);

    List<Notificacion> findByClienteIdClienteAndLeidoFalse(Integer idCliente);

    long countByClienteIdClienteAndLeidoFalse(Integer idCliente);

    @Modifying
    @Query("UPDATE Notificacion n SET n.leido = true WHERE n.cliente.idCliente = :idCliente")
    void marcarTodasComoLeidas(Integer idCliente);
}