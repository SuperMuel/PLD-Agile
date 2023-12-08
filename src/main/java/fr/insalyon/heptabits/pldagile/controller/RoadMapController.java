package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;

public class RoadMapController {

    private final DependencyManager dependencyManager;

    private MapView mapView;
    @FXML
    private StackPane mapContainer;

    public RoadMapController(DependencyManager dependencyManager) {
        this.dependencyManager = dependencyManager;
    }

    public void initialize(){
        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500);
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
