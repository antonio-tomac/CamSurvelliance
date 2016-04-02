package tomac.camsurvelliance.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ConnectionsServer implements Runnable {

	private final ImageCenter imageCenter;
	private final ServerSocket serverSocket;

	public ConnectionsServer(ImageCenter imageCenter, ServerSocket serverSocket) {
		this.imageCenter = imageCenter;
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("got connection: " + socket.getInetAddress());
				ProviderHandler providerHandler = new ProviderHandler(socket, imageCenter);
				Thread thread = new Thread(providerHandler);
				thread.setDaemon(true);
				thread.start();
			}
		} catch (IOException ex) {

		}
	}
}
