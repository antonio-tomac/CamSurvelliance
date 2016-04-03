package tomac.camsurvelliance.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class Server {

	public static void main(String[] args) throws IOException {
		ImageCenter imageCenter = new ImageCenter();
		int serverPort = Config.DEFAULT_SERVER_PORT;
		String exportPath = Config.DEFAULT_EXPORT_PATH;
		if (args.length == 1) {
			exportPath = args[0];
		}
		ImageExporter imageExporter = new ImageExporter(imageCenter, exportPath);
		imageCenter.addChangeListener(imageExporter);
		System.out.println("openning socket...");
		ServerSocket serverSocket = new ServerSocket(serverPort);
		ConnectionsServer connectionsServer = new ConnectionsServer(
			imageCenter, serverSocket
		);
		System.out.println("listening for connections...");
		Thread connectionsThread = new Thread(connectionsServer);
		connectionsThread.start();
	}
}
