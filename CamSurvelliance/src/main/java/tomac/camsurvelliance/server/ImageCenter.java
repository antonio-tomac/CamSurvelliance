package tomac.camsurvelliance.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tomac.camsurvelliance.comunication.ImageMessage;

/**
 *
 * @author Antonio Tomac <antonio.tomac@mediatoolkit.com>
 */
public class ImageCenter {

	private final Object lock = new Object();
	private final Map<ProviderHandler, ImageMessage> data = new HashMap<>();
	private final Set<ChangeListener> listeners = new HashSet<>();

	public interface ChangeListener {

		void changed();
	}
	
	public void addChangeListener(ChangeListener changeListener) {
		listeners.add(changeListener);
	}

	public void removeHandlerProvider(ProviderHandler handler) {
		synchronized (lock) {
			data.remove(handler);
		}
		listeners.forEach(ChangeListener::changed);
	}

	public void addMessageImage(ProviderHandler handler, ImageMessage imageMessage) {
		synchronized (lock) {
			data.put(handler, imageMessage);
		}
		listeners.forEach(ChangeListener::changed);
	}

	public List<ImageMessage> getLastImages() {
		synchronized (lock) {
			return new ArrayList<>(data.values());
		}
	}

}
