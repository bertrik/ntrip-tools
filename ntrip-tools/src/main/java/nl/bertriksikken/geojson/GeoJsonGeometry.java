package nl.bertriksikken.geojson;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class GeoJsonGeometry extends GeoJsonObject {

    private GeoJsonGeometry(EGeometry geometry) {
        super(geometry.id);
    }

    enum EGeometry {
        POINT("Point");

        private final String id;

        EGeometry(String id) {
            this.id = id;
        }
    }

    @JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
    public static final class Point extends GeoJsonGeometry {
        @JsonProperty("coordinates")
        private final double[] coordinates;

        // jackson no-arg constructor
        @SuppressWarnings("unused")
        private Point() {
            this(Double.NaN, Double.NaN);
        }

        public Point(double latitude, double longitude) {
            super(EGeometry.POINT);
            coordinates = new double[]{longitude, latitude};
        }

        public double getLatitude() {
            return coordinates[1];
        }

        public double getLongitude() {
            return coordinates[0];
        }
    }

}
