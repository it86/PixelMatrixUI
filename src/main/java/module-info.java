module de.triacos.matrixui {
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    exports de.it86.pixelmatrixui;
    opens de.it86.pixelmatrixui.fxml to javafx.fxml;
    opens de.it86.pixelmatrixui.controller to javafx.fxml;
}