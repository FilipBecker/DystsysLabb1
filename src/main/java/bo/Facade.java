package bo;

import Model.Product;
import dao.ProductDAO;
import ui.ViewItem;
import ui.ViewProduct;

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
        List<Product> products = ProductDAO.findAll();
        if (!products.isEmpty()) {
            List<ViewProduct> viewProducts = new ArrayList<>();
            for (Product product : products) {
                viewProducts.add(new ViewProduct(product));
            }
            return viewProducts;
        }
        return null;

    }
}
