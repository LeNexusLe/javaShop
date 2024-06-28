package project.project;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddUserController {

    @FXML
    private TextField peselInput;

    @FXML
    private TextField addressInput;

    @FXML
    private TextField cityInput;

    @FXML
    private TextField nameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private TextField phoneInput;

    @FXML
    private TextField balanceInput;

    @FXML
    private TextField roleInput;

    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    User user = null;
    loginController loginController = new loginController();
    AlertStyleController alert = new AlertStyleController();
    private boolean update;
    String sql = null;
    String sql2 = null;
    String id = null;
    void setTextField(String pesel,String name,String password,String role,String city,String address,String phone,int balance) {
        peselInput.setDisable(true);
        nameInput.setDisable(true);
        id = pesel;
        peselInput.setText(pesel);
        nameInput.setText(name);
        passwordInput.setText(password);
        roleInput.setText(role);
        cityInput.setText(city);
        addressInput.setText(address);
        phoneInput.setText(phone);
        balanceInput.setText(String.valueOf(balance));
    }

    void setUpdate(boolean b) {
        this.update = b;

    }

    private void getQuery() {

        if (update == false) {
            sql = "INSERT INTO user (pesel, username, password, role,balance) VALUES (?,?,?,?,?)";
            sql2 = "INSERT INTO user_information (pesel, city, address, phone_number) VALUES (?,?,?,?)";

        }else{
            sql = "UPDATE user SET username = ?, password = ?, role = ?, balance = ? WHERE pesel = '"+id+"'";
            sql2 = "UPDATE user_information SET city = ?, address = ?, phone_number = ? WHERE pesel = '"+id+"'";

        }

    }


    private void insert(String pesel, String name, String password, String role, String city, String address, String phone, int balance) {

        try {
            if(update == false){
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, pesel);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, password);
                preparedStatement.setString(4, role);
                preparedStatement.setInt(5, balance);
                preparedStatement.execute();
                preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setString(1, pesel);
                preparedStatement.setString(2, city);
                preparedStatement.setString(3, address);
                preparedStatement.setString(4, phone);
                preparedStatement.execute();
                alert.notificationInfo(" Sukces!", "Udało się stworzyć użytkownika");
            }else {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, role);
                preparedStatement.setInt(4, balance);
                preparedStatement.execute();
                preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setString(1, city);
                preparedStatement.setString(2, address);
                preparedStatement.setString(3, phone);
                preparedStatement.execute();
                alert.notificationInfo(" Sukces!", "Udało się zedytować użytkownika");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    @FXML
    void saveUser(MouseEvent event) {
        con = ConnectionUtil.conDB();
        String pesel = peselInput.getText();
        String name = nameInput.getText();
        String password = passwordInput.getText();
        String role = roleInput.getText();
        String city = cityInput.getText();
        String address = addressInput.getText();
        String phone = phoneInput.getText();
        int balance = 0;
        try {
            balance = Integer.parseInt(balanceInput.getText());
            if (!loginController.checkValue(name,password,pesel,city,address,phone,update)) {

            }else if (balance >= 0) {
                getQuery();
                insert(pesel, name, password, role, city, address, phone, balance);
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }else {
                alert.notificationError(" Błąd!", " Za mała wartość salda");
            }
        } catch (NumberFormatException ex) {
            alert.notificationError(" Błąd!", "Za duża wartość salda lub saldo zawiera znaki");
        }
    }

}
