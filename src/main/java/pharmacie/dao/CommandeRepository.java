package pharmacie.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;

public interface CommandeRepository extends JpaRepository<Commande, Integer>{
    List<Commande> findByDispensaire_Nom(String nom);
	List<Commande> findByDispensaire(Dispensaire dispensaire);
    List<Commande> findBySaisieLeBetween(LocalDate startDate, LocalDate endDate);
    List<Commande> findBySaisieLe(LocalDate saisieLe);

}
