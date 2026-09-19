import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Bin2Dec {

    public int B2D(String n) {

        int temp = Integer.parseInt(n);
        int result = 0;
        int count = 0;
        while (temp != 0) {
            if(temp%10 !=0 && temp%10 != 1) throw new IllegalArgumentException("Invalid input");
            result += (int) ((temp % 10) * Math.pow(2, count++));
            temp /= 10;
        }

        return result;
    }

    public static void main(String[] args) throws IOException {

        Bin2Dec converter = new Bin2Dec();

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        server.createContext("/convert", exchange -> {

            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin", "*"
            );

            if ("POST".equals(exchange.getRequestMethod())) {

                String binary = new String(
                        exchange.getRequestBody().readAllBytes(),
                        StandardCharsets.UTF_8
                );

                int result = converter.B2D(binary);

                String response = String.valueOf(result);

                exchange.sendResponseHeaders(
                        200,
                        response.getBytes().length
                );

                OutputStream output = exchange.getResponseBody();
                output.write(response.getBytes());
                output.close();
            }
        });

        server.start();

        System.out.println("Server running at http://localhost:8080");
    }
}