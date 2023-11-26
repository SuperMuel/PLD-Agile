package fr.insalyon.heptabits.pldagile.controller;

import fr.insalyon.heptabits.pldagile.HelloApplication;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;


public class HelloController {

    @FXML
    protected void onHistoryButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("History.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("INFAT'IFGABLES");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    protected void onNewDeliveryButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewDelivery.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("INFAT'IFGABLES");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onNewCourierButtonClick() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("NewCourier.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("INFAT'IFGABLES");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onValidateNewDeliveryButtonClick(InputEvent e) throws IOException {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onValidateNewCourierButtonClick(InputEvent e) throws IOException {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onReturnButtonClick(InputEvent e) throws IOException {
        final Node source = (Node) e.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}