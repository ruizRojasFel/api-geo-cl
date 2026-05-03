package cl.felruiz.apigeocl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.felruiz.apigeocl.model.Region;

/**
 * Repository para la entidad Region.
 *
 * JpaRepository<Region, Long> nos da GRATIS: findAll() → SELECT * FROM region
 * findById(id) → SELECT * FROM region WHERE id = ? existsById(id) → SELECT 1
 * FROM region WHERE id = ? count() → SELECT COUNT(*) FROM region
 *
 * findByNombreNormalizado → Búsqueda robusta: usa translate() de PostgreSQL
 * para ignorar tildes y LOWER() para ignorar mayúsculas. El Service normaliza
 * el input con TextUtils antes de llamar.
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * Búsqueda robusta por nombre.
     *
     * translate() reemplaza caracteres con tilde por su equivalente sin tilde
     * directamente en PostgreSQL.
     *
     * Ejemplo: buscar "nuble" encuentra "Ñuble" Input normalizado por Service:
     * "nuble" BD normalizado por translate(): "nuble" → MATCH ✅
     */
    @Query("""
      SELECT r FROM Region r
      WHERE LOWER(FUNCTION('translate', r.nombre,
          'áéíóúàèìòùâêîôûäëïöüñÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÄËÏÖÜÑ',
          'aeiouaeiouaeiouaeiounAEIOUAEIOUAEIOUAEIOUN'))
      LIKE CONCAT('%', :nombre, '%')
      """)
    List<Region> findByNombreNormalizado(@Param("nombre") String nombre);

    @Query("""
      SELECT r FROM Region r
      WHERE LOWER(FUNCTION('translate', r.nombre,
          'áéíóúàèìòùâêîôûäëïöüñÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÄËÏÖÜÑ',
          'aeiouaeiouaeiouaeiounAEIOUAEIOUAEIOUAEIOUN'))
      = :nombre
      """)
    Optional<Region> findByNombreNormalizadoExact(@Param("nombre") String nombre);
}
