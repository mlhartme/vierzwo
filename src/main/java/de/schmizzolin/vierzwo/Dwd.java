package de.schmizzolin.vierzwo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

// https://dwd.api.bund.dev
public class Dwd {
    static void main() throws IOException, InterruptedException {
        var wetter = new Dwd();
        var result = wetter.stationOverviewExtendedDays(WeatherStation.AACHEN);
        for (int i = 0; i < result.size(); i++) {
            var obj = (ObjectNode) result.get(i);
            System.out.println("day " + i + ": " + obj.get("icon").asInt());
        }
    }

    private final String baseUri;
    private final HttpClient client;

    public Dwd() {
        this.baseUri = "https://app-prod-ws.warnwetter.de/v30/";
        this.client = HttpClient.newHttpClient();
    }

    record Today(String date, int temperaturMin, int temperatureMax, int precipitation, int sunshine, int icon) {
        public int sunshineHours() {
            return (sunshine + 300) / 600;
        }
        public int precipitationMM() {
            return (precipitation + 5) / 10;
        }
    }

    public Today stationOverviewExtendedToday(WeatherStation ws) throws IOException, InterruptedException {
        var obj = stationOverviewExtendedDays(ws).get(0);
        return new Today(
                obj.get("dayDate").asText(),
                obj.get("temperatureMin").asInt(),
                obj.get("temperatureMax").asInt(),
                obj.get("precipitation").asInt(),
                obj.get("sunshine").asInt(),
                obj.get("icon").asInt()
        );
    }
    public ArrayNode stationOverviewExtendedDays(WeatherStation ws) throws IOException, InterruptedException {
        var all = get("stationOverviewExtended", Map.of("stationIds", ws.kennziffer() + ",G" + ws.id()), ObjectNode.class);
        return (ArrayNode) all.get("" + ws.kennziffer()).get("days");
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
