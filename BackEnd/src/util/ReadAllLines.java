package util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by kai on 4/24/17.
 */
public class ReadAllLines {
  public static String readAllLines(BufferedReader reader) throws IOException {
    StringBuilder json = new StringBuilder();
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line).append("\n");
      }
    } finally {
      reader.close();
    }
    return json.toString();
  }
}
