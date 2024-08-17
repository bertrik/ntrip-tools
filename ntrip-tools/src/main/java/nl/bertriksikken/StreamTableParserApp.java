package nl.bertriksikken;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.bertriksikken.geojson.FeatureCollection;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

public final class StreamTableParserApp {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            printUsage("parse");
            return;
        }

        URL url = new URL(args[0]);
        StreamTableParser parser = new StreamTableParser();
        FeatureCollection geojson = parser.parseURL(url, Duration.ofSeconds(10));

        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, geojson);
    }

    private static void printUsage(String appName) {
        System.err.println("Usage: " + appName + " <url>");
    }

}
