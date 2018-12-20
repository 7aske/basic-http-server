package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static handlers.utils.HandlerUtils.*;

public class RootHandler implements HttpHandler {
	private Path cwd = Paths.get(System.getProperty("user.dir"));
	private String rootFolder = "/statics";
	private List<Path> validPaths;

	public RootHandler() {
		try {
			validPaths = Files.walk(Paths.get(cwd.toString(), rootFolder)).filter(p -> Files.isRegularFile(p)).collect(Collectors.toList());
		} catch (IOException e) {
			validPaths = new ArrayList<>();
		}
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		final String METHOD = exchange.getRequestMethod().toUpperCase();
		// Print out method and request url for debugging
		System.out.printf("%s %s\n", METHOD, exchange.getRequestURI().getPath());

		if (METHOD.equals("GET")) {
			String path = exchange.getRequestURI().getPath();

			byte[] content;
			String contentType;
			int status;

			if (validPaths.contains(parsePath(this.rootFolder, path))) {
				content = readResource(new FileInputStream(parsePath(this.rootFolder, path).toString()));
				contentType = getContentType(path);
				status = 200;
			} else {
				content = "404 Not Found".getBytes();
				contentType = "text/plain";
				status = 404;
			}
			exchange.getResponseHeaders().set("Content-Type", contentType);
			exchange.sendResponseHeaders(status, content.length);
			exchange.getResponseBody().write(content);
			exchange.getResponseBody().close();

		} else {
			exchange.getResponseHeaders().set("Content-Type", "text/plain");
			exchange.sendResponseHeaders(501, "501 Not implemented".length());
			exchange.getResponseBody().write("501 Not implemented".getBytes());
			exchange.getResponseBody().close();
		}


	}
}