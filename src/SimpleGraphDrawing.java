import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SimpleGraphDrawing extends JPanel {
    private List<Point> nodes;
    private List<int[]> edges;
    private String[] nodeNames;
    private List<Integer> topologicalOrder;

    public SimpleGraphDrawing() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        nodeNames = new String[]{"A", "B", "C", "D", "E", "F", "G"};
        generateGraph();
        topologicalOrder = topologicalSort();
    }

    private void generateGraph() {

        nodes.add(new Point(100, 100));
        nodes.add(new Point(200, 50));
        nodes.add(new Point(300, 100));
        nodes.add(new Point(200, 200));
        nodes.add(new Point(100, 150));
        nodes.add(new Point(150, 250));
        nodes.add(new Point(250, 350));


        edges.add(new int[]{0, 1});
        edges.add(new int[]{1, 2});
        edges.add(new int[]{2, 3});
        edges.add(new int[]{3, 4});
        //edges.add(new int[]{4, 0}); // حذف این خط اگر گراف جهت‌دار بدون حلقه باشد
        edges.add(new int[]{1, 3});
        edges.add(new int[]{4, 5});
        edges.add(new int[]{5, 6});
    }


    private List<Integer> topologicalSort() {
        int n = nodes.size();
        int[] indegree = new int[n];
        List<Integer> order = new ArrayList<>();

        for (int[] edge : edges) {
            indegree[edge[1]]++;
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int node = queue.poll();
            order.add(node);

            for (int[] edge : edges) {
                if (edge[0] == node) {
                    indegree[edge[1]]--;
                    if (indegree[edge[1]] == 0) {
                        queue.add(edge[1]);
                    }
                }
            }
        }


        if (order.size() != n) {
            JOptionPane.showMessageDialog(this, "گراف شامل چرخه است و مرتب‌سازی توپولوژیک ممکن نیست.");
            return new ArrayList<>();
        }

        return order;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);


        for (int[] edge : edges) {
            Point start = nodes.get(edge[0]);
            Point end = nodes.get(edge[1]);
            g.drawLine(start.x, start.y, end.x, end.y);
        }

        int nodeSize = 30;
        for (int i = 0; i < nodes.size(); i++) {
            Point node = nodes.get(i);
            g.fillOval(node.x - nodeSize / 2, node.y - nodeSize / 2, nodeSize, nodeSize);


            g.setColor(Color.WHITE);
            g.drawString(nodeNames[i], node.x - 10, node.y + 5);
            g.setColor(Color.BLACK);
        }

        //  *

        if (!topologicalOrder.isEmpty()) {
            g.setColor(Color.BLUE);
            int xOffset = 500;

            for (int i = 0; i < topologicalOrder.size(); i++) {
                int nodeIndex = topologicalOrder.get(i);
                Point originalNode = nodes.get(nodeIndex);
                Point sortedNode = new Point(originalNode.x + xOffset, originalNode.y);


                if (i > 0) {
                    int prevNodeIndex = topologicalOrder.get(i - 1);
                    Point prevSortedNode = new Point(nodes.get(prevNodeIndex).x + xOffset, nodes.get(prevNodeIndex).y);
                    g.drawLine(prevSortedNode.x, prevSortedNode.y, sortedNode.x, sortedNode.y);
                }


                g.fillOval(sortedNode.x - nodeSize / 2, sortedNode.y - nodeSize / 2, nodeSize, nodeSize);
                g.setColor(Color.WHITE);
                g.drawString(nodeNames[nodeIndex], sortedNode.x - 10, sortedNode.y + 5);
                g.setColor(Color.BLUE);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graph Drawing with Topological Sort");
        SimpleGraphDrawing graphPanel = new SimpleGraphDrawing();
        frame.add(graphPanel);
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
