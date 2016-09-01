package tomac.camsurvelliance.server;

import java.io.IOException;
import java.net.ServerSocket;
import lombok.Data;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class Server {

	private static void printUsageAndExit() {
		System.out.println("./runServar <port> <export-path>");
		System.exit(-1);
	}

	private static ServerConfig resolveConfig(String[] args) {
		if (args.length != 2) {
			printUsageAndExit();
		}
		int port = Integer.parseInt(args[0]);
		String exportPath = args[1];
		return new ServerConfig(port, exportPath);
	}
	
	public static void main(String[] args) throws IOException {
		ImageCenter imageCenter = new ImageCenter();
		ServerConfig serverConfig = resolveConfig(args);
		ImageExporter imageExporter = new ImageExporter(imageCenter, serverConfig.getExportPath());
		imageCenter.addChangeListener(imageExporter);
		System.out.println("openning socket...");
		ServerSocket serverSocket = new ServerSocket(serverConfig.getPort());
		ConnectionsServer connectionsServer = new ConnectionsServer(
			imageCenter, serverSocket
		);
		System.out.println("listening for connections...");
		Thread connectionsThread = new Thread(connectionsServer);
		connectionsThread.start();
	}

	@Data
	private static class ServerConfig {

		private final int port;
		private final String exportPath;
	}
}
