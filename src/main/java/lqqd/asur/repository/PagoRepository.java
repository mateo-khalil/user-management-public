package lqqd.asur.repository;

import lqqd.asur.model.Pago;
import lqqd.asur.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByUsuario(Usuario usuario);

    List<Pago> findByFechaPagoBetween(LocalDateTime inicio, LocalDateTime fin);
}

