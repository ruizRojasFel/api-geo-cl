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