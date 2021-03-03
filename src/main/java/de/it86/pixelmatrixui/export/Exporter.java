package de.it86.pixelmatrixui.export;

import de.it86.pixelmatrixui.common.Matrix;
import de.it86.pixelmatrixui.data.FrameData;
import de.it86.pixelmatrixui.export.cpp.VCImage;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Exporter {

    private static int calculateNumberOfBits(int numberOfColors) {
        int value = 2;
        int counter = 1;

        while (true) {
            if (value >= numberOfColors) {
                return counter;
            }

            value = value << 1;
            counter++;
        }
    }

    private static List<Integer> matrixToList(Matrix<Integer> matrix) {
        List<Integer> list = new ArrayList<>(matrix.getRows() * matrix.getColumns());

        for (int row = 0; row < matrix.getRows(); row++) {
            for (int column = 0; column < matrix.getColumns(); column++) {
                list.add(matrix.get(row, column));
            }
        }

        return list;
    }

    public Exporter() {
    }

    public String export(List<FrameData> frames, PrintStream printStream) {
        printStream.println(String.format("Es müssen %d Frames konvertiert werden.", frames.size()));

        printStream.print("Konvertierung in indiziertes Farbformat...");
        List<IndexedColorsFormat> indexedFrames = frames.stream()
                .map(IndexedColorsFormat::convert)
                .collect(Collectors.toList());
        printStream.println("Erledigt");

        printStream.print("Komprimierung der Einzelbilder...");
        List<VCImage> images = new ArrayList<>(indexedFrames.size());
        for (int index = 0; index < indexedFrames.size(); index++) {
            printStream.println(String.format("Frame: %d", index + 1));
            IndexedColorsFormat indexedFrame = indexedFrames.get(index);

            int numberOfColors = indexedFrame.getColors().size();
            int numberOfBits = calculateNumberOfBits(numberOfColors);

            printStream.println(String.format("Anzahl an Farben: %d", numberOfColors));
            printStream.println(String.format("Anzahl an Bits: %d", numberOfBits));

            List<Integer> list = matrixToList(indexedFrame.getIndices());
            ByteBuffer byteBuffer = BitExporter.export(list, numberOfBits);

            images.add(new VCImage(numberOfBits, indexedFrame.getColors(), byteBuffer));
        }
        printStream.println("Erledigt");

        printStream.println("Ausgabe");
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < images.size(); index++) {
            if (index > 0) {
                stringBuilder
                        .append(",")
                        .append(System.lineSeparator());
            }
            stringBuilder.append(ExportHelper.stringify(images.get(index)));
        }
        printStream.println("Erledigt");

        return stringBuilder.toString();
    }
}
