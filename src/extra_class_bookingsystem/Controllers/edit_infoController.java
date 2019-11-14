package extra_class_bookingsystem.Controllers;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Loader;
import extra_class_bookingsystem.Controllers.LoginController;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import extra_class_bookingsystem.Alert_maker.Alert_handler;
import extra_class_bookingsystem.Database.DatabaseHelper;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class edit_infoController {

    @FXML
    private TextField txtnewName;

    @FXML
    private Button update_name_button;

    @FXML
    private TextField txtnewEmail;
public DatabaseHelper handler=null;
    @FXML
    private Button update_email_button;
    public void handle_update_name()
    {  taskChangeName task= new taskChangeName();
        ExecutorService executorService= Executors.newFixedThreadPool(1);
        executorService.execute(task);
        executorService.shutdown();
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("succesful name updation");
            }
        });
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("failed name updation");
                Alert_handler.showErrorMessage("Error"," Cant update name","");}
        });
    }

    public class taskChangeName extends Task<Void> {
        @Override
        protected Void call() throws Exception {
           handleUupdateNname();
           return null;
        }


        public void handleUupdateNname() throws Exception {

            if (txtnewName.getLength() == 0) {
                Alert_handler.showErrorMessage(" Error-Empty name field", " New name cant be empty\n Type some value", null);

            } else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Update Name ");
                    alert.setHeaderText("Your name will be updated ");
                    alert.setContentText(" Are you sure you want to proceed ?");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            try {
                                String sql = "update bookingsystem.employees set name= ? where username =? ";

                                PreparedStatement preparedStatement = handler.getInstance().getConnection().prepareStatement(sql);
                                preparedStatement.setString(1, txtnewName.getText());
                                preparedStatement.setString(2, LoginController.uname);
                                int p = preparedStatement.executeUpdate();
                                System.out.println(p);
                                if (p == 1) {
                                    String names = txtnewName.getText();
                                    System.out.println(names);
                                    int t1 = names.indexOf(' ');
                                    if (t1 != -1)
                                        LoginController.fname = names.substring(0, t1);
                                    else
                                        LoginController.fname = names;
                                    dashboard b= LoginController.refernceLoader.getController();
                                    b.refresh_userLabel();
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                    alert1.setTitle("Name Update successfull ");
                                    alert1.setHeaderText("Your name has been updated succesfully ");
                                    alert1.showAndWait();
                                    closeStage();


                                } else {
                                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                    alert1.setTitle("Name update unsuccessful ");
                                    alert1.setHeaderText("Your name cant be updated  ");
                                    alert1.showAndWait();
                                }
                                txtnewName.setText(null);

                            } catch (SQLException ex) {
                                // Alert_handler.showErrorMessage("Error","Error occurred while booking","Please try again later");
                                System.err.println(ex.getMessage());
                            }
                        }
                    });
                });
            }
        }
    }
        public void handle_enter_key_action(KeyEvent key_event) throws Exception {
            {
                if (key_event.getCode() == KeyCode.ENTER) {
                    //System.out.println(key_event.getCode());
                    handle_update_name();

                }
            }
        }
    public void closeStage()
    {
     //Stage curent= (Stage) update_name_button.getScene().getWindow();
       // curent.close();
    }
    }





