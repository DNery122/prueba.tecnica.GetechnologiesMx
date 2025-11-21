package com.example.prueba.tecnica.GetechnologiesMx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prueba.tecnica.GetechnologiesMx.exception.DuplicateIdentifierException;
import com.example.prueba.tecnica.GetechnologiesMx.exception.InvalidPersonaException;
import com.example.prueba.tecnica.GetechnologiesMx.exception.PersonaNotFoundException;
import com.example.prueba.tecnica.GetechnologiesMx.model.Persona;
import com.example.prueba.tecnica.GetechnologiesMx.repository.FacturaRepository;
import com.example.prueba.tecnica.GetechnologiesMx.repository.PersonaRepository;

@Service
@Transactional
public class DirectorioService {
    private static final Logger logger = LoggerFactory.getLogger(DirectorioService.class);
    private final PersonaRepository personaRepository;
    private final FacturaRepository facturaRepository;

    public DirectorioService(PersonaRepository personaRepository,
            FacturaRepository facturaRepository) {
        this.personaRepository = personaRepository;
        this.facturaRepository = facturaRepository;
    }

    public Persona crearPersona(Persona persona) {
        logger.info("Creando nueva persona: {}", persona.getIdentificacion());

        if (persona.getNombre() == null || persona.getNombre().isBlank()) {
            throw new InvalidPersonaException("El nombre es obligatorio");
        }
        if (persona.getApellidoPaterno() == null || persona.getApellidoPaterno().isBlank()) {
            throw new InvalidPersonaException("El apellido paterno es obligatorio");
        }
        if (persona.getIdentificacion() == null || persona.getIdentificacion().isBlank()) {
            throw new InvalidPersonaException("La identificacion es obligatoria");
        }
        if (personaRepository.findByIdentificacion(persona.getIdentificacion()).isPresent()) {
            throw new DuplicateIdentifierException("El identificador ya extiste: " + persona.getIdentificacion());
        }

        Persona saved = personaRepository.save(persona);
        logger.info("Persona creada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    public Persona obtenerPersona(Long id) {
        logger.info("Buscando persona con ID: {}", id);
        return personaRepository.findById(id)
                .orElseThrow(() -> new PersonaNotFoundException("Persona no concontrada con ID " + id));
    }

    public Persona obtenerPersonaPorIdentificacion(String identifier) {
        logger.info("Buscando persona con identificacion : {}", identifier);
        return personaRepository.findByIdentificacion(identifier)
                .orElseThrow(
                        () -> new PersonaNotFoundException("Persona no encontrada con identifiacion: " + identifier));
    }

    public Page<Persona> listarPersonas(Pageable pageable) {
        logger.info("Listando personas con paginación: {}", pageable);
        return personaRepository.findAll(pageable);
    }

    public Persona actualizarPersona(Long id, Persona personaActualizada) {
        logger.info("Actualizando persona con ID: {}", id);

        Persona persona = obtenerPersona(id);

        if (personaActualizada.getNombre() != null) {
            persona.setNombre(personaActualizada.getNombre());
        }
        if (personaActualizada.getApellidoPaterno() != null) {
            persona.setApellidoPaterno(personaActualizada.getApellidoPaterno());
        }
        if (personaActualizada.getApellidoMaterno() != null) {
            persona.setApellidoMaterno(personaActualizada.getApellidoMaterno());
        }
        if (personaActualizada.getIdentificacion() != null) {
            persona.setIdentificacion(personaActualizada.getIdentificacion());
        }

        Persona updated = personaRepository.save(persona);
        logger.info("Persona actualizada exitosamente");
        return updated;
    }

    public void eliminarPersona(Long id) {
        logger.info("Eliminando persona con ID: {}", id);

        Persona persona = obtenerPersona(id);

        // Eliminar en cascada
        facturaRepository.findByPersonaId(id, org.springframework.data.domain.Pageable.unpaged())
                .forEach(factura -> facturaRepository.delete(factura));

        // Eliminar la persona
        personaRepository.delete(persona);
        logger.info("Persona y sus facturas eliminadas exitosamente");
    }
}
