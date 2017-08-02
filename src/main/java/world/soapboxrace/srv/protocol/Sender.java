package world.soapboxrace.srv.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sender implements Runnable {

	public Sender() {
		ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleWithFixedDelay(this, 1000L, 30L, TimeUnit.MILLISECONDS);
	}

	@Override
	public void run() {
		HashMap<Integer, FreeroamTalker> freeroamTalkers = FreeroamAllTalkers.getFreeroamTalkers();
		Iterator<Entry<Integer, FreeroamTalker>> iterator = freeroamTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, FreeroamTalker> next = iterator.next();
			FreeroamTalker freeroamTalker = next.getValue();
			if (freeroamTalker != null) {
				if (freeroamTalker.isReady()) {
					freeroamTalker.broadcastPlayersInfo();
				}
			}
		}
	}

}
