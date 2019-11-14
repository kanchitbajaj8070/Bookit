package extra_class_bookingsystem.Controllers;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXClippedPane;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.sun.org.apache.xpath.internal.axes.FilterExprWalker;
import extra_class_bookingsystem.Alert_maker.Alert_handler;

import extra_class_bookingsystem.Database.DatabaseHelper;
import javafx.animation.FillTransition;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


import javax.swing.text.Document;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.jfoenix.transitions.hamburger.*;
import javafx.stage.WindowEvent;

import static javafx.application.Application.launch;

public class dashboard  implements Initializable {
    @FXML
    private AnchorPane pane;

    @FXML
    private ImageView logo;

    @FXML
    private VBox sidepane;

    @FXML
    private Label welcome_label;

    @FXML
    private Button user_name;

    @FXML
    private Button home_button;
    @FXML
    private Button generateButton;
    @FXML
    private Button info_button;

    @FXML
    private Button password_button;

    @FXML
    private Button contact_button;

    @FXML
    private Pane title_pane;

    private Label label;
    @FXML
    private ProgressBar progressoftable;

    @FXML
    private Label loadLabel;
    @FXML
    public Button add_button;

    @FXML
    private Button delete_button;

    @FXML
    private JFXDrawer dashboardDrawer;
    @FXML
    private ProgressIndicator progressDelete;
    @FXML
    private Button modify_button;
    @FXML
    private ContextMenu contextMenuDashboard;

    @FXML
    private MenuItem deleteMenuitem;

    @FXML
    private MenuItem editMenuItem;

    @FXML
    private Circle circleImage;

    @FXML
    void add_func1(MouseEvent event) throws Exception {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/ADD_BUTTON_WINDOW.fxml"));
        primaryStage.setTitle("Date and Time Selection");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void clear_selection() {
        tableData.getSelectionModel().clearSelection();
    }

    @FXML
    private ImageView userImage;
    @FXML
    public Label USER_LABEL;

    @FXML
    private JFXHamburger dashboarHambuger;
    @FXML
    private Button imageButton;
    @FXML
    private TableView<extra_class_bookingsystem.Controllers.ModelTable> tableData;

    @FXML
    private TableColumn<extra_class_bookingsystem.Controllers.ModelTable, String> roomname;

    @FXML
    private TableColumn<extra_class_bookingsystem.Controllers.ModelTable, Date> book_date;

    @FXML
    private JFXClippedPane paneImage;
    @FXML
    private TableColumn<extra_class_bookingsystem.Controllers.ModelTable, String> timeslot;
    @FXML
    private Button sign_out;
    @FXML
    private TableColumn<extra_class_bookingsystem.Controllers.ModelTable, String> name;
    static ObservableList<extra_class_bookingsystem.Controllers.ModelTable> obList = FXCollections.observableArrayList();
    public static HashMap<Integer, String> time_slot_map_inverse = new HashMap<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ExecutorService e = Executors.newFixedThreadPool(4);
        taskint t = new taskint();
        e.submit(t);
        e.shutdown();


        progressoftable.progressProperty().bind(t.progressProperty());
        t.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("tables loaded");
                Platform.runLater(() -> {
                    progressoftable.setVisible(false);
                    loadLabel.setVisible(false);
                });
                progressoftable.progressProperty().unbind();
            }

        });
        t.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                Alert_handler.showErrorMessage("error", "failed uploading table", "Check Internet");
                progressoftable.progressProperty().unbind();
            }
        });

        String path = "resources\\uniLogo.png";
        File file = new File(path);
        System.out.println(file.getPath());
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        Image image = new Image(input);

        logo.setImage(image);

    }

    public DatabaseHelper handler = null;
    public static void main(String[] args) {
        launch(args);
    }

    static int ii = 1;

    public void handleGenerateAction(MouseEvent event) {

        Font f = new Font();
        f.setStyle(Font.BOLD);
        f.setSize(14);
        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        FileChooser fileChooser = new FileChooser();
        File fileSelected = fileChooser.showSaveDialog(LoginController.referenceStage);
        if (fileSelected == null) {
            Alert_handler.showErrorMessage("Error", "Please select a path to store the Pdf", "");
            return;

        }
        String path = fileSelected.getPath();
        if (path == null) {
            Alert_handler.showErrorMessage("Error", "Please select a path to store the Pdf", "");
            return;

        }
        if (path.length() >= 4 && path.substring(path.length() - 3, path.length()).equalsIgnoreCase("pdf"))
            System.out.println("correct");
        else {
            path = path + ".pdf";

        }
        try {

            PdfWriter.getInstance(document, new FileOutputStream(new File(path)));
            document.open();

            Paragraph p = new Paragraph();
            p.setFont(f);
            p.add("                                               Room Bookings                                         \n          User :- " + LoginController.uname + " \n          Date is :- " + LocalDate.now() + "\n\n");
            p.setAlignment(Element.ALIGN_LEFT);
            document.add(p);
            float[] pointColumnWidths = {250F, 250F, 250F, 750F};
            PdfPTable pdfPTable1 = new PdfPTable(pointColumnWidths);
            PdfPCell pdfPCell11 = new PdfPCell(new Paragraph("Book Date", f));
            PdfPCell pdfPCell21 = new PdfPCell(new Paragraph("Time Slot", f));
            PdfPCell pdfPCell31 = new PdfPCell(new Paragraph("Room Name", f));
            PdfPCell pdfPCell41 = new PdfPCell(new Paragraph("Description", f));


            pdfPTable1.addCell(pdfPCell11);
            pdfPTable1.addCell(pdfPCell21);
            pdfPTable1.addCell(pdfPCell31);
            pdfPTable1.addCell(pdfPCell41);
            pdfPTable1.setTotalWidth(1500);

            document.add(pdfPTable1);
            int size = obList.size();
            for (int i = 0; i < size; i++) {
                ModelTable obj = obList.get(i);
                PdfPTable pdfPTable = new PdfPTable(pointColumnWidths);

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                PdfPCell pdfPCell1 = new PdfPCell(new Paragraph(format.format(obj.getBook_date())));
                PdfPCell pdfPCell2 = new PdfPCell(new Paragraph(obj.getTimeslot()));
                PdfPCell pdfPCell3 = new PdfPCell(new Paragraph(obj.Roomname));
                PdfPCell pdfPCell4 = new PdfPCell(new Paragraph(obj.getName()));

                //Add cells to table
                pdfPTable.addCell(pdfPCell1);
                pdfPTable.addCell(pdfPCell2);
                pdfPTable.addCell(pdfPCell3);
                pdfPTable.addCell(pdfPCell4);
                pdfPTable.setTotalWidth(1500);
                document.add(pdfPTable);


            }


            document.close();
            Alert_handler.showInfoMessage("Success", "Pdf created succesfully", "");

            System.out.println("Done");

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void editRowFromTable(ActionEvent actionEvent) {

    }

    public class taskint extends Task<Void> {
        @Override
        protected Void call() throws Exception {
            intial_data();

            return null;
        }



        public void intial_data() {
            obList.clear();
            tableData.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
            int c = 8;

            for (int i = 0; i < DATE_TIME_SELECT.list.size(); i++) {
                time_slot_map_inverse.put(c, DATE_TIME_SELECT.list.get(i));
                c = c + 1;
            }
            updateMessage("loaded hash map ....");
            Platform.runLater(() -> loadLabel.setText("Loading Results..."));
            System.out.println(time_slot_map_inverse);
            Platform.runLater(() -> USER_LABEL.setText(LoginController.fname));
            roomname.setCellValueFactory(new PropertyValueFactory<extra_class_bookingsystem.Controllers.ModelTable, String>("roomname"));
            book_date.setCellFactory(column -> {
                TableCell<ModelTable, Date> cell = new TableCell<ModelTable, Date>() {
                    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

                    @Override
                    protected void updateItem(Date item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            this.setText(format.format(item));

                        }
                    }
                };

                return cell;
            });
            book_date.setCellValueFactory(new PropertyValueFactory<extra_class_bookingsystem.Controllers.ModelTable, Date>("book_date"));
            timeslot.setCellValueFactory(new PropertyValueFactory<extra_class_bookingsystem.Controllers.ModelTable, String>("timeslot"));
            name.setCellValueFactory(new PropertyValueFactory<extra_class_bookingsystem.Controllers.ModelTable, String>("name"));
            System.out.println("start");
    /*        try {

                Class.forName("com.mysql.jdbc.Driver");
                DriverManager.setLoginTimeout(3);
                Connection con = DriverManager.getConnection(
                        "jdbc:oracle:thin:@localhost:1521:xe", "SYSTEM", "oracle");

                ResultSet rs = con.createStatement().executeQuery("select * from booked_rooms where lower(name)='" + LoginController.uname + "';");


                while (rs.next()) {
                    Platform.runLater(()->{loadLabel.setText("uploaded "+ ii+" results");ii++;});
                    updateMessage("uploaded "+ ii+" results");
                    obList.add(new extra_class_bookingsystem.Controllers.ModelTable(rs.getString("Roomname"), rs.getDate("book_date"), rs.getString("timeslot"), rs.getString("description")));
                }
*/
            try {
                obList.addAll(handler.getInstance().intialTableData(LoginController.uname));
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            tableData.setItems(obList);

        }
    }

    public void home_window_open() throws Exception {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/home_window.fxml"));
        primaryStage.setTitle(" User Detials");
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void refresh_userLabel() {
        Platform.runLater(() -> USER_LABEL.setText(LoginController.fname));
    }

    public void handle_edit_info_button() {
        Stage primaryStage = new Stage();

        try {

            Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/edit_info_window.fxml"));
            primaryStage.setTitle("Edit Name");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.initModality(Modality.APPLICATION_MODAL);


        } catch (Exception e) {
            //Alert_handler.showErrorMessage("Error","Error occurred while booking","Please try again later");
        }
        USER_LABEL.setText(LoginController.fname);
    }

    public void contactus_window_open() throws Exception {
        Stage primaryStage = new Stage();
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/contact_window.fxml"));
            primaryStage.setTitle("Contact Us");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.initModality(Modality.APPLICATION_MODAL);

        } catch (Exception e) {

        }

    }

    public void sign_out_function() throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("You are about to sign out");
        alert.setHeaderText("Are you sure you want to proceed ?");
        alert.showAndWait().ifPresent(rs -> {
            if (rs == ButtonType.OK) {
                Stage current = (Stage) USER_LABEL.getScene().getWindow();
                LoginController.uname = null;
                LoginController.password = null;
                current.close();
                Stage primaryStage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/login.fxml"));
                    primaryStage.setTitle("bookIT-extra class booking system");
                    primaryStage.setScene(new Scene(root));
                    primaryStage.setResizable(false);
                    primaryStage.show();
                } catch (Exception e) {
                    Alert_handler.showErrorMessage("Error", "Error occurred while booking", "Please try again later");
                }
            }
        });
    }

    deleteTask delTask;

    public void deleteRowFromTable() {
        ExecutorService service = Executors.newFixedThreadPool(2);

        delTask = new deleteTask();

        progressDelete.visibleProperty().bind(delTask.runningProperty());
        loadLabel.visibleProperty().bind(delTask.runningProperty());
        service.execute(delTask);
        service.shutdown();


        delTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("failed in deletion");
                Alert_handler.showErrorMessage("Error", "Deletion not succesful", "Check Internet");
            }
        });
        delTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {


            }
        });
    }

    public class deleteTask extends Task<Boolean> {
        Boolean result = false;

        @Override
        protected Boolean call() throws Exception {
            delete_row_from_table();

            return result;
        }


        public void delete_row_from_table() {
            extra_class_bookingsystem.Controllers.ModelTable row_to_delete = tableData.getSelectionModel().getSelectedItem();
            if (row_to_delete == null)
                Alert_handler.showErrorMessage("No Booking Selected", " Please  select a entry from table", "");
            if (row_to_delete != null) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Selected Booking");
                    alert.setHeaderText("Are you sure you want to delete selected entry from booked_rooms table?");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {

                            try {

                                int p =handler.getInstance().deleteRow(row_to_delete.getBook_date(),row_to_delete.Roomname,row_to_delete.getTimeslot(),LoginController.uname);
                                if (p == 1) {
                                    Platform.runLater(() -> {
                                        Alert_handler.showInfoMessage("Deletion Successful", "The entry was succesfully deleted ", "");
                                    });
                                    result = true;
                                }


                            } catch (Exception e) {
                                Alert_handler.showErrorMessage("Error", "Error occurred while booking", "Please try again later");
                                result = false;
                            }
                            Platform.runLater(() -> tableData.getItems().removeAll(row_to_delete));
                        }
                    });
                });
            }
        }
    }

    public void change_password_window_open() throws Exception
    {
        Stage primaryStage= new Stage();
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/extra_class_bookingsystem/fxmls/change_password_window.fxml"));
            primaryStage.setTitle("Change Password");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.initModality(Modality.APPLICATION_MODAL);

        } catch (Exception e) {

            System.out.println(e.getCause());
        }

    }

}







