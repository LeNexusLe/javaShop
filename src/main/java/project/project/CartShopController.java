package project.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CartShopController  implements Initializable {


    @FXML
    private TableView<UserCart> cartTable;

    @FXML
    private TableColumn<UserCart, Integer> productAmountCol;

    @FXML
    private TableColumn<UserCart, String> productNameCol;

    @FXML
    private TableColumn<UserCart, Integer> fullPriceCol;

    @FXML
    private Label balanceLbl;

    @FXML
    private Label cartSumLbl;


    String sql = null;
    String sql2 = null;
    String sql3 = null;
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ResultSet resultSet2 = null;
    ResultSet resultSet3 = null;
    User user = null;
    AlertStyleController alert = new AlertStyleController();

    ObservableList<UserCart> UserCartList = FXCollections.observableArrayList();

    int index = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        user = loginController.getUser();
        loadData();
    }

    private void loadData(){
        con = ConnectionUtil.conDB();
        refreshTable();
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        fullPriceCol.setCellValueFactory(new PropertyValueFactory<>("fullPrice"));
    }

    private void refreshTable() {
        UserCartList.clear();
        balanceLbl.setText(String.valueOf(user.balance) + " PLN");
        cartSumLbl.setText(String.valueOf(sumProduct() + " PLN"));
        sql = "SELECT * FROM shopping_cart WHERE pesel = '"+user.pesel+"'";
        try {
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserCartList.add(new UserCart(
                        resultSet.getInt("amount"),
                        resultSet.getString("name_product"),
                        resultSet.getInt("full_price")
                ));
                cartTable.setItems(UserCartList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void deleteBtn(MouseEvent event) {
        index = -1;
        index = cartTable.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            String id = productNameCol.getCellData(index);
            sql = "DELETE FROM shopping_cart WHERE name_product = ? AND pesel = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, user.pesel);
                preparedStatement.execute();
                refreshTable();
                alert.notificationInfo(" Sukces!","Usunięto produkt z koszyka");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int sumProduct(){
        sql = "SELECT * FROM shopping_cart WHERE pesel = ?";
        int sum = 0;
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, user.pesel);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next() == true) {
                sum = sum + resultSet.getInt("full_price");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return sum;
    }


    private boolean checkAvalible(){
        sql = "SELECT * FROM shopping_cart WHERE pesel = ?";
        sql2 = "SELECT product_amount FROM product WHERE id_product = ?";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, user.pesel);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next() == true) {
                preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setInt(1, resultSet.getInt("id_product"));
                resultSet2 = preparedStatement.executeQuery();
                if (resultSet2.next() && resultSet.getInt("amount") > resultSet2.getInt("product_amount")) {
                    alert.notificationError(" Błąd","Nie wystarczająco " + resultSet.getString("name_product") +" w magazynie");
                    return false;
                }
            }
            return true;
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteProduct(){
        sql = "SELECT * FROM shopping_cart WHERE pesel = ?";
        sql2 = "SELECT * FROM product WHERE id_product = ?";
        sql3 = "UPDATE product SET product_amount = ? WHERE id_product = ?";
        int amount = 0;
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, user.pesel);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next() == true) {
                preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setInt(1, resultSet.getInt("id_product"));
                resultSet2 = preparedStatement.executeQuery();
                if(resultSet2.next() == true){
                    amount = resultSet2.getInt("product_amount") - resultSet.getInt("amount");
                    preparedStatement = con.prepareStatement(sql3);
                    preparedStatement.setInt(1, amount);
                    preparedStatement.setInt(2, resultSet.getInt("id_product"));
                    preparedStatement.execute();
                }
            }
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void payBtn(MouseEvent event) {
        if(checkAvalible() && user.balance >= sumProduct()){
            if(alert.confirmationBox(" Potwierdzenie Zakupu", "Czy na pewno chcesz wszystko kupić?")){
                try {
                    int sum = user.balance - sumProduct();
                    deleteProduct();
                    sql = "DELETE FROM shopping_cart WHERE pesel = ?";
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, user.pesel);
                    preparedStatement.execute();
                    sql2 = "UPDATE user SET balance = ? WHERE pesel = ?";
                    user.balance = sum;
                    preparedStatement = con.prepareStatement(sql2);
                    preparedStatement.setInt(1, sum);
                    preparedStatement.setString(2, user.pesel);
                    preparedStatement.execute();
                    refreshTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
