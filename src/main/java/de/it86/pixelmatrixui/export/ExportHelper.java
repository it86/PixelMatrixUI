package de.it86.pixelmatrixui.export;

import de.it86.pixelmatrixui.data.PixelData;
import de.it86.pixelmatrixui.export.cpp.VCImage;

class ExportHelper {

    public static String stringify(VCImage image) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("{ ")
                .append(image.getBits())
                .append(", ")
                .append(System.lineSeparator())
                .append(stringify(image.getColors().toArray(new PixelData[0])))
                .append(", ")
                .append(System.lineSeparator())
                .append(stringify(image.getPixels().array()))
                .append(" }");

        return stringBuilder.toString();
    }

    public static String stringify(PixelData[] colors) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("new vc_color_t[")
                .append(colors.length)
                .append("] { ");

        for (int i = 0; i < colors.length; i++) {
            if (i > 0) {
                stringBuilder.append(", ");
            }

            PixelData color = colors[i];

            stringBuilder
                    .append("{")
                    .append(color.getRed())
                    .append(", ")
                    .append(color.getGreen())
                    .append(", ")
                    .append(color.getBlue())
                    .append("}");
        }

        stringBuilder
                .append(" }");

        return stringBuilder.toString();
    }

    public static String stringify(byte[] data) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("new byte[")
                .append(data.length)
                .append("] { ")
                .append(stringifyPayload(data))
                .append(" }");

        return stringBuilder.toString();
    }

    private static String stringifyPayload(byte[] data) {
        StringBuilder builder = new StringBuilder();

        for (byte b : data) {
            if (!builder.isEmpty()) {
                builder.append(", ");
            }

            builder.append("0b");
            builder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }

        return builder.toString();
    }

    private ExportHelper() {
        /* utility class */
    }

}
