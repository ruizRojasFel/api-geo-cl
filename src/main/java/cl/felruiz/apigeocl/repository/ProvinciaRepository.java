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