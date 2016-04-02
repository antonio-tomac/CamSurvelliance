package tomac.camsurvelliance.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import tomac.camsurvelliance.comunication.ImageMessage;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class SurevellianceServer {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		JFrame imageFrame = new JFrame();
		imageFrame.setSize(640, 480);
		DrawPanel drawPanel = new DrawPanel();
		imageFrame.add(drawPanel);
		imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imageFrame.setVisible(true);

//		CameraShooter cameraShooter = CameraShooter.getCameraShooter();
//		BufferedImage takePicture = cameraShooter.takePicture();
//		drawPanel.drawImageAndText(takePicture, "dummy text glupi");
		try (ServerSocket serverSocket = new ServerSocket(12345)) {
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
					while (true) {
						ImageMessage imageMessage = (ImageMessage) objectInputStream.readObject();
						drawPanel.drawImageAndText(imageMessage.getImage(), imageMessage.getSenderId()+imageMessage.getTimestamp());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}

	}
}
