package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.controller.HelloController;
import fr.insalyon.heptabits.pldagile.model.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        DependencyManager dependencyManager = new DependencyManager();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        HelloController controller = new HelloController(dependencyManager);
        fxmlLoader.setController(controller);

        Parent root = fxmlLoader.load();

        Map map = dependencyManager.getMapService().getCurrentMap();
        double [][] cost = map.getAdjacencyMatrix();
        int counter = 0;
        for(int i = 0; i<308; i++){
            for(int j = 0; j<308; j++){
                if(cost[i][j] != -1){
                    System.out.println(i + " " + j + " " + cost[i][j]);
                    counter++;
                }
            }
        }
        System.out.println(counter);
        Scene scene = new Scene(root);
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}