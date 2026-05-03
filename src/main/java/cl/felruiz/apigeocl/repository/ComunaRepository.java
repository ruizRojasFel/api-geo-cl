package cl.felruiz.apigeocl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.felruiz.apigeocl.model.Comuna;

/**
 * Repository para la entidad Comuna.
 *
 * findAll
 *   → SELECT * FROM comuna
 *
 * findById
 *   → SELECT * FROM comuna WHERE id = ?
 *
 * findByProvinciaId
 *   → SELECT * FROM comuna WHERE provincia_id = ?
 *
 * findByProvinciaRegionId
 *   → SELECT * FROM comuna c
 *     JOIN provincia p ON c.provincia_id = p.id
 *     WHERE p.region_id = ?
 *
 * findByNombreNormalizado
 *   → Búsqueda robusta: usa translate() de PostgreSQL para
 *     ignorar tildes y LOWER() para ignorar mayúsculas.
 *     El Service normaliza el input con TextUtils antes de llamar.
 *
 * @EntityGraph → le dice a Hibernate que traiga las relaciones
 * en una sola query con JOIN, en lugar de N queries separadas.
 *
 * Sin @EntityGraph (problema N+1):
 *   SELECT * FROM comuna                    → 1 query
 *   SELECT * FROM provincia WHERE id = ?    → 346 queries (una por comuna)
 *   SELECT * FROM region WHERE id = ?       → 346 queries (una por provincia)
 *   Total: 693 queries ❌
 *
 * Con @EntityGraph / JOIN FETCH (single JOIN query):
 *   SELECT DISTINCT c.*, p.*, r.*
 *   FROM comuna c
 *   JOIN provincia p ON c.provincia_id = p.id
 *   JOIN region r ON p.region_id = r.id
 *   Total: 1 query ✅
 */
@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Long> {

  @EntityGraph(attributePaths = {"provincia", "provincia.region"})
  @Override
  List<Comuna> findAll();

  @EntityGraph(attributePaths = {"provincia", "provincia.region"})
  @Override
  Optional<Comuna> findById(Long id);

  @Query("SELECT c FROM Comuna c JOIN FETCH c.provincia p JOIN FETCH p.region WHERE p.id = :provinciaId")
  List<Comuna> findByProvinciaId(@Param("provinciaId") Long provinciaId);

  /**
   * Búsqueda robusta por nombre.
   *
   * Ejemplo: buscar "concepcion" encuentra "Concepción"
   *   Input normalizado por Service:  "concepcion"
   *   BD normalizado por translate(): "concepcion"
   *   → MATCH ✅
   */
  @Query("""
      SELECT c FROM Comuna c
      JOIN FETCH c.provincia p
      JOIN FETCH p.region
      WHERE LOWER(FUNCTION('translate', c.nombre,
          'áéíóúàèìòùâêîôûäëïöüñÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÄËÏÖÜÑ',
          'aeiouaeiouaeiouaeiounAEIOUAEIOUAEIOUAEIOUN'))
      LIKE CONCAT('%', :nombre, '%')
      """)
  List<Comuna> findByNombreNormalizado(@Param("nombre") String nombre);

  @Query("SELECT c FROM Comuna c JOIN FETCH c.provincia p JOIN FETCH p.region r WHERE r.id = :regionId")
  List<Comuna> findByProvinciaRegionId(@Param("regionId") Long regionId);
}