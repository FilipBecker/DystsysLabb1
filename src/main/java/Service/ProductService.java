package Service;

import Model.Product;
import Util.exeptions.ConnectionFailExeption;
import dao.ProductDAO;


import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() throws ConnectionFailExeption, SQLException {
        return productDAO.findAll();
    }

    public Product getProductById(int id) throws ConnectionFailExeption, SQLException {
        return productDAO.findById(id);
    }
}