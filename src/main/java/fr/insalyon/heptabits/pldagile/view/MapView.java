package fr.insalyon.heptabits.pldagile.view;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapView {

    private final Map map;
    private final float minLatitude;
    private final float minLongitude;

    private final float maxLatitude;
    private final float maxLongitude;

    private final int size;


    public MapView(Map map, int size) {
        this.map = map;
        this.minLatitude = map.getMinLatitude();
        this.minLongitude = map.getMinLongitude();
        this.maxLatitude = map.getMaxLatitude();
        this.maxLongitude = map.getMaxLongitude();

        this.size = size;
    }

    public Group createView() {
        Group group = new Group();
        List<Line> lines = createLines(map.getSegments(), map.getIntersections(), minLatitude, minLongitude, maxLatitude, maxLongitude);

        List<Circle> circles = createCircles(map.getIntersections(), minLatitude, minLongitude, maxLatitude, maxLongitude);
        ImageView warehouseImageView = createWarehouseImageView(map.getWarehouse(), minLatitude, minLongitude, maxLatitude, maxLongitude, Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/warehouse-location-pin.png"));

        group.getChildren().addAll(lines);
        group.getChildren().addAll(circles);
        group.getChildren().add(warehouseImageView);

        return group;
    }


    private List<Line> createLines(List<Segment> segments, HashMap<Long, Intersection> intersections, float minLatitude, float minLongitude, float maxLatitude, float maxLongitude) {
        List<Line> lineList = new ArrayList<>();

        for (Segment segment : segments) {
            Long idDestination = segment.getDestinationId();
            Long idOrigin = segment.getOriginId();

            float latDestination = (intersections.get(idDestination).getLatitude() - minLatitude) * size / (maxLatitude - minLatitude);
            float longDestination = (intersections.get(idDestination).getLongitude() - minLongitude) * size / (maxLongitude - minLongitude);

            float latOrigin = (intersections.get(idOrigin).getLatitude() - minLatitude) * size / (maxLatitude - minLatitude);
            float longOrigin = (intersections.get(idOrigin).getLongitude() - minLongitude) * size / (maxLongitude - minLongitude);

            Line line = new Line(latOrigin, longOrigin, latDestination, longDestination);
            line.setStrokeWidth(3);
            line.setStroke(Color.web("#d8e0e7"));

            lineList.add(line);
        }

        return lineList;
    }

    private List<Circle> createCircles(HashMap<Long, Intersection> intersections, float minLatitude, float minLongitude, float maxLatitude, float maxLongitude) {
        List<Circle> circleList = new ArrayList<>();
        final Color CIRCLE_COLOR = Color.web("#de1c24");
        final Color CIRCLE_COLOR_CLICKED = Color.web("#ab151b");
        final double CIRCLE_OPACITY = 0.5;
        final double CIRCLE_OPACITY_HOVERED = 0.8;
        final double CIRCLE_RADIUS = 3;

        for (java.util.Map.Entry<Long, Intersection> mapentry : intersections.entrySet()) {
            Intersection temp = mapentry.getValue();
            float x = (temp.getLatitude() - minLatitude) * size / (maxLatitude - minLatitude);
            float y = (temp.getLongitude() - minLongitude) * size / (maxLongitude - minLongitude);

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
            circle.setFill(circleColor);
        });
    }


    private ImageView createWarehouseImageView(Intersection warehouse, float minLatitude, float minLongitude, float maxLatitude, float maxLongitude, Path imagePath) {
        final int PIN_SIZE = 40;

        float x = (warehouse.getLatitude() - minLatitude) * size / (maxLatitude - minLatitude);
        float y = (warehouse.getLongitude() - minLongitude) * size / (maxLongitude - minLongitude);

        Image image = new Image(imagePath.toUri().toString(), PIN_SIZE, PIN_SIZE, false, false);
        ImageView imageView = new ImageView(image);
        imageView.setX(x - PIN_SIZE / 2.0);
        imageView.setY(y - PIN_SIZE);

        return imageView;
    }

}
