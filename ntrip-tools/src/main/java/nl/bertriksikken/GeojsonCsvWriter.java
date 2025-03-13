package nl.bertriksikken;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import nl.bertriksikken.geojson.FeatureCollection;
import nl.bertriksikken.geojson.GeoJsonGeometry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GeojsonCsvWriter {

    public void write(FeatureCollection collection, File file) throws IOException {
        // get all properties
        Set<String> propertyKeys = new LinkedHashSet<>();
        for (FeatureCollection.Feature feature : collection.getFeatures()) {
            feature.getProperties().forEach((key, value) -> propertyKeys.add(key));
        }

        CsvSchema.Builder builder = CsvSchema.builder().setUseHeader(true);
        builder.addColumn("latitude").addColumn("longitude");
        propertyKeys.forEach(builder::addColumn);
        CsvSchema schema = builder.build();

        List<Map<String, Object>> rows = new ArrayList<>();
        for (FeatureCollection.Feature feature : collection.getFeatures()) {
            Map<String, Object> row = new LinkedHashMap<>();
            if (feature.getGeometry() instanceof GeoJsonGeometry.Point point) {
                row.put("latitude", Double.toString(point.getLatitude()));
                row.put("longitude", Double.toString(point.getLongitude()));
                row.putAll(feature.getProperties());
                rows.add(row);
            }
        }
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.writer(schema).writeValue(file, rows);
    }
}
