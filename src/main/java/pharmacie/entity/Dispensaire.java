package pharmacie.entity;

import java.util.LinkedList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Dispensaire {
    @Id
    @Column(name = "code", length = 5)
    @NonNull
    @NotBlank(message = "Le code du dispensaire est obligatoire")
    @Size(min=1, max=5)
    private String code;

    @NonNull
    @Column(name = "nom", length = 40, nullable = false)
    @NotBlank(message = "Le nom du dispensaire est obligatoire")
    @Size(min=3, max=40)
    private String nom;

    @NonNull
    @Column(name = "contact", length = 30, nullable = false)
    @NotBlank(message = "Le contact est obligatoire")
    @Size(max=30)
    private String contact;

    @NonNull
    @Column(name = "fonction", length = 30, nullable = false)
    @NotBlank(message = "La fonction est obligatoire")
    @Size(max=30)
    private String fonction;

    @NonNull
    @Column(name = "telephone", length = 24, nullable = false)
    @NotBlank(message = "Le telephone est obligatoire")
    @Size(max=24)
    private String telephone;

    @NonNull
    @Column(name = "fax", length = 24, nullable = false)
    @NotBlank(message = "Le fax est obligatoire")
    @Size(max=24)
    private String fax;

    @Embedded
    private AdressePostale adressePostale;

    @ToString.Exclude
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "dispensaire", orphanRemoval=true)
    private List<Commande> commandes = new LinkedList<>();

}