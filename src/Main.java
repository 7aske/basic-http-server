import server.Server;

import java.io.IOException;

import static server.utils.ServerUtils.pathJoin;

public class Main {
	public static void main(String[] args){
		if (args.length > 0) {
			if (args[0].equals("-help") || args[0].equals("--help")) {
				System.out.println("Usage: java -jar basic-http-server.jar [root] [port]");
				return;
			}
		}
		String path;
		try {
			path = args[0];
		} catch (ArrayIndexOutOfBoundsException e){
			path = "";
		}
		int port;
		try {
			port = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException e){
			port = 8000;
		} catch (NumberFormatException e) {
			port = 8000;
		}
		try {
			new Server(pathJoin(System.getProperty("user.dir"), path), port);
		} catch (IOException e) {
			System.out.printf("%s\n", e.getMessage());
			return;
		}
	}
}
