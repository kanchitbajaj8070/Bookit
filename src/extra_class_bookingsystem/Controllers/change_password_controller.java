package extra_class_bookingsystem.Controllers;

import extra_class_bookingsystem.Database.DatabaseHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import extra_class_bookingsystem.Alert_maker.Alert_handler;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class change_password_controller implements Initializable {
    @FXML
    private PasswordField old_password;

    @FXML
    private PasswordField new_password;

    @FXML
    private PasswordField confirm_password;
        @FXML
        private Button change_password_button;
    @FXML
        private void handleButton() throws Exception {
        String old_pass = old_password.getText();
        String new_pass = new_password.getText();
        String confirm_pass = confirm_password.getText();
        if (old_pass.length() < 2 || new_pass.length() < 2 || confirm_pass.length() < 2) {
            Alert_handler.showErrorMessage("Error-Empty Enteries", "No enteries can be left empty", null);
            return;
        }
        if (new_pass.length() < 6) {
            Alert_handler.showErrorMessage("Error", "New password should be greater than 6 characters", "");
            return;

        }
        if (!old_pass.equals(LoginController.password)) {
            Alert_handler.showErrorMessage("Error", "Current password doesnt match", "");
            return;

        }
        if (!new_pass.equals(confirm_pass)) {
            Alert_handler.showErrorMessage("passwords dont match", "Password entered dont match \n kindly enter same password ", "");
            return;
        }
        if (new_pass.equals(confirm_pass)&&old_pass.equals(LoginController.password)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Password ");
            alert.setHeaderText("Your password will be updated  ");
            alert.setContentText(" Are you sure you want to proceed?");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    try {
                        String sql = "update bookingsystem.employees set password= ? where username =? ";


                        PreparedStatement preparedStatement = DatabaseHelper.getInstance().getConnection().prepareStatement(sql);
                        preparedStatement.setString(1, new_pass);
                        preparedStatement.setString(2, LoginController.uname);
                        int p = preparedStatement.executeUpdate();
                        System.out.println(p);
                        if (p == 1) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Password Updation Successfull ");
                            alert1.setHeaderText("Your password was updated succesfully ");
                            alert1.showAndWait();
                            LoginController.password=new_pass;

                        } else {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Name Update Unsuccesfull ");
                            alert1.setHeaderText("Your name can not be updated ");
                            alert1.showAndWait();
                        }
                        new_password.setText(null);
                        old_password.setText(null);
                        confirm_password.setText(null);

                    } catch (SQLException ex) {

                        Alert_handler.showErrorMessage("Error","Error occurred while booking","Please try again later");
                    }
                }
            });
        }
    }
    public void handleEnter(KeyEvent key_event) throws Exception {
        if (key_event.getCode() == KeyCode.ENTER) {
            System.out.println(key_event.getCode());
            handleButton();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}


