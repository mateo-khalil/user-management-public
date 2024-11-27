package lqqd.asur.service;

import lqqd.asur.model.Pago;
import lqqd.asur.model.Usuario;
import lqqd.asur.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    public List<Pago> listarTodosLosPagos() {
        return pagoRepository.findAll();
    }

    public List<Pago> listarPagosPorUsuario(Usuario usuario) {
        return pagoRepository.findByUsuario(usuario);
    }

    public List<Pago> listarPagosPorFecha(LocalDateTime inicio, LocalDateTime fin) {
        return pagoRepository.findByFechaPagoBetween(inicio, fin);
    }

    public Optional<Pago> buscarPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public void guardarPago(Pago pago) {
        pagoRepository.save(pago);
    }
}

