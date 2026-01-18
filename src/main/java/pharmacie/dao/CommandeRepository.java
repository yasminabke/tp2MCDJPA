package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;

public interface CommandeRepository extends JpaRepository<Commande, Integer>{
    List<Commande> findByDispensaire_Nom(String nom);
	List<Commande> findByDispensaire(Dispensaire dispensaire);

}
