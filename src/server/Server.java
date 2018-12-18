package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

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

class StaticHandler implements HttpHandler {
	private String root;
	private Path cwd = Paths.get(System.getProperty("user.dir"));
	private String rootFolder = "/src/statics";

	public StaticHandler(String root) {
		this.root = root;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		System.out.printf("PATH: %s\n", path);
		String content;
		int code = 404;
		if (path.equals("/")) {
			content = readFile(Paths.get(cwd.toString(), rootFolder, "index.html"));
			code = 200;
		} else {
			content = "404 NOT FOUND";
		}
		httpExchange.getResponseHeaders().set("Content-Type", "text/html");
		httpExchange.sendResponseHeaders(code, content.length());
		httpExchange.getResponseBody().write(content.getBytes());
		httpExchange.getResponseBody().close();

	}

	public String readFile(Path path) {
		String content = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(path.toString()));
			String str;
			while ((str = in.readLine()) != null) {
				content += str;
			}
			in.close();
		} catch (IOException e) {
			return "";
		}
		return content;
	}
}