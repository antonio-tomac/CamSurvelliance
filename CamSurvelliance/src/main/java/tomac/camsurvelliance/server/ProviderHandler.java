package tomac.camsurvelliance.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import tomac.camsurvelliance.comunication.ImageMessage;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ProviderHandler implements Runnable {

	private final Socket inputSocket;
	private final ImageCenter imageCenter;

	public ProviderHandler(Socket inputSocket, ImageCenter imageCenter) {
		this.inputSocket = inputSocket;
		this.imageCenter = imageCenter;
	}

	@Override
	public void run() {
		try {
			InputStream inputStream = inputSocket.getInputStream();
			ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
			while (true) {
				ImageMessage imageMessage = (ImageMessage) objectInputStream.readObject();
				System.out.println("got image from: " + imageMessage.getSenderId());
				imageCenter.addMessageImage(this, imageMessage);
			}
		} catch (IOException | ClassNotFoundException ex) {

		} finally {
			imageCenter.removeHandlerProvider(this);
			try {
				inputSocket.close();
			} catch (IOException ex) {
			}
		}
	}

}
