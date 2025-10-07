import java.util.List;

public class OriginalGreedyVRP {
    int[][] timeMatrix;
    List<Node> nodes;
    List<Vehicle> vehicles;
    int depotId;
    int startPointId;

    public OriginalGreedyVRP(int[][] timeMatrix, List<Node> nodes, List<Vehicle> vehicles, int depotId, int startPointId) {
        this.timeMatrix = timeMatrix;
        this.nodes = nodes;
        this.vehicles = vehicles;
        this.depotId = depotId;
        this.startPointId = startPointId;
    }

    /**
     * THUẬT TOÁN GREEDY VRPTW TRUYỀN THỐNG
     * - Chiến thuật: c
     */
    public void solve() {
        System.out.println("🔄 Chạy thuật toán Greedy VRPTW truyền thống...");
        
        for (Vehicle v : vehicles) {
            v.route.add(nodes.get(depotId)); // bắt đầu ở depot
            
            while (true) {
                Node next = findNextNode(v);
                if (next == null) {
                    // không còn node khả thi -> đi đến startPoint
                    int travelTime = timeMatrix[v.currentNode][startPointId];
                    v.addNode(nodes.get(startPointId), travelTime);
                    break;
                }
                int travelTime = timeMatrix[v.currentNode][next.id];
                v.addNode(next, travelTime);
            }
        }
    }

    /**
     * Thuật toán tìm node tiếp theo cho xe
     * Chiến lược: Chọn khách hàng gần nhất có thể phục vụ được
     */
    private Node findNextNode(Vehicle v) {
        Node best = null;
        int bestCost = Integer.MAX_VALUE;
        
        for (Node node : nodes) {
            // Kiểm tra node chưa được thăm và không phải depot/startPoint
            if (!node.visited && node.id != depotId && node.id != startPointId) {
                
                // Kiểm tra constraint capacity
                if (v.load + node.demand <= v.capacity) {
                    
                    int travelTime = timeMatrix[v.currentNode][node.id];
                    
                    if (travelTime < bestCost) {
                        bestCost = travelTime;
                        best = node;
                    }
                }
            }
        }
        
        return best;
    }

    public double calculateAverageTime() {
        int total = 0;
        for (Vehicle v : vehicles) {
            total += v.currentTime;
        }
        return (double) total / vehicles.size();
    }

    public void printSolution() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("📋 KẾT QUẢ THUẬT TOÁN GREEDY TRUYỀN THỐNG");
        System.out.println("=".repeat(50));
        
        int totalCustomersServed = 0;
        int totalDemandServed = 0;
        
        for (Vehicle v : vehicles) {
            System.out.print("Xe " + v.id + " (capacity: " + v.capacity + ", load: " + v.load + "): ");
            
            int customerCount = 0;
            for (Node n : v.route) {
                System.out.print(n.name);
                if (n.demand > 0) { // không tính depot và startPoint
                    customerCount++;
                    totalDemandServed += n.demand;
                }
                if (v.route.indexOf(n) < v.route.size() - 1) {
                    System.out.print(" → ");
                }
            }
            
            System.out.println(" (Khách: " + customerCount + ", Thời gian: " + v.currentTime + ")");
            totalCustomersServed += customerCount;
        }
        
        // Thống kê cơ bản
        System.out.println("\n📊 THỐNG KÊ:");
        System.out.println("• Tổng số khách được phục vụ: " + totalCustomersServed);
        System.out.println("• Tổng demand được phục vụ: " + totalDemandServed);
        System.out.println("• Thời gian trung bình: " + String.format("%.2f", calculateAverageTime()));
        
        // Kiểm tra khách hàng chưa được phục vụ
        int unservedCount = 0;
        int unservedDemand = 0;
        System.out.println("\n⚠️ KHÁCH HÀNG CHƯA ĐƯỢC PHỤC VỤ:");
        
        for (Node node : nodes) {
            if (!node.visited && node.id != depotId && node.id != startPointId) {
                System.out.println("   ❌ " + node.name + " (demand: " + node.demand + ")");
                unservedCount++;
                unservedDemand += node.demand;
            }
        }
        
        if (unservedCount == 0) {
            System.out.println("   ✅ Tất cả khách hàng đã được phục vụ!");
        } else {
            System.out.println("\n🚨 VẤN ĐỀ PHÁT HIỆN:");
            System.out.println("• Số khách chưa được phục vụ: " + unservedCount);
            System.out.println("• Demand chưa được phục vụ: " + unservedDemand);
            
            // Phân tích nguyên nhân
            int totalAvailableCapacity = 0;
            for (Vehicle v : vehicles) {
                totalAvailableCapacity += (v.capacity - v.load);
            }
            
            System.out.println("• Tổng capacity còn lại: " + totalAvailableCapacity);
            
            if (totalAvailableCapacity >= unservedDemand) {
                System.out.println("🔍 NGUYÊN NHÂN: Capacity bị PHÂN MẢNH!");
                System.out.println("   → Có đủ chỗ tổng cộng nhưng bị chia nhỏ ở nhiều xe");
                System.out.println("   → Thuật toán greedy đã chọn khách gần mà không tối ưu capacity");
            } else {
                System.out.println("🔍 NGUYÊN NHÂN: KHÔNG ĐỦ CAPACITY tổng cộng");
            }
        }
        
        // Phân tích hiệu quả sử dụng capacity
        System.out.println("\n📈 PHÂN TÍCH CAPACITY:");
        for (Vehicle v : vehicles) {
            double utilizationRate = (double) v.load / v.capacity * 100;
            String status = utilizationRate >= 90.0 ? "🔥" : 
                           utilizationRate >= 70.0 ? "✅" : 
                           utilizationRate >= 50.0 ? "⚠️" : "❌";
            System.out.println("   " + status + " Xe " + v.id + ": " + v.load + "/" + v.capacity + 
                             " (" + String.format("%.1f", utilizationRate) + "%)");
        }
        
        System.out.println("\n💡 LƯU Ý: Đây là thuật toán Greedy truyền thống với các hạn chế:");
        System.out.println("   • Chỉ tối ưu khoảng cách, không tối ưu capacity");
        System.out.println("   • Có thể bỏ sót khách hàng do capacity bị phân mảnh");
        System.out.println("   • Không có cơ chế xử lý các trường hợp đặc biệt");
    }
}
