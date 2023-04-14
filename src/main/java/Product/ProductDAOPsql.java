package Product;

import OVChipkaart.OVChipkaart;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ProductDAOPsql implements ProductDAO {

    private Connection conn;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        String sql = "INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, product.getProductNummer());
        pstmt.setString(2, product.getNaam());
        pstmt.setString(3, product.getBeschrijving());
        pstmt.setDouble(4, product.getPrijs());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        String sql = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, product.getNaam());
        pstmt.setString(2, product.getBeschrijving());
        pstmt.setDouble(3, product.getPrijs());
        pstmt.setInt(4, product.getProductNummer());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        String sql = "DELETE FROM product WHERE product_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, product.getProductNummer());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1;
    }

    @Override
    public Product findByProductNummer(int productNummer) throws SQLException {
        String sql = "SELECT * FROM product WHERE product_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, productNummer);
        ResultSet rs = pstmt.executeQuery();

        Product product = null;
        if (rs.next()) {
            product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"), rs.getDouble("prijs"));
        }
        pstmt.close();
        rs.close();
        return product;
    }

    @Override
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT * FROM product";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Product> producten = new ArrayList<>();
        while (rs.next()) {
            int productNummer = rs.getInt("product_nummer");
            String naam = rs.getString("naam");
            String beschrijving = rs.getString("beschrijving");
            double prijs = rs.getDouble("prijs");
            Product product = new Product(productNummer, naam, beschrijving, prijs);
            producten.add(product);
        }
        pstmt.close();
        rs.close();

        return producten;
    }

    @Override
    public boolean saveOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        String sql = "INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, ovChipkaart.getKaartnummer());
        pstmt.setDate(2, (java.sql.Date) ovChipkaart.getGeldigTot());
        pstmt.setInt(3, ovChipkaart.getKlasse());
        pstmt.setBigDecimal(4, new BigDecimal(ovChipkaart.getSaldo()));
        pstmt.setInt(5, ovChipkaart.getReiziger().getId());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        // Save the product relations
        List<Product> products = ovChipkaart.getProducten();
        for (Product product : products) {
            sql = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ovChipkaart.getKaartnummer());
            pstmt.setInt(2, product.getProductNummer());
            pstmt.setString(3, "actief");
            pstmt.setDate(4, (java.sql.Date) new Date(System.currentTimeMillis()));
            rowsAffected += pstmt.executeUpdate();
            pstmt.close();
        }

        return rowsAffected == 1 + products.size();
    }

    @Override
    public boolean deleteOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        // Delete the product relations first
        String sql = "DELETE FROM ov_chipkaart_product WHERE kaart_nummer = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, ovChipkaart.getKaartnummer());
        int rowsAffected = pstmt.executeUpdate();
        pstmt.close();

        // Delete the ov-chipkaart itself
        sql = "DELETE FROM ov_chipkaart WHERE kaart_nummer = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, ovChipkaart.getKaartnummer());
        rowsAffected += pstmt.executeUpdate();
        pstmt.close();

        return rowsAffected == 1 + ovChipkaart.getProducten().size();
    }

}
