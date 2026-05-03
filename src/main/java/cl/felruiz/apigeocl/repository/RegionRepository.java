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
 */

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

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
