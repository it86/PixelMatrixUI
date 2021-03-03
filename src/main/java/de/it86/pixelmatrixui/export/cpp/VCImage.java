package de.it86.pixelmatrixui.export.cpp;

import de.it86.pixelmatrixui.data.PixelData;

import java.nio.ByteBuffer;
import java.util.List;

public class VCImage {

    private final int bits;
    private final List<PixelData> colors;
    private final ByteBuffer pixels;

    public VCImage(int bits, List<PixelData> colors, ByteBuffer pixels) {
        this.bits = bits;
        this.colors = colors;
        this.pixels = pixels;
    }

    public int getBits() {
        return bits;
    }

    public List<PixelData> getColors() {
        return colors;
    }

    public ByteBuffer getPixels() {
        return pixels;
    }

}
