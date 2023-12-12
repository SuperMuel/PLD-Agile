package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
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
 * Controller to handle the creation of a new client.
 */
public class NewClientController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private final DependencyManager dependencyManager;
    @FXML
    private ImageView logo;
    @FXML
    private Button returnButton;
    @FXML
    private Button confirmNewClientButton;

    public NewClientController(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    /**
     * Initializes the controller.
     */
    public void initialize() {

        confirmNewClientButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> confirmNewClientButton.setStyle("-fx-background-color: #00BCAD"));
        confirmNewClientButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> confirmNewClientButton.setStyle("-fx-background-color: #00CCBC"));
        confirmNewClientButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> confirmNewClientButton.setStyle("-fx-background-color: #00A093"));

        returnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> returnButton.setStyle("-fx-background-color: #00BCAD"));
        returnButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> returnButton.setStyle("-fx-background-color: #00CCBC"));
        returnButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> returnButton.setStyle("-fx-background-color: #00A093"));


    }

    /**
     * Handles the click on the validate new client button.
     *
     * @param e the event
     * @throws IOException if an error occurs while loading the next view
     */
    @FXML
    protected void onValidateNewClientButtonClick(InputEvent e) throws IOException {
        final String lastName = lastNameField.getText();
        final String firstName = firstNameField.getText();
        final String phoneNumber = phoneNumberField.getText();

        final boolean isValid = lastName != null && firstName != null  && phoneNumber != null;

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Validation échouée");
            alert.setContentText("Veuillez remplir tous les champs");
            alert.showAndWait();
            return;
        }

        dependencyManager.getClientRepository().create(firstName, lastName, phoneNumber);

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
     * @throws IOException if an error occurs while loading the next view
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
