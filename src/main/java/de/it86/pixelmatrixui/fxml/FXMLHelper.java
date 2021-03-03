package de.it86.pixelmatrixui.fxml;

import java.io.InputStream;

public class FXMLHelper {

    public static InputStream frameViewStream() {
        return FXMLHelper.class.getResourceAsStream("FrameView.fxml");
    }

    public static InputStream menuBarStream() {
        return FXMLHelper.class.getResourceAsStream("MenuBar.fxml");
    }

    public static InputStream ledPaneStream() {
        return FXMLHelper.class.getResourceAsStream("LEDPane.fxml");
    }

    public static InputStream dialogMoveStream() { return FXMLHelper.class.getResourceAsStream("DialogMove.fxml"); }

}
