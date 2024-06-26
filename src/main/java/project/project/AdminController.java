package project.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController implements Initializable {

    @FXML
    private Button adminAddButton;

    @FXML
    private Button adminEditButton;

    @FXML
    private Button adminRemoveButton;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> addressCol;

    @FXML
    private TableColumn<User, String> cityCol;

    @FXML
    private TableColumn<User, String> nameCol;

    @FXML
    private TableColumn<User, String> passwordCol;

    @FXML
    private TableColumn<User, String> peselCol;

    @FXML
    private TableColumn<User, String> phoneCol;

    @FXML
    private TableColumn<User, String> roleCol;

    @FXML
    private TableView<Product> productsTable;

    @FXML
    private TableColumn<Product, Integer> idProductCol;

    @FXML
    private TableColumn<Product, String> nameProductCol;

    @FXML
    private TableColumn<Product, Integer> priceCol;

    @FXML
    private TableColumn<Product, String> descriptionCol;

    @FXML
    private TableColumn<Product, Integer> productAmountCol;

    @FXML
    private TableView<UserCart> cartsTable;

    @FXML
    private TableColumn<UserCart, Integer> idCartCol;

    @FXML
    private TableColumn<UserCart, String> peselCartCol;

    @FXML
    private TableColumn<UserCart, Integer> idProductCartCol;

    @FXML
    private TableColumn<?, ?> amountCol;



    String sql = null;
    String sql2 = null;
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ResultSet resultSet2 = null;
    User user = null;
    Product product = null;
    UserCart userCart = null;
    int index = -1;
    int tableOption = 1;

    AlertStyleController alert = new AlertStyleController();

    ObservableList<User> UserList = FXCollections.observableArrayList();
    ObservableList<Product> ProductsList = FXCollections.observableArrayList();
    ObservableList<UserCart> UserCartList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productsTable.setVisible(false);
        cartsTable.setVisible(false);
        loadUserDate();
    }

    public void CloseWindow(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void usersButton(MouseEvent event) {
        loadUserDate();
        userTable.setVisible(true);
        productsTable.setVisible(false);
        cartsTable.setVisible(false);
        index = -1;
        tableOption = 1;
        adminAddButton.setDisable(false);
        adminRemoveButton.setDisable(false);
        adminEditButton.setDisable(false);
    }


    public void refreshUserTable() {
        UserList.clear();
        sql = "SELECT * FROM user";
        sql2 = "SELECT * FROM user_information";
        try {
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            preparedStatement = con.prepareStatement(sql2);
            resultSet2 = preparedStatement.executeQuery();
            while (resultSet.next() && resultSet2.next()) {
                UserList.add(new User(
                        resultSet.getString("username"),
                        resultSet.getString("role"),
                        resultSet.getString("password"),
                        resultSet.getString("pesel"),
                        resultSet2.getString("city"),
                        resultSet2.getString("address"),
                        resultSet2.getString("phone_number")
                ));
                userTable.setItems(UserList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadUserDate(){
        con = ConnectionUtil.conDB();
        refreshUserTable();
        peselCol.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

    }

    @FXML
    private void getAddView(MouseEvent event) {
        try {
            Parent root;
            if(tableOption == 1) {
                root = FXMLLoader.load(getClass().getResource("addUser.fxml"));
            } else {
                root = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
            }
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void getRefreshTable(MouseEvent event) {
        refreshUserTable();
        refreshProductsTable();
        refreshCartTable();
    }
    @FXML
    private void deleteUserRow(MouseEvent event) {
        index = -1;
        if(tableOption == 1) {
            index = userTable.getSelectionModel().getSelectedIndex();
            if (index > -1) {
                String pesel = peselCol.getCellData(index).toString();
                sql = "DELETE FROM user WHERE pesel = ?";
                sql2 = "DELETE FROM user_information WHERE pesel = ?";
                try {
                    preparedStatement = con.prepareStatement(sql2);
                    preparedStatement.setString(1, pesel);
                    preparedStatement.execute();
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, pesel);
                    preparedStatement.execute();
                    refreshUserTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else if(tableOption == 2) {
            index = productsTable.getSelectionModel().getSelectedIndex();
            if (index > -1) {
                int id = idProductCol.getCellData(index);
                sql = "DELETE FROM product WHERE id_product = ?";
                try {
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                    refreshProductsTable();
                } catch (SQLException e) {
                    alert.notificationError(" Błąd!", "Jakiś użytkowink posiada ten przedmiot w koszyku");
                }
            }
        } else {
            index = cartsTable.getSelectionModel().getSelectedIndex();
            if (index > -1) {
                int id = idCartCol.getCellData(index);
                sql = "DELETE FROM shopping_cart WHERE id_cart = ?";
                try {
                    preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setInt(1, id);
                    preparedStatement.execute();
                    refreshCartTable();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
    @FXML
    private void editRow(MouseEvent event) {
        index = -1;
        if(tableOption == 1) {
            index = userTable.getSelectionModel().getSelectedIndex();
            if (index > -1) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("addUser.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                String pesel = peselCol.getCellData(index).toString();
                String name = nameCol.getCellData(index).toString();
                String password = passwordCol.getCellData(index).toString();
                String role = roleCol.getCellData(index).toString();
                String city = cityCol.getCellData(index).toString();
                String address = addressCol.getCellData(index).toString();
                String phoneNumber = phoneCol.getCellData(index).toString();

                AddUserController addUserController = fxmlLoader.getController();
                addUserController.setUpdate(true);
                addUserController.setTextField(pesel, name, password, role, city, address, phoneNumber);
                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            }
        } else {
            index = productsTable.getSelectionModel().getSelectedIndex();
            if (index > -1) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("addProduct.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int id = idProductCol.getCellData(index);
                String name = nameProductCol.getCellData(index).toString();
                int price = priceCol.getCellData(index);
                String description = descriptionCol.getCellData(index).toString();
                int amount = productAmountCol.getCellData(index);
                AddProductController addProductController = fxmlLoader.getController();
                addProductController.setUpdate(true);
                addProductController.setTextField(id,name,price,description,amount);
                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();
            }
        }
    }

    //    PRODUCTS

    @FXML
    void productsButton(MouseEvent event) {
        loadProductsDate();
        userTable.setVisible(false);
        productsTable.setVisible(true);
        cartsTable.setVisible(false);
        index = -1;
        tableOption = 2;
        adminAddButton.setDisable(false);
        adminRemoveButton.setDisable(false);
        adminEditButton.setDisable(false);
    }

    public void refreshProductsTable() {
        ProductsList.clear();
        sql = "SELECT * FROM product";
        try {
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                ProductsList.add(new Product(
                        resultSet.getInt("id_product"),
                        resultSet.getString("name"),
                        resultSet.getInt("price"),
                        resultSet.getString("description"),
                        resultSet.getInt("product_amount")
                ));
                productsTable.setItems(ProductsList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadProductsDate(){
        con = ConnectionUtil.conDB();
        refreshProductsTable();
        idProductCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        nameProductCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("productDescription"));
        productAmountCol.setCellValueFactory(new PropertyValueFactory<>("productAmount"));

    }

    @FXML
    void shopCartsButton(MouseEvent event) {
        loadCartDate();
        userTable.setVisible(false);
        productsTable.setVisible(false);
        cartsTable.setVisible(true);
        index = -1;
        tableOption = 3;
        adminAddButton.setDisable(true);
        adminEditButton.setDisable(true);
    }

    public void refreshCartTable() {
        UserCartList.clear();
        sql = "SELECT * FROM shopping_cart";
        try {
            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UserCartList.add(new UserCart(
                        resultSet.getInt("id_cart"),
                        resultSet.getString("pesel"),
                        resultSet.getInt("id_product"),
                        resultSet.getInt("amount")
                ));
                cartsTable.setItems(UserCartList);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCartDate(){
        con = ConnectionUtil.conDB();
        refreshCartTable();
        idCartCol.setCellValueFactory(new PropertyValueFactory<>("idCart"));
        peselCartCol.setCellValueFactory(new PropertyValueFactory<>("pesel"));
        idProductCartCol.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

    }

}
