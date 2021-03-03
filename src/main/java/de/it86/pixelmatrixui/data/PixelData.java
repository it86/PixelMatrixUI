package de.it86.pixelmatrixui.data;

import javafx.scene.paint.Color;
import org.json.JSONObject;

import java.util.Objects;

public class PixelData {

    private static final String RED = "red";
    private static final String GREEN = "green";
    private static final String BLUE = "blue";

    public static JSONObject toJSONObject(PixelData data) {
        JSONObject object = new JSONObject();

        object.put(RED, Short.toString(data.getRed()));
        object.put(GREEN, Short.toString(data.getGreen()));
        object.put(BLUE, Short.toString(data.getBlue()));

        return object;
    }

    public static PixelData fromJSONObject(JSONObject object) {
        short red = Short.valueOf(object.getString(RED));
        short green = Short.valueOf(object.getString(GREEN));
        short blue = Short.valueOf(object.getString(BLUE));

        return new PixelData(red, green, blue);
    }

    private short red;
    private short green;
    private short blue;

    public PixelData() {
        this((short) 0, (short) 0, (short) 0);
    }

    public PixelData(short red, short green, short blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public PixelData(PixelData other) {
        this(other.red, other.green, other.blue);
    }

    public short getRed() {
        return red;
    }

    public void setRed(short red) {
        this.red = red;
    }

    public short getGreen() {
        return green;
    }

    public void setGreen(short green) {
        this.green = green;
    }

    public short getBlue() {
        return blue;
    }

    public void setBlue(short blue) {
        this.blue = blue;
    }

    public Color getColor() {
        return Color.rgb(red, green, blue);
    }

    public void setColor(Color color) {
        this.red = (short) Math.round(color.getRed() * 255);
        this.green = (short) Math.round(color.getGreen() * 255);
        this.blue = (short) Math.round(color.getBlue() * 255);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PixelData pixelData = (PixelData) o;
        return red == pixelData.red && green == pixelData.green && blue == pixelData.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(red, green, blue);
    }

}
