package gest;

import java.util.ArrayList;

/**
 * A {@link Gesture} is a set of {@link Compass} directions.
 * @author dadi
 *
 */
public class Gesture {
	ArrayList<Compass> elems;
	
	public Gesture(Compass... directions) {
		this.elems = new ArrayList<Compass>(directions.length);
		
		for (Compass d : directions)
			this.elems.add(d);
	}
	
	public int size() {
		return this.elems.size();
	}
	
	public Compass getAt(int index) {
		return elems.get(index);
	}
	
	public int distanceFrom(Gesture source) {
		if (this.size() != source.size())
			throw new IllegalArgumentException("the two gestures should have the same size");
		int result = 0;
		for (int i = 0; i < this.elems.size(); i++) {
			Compass srcDir = source.getAt(i);
			Compass destDir = this.getAt(i);
			result += Compass.cc(destDir, srcDir);
		}
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Compass c : this.elems) 
			sb.append(c.toString()).append(' ');
		
		return sb.toString();
	}
}
