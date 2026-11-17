package io.github.go.gerenciamento_adocoes.auth;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    public UsuarioDetailsService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usuario = repository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
        var authorities = usuario.getPapeis().stream()
                .map(papel -> new SimpleGrantedAuthority("ROLE_" + papel.getNome()))
                .toList();

        return User.withUsername(usuario.getUsername())
                .password(usuario.getPasswordHash())
                .disabled(!usuario.isEnabled())
                .authorities(authorities)
                .build();
    }
}
