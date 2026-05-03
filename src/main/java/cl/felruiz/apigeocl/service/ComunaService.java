package cl.felruiz.apigeocl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.mapper.ComunaMapper;
import cl.felruiz.apigeocl.repository.ComunaRepository;
import cl.felruiz.apigeocl.util.TextUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service con la lógica de negocio para Comunas.
 *
 * La búsqueda por nombre usa doble normalización:
 * 1. TextUtils.normalizar() → normaliza el input del usuario (Java)
 * 2. translate() + LOWER() → normaliza los datos en la BD (PostgreSQL)
 *
 * Esto permite búsquedas robustas que ignoran:
 *   - Tildes:      "concepcion" encuentra "Concepción"
 *   - Mayúsculas:  "SANTIAGO" encuentra "Santiago"
 *   - Espacios:    "  san pedro  " encuentra "San Pedro de la Paz"
 *   - Ñ:           "nunoa" encuentra "Ñuñoa"
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ComunaService {

  private final ComunaRepository comunaRepository;
  private final ComunaMapper comunaMapper;

  public List<ComunaDTO> obtenerTodas() {
    return comunaRepository.findAll()
        .stream()
        .map(comunaMapper::toDTO)
        .toList();
  }

  public ComunaDTO obtenerPorId(Long id) {
    return comunaRepository.findById(id)
        .map(comunaMapper::toDTO)
        .orElseThrow(() ->
            new ResourceNotFoundException("Comuna con id " + id + " no encontrada")
        );
  }

  /**
   * Busca comunas por nombre con normalización robusta.
   *
   * Flujo:
   *   1. Usuario envía: "  CONCEPCIÓN  "
   *   2. TextUtils.normalizar() → "concepcion"
   *   3. Repository busca con translate() en la BD
   *   4. "Concepción" en BD → translate → "concepcion" → MATCH ✅
   */
  
  public List<ComunaDTO> buscarPorNombre(String nombre) {
    String normalizado = TextUtils.normalizar(nombre);
    if (normalizado.isEmpty()) return List.of();

    return comunaRepository.findByNombreNormalizado(normalizado)
        .stream()
        .map(comunaMapper::toDTO)
        .toList();
  }

  public List<ComunaDTO> obtenerComunasPorRegion(Long regionId) {
    return comunaRepository.findByProvinciaRegionId(regionId)
        .stream()
        .map(comunaMapper::toDTO)
        .toList();
  }
}