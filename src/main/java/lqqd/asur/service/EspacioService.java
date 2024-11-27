package lqqd.asur.service;

import lqqd.asur.model.Espacio;
import lqqd.asur.repository.EspacioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EspacioService {

    @Autowired
    private EspacioRepository espacioRepository;

    public Optional<Espacio> obtenerEspacioPorId(Long id) {
        return espacioRepository.findById(id);
    }

    public List<Espacio> obtenerEspaciosDisponibles(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Espacio> espaciosDisponibles = new ArrayList<>();
        List<Espacio> todosLosEspacios = espacioRepository.findAll();

        for (Espacio espacio : todosLosEspacios) {
            boolean disponible = espacioRepository.espacioDisponible(
                    espacio.getId(),
                    fechaInicio,
                    fechaFin
            );
            if (disponible) {
                espaciosDisponibles.add(espacio);
            }
        }

        return espaciosDisponibles;
    }
}
