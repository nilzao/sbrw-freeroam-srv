package world.soapboxrace.srv.protocol;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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

	private static List<FreeroamTalker> calculateClosestPlayers(FreeroamTalker freeroamTalker) {
		return FreeroamAllTalkers.getFreeroamTalkers() //
				.entrySet() //
				.stream() //
				.filter(t -> !t.getValue().equals(freeroamTalker)) //
				.filter(t -> t.getValue().isReady()) //
				.sorted(Comparator.comparingDouble(t -> { //
					int[] self = { //
							freeroamTalker.getXPos(), //
							freeroamTalker.getYPos(),//
					};//
					int[] them = { //
							t.getValue().getXPos(), //
							t.getValue().getYPos()//
					};//
					return Math.sqrt(Math.pow(them[0] - self[0], 2) + Math.pow(them[1] - self[1], 2));//
				}))//
				.limit(2)//
				.map(Entry::getValue)//
				.collect(Collectors.toList());
	}
}
