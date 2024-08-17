package nl.bertriksikken;

import nl.bertriksikken.geojson.FeatureCollection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StreamTableParser {

    public FeatureCollection parseURL(URL url, Duration timeout) throws IOException {
        Document document = Jsoup.parse(url, (int) timeout.toMillis());
        return parseDocument(document);
    }

    public FeatureCollection parseStream(InputStream stream, Charset charset) throws IOException {
        Document document = Jsoup.parse(stream, charset.name(), "");
        return parseDocument(document);
    }

    private FeatureCollection parseDocument(Document document)  {
        Element table = document.selectFirst("table");
        Elements header = table.select("th");
        Elements rows = table.select("tr");

        FeatureCollection featureCollection = new FeatureCollection();
        for (Element row : rows) {
            Elements cols = row.select("td");
            int colIndex = 0;
            Map<String, String> properties = new LinkedHashMap<>();
            String position = "";
            for (Element col : cols) {
                String field = header.get(colIndex).text();
                String value = col.text();
                if (field.equals("Position")) {
                    position = value;
                } else {
                    properties.put(field, value);
                }
                colIndex++;
            }
            if (!position.isEmpty()) {
                featureCollection.add(parseFeature(position, properties));
            }
        }
        return featureCollection;
    }

    private FeatureCollection.Feature parseFeature(String position, Map<String, String> properties) {
        double[] coord = parseDegrees(position);
        FeatureCollection.PointGeometry geometry = new FeatureCollection.PointGeometry(coord[0], coord[1]);
        FeatureCollection.Feature feature = new FeatureCollection.Feature(geometry);
        properties.forEach(feature::addProperty);
        return feature;
    }

    /**
     * Example: "52.18°N 5.96°E"
     */
    private double[] parseDegrees(String degrees) {
        String[] coords = degrees.split(" ", -1);
        double latitude = Double.NaN;
        double longitude = Double.NaN;
        for (String coord : coords) {
            String[] parts = coord.split("°", -1);
            double value = Double.parseDouble(parts[0]);
            String direction = parts[1];
            switch (direction) {
                case "N" -> latitude = value;
                case "S" -> latitude = -value;
                case "E" -> longitude = value;
                case "W" -> longitude = -value;
            }
        }
        return new double[]{latitude, longitude};
    }

}