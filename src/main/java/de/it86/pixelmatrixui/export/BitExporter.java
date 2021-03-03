package de.it86.pixelmatrixui.export;

import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.List;

class BitExporter {

    static ByteBuffer export(List<Integer> data, int bitWidth) {
        int bytes = Math.floorDiv(data.size() * bitWidth, 8);
        if (bytes * 8 < data.size()) {
            bytes += 1;
        }

        int maxValue = 1 << bitWidth;

        BitSet bitSet = new BitSet(bytes * 8);

        int index = 0;

        for (int number : data) {
            if (number > maxValue) {
                throw new IllegalArgumentException(
                        String.format(
                                "The number %d can not be represented using %d bits!",
                                number,
                                bitWidth));
            }

            for (int bit = 0; bit < bitWidth; bit++) {
                if (getBit(number, bit)) {
                    bitSet.set(index);
                }
                index++;
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes);
        byteBuffer.put(bitSet.toByteArray());
        return byteBuffer;
    }

    private static boolean getBit(int n, int bit) {
        return ((n >> bit) & 1) == 1;
    }

    private BitExporter() {
        /* utility class */
    }

}
