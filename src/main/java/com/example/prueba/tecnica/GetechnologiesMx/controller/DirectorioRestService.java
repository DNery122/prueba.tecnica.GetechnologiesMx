package com.example.prueba.tecnica.GetechnologiesMx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.prueba.tecnica.GetechnologiesMx.model.Factura;
import com.example.prueba.tecnica.GetechnologiesMx.model.Persona;
import com.example.prueba.tecnica.GetechnologiesMx.service.DirectorioService;
import com.example.prueba.tecnica.GetechnologiesMx.service.VentasService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class DirectorioRestService {
    private static final Logger logger = LoggerFactory.getLogger(DirectorioRestService.class);
    private final DirectorioService directorioService;
    private final VentasService ventasService;

    public DirectorioRestService(DirectorioService directorioService, VentasService ventasService) {
        this.directorioService = directorioService;
        this.ventasService = ventasService;
    }

    // PERSONAS
    @PostMapping("/personas")
    public ResponseEntity<Persona> crearPersona(@RequestBody Persona persona) {
        logger.info("POST /persona - Creando persona");
        Persona created = directorioService.crearPersona(persona);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/personas/identificacion/{identifier}")
    public ResponseEntity<Persona> obtenerPersonaPorIdentificacion(@PathVariable String identifier) {
        logger.info("GET /personas/identificacion/{} - Obteniendo persona " + identifier);
        Persona persona = directorioService.obtenerPersonaPorIdentificacion(identifier);
        return ResponseEntity.ok(persona);
    }

    @GetMapping("/personas/{id}")
    public ResponseEntity<Persona> obtenerPersona(@PathVariable Long id) {
        logger.info("GET /personas/{} - Obteniendo persona " + id);
        Persona persona = directorioService.obtenerPersona(id);
        return ResponseEntity.ok(persona);
    }

    @GetMapping("/personas")
    public ResponseEntity<Page<Persona>> listarPersonas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /personas - Listar personas con paginacion: page={} size={}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Persona> personas = directorioService.listarPersonas(pageable);
        return ResponseEntity.ok(personas);
    }

    @PutMapping("/personas/{id}")
    public ResponseEntity<Persona> actualizarPersona(
            @PathVariable Long id,
            @RequestBody Persona persona) {
        logger.info("PUT /personas/{} - Actualizar persona", id);
        Persona updated = directorioService.actualizarPersona(id, persona);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/personas/{id}")
    public ResponseEntity<Void> eliminarPersona(@PathVariable Long id) {
        logger.info("DELETE /personas/{} - Eliminar persona", id);
        directorioService.eliminarPersona(id);
        return ResponseEntity.noContent().build();
    }

    // FACTURAS
    @PostMapping("/facturas")
    public ResponseEntity<Factura> crearFactura(@RequestBody Factura factura) {
        logger.info("POST /facturas - Crear nueva factura");
        Factura created = ventasService.crearFactura(factura);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/facturas/{id}")
    public ResponseEntity<Factura> obtenerFactura(@PathVariable Long id) {
        logger.info("GET /facturas/{} - Obtener factura", id);
        Factura factura = ventasService.obtenerFactura(id);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/personas/{personaID}/facturas")
    public ResponseEntity<Page<Factura>> listarFacturasPorPersona(
            @PathVariable Long personaID,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("GET /personas/{}/facturas -Listar facturas con paginacion", personaID);
        Pageable pageable = PageRequest.of(page, size);
        Page<Factura> facturas = ventasService.listarFacturasPorPersona(personaID, pageable);
        return ResponseEntity.ok(facturas);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Servicio en linea");
    }

}
