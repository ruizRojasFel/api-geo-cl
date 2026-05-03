package cl.felruiz.apigeocl.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una Comuna de Chile.
 * Mapea directamente a la tabla "comuna" en PostgreSQL.
 */
@Entity
@Table(name = "comuna")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comuna {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String nombre;

  /**
   * Código Único Territorial del INE.
   * Ejemplo: "13101" Santiago, "08101" Concepción.
   * unique = true → no pueden existir dos comunas con el mismo código
   */
  @Column(name = "codigo_cut", nullable = false, unique = true, length = 10)
  private String codigoCut;

  /**
   * Relación con Provincia.
   * @ManyToOne → muchas comunas pertenecen a una provincia
   * @JoinColumn → nombre de la FK en la tabla "comuna" → "provincia_id"
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "provincia_id", nullable = false)
  private Provincia provincia;
}