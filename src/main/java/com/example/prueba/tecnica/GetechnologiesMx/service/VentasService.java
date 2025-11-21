package com.example.prueba.tecnica.GetechnologiesMx.service;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prueba.tecnica.GetechnologiesMx.exception.FacturaNotFoundException;
import com.example.prueba.tecnica.GetechnologiesMx.exception.InvalidPersonaException;
import com.example.prueba.tecnica.GetechnologiesMx.model.Factura;
import com.example.prueba.tecnica.GetechnologiesMx.repository.FacturaRepository;

@Service
@Transactional
public class VentasService {
    private static final Logger logger = LoggerFactory.getLogger(VentasService.class);
    private final FacturaRepository facturaRepository;
    private final DirectorioService directorioService;

    public VentasService(FacturaRepository facturaRepository,
            DirectorioService directorioService) {
        this.facturaRepository = facturaRepository;
        this.directorioService = directorioService;
    }

    public Factura crearFactura(Factura factura) {
        logger.info("Creando nueva factura para persona ID: {}", factura);
        if (factura.getPersona() == null || factura.getPersona().getId() == null) {
            throw new InvalidPersonaException("Debe proporcionar una persona válida");
        }
        factura.setPersona(directorioService.obtenerPersona(factura.getPersona().getId()));
        Factura saved = facturaRepository.save(factura);
        logger.info("Factura creada exitosamente con ID: {}", saved.getId());
        return saved;
    }

    public Factura obtenerFactura(Long id) {
        logger.info("Buscando factura con ID: {}", id);
        return facturaRepository.findById(id)
                .orElseThrow(() -> new FacturaNotFoundException("Factura no econtrada con ID " + id));
    }
// return facturaRepository.findByPersonaId(personaId, pageable);
    public Page<Factura> listarFacturasPorPersona(Long personaId, Pageable pageable) {
        logger.info("Listando facturas para la persona on ID: {}", personaId);
        directorioService.obtenerPersona(personaId);
        return facturaRepository.findByPersonaId(personaId, pageable);
    }
}
