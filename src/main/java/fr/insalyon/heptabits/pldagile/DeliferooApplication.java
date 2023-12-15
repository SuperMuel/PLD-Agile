package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.controller.DeliferooController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DeliferooApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        DependencyManager dependencyManager = new DependencyManager();

        FXMLLoader fxmlLoader = new FXMLLoader(DeliferooApplication.class.getResource("deliferoo-view.fxml"));
        DeliferooController controller = new DeliferooController(dependencyManager);
        fxmlLoader.setController(controller);

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}