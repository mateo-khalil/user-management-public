package lqqd.asur.service;

import lqqd.asur.model.Usuario;
import lqqd.asur.model.usuario.Estado;
import lqqd.asur.model.usuario.Role;
import lqqd.asur.repository.UsuarioRepository;
import lqqd.asur.utils.ConsoleColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<Usuario> listarUsuariosPorEstado(Estado estado) {
        return usuarioRepository.findByEstado(estado);
    }

    public List<Usuario> listarUsuariosPorNombresApellidos(String criterio) {
        return usuarioRepository.findByNombresOrApellidosContainingIgnoreCase(criterio);
    }

    public Usuario buscarUsuarioPorDocumento(Long documento) {
        return (Usuario) usuarioRepository.findByDocumento(documento).orElse(null);
    }

    public List<Usuario> listarUsuariosPorRol(Role role) {
        return usuarioRepository.findByRole(role);
    }

    public Usuario login(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getEstado() == Estado.INACTIVO) {
                System.out.println(ConsoleColors.YELLOW + "El usuario está inactivo. No se permite el inicio de sesión.");
                return null; // Usuario inactivo
            }

            if (usuario.getPassword().equals(password)) {
                ConsoleColors.limpiarConsola();
                System.out.println(ConsoleColors.GREEN + "Inicio de sesión exitoso.");
                return usuario; // Login exitoso
            } else {
                System.out.println(ConsoleColors.RED + "Contraseña incorrecta.");
            }
        } else {
            System.out.println(ConsoleColors.RED + "Usuario no encontrado.");
        }
        return null;
    }

    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public boolean existeDocumento(Long documento) {
        return usuarioRepository.findByDocumento(documento).isPresent();
    }

    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    public Object getUserById(Long userId) {
        return usuarioRepository.findById(userId);
    }
}
