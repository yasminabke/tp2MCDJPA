-- Données de base pour le projet Pharmacie
-- Dispensaire (Etablissements de santé qui passent commande de médicaments)
-- Le fichier est chargé au démarrage de l''application

-- ===== INSERTION DES CATEGORIES =====
-- Insertion des catégories de médicaments
INSERT INTO CATEGORIE (CODE, LIBELLE, DESCRIPTION) VALUES
(DEFAULT, 'Antalgiques et Antipyrétiques', 'Médicaments contre la douleur et la fièvre'), -- code : 1
(DEFAULT, 'Anti-inflammatoires', 'Médicaments réduisant l''inflammation'), -- code : 2
(DEFAULT, 'Antibiotiques', 'Médicaments pour traiter les infections bactériennes'),
(DEFAULT, 'Antihypertenseurs', 'Médicaments pour traiter l''hypertension artérielle'),
(DEFAULT, 'Antidiabétiques', 'Médicaments pour traiter le diabète'),
(DEFAULT, 'Antihistaminiques', 'Médicaments pour traiter les allergies'),
(DEFAULT, 'Vitamines et Compléments', 'Suppléments nutritionnels'),
(DEFAULT, 'Médicaments Cardiovasculaires', 'Médicaments pour le cœur et la circulation'),
(DEFAULT, 'Médicaments Gastro-intestinaux', 'Médicaments pour les troubles digestifs'),
(DEFAULT, 'Médicaments Respiratoires', 'Médicaments pour les troubles respiratoires');

-- ===== INSERTION DES MEDICAMENTS =====
-- Catégorie 1: Antalgiques et Antipyrétiques
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Morphine 10mg', 1, 'Boîte de 14 comprimés', 25.80, 80, 0, 15, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400'),
('Doliprane Effervescent 1g', 1, 'Boîte de 8 comprimés', 3.50, 280, 0, 30, false, 'https://images.unsplash.com/photo-1587854692152-cbe660dbde88?w=400'),
('Efferalgan Vitamine C', 1, 'Boîte de 16 comprimés', 4.20, 220, 0, 25, false, 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=400');

-- Catégorie 2: Anti-inflammatoires
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Étodolac 400mg', 2, 'Boîte de 14 comprimés', 12.50, 110, 0, 15, false, 'https://images.unsplash.com/photo-1471864190281-a93a3070b6de?w=400'),
('Flurbiprofène 100mg', 2, 'Boîte de 30 comprimés', 10.80, 130, 0, 16, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400');

-- Catégorie 3: Antibiotiques (2 médicaments indisponibles)
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Lévofloxacine 500mg', 3, 'Boîte de 7 comprimés', 15.80, 160, 0, 18, true, 'https://images.unsplash.com/photo-1628771065518-0d82f1938462?w=400'),
('Clindamycine 300mg', 3, 'Boîte de 16 gélules', 13.20, 140, 0, 16, true, 'https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?w=400');

-- Catégorie 4: Antihypertenseurs
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Lisinopril 5mg', 4, 'Boîte de 30 comprimés', 9.99, 95, 0, 12, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400');

-- Catégorie 5: Antidiabétiques
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Metformine 500mg', 5, 'Boîte de 30 comprimés', 4.25, 240, 0, 40, false, 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=400');

-- Catégorie 6: Antihistaminiques
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Cetirizine 10mg', 6, 'Boîte de 7 comprimés', 5.99, 150, 0, 20, false, 'https://images.unsplash.com/photo-1471864190281-a93a3070b6de?w=400');

-- Catégorie 7: Vitamines et Compléments
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Vitamine C 500mg', 7, 'Boîte de 30 comprimés', 7.50, 200, 0, 25, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400');

-- Catégorie 8: Médicaments Cardiovasculaires
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Amlodipine 5mg', 8, 'Boîte de 30 comprimés', 11.50, 105, 0, 14, false, 'https://images.unsplash.com/photo-1587854692152-cbe660dbde88?w=400');

-- Catégorie 9: Médicaments Gastro-intestinaux
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Oméprazole 20mg', 9, 'Boîte de 14 comprimés', 8.99, 170, 0, 20, false, 'https://images.unsplash.com/photo-1576091160550-2173dba999ef?w=400');

-- Catégorie 10: Médicaments Respiratoires
INSERT INTO MEDICAMENT (NOM, CATEGORIE_CODE, QUANTITE_PAR_UNITE, PRIX_UNITAIRE, UNITES_EN_STOCK, UNITES_COMMANDEES, NIVEAU_DE_REAPPRO, INDISPONIBLE, imageURL) VALUES
('Salbutamol 100mcg', 10, 'Aérosol de 200 doses', 15.50, 85, 0, 10, false, 'https://images.unsplash.com/photo-1550572017-edd951aa8f72?w=400');

-- ===== INSERTION DES DISPENSAIRES =====
-- Structure: CODE, NOM, CONTACT, FONCTION, TELEPHONE, FAX, ADRESSE, VILLE, REGION, CODE_POSTAL, PAYS
INSERT INTO DISPENSAIRE (code, nom, contact, fonction, telephone, fax, adresse, ville, region, code_postal, pays) VALUES
('PAR01', 'Pharmacie Centrale Paris', 'contact@pharma-paris.fr', 'Directeur', '0142345678', '0142345679', '42 Rue de Rivoli', 'Paris', 'Île-de-France', '75004', 'France'),
('PAR02', 'Pharmacie du Marais', 'marais@pharma.fr', 'Gérant', '0142987654', '0142987655', '15 Rue des Rosiers', 'Paris', 'Île-de-France', '75004', 'France'),
('MRS01', 'Pharmacie Marseille', 'marseille@pharma.fr', 'Directeur', '0491567890', '0491567891', '123 Boulevard Michelet', 'Marseille', 'PACA', '13008', 'France'),
('AIX01', 'Pharmacie Aix-en-Provence', 'aix@pharma.fr', 'Responsable', '0442123456', '0442123457', '45 Cours Mirabeau', 'Aix-en-Provence', 'PACA', '13100', 'France'),
('LYO01', 'Pharmacie Lyon', 'lyon@pharma.fr', 'Directeur', '0472345678', '0472345679', '78 Rue de la République', 'Lyon', 'Rhône-Alpes', '69002', 'France'),
('BOR01', 'Pharmacie Bordeaux', 'bordeaux@pharma.fr', 'Gérant', '0556789012', '0556789013', '99 Rue Porte Dijeaux', 'Bordeaux', 'Nouvelle-Aquitaine', '33000', 'France'),
('GRE01', 'Pharmacie Grenoble', 'grenoble@pharma.fr', 'Responsable', '0476543210', '0476543211', '56 Rue Très-Cloître', 'Grenoble', 'Auvergne-Rhône-Alpes', '38000', 'France'),
('VER01', 'Pharmacie Versailles', 'versailles@pharma.fr', 'Directeur', '0139509080', '0139509081', '88 Rue de l''Église', 'Versailles', 'Île-de-France', '78000', 'France'),
('DIJ01', 'Pharmacie Dijon', 'dijon@pharma.fr', 'Gérant', '0380123456', '0380123457', '200 Rue d''Auxonne', 'Dijon', 'Bourgogne-Franche-Comté', '21000', 'France'),
('BES01', 'Pharmacie Besançon', 'besancon@pharma.fr', 'Responsable', '0381234567', '0381234568', '77 Rue Battant', 'Besançon', 'Bourgogne-Franche-Comté', '25000', 'France');

-- ===== INSERTION DES COMMANDES =====
-- Structure: NUMERO (auto), DATE_COMMANDE, ENVOYEELE, PORT, REMISE, DISPENSAIRE_CODE
INSERT INTO COMMANDE (date_commande, envoyeele, port, remise, dispensaire_code) VALUES
('2025-01-08', '2025-01-10', 15.00, 5.00, 'PAR01'),
('2025-01-09', NULL, 12.00, 2.00, 'PAR02'),
('2025-01-10', NULL, 18.00, 3.00, 'MRS01'),
('2025-01-11', NULL, 10.00, 0.00, 'PAR01'),
('2025-01-06', '2025-01-08', 20.00, 4.00, 'AIX01'),
('2025-01-07', '2025-01-09', 16.00, 2.50, 'LYO01'),
('2025-01-04', NULL, 22.00, 5.00, 'BOR01'),
('2025-01-10', NULL, 14.00, 1.00, 'VER01'),
('2025-01-09', '2025-01-11', 18.00, 3.00, 'DIJ01'),
('2025-01-05', '2025-01-07', 25.00, 6.00, 'BES01'),
('2025-01-12', NULL, 11.00, 2.00, 'GRE01'),
('2025-01-13', NULL, 17.00, 4.50, 'MRS01');

-- ===== INSERTION DES LIGNES DE COMMANDE =====
-- Structure: ID (auto), QUANTITE, COMMANDE_NUMERO, MEDICAMENT_REFERENCE

-- Commande 1 (PAR01, 2025-01-08)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (50, 1, 1);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (30, 1, 3);

-- Commande 2 (PAR02, 2025-01-09)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (100, 2, 2);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (20, 2, 4);

-- Commande 3 (MRS01, 2025-01-10)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (60, 3, 5);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (40, 3, 6);

-- Commande 4 (PAR01, 2025-01-11)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (80, 4, 2);

-- Commande 5 (AIX01, 2025-01-06)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (30, 5, 1);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (50, 5, 8);

-- Commande 6 (LYO01, 2025-01-07)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (25, 6, 7);

-- Commande 7 (BOR01, 2025-01-04)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (150, 7, 2);

-- Commande 8 (VER01, 2025-01-10)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (40, 8, 4);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (35, 8, 9);

-- Commande 9 (DIJ01, 2025-01-09)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (70, 9, 1);

-- Commande 10 (BES01, 2025-01-05)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (60, 10, 3);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (50, 10, 5);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (40, 10, 10);

-- Commande 11 (GRE01, 2025-01-12)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (45, 11, 1);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (55, 11, 3);

-- Commande 12 (MRS01, 2025-01-13)
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (75, 12, 2);
INSERT INTO LIGNE (quantite, commande_numero, medicament_reference) VALUES (25, 12, 5);