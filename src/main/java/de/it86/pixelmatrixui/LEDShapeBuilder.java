package de.it86.pixelmatrixui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.paint.Color;

public final class LEDShapeBuilder {

    private final IntegerProperty rowIndex;
    private final IntegerProperty columnIndex;
    private final DoubleProperty width;
    private final DoubleProperty height;

    public LEDShapeBuilder() {
        this.rowIndex = new SimpleIntegerProperty(this, "rowIndex");
        this.columnIndex = new SimpleIntegerProperty(this, "columnIndex");
        this.width = new SimpleDoubleProperty(this, "width");
        this.height = new SimpleDoubleProperty(this, "height");
    }

    public LEDShapeBuilder rowIndex(int rowIndex) {
        this.rowIndex.set(rowIndex);
        return this;
    }

    public LEDShapeBuilder columnIndex(int columnIndex) {
        this.columnIndex.set(columnIndex);
        return this;
    }

    public LEDShapeBuilder width(double width) {
        this.width.set(width);
        return this;
    }

    public LEDShapeBuilder height(double height) {
        this.height.set(height);
        return this;
    }

    public LEDShape build() {
        return new LEDShape(
                this.rowIndex.get(),
                this.columnIndex.get(),
                this.width.get(),
                this.height.get(),
                Color.BLACK,
                Color.BLACK);
    }

}
