package Product;

import Product.Product;
import OVChipkaart.OVChipkaart;

import java.sql.SQLException;
import java.util.List;

public interface ProductDAO {
    boolean save(Product product) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean delete(Product product) throws SQLException;
    Product findByProductNummer(int productNummer) throws SQLException;
    List<Product> findAll() throws SQLException;
    boolean saveOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;

    boolean deleteOVChipkaart(OVChipkaart ovChipkaart) throws SQLException;
}
