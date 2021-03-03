package de.it86.pixelmatrixui.controller;

import de.it86.pixelmatrixui.common.ControllerAndView;
import de.it86.pixelmatrixui.fxml.FXMLHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class DialogMoveController {

    public static ControllerAndView<DialogMoveController, GridPane> create() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        GridPane view = loader.load(FXMLHelper.dialogMoveStream());
        DialogMoveController controller = loader.getController();

        return new ControllerAndView<>(controller, view);
    }

    @FXML
    private Spinner<Integer> xDirection;

    public Integer getXDirection() {
        return xDirection.getValue();
    }

}
