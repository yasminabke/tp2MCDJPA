package pharmacie.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pharmacie.entity.Categorie;
import pharmacie.entity.Commande;
import pharmacie.entity.Dispensaire;
import pharmacie.entity.Ligne;
import pharmacie.entity.Medicament;

@DataJpaTest
public class RepositoryCustomMethodsTest {

    @Autowired
    private CategorieRepository categorieRepository;
    @Autowired
    private MedicamentRepository medicamentRepository;
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private DispensaireRepository dispensaireRepository;
    @Autowired
    private LigneRepository ligneRepository;


    @Test // Ce test se base uniquement sur les données définies dans data.sql
    public void testMedicamentCustomMethods() {    
        Medicament indisponible = medicamentRepository.findByNom("Lévofloxacine 500mg").orElseThrow();
        Medicament disponible   = medicamentRepository.findByNom("Doliprane Effervescent 1g").orElseThrow();
    
        // Trouve tous les médicaments disponibles
        List<Medicament> disponibles = medicamentRepository.findByIndisponibleFalse();

        assertTrue(disponibles.contains(disponible));
        assertFalse(disponibles.contains(indisponible));        
        assertFalse(disponibles.isEmpty());
    }

    @Test // Ce test crée les enregistrements nécessaires
    public void testCategorieCustomMethods() {
        Categorie c1 = new Categorie();
        c1.setLibelle("AnalgesiquesTest");
        categorieRepository.save(c1);

        Categorie c2 = new Categorie();
        c2.setLibelle("AntibiotiquesTest");
        categorieRepository.save(c2);

        // findByLibelle
        Categorie found = categorieRepository.findByLibelle("AnalgesiquesTest");
        assertNotNull(found);
        assertEquals("AnalgesiquesTest", found.getLibelle());

        // findByLibelleContaining
        List<Categorie> list = categorieRepository.findByLibelleContaining("iquesTest");
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AntibiotiquesTest")));
        assertTrue(list.stream().anyMatch(cat -> cat.getLibelle().equals("AnalgesiquesTest")));
    }

    @Test
    @DisplayName("Trouver un dispensaire par nom - données du data.sql")
    public void testFindDispensaireByNom() {
        Dispensaire dispensaire = dispensaireRepository.findByNom("Pharmacie Centrale Paris");
        assertNotNull(dispensaire);
        assertEquals("PAR01", dispensaire.getCode());
        assertEquals("Pharmacie Centrale Paris", dispensaire.getNom());
    }

    @Test
    @DisplayName("Rechercher dispensaires par nom (substring) - données du data.sql")
    public void testFindDispensaireByNomContaining() {
        List<Dispensaire> dispensaires = dispensaireRepository.findByNomContainingIgnoreCase("Pharmacie");
        assertFalse(dispensaires.isEmpty());
        assertEquals(10, dispensaires.size());
        assertTrue(dispensaires.stream().allMatch(d -> d.getNom().toLowerCase().contains("pharmacie")));
    }

    @Test
    @DisplayName("Trouver les dispensaires par ville - Paris")
    public void testFindDispensaireByVilleParis() {
        List<Dispensaire> dispensairesParisiens = dispensaireRepository.findByAdressePostale_VilleIgnoreCase("Paris");
        assertFalse(dispensairesParisiens.isEmpty());
        assertEquals(2, dispensairesParisiens.size());
        assertTrue(dispensairesParisiens.stream()
            .allMatch(d -> d.getAdressePostale().getVille().equalsIgnoreCase("Paris")));
    }

    @Test
    @DisplayName("Trouver les dispensaires par ville - Marseille")
    public void testFindDispensaireByVilleMarseille() {
        List<Dispensaire> dispensaires = dispensaireRepository.findByAdressePostale_VilleIgnoreCase("Marseille");
        assertFalse(dispensaires.isEmpty());
        assertEquals(1, dispensaires.size());
    }


    @Test
    @DisplayName("Trouver les commandes par nom de dispensaire")
    public void testFindCommandeByDispensaireNom() {
        List<Commande> commandes = commandeRepository.findByDispensaire_Nom("Pharmacie Centrale Paris");
        
        assertFalse(commandes.isEmpty());
        assertEquals(2, commandes.size());
        assertTrue(commandes.stream()
            .allMatch(c -> c.getDispensaire().getNom().equals("Pharmacie Centrale Paris")));
    }

    @Test
    @DisplayName("Trouver les commandes d'un dispensaire")
    public void testFindCommandeByDispensaire() {
        Dispensaire dispensaire = dispensaireRepository.findByNom("Pharmacie du Marais");
        assertNotNull(dispensaire);
        
        List<Commande> commandes = commandeRepository.findByDispensaire(dispensaire);
        assertEquals(1, commandes.size());
        assertTrue(commandes.stream()
            .allMatch(c -> c.getDispensaire().equals(dispensaire)));
    }

    @Test
    @DisplayName("Trouver les commandes entre deux dates")
    public void testFindCommandeBetweenDates() {
        LocalDate debut = LocalDate.of(2025, 1, 4);
        LocalDate fin = LocalDate.of(2025, 1, 10);
        List<Commande> commandes = commandeRepository.findBySaisieLeBetween(debut, fin);
        
        assertFalse(commandes.isEmpty());
        assertTrue(commandes.size() >= 7);
        assertTrue(commandes.stream()
            .allMatch(c -> !c.getSaisieLe().isBefore(debut) && !c.getSaisieLe().isAfter(fin)));
    }

    @Test
    @DisplayName("Trouver les commandes du 8 janvier 2025")
    public void testFindCommandeByDate8janvier() {
        LocalDate date = LocalDate.of(2025, 1, 8);
        List<Commande> commandes = commandeRepository.findBySaisieLe(date);
        
        assertFalse(commandes.isEmpty());
        assertEquals(1, commandes.size());
        assertTrue(commandes.stream()
            .allMatch(c -> c.getSaisieLe().equals(date)));
    }

    @Test
    @DisplayName("Trouver les commandes du 9 janvier 2025")
    public void testFindCommandeByDate9janvier() {
        LocalDate date = LocalDate.of(2025, 1, 9);
        List<Commande> commandes = commandeRepository.findBySaisieLe(date);
        
        assertEquals(2, commandes.size());
        assertTrue(commandes.stream()
            .allMatch(c -> c.getSaisieLe().equals(date)));
    }

    @Test
    @DisplayName("Vérifier qu'il y a 12 commandes au total")
    public void testCommandesCount() {
        List<Commande> allCommandes = commandeRepository.findAll();
        assertEquals(12, allCommandes.size());
    }

    @Test
    @DisplayName("Vérifier les commandes livrées (6) et non livrées (6)")
    public void testCommandesDeliveredVsUndelivered() {
        List<Commande> allCommandes = commandeRepository.findAll();
        long livrees = allCommandes.stream().filter(c -> c.getEnvoyeeLe() != null).count();
        long nonLivrees = allCommandes.stream().filter(c -> c.getEnvoyeeLe() == null).count();
        
        assertEquals(6, livrees);
        assertEquals(6, nonLivrees);
    }


    @Test
    @DisplayName("Trouver les lignes de la commande 1")
    public void testFindLigneByCommande1() {
        Commande commande = commandeRepository.findById(1).orElseThrow();
        List<Ligne> lignes = ligneRepository.findByCommande(commande);
        
        assertFalse(lignes.isEmpty());
        assertEquals(2, lignes.size());
        assertTrue(lignes.stream().allMatch(l -> l.getCommande().equals(commande)));
    }

    @Test
    @DisplayName("Trouver les lignes pour le médicament Morphine (référence 1)")
    public void testFindLigneByMedicament() {
        Medicament medicament = medicamentRepository.findById(1).orElseThrow();
        List<Ligne> lignes = ligneRepository.findByMedicament(medicament);
        
        assertFalse(lignes.isEmpty());
        assertTrue(lignes.stream().allMatch(l -> l.getMedicament().equals(medicament)));
    }

    @Test
    @DisplayName("Compter les lignes de la commande 1")
    public void testCountLigneByCommande1() {
        Commande commande = commandeRepository.findById(1).orElseThrow();
        long count = ligneRepository.countByCommande(commande);
        
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Compter les lignes du médicament 2 (Doliprane)")
    public void testCountLigneByMedicament2() {
        Medicament medicament = medicamentRepository.findById(2).orElseThrow();
        long count = ligneRepository.countByMedicament(medicament);
        
        assertEquals(3, count);
    }

    @Test
    @DisplayName("Vérifier qu'il y a 22 lignes au total")
    public void testLignesCount() {
        List<Ligne> allLignes = ligneRepository.findAll();
        assertEquals(22, allLignes.size());
    }


    @Test
    @DisplayName("Calculer le montant total d'une ligne (Morphine 50 unités)")
    public void testPrixTotalLigne() {
        Medicament medicament = medicamentRepository.findById(1).orElseThrow(); // Morphine 25.80
        Commande commande = commandeRepository.findById(1).orElseThrow();
        
        Ligne ligne = new Ligne();
        ligne.setCommande(commande);
        ligne.setMedicament(medicament);
        ligne.setQuantite(50);

        BigDecimal prixTotal = ligne.getPrixTotal();
        assertNotNull(prixTotal);
        
        BigDecimal expected = BigDecimal.valueOf(25.80).multiply(BigDecimal.valueOf(50));
        assertEquals(0, prixTotal.compareTo(expected));
    }


    @Test
    @DisplayName("Vérifier la cohérence des dispensaires et commandes")
    public void testDispensaireCommandeConsistency() {
        List<Dispensaire> allDispensaires = dispensaireRepository.findAll();
        List<Commande> allCommandes = commandeRepository.findAll();

        assertEquals(10, allDispensaires.size());
        assertEquals(12, allCommandes.size());
        
        assertTrue(allCommandes.stream()
            .allMatch(c -> allDispensaires.contains(c.getDispensaire())));
    }

    @Test
    @DisplayName("Vérifier les commandes entre deux dates janvier 2025")
    public void testCommandesBetweenDatesJanvier() {
        LocalDate debut = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        
        List<Commande> commandes = commandeRepository.findBySaisieLeBetween(debut, fin);
        assertEquals(12, commandes.size());
        
        assertTrue(commandes.stream()
            .allMatch(c -> !c.getSaisieLe().isBefore(debut) && !c.getSaisieLe().isAfter(fin)));
    }

    @Test
    @DisplayName("Vérifier les commandes du dispensaire Paris avec 2 méthodes")
    public void testCommandesParisConsistency() {
        List<Commande> commandesParis1 = commandeRepository.findByDispensaire_Nom("Pharmacie Centrale Paris");
        
        Dispensaire dispensaire = dispensaireRepository.findByNom("Pharmacie Centrale Paris");
        List<Commande> commandesParis2 = commandeRepository.findByDispensaire(dispensaire);
        
        assertEquals(commandesParis1.size(), commandesParis2.size());
        assertEquals(2, commandesParis1.size());
    }

    @Test
    @DisplayName("Vérifier qu'il y a 10 catégories")
    public void testCategoriesCount() {
        List<Categorie> allCategories = categorieRepository.findAll();
        assertEquals(10, allCategories.size());
    }

    @Test
    @DisplayName("Vérifier qu'il y a 10 médicaments")
    public void testMedicamentsCount() {
        List<Medicament> allMedicaments = medicamentRepository.findAll();
        assertEquals(10, allMedicaments.size());
    }

    @Test
    @DisplayName("Vérifier qu'il y a 2 médicaments indisponibles")
    public void testIndisponibleMedicaments() {
        List<Medicament> indisponibles = medicamentRepository.findAll().stream()
            .filter(Medicament::isIndisponible)
            .toList();
        
        assertEquals(2, indisponibles.size());
    }

    @Test
    @DisplayName("Vérifier qu'il y a 10 dispensaires")
    public void testDispensairesCount() {
        List<Dispensaire> allDispensaires = dispensaireRepository.findAll();
        assertEquals(10, allDispensaires.size());
    }
    
}
