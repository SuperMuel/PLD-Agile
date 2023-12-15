package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.DeliferooApplication;
import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.model.RoadMap;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CouriersListController {
    private final DependencyManager dependencyManager;
    private Stage stage;
    @FXML
    private ChoiceBox<Courier> courierChoiceBox;

    @FXML
    private DatePicker datePicker;
    public CouriersListController(DependencyManager dependencyManager, Stage stage) {
        this.dependencyManager = dependencyManager;
        this.stage = stage;
    }

    @FXML
    protected void onReturnButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage newStage = (Stage) source.getScene().getWindow();
        newStage.close();
        this.stage.close();

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
    protected void onValidateCourierSelected(InputEvent e) throws IOException{
        Node source = (Node) e.getSource();
        Stage newStage = (Stage) source.getScene().getWindow();
        Courier courier = courierChoiceBox.getValue();
        LocalDate chosenDate = datePicker.getValue();

        // TODO ensure a the courier and date are not null

        newStage.close();
        this.stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new RoadMapController(dependencyManager, courier, chosenDate));
        fxmlLoader.setLocation(DeliferooApplication.class.getResource("RoadMap.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(){
        LocalDate date = LocalDate.now();
        datePicker.setValue(date);

        final RoadMapRepository roadMapRepository = dependencyManager.getRoadMapRepository();
        final CourierRepository courierRepository = dependencyManager.getCourierRepository();

        final List<RoadMap> roadMaps = roadMapRepository.getByDate(date);
        final List<Courier> couriers = roadMaps.stream().map(RoadMap::getCourierId).distinct().map(courierRepository::findById).toList();
        //TODO If no couriers, show message
        courierChoiceBox.getItems().addAll(couriers);
    }
}
