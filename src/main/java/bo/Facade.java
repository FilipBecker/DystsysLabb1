package bo;

import Enums.Privlige;
import Exeptions.NoSuchUserExeption;
import Model.Product;
import Util.exeptions.ConnectionFailExeption;
import dao.ProductDAO;
import ui.ViewItem;
import ui.ViewProduct;

import javax.swing.text.View;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class Facade {
    public static ViewItem getItem() {
        TestItem testItem = new TestItem("Test", 100);
        return new ViewItem(testItem);
    }

    public static List<ViewProduct> getAllProducts() throws SQLException {
        return toViewProductList(ProductDAO.findAll());
        /*List<Product> products = ProductDAO.findAll();
        if (!products.isEmpty()) {
            List<ViewProduct> viewProducts = new ArrayList<>();
            for (Product product : products) {
                viewProducts.add(new ViewProduct(product));
            }
            return viewProducts;
        }
        return null;*/

    }

    public static ViewProduct getProductById(int id) throws  SQLException {
        Product product = ProductDAO.findById(id);
        if(product == null) return null;
        return new ViewProduct(product);
    }

    public static List<ViewProduct> getProductByName(String name) throws SQLException{
        return toViewProductList(ProductDAO.findByName(name));
        /*
        List<Product> products = ProductDAO.findByName(name);
        if(!products.isEmpty()){
            List<ViewProduct> viewProducts = new ArrayList<>();
            for (Product product : products) viewProducts.add(new ViewProduct(product));
            return viewProducts;
        }
        return null;*/
    }

    private static List<ViewProduct> toViewProductList(List<Product> products) {
        if (products == null || products.isEmpty()) {
            return null;
        }

        List<ViewProduct> viewProducts = new ArrayList<>();
        for (Product product : products) {
            viewProducts.add(new ViewProduct(product));
        }
        return viewProducts;
    }

    public static Privlige validateUser(String username, String password) throws NoSuchUserExeption {
        if (username == null || password == null) return null;
        return Privlige.COSTUMER;
    }

}
