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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private ChoiceBox<Intersection> intersectionChoiceBox;

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
            intersectionChoiceBox.setValue(intersection);
            System.out.println("Intersection clicked: " + intersection);
        };
        final MapView mapView = new MapView(mapService.getCurrentMap(), 500, onIntersectionClicked);
        mapAnchorPane.getChildren().add(mapView.createView());

    }


    public void onNewClientButtonClick(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewClient.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("INFAT'IFGABLES");
        stage.setScene(scene);
        stage.show();
    }

    public void onNewCourierButtonClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewCourier.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("INFAT'IFGABLES");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onValidateNewDeliveryButtonClick(InputEvent e) throws IOException {
        final Courier chosenCourier = courierChoiceBox.getValue();
        final Client chosenClient = clientChoiceBox.getValue();
        final LocalDate chosenDate = datePicker.getValue();
        final LocalTime chosenTime = timeWindowChoiceBox.getValue().getStart(); // TODO in the future, use TimeWindow to create a DeliveryRequest

        final LocalDateTime chosenDateTime = LocalDateTime.of(chosenDate, chosenTime);

        final boolean isValid = chosenCourier != null && chosenClient != null  && chosenIntersection != null;

        if (!isValid) {
            System.out.println("Invalid delivery");
            return;
        }
        // TODO check if delivery is possible, and if so, add it to the courier's roadmap
        dependencyManager.getDeliveryRepository().create(chosenDateTime, chosenIntersection, chosenCourier.getId());
        System.out.println("New delivery created");

        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new HelloController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }
}
