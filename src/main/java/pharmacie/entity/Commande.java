package pharmacie.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;


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
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "commande", orphanRemoval=true)
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