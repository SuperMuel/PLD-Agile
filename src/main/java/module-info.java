module fr.insalyon.heptabits.pldagile {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.xml;

    opens fr.insalyon.heptabits.pldagile to javafx.fxml;
    exports fr.insalyon.heptabits.pldagile;
}