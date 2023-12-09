package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.Delivery;
import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.animation.ScaleTransition;
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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class HelloController {

    private final DependencyManager dependencyManager;
    private final Color hoveredColor = Color.web("#00BCAD");

    private MapView mapView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableView<Delivery> deliveryTable;
    @FXML
    private TableColumn<Delivery, Long> roadMapId;
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
        File logoFile = new File("src/main/resources/img/del'iferoo-white 1.png");
        Image logoImage = new Image(logoFile.toURI().toString());
        logo.setImage(logoImage);

        datePicker.setValue(LocalDate.now());
        datePicker.setOnAction(e -> displayDeliveries());

        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500);
        displayDeliveries();

        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> newDeliveryButton.setStyle("-fx-background-color: #00BCAD"));
        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> newDeliveryButton.setStyle("-fx-background-color: #00CCBC"));
        newDeliveryButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> newDeliveryButton.setStyle("-fx-background-color: #00A093"));

    }

    public void initializeMap(Map map, int width) {
        mapView = new MapView(map, width);
        Group mapGroup = mapView.createView();

        mapContainer.getChildren().clear(); // Clear existing content if necessary
        mapContainer.getChildren().add(mapGroup); // Add the map to the pane
    }

    @FXML
    public void displayDeliveries() {
        if (!deliveryTable.getItems().isEmpty()) {
            deliveryTable.getItems().clear();
        }

        LocalDate date = datePicker.getValue();
        List<Delivery> deliveries = dependencyManager.getDeliveryService().getDeliveriesOnDate(date);

        List<RoadMap> roadMaps = dependencyManager.getRoadMapRepository().getByDate(date);

        if (roadMaps.isEmpty()) {
            System.out.println("Pas de livraison prévue");
            return;
        }


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        roadMapId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        address.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDestination()));
        courierName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(dependencyManager.getCourierRepository().findById(cellData.getValue().getCourierId()).getFirstName() + " " + dependencyManager.getCourierRepository().findById(cellData.getValue().getCourierId()).getLastName()));
        clientName.setCellValueFactory(cellData -> new SimpleObjectProperty<>(dependencyManager.getClientRepository().findById(cellData.getValue().getClientId()).getFirstName() + " " + dependencyManager.getClientRepository().findById(cellData.getValue().getClientId()).getLastName()));
        time.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getScheduledDateTime().format(formatter)));
        for (Delivery d : deliveries) {
            deliveryTable.getItems().addAll(d);

            // rendre chaque ligne du tableau hoverable
            deliveryTable.setRowFactory(tv -> {
                TableRow<Delivery> row = new TableRow<>();
                row.setOnMouseEntered(event -> {
                    if (!row.isEmpty()) {
                        // livraison associée à la ligne
                        long selectedDeliveryIntersectionId = row.getItem().getDestination().getId();
                        Circle c = mapView.getDeliveryCircleMap().get(selectedDeliveryIntersectionId);

                        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.10), c);
                        scaleIn.setToX(5);
                        scaleIn.setToY(5);

                        scaleIn.playFromStart();
                        c.setFill(Color.web("#18c474"));
                        c.setOpacity(1);
                    }
                });
                row.setOnMouseExited(event -> {
                    if (!row.isEmpty()) {
                        // livraison associée à la ligne
                        long selectedDeliveryIntersectionId = row.getItem().getDestination().getId();
                        Circle c = mapView.getDeliveryCircleMap().get(selectedDeliveryIntersectionId);

                        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.10), c);
                        scaleOut.setToX(1);
                        scaleOut.setToY(1);

                        scaleOut.playFromStart();
                        c.setFill(Color.web("#de1c24"));
                        c.setOpacity(0.5);
                    }
                });
                return row;
            });
        }

    }

    void addRoadMap(RoadMap roadMap) {
        // TODO : add the road map to the table
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
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void importerMenuItemClicked(ActionEvent event) throws IOException, SAXException {
        // Assurez-vous que l'événement provient bien d'un MenuItem
        if (event.getSource() instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) event.getSource();

            // Créer un FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier XML");

            // Ajouter un filtre pour ne montrer que les fichiers XML
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));

            // Afficher la boîte de dialogue et attendre que l'utilisateur sélectionne un fichier
            Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
            File selectedFile = fileChooser.showOpenDialog(stage);

            // Vérifier si un fichier a été sélectionné
            if (selectedFile != null) {
                // Faites quelque chose avec le fichier sélectionné
                System.out.println("Fichier XML sélectionné : " + selectedFile.getAbsolutePath());
                dependencyManager.getXmlDeliveriesService().importDeliveriesFromXml(selectedFile.getAbsolutePath());
                displayDeliveries();

            } else {
                // L'utilisateur a annulé la sélection
                System.out.println("Sélection de fichier annulée.");
            }
        }
    }


    @FXML
    private void exporterMenuItemClicked(ActionEvent event) {
        // Assurez-vous que l'événement provient bien d'un MenuItem
        if (event.getSource() instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) event.getSource();

            // Créer un FileChooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner un fichier XML");

            // Ajouter un filtre pour ne montrer que les fichiers XML
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers XML", "*.xml"));

            // Afficher la boîte de dialogue et attendre que l'utilisateur sélectionne un fichier
            Stage stage = (Stage) menuItem.getParentPopup().getOwnerWindow();
            File selectedFile = fileChooser.showSaveDialog(stage);

            // Vérifier si un fichier a été sélectionné
            if (selectedFile != null) {
                // Faites quelque chose avec le fichier sélectionné
                System.out.println("Fichier XML sélectionné : " + selectedFile.getAbsolutePath());
                dependencyManager.getXmlDeliveriesService().exportDeliveriesToXml(selectedFile.getAbsolutePath());

                // À partir d'ici, vous pouvez traiter le fichier XML comme nécessaire
                // (par exemple, lire son contenu, analyser les données, etc.)
            } else {
                // L'utilisateur a annulé la sélection
                System.out.println("Sélection de fichier annulée.");
            }
        }

    }
}