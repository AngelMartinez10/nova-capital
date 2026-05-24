package com.novacapital.repository;

import com.novacapital.entity.Aurus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AurusRepository extends JpaRepository<Aurus, Integer> {
    Optional<Aurus> findByClienteIdCliente(Integer idCliente);
}