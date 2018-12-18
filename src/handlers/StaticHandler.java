package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class StaticHandler implements HttpHandler {
	private String root;
	private Path cwd = Paths.get(System.getProperty("user.dir"));
	private String rootFolder = "/src/statics";
	private Stream<Path> validPaths;

	public StaticHandler(String root) {
		this.root = root;
		try {
			validPaths = Files.walk(Paths.get(cwd.toString(), rootFolder)).filter(p -> Files.isRegularFile(p));
			validPaths.forEach(p -> System.out.printf("%s\n", p.toString()));
		} catch (IOException e) {
			validPaths = Stream.of();
		}
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		System.out.printf("%s %s\n", httpExchange.getRequestMethod().toUpperCase(), path);
		String content;
		String contentType = getContentType(path);
		int status = 200;
		Predicate<Path> pred = p -> p.endsWith(path) || p.endsWith("index.html");
		if (validPaths.anyMatch(pred)){
			content = readFile(parsePath(path));
			System.out.printf("cont %s", content);
		} else {
			content = "404 (ノಠ益ಠ)ノ彡┻━┻";
			System.out.printf("cont %s", content);
			status = 404;
		}
		httpExchange.getResponseHeaders().set("Content-Type", contentType);
		httpExchange.sendResponseHeaders(status, content.length());
		httpExchange.getResponseBody().write(content.getBytes());
		httpExchange.getResponseBody().close();

	}

	private Path parsePath(String p) {
		switch (p) {
			case "/":
				return Paths.get(cwd.toString(), rootFolder, "index.html");
			default:
				return Paths.get(cwd.toString(), rootFolder, p);
		}

	}

	private String getContentType(String ct) {
		if (ct.endsWith(".js"))
			return "text/javascript";
		else if (ct.endsWith(".html") || ct.equals("/"))
			return "text/html";
		else if (ct.endsWith(".css"))
			return "text/css";
		else if (ct.endsWith(".json"))
			return "application/json";
		else return "text/html";
	}

	public String readFile(Path path) {
		String content = "";
		try {
			BufferedReader fileBuffer = new BufferedReader(new FileReader(path.toString()));
			String str;
			while ((str = fileBuffer.readLine()) != null) {
				content += str;
			}
			fileBuffer.close();
		} catch (IOException e) {
			return "";
		}
		System.out.printf("%s", content);
		return content;
	}
}
