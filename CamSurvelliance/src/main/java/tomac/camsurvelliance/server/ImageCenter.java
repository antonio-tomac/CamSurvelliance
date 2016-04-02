package tomac.camsurvelliance.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tomac.camsurvelliance.comunication.ImageMessage;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ImageCenter {
	
	private final Object lock = new Object();
	private final Map<ProviderHandler, ImageMessage> data = new HashMap<>();
	
	public void removeHandlerProvider(ProviderHandler handler) {
		synchronized (lock) {
			data.remove(handler);
		}
	}
	
	public void addMessageImage(ProviderHandler handler, ImageMessage imageMessage) {
		synchronized (lock) {
			data.put(handler, imageMessage);
		}
	}
	
	public List<ImageMessage> getLastImages() {
		synchronized (lock) {
			return new ArrayList<>(data.values());
		}
	}
	
}
