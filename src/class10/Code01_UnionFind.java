package class10;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Code01_UnionFind {

	public static class Node<V> {
		V value;

		public Node(V v) {
			value = v;
		}
	}

	public static class UnionSet<V> {
		// V --> 节点信息
		public HashMap<V, Node<V>> nodes;
		// 子节点 --> 父节点
		public Map<Node<V>, Node<V>> parents;
		// 最终父节点 --> 子节点个数
		public Map<Node<V>, Integer> sizeMap;

		public UnionSet() {
			this.parents = new HashMap<>();
			sizeMap = new HashMap<>();
		}

		public UnionSet(List<V> values) {
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				parents.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		// 从点cur开始，一直往上找，找到不能再往上的代表点，返回
		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			while (cur != parents.get(cur)) {
				path.push(cur);
				cur = parents.get(cur);
			}
			// 优化：将经过的的子节点，连接上顶层的父节点。
			while (!path.isEmpty()) {
				parents.put(path.pop(), cur);
			}
			// cur头节点
			return cur;
		}
		// 判断 a, b 是否是 是否是同一个集合
		public boolean isSameSet(V a, V b) {
			if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
				return false;
			}
			return findFather(nodes.get(a)) == findFather(nodes.get(b));
		}
		// 将 节点A 和B 结合起来
		public void union(V a, V b) {
			if (!nodes.containsKey(a) || !nodes.containsKey(b)) {
				return;
			}
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			if (aHead != bHead) {
				int aSetSize = sizeMap.get(aHead);
				int bSetSize = sizeMap.get(bHead);
				Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
				Node<V> small = big == aHead ? bHead : aHead;
				parents.put(small, big);
				sizeMap.put(big, aSetSize + bSetSize);
				sizeMap.remove(small);
			}
		}
	}

}
