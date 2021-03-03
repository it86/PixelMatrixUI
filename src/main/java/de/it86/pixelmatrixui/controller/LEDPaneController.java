package de.it86.pixelmatrixui.controller;

import de.it86.pixelmatrixui.LEDShape;
import de.it86.pixelmatrixui.LEDShapeBuilder;
import de.it86.pixelmatrixui.common.ControllerAndView;
import de.it86.pixelmatrixui.data.FrameData;
import de.it86.pixelmatrixui.data.PixelData;
import de.it86.pixelmatrixui.fxml.FXMLHelper;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;

public final class LEDPaneController {

    public static ControllerAndView<LEDPaneController, VBox> create() throws IOException {
        FXMLLoader loader = new FXMLLoader();

        VBox view = loader.load(FXMLHelper.ledPaneStream());
        LEDPaneController controller = loader.getController();

        return new ControllerAndView<>(controller, view);
    }

    private final ObjectProperty<FrameData> foregroundFrameData;
    private final ObjectProperty<FrameData> backgroundFrameData;
    private final ObjectProperty<Color> paintColor;
    private final ObjectProperty<Color> defaultColor;

    private LEDShape[][] leds;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private GridPane gridPane;
    @FXML
    private ToggleGroup modeToggleGroup;
    @FXML
    private ToggleButton backgroundToggle;

    public LEDPaneController() {
        this.foregroundFrameData = new SimpleObjectProperty<>(this, "foregroundFrameData");
        this.backgroundFrameData = new SimpleObjectProperty<>(this, "backgroundFrameData");

        this.paintColor = new SimpleObjectProperty<>(this, "paintColor");
        this.defaultColor = new SimpleObjectProperty<>(this, "defaultColor");
    }

    //**************************************************************************
    // Getter & Setter
    //**************************************************************************

    public FrameData getForegroundFrameData() {
        return foregroundFrameData.get();
    }

    public void setForegroundFrameData(FrameData foregroundFrameData) {
        this.foregroundFrameData.set(foregroundFrameData);
    }

    public ObjectProperty<FrameData> foregroundFrameDataProperty() {
        return foregroundFrameData;
    }

    public FrameData getBackgroundFrameData() {
        return backgroundFrameData.get();
    }

    public void setBackgroundFrameData(FrameData backgroundFrameData) {
        this.backgroundFrameData.set(backgroundFrameData);
    }

    public ObjectProperty<FrameData> backgroundFrameDataProperty() {
        return backgroundFrameData;
    }

    //**************************************************************************
    // Other Stuff
    //**************************************************************************

    public ObjectProperty<Color> defaultColorProperty() {
        return defaultColor;
    }

    public Color getForegroundColor(int row, int column) {
        return this.leds[row][column].getForegroundColor();
    }

    public void setForegroundColor(int row, int column, Color color) {
        this.leds[row][column].setForegroundColor(color);
    }

    public FrameData createFrameData() {
        PixelData[][] pixels = new PixelData[leds.length][leds[0].length];

        for (int row = 0; row < leds.length; row++) {
            for (int column = 0; column < leds[0].length; column++) {
                Color color = getForegroundColor(row, column);

                short red = (short) Math.round(color.getRed() * 255);
                short green = (short) Math.round(color.getGreen() * 255);
                short blue = (short) Math.round(color.getBlue() * 255);

                pixels[row][column] = new PixelData(red, green, blue);
            }
        }

        return new FrameData(pixels);
    }

    @FXML
    private void initialize() {
        this.foregroundFrameData.addListener((observable, oldValue, newValue) -> {
            frameDataChanged(newValue);
        });
        this.paintColor.bindBidirectional(this.colorPicker.valueProperty());

        // ToggleButton to show / hide background
        this.backgroundToggle.disableProperty().bind(this.backgroundFrameData.isNull());
        this.backgroundToggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                for (int row = 0; row < leds.length; row++) {
                    for (int column = 0; column < leds[0].length; column++) {
                        this.leds[row][column].setBackgroundColor(backgroundFrameData.get().getPixel(row, column).getColor());
                    }
                }
            } else {
                for (int row = 0; row < leds.length; row++) {
                    for (int column = 0; column < leds[0].length; column++) {
                        this.leds[row][column].setBackgroundColor(defaultColor.get());
                    }
                }
            }
        });

        // Changes of backgroundFrameData
        this.backgroundFrameData.addListener((observable, oldValue, newValue) -> {
            backgroundToggle.setSelected(newValue != null);
        });
    }

    private void frameDataChanged(FrameData data) {
        if (data == null) {
            return;
        }

        if (gridPane.getChildren().size() == 0) {
            LEDShapeBuilder builder = new LEDShapeBuilder().width(16d).height(16d);

            this.leds = new LEDShape[data.getRows()][data.getColumns()];

            for (int row = 0; row < data.getRows(); row++) {
                for (int column = 0; column < data.getColumns(); column++) {
                    LEDShape shape = builder
                            .rowIndex(row)
                            .columnIndex(column)
                            .build();
                    shape.setOnMouseClicked(this::handleMouseClicked);
                    PixelData pixel = data.getPixel(row, column);
                    shape.setForegroundColor(Color.rgb(pixel.getRed(), pixel.getGreen(), pixel.getBlue()));

                    gridPane.add(shape, column, row);
                    this.leds[row][column] = shape;
                }
            }

        } else {
            for (int row = 0; row < leds.length; row++) {
                for (int column = 0; column < leds[0].length; column++) {
                    PixelData pixel = data.getPixel(row, column);
                    this.setForegroundColor(row, column, Color.rgb(pixel.getRed(), pixel.getGreen(), pixel.getBlue()));
                }
            }
        }
    }

    private void handleMouseClicked(MouseEvent event) {
        if (!(event.getSource() instanceof LEDShape)) {
            return;
        }

        LEDShape led = (LEDShape) event.getSource();
        switch ((Mode) modeToggleGroup.getSelectedToggle().getUserData()) {
            case Paint:
                handlePaint(event, led);
                break;
            case PipetteForeground:
                handlePipette(event, led, true);
                break;
            case PipetteBackground:
                handlePipette(event, led, false);
                break;
        }
    }

    private void handlePaint(MouseEvent event, LEDShape led) {
        Color color = null;

        switch (event.getButton()) {
            case PRIMARY:
                color = paintColor.get();
                break;
            case SECONDARY:
                color = defaultColor.get();
                break;
        }

        if (color == null) {
            return;
        }

        led.setForegroundColor(color);

        PixelData pixel = foregroundFrameData.get().getPixel(led.getRowIndex(), led.getColumnIndex());

        short red = (short) Math.round(color.getRed() * 255);
        short green = (short) Math.round(color.getGreen() * 255);
        short blue = (short) Math.round(color.getBlue() * 255);

        pixel.setRed(red);
        pixel.setGreen(green);
        pixel.setBlue(blue);
    }

    private void handlePipette(MouseEvent event, LEDShape led, boolean foreground) {
        if (event.getButton() != MouseButton.PRIMARY) {
            return;
        }

        Color color = foreground ? led.getForegroundColor() : led.getBackgroundColor();

        colorPicker.setValue(color);

        // select paint
        modeToggleGroup.getToggles().stream()
                .filter(toggle -> Mode.Paint.equals(toggle.getUserData()))
                .findFirst()
                .ifPresent(modeToggleGroup::selectToggle);
    }

    public enum Mode {
        Paint,
        PipetteForeground,
        PipetteBackground
    }

}
