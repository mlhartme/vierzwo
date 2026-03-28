package de.schmizzolin.vierzwo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

// https://brightsky.dev/docs/#/operations/getCurrentWeather
public class BrightSky {
    static void main() throws IOException, InterruptedException {
        // aachen:  curl  'https://api.brightsky.dev/current_weather?lat=50.767897&lon=6.121299' | jq

        var wetter = new BrightSky();
        var result = wetter.temperatur(50.767897, 6.121299);
        System.out.println("aachen: " + result);
    }

    private final String baseUri;
    private final HttpClient client;

    public BrightSky() {
        this.baseUri = "https://api.brightsky.dev";
        this.client = HttpClient.newHttpClient();
    }

    public int temperatur(double lat, double lon) throws IOException, InterruptedException {
        var all = get("/current_weather", Map.of("lat", Double.toString(lat), "lon", Double.toString(lon)),
                ObjectNode.class);
        return (int) Math.round(all.get("weather").get("temperature").asDouble());
    }

    public <T> T get(String path, Map<String, String> args, Class<T> clazz) throws IOException, InterruptedException {
        return parse(get(path, args), clazz);
    }

    public String get(String path, Map<String, String> args) throws IOException, InterruptedException {
        var request = requestBuilder(uri(path, args)).GET().build();
        return send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> bodyHandler) throws IOException, InterruptedException {
        var response = client.send(request, bodyHandler);
        if (response.statusCode() != 200) {
            throw new IOException("failed: " + response.statusCode());
        }
        return response;
    }

    private HttpRequest.Builder requestBuilder(URI uri) {
        var result = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json");
        return result;
    }

    private URI uri(String path, Map<String, String> args) {
        var sb = new StringBuilder(baseUri);
        sb.append(path);
        if (!args.isEmpty()) {
            sb.append("?");
            args.entrySet().forEach(e -> sb.append(e.getKey()).append("=").append(e.getValue()).append("&"));
            sb.deleteCharAt(sb.length() - 1);
        }
        return URI.create(sb.toString());
    }

    private static final ObjectMapper OBJECT_MAPPER;
    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> T parse(String str, Class<T> clazz) throws IOException {
        return OBJECT_MAPPER.readValue(str, clazz);
    }

}
