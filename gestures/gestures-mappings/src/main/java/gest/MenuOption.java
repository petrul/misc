package gest;

import java.awt.Point;

/**
 * Modelled as a 2d point
 * @author dadi
 *
 */
public class MenuOption {
	
	public String name;
	public Point point;
	
	public MenuOption(String name, int x, int y) {
		this.name = name;
		this.point = new Point(x, y);
	}
	
	@Override
	public String toString() {
		return this.name;// + "[" + this.point.toString() +"]";
	}
}
