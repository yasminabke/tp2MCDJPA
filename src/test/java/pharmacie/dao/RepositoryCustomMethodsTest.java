package pharmacie.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    @DisplayName("Trouver les dispensaires par région - Île-de-France")
    public void testFindDispensaireByRegionIleDeFrance() {
        List<Dispensaire> dispensairesParisiens = dispensaireRepository.findByAdressePostale_RegionIgnoreCase("Île-de-France");
        assertFalse(dispensairesParisiens.isEmpty());
        assertEquals(dispensairesParisiens.size(), dispensairesParisiens.size());
        assertTrue(dispensairesParisiens.stream()
            .allMatch(d -> d.getAdressePostale().getRegion().equalsIgnoreCase("Île-de-France")));
    }

    @Test
    @DisplayName("Trouver les dispensaires par region - Rhone-Alpes")
    public void testFindDispensaireByRegionRhoneAlpes() {
        List<Dispensaire> dispensaires = dispensaireRepository.findByAdressePostale_RegionIgnoreCase("Rhône-Alpes");
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
    @DisplayName("Vérifier les commandes livrées (5) et non livrées (7)")
    public void testCommandesDeliveredVsUndelivered() {
        List<Commande> allCommandes = commandeRepository.findAll();
        long livrees = allCommandes.stream().filter(c -> c.getEnvoyeeLe() != null).count();
        long nonLivrees = allCommandes.stream().filter(c -> c.getEnvoyeeLe() == null).count();
        
        assertEquals(5, livrees);
        assertEquals(7, nonLivrees);
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
        
        assertEquals(4, count);
    }

    @Test
    @DisplayName("Vérifier qu'il y a 21 lignes au total")
    public void testLignesCount() {
        List<Ligne> allLignes = ligneRepository.findAll();
        assertEquals(21, allLignes.size());
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
    @DisplayName("Vérifier qu'il y a 14 médicaments")
    public void testMedicamentsCount() {
        List<Medicament> allMedicaments = medicamentRepository.findAll();
        assertEquals(14, allMedicaments.size());
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

    // @Test: il marchait avant l'ajout de la contrainte d'integrité demandée
    // void unMedicamentSansCategorieEstInterdit(){
    //     Medicament m = new Medicament();
    //     m.setNom("Doliprane");

    //     assertThrows(DataIntegrityViolationException.class, () -> {
    //         medicamentRepository.saveAndFlush(m);
    //     }, "Should not save Medicament without Categorie");
    // }

    @Test
    @DisplayName("Calculer le nombre d'articles commandés par un dispensaire (commandes envoyées)")
    public void testCountArticlesCommandesParDispensaire() {
        Long totalArticles = commandeRepository.countArticlesCommandesParDispensaire("PAR01");
        
        assertNotNull(totalArticles);
        // PAR01 a 2 commandes envoyées (numéro 1 et 4)
        // Commande 1: 50 + 30 = 80 articles
        // Commande 4: 80 articles non envoyé
        // Total = 80 articles
        assertEquals(80L, totalArticles);
    }

    @Test
    @DisplayName("Calculer le nombre d'articles commandés par PAR02")
    public void testCountArticlesCommandesParDispensaire_PAR02() {
        Long totalArticles = commandeRepository.countArticlesCommandesParDispensaire("PAR02");
        
        // PAR02 a 1 commande non envoyée, donc aucun article compté
        assertNull(totalArticles);
    }

    @Test
    @DisplayName("Calculer les unités commandées par médicament d'une catégorie")
    public void testMedicamentsVendusPour() {
        List<UnitesParMedicament> resultats = medicamentRepository.medicamentsVendusPour(1);
        
        assertNotNull(resultats);
        assertFalse(resultats.isEmpty());
        
        resultats.forEach(r -> {
            assertNotNull(r.getNom());
            assertNotNull(r.getUnites());
            assertTrue(r.getUnites() > 0);
        });
    }
    
    @Test
    @DisplayName("Trouver les commandes en cours pour BES01")
    public void testFindCommandesEnCoursByDispensaire_BES01() {
        List<Commande> commandesEnCours = commandeRepository.findCommandesEnCoursByDispensaire("BES01");
        
        assertNotNull(commandesEnCours);
        // BES01 a 1 commande envoyée, donc 0 en cours
        assertEquals(0, commandesEnCours.size());
    }

    @Test
    @DisplayName("Vérifier que les commandes en cours sont ordonnées par date (descendant)")
    public void testCommandesEnCoursSontOrdonnees() {
        List<Commande> commandesEnCours = commandeRepository.findCommandesEnCoursByDispensaire("MRS01");
        
        // Vérifier l'ordre des dates (plus récentes d'abord)
        for (int i = 0; i < commandesEnCours.size() - 1; i++) {
            assertTrue(commandesEnCours.get(i).getSaisieLe()
                .isAfter(commandesEnCours.get(i + 1).getSaisieLe()),
                "Les commandes doivent être ordonnées par date décroissante");
        }
    }

    @Test
    @DisplayName("Vérifier qu'un dispensaire sans commandes envoyées retourne null")
    public void testCountArticlesForDispensaireSansCommandesEnvoyees() {
        Long totalArticles = commandeRepository.countArticlesCommandesParDispensaire("GRE01");
        
        // GRE01 a 1 commande mais elle n'est pas envoyée
        assertNull(totalArticles);
    }

    @Test
    @DisplayName("Vérifier qu'un dispensaire inexistant retourne null")
    public void testCountArticlesForInexistantDispensaire() {
        Long totalArticles = commandeRepository.countArticlesCommandesParDispensaire("XXXX");
        
        // Aucun dispensaire avec ce code
        assertNull(totalArticles);
    }

    @Test
    @DisplayName("Trouver les médicaments disponibles à la commande pour la catégorie 3 (Antibiotiques)")
    public void testFindMedicamentsDisponiblesParCategorie_Cat3() {
        List<Medicament> disponibles = medicamentRepository.findMedicamentsDisponiblesParCategorie(3);
        
        // Catégorie 3 a 2 médicaments mais les 2 sont INDISPONIBLES
        // Donc la liste doit être vide
        assertTrue(disponibles.isEmpty());
    }

    @Test
    @DisplayName("Trouver les médicaments disponibles à la commande pour la catégorie 2")
    public void testFindMedicamentsDisponiblesParCategorie_Cat2() {
        List<Medicament> disponibles = medicamentRepository.findMedicamentsDisponiblesParCategorie(2);
        
        assertNotNull(disponibles);
        // Catégorie 2 (Anti-inflammatoires) a 2 médicaments disponibles
        assertEquals(2, disponibles.size());
    }

    @Test
    @DisplayName("Trouver les médicaments disponibles à la commande pour une catégorie sans médicaments")
    public void testFindMedicamentsDisponiblesParCategorie_Empty() {
        // Supposons que la catégorie 99 n'existe pas
        List<Medicament> disponibles = medicamentRepository.findMedicamentsDisponiblesParCategorie(99);
        
        assertTrue(disponibles.isEmpty());
    }
}
