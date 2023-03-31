-- S6.1.
--
-- 1. Maak een view met de naam "deelnemers" waarmee je de volgende gegevens uit de tabellen inschrijvingen en uitvoering combineert:
--    inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie
CREATE OR REPLACE VIEW deelnemers AS
    SELECT i.cursist, i.cursus, i.begindatum, u.docent, u.locatie
    FROM inschrijvingen i, uitvoeringen u
    WHERE i.cursus = u.cursus AND i.begindatum = u.begindatum;

-- 2. Gebruik de view in een query waarbij je de "deelnemers" view combineert met de "personeels" view  (behandeld in de les):
CREATE OR REPLACE VIEW personeel AS
    SELECT mnr, voorl, naam as medewerker, afd, functie
    FROM medewerkers;

select * from deelnemers d, personeel p;
-- 3. Is de view "deelnemers" updatable ? Waarom ?
--Niet updateable, Bestaat uit meerdere tabellen


-- S6.2.
--
-- 1. Maak een view met de naam "dagcursussen". Deze view dient de gegevens op te halen: 
--      code, omschrijving en type uit de tabel curssussen met als voorwaarde dat de lengte = 1. Toon aan dat de view werkt.
CREATE OR REPLACE VIEW dagcursussen AS
    SELECT code, omschrijving, type
    FROM cursussen WHERE lengte = 1;
-- 2. Maak een tweede view met de naam "daguitvoeringen". 
--    Deze view dient de uitvoeringsgegevens op te halen voor de "dagcurssussen" (gebruik ook de view "dagcursussen"). Toon aan dat de view werkt
CREATE OR REPLACE VIEW daguitvoeringen AS
    SELECT *
    FROM dagcursussen d, uitvoeringen u
    WHERE d.code = u.cursus;
-- 3. Verwijder de views en laat zien wat de verschillen zijn bij DROP view <viewnaam> CASCADE en bij DROP view <viewnaam> RESTRICT

DROP VIEW dagcursussen CASCADE;

DROP VIEW daguitvoeringen RESTRICT;

--Cascade verwijderd alles
--Restrict kijkt eerst of er relatie's is en zo ja verwijderd hij niks;