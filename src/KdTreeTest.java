import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class KdTreeTest {
	public static void main(String[] args) {
		int [] targetCoords = {0,0,0};

		testRandom(1000, 65000, 10000, targetCoords);
//		testSpecific();
	}

	private static KdTree.Node randomPoint(Random random, int max) {
		double x = random.nextInt(0, max);
		double y = random.nextInt(0, max);
		double z = random.nextInt(0, max);
		return new KdTree.Node(x, y, z);
	}

	private static void testSpecific(List<int[]> coordsList, int[] targetCoords, int range) {
		List<KdTree.Node> nodes = new ArrayList<>();
		for (int[] coord : coordsList) {
			int x = coord[0];
			int y = coord[1];
			int z = coord[2];
			if (0 > x || x > 65000 || 0 > y || y > 65000 || 0 > z || z > 65000)
				throw new IllegalArgumentException("Coordinates are not in Range (0-65000)");
			nodes.add(new KdTree.Node(x, y, z));
		}
		KdTree tree = new KdTree(3, nodes);
		
		int tx = targetCoords[0];
		int ty = targetCoords[1];
		int tz = targetCoords[2];
		if (0 > tx || tx > 65000 || 0 > ty || ty > 65000 || 0 > tz || tz > 65000)
			throw new IllegalArgumentException("Coordinates are not in Range (0-65000)");
		KdTree.Node target = new KdTree.Node(tx,ty,tz);
		Map<KdTree.Node, Double> inRange = tree.findInRange(target, range);

		System.out.println("Target: " + target);
		System.out.println("Range: " + range);
		System.out.println("Number of nodes in Range: " + inRange.size());
		System.out.println("nodes visited: " + tree.visited());
		System.out.println("Nodes in Range: ");
		System.out.println(tree.inRangeToString());
	}

	/*
	 * - points: number of Nodes to randomly generate
	 * - max: node's coordinates constrained by [{0-max,0-max,0-max}]
	 * - range
	 * - targetCoords: target node to be created at coordinates {0-max,0-max,0-max} 
	 */
	private static void testRandom(int points, int max, int range, int[] targetCoords) {
		Random random = new Random();
		List<KdTree.Node> nodes = new ArrayList<>();
		for (int i = 0; i < points; ++i)
			nodes.add(randomPoint(random, max));
		KdTree tree = new KdTree(3, nodes);
		int tx = targetCoords[0];
		int ty = targetCoords[1];
		int tz = targetCoords[2];
		KdTree.Node target = new KdTree.Node(tx, ty, tz);
		Map<KdTree.Node, Double> inRange = tree.findInRange(target, range);

		System.out.println("Random data (" + points + " points):");
		System.out.println("Target: " + target);
		System.out.println("Range: " + range);
		System.out.println("Number of nodes in Range: " + inRange.size());
		System.out.println("nodes visited: " + tree.visited());
		System.out.println("Nodes in Range: ");
		System.out.println(tree.inRangeToString());
	}

}
