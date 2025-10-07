public class Node {
    int id;
    String name;
    int demand; // số hành khách tại node
    boolean visited;

    public Node(int id, String name, int demand) {
        this.id = id;
        this.name = name;
        this.demand = demand;
        this.visited = false;
    }
}
