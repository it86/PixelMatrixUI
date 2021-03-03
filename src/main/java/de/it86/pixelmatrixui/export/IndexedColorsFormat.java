package de.it86.pixelmatrixui.export;

import de.it86.pixelmatrixui.common.Matrix;
import de.it86.pixelmatrixui.data.FrameData;
import de.it86.pixelmatrixui.data.PixelData;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class IndexedColorsFormat {

    static IndexedColorsFormat convert(FrameData frame) {
        Map<PixelData, Integer> colors = new HashMap<>();
        Matrix<Integer> indices = new Matrix<>(frame.getRows(), frame.getColumns());

        for (int row = 0; row < frame.getRows(); row++) {
            for (int column = 0; column < frame.getColumns(); column++) {
                PixelData pixel = frame.getPixel(row, column);

                Integer index = colors.computeIfAbsent(pixel, p -> Integer.valueOf(colors.size()));
                indices.set(row, column, index);
            }
        }

        List<PixelData> pixelColors = colors.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().intValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        return new IndexedColorsFormat(pixelColors, indices);
    }

    private final List<PixelData> colors;
    private final Matrix<Integer> indices;

    public IndexedColorsFormat(List<PixelData> colors, Matrix<Integer> indices) {
        this.colors = colors;
        this.indices = indices;
    }

    public List<PixelData> getColors() {
        return colors;
    }

    public Matrix<Integer> getIndices() {
        return indices;
    }

}
