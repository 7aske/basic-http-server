package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetHandler implements HttpHandler {
	private Path cwd = Paths.get(System.getProperty("user.dir"));
	private String rootFolder = "/src/statics";
	private List<Path> validPaths;

	public GetHandler() {
		try {
			validPaths = Files.walk(Paths.get(cwd.toString(), rootFolder)).filter(p -> Files.isRegularFile(p)).collect(Collectors.toList());
			validPaths.forEach(System.out::println);
		} catch (IOException e) {
			validPaths = new ArrayList<>();
		}
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		String path = httpExchange.getRequestURI().getPath();
		System.out.printf("%s %s\n", httpExchange.getRequestMethod().toUpperCase(), path);
		byte[] content;
		String contentType;
		int status;
		if (validPaths.contains(parsePath(path))) {
			content = readResource(new FileInputStream(parsePath(path).toString()));
			contentType = getContentType(path);
			status = 200;
		} else {
			content = "404 Not Found".getBytes();
			contentType = "text/plain";
			status = 404;
		}
		httpExchange.getResponseHeaders().set("Content-Type", contentType);
		httpExchange.sendResponseHeaders(status, content.length);
		httpExchange.getResponseBody().write(content);
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

	private byte[] readResource(InputStream in) throws IOException {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream gout = new DataOutputStream(bout);
		byte[] tmp = new byte[4096];
		int r;
		while ((r = in.read(tmp)) >= 0)
			gout.write(tmp, 0, r);
		gout.flush();
		gout.close();
		in.close();
		return bout.toByteArray();
	}
	private String getContentType(String ct) {
		if (ct.endsWith(".js"))
			return "text/javascript";
		else if (ct.endsWith(".html") || ct.equals("/"))
			return "text/html";
		else if (ct.endsWith(".css"))
			return "text/css";
		else if (ct.endsWith(".png"))
			return "image/png";
		else if (ct.endsWith(".jpg") || ct.endsWith(".jpg"))
			return "image/jpeg";
		else if (ct.endsWith(".json"))
			return "application/json";
		else return "text/plain";
	}
}