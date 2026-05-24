package com.novacapital.repository;

import com.novacapital.entity.ClienteReto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRetoRepository extends JpaRepository<ClienteReto, ClienteReto.ClienteRetoId> {

    List<ClienteReto> findByClienteIdCliente(Integer idCliente);

    Optional<ClienteReto> findByClienteIdClienteAndRetoIdReto(Integer idCliente, Integer idReto);

    List<ClienteReto> findByClienteIdClienteAndCompletadoTrue(Integer idCliente);
}