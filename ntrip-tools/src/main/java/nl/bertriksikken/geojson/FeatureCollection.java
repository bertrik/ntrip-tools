package nl.bertriksikken.geojson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class FeatureCollection extends GeoJsonObject {

    @JsonProperty("features")
    private final List<Feature> features = new ArrayList<>();

    public FeatureCollection() {
        super("FeatureCollection");
    }

    public void add(Feature feature) {
        features.add(feature);
    }

    public List<Feature> getFeatures() {
        return List.copyOf(features);
    }

    public static final class Feature extends GeoJsonObject {
        @JsonProperty("geometry")
        private final GeoJsonGeometry geometry;

        @JsonProperty("properties")
        private final Map<String, Object> properties = new LinkedHashMap<>();

        public Feature(GeoJsonGeometry geometry) {
            super("Feature");
            this.geometry = geometry;
        }

        // copy-constructor
        public Feature(Feature feature) {
            this(feature.geometry);
            properties.putAll(feature.properties);
        }

        public GeoJsonGeometry getGeometry() {
            return geometry;
        }

        public Map<String, Object> getProperties() {
            return new LinkedHashMap<>(properties);
        }

        public void addProperty(String name, Object value) {
            properties.put(name, value);
        }
    }

}
