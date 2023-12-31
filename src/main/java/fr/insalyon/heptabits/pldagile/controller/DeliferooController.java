package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.DeliferooApplication;
import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 * Controller for the main view.
 * <p>
 * This controller is responsible for handling the events on the main view.
 * <p>
 * The main view allows the user to select a date and displays the deliveries for this date.
 * <p>
 * The main view also displays a map with the roadmaps.
 */
public class DeliferooController {

    private final DependencyManager dependencyManager;
    private final Color hoveredColor = Color.web("#00BCAD");

    private MapView mapView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Delivery> deliveryTable;

    @FXML
    private TableColumn<Delivery, String> courierName;
    @FXML
    private TableColumn<Delivery, String> address;
    @FXML
    private TableColumn<Delivery, String> time;

    @FXML
    private TableColumn<Delivery, TimeWindow> timeWindow;

    @FXML
    private TableColumn<Delivery, String> clientName;
    @FXML
    private StackPane mapContainer;
    @FXML
    private ImageView logo;
    @FXML
    private Button newDeliveryButton;

    @FXML
    private Button viewRoadMaps;


    public DeliferooController(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    int mapSizeX = 500;
    int mapSizeY = 500;

    @FXML
    public void initialize() {
        File logoFile = new File("src/main/resources/img/del'iferoo-white 1.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logo.setImage(logoImage);

        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(e -> {
            updateDeliveriesTable();
            updateMap();
        });

        updateMap();
        updateDeliveriesTable();

        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> newDeliveryButton.setStyle("-fx-background-color: #00BCAD"));
        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> newDeliveryButton.setStyle("-fx-background-color: #00CCBC"));
        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> newDeliveryButton.setStyle("-fx-background-color: #00A093"));

        viewRoadMaps.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> viewRoadMaps.setStyle("-fx-background-color: #00BCAD"));
        viewRoadMaps.addEventHandler(MouseEvent.MOUSE_EXITED, e -> viewRoadMaps.setStyle("-fx-background-color: #00CCBC"));
        viewRoadMaps.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> viewRoadMaps.setStyle("-fx-background-color: #00A093"));
    }

    public void updateMap() {
        Map map = dependencyManager.getMapService().getCurrentMap();
        List<RoadMap> roadMaps = dependencyManager.getRoadMapRepository().getByDate(datePicker.getValue());
        mapView = new MapView(map, mapSizeX, mapSizeY, null, roadMaps);
        Group mapGroup = mapView.createView();

        mapContainer.getChildren().clear(); // Clear existing content if necessary
        mapContainer.getChildren().add(mapGroup); // Add the map to the pane
    }

    @FXML
    public void updateDeliveriesTable() {
        if (!deliveryTable.getItems().isEmpty()) {
            deliveryTable.getItems().clear();
        }

        LocalDate date = datePicker.getValue();
        List<Delivery> deliveries = dependencyManager.getDeliveryService().getDeliveriesOnDate(date);

        List<RoadMap> roadMaps = dependencyManager.getRoadMapRepository().getByDate(date);

        if (roadMaps.isEmpty()) {
            return;
        }


        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HH:mm");
        address.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().destination().latLongPrettyPrint()));
        courierName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(dependencyManager.getCourierRepository().findById(cellData.getValue().courierId()).getFirstName() + " " + dependencyManager.getCourierRepository().findById(cellData.getValue().courierId()).getLastName()));
        clientName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(dependencyManager.getClientRepository().findById(cellData.getValue().clientId()).getFirstName() + " " + dependencyManager.getClientRepository().findById(cellData.getValue().clientId()).getLastName()));
        time.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().scheduledDateTime().format(dtFormatter)));
        timeWindow.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().timeWindow()));
        for (Delivery d : deliveries) {
            deliveryTable.getItems().addAll(d);

            // rendre chaque ligne du tableau hoverable
            deliveryTable.setRowFactory(tv -> {
                TableRow<Delivery> row = new TableRow<>();
                row.setOnMouseEntered(event -> {
                    if (!row.isEmpty()) {
                        // livraison associée à la ligne
                        long selectedDeliveryIntersectionId = row.getItem().destination().getId();
                        mapView.onDeliveryHovered(selectedDeliveryIntersectionId);

                    }
                });
                row.setOnMouseExited(event -> {
                    if (!row.isEmpty()) {
                        // livraison associée à la ligne
                        long selectedDeliveryIntersectionId = row.getItem().destination().getId();
                        mapView.onDeliveryExited(selectedDeliveryIntersectionId);
                    }
                });
                return row;
            });
        }
    }

    @FXML
    protected void onViewRoadMapsButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage oldStage = (Stage) source.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(DeliferooApplication.class.getResource("couriersList.fxml"));
        fxmlLoader.setController(new CouriersListController(dependencyManager, oldStage));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onNewDeliveryButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new NewDeliveryController(dependencyManager));
        fxmlLoader.setLocation(DeliferooApplication.class.getResource("NewDelivery.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void exporterMenuItemClicked(ActionEvent event) throws IOException {
        if (!(event.getSource() instanceof MenuItem menuItem)) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier XML");

        // Only show XML files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));

        // Show the dialog and wait for the user to select a file
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile == null) {
            return;
        }


        FileWriter fileWriter = new FileWriter(selectedFile);

        dependencyManager.getXmlRoadMapService().exportRoadMapsToXml(fileWriter);


    }

    @FXML
    private void importerMenuItemClicked(ActionEvent event) throws IOException, SAXException {
        if (!(event.getSource() instanceof MenuItem menuItem)) {
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier XML");

        // Only show XML files
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));
        fileChooser.setInitialFileName("roadmaps.xml");

        // Show the dialog and wait for the user to select a file
        Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);


        if (selectedFile == null) {
            return;
        }

        dependencyManager.getXmlRoadMapService().importRoadMapsFromXml(selectedFile);
        updateDeliveriesTable();
        updateMap();
    }
}