import Adres.Adres;
import Adres.AdresDAO;
import Adres.AdresDAOPsql;
import Reiziger.Reiziger;
import Reiziger.ReizigerDAO;
import Reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class Main {
    private static Connection conn;

    public void getConnection(String url, String user, String password) throws SQLException {
        conn = DriverManager.getConnection(url, user, password);
    }

    public void closeConnection() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/ovchip";
        String user = "postgres";
        String password = "root";

        Main app = new Main();

        try {
            app.getConnection(url, user, password);

            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(conn);
            testReizigerDAO(reizigerDAO);

            app.closeConnection();
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("Alle reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println("     " + r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(78, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
        // Update test
        sietske.setAchternaam("Boersma");
        rdao.update(sietske);
        System.out.println("[Test] Updated Sietske: " + rdao.findById(sietske.getId()));

        // Find by date of birth test
        System.out.println("[Test] Reizigers with date of birth 1981-03-14:");
        List<Reiziger> findByGbdatumReizigers = rdao.findByGbdatum(gbdatum);
        for (Reiziger r : findByGbdatumReizigers) {
            System.out.println("     " + r);
        }
        System.out.println();

        // Delete test
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.delete() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        System.out.println("\n---------- End of Test ReizigerDAO -------------");
    }

    public static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test AdresDAO ----------");

        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende " + adressen.size() + " adressen:");
        for (Adres adres : adressen) {
            System.out.println(adres);
        }
        System.out.println();

        // Maak een nieuwe adres aan en persisteer deze in de database
        Reiziger reiziger = new Reiziger(76,"J", "de", "Tester", Date.valueOf("1995-05-25"));
        rdao.save(reiziger);
        Adres nieuwAdres = new Adres(442,"1234AB", "10", "Teststraat", "Teststad", reiziger);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(nieuwAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen");
        System.out.println();

        // Update het adres
        System.out.println("[Test] AdresDAO.update() voor de update: " + nieuwAdres);
        nieuwAdres.setPostcode("5678CD");
        nieuwAdres.setHuisnummer("20");
        nieuwAdres.setWoonplaats("GewijzigdeTeststad");
        adao.update(nieuwAdres);
        System.out.println("[Test] AdresDAO.update() na de update: " + nieuwAdres);
        System.out.println();

        // Vind adres via reiziger
        System.out.println("[Test] AdresDAO.findByReiziger() geeft de volgende adressen voor reiziger " + reiziger.getNaam() + ":");
        Adres adressenVanReiziger = adao.findByReiziger(reiziger);
        System.out.println(adressenVanReiziger);
        System.out.println();

        // Verwijder het adres
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.delete() ");
        adao.delete(nieuwAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen");
        System.out.println();

        // Verwijder de reiziger
        rdao.delete(reiziger);
    }


}
