package com.novacapital.service;

import com.novacapital.config.JwtUtil;
import com.novacapital.dto.AuthResponse;
import com.novacapital.dto.LoginRequest;
import com.novacapital.dto.RegistroRequest;
import com.novacapital.entity.Aurus;
import com.novacapital.entity.Cliente;
import com.novacapital.entity.ClienteReto;
import com.novacapital.entity.Reto;
import com.novacapital.exception.BadRequestException;
import com.novacapital.repository.AurusRepository;
import com.novacapital.repository.ClienteRepository;
import com.novacapital.repository.ClienteRetoRepository;
import com.novacapital.repository.RetoRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de autenticación.
 * Gestiona el registro de nuevos clientes y el login.
 */
@Service
public class AuthService {

    private final ClienteRepository clienteRepo;
    private final AurusRepository aurusRepo;
    private final RetoRepository retoRepo;
    private final ClienteRetoRepository clienteRetoRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;

    public AuthService(ClienteRepository clienteRepo,
                       AurusRepository aurusRepo,
                       RetoRepository retoRepo,
                       ClienteRetoRepository clienteRetoRepo,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       AuthenticationManager authManager) {
        this.clienteRepo     = clienteRepo;
        this.aurusRepo       = aurusRepo;
        this.retoRepo        = retoRepo;
        this.clienteRetoRepo = clienteRetoRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil         = jwtUtil;
        this.authManager     = authManager;
    }

    /**
     * Registra un nuevo cliente con saldo inicial de 1000 Aurus.
     * Asigna automáticamente todos los retos disponibles.
     */
    @Transactional
    public AuthResponse registrar(RegistroRequest request) {
        // Comprobar que el email no existe ya
        if (clienteRepo.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado: " + request.getEmail());
        }

        // Crear cliente con contraseña hasheada
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setEmail(request.getEmail());
        cliente.setContrasena(passwordEncoder.encode(request.getContrasena()));
        cliente.setTelefono(request.getTelefono());
        cliente = clienteRepo.save(cliente);

        // Crear saldo Aurus (1000 por defecto)
        Aurus aurus = new Aurus(cliente);
        aurusRepo.save(aurus);

        // Asignar todos los retos activos al nuevo cliente
        List<Reto> retos = retoRepo.findByActivoTrue();
        for (Reto reto : retos) {
            ClienteReto cr = new ClienteReto();
            cr.setId(new ClienteReto.ClienteRetoId(cliente.getIdCliente(), reto.getIdReto()));
            cr.setCliente(cliente);
            cr.setReto(reto);
            cr.setCompletado(false);
            clienteRetoRepo.save(cr);
        }

        // Generar token JWT
        String token = jwtUtil.generarToken(cliente.getEmail());
        return new AuthResponse(token, cliente.getIdCliente(),
                cliente.getNombre(), cliente.getEmail(), aurus.getSaldo());
    }

    /**
     * Autentica al cliente con email y contraseña y devuelve un token JWT.
     */
    public AuthResponse login(LoginRequest request) {
        // Esto lanza excepción si las credenciales son incorrectas
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getContrasena()));

        Cliente cliente = clienteRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado"));

        // Obtener el saldo Aurus del cliente (si no existe, se pone 0)
        BigDecimal saldo = BigDecimal.ZERO;
        Optional<Aurus> aurusOpt = aurusRepo.findByClienteIdCliente(cliente.getIdCliente());
        if (aurusOpt.isPresent()) {
            saldo = aurusOpt.get().getSaldo();
        }

        String token = jwtUtil.generarToken(cliente.getEmail());
        return new AuthResponse(token, cliente.getIdCliente(),
                cliente.getNombre(), cliente.getEmail(), saldo);
    }
}