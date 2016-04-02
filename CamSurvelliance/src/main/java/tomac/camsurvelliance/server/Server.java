package tomac.camsurvelliance.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
		System.out.println("openning socket...");
		ServerSocket serverSocket = new ServerSocket(serverPort);
		ConnectionsServer connectionsServer = new ConnectionsServer(
			imageCenter, serverSocket
		);
		System.out.println("listening for connections...");
		Thread connectionsThread = new Thread(connectionsServer);
		connectionsThread.start();
		ImageExporter imageExporter = new ImageExporter(imageCenter, exportPath);
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
		scheduledExecutorService.scheduleAtFixedRate(imageExporter, 0, 5, TimeUnit.SECONDS);
		System.out.println("started exporter");
	}
}
