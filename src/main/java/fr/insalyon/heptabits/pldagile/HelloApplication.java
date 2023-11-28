package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.service.XmlMapParser;
import fr.insalyon.heptabits.pldagile.service.XmlMapService;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;

import fr.insalyon.heptabits.pldagile.model.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        final IdGenerator idGenerator = new IdGenerator();
        XmlMapParser mapParser = new XmlMapParser();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        XmlMapService mapService = new XmlMapService(mapParser, documentBuilder);
        mapService.loadMap(Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/ExamplesMap/smallMap.xml"));
        Map map = mapService.getCurrentMap();

        MapView mapView = new MapView(map, 800);
        Group group = mapView.createView();
        // TODO : add the map view to the scene

        //Scene scene = new Scene(group, 800, 800);

        stage.setTitle("Map visualization");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        scene.setFill(Color.web("#f6f5f5"));



        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}