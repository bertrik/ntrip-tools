package nl.bertriksikken;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import nl.bertriksikken.geojson.FeatureCollection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public final class StreamTableParserApp {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            printUsage("parse");
            return;
        }
        URL url = new URL(args[0]); // typically https://ntrip.kadaster.nl/streamtable.htm
        StreamTableParserApp app = new StreamTableParserApp();
        app.run(url, Duration.ofSeconds(10));
    }

    private static void printUsage(String appName) {
        System.err.println("Usage: " + appName + " <url>");
    }

    private FeatureCollection filterRTCM(FeatureCollection collection) {
        FeatureCollection filtered = new FeatureCollection();
        collection.getFeatures().stream()
                .filter(f -> f.getProperties().get("Format").toString().contains("RTCM")).forEach(filtered::add);
        return filtered;
    }

    private void run(URL url, Duration timeout) throws IOException {
        StreamTableParser parser = new StreamTableParser();
        FeatureCollection geojson = parser.parseURL(url, timeout);

        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

        // entire table
        writer.writeValue(new File("streamtable.geojson"), geojson);

        // RTCM entries only
        FeatureCollection filtered = filterRTCM(geojson);
        writer.writeValue(new File("rtcm.geojson"), filtered);
    }

}
