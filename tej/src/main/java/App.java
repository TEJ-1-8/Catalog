import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class App {

    // Function to decode a value from a specific base to decimal
    public static long decodeValue(String value, int base) {
        return Long.parseLong(value, base);
    }

    // Function to perform Lagrange Interpolation and find constant 'c'
    public static double lagrangeInterpolation(List<Point> points) {
        int k = points.size();
        double secret = 0;

        for (int i = 0; i < k; ++i) {
            double li = 1;
            for (int j = 0; j < k; ++j) {
                if (i != j) {
                    li *= -points.get(j).x / (double)(points.get(i).x - points.get(j).x);
                }
            }
            secret += li * points.get(i).y;
        }
        return secret;
    }

    public static void main(String[] args) {
        try {
            // Parse the input JSON file
            JsonObject inputData = JsonParser.parseReader(new FileReader("testcase.json")).getAsJsonObject();

            // Extract number of points and the degree of the polynomial
            int n = inputData.get("keys").getAsJsonObject().get("n").getAsInt();
            int k = inputData.get("keys").getAsJsonObject().get("k").getAsInt();

            // Decode the x, y values
            List<Point> points = new ArrayList<>();
            for (Map.Entry<String, com.google.gson.JsonElement> entry : inputData.entrySet()) {
                if (!entry.getKey().equals("keys")) {
                    int x = Integer.parseInt(entry.getKey());
                    JsonObject valueObject = entry.getValue().getAsJsonObject();
                    int base = Integer.parseInt(valueObject.get("base").getAsString());
                    String valueStr = valueObject.get("value").getAsString();
                    long decodedY = decodeValue(valueStr, base);
                    points.add(new Point(x, decodedY));
                }
            }

            // Solve for the constant 'c' using Lagrange interpolation
            double secret = lagrangeInterpolation(points);
            System.out.println("Secret (constant c): " + secret);

            // Logic to find wrong points in second test case (if needed)
            // Implement residual check to identify wrong points

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Class to represent a Point (x, y)
    static class Point {
        int x;
        double y;

        Point(int x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
