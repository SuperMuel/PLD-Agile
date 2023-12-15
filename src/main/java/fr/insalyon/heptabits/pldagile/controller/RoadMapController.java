package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.DeliferooApplication;
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
import java.time.LocalTime;
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

    public void initialize() {
        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500);
        courierName.setText(courier.getFirstName() + " " + courier.getLastName() + " :");
        date.setText(chosenDate.toString());
        initializeRoadMap();
    }


    /**
     * Returns the title of the step corresponding to the leg
     * <p>
     * The title is "1ère livraison" for the first leg, "Retour à l'entrepôt" for the last leg,
     * and "nème livraison" for the other legs
     *
     * @param legIndex  the index of the leg
     * @param legsCount the total number of legs
     * @return the title of the step corresponding to the leg
     */
    private String legIntexToStepName(int legIndex, int legsCount) {
        if (legIndex == 0) {
            return "1ère livraison";
        }
        if (legIndex == legsCount - 1) {
            return "Retour à l'entrepôt";
        }
        return (legIndex + 1) + "ème livraison";
    }

    /**
     * Returns the title of the step corresponding to the leg
     * <p>
     * The title is "1ère livraison (HH:MM)" for the first leg, "Retour à l'entrepôt (HH:MM)" for the last leg,
     * and "nème livraison (HH:MM)" for the other legs
     *
     * @param legIndex  the index of the leg
     * @param allLegs   the list of all legs
     * @return the title of the step corresponding to the leg
     */
    private String formatStepTitle(int legIndex, List<Leg> allLegs) {
        String stepTitle = legIntexToStepName(legIndex, allLegs.size()); // 1ère livraison, ... or "Retour à l'entrepôt"

        // Format in HH:MM, not HH:MM:SS
        LocalTime departureTime = allLegs.get(legIndex).departureTime().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
        stepTitle = stepTitle + " (" + departureTime + ")"; // 1ère livraison (10:00), ... or "Retour à l'entrepôt (10:00)"
        return stepTitle;
    }


    /**
     * Returns the number of consecutive segments with the same name, starting from the current segment
     *
     * @param allSegments        the list of all segments
     * @param currentSegmentIndex the index of the current segment
     * @return the number of consecutive segments with the same name, starting from the current segment
     */
    private int countConsecutiveSegmentsNames(List<Segment> allSegments, int currentSegmentIndex) {
        int counter = 1;

        for (int i = currentSegmentIndex + 1; i < allSegments.size(); i++) {
            if (allSegments.get(currentSegmentIndex).name().equals(allSegments.get(i).name())) counter++;
            else break;
        }
        return counter;
    }


    /**
     * Initializes the text area with the itinerary of the courier
     */
    private void initializeRoadMap() {
        List<Leg> legs = roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate).getLegs();
        StringBuilder itinerary = new StringBuilder();

        for (int legIdx = 0; legIdx < legs.size(); legIdx++) {
            itinerary.append(formatStepTitle(legIdx, legs)).append("\n"); // 1ère livraison (10:00), ... or "Retour à l'entrepôt (10:00)"
            List<Segment> segments = legs.get(legIdx).segments();

            int consecutiveSegmentsCount = 1;
            for (int segmentIdx = 0; segmentIdx < segments.size(); segmentIdx+=consecutiveSegmentsCount) {
                consecutiveSegmentsCount = countConsecutiveSegmentsNames(segments, segmentIdx);
                Segment segment = segments.get(segmentIdx);
                if(segment.name().isEmpty()){
                    // Todo: log warning
                    continue;
                }
                String s = consecutiveSegmentsCount > 1 ? "s" : "";
                itinerary.append("    - ").append(segment.name()).append(" (")
                        .append(consecutiveSegmentsCount).append(" intersection").append(s)
                        .append(")\n");
            }

        }
        itinerary.append(" \n");
        courierItinirary.setText(itinerary.toString());
    }


    private void initializeMap(Map map, int width) {
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
        fxmlLoader.setController(new DeliferooController(dependencyManager));
        fxmlLoader.setLocation(DeliferooApplication.class.getResource("deliferoo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void generatePdfButton(ActionEvent e) {
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
