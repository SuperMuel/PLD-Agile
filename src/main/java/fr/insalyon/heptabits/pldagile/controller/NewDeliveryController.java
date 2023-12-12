package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.ClientRepository;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import fr.insalyon.heptabits.pldagile.repository.TimeWindowRepository;
import fr.insalyon.heptabits.pldagile.service.ImpossibleRoadMapException;
import fr.insalyon.heptabits.pldagile.service.MapService;
import fr.insalyon.heptabits.pldagile.service.RoadMapService;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class NewDeliveryController {

    public ChoiceBox<TimeWindow> timeWindowChoiceBox;
    @FXML
    private ChoiceBox<Courier> courierChoiceBox;
    @FXML
    private ChoiceBox<Client> clientChoiceBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private AnchorPane mapAnchorPane;
    @FXML
    private TextField intersectionTextField;
    @FXML
    private ImageView logo;
    @FXML
    private Button return_button;
    @FXML
    private Button confirmNewDeliveryButton;
    @FXML
    private Button addNewClientButton;
    @FXML
    private Button addNewCourierButton;
    private final DependencyManager dependencyManager;

    private Intersection chosenIntersection;

    public NewDeliveryController(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }


    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        // Add the courier names to the ChoiceBox
        final CourierRepository courierRepository = dependencyManager.getCourierRepository();
        List<Courier> couriers = courierRepository.findAll();
        courierChoiceBox.getItems().addAll(couriers);

        // Add the client names to the ChoiceBox
        final ClientRepository clientRepository = dependencyManager.getClientRepository();
        clientChoiceBox.getItems().addAll(clientRepository.findAll());

        // Set datepicker to today's date
        datePicker.setValue(java.time.LocalDate.now());

        // Set time window choice box
        final TimeWindowRepository timeWindowRepository = dependencyManager.getTimeWindowRepository();
        timeWindowChoiceBox.getItems().addAll(timeWindowRepository.getAll());

        final MapService mapService = dependencyManager.getMapService();

        MapView.OnIntersectionClicked onIntersectionClicked = intersection -> {
            chosenIntersection = intersection;
            intersectionTextField.setText(chosenIntersection.toString());
        };


        final MapView mapView = new MapView(mapService.getCurrentMap(), 500, onIntersectionClicked);
        mapAnchorPane.getChildren().add(mapView.createView());

        // Afficher l'intersection sélectionnée différemment
        intersectionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            long oldId = (oldValue.split("[=,]").length >= 2) ? Long.parseLong(oldValue.split("[=,]")[1].trim()) : -1;
            long newId = (newValue.split("[=,]").length >= 2) ? Long.parseLong(newValue.split("[=,]")[1].trim()) : -1;

            // afficher la nouvelle intersection en vert et plus grosse
            if (newId != -1) {
                Circle newIntersectionCircle = mapView.getCircleFromIntersectionId(newId);
                if (newIntersectionCircle != null) {
                    newIntersectionCircle.setStroke(Color.web("#119156"));
                    newIntersectionCircle.setStrokeWidth(1);
                    newIntersectionCircle.setFill(Color.web("#18c474"));

                    newIntersectionCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                        newIntersectionCircle.setFill(Color.web("#119156"));
                        onIntersectionClicked.onIntersectionClicked(mapService.getCurrentMap().getIntersections().get(newId));
                    });

                    newIntersectionCircle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> newIntersectionCircle.setFill(Color.web("#18c474")));

                    ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.15), newIntersectionCircle);
                    scaleOut.setToX(2);
                    scaleOut.setToY(2);

                    newIntersectionCircle.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                        scaleOut.playFromStart(); // Play exit animation
                        newIntersectionCircle.setOpacity(0.5);
                    });
                }
            }
            // réafficher l'intersection sélectionnée précédemment de façon normale
            if (oldId != -1) {
                Circle oldIntersectionCircle = mapView.getCircleFromIntersectionId(oldId);
                if (oldIntersectionCircle != null) {
                    oldIntersectionCircle.setStrokeWidth(0);
                    oldIntersectionCircle.setFill(Color.web("de1c24"));
                    oldIntersectionCircle.setScaleX(1);
                    oldIntersectionCircle.setScaleY(1);

                    oldIntersectionCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                        oldIntersectionCircle.setFill(Color.web("#ab151b"));
                        onIntersectionClicked.onIntersectionClicked(mapService.getCurrentMap().getIntersections().get(oldId));
                    });

                    oldIntersectionCircle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> oldIntersectionCircle.setFill(Color.web("de1c24")));

                    ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.15), oldIntersectionCircle);
                    scaleOut.setToX(1);
                    scaleOut.setToY(1);

                    oldIntersectionCircle.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                        scaleOut.playFromStart(); // Play exit animation
                        oldIntersectionCircle.setOpacity(0.5);
                    });
                }
            }
        });


        confirmNewDeliveryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> confirmNewDeliveryButton.setStyle("-fx-background-color: #00BCAD"));
        confirmNewDeliveryButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> confirmNewDeliveryButton.setStyle("-fx-background-color: #00CCBC"));
        confirmNewDeliveryButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> confirmNewDeliveryButton.setStyle("-fx-background-color: #00A093"));

        addNewClientButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> addNewClientButton.setStyle("-fx-background-color: #00BCAD"));
        addNewClientButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> addNewClientButton.setStyle("-fx-background-color: #00CCBC"));
        addNewClientButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> addNewClientButton.setStyle("-fx-background-color: #00A093"));

        addNewCourierButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> addNewCourierButton.setStyle("-fx-background-color: #00BCAD"));
        addNewCourierButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> addNewCourierButton.setStyle("-fx-background-color: #00CCBC"));
        addNewCourierButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> addNewCourierButton.setStyle("-fx-background-color: #00A093"));

        return_button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> return_button.setStyle("-fx-background-color: #00BCAD"));
        return_button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> return_button.setStyle("-fx-background-color: #00CCBC"));
        return_button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> return_button.setStyle("-fx-background-color: #00A093"));

    }


    /**
     * Handles the click on the return button.
     *
     * @param e the event
     * @throws IOException if an error occurs while loading the next view
     */
    @FXML
    public void onReturnButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new HelloController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("del'IFeroo");
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Handles the click on the add new client button.
     *
     * @param e the event
     * @throws IOException if an error occurs while loading the next view
     */
    @FXML
    public void onNewClientButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new NewClientController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewClient.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("del'IFeroo");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Handles the click on the add new courier button.
     *
     * @param e the event
     * @throws IOException if an error occurs while loading the next view
     */
    @FXML
    public void onNewCourierButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new NewCourierController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewCourier.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("del'IFeroo");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onValidateNewDeliveryButtonClick(InputEvent e) throws IOException {
        final Courier chosenCourier = courierChoiceBox.getValue();
        final Client chosenClient = clientChoiceBox.getValue();
        final LocalDate chosenDate = datePicker.getValue();
        final TimeWindow chosenTimeWindow = timeWindowChoiceBox.getValue();

        //TODO : check if intersection is not the warehouse. Or make the warehouse not clickable. Or both.
        boolean isValid = chosenCourier != null && chosenClient != null && chosenDate != null && chosenTimeWindow != null && chosenIntersection != null;

        if (!isValid) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Validation échouée");
            alert.setContentText("Veuillez remplir tous les champs.");

            alert.showAndWait();
            return;
        }

        DeliveryRequest deliveryRequest = new DeliveryRequest(chosenDate, chosenClient.getId(), chosenIntersection, chosenTimeWindow, chosenCourier.getId());
        RoadMapService roadMapService = dependencyManager.getRoadMapService();

        try {
            roadMapService.addRequest(deliveryRequest);
        } catch (ImpossibleRoadMapException exception) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Validation échouée");
            alert.setContentText("Impossible de créer une tournée avec ces paramètres.\n Veuillez choisir un autre livreur ou une autre plage horaire.");
            alert.showAndWait();
            return;
        }

        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new HelloController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("del'IFeroo");
        stage.setScene(scene);
        stage.show();
    }
}
