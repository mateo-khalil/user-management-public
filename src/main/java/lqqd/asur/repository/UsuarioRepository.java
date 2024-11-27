package lqqd.asur.repository;

import lqqd.asur.model.Usuario;
import lqqd.asur.model.usuario.Estado;
import lqqd.asur.model.usuario.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<Usuario> findByEmail(@Param("email") String email);

    Optional<Object> findByDocumento(Long documento);

    @Query("SELECT u FROM Usuario u WHERE u.estado = :estado")
    List<Usuario> findByEstado(@Param("estado") Estado estado);

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.nombres) LIKE LOWER(CONCAT('%', :criterio, '%')) " +
            "OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :criterio, '%'))")
    List<Usuario> findByNombresOrApellidosContainingIgnoreCase(@Param("criterio") String criterio);

    @Query("SELECT u FROM Usuario u WHERE u.role = :role")
    List<Usuario> findByRole(@Param("role") Role role);

}
