package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;
import pharmacie.entity.Ligne;
import pharmacie.entity.Medicament;

public interface LigneRepository extends JpaRepository<Ligne, Integer> {
    List<Ligne> findByCommande(Commande commande);
    List<Ligne> findByMedicament(Medicament medicament);

    long countByCommande(Commande commande);
    long countByMedicament(Medicament medicament);
    void deleteByCommande(Commande commande);
}
