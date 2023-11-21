package fr.insalyon.heptabits.pldagile;

import fr.insalyon.heptabits.pldagile.model.Intersection;
import fr.insalyon.heptabits.pldagile.model.Segment;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();*/

        File file = new File("src/main/resources/fr/insalyon/heptabits/pldagile/ExamplesMap/smallMap.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        DocumentBuilder db;

        {
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        Document doc;

        {
            try {
                doc = db.parse(file);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList intersectionListTemp = doc.getElementsByTagName("intersection");
        NodeList segmentListTemp = doc.getElementsByTagName("segment");

        HashMap<Long, Intersection> intersectionList = new HashMap<Long, Intersection>();
        List<Segment> segmentList = new ArrayList<Segment>();
        Long warehouseAddress = Long.parseLong(doc.getElementsByTagName("warehouse").item(0).getAttributes().getNamedItem("address").getNodeValue());
        float minLatitude = 1000000.00f;
        float minLongitude = 1000000.0f;
        float maxLatitude = 0f;
        float maxLongitude = 0f;

        for(int i = 0; i<intersectionListTemp.getLength(); i++) {
            Node node = intersectionListTemp.item(i);
            long id = Long.parseLong(node.getAttributes().getNamedItem("id").getNodeValue());
            float latitude = Float.parseFloat(node.getAttributes().getNamedItem("latitude").getNodeValue());
            float longitude = Float.parseFloat(node.getAttributes().getNamedItem("longitude").getNodeValue());

            if(latitude<minLatitude){
                minLatitude = latitude;
            }
            if(longitude<minLongitude){
                minLongitude = longitude;
            }
            if(latitude>maxLatitude){
                maxLatitude = latitude;
            }
            if(longitude>maxLongitude){
                maxLongitude = longitude;
            }
            System.out.println(warehouseAddress+" : "+id+" : "+latitude+" : "+longitude);
            Intersection intersection = new Intersection(id, latitude, longitude);
            intersectionList.put(id, intersection);
        }

        for(int i = 0; i<segmentListTemp.getLength(); i++) {
            Node node = segmentListTemp.item(i);
            long destination = Long.parseLong(node.getAttributes().getNamedItem("destination").getNodeValue());
            long origin = Long.parseLong(node.getAttributes().getNamedItem("origin").getNodeValue());
            float length = Float.parseFloat(node.getAttributes().getNamedItem("length").getNodeValue());
            String name = node.getAttributes().getNamedItem("name").getNodeValue();

            System.out.println(warehouseAddress+" : "+destination+" : "+origin+" : "+length+" : "+name);
            Segment segment = new Segment(destination, length, name, origin);
            segmentList.add(segment);
        }

        Group group = new Group();

        List<Line> lineList = new ArrayList<>();
        for (Segment segment : segmentList) {
            Long idDestination = segment.getDestination();
            Long idOrigin = segment.getOrigin();

            float latDestination = (intersectionList.get(idDestination).getLatitude() - minLatitude) * 800 / (maxLatitude - minLatitude);
            float longDestination = (intersectionList.get(idDestination).getLongitude() - maxLongitude) * 800 / (minLongitude - maxLongitude);

            float latOrigin = (intersectionList.get(idOrigin).getLatitude() - minLatitude) * 800 / (maxLatitude - minLatitude);
            float longOrigin = (intersectionList.get(idOrigin).getLongitude() - maxLongitude) * 800 / (minLongitude - maxLongitude);

            // On crée 2 lignes : 1 pour le remplissage et 1 pour le contour
            Line line = new Line(latOrigin, longOrigin, latDestination, longDestination);
            Line stroke = new Line(latOrigin, longOrigin, latDestination, longDestination);
            stroke.setStrokeWidth(5);
            line.setStrokeWidth(3);
            stroke.setStroke(Color.BLACK);
            line.setStroke(Color.WHITE);

            lineList.add(stroke);
            lineList.add(line);
        }

        group.getChildren().addAll(lineList);

        List<Circle> circleList = new ArrayList<>();
        for(Map.Entry mapentry : intersectionList.entrySet()){
            Intersection temp = (Intersection) mapentry.getValue();
            float x = (temp.getLatitude()-minLatitude)*800/(maxLatitude-minLatitude);
            float y = (temp.getLongitude()-maxLongitude)*800/(minLongitude-maxLongitude);
            Circle circle = new Circle(x, y, 3, Color.rgb(255,0,0));
            circleList.add(circle);
        }

        group.getChildren().addAll(circleList);

        float latWareHouse = (intersectionList.get(warehouseAddress).getLatitude()-minLatitude)*800/(maxLatitude-minLatitude);
        float longWareHouse = (intersectionList.get(warehouseAddress).getLongitude()-maxLongitude)*800/(minLongitude-maxLongitude);
        Circle circleWareHouse = new Circle(latWareHouse, longWareHouse, 10, Color.rgb(0,255,0));

        group.getChildren().add(circleWareHouse);

        //Creating a scene object
        Scene scene = new Scene(group, 800, 800);

        scene.setFill(Color.web("#F5F5DC"));

        //Setting title to the Stage
        stage.setTitle("Drawing many circles");

        //Adding scene to the stage
        stage.setScene(scene);

        //Displaying the contents of the stage
        stage.show();
    }

    /*public static void display(Map map){

        float minLatitude = map.listIntersection.latitude.min();
        float minLongitude = map.listIntersection.longitude.min();

        for(int i = 0; i<map.listIntersection.getLength; i++){
            map.listIntersection.latitude = map.listIntersection.latitude - maxLatitude;
        }

    }*/


    public static void main(String[] args) {
        launch();
    }


}