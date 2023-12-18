package fr.insalyon.heptabits.pldagile.view;

import fr.insalyon.heptabits.pldagile.model.*;
import javafx.animation.ScaleTransition;
import javafx.beans.property.adapter.ReadOnlyJavaBeanDoubleProperty;
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

/**
 * A view for the map.
 * <p>
 * It creates a JavaFX Group containing the map.
 *
 *
 */
public class MapView {

    private final Map map;

    private final int sizeX;

    private final int sizeY;

    private final HashMap<Long, Circle> intersectionsToCircles;

    /**
     * Gets the circle corresponding to an intersection.
     *
     * @param id the intersection id
     * @return the circle
     */
    public Circle getCircleFromIntersectionId(long id) {
        return intersectionsToCircles.get(id);
    }

    /**
     * An interface for the onIntersectionClicked event.
     */
    public interface OnIntersectionClicked {
        void onIntersectionClicked(Intersection intersection);
    }


    final OnIntersectionClicked onIntersectionClicked;

    final List<RoadMap> roadMaps;

    /**
     * Creates a new map view.
     *
     * @param map the map
     * @param sizeX the sizeX of the map
     * @param sizeY the sizeX of the map
     * @param onIntersectionClicked the onIntersectionClicked event
     */
    public MapView(Map map, int sizeX, int sizeY, OnIntersectionClicked onIntersectionClicked) {
        this(map, sizeX, sizeY, onIntersectionClicked, List.of());
    }

    /**
     * Creates a new map view.
     *
     * @param map the map
     * @param sizeX the sizeX of the map
     * @param sizeY the sizeY of the map
     * @param onIntersectionClicked the onIntersectionClicked event
     * @param roadMaps the road maps to display
     */
    public MapView(Map map, int sizeX, int sizeY, OnIntersectionClicked onIntersectionClicked, List<RoadMap> roadMaps) {
        this.map = map;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.onIntersectionClicked = onIntersectionClicked;

        intersectionsToCircles = new HashMap<>();
        this.roadMaps = roadMaps;

    }

    /**
     * Converts a latitude to a pixel, relative to the map size.
     *
     * @param latitude the latitude
     * @return the pixel
     */
    private float latToPixel(double latitude) {
        MapBoundaries boundaries = map.getBoundaries();
        return (float) -((latitude - boundaries.minLatitude()) * sizeY / (boundaries.maxLatitude() - boundaries.minLatitude()));
    }

    /**
     * Converts a longitude to a pixel, relative to the map size.
     *
     * @param longitude the longitude
     * @return the pixel
     */
    private float longToPixel(double longitude) {
        MapBoundaries boundaries = map.getBoundaries();
        return (float) ((longitude - boundaries.minLongitude()) * sizeX / (boundaries.maxLongitude() - boundaries.minLongitude()));
    }


    /**
     * Maps an id to a color.
     *
     * @param id the id
     * @return the color
     */
    public static Color mapIdToColor(long id) {
        // Hash the id to generate a long
        long hash = id;
        hash = ((hash >> 32) ^ hash) * 0x45d9f3b;
        hash = ((hash >> 32) ^ hash) * 0x45d9f3b;
        hash = (hash >> 32) ^ hash;


        // Use the hash to generate the hue value (0-360)
        // Saturation and Brightness values are kept high to avoid dark or desaturated colors
        float hue = Math.abs(hash % 360);
        float saturation = 0.7f; // Saturation set to 70%
        float brightness = 0.9f; // Brightness set to 90%

        // Create color from HSB values
        return Color.hsb(hue, saturation, brightness);
    }


    /**
     * Creates the map view.
     *
     * @return the map view
     */
    public Group createView() {
        Group group = new Group();

        group.getChildren().addAll(createMapLines());
        group.getChildren().addAll(createMapCircles());


        for (RoadMap roadMap : roadMaps) {
            Color color = mapIdToColor(roadMap.getId());
            group.getChildren().addAll(createRoadMapLines(roadMap, color));
            group.getChildren().addAll(createDeliveriesCircles(roadMap.getDeliveries(), color));

        }

        ImageView warehouseImageView = createWarehouseImageView(Path.of("src/main/resources/fr/insalyon/heptabits/pldagile/warehouse-location-pin.png"));
        group.getChildren().add(warehouseImageView);

        return group;
    }

    public Group createView(RoadMap roadMap) {
        Group group = new Group();

        group.getChildren().addAll(createMapLines());
        group.getChildren().addAll(createMapCircles());

        Color color = mapIdToColor(roadMap.getId());
        group.getChildren().addAll(createRoadMapLines(roadMap, color));
        group.getChildren().addAll(createDeliveriesCircles(roadMap.getDeliveries(), color));


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

            if(onIntersectionClicked != null) {
                addCircleEventHandlers(intersection, circle, CIRCLE_COLOR_CLICKED, CIRCLE_COLOR);
            }
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


    private Line segmentToLine(Segment segment, Color lineColor) {
        return segmentToLine(segment, lineColor, 3);
    }


    private Line segmentToLine(Segment segment, Color lineColor, int strokeWidth) {
        Intersection origin = segment.origin();
        Intersection destination = segment.destination();

        float originX = longToPixel(origin.getLongitude());
        float originY = latToPixel(origin.getLatitude());

        float destinationX = longToPixel(destination.getLongitude());
        float destinationY = latToPixel(destination.getLatitude());

        Line line = new Line(originX, originY, destinationX, destinationY);
        line.setStrokeWidth(strokeWidth);
        line.setStroke(lineColor);

        return line;
    }

    /**
     * Creates the road map lines.
     *
     * @param roadMap the road map
     * @param lineColor the color of the lines
     * @return the road map lines
     */
    private List<Line> createRoadMapLines(RoadMap roadMap, Color lineColor) {
        List<Line> lines = new ArrayList<>();
        for (Leg leg : roadMap.getLegs()) {
            for (Segment segment : leg.segments()) {
                lines.add(segmentToLine(segment, lineColor, 5));
            }
        }
        return lines;
    }


    /**
     * Creates circles that represent the deliveries.
     *
     * @param deliveries the deliveries
     * @param color the color of the circles
     * @return the deliveries circles
     */
    private List<Circle> createDeliveriesCircles(List<Delivery> deliveries, Color color){
        List<Circle> circles = new ArrayList<>();
        for(Delivery delivery:deliveries){
            Circle circle = new Circle();

            circle.setRadius(5);
            circle.setFill(Color.WHITE); // Set the fill color to white
            circle.setStroke(color); // Set the stroke (outline) color
            circle.setStrokeWidth(3); // Set the stroke width

            circle.setCenterX(longToPixel(delivery.destination().getLongitude()));
            circle.setCenterY(latToPixel(delivery.destination().getLatitude()));

            circles.add(circle);
        }
        return circles;

    }

    /**
     * Creates the warehouse image view.
     *
     * @param imagePath the image path
     * @return the warehouse image view
     */
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


    /**
     * Animates the circle corresponding to the selected intersection.
     *
     * @param selectedIntersectionId the id of the selected intersection
     */
    public void onDeliveryHovered(long selectedIntersectionId) {
        Circle c = intersectionsToCircles.get(selectedIntersectionId);

        ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.10), c);
        scaleIn.setToX(5);
        scaleIn.setToY(5);

        scaleIn.playFromStart();
        c.setFill(Color.web("#18c474"));
        c.setOpacity(1);
    }

    /**
     * Animates the circle corresponding to the selected intersection.
     *
     * @param selectedIntersectionId the id of the selected intersection
     */
    public void onDeliveryExited(long selectedIntersectionId) {
        Circle c = intersectionsToCircles.get(selectedIntersectionId);

        ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.10), c);
        scaleOut.setToX(1);
        scaleOut.setToY(1);

        scaleOut.playFromStart();
        c.setFill(Color.web("#de1c24"));
        c.setOpacity(0.5);
    }


}
