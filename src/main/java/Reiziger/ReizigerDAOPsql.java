package Reiziger;

import Reiziger.Reiziger;
import Reiziger.ReizigerDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection conn;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        String sql = "INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reiziger.getId());
        pstmt.setString(2, reiziger.getVoorletters());
        pstmt.setString(3, reiziger.getTussenvoegsel());
        pstmt.setString(4, reiziger.getAchternaam());
        pstmt.setDate(5, reiziger.getGeboortedatum());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        String sql = "UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, reiziger.getVoorletters());
        pstmt.setString(2, reiziger.getTussenvoegsel());
        pstmt.setString(3, reiziger.getAchternaam());
        pstmt.setDate(4, reiziger.getGeboortedatum());
        pstmt.setInt(5, reiziger.getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        String sql = "DELETE FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reiziger.getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        String sql = "SELECT * FROM reiziger WHERE reiziger_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        Reiziger reiziger = null;
        if (rs.next()) {
            reiziger = new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), rs.getDate("geboortedatum"));
        }
        pstmt.close();
        rs.close();

        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        String sql = "SELECT * FROM reiziger WHERE geboortedatum = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDate(1, Date.valueOf(datum));
        ResultSet rs = pstmt.executeQuery();

        List<Reiziger> reizigers = new ArrayList<>();
        while (rs.next()) {
            reizigers.add(new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), rs.getDate("geboortedatum")));
        }
        pstmt.close();
        rs.close();
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        String sql = "SELECT * FROM reiziger";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Reiziger> reizigers = new ArrayList<>();
        while (rs.next()) {
            reizigers.add(new Reiziger(rs.getInt("reiziger_id"), rs.getString("voorletters"), rs.getString("tussenvoegsel"), rs.getString("achternaam"), rs.getDate("geboortedatum")));
        }
        pstmt.close();
        rs.close();

        return reizigers;
    }
}
