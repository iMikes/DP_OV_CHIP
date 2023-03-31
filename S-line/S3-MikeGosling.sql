-- S3.1.
-- Produceer een overzicht van alle cursusuitvoeringen; geef de
-- code, de begindatum, de lengte en de naam van de docent.
DROP VIEW IF EXISTS s3_1;

CREATE
OR REPLACE VIEW s3_1 AS -- [TEST]
SELECT
    c.code,
    u.begindatum,
    c.lengte,
    m.naam
FROM
    uitvoeringen u,
    medewerkers m,
    cursussen c
WHERE
    c.code = u.cursus
    AND u.docent = m.mnr;

-- S3.2.
-- Geef in twee kolommen naast elkaar de achternaam van elke cursist (`cursist`)
-- van alle S02-cursussen, met de achternaam van zijn cursusdocent (`docent`).
DROP VIEW IF EXISTS s3_2;

CREATE
OR REPLACE VIEW s3_2 AS -- [TEST]
SELECT
    c.naam cursist,
    d.naam docent
FROM
    medewerkers c
    INNER JOIN inschrijvingen i ON c.mnr = i.cursist
    and i.cursus = 'S02'
    INNER JOIN uitvoeringen u ON i.begindatum = u.begindatum
    and u.cursus = i.cursus
    INNER JOIN medewerkers d ON d.mnr = u.docent;

-- S3.3.
-- Geef elke afdeling (`afdeling`) met de naam van het hoofd van die
-- afdeling (`hoofd`).
DROP VIEW IF EXISTS s3_3;

CREATE
OR REPLACE VIEW s3_3 AS
SELECT
    a.naam as afdeling,
    m.naam as hoofd
FROM
    afdelingen a,
    medewerkers m
WHERE
    a.hoofd = m.mnr;

-- S3.4.
-- Geef de namen van alle medewerkers, de naam van hun afdeling (`afdeling`)
-- en de bijbehorende locatie.
DROP VIEW IF EXISTS s3_4;

CREATE
OR REPLACE VIEW s3_4 AS
SELECT
    m.naam as afdeling,
    a.naam as hoofd,
    a.locatie
FROM
    afdelingen a,
    medewerkers m
WHERE
    m.afd = a.anr;

-- S3.5.
-- Geef de namen van alle cursisten die staan ingeschreven voor de cursus S02 van 12 april 2019
DROP VIEW IF EXISTS s3_5;

CREATE
OR REPLACE VIEW s3_5 AS -- [TEST]
SELECT
    m.naam
FROM
    uitvoeringen u,
    inschrijvingen i,
    medewerkers m
WHERE
    i.cursus = u.cursus
    AND i.cursus = 'S02'
    AND i.begindatum = '2019-04-12'
    AND i.cursist = m.mnr;

-- S3.6.
-- Geef de namen van alle medewerkers en hun toelage.
DROP VIEW IF EXISTS s3_6;

CREATE
OR REPLACE VIEW s3_6 AS
SELECT
    m.naam,
    s.toelage
FROM
    medewerkers m,
    schalen s
WHERE
    (
        m.maandsal >= s.ondergrens
        AND m.maandsal <= s.bovengrens
    );

-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.
SELECT
    *
FROM
    test_select('S3.1') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S3.2') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S3.3') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S3.4') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S3.5') AS resultaat
UNION
SELECT
    *
FROM
    test_select('S3.6') AS resultaat
ORDER BY
    resultaat;