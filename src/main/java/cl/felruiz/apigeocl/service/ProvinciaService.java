package cl.felruiz.apigeocl.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.mapper.ComunaMapper;
import cl.felruiz.apigeocl.mapper.ProvinciaMapper;
import cl.felruiz.apigeocl.repository.ComunaRepository;
import cl.felruiz.apigeocl.repository.ProvinciaRepository;
import cl.felruiz.apigeocl.util.TextUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service con la lógica de negocio para Provincias.
 *
 * La búsqueda por nombre usa doble normalización:
 * 1. TextUtils.normalizar() → normaliza el input del usuario (Java)
 * 2. translate() + LOWER() → normaliza los datos en la BD (PostgreSQL)
 *
 * Esto permite búsquedas robustas que ignoran:
 *   - Tildes:      "concepcion" encuentra "Concepción"
 *   - Mayúsculas:  "SANTIAGO" encuentra "Santiago"
 *   - Espacios:    "  el loa  " encuentra "El Loa"
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvinciaService {

  private final ProvinciaRepository provinciaRepository;
  private final ComunaRepository comunaRepository;
  private final ProvinciaMapper provinciaMapper;
  private final ComunaMapper comunaMapper;

  public List<ProvinciaDTO> obtenerTodas() {
    return provinciaRepository.findAll()
        .stream()
        .map(provinciaMapper::toDTO)
        .toList();
  }

  public ProvinciaDTO obtenerPorId(Long id) {
    return provinciaRepository.findById(id)
        .map(provinciaMapper::toDTO)
        .orElseThrow(() ->
            new ResourceNotFoundException("Provincia con id " + id + " no encontrada")
        );
  }

  /**
   * Busca provincias por nombre con normalización robusta.
   *
   * Flujo:
   *   1. Usuario envía: "  CONCEPCIÓN  "
   *   2. TextUtils.normalizar() → "concepcion"
   *   3. Repository busca con translate() en la BD
   *   4. "Concepción" en BD → translate → "concepcion" → MATCH ✅
   */
  
  public List<ProvinciaDTO> buscarPorNombre(String nombre) {
    String normalizado = TextUtils.normalizar(nombre);
    if (normalizado.isEmpty()) return List.of();

    return provinciaRepository.findByNombreNormalizado(normalizado)
        .stream()
        .map(provinciaMapper::toDTO)
        .toList();
  }

  public List<ComunaDTO> obtenerComunasPorProvincia(Long provinciaId) {
    if (!provinciaRepository.existsById(provinciaId)) {
      throw new ResourceNotFoundException("Provincia con id " + provinciaId + " no encontrada");
    }

    return comunaRepository.findByProvinciaId(provinciaId)
        .stream()
        .map(comunaMapper::toDTO)
        .toList();
  }
}