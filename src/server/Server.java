package server;

import com.sun.net.httpserver.HttpServer;
import handlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

	public static void main(String[] args) throws IOException {
		if (args.length < 1 || args[0].equals("-help") || args[0].equals("--help")) {
			System.out.println("Usage: java -jar HttpServer.jar $webroot [$port]");
			return;
		}
		System.out.printf("%s", System.getProperty("user.dir"));
		HttpServer httpServer = HttpServer.create();
		httpServer.createContext("/", new RootHandler());
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			port = 8000;
		} catch (ArrayIndexOutOfBoundsException e) {
			port = 8000;
		}
		try {
			httpServer.bind(new InetSocketAddress("localhost", port), 100);
			System.out.printf("Server running on %s:%d\n", "localhost", port);
		} catch (IOException e) {
			System.out.printf("%s\n", e.getMessage());
		}
		httpServer.start();
	}
}

