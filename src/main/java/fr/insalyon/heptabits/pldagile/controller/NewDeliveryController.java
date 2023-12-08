package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.Client;
import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.TimeWindow;
import fr.insalyon.heptabits.pldagile.repository.ClientRepository;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import fr.insalyon.heptabits.pldagile.repository.TimeWindowRepository;
import fr.insalyon.heptabits.pldagile.service.MapService;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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


    // Initialize the list of couriers in your controller's initialize method or constructor.
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

        // Add the map to the mapAnchorPanechosenTime
        final MapService mapService = dependencyManager.getMapService();

        MapView.OnIntersectionClicked onIntersectionClicked = intersection -> {
            chosenIntersection = intersection;
            intersectionTextField.setText(chosenIntersection.toString());
            System.out.println("Intersection clicked: " + intersection);

        };
        final MapView mapView = new MapView(mapService.getCurrentMap(), 500, onIntersectionClicked);
        mapAnchorPane.getChildren().add(mapView.createView());

        // Afficher l'intersection sélectionnée différemment
        intersectionTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            long oldId = (oldValue.split("[=,]").length >= 2) ? Long.parseLong(oldValue.split("[=,]")[1].trim()) : -1;
            long newId = (newValue.split("[=,]").length >= 2) ? Long.parseLong(newValue.split("[=,]")[1].trim()) : -1;

            // afficher la nouvelle intersection en vert et plus grosse
            if (newId != -1) {
                Circle newIntersectionCircle = mapView.getDeliveryCircleMap().get(newId);
                if (newIntersectionCircle != null) {
                    newIntersectionCircle.setStroke(Color.web("#119156"));
                    newIntersectionCircle.setStrokeWidth(1);
                    newIntersectionCircle.setFill(Color.web("#18c474"));

                    newIntersectionCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                        newIntersectionCircle.setFill(Color.web("#119156"));
                        onIntersectionClicked.onIntersectionClicked(mapService.getCurrentMap().getIntersections().get(newId));
                    });

                    newIntersectionCircle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                        newIntersectionCircle.setFill(Color.web("#18c474"));
                    });

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
                Circle oldIntersectionCircle = mapView.getDeliveryCircleMap().get(oldId);
                if (oldIntersectionCircle != null) {
                    oldIntersectionCircle.setStrokeWidth(0);
                    oldIntersectionCircle.setFill(Color.web("de1c24"));
                    oldIntersectionCircle.setScaleX(1);
                    oldIntersectionCircle.setScaleY(1);

                    oldIntersectionCircle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
                        oldIntersectionCircle.setFill(Color.web("#ab151b"));
                        onIntersectionClicked.onIntersectionClicked(mapService.getCurrentMap().getIntersections().get(oldId));
                    });

                    oldIntersectionCircle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
                        oldIntersectionCircle.setFill(Color.web("de1c24"));
                    });

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


        confirmNewDeliveryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            confirmNewDeliveryButton.setStyle("-fx-background-color: #00BCAD");
        });
        confirmNewDeliveryButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            confirmNewDeliveryButton.setStyle("-fx-background-color: #00CCBC");
        });
        confirmNewDeliveryButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            confirmNewDeliveryButton.setStyle("-fx-background-color: #00A093");
        });

        addNewClientButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            addNewClientButton.setStyle("-fx-background-color: #00BCAD");
        });
        addNewClientButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            addNewClientButton.setStyle("-fx-background-color: #00CCBC");
        });
        addNewClientButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            addNewClientButton.setStyle("-fx-background-color: #00A093");
        });

        addNewCourierButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            addNewCourierButton.setStyle("-fx-background-color: #00BCAD");
        });
        addNewCourierButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            addNewCourierButton.setStyle("-fx-background-color: #00CCBC");
        });
        addNewCourierButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            addNewCourierButton.setStyle("-fx-background-color: #00A093");
        });

        return_button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            return_button.setStyle("-fx-background-color: #00BCAD");
        });
        return_button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            return_button.setStyle("-fx-background-color: #00CCBC");
        });
        return_button.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            return_button.setStyle("-fx-background-color: #00A093");
        });

    }

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
        final boolean isValid;
        LocalTime chosenTime = null;
        LocalDateTime chosenDateTime = null;
        final Courier chosenCourier = courierChoiceBox.getValue();
        final Client chosenClient = clientChoiceBox.getValue();
        final LocalDate chosenDate = datePicker.getValue();

        if(timeWindowChoiceBox.getValue() != null){ // TODO in the future, use TimeWindow to create a DeliveryRequest
            chosenTime = timeWindowChoiceBox.getValue().getStart();
            chosenDateTime = LocalDateTime.of(chosenDate, chosenTime);
            isValid = chosenCourier != null && chosenClient != null  && chosenIntersection != null;
        } else {
            isValid = false;
        }

        if (!isValid) {
            System.out.println("Invalid delivery");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Validation échouée");
            alert.setContentText("Veuillez remplir tous les champs");

            alert.showAndWait();
            return;
        }

        // TODO check if delivery is possible, and if so, add it to the courier's roadmap
        dependencyManager.getDeliveryRepository().create(chosenDateTime, chosenIntersection, chosenCourier.getId(), chosenClient.getId(), timeWindowChoiceBox.getValue());
        System.out.println("New delivery created");

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
