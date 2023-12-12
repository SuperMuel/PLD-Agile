package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.*;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        RoadMap roadMap = roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate);
        List<Delivery> deliveries = roadMap.getDeliveries();
        //System.out.print(deliveries);
        List<Leg> legs = roadMap.getLegs();
        String itinerary = " ";
        for (int i = 0; i<legs.size(); i++){
            if(i == 0){
                itinerary += (i+1) + "ère étape\n";
            } else {
                itinerary += (i+1) + "ème étape\n";
            }
            List<Segment> segments = legs.get(i).getSegments();
            for(int j = 0; j<segments.size(); j++){
                if(!segments.get(j).name().isEmpty()){
                    itinerary += " - " + segments.get(j).name() + "\n" ;
                }

            }
        }
        courierItinirary.setText(itinerary);


    }

    public void initializeMap(Map map, int width) {
        mapView = new MapView(map, width, null);
        Group mapGroup = mapView.createView();

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
}
