import java.util.*;
import java.util.Map.Entry;

public class KdTree {
	private int dimensions_;
	private Node root_ = null;
	private Node best_ = null;
	private double range_ = 0;
	private Map<KdTree.Node, Double> inRange_ = null;
	private double bestDistance_ = 0;
	private int visited_ = 0;

	public KdTree(int dimensions, List<Node> nodes) {
		dimensions_ = dimensions;
		root_ = makeTree(nodes, 0, nodes.size(), 0);
	}

	public Node findNearest(Node target) {
		if (root_ == null)
			throw new IllegalStateException("Tree is empty!");
		best_ = null;
		visited_ = 0;
		bestDistance_ = 0;
		nearest(root_, target, 0);
		return best_;
	}

	public Map<KdTree.Node, Double> findInRange(Node target, double range) {
		if (root_ == null)
			throw new IllegalStateException("Tree is empty!");
		visited_ = 0;
		inRange_ = new HashMap<>();
		range_ = range;
		range(root_, target, 0);
		return inRange_;
	}

	private void range(Node root, Node target, int index) {
		// return if nearest() was called from a leaf node
		if (root == null)
			return;
		++visited_;
		// get distance from current node to target node
		double d = root.euclideanDistance(target);
		if (d <= range_) {
			inRange_.put(root, d);
		}

		double dx = root.get(index) - target.get(index);
		index = (index + 1) % dimensions_;
		// explore branch that contains target
		range(dx > 0 ? root.left_ : root.right_, target, index);
		// break if other branch and range do not overlap
		if (Math.abs(dx) >= range_)
			return;
		// explore branch, as range reaches into branch
		range(dx > 0 ? root.right_ : root.left_, target, index);
	}

	private void nearest(Node root, Node target, int index) {
		if (root == null)
			return;
		++visited_;
		double d = root.distance(target);

		if (best_ == null || d < bestDistance_) {
			bestDistance_ = d;
			best_ = root;
		}
		if (bestDistance_ == 0)
			return;
		double dx = root.get(index) - target.get(index);
		index = (index + 1) % dimensions_;
		nearest(dx > 0 ? root.left_ : root.right_, target, index);
		if (dx * dx >= bestDistance_)
			return;
		nearest(dx > 0 ? root.right_ : root.left_, target, index);
	}

	public String inRangeToString() {
		StringBuilder s = new StringBuilder();
		for (KdTree.Node n : inRange_.keySet()) {
			s.append(n.toString());
			s.append(" - distance to target: ");
			s.append(inRange_.get(n));
			s.append("\n");
		}
		return s.toString();
	}

	public int visited() {
		return visited_;
	}

	public double distance() {
		return Math.sqrt(bestDistance_);
	}

	private Node makeTree(List<Node> nodes, int begin, int end, int index) {
		if (end <= begin)
			return null;
		int n = begin + (end - begin) / 2;
		Node node = QuickSelect.select(nodes, begin, end - 1, n, new NodeComparator(index));
		index = (index + 1) % dimensions_;
		node.left_ = makeTree(nodes, begin, n, index);
		node.right_ = makeTree(nodes, n + 1, end, index);
		return node;
	}

	private static class NodeComparator implements Comparator<Node> {
		private int index_;

		private NodeComparator(int index) {
			index_ = index;
		}

		public int compare(Node n1, Node n2) {
			return Double.compare(n1.get(index_), n2.get(index_));
		}
	}

	public static class Node {
		private double[] coords_;
		private Node left_ = null;
		private Node right_ = null;

		public Node(double[] coords) {
			coords_ = coords;
		}

		public Node(double x, double y) {
			this(new double[] { x, y });
		}

		public Node(double x, double y, double z) {
			this(new double[] { x, y, z });
		}
		
		double get(int index) {
			return coords_[index];
		}

		double distance(Node node) {
			double dist = 0;
			for (int i = 0; i < coords_.length; ++i) {
				double d = coords_[i] - node.coords_[i];
				dist += d * d;
			}
			return dist;
		}

		double euclideanDistance(Node node) {
			return Math.sqrt(Math.pow(coords_[0] - node.coords_[0], 2) + Math.pow(coords_[1] - node.coords_[1], 2)
					+ Math.pow(coords_[2] - node.coords_[2], 2));

		}

		public String toString() {
			StringBuilder s = new StringBuilder("(");
			for (int i = 0; i < coords_.length; ++i) {
				if (i > 0)
					s.append(", ");
				s.append((int) coords_[i]);
			}
			s.append(')');
			return s.toString();
		}
	}
}