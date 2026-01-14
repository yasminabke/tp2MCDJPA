package pharmacie.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Ligne {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE) 
    private Integer id;

    @PositiveOrZero
    @NonNull
    @Column(nullable=false)
    private Integer quantite;

    @ManyToOne
    @NonNull
    @JoinColumn(name="commande_numero", nullable=false)
    @ToString.Exclude
    private Commande commande;

    @NonNull
    @ManyToOne
    @JoinColumn(name="medicament_reference", nullable=false)
    @ToString.Exclude
    private Medicament medicament;

    public BigDecimal getPrixTotal() {
        if (medicament == null || medicament.getPrixUnitaire() == null) {
            return BigDecimal.ZERO;
        }
        return medicament.getPrixUnitaire().multiply(BigDecimal.valueOf(quantite));
    }
}
