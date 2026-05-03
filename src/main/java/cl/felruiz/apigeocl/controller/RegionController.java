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
import cl.felruiz.apigeocl.dto.RegionDTO;
import cl.felruiz.apigeocl.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller REST para el recurso Región.
 */

@RestController
@RequestMapping("/api/v1/regiones")
@RequiredArgsConstructor
@Tag(name = "Regiones", description = "Endpoints para consultar regiones de Chile")
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    @Operation(summary = "Listar todas las regiones")
    public ResponseEntity<List<RegionDTO>> obtenerTodas() {
        return ResponseEntity.ok(regionService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una región por ID")
    public ResponseEntity<RegionDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.obtenerPorId(id));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar regiones por nombre")
    public ResponseEntity<List<RegionDTO>> buscarPorNombre(
            @RequestParam String nombre) {
        return ResponseEntity.ok(regionService.buscarPorNombre(nombre));
    }

    @GetMapping("/{id}/provincias")
    @Operation(summary = "Listar provincias de una región")
    public ResponseEntity<List<ProvinciaDTO>> obtenerProvincias(@PathVariable Long id) {
        return ResponseEntity.ok(regionService.obtenerProvinciasPorRegion(id));
    }

    @GetMapping("/comunas")
    @Operation(summary = "Listar comunas de una región por nombre")
    public ResponseEntity<List<ComunaDTO>> obtenerComunasPorNombreRegion(
            @RequestParam String nombre) {
        return ResponseEntity.ok(regionService.obtenerComunasPorNombreRegion(nombre));
    }
}
