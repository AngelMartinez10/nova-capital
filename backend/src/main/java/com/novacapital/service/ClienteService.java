package com.novacapital.service;

import com.novacapital.dto.ClienteResponse;
import com.novacapital.entity.Aurus;
import com.novacapital.entity.Cliente;
import com.novacapital.exception.ResourceNotFoundException;
import com.novacapital.repository.AurusRepository;
import com.novacapital.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Servicio de gestión de clientes.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepo;
    private final AurusRepository aurusRepo;

    public ClienteService(ClienteRepository clienteRepo, AurusRepository aurusRepo) {
        this.clienteRepo = clienteRepo;
        this.aurusRepo   = aurusRepo;
    }

    /**
     * Obtiene el perfil completo de un cliente.
     */
    public ClienteResponse obtenerPerfil(Integer idCliente) {
        Cliente cliente = clienteRepo.findById(idCliente)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + idCliente));

        // Busca el saldo; si no existe el registro Aurus, devuelve 0
        BigDecimal saldo = BigDecimal.ZERO;
        Optional<Aurus> aurusOpt = aurusRepo.findByClienteIdCliente(idCliente);
        if (aurusOpt.isPresent()) {
            saldo = aurusOpt.get().getSaldo();
        }

        return ClienteResponse.from(cliente, saldo);
    }

    /**
     * Obtiene el cliente por email (usado internamente por otros servicios).
     */
    public Cliente obtenerPorEmail(String email) {
        return clienteRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado: " + email));
    }

    /**
     * Devuelve el saldo Aurus de un cliente.
     */
    public BigDecimal obtenerSaldo(Integer idCliente) {
        // Busca el saldo; si no existe el registro Aurus, devuelve 0
        BigDecimal saldo = BigDecimal.ZERO;
        Optional<Aurus> aurusOpt = aurusRepo.findByClienteIdCliente(idCliente);
        if (aurusOpt.isPresent()) {
            saldo = aurusOpt.get().getSaldo();
        }
        return saldo;
    }
}