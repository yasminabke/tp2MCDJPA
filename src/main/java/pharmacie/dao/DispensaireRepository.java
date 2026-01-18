package pharmacie.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pharmacie.entity.Dispensaire;

public interface DispensaireRepository extends JpaRepository<Dispensaire, String>{
    Dispensaire findByNom(String nom);

    List<Dispensaire> findByNomContainingIgnoreCase(String substring);

    List<Dispensaire> findByAdressePostale_VilleIgnoreCase(String ville);
}
