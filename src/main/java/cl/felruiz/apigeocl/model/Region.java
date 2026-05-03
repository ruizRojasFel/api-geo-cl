package cl.felruiz.apigeocl.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Entidad que representa una Región de Chile. Mapea directamente a la tabla
 * "region" en PostgreSQL.
 */
@Entity
@Table(name = "region")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 5)
    private String numero;      // Ej: "XIII", "VIII", "XVI"

    @Column(nullable = false, length = 100)
    private String nombre;      // Ej: "Metropolitana de Santiago"

    @Column(nullable = false, length = 100)
    private String capital;     // Ej: "Santiago"

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Provincia> provincias;
}
