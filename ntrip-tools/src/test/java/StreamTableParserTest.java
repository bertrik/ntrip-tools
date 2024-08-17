import com.fasterxml.jackson.databind.ObjectMapper;
import nl.bertriksikken.StreamTableParser;
import nl.bertriksikken.geojson.FeatureCollection;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class StreamTableParserTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void test() throws IOException {
        StreamTableParser parser = new StreamTableParser();
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("streamtable.html")) {
            FeatureCollection geojson = parser.parseStream(is, StandardCharsets.UTF_8);
            File file = new File("output.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, geojson);
        }
    }

}
