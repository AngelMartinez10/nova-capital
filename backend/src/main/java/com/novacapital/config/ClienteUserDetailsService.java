package com.novacapital.config;

import com.novacapital.entity.Cliente;
import com.novacapital.repository.ClienteRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Spring Security llama a este método automáticamente cuando alguien intenta hacer login
@Service
public class ClienteUserDetailsService implements UserDetailsService {

    private final ClienteRepository clienteRepo;

    public ClienteUserDetailsService(ClienteRepository clienteRepo) {
        this.clienteRepo = clienteRepo;
    }

    // Busca el cliente por email y lo convierte al formato que entiende Spring Security
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Cliente> resultado = clienteRepo.findByEmail(email);

        if (!resultado.isPresent()) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        Cliente cliente = resultado.get();

        return User.withUsername(cliente.getEmail())
                .password(cliente.getContrasena())
                .roles("CLIENTE")
                .build();
    }
}