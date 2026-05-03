package cl.felruiz.apigeocl.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.felruiz.apigeocl.model.Provincia;

/**
 * Repository para la entidad Provincia.
 *
 * findAll
 *   → SELECT * FROM provincia
 *
 * findById
 *   → SELECT * FROM provincia WHERE id = ?
 *
 * findByRegionId
 *   → SELECT * FROM provincia WHERE region_id = ?
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
 *   SELECT * FROM provincia                 → 1 query
 *   SELECT * FROM region WHERE id = ?       → 56 queries (una por provincia)
 *   Total: 57 queries ❌
 *
 * Con @EntityGraph / JOIN FETCH (single JOIN query):
 *   SELECT DISTINCT p.*, r.*
 *   FROM provincia p
 *   JOIN region r ON p.region_id = r.id
 *   Total: 1 query ✅
 */
@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Long> {

  @EntityGraph(attributePaths = {"region"})
  @Override
  List<Provincia> findAll();

  @EntityGraph(attributePaths = {"region"})
  @Override
  Optional<Provincia> findById(Long id);

  @Query("SELECT p FROM Provincia p JOIN FETCH p.region r WHERE r.id = :regionId")
  List<Provincia> findByRegionId(@Param("regionId") Long regionId);

  /**
   * Búsqueda robusta por nombre.
   *
   * Ejemplo: buscar "concepcion" encuentra provincia "Concepción"
   *   Input normalizado por Service:  "concepcion"
   *   BD normalizado por translate(): "concepcion"
   *   → MATCH ✅
   */
  @Query("""
      SELECT p FROM Provincia p
      JOIN FETCH p.region
      WHERE LOWER(FUNCTION('translate', p.nombre,
          'áéíóúàèìòùâêîôûäëïöüñÁÉÍÓÚÀÈÌÒÙÂÊÎÔÛÄËÏÖÜÑ',
          'aeiouaeiouaeiouaeiounAEIOUAEIOUAEIOUAEIOUN'))
      LIKE CONCAT('%', :nombre, '%')
      """)
  List<Provincia> findByNombreNormalizado(@Param("nombre") String nombre);
}