package cl.felruiz.apigeocl.repository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import cl.felruiz.apigeocl.model.Comuna;
import cl.felruiz.apigeocl.model.Provincia;
import cl.felruiz.apigeocl.model.Region;

/**
 * Repository Tests para ComunaRepository.
 */

@DataJpaTest
@ActiveProfiles("test")
class ComunaRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ComunaRepository comunaRepository;

  private Region region;
  private Provincia provincia;

  /**
   * @BeforeEach → se ejecuta antes de cada test.
   *             Inserta datos de prueba en H2.
   */

  @BeforeEach
  void setUp() {
    region = Region.builder()
        .numero("VIII")
        .nombre("Biobío")
        .capital("Concepción")
        .build();
    entityManager.persist(region);

    provincia = Provincia.builder()
        .nombre("Concepción")
        .capital("Concepción")
        .region(region)
        .build();
    entityManager.persist(provincia);

    entityManager.persist(Comuna.builder()
        .nombre("Concepción").codigoCut("08101").provincia(provincia).build());
    entityManager.persist(Comuna.builder()
        .nombre("Talcahuano").codigoCut("08110").provincia(provincia).build());
    entityManager.persist(Comuna.builder()
        .nombre("Chiguayante").codigoCut("08103").provincia(provincia).build());
    entityManager.persist(Comuna.builder()
        .nombre("San Pedro de la Paz").codigoCut("08108").provincia(provincia).build());

    entityManager.flush();
  }

  @Test
  @DisplayName("findAll debe retornar todas las comunas con provincia y región")
  void findAll_retornaTodasConRelaciones() {
    List<Comuna> comunas = comunaRepository.findAll();

    assertThat(comunas).hasSize(4);
    // Verificar que las relaciones están cargadas (no lazy)
    assertThat(comunas.get(0).getProvincia()).isNotNull();
    assertThat(comunas.get(0).getProvincia().getRegion()).isNotNull();
  }

  @Test
  @DisplayName("findById debe retornar comuna con provincia y región")
  void findById_conIdValido_retornaComunaConRelaciones() {
    Comuna comuna = entityManager.persist(Comuna.builder()
        .nombre("Hualpén").codigoCut("08112").provincia(provincia).build());
    entityManager.flush();

    Optional<Comuna> resultado = comunaRepository.findById(comuna.getId());

    assertThat(resultado).isPresent();
    assertThat(resultado.get().getNombre()).isEqualTo("Hualpén");
    assertThat(resultado.get().getProvincia().getNombre()).isEqualTo("Concepción");
    assertThat(resultado.get().getProvincia().getRegion().getNombre()).isEqualTo("Biobío");
  }

  @Test
  @DisplayName("findByProvinciaId debe retornar comunas de la provincia")
  void findByProvinciaId_retornaComunasDeProvincia() {
    List<Comuna> comunas = comunaRepository.findByProvinciaId(provincia.getId());

    assertThat(comunas).hasSize(4);
    assertThat(comunas).allMatch(c -> c.getProvincia().getId().equals(provincia.getId()));
  }

  @Test
  @DisplayName("findByProvinciaRegionId debe retornar comunas de la región")
  void findByProvinciaRegionId_retornaComunasDeRegion() {
    List<Comuna> comunas = comunaRepository.findByProvinciaRegionId(region.getId());

    assertThat(comunas).hasSize(4);
    assertThat(comunas).allMatch(c -> c.getProvincia().getRegion().getId().equals(region.getId()));
  }

  @Test
  @DisplayName("findByNombreNormalizado debe encontrar sin tildes")
  void findByNombreNormalizado_sinTilde_encuentraResultado() {
    List<Comuna> comunas = comunaRepository.findByNombreNormalizado("concepcion");

    assertThat(comunas).hasSize(1);
    assertThat(comunas.get(0).getNombre()).isEqualTo("Concepción");
  }

  @Test
  @DisplayName("findByNombreNormalizado debe encontrar búsqueda parcial")
  void findByNombreNormalizado_parcial_encuentraResultado() {
    List<Comuna> comunas = comunaRepository.findByNombreNormalizado("san pedro");

    assertThat(comunas).hasSize(1);
    assertThat(comunas.get(0).getNombre()).isEqualTo("San Pedro de la Paz");
  }

  @Test
  @DisplayName("findByNombreNormalizado sin resultados debe retornar lista vacía")
  void findByNombreNormalizado_sinResultados_retornaVacio() {
    List<Comuna> comunas = comunaRepository.findByNombreNormalizado("inexistente");

    assertThat(comunas).isEmpty();
  }

  @Test
  @DisplayName("findById con ID inexistente debe retornar vacío")
  void findById_conIdInexistente_retornaVacio() {
    Optional<Comuna> resultado = comunaRepository.findById(999L);

    assertThat(resultado).isEmpty();
  }
}