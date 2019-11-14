package extra_class_bookingsystem;
import com.sun.org.apache.xpath.internal.operations.Bool;
import extra_class_bookingsystem.Alert_maker.Alert_handler;
import extra_class_bookingsystem.Database.DatabaseHelper;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jdk.nashorn.internal.codegen.CompilerConstants;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.*;

public class Main extends Application {
    private DatabaseHelper handler=null;
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        handler=handler.getInstance();
        Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/login.fxml"));
        primaryStage.setTitle("bookIT - Extra Class Booking System");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    public static void main(String args[]) {

        launch(args);
    }
}
