package com.example.prueba.tecnica.GetechnologiesMx.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.prueba.tecnica.GetechnologiesMx.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    @Query("SELECT f FROM Factura f WHERE f.persona.id = :personaId")
    Page<Factura> findByPersonaId(@Param("personaId") Long personaId, Pageable pageable);

}