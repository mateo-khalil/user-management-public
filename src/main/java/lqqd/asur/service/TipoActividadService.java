package lqqd.asur.service;

import lqqd.asur.model.TipoActividad;
import lqqd.asur.repository.TipoActividadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoActividadService {

    @Autowired
    private TipoActividadRepository tipoActividadRepository;

    public List<TipoActividad> listarTiposDeActividad() {
        return tipoActividadRepository.findAll();
    }

    public List<TipoActividad> listarTiposDeActividadActivos() {
        return tipoActividadRepository.findByActivo(true);
    }

    public Optional<TipoActividad> buscarTipoActividadPorId(Long id) {
        return tipoActividadRepository.findById(id);
    }

    public void guardarTipoActividad(TipoActividad tipoActividad) {
        tipoActividadRepository.save(tipoActividad);
    }

    public void eliminarTipoActividad(Long id) {
        Optional<TipoActividad> tipoActividad = tipoActividadRepository.findById(id);
        tipoActividad.ifPresent(ta -> {
            ta.setActivo(false); // Baja lógica
            tipoActividadRepository.save(ta);
        });
    }

    public List<TipoActividad> listarTodosLosTipos() {
        return tipoActividadRepository.findAll();
    }

}

