-- S2.1. Vier-daagse cursussen
--
-- Geef code en omschrijving van alle cursussen die precies vier dagen duren.
DROP VIEW IF EXISTS s2_1; CREATE OR REPLACE VIEW s2_1 AS                                                     -- [TEST]

SELECT code, omschrijving FROM cursussen WHERE lengte = 4;

-- S2.2. Medewerkersoverzicht
--
-- Geef alle informatie van alle medewerkers, gesorteerd op functie,
-- en per functie op leeftijd (van jong naar oud).
DROP VIEW IF EXISTS s2_2; CREATE OR REPLACE VIEW s2_2 AS                                                     -- [TEST]

SELECT * FROM medewerkers
ORDER BY    functie ASC,
            gbdatum DESC;

-- S2.3. Door het land
--
-- Welke cursussen zijn in Utrecht en/of in Maastricht uitgevoerd? Geef
-- code en begindatum.
DROP VIEW IF EXISTS s2_3; CREATE OR REPLACE VIEW s2_3 AS                                                     -- [TEST]

SELECT cursus, begindatum FROM uitvoeringen
WHERE locatie = 'UTRECHT' OR locatie = 'MAASTRICHT';

-- S2.4. Namen
--
-- Geef de naam en voorletters van alle medewerkers, behalve van R. Jansen.
DROP VIEW IF EXISTS s2_4; CREATE OR REPLACE VIEW s2_4 AS                                                     -- [TEST]

SELECT naam, voorl FROM medewerkers
WHERE naam != 'JANSEN' AND voorl != 'R';


-- S2.5. Nieuwe SQL-cursus
--
-- Er wordt een nieuwe uitvoering gepland voor cursus S02, en wel op de
-- komende 2 maart. De cursus wordt gegeven in Leerdam door Nick Smit.
-- Voeg deze gegevens toe.

INSERT INTO uitvoeringen (cursus, begindatum, docent, locatie)
VALUES ('S02', '02-03-2022', 7902, 'LEERDAM')
	ON CONFLICT DO NOTHING;                                                                                         -- [TEST]


-- S2.6. Stagiairs
--
-- Neem één van je collega-studenten aan als stagiair ('STAGIAIR') en
-- voer zijn of haar gegevens in. Kies een personeelnummer boven de 8000.

INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (8172, 'PIET', 'V', 'STAGIAIR', 7566, '15-04-1990', 0, NULL, 20)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]


-- S2.7. Nieuwe schaal
--
-- We breiden het salarissysteem uit naar zes schalen. Voer een extra schaal in voor mensen die
-- tussen de 3001 en 4000 euro verdienen. Zij krijgen een toelage van 500 euro.

INSERT INTO schalen (snr, ondergrens, bovengrens, toelage)
VALUES (6, 4001, 9999, 600);

UPDATE schalen
SET bovengrens = 4000
WHERE snr = 5;
--ON CONFLICT DO NOTHING;                                                                                         -- [TEST]


-- S2.8. Nieuwe cursus
--
-- Er wordt een nieuwe 6-daagse cursus 'Data & Persistency' in het programma opgenomen.
-- Voeg deze cursus met code 'D&P' toe, maak twee uitvoeringen in Leerdam en schrijf drie
-- mensen in.

INSERT INTO cursussen (code, omschrijving, type, lengte)
VALUES ('D&P', 'Data & Persistency', 'ALG', 6)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO uitvoeringen (cursus, begindatum, docent, locatie)
VALUES ('D&P', '28-02-2022', 7902, 'LEERDAM')
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO uitvoeringen (cursus, begindatum, docent, locatie)
VALUES ('D&P', '07-03-2022', 7902, 'LEERDAM')
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO inschrijvingen (cursist, cursus, begindatum, evaluatie)
VALUES (7499, 'D&P', '28-02-2022', NULL)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO inschrijvingen (cursist, cursus, begindatum, evaluatie)
VALUES (7934, 'D&P', '28-02-2022', NULL)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]
INSERT INTO inschrijvingen (cursist, cursus, begindatum, evaluatie)
VALUES (7902, 'D&P', '07-03-2022', NULL)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]


-- S2.9. Salarisverhoging
--
-- De medewerkers van de afdeling VERKOOP krijgen een salarisverhoging
-- van 5.5%, behalve de manager van de afdeling, deze krijgt namelijk meer: 7%.
-- Voer deze verhogingen door.

UPDATE medewerkers
SET maandsal = maandsal + (maandsal * 0.055)
WHERE afd = 30 AND functie = 'VERKOPER';

UPDATE medewerkers
SET maandsal = maandsal + (maandsal * 0.07)
WHERE afd = 30 AND functie = 'MANAGER';

-- S2.10. Concurrent
--
-- Martens heeft als verkoper succes en wordt door de concurrent
-- weggekocht. Verwijder zijn gegevens.

-- Zijn collega Alders heeft ook plannen om te vertrekken. Verwijder ook zijn gegevens.
-- Waarom lukt dit (niet)?

--DELETE FROM medewerkers WHERE naam = 'MARTENS';

--DELETE FROM medewerkers WHERE naam = 'ALDERS';

-- ERROR:  update or delete on table "medewerkers" violates foreign key constraint "i_cursist_fk" on table "inschrijvingen"
-- DETAIL:  Key (mnr)=(7499) is still referenced from table "inschrijvingen".

-- S2.11. Nieuwe afdeling
--
-- Je wordt hoofd van de nieuwe afdeling 'FINANCIEN' te Leerdam,
-- onder de hoede van De Koning. Kies een personeelnummer boven de 8000.
-- Zorg voor de juiste invoer van deze gegevens.

INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (8000, 'Mike', 'M', 'MANAGER', 7839, '27-06-2000', 3500, NULL, 10)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]

INSERT INTO afdelingen (anr, naam, locatie, hoofd)
VALUES (50, 'FINANCIEN', 'LEERDAM', 8000)
ON CONFLICT DO NOTHING;                                                                                         -- [TEST]



-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_select('S2.1') AS resultaat
UNION
SELECT 'S2.2 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT * FROM test_select('S2.3') AS resultaat
UNION
SELECT * FROM test_select('S2.4') AS resultaat
UNION
SELECT * FROM test_exists('S2.5', 1) AS resultaat
UNION
SELECT * FROM test_exists('S2.6', 1) AS resultaat
UNION
SELECT * FROM test_exists('S2.7', 6) AS resultaat
ORDER BY resultaat;

-- Draai alle wijzigingen terug om conflicten in komende opdrachten te voorkomen.
UPDATE medewerkers SET afd = NULL WHERE mnr < 7369 OR mnr > 7934;
UPDATE afdelingen SET hoofd = NULL WHERE anr > 40;
DELETE FROM afdelingen WHERE anr > 40;
DELETE FROM medewerkers WHERE mnr < 7369 OR mnr > 7934;
DELETE FROM inschrijvingen WHERE cursus = 'D&P';
DELETE FROM uitvoeringen WHERE cursus = 'D&P';
DELETE FROM cursussen WHERE code = 'D&P';
DELETE FROM uitvoeringen WHERE locatie = 'LEERDAM';
INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm, afd)
VALUES (7654, 'MARTENS', 'P', 'VERKOPER', 7698, '28-09-1976', 1250, 1400, 30);
UPDATE medewerkers SET maandsal = 1600 WHERE mnr = 7499;
UPDATE medewerkers SET maandsal = 1250 WHERE mnr = 7521;
UPDATE medewerkers SET maandsal = 2850 WHERE mnr = 7698;
UPDATE medewerkers SET maandsal = 1500 WHERE mnr = 7844;
UPDATE medewerkers SET maandsal = 800 WHERE mnr = 7900;
