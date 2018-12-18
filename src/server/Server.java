package server;

import com.sun.net.httpserver.HttpServer;
import handlers.StaticHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) throws IOException {
		if (args.length < 1 || args[0].equals("-help") || args[0].equals("--help")) {
			System.out.println("Usage: java -jar HttpServer.jar $webroot [$port]");
			return;
		}
		HttpServer httpServer = HttpServer.create();
		httpServer.createContext("/", new StaticHandler(args[0]));
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 8000;
		try {
			httpServer.bind(new InetSocketAddress("localhost", port), 100);
			System.out.printf("Server running on %s:%d\n", "localhost", port);
		} catch (IOException e) {
			System.out.printf("%s\n", e.getMessage());
		}
		httpServer.start();
	}
}

