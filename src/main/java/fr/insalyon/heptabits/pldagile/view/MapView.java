package fr.insalyon.heptabits.pldagile.view;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.MapBoundaries;
import fr.insalyon.heptabits.pldagile.model.Segment;
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



    public MapView(Map map, int size) {
        this(map, size, null);
    }

    public MapView(Map map, int size, OnIntersectionClicked onIntersectionClicked) {
        this.map = map;
        this.size = size;
        this.onIntersectionClicked = onIntersectionClicked;

        intersectionsToCircles = new HashMap<>();

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
        List<Line> lines = createLines(map.getSegments(), map.getIntersections());
        List<Circle> circles = createCircles();


        ImageView warehouseImageView = createWarehouseImageView(map.getWarehouse(), Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/warehouse-location-pin.png"));

        group.getChildren().addAll(lines);
        group.getChildren().addAll(circles);

        //TODO: Add roadmap lines

        group.getChildren().add(warehouseImageView);

        return group;
    }


    private List<Line> createLines(List<Segment> segments, HashMap<Long, Intersection> intersections) {
        List<Line> lineList = new ArrayList<>();

        for (Segment segment : segments) {
            Long idDestination = segment.getDestinationId();
            Long idOrigin = segment.getOriginId();

            Intersection origin = intersections.get(idOrigin);
            Intersection destination = intersections.get(idDestination);

            if(origin == null || destination ==null) {
                System.out.println("A segment couldn't be drawn. The Map object is either malformed, or has been misread.");
                // TODO : add intersections directly in Segment object
                continue;
            }
            float originX = longToPixel(intersections.get(idOrigin).getLongitude());
            float originY = latToPixel(intersections.get(idOrigin).getLatitude());

            float destinationX = longToPixel(intersections.get(idDestination).getLongitude());
            float destinationY = latToPixel(intersections.get(idDestination).getLatitude());

            Line line = new Line(originX, originY, destinationX, destinationY);
            line.setStrokeWidth(3);
            line.setStroke(Color.web("#d8e0e7"));

            lineList.add(line);
            addLineEventHandlers(segment, line);

        }

        return lineList;
    }

    private List<Circle> createCircles() {
        List<Circle> circleList = new ArrayList<>();
        final Color CIRCLE_COLOR = Color.web("#de1c24");
        final Color CIRCLE_COLOR_CLICKED = Color.web("#ab151b");
        final double CIRCLE_OPACITY = 0.5;
        final double CIRCLE_OPACITY_HOVERED = 0.8;
        final double CIRCLE_RADIUS = 3;

        for(Intersection intersection : map.getIntersections().values()) {
            float x = longToPixel(intersection.getLongitude());
            float y = latToPixel(intersection.getLatitude());

            Circle circle = new Circle(x, y, CIRCLE_RADIUS, CIRCLE_COLOR);
            circle.setOpacity(CIRCLE_OPACITY);

            intersectionsToCircles.put(intersection.getId(), circle);
            addCircleEventHandlers(intersection, circle, CIRCLE_COLOR_CLICKED, CIRCLE_OPACITY_HOVERED, CIRCLE_OPACITY, CIRCLE_COLOR);

            circleList.add(circle);
        }

        return circleList;
    }

    private void addCircleEventHandlers(Intersection intersection, Circle circle, Color clickedColor, double hoveredOpacity, double circleOpacity, Color circleColor) {
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
            Tooltip tooltipCircle = new Tooltip(intersection.toString());
            Tooltip.install(circle, tooltipCircle);
        });

        circle.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            scaleOut.playFromStart(); // Play exit animation
            circle.setOpacity(circleOpacity);
        });

        circle.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            circle.setFill(clickedColor);
            onIntersectionClicked.onIntersectionClicked(intersection);
        });

        circle.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> circle.setFill(circleColor));
    }

    private void addLineEventHandlers(Segment segment, Line line){
        Tooltip tooltipCircle = new Tooltip(segment.name());
        Tooltip.install(line, tooltipCircle);
    }

    private ImageView createWarehouseImageView(Intersection warehouse, Path imagePath) {
        final int PIN_SIZE = 40;

        float x = longToPixel(warehouse.getLongitude());
        float y = latToPixel(warehouse.getLatitude());

        Image image = new Image(imagePath.toUri().toString(), PIN_SIZE, PIN_SIZE, false, false);
        ImageView imageView = new ImageView(image);
        imageView.setX(x - PIN_SIZE / 2.0);
        imageView.setY(y - PIN_SIZE);

        return imageView;
    }

}
