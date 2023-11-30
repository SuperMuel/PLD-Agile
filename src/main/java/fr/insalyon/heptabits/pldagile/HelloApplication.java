package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.controller.HelloController;
import fr.insalyon.heptabits.pldagile.model.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        DependencyManager dependencyManager = new DependencyManager();

        stage.setTitle("Home");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        HelloController controller = new HelloController(dependencyManager);
        fxmlLoader.setController(controller);

        Parent root = fxmlLoader.load();

        Map map = dependencyManager.getMapService().getCurrentMap();
        controller.initializeMap(map, 500);



        Scene scene = new Scene(root);
        scene.setFill(Color.web("#f6f5f5"));
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}