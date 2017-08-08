package world.soapboxrace.srv.protocol;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class FreeroamRangeCalc {

	public static void setVisibleTalkersToTalker(FreeroamTalker freeroamTalker) {
		setVisibleClosestPlayers(freeroamTalker);
		freeroamTalker.getFreeroamVisibleTalkers().removeIdleVisibleTalkerToTalker();
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

	private static List<FreeroamTalker> setVisibleClosestPlayers(FreeroamTalker freeroamTalker) {
		List<FreeroamTalker> closestList = FreeroamAllTalkers.getFreeroamTalkers() //
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
		List<FreeroamTalker> visibleTalkers = freeroamTalker.getFreeroamVisibleTalkers().getVisibleTalkers();
		List<FreeroamTalker> alreadyThere = copyList(closestList);
		alreadyThere.retainAll(visibleTalkers);
		List<FreeroamTalker> objectsToAdd = copyList(closestList);
		objectsToAdd.removeAll(alreadyThere);
		List<FreeroamTalker> objectsToRemove = copyList(visibleTalkers);
		objectsToRemove.removeAll(alreadyThere);
		for (FreeroamTalker freeroamTalkerToRemove : objectsToRemove) {
			freeroamTalker.getFreeroamVisibleTalkers().removeVisibleTalkerToTalker(freeroamTalkerToRemove);
		}
		for (FreeroamTalker freeroamTalkerToAdd : objectsToAdd) {
			freeroamTalker.getFreeroamVisibleTalkers().addVisibleTalkerToTalker(freeroamTalkerToAdd, freeroamTalker);
		}
		return closestList;
	}

	private static List<FreeroamTalker> copyList(List<FreeroamTalker> originalList) {
		List<FreeroamTalker> copiedList = new LinkedList<>();
		for (FreeroamTalker freeroamTalkerTmp : originalList) {
			copiedList.add(freeroamTalkerTmp);
		}
		return copiedList;
	}

}
