package server;

import com.sun.net.httpserver.HttpServer;
import handlers.RootHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
	public static String rootDir;

	public Server(String root, int port) throws IOException {
		this.rootDir = root;
		System.out.printf("Root: %s\n", root);
		HttpServer httpServer = HttpServer.create();
		httpServer.createContext("/", new RootHandler(rootDir));
		try {
			httpServer.bind(new InetSocketAddress("localhost", port), 100);
			System.out.printf("Server running on %s:%d\n", "localhost", port);
		} catch (IOException e) {
			System.out.printf("%s (localhost:%d)\n", e.getMessage(), port);
			System.exit(-1);
		}
		httpServer.start();
	}
}

