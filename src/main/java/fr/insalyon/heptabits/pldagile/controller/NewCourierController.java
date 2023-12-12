package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.Courier;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller to handle the creation of a new courier.
 */
public class NewCourierController {
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private final DependencyManager dependencyManager;
    @FXML
    private ImageView logo;
    @FXML
    private Button returnButton;
    @FXML
    private Button confirmNewCourierButton;

    public NewCourierController(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {

        confirmNewCourierButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> confirmNewCourierButton.setStyle("-fx-background-color: #00BCAD"));
        confirmNewCourierButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> confirmNewCourierButton.setStyle("-fx-background-color: #00CCBC"));
        confirmNewCourierButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> confirmNewCourierButton.setStyle("-fx-background-color: #00A093"));

        returnButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> returnButton.setStyle("-fx-background-color: #00A093"));

    }

    /**
     * Handles the click on the validate new courier button.
     *
     * @param e the event
     * @throws IOException if the next view cannot be loaded
     */
    @FXML
    protected void onValidateNewCourierButtonClick(InputEvent e) throws IOException {
        final String lastName = lastNameField.getText();
        final String firstName = firstNameField.getText();
        final String phoneNumber = phoneNumberField.getText();
        final String email = emailField.getText();

        final boolean isValid = lastName != null && firstName != null  && phoneNumber != null && email != null;

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Validation échouée");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }

        Courier courier = dependencyManager.getCourierRepository().create(firstName, lastName, email, phoneNumber);
        System.out.println("New Courier created");

        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new NewDeliveryController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewDelivery.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the click on the return button.
     *
     * @param e the event
     * @throws IOException if the next view cannot be loaded
     */
    public void onReturnButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new NewDeliveryController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("newDelivery.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("del'IFeroo");
        stage.setScene(scene);
        stage.show();
    }
}
