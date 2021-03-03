package de.it86.pixelmatrixui;

import de.it86.pixelmatrixui.common.ControllerAndView;
import de.it86.pixelmatrixui.controller.LEDPaneController;
import de.it86.pixelmatrixui.controller.DialogMoveController;
import de.it86.pixelmatrixui.controller.FrameViewController;
import de.it86.pixelmatrixui.controller.MenuBarController;
import de.it86.pixelmatrixui.data.FrameData;
import de.it86.pixelmatrixui.export.Exporter;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONTokener;
import org.json.JSONWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main extends Application {

    private static final int NUM_ROWS = 32;
    private static final int NUM_COLUMNS = 64;

    public static void main(String[] args) {
        launch(args);
    }

    private final FileChooser jsonFileChooser;
    private final FileChooser fileChooser;

    private Stage stage;

    // Controller
    private MenuBarController menuBarController;
    private LEDPaneController ledPaneController;
    private FrameViewController frameViewController;

    public Main() {
        this.jsonFileChooser = new FileChooser();
        this.jsonFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

        this.fileChooser = new FileChooser();
        this.fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Bild", "*.png"));
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.stage = primaryStage;

        HBox box = new HBox();
        box.getChildren().add(buildFrameView());
        box.getChildren().add(buildLEDPane());

        VBox root = new VBox();
        root.getChildren().add(buildMenuBar());
        root.getChildren().add(box);

        complexSetup();

        Scene scene = new Scene(root, 640, 480);

        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void complexSetup() {
        ledPaneController.foregroundFrameDataProperty().bind(Bindings.createObjectBinding(() -> {
            if (frameViewController.getSelectedFrame() == null) {
                return null;
            }

            return frameViewController.getSelectedFrame().getData();
        }, frameViewController.selectedFrameProperty()));
    }

    //******************************************************************************************************************
    // ACTIONS
    //******************************************************************************************************************

    private void load() {
        File file = jsonFileChooser.showOpenDialog(stage);
        if (file == null) {
            return;
        }

        try (FileReader fileReader = new FileReader(file)) {
            JSONTokener tokener = new JSONTokener(fileReader);
            JSONArray array = new JSONArray(tokener);
            List<FrameItem> frames = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                FrameData data = FrameData.fromJSONObject(array.getJSONObject(i));
                FrameItem item = new FrameItem();
                item.setIndex(i);
                item.setData(data);
                frames.add(item);
            }

            frameViewController.framesProperty().get().setAll(frames);
        } catch (IOException ex) {
            // TODO exception handling
        }
    }

    private void save() {
        File file = jsonFileChooser.showSaveDialog(stage);
        if (file == null) {
            return;
        }

        // Den Zustand des aktuellen Frames speichern
        frameViewController.selectedFrameProperty().get().setData(ledPaneController.createFrameData());

        try {
            FileWriter writer = new FileWriter(file);
            JSONWriter jsonWriter = new JSONWriter(writer);

            jsonWriter.array();

            for (FrameItem frame : frameViewController.getFrames()) {
                jsonWriter.value(FrameData.toJSONObject(frame.getData()));
            }

            jsonWriter.endArray();

            writer.close();
        } catch (IOException ex) {
            // TODO exception handling
        }
    }

    private void export() {
        Exporter exporter = new Exporter();
        String export = exporter.export(frameViewController.getFrames().stream().map(FrameItem::getData).collect(Collectors.toList()), System.out);

        TextArea textArea = new TextArea();
        textArea.setText(export);

        Dialog<String> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initStyle(StageStyle.UTILITY);
        dialog.setTitle("Export");
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE));
        dialog.getDialogPane().setContent(textArea);
        dialog.setResizable(true);
        dialog.showAndWait();
    }

    private void move() {
        try {
            ControllerAndView<DialogMoveController, GridPane> controllerAndView = DialogMoveController.create();

            Dialog<Integer> dialog = new Dialog<>();
            dialog.initOwner(stage);
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle("Export");
            dialog.getDialogPane().getButtonTypes().addAll(
                    new ButtonType("Verschieben", ButtonBar.ButtonData.APPLY),
                    new ButtonType("Abbrechen", ButtonBar.ButtonData.CANCEL_CLOSE));
            dialog.getDialogPane().setContent(controllerAndView.getView());
            dialog.setResizable(true);
            dialog.setResultConverter(buttonType -> {
                if (buttonType.getButtonData() == ButtonBar.ButtonData.APPLY) {
                    return controllerAndView.getController().getXDirection();
                }
                return null;
            });

            Optional<Integer> xDirection = dialog.showAndWait();
            if (xDirection.isEmpty()) {
                return;
            }

            frameViewController.getFrames().stream().forEach(frame -> {
                frame.getData().translateInXDirection(xDirection.get().intValue());
            });
        } catch (IOException ex) {
            ex.printStackTrace();
            // TODO: Exception handling
        }
    }

    private void importBackground() {
        File file = fileChooser.showOpenDialog(stage);
        if (file == null || !file.exists()) {
            return;
        }

        try {
            Image image = new Image(Files.newInputStream(file.toPath()));

            if (image.getWidth() != NUM_COLUMNS || image.getHeight() != NUM_ROWS) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Es können nur Bilder der Größe 64 x 32 geladen werden.", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            FrameData background = FrameData.createEmpty(NUM_ROWS, NUM_COLUMNS);

            for (int row = 0; row < NUM_ROWS; row++) {
                for (int column = 0; column < NUM_COLUMNS; column++) {
                    background.getPixel(row, column).setColor(image.getPixelReader().getColor(column, row));
                }
            }

            ledPaneController.setBackgroundFrameData(background);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void clearBackground() {
        ledPaneController.setBackgroundFrameData(null);
    }

    //******************************************************************************************************************
    // INITIALIZATION
    //******************************************************************************************************************

    private TitledPane buildFrameView() throws IOException {
        ControllerAndView<FrameViewController, TitledPane> controllerAndView = FrameViewController.create();

        this.frameViewController = controllerAndView.getController();

        return controllerAndView.getView();
    }

    private MenuBar buildMenuBar() throws IOException {
        ControllerAndView<MenuBarController, MenuBar> controllerAndView = MenuBarController.create();

        this.menuBarController = controllerAndView.getController();
        this.menuBarController.setLoadProcedure(this::load);
        this.menuBarController.setSaveProcedure(this::save);
        this.menuBarController.setExportProcedure(this::export);

        this.menuBarController.setMoveProcedure(this::move);

        this.menuBarController.setImportBackground(this::importBackground);
        this.menuBarController.setClearBackground(this::clearBackground);

        return controllerAndView.getView();
    }

    private VBox buildLEDPane() throws IOException {
        ControllerAndView<LEDPaneController, VBox> controllerAndView = LEDPaneController.create();

        this.ledPaneController = controllerAndView.getController();
        this.ledPaneController.defaultColorProperty().set(Color.BLACK);

        return controllerAndView.getView();
    }

}
