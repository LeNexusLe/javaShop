package project.project;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.TextArea;

public class AddProductController {
    @FXML
    private TextField nameInput;

    @FXML
    private TextField priceInput;

    @FXML
    private TextArea descriptionInput;

    @FXML
    private TextField amountInput;

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;
    AlertStyleController alert = new AlertStyleController();
    private boolean update;
    String sql = null;
    int id = -1;
    void setTextField(int idCol,String name,int price,String description, int amount) {
        id = idCol;
        nameInput.setText(name);
        priceInput.setText(String.valueOf(price));
        descriptionInput.setText(description);
        amountInput.setText(String.valueOf(amount));
    }

    void setUpdate(boolean b) {
        this.update = b;
    }

    private void getQuery() {

        if (update == false) {
            sql = "INSERT INTO product (name, price, description, product_amount) VALUES (?,?,?,?)";

        }else{
            sql = "UPDATE product SET name = ?, price = ?, description = ?, product_amount = ? WHERE id_product = '"+id+"'";
        }

    }


    private void insert(String name,int price,String description, int amount) {

        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, price);
            preparedStatement.setString(3, description);
            preparedStatement.setInt(4, amount);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @FXML
    void saveProduct(MouseEvent event) {
        con = ConnectionUtil.conDB();
        String name = nameInput.getText();
        String price = priceInput.getText();
        String description = descriptionInput.getText();
        String amount = amountInput.getText();

        if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {

            alert.notificationError(" Bład!","Wszystkie dane muszą być wypełnione!");
        }else {
            int priceNumber = Integer.parseInt(price);
            int amountNumber = Integer.parseInt(amount);
            getQuery();
            insert(name,priceNumber,description,amountNumber);
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }


}
