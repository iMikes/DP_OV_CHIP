-- S4.1. 
-- Geef nummer, functie en geboortedatum van alle medewerkers die vóór 1980
-- geboren zijn, en trainer of verkoper zijn.
DROP VIEW IF EXISTS s4_1;

CREATE
OR REPLACE VIEW s4_1 AS -- [TEST]
SELECT
    mnr,
    functie,
    gbdatum
FROM
    medewerkers
WHERE
    gbdatum <= '1980-01-01'
    AND (
        functie = 'TRAINER'
        OR functie = 'VERKOPER'
    );

-- S4.2. 
-- Geef de naam van de medewerkers met een tussenvoegsel (b.v. 'van der').
DROP VIEW IF EXISTS s4_2;

CREATE
OR REPLACE VIEW s4_2 AS -- [TEST]
SELECT
    naam
from
    medewerkers
WHERE
    naam like '% %';

-- S4.3. 
-- Geef nu code, begindatum en aantal inschrijvingen (`aantal_inschrijvingen`) van alle
-- cursusuitvoeringen in 2019 met minstens drie inschrijvingen.
DROP VIEW IF EXISTS s4_3;

CREATE
OR REPLACE VIEW s4_3 AS -- [TEST]
SELECT
    cursus,
    begindatum,
    COUNT(cursus) AS aantal_inschrijvingen
FROM
    inschrijvingen
WHERE
    begindatum >= '2019-01-01'
    AND begindatum < '2020-01-01'
GROUP BY
    cursus,
    begindatum
HAVING
    COUNT(cursus) >= 3;

-- S4.4. 
-- Welke medewerkers hebben een bepaalde cursus meer dan één keer gevolgd?
-- Geef medewerkernummer en cursuscode.
DROP VIEW IF EXISTS s4_4;

CREATE
OR REPLACE VIEW s4_4 AS -- [TEST]
SELECT
    cursist,
    cursus
FROM
    medewerkers m
    INNER JOIN inschrijvingen i ON m.mnr = i.cursist
GROUP BY
    cursist,
    cursus
HAVING
    COUNT(cursist) > 1;

-- S4.5. 
-- Hoeveel uitvoeringen (`aantal`) zijn er gepland per cursus?
-- Een voorbeeld van het mogelijke resultaat staat hieronder.
--
--   cursus | aantal   
--  --------+-----------
--   ERM    | 1 
--   JAV    | 4 
--   OAG    | 2 
DROP VIEW IF EXISTS s4_5;

CREATE
OR REPLACE VIEW s4_5 AS -- [TEST]
SELECT
    cursus,
    COUNT(cursus) AS aantal
FROM
    uitvoeringen
GROUP BY
    cursus;

-- S4.6. 
-- Bepaal hoeveel jaar leeftijdsverschil er zit tussen de oudste en de 
-- jongste medewerker (`verschil`) en bepaal de gemiddelde leeftijd van
-- de medewerkers (`gemiddeld`).
-- Je mag hierbij aannemen dat elk jaar 365 dagen heeft.
DROP VIEW IF EXISTS s4_6;

CREATE
OR REPLACE VIEW s4_6 AS -- [TEST]
SELECT
    (
        MAX(
            EXTRACT(
                YEAR
                from
                    AGE(NOW(), gbdatum)
            )
        ) - MIN(
            EXTRACT(
                YEAR
                from
                    AGE(NOW(), gbdatum)
            )
        )
    ) as "verschil",
    AVG(
        EXTRACT(
            YEAR
            from
                AGE(NOW(), gbdatum)
        )
    ) as "gemiddeld"
FROM
    medewerkers;

-- S4.7. 
-- Geef van het hele bedrijf een overzicht van het aantal medewerkers dat
-- er werkt (`aantal_medewerkers`), de gemiddelde commissie die ze
-- krijgen (`commissie_medewerkers`), en hoeveel dat gemiddeld
-- per verkoper is (`commissie_verkopers`).
DROP VIEW IF EXISTS s4_7;

CREATE
OR REPLACE VIEW s4_7 AS -- [TEST]
select
    count(*) as aantal_medewerkers,
    sum(comm) / count(*) as commissie_medewerkers,
    avg(comm) as commissie_verkopers
from
    medewerkers;

-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.
SELECT
    *
FROM
    test_select('S4.1') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S4.2') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S4.3') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S4.4') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S4.5') AS resultaat
UNION
SELECT
    'S4.6 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT
    *
FROM
    test_select('S4.7') AS resultaat
ORDER BY
    resultaat;