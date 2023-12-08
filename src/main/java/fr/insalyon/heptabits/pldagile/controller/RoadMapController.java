package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class RoadMapController {

    private final DependencyManager dependencyManager;

    private MapView mapView;
    private Courier courier;
    private LocalDate chosenDate;
    @FXML
    private StackPane mapContainer;
    @FXML
    private Label courierName;
    @FXML
    private Label date;

    public RoadMapController(DependencyManager dependencyManager, Courier courier, LocalDate chosenDate) {
        this.dependencyManager = dependencyManager;
        this.chosenDate = chosenDate;
        this.courier = courier;

    }

    public void initialize(){
        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500);
        courierName.setText(courier.getFirstName() + " " + courier.getLastName() + " :");
        date.setText(chosenDate.toString());
    }

    public void initializeMap(Map map, int width) {
        mapView = new MapView(map, width);
        Group mapGroup = mapView.createView();

        mapContainer.getChildren().clear(); // Clear existing content if necessary
        mapContainer.getChildren().add(mapGroup); // Add the map to the pane
    }

    @FXML
    protected void onReturnButtonClick(InputEvent e){

    }
}
