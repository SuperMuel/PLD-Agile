package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;


import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class HelloController {

    private  final DependencyManager dependencyManager;
    private  final Color hoveredColor = Color.web("#00BCAD");

    @FXML
    private TableView<Delivery> deliveryTable;
    @FXML
    private TableColumn<Delivery, Long> deliveryId;
    @FXML
    private TableColumn<Delivery, String> courierName;
    @FXML
    private TableColumn<Delivery, Intersection> address;
    @FXML
    private TableColumn<Delivery, String> time;
    @FXML
    private TableColumn<Delivery, String> clientName;
    @FXML
    private StackPane mapContainer;
    @FXML
    private ImageView logo;
    @FXML
    private Button newDeliveryButton;
    @FXML
    private Button fileButton;

    public HelloController(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    @FXML
    public void initialize() {
        File file = new File("src/main/resources/img/del'iferoo-white 1.png");
        Image image = new Image(file.toURI().toString());
        logo.setImage(image);
        displayDeliveries();
        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500);

        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            newDeliveryButton.setStyle("-fx-background-color: #00BCAD");
        });
        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            newDeliveryButton.setStyle("-fx-background-color: #00CCBC");
        });
        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            newDeliveryButton.setStyle("-fx-background-color: #00A093");
        });

        fileButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            fileButton.setStyle("-fx-background-color: #00BCAD");
        });
        fileButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            fileButton.setStyle("-fx-background-color: #00CCBC");
        });
        fileButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            fileButton.setStyle("-fx-background-color: #00A093");
        });

    }

    public void initializeMap(Map map, int width) {
        MapView mapView = new MapView(map, width);
        Group mapGroup = mapView.createView();

        mapContainer.getChildren().clear(); // Clear existing content if necessary
        mapContainer.getChildren().add(mapGroup); // Add the map to the pane
    }

    @FXML
    public void displayDeliveries(){
        List<Delivery> deliveries = dependencyManager.getDeliveryRepository().findAll();
        if(deliveries.isEmpty()){
            System.out.println("Pas de livraisons prévues");
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            deliveryId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
            address.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDestination()));
            courierName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(dependencyManager.getCourierRepository().findById(cellData.getValue().getCourierId()).getFirstName() + " " + dependencyManager.getCourierRepository().findById(cellData.getValue().getCourierId()).getLastName()));
            clientName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(dependencyManager.getClientRepository().findById(cellData.getValue().getClientId()).getFirstName() + " " + dependencyManager.getClientRepository().findById(cellData.getValue().getClientId()).getLastName()));
            time.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getScheduledDateTime().format(formatter)));
            for(Delivery d : deliveries){
                deliveryTable.getItems().addAll(d);
            }
        }
    }

    @FXML
    protected void onNewDeliveryButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new NewDeliveryController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewDelivery.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("INFAT'IFGABLES");
        stage.setScene(scene);
        stage.show();
    }
}