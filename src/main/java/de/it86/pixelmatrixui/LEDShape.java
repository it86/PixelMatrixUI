package de.it86.pixelmatrixui;

import javafx.beans.property.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;

public class LEDShape extends StackPane {

    private final ReadOnlyIntegerProperty rowIndex;
    private final ReadOnlyIntegerProperty columnIndex;

    private final DoubleProperty pixelWidth;
    private final DoubleProperty pixelHeight;

    private final ObjectProperty<Color> foregroundColor;
    private final ObjectProperty<Color> backgroundColor;

    private final Rectangle rectangle;
    private final Ellipse ellipse;

    public LEDShape(int rowIndex, int columnIndex, double pixelWidth, double pixelHeight, Color foregroundColor, Color backgroundColor) {
        this.rowIndex = new SimpleIntegerProperty(this, "rowIndex", rowIndex);
        this.columnIndex = new SimpleIntegerProperty(this, "columnIndex", columnIndex);

        this.pixelWidth = new SimpleDoubleProperty(this, "pixelWidth", pixelWidth);
        this.pixelHeight = new SimpleDoubleProperty(this, "pixelHeight", pixelHeight);

        this.foregroundColor = new SimpleObjectProperty<>(this, "foregroundColor", foregroundColor);
        this.backgroundColor = new SimpleObjectProperty<>(this, "backgroundColor", backgroundColor);

        this.rectangle = new Rectangle();
        this.rectangle.widthProperty().bind(this.pixelWidth);
        this.rectangle.heightProperty().bind(this.pixelHeight);
        this.rectangle.fillProperty().bind(this.backgroundColor);

        this.ellipse = new Ellipse();
        this.ellipse.radiusXProperty().bind(this.pixelWidth.divide(2).subtract(1));
        this.ellipse.radiusYProperty().bind(this.pixelHeight.divide(2).subtract(1));
        this.ellipse.fillProperty().bind(this.foregroundColor);

        getChildren().add(rectangle);
        getChildren().add(ellipse);
    }

    public int getRowIndex() {
        return rowIndex.get();
    }

    public int getColumnIndex() {
        return columnIndex.get();
    }

    public Color getForegroundColor() {
        return foregroundColor.get();
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor.set(foregroundColor);
    }

    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

}
