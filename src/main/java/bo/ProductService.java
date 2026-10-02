package bo;

import Model.Product;
import Util.exeptions.ConnectionFailExeption;
import dao.ProductDAO;


import java.sql.SQLException;
import java.util.List;

public class ProductService {

    public List<Product> getAllProducts() throws ConnectionFailExeption, SQLException {
        return ProductDAO.findAll();
    }

    public Product getProductById(int id) throws ConnectionFailExeption, SQLException {
        return ProductDAO.findById(id);
    }
}