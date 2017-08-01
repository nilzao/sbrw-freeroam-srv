package world.soapboxrace.srv.protocol;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class FreeroamRangeCalc {

	public static void setVisibleTalkersToTalker(FreeroamTalker freeroamTalker) {
		HashMap<Integer, FreeroamTalker> freeroamTalkers = FreeroamAllTalkers.getFreeroamTalkers();
		Iterator<Entry<Integer, FreeroamTalker>> iterator = freeroamTalkers.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, FreeroamTalker> next = iterator.next();
			FreeroamTalker freeroamTalkerTmp = next.getValue();
			if (freeroamTalkerTmp != null && !freeroamTalkerTmp.getPort().equals(freeroamTalker.getPort())) {
				if (freeroamTalker.isReady() && freeroamTalkerTmp.isReady()) {
					freeroamTalker.getFreeroamVisibleTalkers().addVisibleTalkerToTalker(freeroamTalkerTmp, freeroamTalker);
				}
			}
		}
	}
}
