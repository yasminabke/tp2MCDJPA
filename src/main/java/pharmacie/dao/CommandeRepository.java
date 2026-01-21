package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;

public interface CommandeRepository extends JpaRepository<Commande, Integer>{
    List<Commande> findByDispensaire_Nom(String nom);
	List<Commande> findByDispensaire(Dispensaire dispensaire);
    List<Commande> findBySaisieLe(LocalDate saisieLe);

    /**
     * Calcule le nombre d'articles (unités) déjà commandés par un dispensaire.
     * Critère: La commande doit avoir été envoyée (envoyeeLe NOT NULL)
     * 
     * @param dispensaireCode le code du dispensaire
     * @return le nombre total d'articles commandés et envoyés, null si aucune commande
     */
    @Query("SELECT SUM(ligne.quantite) FROM Ligne ligne " +
           "WHERE ligne.commande.dispensaire.code = :dispensaireCode " +
           "AND ligne.commande.envoyeeLe IS NOT NULL")
    Long countArticlesCommandesParDispensaire(@Param("dispensaireCode") String dispensaireCode);
    
    /**
     * Trouve toutes les commandes en cours pour un dispensaire.
     * En cours = la date d'envoi (envoyeeLe) n'est pas renseignée (IS NULL)
     * 
     * @param dispensaireCode le code du dispensaire
     * @return liste des commandes en cours, ordonnées par date de saisie (descendant)
     */
    @Query("SELECT c FROM Commande c " +
           "WHERE c.dispensaire.code = :dispensaireCode " +
           "AND c.envoyeeLe IS NULL " +
           "ORDER BY c.saisieLe DESC")
    List<Commande> findCommandesEnCoursByDispensaire(@Param("dispensaireCode") String dispensaireCode);
}
