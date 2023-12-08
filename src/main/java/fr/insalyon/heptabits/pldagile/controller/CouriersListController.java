package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.HelloApplication;
import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.repository.CourierRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CouriersListController {
    private final DependencyManager dependencyManager;
    private Stage stage;

    @FXML
    private ChoiceBox<Courier> courierChoiceBox;

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
        fxmlLoader.setController(new HelloController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("hello-view.fxml"));
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
        newStage.close();
        this.stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new RoadMapController(dependencyManager));
        fxmlLoader.setLocation(HelloApplication.class.getResource("RoadMap.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(){
        final CourierRepository courierRepository = dependencyManager.getCourierRepository();
        List<Courier> couriers = courierRepository.findAll();
        courierChoiceBox.getItems().addAll(couriers);
    }
}
