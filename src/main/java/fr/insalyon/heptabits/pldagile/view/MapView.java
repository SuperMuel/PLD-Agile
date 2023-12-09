package fr.insalyon.heptabits.pldagile.view;

import fr.insalyon.heptabits.pldagile.model.*;
import javafx.animation.ScaleTransition;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
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

    private final int size;

    private final HashMap<Long, Circle> intersectionsToCircles;

    public Circle getCircleFromIntersectionId(long id) {
        return intersectionsToCircles.get(id);
    }

    public interface OnIntersectionClicked {
        void onIntersectionClicked(Intersection intersection);
    }

    final OnIntersectionClicked onIntersectionClicked;

    final List<RoadMap> roadMaps;

    public MapView(Map map, int size) {
        this(map, size, null, List.of());
    }

    public MapView(Map map, int size, OnIntersectionClicked onIntersectionClicked, List<RoadMap> roadMaps) {
        this.map = map;
        this.size = size;
        this.onIntersectionClicked = onIntersectionClicked;

        intersectionsToCircles = new HashMap<>();
        this.roadMaps = roadMaps;

    }



    private float latToPixel(double latitude) {
        MapBoundaries boundaries = map.getBoundaries();
        return (float) ((latitude - boundaries.minLatitude()) * size / (boundaries.maxLatitude() - boundaries.minLatitude()));
    }

    private float longToPixel(double longitude) {
        MapBoundaries boundaries = map.getBoundaries();
        return (float) ((longitude - boundaries.minLongitude()) * size / (boundaries.maxLongitude() - boundaries.minLongitude()));
    }



    public Group createView() {
        Group group = new Group();

        group.getChildren().addAll(createMapLines());
        group.getChildren().addAll(createMapCircles());


        for (RoadMap roadMap : roadMaps) {
            // TODO Get a random color
            group.getChildren().addAll(createRoadMapLines(roadMap, Color.BLUE));
        }

        ImageView warehouseImageView = createWarehouseImageView(Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/warehouse-location-pin.png"));
        group.getChildren().add(warehouseImageView);

        return group;
    }


    private List<Line> createMapLines() {
        List<Line> lineList = new ArrayList<>();
        for (Segment segment : map.getSegments()) {
            Line line = segmentToLine(segment, Color.web("#d8e0e7"));
            lineList.add(line);
            addLineEventHandlers(segment, line);
        }
        return lineList;
    }

    private List<Circle> createMapCircles() {
        List<Circle> circleList = new ArrayList<>();
        final Color CIRCLE_COLOR = Color.web("#de1c24");
        final Color CIRCLE_COLOR_CLICKED = Color.web("#ab151b");
        final double CIRCLE_OPACITY = 0.5;
        final double CIRCLE_RADIUS = 3;

        for (Intersection intersection : map.getIntersections().values()) {
            float x = longToPixel(intersection.getLongitude());
            float y = latToPixel(intersection.getLatitude());

            Circle circle = new Circle(x, y, CIRCLE_RADIUS, CIRCLE_COLOR);
            circle.setOpacity(CIRCLE_OPACITY);

            intersectionsToCircles.put(intersection.getId(), circle);
            addCircleEventHandlers(intersection, circle, CIRCLE_COLOR_CLICKED, CIRCLE_COLOR);

            circleList.add(circle);
        }

        return circleList;
    }

    private void addCircleEventHandlers(Intersection intersection, Circle circle, Color clickedColor, Color circleColor) {
        // Animation for mouse hover
        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.15), circle);
        scaleIn.setToX(3);
        scaleIn.setToY(3);

        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.15), circle);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        circle.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            scaleIn.playFromStart(); // Play hover animation
            circle.setOpacity(0.8);
            Tooltip tooltipCircle = new Tooltip(intersection.toString());
            Tooltip.install(circle, tooltipCircle);
        });

        circle.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            scaleOut.playFromStart(); // Play exit animation
            circle.setOpacity(0.5);
        });

        circle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            circle.setFill(clickedColor);
            onIntersectionClicked.onIntersectionClicked(intersection);
        });

        circle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> circle.setFill(circleColor));
    }

    private void addLineEventHandlers(Segment segment, Line line) {
        Tooltip tooltipCircle = new Tooltip(segment.name());
        Tooltip.install(line, tooltipCircle);
    }


    Line segmentToLine(Segment segment, Color lineColor) {
        Intersection origin = segment.origin();
        Intersection destination = segment.destination();

        float originX = longToPixel(origin.getLongitude());
        float originY = latToPixel(origin.getLatitude());

        float destinationX = longToPixel(destination.getLongitude());
        float destinationY = latToPixel(destination.getLatitude());

        Line line = new Line(originX, originY, destinationX, destinationY);
        line.setStrokeWidth(3);
        line.setStroke(lineColor);

        return line;
    }

    private List<Line> createRoadMapLines(RoadMap roadMap, Color lineColor) {
        List<Line> lines = new ArrayList<>();
        for (Leg leg : roadMap.getLegs()) {
            for (Segment segment : leg.getSegments()) {
                System.out.println("Drawing  " + segment);
                lines.add(segmentToLine(segment, lineColor));
            }
        }
        return lines;
    }


    private ImageView createWarehouseImageView(Path imagePath) {
        final int PIN_SIZE = 40;

        Intersection warehouse = map.getWarehouse();

        float x = longToPixel(warehouse.getLongitude());
        float y = latToPixel(warehouse.getLatitude());

        Image image = new Image(imagePath.toUri().toString(), PIN_SIZE, PIN_SIZE, false, false);
        ImageView imageView = new ImageView(image);
        imageView.setX(x - PIN_SIZE / 2.0);
        imageView.setY(y - PIN_SIZE);

        return imageView;
    }


}
