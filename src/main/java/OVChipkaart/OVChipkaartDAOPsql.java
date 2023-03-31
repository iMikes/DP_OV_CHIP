package OVChipkaart;

import Reiziger.Reiziger;
import Reiziger.ReizigerDAO;
import Reiziger.ReizigerDAOPsql;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {

    private final Connection conn;
    private ReizigerDAO reizigerDao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, ovChipkaart.getKaartnummer());
        pstmt.setDate(2, (Date) ovChipkaart.getGeldigTot());
        pstmt.setInt(3, ovChipkaart.getKlasse());
        pstmt.setDouble(4, ovChipkaart.getSaldo());
        pstmt.setInt(5, ovChipkaart.getReiziger().getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDate(1, (Date) ovChipkaart.getGeldigTot());
        pstmt.setInt(2, ovChipkaart.getKlasse());
        pstmt.setDouble(3, ovChipkaart.getSaldo());
        pstmt.setInt(4, ovChipkaart.getReiziger().getId());
        pstmt.setInt(5, ovChipkaart.getKaartnummer());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, ovChipkaart.getKaartnummer());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();

        OVChipkaart ovChipkaart = null;
        if (rs.next()) {
            int kaartNummer = rs.getInt("kaart_nummer");
            Date geldigTot = rs.getDate("geldig_tot");
            int klasse = rs.getInt("klasse");
            double saldo = rs.getDouble("saldo");
            int reizigerId = rs.getInt("reiziger_id");
            Reiziger reiziger = new ReizigerDAOPsql(conn).findById(reizigerId);

            ovChipkaart = new OVChipkaart(kaartNummer, geldigTot, klasse, saldo, reiziger);
        }
        pstmt.close();
        rs.close();

        return ovChipkaart;
    }
    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reiziger.getId());
        ResultSet rs = pstmt.executeQuery();

        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        while (rs.next()) {
            OVChipkaart ovChipkaart = new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"), rs.getDouble("saldo"), reiziger);
            ovChipkaarten.add(ovChipkaart);
        }
        pstmt.close();
        rs.close();
        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        String sql = "SELECT * FROM ov_chipkaart";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        while (rs.next()) {
            int kaartNummer = rs.getInt("kaart_nummer");
            Date geldigTot = rs.getDate("geldig_tot");
            int klasse = rs.getInt("klasse");
            double saldo = rs.getDouble("saldo");
            int reizigerId = rs.getInt("reiziger_id");

            Reiziger reiziger = null;
            if (reizigerDao != null) {
                reiziger = reizigerDao.findById(reizigerId);
            }

            OVChipkaart ovChipkaart = new OVChipkaart(kaartNummer, geldigTot, klasse, saldo, reiziger);
            ovChipkaarten.add(ovChipkaart);
        }
        pstmt.close();
        rs.close();
        return ovChipkaarten;
    }

}