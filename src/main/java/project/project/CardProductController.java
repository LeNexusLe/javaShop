package project.project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class CardProductController implements Initializable {

    @FXML
    private AnchorPane cardForm;

    @FXML
    private Button productAddButton;

    @FXML
    private Label productName;

    @FXML
    private Label productPrice;

    @FXML
    private Label productAmount;

    @FXML
    private Spinner<Integer> productSpinner;

    private Product product;

    private SpinnerValueFactory<Integer> productValueFactory;
    private int prodId;
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;
    AlertStyleController alert = new AlertStyleController();
    private int totalPrice;

    public void setData(Product product) {
        this.user = loginController.getUser();
        this.product = product;
        prodId = product.idProduct;
        productName.setText(product.productName);
        productPrice.setText(String.valueOf(product.productPrice) + " PLN");
        productAmount.setText(String.valueOf(product.productAmount));
    }
    private int qty;
    public void setQuantity() {
        productValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        productSpinner.setValueFactory(productValueFactory);
    }

    public void addBtn(MouseEvent mouseEvent){
        qty = productSpinner.getValue();
        int check = 0;
        String checkAvalible = "SELECT product_amount FROM product WHERE id_product = '" + prodId + "'";
        con = ConnectionUtil.conDB();
        try {
            preparedStatement = con.prepareStatement(checkAvalible);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                check = resultSet.getInt("product_amount");
            }
            if(check < qty || qty == 0){
                alert.notificationError(" Błąd", "Brak wymaganej ilości lub wybrana wartość równa 0");
            }else {
                String alreadyExits = "SELECT * FROM shopping_cart WHERE id_product = '" + prodId + "' AND pesel = '"+ user.pesel +"'";
                preparedStatement = con.prepareStatement(alreadyExits);
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    String insertData = "UPDATE shopping_cart SET amount = ?, full_price = ? WHERE id_product = '" + prodId + "' AND pesel = '"+ user.pesel +"'";
                    preparedStatement = con.prepareStatement(insertData);
                    preparedStatement.setInt(1, (resultSet.getInt("amount") + qty));
                    totalPrice = ((resultSet.getInt("amount") + qty) * product.productPrice);
                    preparedStatement.setInt(2, totalPrice);
                    preparedStatement.executeUpdate();
                }else {
                    String insertData = "INSERT INTO shopping_cart (pesel,id_product,amount,full_price,name_product) VALUES(?,?,?,?,?)";
                    preparedStatement = con.prepareStatement(insertData);
                    preparedStatement.setString(1,user.pesel);
                    preparedStatement.setInt(2,prodId);
                    preparedStatement.setInt(3, qty);
                    totalPrice = (qty * product.productPrice);
                    preparedStatement.setInt(4, totalPrice);
                    preparedStatement.setString(5, product.productName);
                    preparedStatement.executeUpdate();
                }
                alert.notificationInfo(" Sukces!", "Dodano przedmiot do koszyka");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setQuantity();
    }
}
