package tomac.camsurvelliance.cam;

import com.github.sarxos.webcam.Webcam;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class CameraShooter {

	private static final Webcam WEBCAM = Webcam.getDefault();
	private static final CameraShooter INSTANCE = new CameraShooter();	
	
	private CameraShooter() {
		if (WEBCAM != null) {
			WEBCAM.setViewSize(new Dimension(640, 480));
			WEBCAM.open();
		}
	}

	public BufferedImage takePicture() {
		return WEBCAM.getImage();
	}
	
	public static boolean isCameraSupported() {
		return WEBCAM != null;
	}

	public static CameraShooter getCameraShooter() {
		if (isCameraSupported()) {
			return INSTANCE;
		}
		throw new RuntimeException("Web cam is not found");
	}
}
