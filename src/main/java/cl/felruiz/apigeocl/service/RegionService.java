package cl.felruiz.apigeocl.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.felruiz.apigeocl.dto.ComunaDTO;
import cl.felruiz.apigeocl.dto.ProvinciaDTO;
import cl.felruiz.apigeocl.dto.RegionDTO;
import cl.felruiz.apigeocl.exception.ResourceNotFoundException;
import cl.felruiz.apigeocl.mapper.ComunaMapper;
import cl.felruiz.apigeocl.mapper.ProvinciaMapper;
import cl.felruiz.apigeocl.mapper.RegionMapper;
import cl.felruiz.apigeocl.model.Region;
import cl.felruiz.apigeocl.repository.ComunaRepository;
import cl.felruiz.apigeocl.repository.ProvinciaRepository;
import cl.felruiz.apigeocl.repository.RegionRepository;
import cl.felruiz.apigeocl.util.TextUtils;
import lombok.RequiredArgsConstructor;

/**
 * Service con la lógica de negocio para Regiones.
 *
 * La búsqueda por nombre usa doble normalización: 1. TextUtils.normalizar() →
 * normaliza el input del usuario (Java) 2. translate() + LOWER() → normaliza
 * los datos en la BD (PostgreSQL)
 *
 * Esto permite búsquedas robustas que ignoran: - Tildes: "nuble" encuentra
 * "Ñuble" - Mayúsculas: "BIOBIO" encuentra "Biobío" - Espacios: " los rios "
 * encuentra "Los Ríos"
 */

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegionService {

  private final RegionRepository regionRepository;
  private final ProvinciaRepository provinciaRepository;
  private final RegionMapper regionMapper;
  private final ProvinciaMapper provinciaMapper;
  private final ComunaMapper comunaMapper;
  private final ComunaRepository comunaRepository;

  public List<RegionDTO> obtenerTodas() {
    return regionRepository.findAll()
        .stream()
        .map(regionMapper::toDTO)
        .toList();
  }

  public RegionDTO obtenerPorId(Long id) {
    return regionRepository.findById(id)
        .map(regionMapper::toDTO)
        .orElseThrow(() -> new ResourceNotFoundException("Región con id " + id + " no encontrada"));
  }

  /**
   * Busca regiones por nombre con normalización robusta.
   *
   * Flujo: 1. Usuario envía: " ÑUBLE " 2. TextUtils.normalizar() → "nuble" 3.
   * Repository busca con translate() en la BD 4. "Ñuble" en BD → translate →
   * "nuble" → MATCH ✅
   */

  public List<RegionDTO> buscarPorNombre(String nombre) {
    String normalizado = TextUtils.normalizar(nombre);
    if (normalizado.isEmpty()) {
      return List.of();
    }

    return regionRepository.findByNombreNormalizado(normalizado)
        .stream()
        .map(regionMapper::toDTO)
        .toList();
  }

  public List<ProvinciaDTO> obtenerProvinciasPorRegion(Long regionId) {
    if (!regionRepository.existsById(regionId)) {
      throw new ResourceNotFoundException("Región con id " + regionId + " no encontrada");
    }

    return provinciaRepository.findByRegionId(regionId)
        .stream()
        .map(provinciaMapper::toDTO)
        .toList();
  }

  public List<ComunaDTO> obtenerComunasPorNombreRegion(String nombre) {
    String normalizado = TextUtils.normalizar(nombre);

    Region region = regionRepository.findByNombreNormalizadoExact(normalizado)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Región con nombre '" + nombre + "' no encontrada"));

    return comunaRepository.findByProvinciaRegionId(region.getId()).stream()
        .map(comunaMapper::toDTO)
        .sorted(Comparator.comparing(ComunaDTO::getNombre))
        .toList();
  }
}
