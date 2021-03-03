package de.it86.pixelmatrixui.export;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class BitExporterTest {

    public static void main(String[] args) {
        ByteBuffer bitSet = BitExporter.export(Arrays.asList(1,2,3,4,3,2,1,0), 4);
        System.out.println(bitSet.toString());

        printByteArray(bitSet.array());
    }

    private static void printByteArray(byte[] bytes) {
        for (byte b1 : bytes){
            String s1 = "0b" + String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
            s1 += " " + Integer.toHexString(b1);
            s1 += " " + b1;
            System.out.println(s1);
        }
    }

}
