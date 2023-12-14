package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class RoadMapController {

    private final DependencyManager dependencyManager;
    private final RoadMapRepository roadMapRepository;
    private MapView mapView;
    private Courier courier;
    private LocalDate chosenDate;
    @FXML
    private StackPane mapContainer;
    @FXML
    private Label courierName;
    @FXML
    private Label date;

    @FXML
    private TextArea courierItinirary;

    public RoadMapController(DependencyManager dependencyManager, Courier courier, LocalDate chosenDate) {
        this.dependencyManager = dependencyManager;
        this.chosenDate = chosenDate;
        this.courier = courier;
        this.roadMapRepository = dependencyManager.getRoadMapRepository();
    }

    public void initialize(){
        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500);
        courierName.setText(courier.getFirstName() + " " + courier.getLastName() + " :");
        date.setText(chosenDate.toString());
        initializeRoadMap();


    }


    public void initializeRoadMap(){
        List<Delivery> deliveries = roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate).getDeliveries();
        //System.out.print(deliveries);
        List<Leg> legs = roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate).getLegs();
        String itinerary = " ";
        for (int i = 0; i<legs.size(); i++){
            if(i == 0){
                itinerary += (i+1) + "ère étape\n";
            } else {
                itinerary += (i+1) + "ème étape\n";
            }
            List<Segment> segments = legs.get(i).segments();
            for(int j = 0; j<segments.size(); j++){
                if(!segments.get(j).name().isEmpty()){
                    String route = segments.get(j).name();
                    System.out.println(route);
                    int counter = getConsecutiveNumber(route, segments, j);
                    itinerary += " - " + segments.get(j).name() + " (" + counter + " intersections)\n";
                    j += counter;
                }
            }
        }
        courierItinirary.setText(itinerary);


    }

    public int getConsecutiveNumber(String name, List<Segment> segments, int i){
        int counter = 1;
        for(int j = i+1; j<segments.size(); j++){
            if(name.equals(segments.get(j).name())) counter++;
            else break;
        }
        return counter;
    }

    public void initializeMap(Map map, int width) {
        mapView = new MapView(map, width, null);
        Group mapGroup = mapView.createView(roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate));

        mapContainer.getChildren().clear(); // Clear existing content if necessary
        mapContainer.getChildren().add(mapGroup); // Add the map to the pane
    }

    @FXML
    protected void onReturnButtonClick(InputEvent e) throws IOException {
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

    @FXML
    protected void generateRoadMapToPDFButton(ActionEvent e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        String itinerary = courierItinirary.getText();

        // Créer un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");

        // Ajouter un filtre pour ne montrer que les fichiers PDF
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers ¨PDF", "*.pdf"));

        // Afficher la boîte de dialogue et attendre que l'utilisateur sélectionne un fichier
        File selectedFile = fileChooser.showSaveDialog(stage);

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            // Faites quelque chose avec le fichier sélectionné

            System.out.println("Fichier PDF sélectionné : " + selectedFile.getAbsolutePath());
            // À partir d'ici, vous pouvez traiter le fichier PDF comme nécessaire
            // (par exemple, lire son contenu, analyser les données, etc.)
        } else {
            // L'utilisateur a annulé la sélection
            System.out.println("Sélection de fichier annulée.");
        }

    }


}
