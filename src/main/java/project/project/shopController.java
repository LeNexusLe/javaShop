package project.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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

import project.project.loginController;

public class shopController implements Initializable {

    @FXML
    private GridPane menuGrid;

    @FXML
    private ScrollPane menuScroll;

    private ObservableList<Product> cardListData = FXCollections.observableArrayList();

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user;
    AlertStyleController alert = new AlertStyleController();

    public ObservableList<Product> menuGetData(){
        String sql = "SELECT * FROM product";
        ObservableList<Product> list = FXCollections.observableArrayList();
        con = ConnectionUtil.conDB();
        try {

            preparedStatement = con.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            Product product;
            while (resultSet.next()) {
                product = new Product(
                        resultSet.getInt("id_product"),
                        resultSet.getString("name"),
                        resultSet.getInt("price"),
                        resultSet.getString("description"),
                        resultSet.getInt("product_amount")
                );
                list.add(product);
            }

        } catch (Exception ex) {

        }

        return list;
    }

    public void menuDisplayCard(){
        cardListData.clear();
        cardListData.addAll(menuGetData());
        int row = 0;
        int column = 0;
        menuGrid.getChildren().clear();
        menuGrid.getRowConstraints().clear();
        menuGrid.getColumnConstraints().clear();

        for (int i = 0; i < cardListData.size(); i++) {

            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("cardProduct.fxml"));
                AnchorPane pane = load.load();
                CardProductController cardC = load.getController();
                cardC.setData(cardListData.get(i));
                if (column == 4) {
                    column = 0;
                    row += 1;
                }

                menuGrid.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(10));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }

    @FXML
    void cartGetView(MouseEvent event) {
        try {
            Parent root;
            root = FXMLLoader.load(getClass().getResource("cartPanel.fxml"));
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
    void logOut(MouseEvent event) {
        if(alert.confirmationBox(" Opuszczanie aplikacji","Czy chcesz się wylogować?")){
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    void refreshBtn(MouseEvent event) {
        menuDisplayCard();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        menuDisplayCard();
    }
}
