package pertinence.cognibox.huffman;

import java.util.Map;

public class Data {

	Map<? extends Comparable<?>, Integer> map;
	
	public Data(Map<? extends Comparable<?>, Integer> data) {
		this.map = data;
	}
	
	public Map<? extends Comparable<?>, Integer> getMap() {
		return map;
	}

	public void setMap(Map<? extends Comparable<?>, Integer> map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return this.map.toString();
	}
}
