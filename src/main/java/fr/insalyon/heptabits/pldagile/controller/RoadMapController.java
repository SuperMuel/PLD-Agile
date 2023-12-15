package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.DependencyManager;
import fr.insalyon.heptabits.pldagile.model.Courier;
import fr.insalyon.heptabits.pldagile.model.Leg;
import fr.insalyon.heptabits.pldagile.model.Map;
import fr.insalyon.heptabits.pldagile.model.Segment;
import fr.insalyon.heptabits.pldagile.DeliferooApplication;
import fr.insalyon.heptabits.pldagile.repository.RoadMapRepository;
import fr.insalyon.heptabits.pldagile.view.MapView;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.ByteArrayOutputStream;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoadMapController {

    private final DependencyManager dependencyManager;
    private final RoadMapRepository roadMapRepository;
    private MapView mapView;
    private Courier courier;
    private LocalDate chosenDate;
    @FXML
    private StackPane mapContainer;
    @FXML
    private Label courierName;
    @FXML
    private Label date;
    @FXML
    private Label title;

    @FXML
    private javafx.scene.control.Button generatePDFButton;
    @FXML
    private TextArea courierItinirary;



    public RoadMapController(DependencyManager dependencyManager, Courier courier, LocalDate chosenDate) {
        this.dependencyManager = dependencyManager;
        this.chosenDate = chosenDate;
        this.courier = courier;
        this.roadMapRepository = dependencyManager.getRoadMapRepository();
    }

    public void initialize() {
        Map map = dependencyManager.getMapService().getCurrentMap();
        initializeMap(map, 500, 500);
        courierName.setText(courier.getFirstName() + " " + courier.getLastName() + " :");
        date.setText(chosenDate.toString());
        initializeRoadMap();

        generatePDFButton.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> generatePDFButton.setStyle("-fx-background-color: #00BCAD"));
        generatePDFButton.addEventHandler(MouseEvent.MOUSE_EXITED, e -> generatePDFButton.setStyle("-fx-background-color: #00CCBC"));
        generatePDFButton.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> generatePDFButton.setStyle("-fx-background-color: #00A093"));


    }


    /**
     * Returns the title of the step corresponding to the leg
     * <p>
     * The title is "1ère livraison" for the first leg, "Retour à l'entrepôt" for the last leg,
     * and "nème livraison" for the other legs
     *
     * @param legIndex  the index of the leg
     * @param legsCount the total number of legs
     * @return the title of the step corresponding to the leg
     */
    private String legIntexToStepName(int legIndex, int legsCount) {
        if (legIndex == 0) {
            return "1ère livraison";
        }
        if (legIndex == legsCount - 1) {
            return "Retour à l'entrepôt";
        }
        return (legIndex + 1) + "ème livraison";
    }

    /**
     * Returns the title of the step corresponding to the leg
     * <p>
     * The title is "1ère livraison (HH:MM)" for the first leg, "Retour à l'entrepôt (HH:MM)" for the last leg,
     * and "nème livraison (HH:MM)" for the other legs
     *
     * @param legIndex  the index of the leg
     * @param allLegs   the list of all legs
     * @return the title of the step corresponding to the leg
     */
    private String formatStepTitle(int legIndex, List<Leg> allLegs) {
        String stepTitle = legIntexToStepName(legIndex, allLegs.size()); // 1ère livraison, ... or "Retour à l'entrepôt"

        // Format in HH:MM, not HH:MM:SS
        LocalTime departureTime = allLegs.get(legIndex).departureTime().truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
        stepTitle = stepTitle + " (" + departureTime + ")"; // 1ère livraison (10:00), ... or "Retour à l'entrepôt (10:00)"
        return stepTitle;
    }


    /**
     * Returns the number of consecutive segments with the same name, starting from the current segment
     *
     * @param allSegments        the list of all segments
     * @param currentSegmentIndex the index of the current segment
     * @return the number of consecutive segments with the same name, starting from the current segment
     */
    private int countConsecutiveSegmentsNames(List<Segment> allSegments, int currentSegmentIndex) {
        int counter = 1;

        for (int i = currentSegmentIndex + 1; i < allSegments.size(); i++) {
            if (allSegments.get(currentSegmentIndex).name().equals(allSegments.get(i).name())) counter++;
            else break;
        }
        return counter;
    }


    /**
     * Initializes the text area with the itinerary of the courier
     */
    private void initializeRoadMap() {
        List<Leg> legs = roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate).getLegs();
        StringBuilder itinerary = new StringBuilder();

        for (int legIdx = 0; legIdx < legs.size(); legIdx++) {
            itinerary.append(formatStepTitle(legIdx, legs)).append("\n"); // 1ère livraison (10:00), ... or "Retour à l'entrepôt (10:00)"
            List<Segment> segments = legs.get(legIdx).segments();

            int consecutiveSegmentsCount = 1;
            for (int segmentIdx = 0; segmentIdx < segments.size(); segmentIdx+=consecutiveSegmentsCount) {
                consecutiveSegmentsCount = countConsecutiveSegmentsNames(segments, segmentIdx);
                Segment segment = segments.get(segmentIdx);
                if(segment.name().isEmpty()){
                    // Todo: log warning
                    continue;
                }
                String s = consecutiveSegmentsCount > 1 ? "s" : "";
                itinerary.append("    - ").append(segment.name()).append(" (")
                        .append(consecutiveSegmentsCount).append(" intersection").append(s)
                        .append(")\n");
            }

        }
        itinerary.append(" \n");
        courierItinirary.setText(itinerary.toString());
    }


    private void initializeMap(Map map, int width, int height) {
        mapView = new MapView(map, width, height, null);
        Group mapGroup = mapView.createView(roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate));

        mapContainer.getChildren().clear(); // Clear existing content if necessary
        mapContainer.getChildren().add(mapGroup); // Add the map to the pane
    }

    @FXML
    protected void onReturnButtonClick(InputEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new DeliferooController(dependencyManager));
        fxmlLoader.setLocation(DeliferooApplication.class.getResource("deliferoo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = new Stage();
        stage.setTitle("DEL'IFEROO");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void generatePdfButton(ActionEvent e) throws IOException {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        String itinerary = courierItinirary.getText();

        // Créer un FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier PDF");

        // Ajouter un filtre pour ne montrer que les fichiers PDF
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers ¨PDF", "*.pdf"));

        // Afficher la boîte de dialogue et attendre que l'utilisateur sélectionne un fichier
        File selectedFile = fileChooser.showSaveDialog(stage);

        // Vérifier si un fichier a été sélectionné
        if (selectedFile != null) {
            // Faites quelque chose avec le fichier sélectionné
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(javafx.scene.paint.Color.TRANSPARENT); // Set the fill as needed

            // Take a snapshot of the Group
            Image image = mapView.createView(roadMapRepository.getByCourierAndDate(courier.getId(), chosenDate)).snapshot(params, null);

            // Convert JavaFX Image to BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            PDDocument doc = new PDDocument();
            PDPage blankPageImg = new PDPage();
            PDPage blankPageText = new PDPage();
            doc.addPage( blankPageImg );
            doc.addPage(blankPageText);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            baos.flush();
            byte[] imageBytes = baos.toByteArray();
            baos.close();

            // Create a PDImageXObject from the byte array
            org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject pdImage = org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject.createFromByteArray(doc, imageBytes, "image");

            // Define the position and size of the image on the PDF page
            float x = 30; // Change the X coordinate as needed
            float y = 100; // Change the Y coordinate as needed
            float width = pdImage.getWidth();
            float height = pdImage.getHeight();

            PDPage pageImg = doc.getPage(0);
            PDPageContentStream contentStreamImg = new PDPageContentStream(doc, pageImg);
            PDTrueTypeFont font = PDTrueTypeFont.load(doc, PDDocument.class.getResourceAsStream(
                    "/org/apache/pdfbox/resources/ttf/LiberationSans-Regular.ttf"), WinAnsiEncoding.INSTANCE);

            contentStreamImg.drawImage(pdImage, x, y, width, height);
            contentStreamImg.beginText();
            contentStreamImg.setFont(font, 20);
            contentStreamImg.newLineAtOffset(10, 750);
            contentStreamImg.showText(courierName.getText() + title.getText() + date.getText());
            contentStreamImg.endText();
            contentStreamImg.close();

            PDPage pageText = doc.getPage(1);
            PDPageContentStream contentStreamText = new PDPageContentStream(doc, pageText);

            contentStreamText.beginText();
            contentStreamText.setFont(font, 12);
            contentStreamText.newLineAtOffset(50,700);
            String[] itinerary_list = itinerary.split("\n");
            for(String s: itinerary_list){
                contentStreamText.showText(s);
                contentStreamText.newLineAtOffset(0,-15);
            }
            contentStreamText.endText();
            contentStreamText.close();
            doc.save(selectedFile.getAbsolutePath());

            System.out.println("Fichier PDF sélectionné : " + selectedFile.getAbsolutePath());
            // À partir d'ici, vous pouvez traiter le fichier PDF comme nécessaire
            // (par exemple, lire son contenu, analyser les données, etc.)
        } else {
            // L'utilisateur a annulé la sélection
            System.out.println("Sélection de fichier annulée.");
        }

    }


}
