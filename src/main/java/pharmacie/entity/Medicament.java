package pharmacie.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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
public class Medicament {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.NONE) // la clé est autogénérée par la BD, On ne veut pas de "setter"
	private Integer reference = null;

	@NonNull
	@Column(unique=true, length = 255)
	private String nom;

	private String quantiteParUnite = "Une boîte de 12";

	@PositiveOrZero
	private BigDecimal prixUnitaire = BigDecimal.TEN;

	/**
	 * Nombre d'unités en stock
	 * Décrémenté quand on expédie une commande contenant ce médicament
	 */
	@ToString.Exclude
	@PositiveOrZero
	private int unitesEnStock = 0;

	/**
	 * Nombre d'unités "en commande"
	 * Un médicament est "en commande" si il est dans une commande qui n'est pas encore expédiée
	 * Incrementé quand on ajoute des unités de ce médicament à une ligne de commande
	 * Décrémenté quand on expédie une commande contenant ce médicament
	 */
	@ToString.Exclude
	@PositiveOrZero
	private int unitesCommandees = 0;

	/**
	 * Niveau de reapprovisionnement
	 * Si le stock devient inférieur ou égal à ce niveau, 
	 * on doit approvisionner de nouvelles unités de ce médicament auprès d'un fournisseur
	 */
	@ToString.Exclude
	@PositiveOrZero
	private int niveauDeReappro = 0;

	/**
	 * Indique si le médicament est indisponible
	 */
	@ToString.Exclude
	private boolean indisponible = false;

	@Column(length = 500)
	private String imageURL;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@NonNull
	@ToString.Exclude
	@NotNull(message="La catégorie est obligatoire")
	@JoinColumn(name="categorie_code", nullable=false)
	private Categorie categorie ;

}
