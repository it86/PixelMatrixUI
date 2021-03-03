package de.it86.pixelmatrixui.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class FrameData {

    private static final String ROWS = "rows";
    private static final String COLUMNS = "columns";
    private static final String DATA = "data";

    public static JSONObject toJSONObject(FrameData data) {
        JSONArray array = new JSONArray();

        for (int row = 0; row < data.pixels.length; row++) {
            for (int column = 0; column < data.pixels[0].length; column++) {
                array.put(PixelData.toJSONObject(data.pixels[row][column]));
            }
        }

        JSONObject object = new JSONObject();
        object.put(ROWS, Integer.toString(data.pixels.length));
        object.put(COLUMNS, Integer.toString(data.pixels[0].length));
        object.put(DATA, array);

        return object;
    }

    public static FrameData fromJSONObject(JSONObject object) {
        int rows = Integer.parseInt(object.getString(ROWS));
        int columns = Integer.parseInt(object.getString(COLUMNS));

        PixelData[][] pixels = new PixelData[rows][columns];
        JSONArray array = object.getJSONArray(DATA);

        int index = 0;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                pixels[row][column] = PixelData.fromJSONObject(array.getJSONObject(index));
                index++;
            }
        }

        return new FrameData(pixels);
    }

    public static FrameData createEmpty(int rows, int columns) {
        PixelData[][] pixels = new PixelData[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                pixels[row][column] = new PixelData();
            }
        }

        return new FrameData(pixels);
    }

    private PixelData[][] pixels;

    public FrameData(PixelData[][] pixels) {
        this.pixels = pixels;
    }

    public FrameData(FrameData other) {
        this.pixels = new PixelData[other.pixels.length][other.pixels[0].length];

        for (int row = 0; row < other.pixels.length; row++) {
            for (int column = 0; column < other.pixels[0].length; column++) {
                this.pixels[row][column] = new PixelData(other.pixels[row][column]);
            }
        }
    }

    public int getRows() {
        return pixels.length;
    }

    public int getColumns() {
        return pixels[0].length;
    }

    public PixelData getPixel(int row, int column) {
        return pixels[row][column];
    }

    public void setPixel(int row, int column, PixelData pixel) {
        pixels[row][column] = pixel;
    }

    public void translateInXDirection(int shift) {
        PixelData[][] pixels = new PixelData[getRows()][getColumns()];

        for (int row = 0; row < getRows(); row++) {
            for (int column = 0; column < getColumns(); column++) {
                int otherColumn = column - shift;

                if (otherColumn < 0 || otherColumn >= getColumns()) {
                    pixels[row][column] = new PixelData();
                } else {
                    pixels[row][column] = this.pixels[row][otherColumn];
                }
            }
        }

        this.pixels = pixels;
    }

}
