package everymatrix.homework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Miscellaneous utils
 */
public class Util {

    public static String streamToString(java.io.InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString("UTF-8");
    }

    public static String generateRandomAlphanumeric(int n)  {
            final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

            StringBuilder sb = new StringBuilder();
            Random rnd = new Random();

            while (sb.length() < n) { // length of the random string.
                int index = (int) (rnd.nextFloat() * CHARS.length());
                sb.append(CHARS.charAt(index));
            }

            final String str = sb.toString();
            return str;
        }

        public static String newRandomSessionKey() {
            return generateRandomAlphanumeric(10);
        }
    
}
