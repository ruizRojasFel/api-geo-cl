package cl.felruiz.apigeocl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidad que representa una Provincia de Chile.
 * Mapea directamente a la tabla "provincia" en PostgreSQL.
 */
@Entity
@Table(name = "provincia")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provincia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nombre;

  @Column(nullable = false, length = 100)
  private String capital;

  /**
   * Relación con Región.
   * @ManyToOne → muchas provincias pertenecen a una región
   * @JoinColumn → nombre de la FK en la tabla "provincia" → "region_id"
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "region_id", nullable = false)
  private Region region;

  /**
   * @OneToMany → una provincia tiene muchas comunas
   */
  @OneToMany(mappedBy = "provincia", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Comuna> comunas;
}