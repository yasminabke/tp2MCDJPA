package pharmacie.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
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

    private Dispensaire dispensaireParis;
    private Dispensaire dispensaireMarseille;
    private Medicament doliprane;
    private Medicament levofloxacine;

    @BeforeEach
    void setUp() {
        // Récupérer les données existantes de data.sql
        dispensaireParis = dispensaireRepository.findByNom("Pharmacie Centrale Paris");
        dispensaireMarseille = dispensaireRepository.findByNom("Pharmacie Marseille");
        doliprane = medicamentRepository.findByNom("Doliprane Effervescent 1g").orElse(null);
        levofloxacine = medicamentRepository.findByNom("Lévofloxacine 500mg").orElse(null);
    }



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
    @DisplayName("Trouver un dispensaire par nom")
    public void testFindDispensaireByNom() {
        assertNotNull(dispensaireParis);
        assertEquals("PAR01", dispensaireParis.getCode());
        assertEquals("Pharmacie Centrale Paris", dispensaireParis.getNom());
    }

    @Test
    @DisplayName("Rechercher dispensaires par nom (substring)")
    public void testFindDispensaireByNomContaining() {
        List<Dispensaire> dispensaires = dispensaireRepository.findByNomContainingIgnoreCase("pharmacie");
        assertFalse(dispensaires.isEmpty());
        assertEquals(10, dispensaires.size()); // 10 dispensaires exactement
        assertTrue(dispensaires.stream().allMatch(d -> d.getNom().toLowerCase().contains("pharmacie")));
    }

    @Test
    @DisplayName("Trouver les dispensaires par ville - Paris")
    public void testFindDispensaireByVilleParis() {
        List<Dispensaire> dispensairesParisiens = dispensaireRepository.findByAdressePostale_VilleIgnoreCase("Paris");
        assertFalse(dispensairesParisiens.isEmpty());
        assertEquals(2, dispensairesParisiens.size()); // PAR01, PAR02
        assertTrue(dispensairesParisiens.stream()
            .allMatch(d -> d.getAdressePostale().getVille().equalsIgnoreCase("Paris")));
    }

    @Test
    @DisplayName("Trouver les dispensaires par ville - Marseille")
    public void testFindDispensaireByVilleMarseille() {
        List<Dispensaire> dispensaires = dispensaireRepository.findByAdressePostale_VilleIgnoreCase("Marseille");
        assertFalse(dispensaires.isEmpty());
        assertEquals(1, dispensaires.size()); // MRS01 uniquement
        assertTrue(dispensaires.stream()
            .allMatch(d -> d.getAdressePostale().getVille().equalsIgnoreCase("Marseille")));
    }

    @Test
    @DisplayName("Vérifier les 10 dispensaires")
    public void testCount10Dispensaires() {
        List<Dispensaire> allDispensaires = dispensaireRepository.findAll();
        assertEquals(10, allDispensaires.size(), "Il doit y avoir exactement 10 dispensaires");
    }

    // ===== TESTS COMMANDE - MÉTHODES PERSONNALISÉES PRINCIPALES =====
    @Test
    @DisplayName("Trouver les commandes par nom de dispensaire - Pharmacie Centrale Paris")
    public void testFindCommandeByDispensaireNom() {
        List<Commande> commandes = commandeRepository.findByDispensaire_Nom("Pharmacie Centrale Paris");
        
        assertFalse(commandes.isEmpty(), "Il doit y avoir des commandes pour Pharmacie Centrale Paris");
        assertEquals(2, commandes.size(), "Pharmacie Centrale Paris doit avoir 2 commandes");
        assertTrue(commandes.stream()
            .allMatch(c -> c.getDispensaire().getNom().equals("Pharmacie Centrale Paris")));
    }

    @Test
    @DisplayName("Trouver les commandes d'un dispensaire - PAR02")
    public void testFindCommandeByDispensairePAR02() {
        Dispensaire dispensairePAR02 = dispensaireRepository.findByNom("Pharmacie du Marais");
        assertNotNull(dispensairePAR02);
        
        List<Commande> commandes = commandeRepository.findByDispensaire(dispensairePAR02);
        assertEquals(1, commandes.size(), "Pharmacie du Marais doit avoir 1 commande");
        assertTrue(commandes.stream()
            .allMatch(c -> c.getDispensaire().equals(dispensairePAR02)));
    }

    @Test
    @DisplayName("Trouver les commandes d'un dispensaire - Marseille")
    public void testFindCommandeByDispensaireMarseille() {
        assertNotNull(dispensaireMarseille);
        
        List<Commande> commandes = commandeRepository.findByDispensaire(dispensaireMarseille);
        assertEquals(2, commandes.size(), "Pharmacie Marseille doit avoir 2 commandes");
        assertTrue(commandes.stream()
            .allMatch(c -> c.getDispensaire().equals(dispensaireMarseille)));
    }

    @Test
    @DisplayName("Trouver les commandes entre deux dates - 4 et 10 janvier")
    public void testFindCommandeBetweenDates() {
        LocalDate debut = LocalDate.of(2025, 1, 4);
        LocalDate fin = LocalDate.of(2025, 1, 10);
        List<Commande> commandes = commandeRepository.findBySaisieLeBetween(debut, fin);
        
        assertFalse(commandes.isEmpty(), "Il doit y avoir des commandes entre le 4 et 10 janvier");
        assertTrue(commandes.size() >= 7, "Il doit y avoir au moins 7 commandes");
        assertTrue(commandes.stream()
            .allMatch(c -> !c.getSaisieLe().isBefore(debut) && !c.getSaisieLe().isAfter(fin)));
    }

    @Test
    @DisplayName("Trouver les commandes du 8 janvier 2025")
    public void testFindCommandeByDate8janvier() {
        LocalDate date = LocalDate.of(2025, 1, 8);
        List<Commande> commandes = commandeRepository.findBySaisieLe(date);
        
        assertFalse(commandes.isEmpty(), "Il doit y avoir une commande le 2025-01-08");
        assertEquals(1, commandes.size(), "Il doit y avoir exactement 1 commande le 8 janvier");
        assertTrue(commandes.stream()
            .allMatch(c -> c.getSaisieLe().equals(date)));
    }

    @Test
    @DisplayName("Trouver les commandes du 9 janvier 2025")
    public void testFindCommandeByDate9janvier() {
        LocalDate date = LocalDate.of(2025, 1, 9);
        List<Commande> commandes = commandeRepository.findBySaisieLe(date);
        
        assertEquals(2, commandes.size(), "Il doit y avoir 2 commandes le 9 janvier");
        assertTrue(commandes.stream()
            .allMatch(c -> c.getSaisieLe().equals(date)));
    }

    @Test
    @DisplayName("Trouver les commandes du 10 janvier 2025")
    public void testFindCommandeByDate10janvier() {
        LocalDate date = LocalDate.of(2025, 1, 10);
        List<Commande> commandes = commandeRepository.findBySaisieLe(date);
        
        assertEquals(2, commandes.size(), "Il doit y avoir 2 commandes le 10 janvier");
    }

    @Test
    @DisplayName("Vérifier qu'il y a exactement 12 commandes")
    public void testCommandesCount() {
        List<Commande> allCommandes = commandeRepository.findAll();
        assertEquals(12, allCommandes.size(), "Il doit y avoir exactement 12 commandes");
    }

    @Test
    @DisplayName("Vérifier les commandes livrées vs non livrées")
    public void testCommandesDeliveredVsUndelivered() {
        List<Commande> allCommandes = commandeRepository.findAll();
        long livrees = allCommandes.stream().filter(c -> c.getEnvoyeeLe() != null).count();
        long nonLivrees = allCommandes.stream().filter(c -> c.getEnvoyeeLe() == null).count();
        
        assertEquals(6, livrees, "Il doit y avoir 6 commandes livrées");
        assertEquals(6, nonLivrees, "Il doit y avoir 6 commandes non livrées");
    }

    // ===== TESTS LIGNE =====
    @Test
    @DisplayName("Trouver les lignes de la commande 1")
    public void testFindLigneByCommande1() {
        Commande commande = commandeRepository.findById(1).orElseThrow();
        List<Ligne> lignes = ligneRepository.findByCommande(commande);
        
        assertFalse(lignes.isEmpty());
        assertEquals(2, lignes.size(), "Commande 1 doit avoir 2 lignes");
        assertTrue(lignes.stream().allMatch(l -> l.getCommande().equals(commande)));
    }

    @Test
    @DisplayName("Trouver les lignes pour le médicament Morphine (référence 1)")
    public void testFindLigneByMedicament() {
        Medicament medicament = medicamentRepository.findById(1).orElseThrow();
        List<Ligne> lignes = ligneRepository.findByMedicament(medicament);
        
        assertFalse(lignes.isEmpty(), "Le médicament 1 (Morphine) doit avoir des lignes");
        assertTrue(lignes.stream().allMatch(l -> l.getMedicament().equals(medicament)));
    }

    @Test
    @DisplayName("Compter les lignes de la commande 1")
    public void testCountLigneByCommande1() {
        Commande commande = commandeRepository.findById(1).orElseThrow();
        long count = ligneRepository.countByCommande(commande);
        
        assertEquals(2, count, "Commande 1 doit avoir 2 lignes");
    }

    @Test
    @DisplayName("Compter les lignes du médicament 2 (Doliprane)")
    public void testCountLigneByMedicament2() {
        Medicament medicament = medicamentRepository.findById(2).orElseThrow();
        long count = ligneRepository.countByMedicament(medicament);
        
        assertTrue(count > 0, "Le médicament 2 (Doliprane) doit avoir au moins 1 ligne");
        assertEquals(3, count, "Le médicament 2 (Doliprane) doit avoir 3 lignes");
    }

    @Test
    @DisplayName("Vérifier qu'il y a exactement 22 lignes")
    public void testLignesCount() {
        List<Ligne> allLignes = ligneRepository.findAll();
        assertEquals(22, allLignes.size(), "Il doit y avoir exactement 22 lignes");
    }

    // ===== TESTS DE CALCUL =====
    @Test
    @DisplayName("Calculer le montant total d'une ligne")
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
        assertEquals(0, prixTotal.compareTo(expected), "Le prix total doit être 25.80 * 50 = 1290.00");
    }

    @Test
    @DisplayName("Vérifier la cohérence des dispensaires et commandes")
    public void testDispensaireCommandeConsistency() {
        List<Dispensaire> allDispensaires = dispensaireRepository.findAll();
        List<Commande> allCommandes = commandeRepository.findAll();

        assertEquals(10, allDispensaires.size());
        assertEquals(12, allCommandes.size());
        
        // Chaque commande doit avoir un dispensaire valide
        assertTrue(allCommandes.stream()
            .allMatch(c -> allDispensaires.contains(c.getDispensaire())));
    }

    @Test
    @DisplayName("Vérifier les commandes entre deux dates avec filtre")
    public void testCommandesBetweenDatesFiltered() {
        LocalDate debut = LocalDate.of(2025, 1, 1);
        LocalDate fin = LocalDate.of(2025, 1, 31);
        
        List<Commande> commandes = commandeRepository.findBySaisieLeBetween(debut, fin);
        assertEquals(12, commandes.size(), "Il doit y avoir 12 commandes en janvier 2025");
        
        assertTrue(commandes.stream()
            .allMatch(c -> !c.getSaisieLe().isBefore(debut) && !c.getSaisieLe().isAfter(fin)));
    }

    @Test
    @DisplayName("Vérifier les commandes du dispensaire Paris par 2 méthodes")
    public void testCommandesParisConsistency() {
        List<Commande> commandesParis1 = commandeRepository.findByDispensaire_Nom("Pharmacie Centrale Paris");
        List<Commande> commandesParis2 = commandeRepository.findByDispensaire(dispensaireParis);
        
        assertEquals(commandesParis1.size(), commandesParis2.size(), 
            "Les deux méthodes doivent retourner le même nombre de commandes");
        assertEquals(2, commandesParis1.size(), "Pharmacie Centrale Paris doit avoir 2 commandes");
    }

    @Test
    @DisplayName("Vérifier les catégories - 10 catégories exactement")
    public void testCategoriesCount() {
        List<Categorie> allCategories = categorieRepository.findAll();
        assertEquals(10, allCategories.size(), "Il doit y avoir exactement 10 catégories");
    }

    @Test
    @DisplayName("Vérifier les médicaments - 10 médicaments exactement")
    public void testMedicamentsCount() {
        List<Medicament> allMedicaments = medicamentRepository.findAll();
        assertEquals(10, allMedicaments.size(), "Il doit y avoir exactement 10 médicaments");
    }

    @Test
    @DisplayName("Vérifier qu'il y a 2 médicaments indisponibles")
    public void testIndisponibleMedicaments() {
        List<Medicament> indisponibles = medicamentRepository.findAll().stream()
            .filter(Medicament::isIndisponible)
            .toList();
        
        assertEquals(2, indisponibles.size(), "Il doit y avoir 2 médicaments indisponibles");
    }
}
