package pharmacie.dao;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pharmacie.entity.Medicament;

// Cette interface sera auto-implémentée par Spring
public interface MedicamentRepository extends JpaRepository<Medicament, Integer> {
    /**
     * Trouve un médicament à partir de son nom (unique dans Medicament)
     * @return un médicament "optionnel"
     */
    Optional<Medicament>findByNom(String nom);

    /**
     * Trouve les médicaments disponibles (indisponible = false)
     * @return la liste des médicaments disponibles
     */
    List<Medicament> findByIndisponibleFalse();

    /**
    * Calcule le nombre d'unités commandées pour chaque produit d'une catégorie
    * @param codeCategorie la catégorie à traiter
    * @return le nombre d'unités commandées pour chaque produit, 
    *.        sous la forme d'une liste de projections UnitesParProduit
    */
    @Query("SELECT ligne.medicament.nom as nom, SUM(ligne.quantite) AS unites "
    + "FROM Ligne ligne "
    + "WHERE ligne.medicament.categorie.code = :codeCategorie "
    + "GROUP BY nom ")
    public List<UnitesParMedicament> medicamentsVendusPour(Integer codeCategorie);

    /**
     * Trouve tous les médicaments disponibles à la commande pour une catégorie.
     * 
     * Critères de disponibilité :
     * - Le médicament n'est pas indisponible (indisponible = false)
     * - La quantité en stock >= quantité en commande (unitesEnStock >= unitesCommandees)
     * 
     * @param codeCategorie le code de la catégorie
     * @return liste des médicaments disponibles à la commande
     */
    @Query("SELECT m FROM Medicament m " +
           "WHERE m.categorie.code = :codeCategorie " +
           "AND m.indisponible = false " +
           "AND m.unitesEnStock >= m.unitesCommandees")
    List<Medicament> findMedicamentsDisponiblesParCategorie(@Param("codeCategorie") Integer codeCategorie);

}
