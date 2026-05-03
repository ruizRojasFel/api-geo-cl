package cl.felruiz.apigeocl.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.service.ComunaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para el recurso Comuna.
 */

@RestController
@RequestMapping("/api/v1/comunas")
@RequiredArgsConstructor
@Tag(name = "Comunas", description = "Endpoints para consultar comunas de Chile")
public class ComunaController {

  private final ComunaService comunaService;

  @GetMapping
  @Operation(summary = "Listar todas las comunas")
  public ResponseEntity<List<ComunaDTO>> obtenerTodas() {
    return ResponseEntity.ok(comunaService.obtenerTodas());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Obtener una comuna por ID")
  public ResponseEntity<ComunaDTO> obtenerPorId(@PathVariable Long id) {
    return ResponseEntity.ok(comunaService.obtenerPorId(id));
  }

  @GetMapping("/buscar")
  @Operation(summary = "Buscar comunas por nombre")
  public ResponseEntity<List<ComunaDTO>> buscarPorNombre(
      @RequestParam String nombre) {
    return ResponseEntity.ok(comunaService.buscarPorNombre(nombre));
  }
}