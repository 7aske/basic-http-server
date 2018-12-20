package handlers.utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HandlerUtils {
	private static Path cwd = Paths.get(System.getProperty("user.dir"));

	// parse request path so it return the absolute path to the requested file
	public static Path parsePath(String rootFolder, String rel) {
		switch (rel) {
			case "/":
				return Paths.get(cwd.toString(), rootFolder, "index.html");
			default:
				return Paths.get(cwd.toString(), rootFolder, rel);
		}

	}

	public static byte[] readResource(InputStream in) throws IOException {
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

	// Parsing Content-Type header from request file extension
	public static String getContentType(String ct) {
		if (ct.endsWith(".js"))
			return "text/javascript";
		else if (ct.endsWith(".html") || ct.equals("/"))
			return "text/html";
		else if (ct.endsWith(".css"))
			return "text/css";
		else if (ct.endsWith(".png"))
			return "image/png";
		else if (ct.endsWith(".gif"))
			return "image/gif";
		else if (ct.endsWith(".jpg") || ct.endsWith(".jpeg"))
			return "image/jpeg";
		else if (ct.endsWith(".json"))
			return "application/json";
		else return "text/plain";
	}
}
