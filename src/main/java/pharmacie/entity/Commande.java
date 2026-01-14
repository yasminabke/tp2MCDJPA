package pharmacie.entity;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;


@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    @NonNull
    @NotNull(message="La date de saisie est obligatoire")
    @Column(nullable=false)
    private LocalDate saisieLe;

    @Column
    private LocalDate envoyeeLe;

    @NonNull
    @NotNull(message="Le port est obligatoire")
    @Column(nullable=false)
    @DecimalMin(value="0.0", message="Le port doit être positif ou nul")
    private BigDecimal port;

    @NonNull
    @NotNull(message="La remise est obligatoire")
    @Column(nullable=false)
    @DecimalMin(value="0.0", message="La remise doit être positive ou nulle")
    private BigDecimal remise;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "commande")
    private List<Ligne> lignes = new LinkedList<>();

    @NonNull
    @ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="dispensaire_code", nullable=false)
    @ToString.Exclude
    private Dispensaire dispensaire;

    public List<Ligne> getLignes() {
        if (lignes == null) {
            lignes = new LinkedList<>();
        }
        return lignes;
    }   
}