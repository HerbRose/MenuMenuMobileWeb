package com.veliasystems.menumenu.client.security;

public class A9Utility {

    public static String bytesToHex(byte[] b) {
            String s = "";
            for (int i = 0; i < b.length; i++) {
                    if (i > 0 && i % 4 == 0) {
                            s += " ";
                    }
                    s += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
            }
            return s;
    }


    public static byte[] intToBytes(int i) {
            byte[] b = new byte[4];
            for (int c = 0; c < 4; c++) {
                    b[c] = (byte) ((i >>> (56 - 8 * c)) & 0xff);
            }
            return b;
    }

    
}