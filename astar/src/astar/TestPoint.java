package astar;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class TestPoint {

	@Test
	public void test() {
		AStar aStar = new AStar();
		Set<Point> barrier = new HashSet<Point>();
		/*
		 * for (int j = 30; j > 15; j--) { 
		 * 		for (int i = 20; i < 50; i++) {
		 * 			barrier.add(new Point(j, i));
		 * 		} 
		 * }
		 */

		for (int j = 30; j > 15; j--) {
			barrier.add(new Point(j, 20));
		}
		/*
		 * for (int j = 30; j > 15; j--) { barrier.add(new Point(j, 50)); }
		 */
		for (int i = 20; i < 50; i++) {
			barrier.add(new Point(30, i));
		}

		for (int i = 20; i < 55; i++) {
			barrier.add(new Point(15, i));
		}
		long start = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			aStar = new AStar();
			aStar.move(10, 25, 28, 40, barrier);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
		Set<Point> set = new HashSet<Point>();
		Point endPoint = aStar.getEndPoint();
		Point startPoint = aStar.getStartPoint();
		Map<String, Point> openMap = aStar.getOpenMap();
		Map<String, Point> closeMap = aStar.getCloseMap();
		set = TestPoint.get(endPoint, set);
		/**
		 * ÏÔÊ¾×î¼ÑÂ·¾¶
		 */
		System.out.println(aStar.getEndPoint().getKey());
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 70; j++) {
				Point p = new Point(j, i);
				if (p.equals(aStar.getEndPoint())) {
					System.out.print("o");
				} else if (p.equals(startPoint)) {
					System.out.print("^");
				} else {
					if (set.contains(p)) {
						System.out.print("@");
					} else if (barrier.contains(p)) {
						System.out.print("#");
					} else {
						System.out.print("*");
					}

				}
				System.out.print(" ");
			}
			System.out.println();
		}

		System.out
				.println("--------------------------------------------------------------------------------------------------------");
		/**
		 * É¨ÃèµÄ·¶Î§
		 */
		for (int i = 0; i < 70; i++) {
			for (int j = 0; j < 70; j++) {
				Point p = new Point(j, i);
				if (p.equals(endPoint)) {
					System.out.print("o");
				} else if (p.equals(startPoint)) {
					System.out.print("^");
				} else {
					if (openMap.containsKey(p.getKey())) {
						System.out.print("%");
					} else if (closeMap.containsKey(p.getKey())) {
						System.out.print("@");
					} else if (barrier.contains(p)) {
						System.out.print("#");
					} else {
						System.out.print("*");
					}

				}
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	public static Set<Point> get(Point p, Set<Point> set) {
		if (p != null) {
			set.add(p);
		}
		Point pp = p.prev;
		if (pp != null) {
			TestPoint.get(pp, set);
		} else {
			return set;
		}
		return set;
	}

}
