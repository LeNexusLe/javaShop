package project.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.stage.Stage;
import java.io.IOException;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class loginController {
    @FXML
    private Button loginButton;
    @FXML
    private TextField nameInput;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField registerNameInput;
    @FXML
    private PasswordField registerPasswordInput;
    @FXML
    private TextField registerPeselInput;
    @FXML
    private TextField registerCityInput;
    @FXML
    private TextField registerAddressInput;
    @FXML
    private TextField registerPhoneInput;

    static User user;

    private Stage stage;
    private Scene scene;
    private Parent root;
    AlertStyleController alert = new AlertStyleController();
//    KONTROLKI DLA GUI

    public void SceneControllerToRegister(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1020, 590);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void SceneControllerToLogin(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1020, 590);
        scene.getStylesheets().add(getClass().getResource("main.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    public void CloseWindow(ActionEvent event) {
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }


//    PODŁĄCZENIE DO BAZY

    Connection con = null;
    PreparedStatement preparedStatement = null;
    PreparedStatement preparedStatement2 = null;
    ResultSet resultSet = null;

    public loginController(){
        con = ConnectionUtil.conDB();
    }

//    LOGOWANIE UŻYTKOWNIKA

    public boolean isAdmin(String pesel){
        String sql = "SELECT role FROM user WHERE pesel = ?;";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, pesel);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String result = resultSet.getString(1);
                if(result.equals("admin")) {
                 return true;
                }
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public User userLogin(ActionEvent event) {
        String name = nameInput.getText();
        String userPassword = passwordInput.getText();
        if(name.isEmpty() || userPassword.isEmpty()) {
            alert.notificationError(" Bład!","Wszystkie dane muszą być wypełnione!");
        }
        else {
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, userPassword);
                resultSet = preparedStatement.executeQuery();
                if(!resultSet.next()) {
                    alert.notificationError(" Bład!","Wprowadzono złe dane");
                }else {
                    String pesel = resultSet.getString("pesel");
                    user = new User(name,pesel,userPassword);
                    if(isAdmin(pesel)) {
                        root = FXMLLoader.load(getClass().getResource("adminPanel.fxml"));
                        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root, 1220, 750);
                        scene.getStylesheets().add(getClass().getResource("admin.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();
                    } else {
                        root = FXMLLoader.load(getClass().getResource("shopPanel.fxml"));
                        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root, 1220, 750);
                        scene.getStylesheets().add(getClass().getResource("shop.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();
                    }
                    alert.notificationInfo(" Sukces!","Udało ci się zalogować");
                }
            } catch (Exception ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return user;
    }


    public static User getUser(){
        return user;
    }

//    REJESTROWANIE UŻYTKOWNIKA

    public boolean checkUserName(String userName) {
        String sql = "SELECT * FROM user WHERE username = ?;";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkUserPesel(String userPesel) {
        String sql = "SELECT * FROM user WHERE pesel = ?;";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, userPesel);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkValue(String userName, String userPassword, String userPesel, String userCity, String userAddress, String userPhone, boolean update){
        try {
            Double.parseDouble(userPesel);
            Double.parseDouble(userPhone);
        } catch (final NumberFormatException e) {
            alert.notificationError(" Bład!","Pesel oraz numer telefonu muszą zawierać same cyfry!");
            return false;
        }

        if(userName.isEmpty() || userPassword.isEmpty() || userPesel.isEmpty() || userCity.isEmpty() || userAddress.isEmpty() || userPhone.isEmpty()) {
            alert.notificationError(" Bład!","Wszystkie dane muszą być wypełnione!");
            return false;
        } else if (userPassword.length() < 8 || userPassword.length() > 18) {
            alert.notificationError(" Bład!","Hasło musi spełniać: 8-18 liter!");
            return false;
        } else if (userPesel.length() != 11){
            alert.notificationError(" Bład!","Pesel musi zawierać: 11 cyfr!");
            return false;
        } else if (checkUserPesel(userPesel) && !update) {
            alert.notificationError(" Bład!", "Istnieje użytkownik o takim peselu!");
            return false;
        } else if (userName.length() < 5 || userName.length() > 25) {
            alert.notificationError(" Bład!", "Nazwa użytkownika musi spełniać: 5-25 liter!");
            return false;
        }  else if (checkUserName(userName) && !update) {
            alert.notificationError(" Bład!", "Istnieje użytkownik o takiej nazwie!");
            return false;
        } else if (userCity.length() < 4 || userCity.length() > 30 || userCity.matches(".*\\d.*")) {
            alert.notificationError(" Bład!", "Nazwa miasta musi spełniać: 4-30 liter i nie może zawierać cyfr!");
            return false;
        }else if (userAddress.length() < 5 || userAddress.length() > 30) {
            alert.notificationError(" Bład!", "Adres musi spełniać: 5-30 liter!");
            return false;
        }else if (userPhone.length() < 8 || userPhone.length() > 12) {
            alert.notificationError(" Bład!", "Numer telefonu musi spełniać: 8-12 liter!");
            return false;
        }
        return true;
    }

    public void registerButtonAction(ActionEvent event) {
        String userName = registerNameInput.getText();
        String userPassword = registerPasswordInput.getText();
        String userPesel = registerPeselInput.getText();
        String userCity = registerCityInput.getText();
        String userAddress = registerAddressInput.getText();
        String userPhone = registerPhoneInput.getText();
        if(checkValue(userName,userPassword,userPesel,userCity,userAddress,userPhone,false)) {
            addUserToDatabase(userName,userPassword,userPesel,userCity,userAddress,userPhone);
        }
    }

    public void addUserToDatabase(String userName, String userPassword, String userPesel, String userCity, String userAddress, String userPhone) {
        String sql = "INSERT INTO user (pesel, username, password, role) VALUES (?,?,?,?)";
        String sql2 = "INSERT INTO user_information (pesel, city, address, phone_number) VALUES (?,?,?,?)";
        try {
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, userPesel);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, userPassword);
            preparedStatement.setString(4, "user");
            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0) {
                preparedStatement = con.prepareStatement(sql2);
                preparedStatement.setString(1, userPesel);
                preparedStatement.setString(2, userCity);
                preparedStatement.setString(3, userAddress);
                preparedStatement.setString(4, userPhone);
                int addedRows2 = preparedStatement.executeUpdate();
                if(addedRows2 > 0) {
                    alert.notificationInfo(" Sukces!","Udało ci się stworzyć konto");
                }
                else {
                    alert.notificationError(" Bład!","Błąd przy tworzeniu użytkownika!");
                }
            }else {
                alert.notificationError(" Bład!","Błąd przy tworzeniu użytkownika!");
            }
        } catch (Exception ex){
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
