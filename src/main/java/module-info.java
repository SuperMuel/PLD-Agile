module fr.insalyon.heptabits.pldagile {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires javafx.base;
    requires java.xml;
    requires java.sql;

    opens fr.insalyon.heptabits.pldagile to javafx.fxml;
    exports fr.insalyon.heptabits.pldagile;
    exports fr.insalyon.heptabits.pldagile.controller;
    exports fr.insalyon.heptabits.pldagile.model;
    opens fr.insalyon.heptabits.pldagile.controller to javafx.fxml;
    opens fr.insalyon.heptabits.pldagile.model to javafx.fxml;


}