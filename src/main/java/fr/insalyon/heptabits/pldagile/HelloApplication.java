package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Segment;
import fr.insalyon.heptabits.pldagile.service.XmlMapParser;
import fr.insalyon.heptabits.pldagile.service.XmlMapService;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.insalyon.heptabits.pldagile.model.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Map.Entry;

public class HelloApplication extends Application {

    public List<Line> createLines(List<Segment> segments, HashMap<Long, Intersection> intersections, float minLatitude, float minLongitude, float maxLatitude, float maxLongitude) {
        List<Line> lineList = new ArrayList<>();

        for (Segment segment : segments) {
            Long idDestination = segment.getDestinationId();
            Long idOrigin = segment.getOriginId();

            float latDestination = (intersections.get(idDestination).getLatitude() - minLatitude) * 800 / (maxLatitude - minLatitude);
            float longDestination = (intersections.get(idDestination).getLongitude() - minLongitude) * 800 / (maxLongitude - minLongitude);

            float latOrigin = (intersections.get(idOrigin).getLatitude() - minLatitude) * 800 / (maxLatitude - minLatitude);
            float longOrigin = (intersections.get(idOrigin).getLongitude() - minLongitude) * 800 / (maxLongitude - minLongitude);

            Line line = new Line(latOrigin, longOrigin, latDestination, longDestination);
            line.setStrokeWidth(3);
            line.setStroke(Color.web("#d8e0e7"));

            lineList.add(line);
        }

        return lineList;
    }

    public List<Circle> createCircles(HashMap<Long, Intersection> intersections, float minLatitude, float minLongitude, float maxLatitude, float maxLongitude) {
        List<Circle> circleList = new ArrayList<>();
        final Color CIRCLE_COLOR = Color.web("#de1c24");
        final Color CIRCLE_COLOR_CLICKED = Color.web("#ab151b");
        final double CIRCLE_OPACITY = 0.5;
        final double CIRCLE_OPACITY_HOVERED = 0.8;
        final double CIRCLE_RADIUS = 3;

        for (Entry<Long, Intersection> mapentry : intersections.entrySet()) {
            Intersection temp = mapentry.getValue();
            float x = (temp.getLatitude() - minLatitude) * 800 / (maxLatitude - minLatitude);
            float y = (temp.getLongitude() - minLongitude) * 800 / (maxLongitude - minLongitude);

            Circle circle = new Circle(x, y, CIRCLE_RADIUS, CIRCLE_COLOR);
            circle.setOpacity(CIRCLE_OPACITY);

            addCircleEventHandlers(circle, CIRCLE_COLOR_CLICKED, CIRCLE_OPACITY_HOVERED, CIRCLE_OPACITY, CIRCLE_COLOR);

            circleList.add(circle);
        }

        return circleList;
    }

    private void addCircleEventHandlers(Circle circle, Color clickedColor, double hoveredOpacity, double circleOpacity, Color circleColor) {
        // Animation for mouse hover
        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.15), circle);
        scaleIn.setToX(3);
        scaleIn.setToY(3);

        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.15), circle);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        circle.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            scaleIn.playFromStart(); // Play hover animation
            circle.setOpacity(hoveredOpacity);
        });

        circle.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            scaleOut.playFromStart(); // Play exit animation
            circle.setOpacity(circleOpacity);
        });

        circle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> circle.setFill(clickedColor));

        circle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            // Add logic here if needed when the mouse is released
            circle.setFill(circleColor);
        });
    }


    public ImageView createWarehouseImageView(Intersection warehouse, float minLatitude, float minLongitude, float maxLatitude, float maxLongitude, Path imagePath) {
        final int PIN_SIZE = 40;

        float x = (warehouse.getLatitude() - minLatitude) * 800 / (maxLatitude - minLatitude);
        float y = (warehouse.getLongitude() - minLongitude) * 800 / (maxLongitude - minLongitude);

        Image image = new Image(imagePath.toUri().toString(), PIN_SIZE, PIN_SIZE, false, false);
        ImageView imageView = new ImageView(image);
        imageView.setX(x - PIN_SIZE / 2.0);
        imageView.setY(y - PIN_SIZE);

        return imageView;
    }

    @Override
    public void start(Stage stage) {
        XmlMapParser mapParser = new XmlMapParser();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = null;
        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        XmlMapService mapService = new XmlMapService(mapParser, documentBuilder);
        mapService.loadMap(Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/ExamplesMap/smallMap.xml"));
        Map map = mapService.getCurrentMap();

        float minLatitude = map.getMinLatitude();
        float minLongitude = map.getMinLongitude();
        float maxLatitude = map.getMaxLatitude();
        float maxLongitude = map.getMaxLongitude();


        Group group = new Group();
        List<Line> lines = createLines(map.getSegments(), map.getIntersections(), minLatitude, minLongitude, maxLatitude, maxLongitude);

        List<Circle> circles = createCircles(map.getIntersections(), minLatitude, minLongitude, maxLatitude, maxLongitude);
        ImageView warehouseImageView = createWarehouseImageView(map.getWarehouse(), minLatitude, minLongitude, maxLatitude, maxLongitude, Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/warehouse-location-pin.png"));

        group.getChildren().addAll(lines);
        group.getChildren().addAll(circles);
        group.getChildren().add(warehouseImageView);

        // Final scene setup
        Scene scene = new Scene(group, 800, 800);

        scene.setFill(Color.web("#f6f5f5"));
        stage.setTitle("Map visualization");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}