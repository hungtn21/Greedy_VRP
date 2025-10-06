import java.util.*;

public class Vehicle {
    int id;
    int capacity;
    int load;
    List<Node> route;
    int currentTime;
    int currentNode;

    public Vehicle(int id, int capacity, int depotId) {
        this.id = id;
        this.capacity = capacity;
        this.load = 0;
        this.route = new ArrayList<>();
        this.currentTime = 0;
        this.currentNode = depotId;
    }

    public void addNode(Node node, int travelTime) {
        currentTime += travelTime;
        if (currentTime < node.earliest) {
            currentTime = node.earliest; // chờ đến earliest
        }
        this.load += node.demand;
        this.route.add(node);
        this.currentNode = node.id;
        node.visited = true;
    }
}
