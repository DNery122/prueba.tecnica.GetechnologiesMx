package com.example.prueba.tecnica.GetechnologiesMx;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.example.prueba.tecnica.GetechnologiesMx.exception.DuplicateIdentifierException;
import com.example.prueba.tecnica.GetechnologiesMx.exception.PersonaNotFoundException;
import com.example.prueba.tecnica.GetechnologiesMx.model.Persona;
import com.example.prueba.tecnica.GetechnologiesMx.repository.PersonaRepository;
import com.example.prueba.tecnica.GetechnologiesMx.service.DirectorioService;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ApplicationTests {

	@Autowired
	private DirectorioService directorioService;

	@Autowired
	PersonaRepository personaRepository;

	private Persona personaTest;

	@BeforeEach
	void setUp() {
		personaRepository.deleteAll();

		personaTest = new Persona();
		personaTest.setNombre("Daniel");
		personaTest.setApellidoPaterno("Neri");
		personaTest.setApellidoMaterno("Herrera");
		personaTest.setIdentificacion("999999999");
	}

	@Test
	void testCrearPersonaExitosamente() {
		Persona created = directorioService.crearPersona(personaTest);

		assertNotNull(created.getId());
		assertEquals("Daniel", created.getNombre());
		assertEquals("Neri", created.getApellidoPaterno());
		assertEquals("Herrera", created.getApellidoMaterno());
		assertEquals("999999999", created.getIdentificacion());

		Optional<Persona> found = personaRepository.findById(created.getId());
		assertTrue(found.isPresent());
		assertEquals("Daniel", found.get().getNombre());
	}

	@Test
	void testCrearPersonaConIdentificacionDuplicada() {
		directorioService.crearPersona(personaTest);

		Persona personaDuplicada = new Persona();
		personaDuplicada.setNombre("Luis");
		personaDuplicada.setApellidoPaterno("Jimenez");
		personaDuplicada.setApellidoMaterno("Flores");
		personaDuplicada.setIdentificacion("999999999");

		assertThrows(DuplicateIdentifierException.class, () -> {
			directorioService.crearPersona(personaDuplicada);
		});
	}

	@Test
	void testObtenerPersonaNoExistente() {
		assertThrows(PersonaNotFoundException.class, () -> {
			directorioService.obtenerPersona(999L);
		});
	}

	@Test
	void testEliminarPersona(){
		Persona created = directorioService.crearPersona(personaTest);
		Long id = created.getId();

		directorioService.eliminarPersona(id);

		assertThrows(PersonaNotFoundException.class, () ->{
			directorioService.obtenerPersona(id);
		});

		Optional<Persona> found = personaRepository.findById(id);
		assertFalse(found.isPresent());
	}

}
