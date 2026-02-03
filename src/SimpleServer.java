import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SimpleServer {
    private static final int PORT = 8000;
    private static final Path PUBLIC_DIR = Paths.get("public");

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        server.createContext("/", new RootHandler());
        server.createContext("/public", new StaticHandler());
        server.createContext("/contact", new ContactHandler());

        server.setExecutor(null); // default executor
        server.start();
        System.out.println("Server started on port " + PORT);
        System.out.println("Open http://localhost:" + PORT + "/ in your browser");
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("GET".equalsIgnoreCase(method)) {
                Path indexPath = PUBLIC_DIR.resolve("index.html");
                serveFile(exchange, indexPath);
            } else {
                exchange.getResponseHeaders().add("Allow", "GET");
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String uriPath = exchange.getRequestURI().getPath();
            // uriPath starts with /public/...
            String rel = uriPath.replaceFirst("/public/?", "");
            if (rel.isEmpty()) {
                // no file specified
                exchange.sendResponseHeaders(400, -1);
                return;
            }
            Path filePath = PUBLIC_DIR.resolve(rel).normalize();
            // Prevent path traversal attacks: file must be inside PUBLIC_DIR
            if (!filePath.startsWith(PUBLIC_DIR)) {
                exchange.sendResponseHeaders(403, -1);
                return;
            }
            serveFile(exchange, filePath);
        }
    }

    static class ContactHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if ("POST".equalsIgnoreCase(method)) {
                byte[] bodyBytes = exchange.getRequestBody().readAllBytes();
                String body = new String(bodyBytes, StandardCharsets.UTF_8);
                Map<String, String> params = parseForm(body);

                System.out.println("Received contact form submission:");
                params.forEach((k, v) -> System.out.println(k + " = " + v));

                String response = "<html><body><h2>Thank you!</h2><p>Your message was received.</p><p><a href=\"/\">Back</a></p></body></html>";
                byte[] respBytes = response.getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "text/html; charset=utf-8");
                exchange.sendResponseHeaders(200, respBytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(respBytes);
                }
            } else {
                exchange.getResponseHeaders().add("Allow", "POST");
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private Map<String, String> parseForm(String body) throws IOException {
            Map<String, String> map = new HashMap<>();
            if (body == null || body.isEmpty()) return map;
            String[] pairs = body.split("&");
            for (String pair : pairs) {
                String[] kv = pair.split("=", 2);
                String key = URLDecoder.decode(kv[0], StandardCharsets.UTF_8.name());
                String val = kv.length > 1 ? URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name()) : "";
                map.put(key, val);
            }
            return map;
        }
    }

    private static void serveFile(HttpExchange exchange, Path path) throws IOException {
        if (!Files.exists(path) || Files.isDirectory(path)) {
            String notFound = "404 (Not Found)\n";
            exchange.sendResponseHeaders(404, notFound.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(notFound.getBytes());
            }
            return;
        }

        String contentType = guessContentType(path.toString());
        byte[] bytes = Files.readAllBytes(path);
        if (contentType != null) {
            exchange.getResponseHeaders().add("Content-Type", contentType);
        }
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static String guessContentType(String filename) {
        String lower = filename.toLowerCase();
        if (lower.endsWith(".html") || lower.endsWith(".htm")) return "text/html; charset=utf-8";
        if (lower.endsWith(".css")) return "text/css; charset=utf-8";
        if (lower.endsWith(".js")) return "application/javascript; charset=utf-8";
        if (lower.endsWith(".json")) return "application/json; charset=utf-8";
        if (lower.endsWith(".png")) return "image/png";
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return "image/jpeg";
        if (lower.endsWith(".svg")) return "image/svg+xml";
        return "application/octet-stream";
    }
}
