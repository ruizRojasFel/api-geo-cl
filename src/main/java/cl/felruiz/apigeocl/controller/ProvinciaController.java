package cl.felruiz.apigeocl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.service.ProvinciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para el recurso Provincia.
 */

@RestController
@RequestMapping("/api/v1/provincias")
@RequiredArgsConstructor
@Tag(name = "Provincias", description = "Endpoints para consultar provincias de Chile")
public class ProvinciaController {

  private final ProvinciaService provinciaService;

  @GetMapping
  @Operation(summary = "Listar todas las provincias")
  public ResponseEntity<List<ProvinciaDTO>> obtenerTodas() {
    return ResponseEntity.ok(provinciaService.obtenerTodas());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener una provincia por ID")
  public ResponseEntity<ProvinciaDTO> obtenerPorId(@PathVariable Long id) {
    return ResponseEntity.ok(provinciaService.obtenerPorId(id));
  }

  @GetMapping("/buscar")
  @Operation(summary = "Buscar provincias por nombre")
  public ResponseEntity<List<ProvinciaDTO>> buscarPorNombre(
      @RequestParam String nombre) {
    return ResponseEntity.ok(provinciaService.buscarPorNombre(nombre));
  }

  @GetMapping("/{id}/comunas")
  @Operation(summary = "Listar comunas de una provincia")
  public ResponseEntity<List<ComunaDTO>> obtenerComunas(@PathVariable Long id) {
    return ResponseEntity.ok(provinciaService.obtenerComunasPorProvincia(id));
  }
}