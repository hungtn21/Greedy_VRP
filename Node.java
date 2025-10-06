public class Node {
    int id;
    String name;
    int demand; // số hành khách tại node
    int earliest;
    int latest;
    boolean visited;

    public Node(int id, String name, int demand, int earliest, int latest) {
        this.id = id;
        this.name = name;
        this.demand = demand;
        this.earliest = earliest;
        this.latest = latest;
        this.visited = false;
    }
}
