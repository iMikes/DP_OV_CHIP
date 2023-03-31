package Adres;

import Reiziger.Reiziger;
import Reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {

    private Connection conn;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        String sql = "INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, adres.getId());
        pstmt.setString(2, adres.getPostcode());
        pstmt.setString(3, adres.getHuisnummer());
        pstmt.setString(4, adres.getStraat());
        pstmt.setString(5, adres.getWoonplaats());
        pstmt.setInt(6, adres.getReiziger().getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        String sql = "UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, adres.getPostcode());
        pstmt.setString(2, adres.getHuisnummer());
        pstmt.setString(3, adres.getStraat());
        pstmt.setString(4, adres.getWoonplaats());
        pstmt.setInt(5, adres.getReiziger().getId());
        pstmt.setInt(6, adres.getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        String sql = "DELETE FROM adres WHERE adres_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, adres.getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM adres WHERE reiziger_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reiziger.getId());
        ResultSet rs = pstmt.executeQuery();

        Adres adres = null;
        if (rs.next()) {
            adres = new Adres(rs.getInt("adres_id"), rs.getString("postcode"), rs.getString("huisnummer"), rs.getString("straat"), rs.getString("woonplaats"), reiziger);
        }
        pstmt.close();
        rs.close();
        return adres;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        String sql = "SELECT * FROM adres JOIN reiziger ON adres.reiziger_id = reiziger.reiziger_id";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Adres> adressen = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("adres_id");
            String postcode = rs.getString("postcode");
            String huisnummer = rs.getString("huisnummer");
            String straat = rs.getString("straat");
            String woonplaats = rs.getString("woonplaats");
            int reizigerId = rs.getInt("reiziger_id");
            String voorletters = rs.getString("voorletters");
            String tussenvoegsel = rs.getString("tussenvoegsel");
            String achternaam = rs.getString("achternaam");
            Date geboortedatum = rs.getDate("geboortedatum");
            Reiziger reiziger = new Reiziger(reizigerId, voorletters, tussenvoegsel, achternaam, geboortedatum);
            Adres adres = new Adres(id, postcode, huisnummer, straat, woonplaats, reiziger);
            adressen.add(adres);
        }
        pstmt.close();
        rs.close();

        return adressen;
    }


}
