import java.util.*;

public class MainOriginalGreedyVRP {
    public static void main(String[] args) {
        System.out.println("🔬 TEST THUẬT TOÁN ORIGINAL GREEDY VRP");
        System.out.println("=".repeat(50));
        
        // Test case: Trường hợp thể hiện vấn đề phân mảnh capacity
        testCapacityFragmentationCase();
    }
    
    private static void testCapacityFragmentationCase() {
        System.out.println("\n📋 TEST CASE: PHÂN MẢNH CAPACITY");
        System.out.println("Mục tiêu: Thể hiện vấn đề xe 1 đón [1,2,2] trước, xe 2 không thể đón hết [3,2,2]");
        
        // ===== Tạo danh sách node =====
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, "Depot", 0));           // depot
        nodes.add(new Node(1, "Khach1", 1));          // demand = 1
        nodes.add(new Node(2, "Khach2", 2));          // demand = 2  
        nodes.add(new Node(3, "Khach3", 2));          // demand = 2
        nodes.add(new Node(4, "Khach4", 3));          // demand = 3
        nodes.add(new Node(5, "Khach5", 2));          // demand = 2
        nodes.add(new Node(6, "Khach6", 2));          // demand = 2
        nodes.add(new Node(7, "StartPoint", 0));      // điểm khởi hành cuối

        // ===== Tạo đội xe =====
        List<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(new Vehicle(1, 6, 0)); // Xe 1, sức chứa 6
        vehicles.add(new Vehicle(2, 6, 0)); // Xe 2, sức chứa 6
        // Tổng capacity = 12, tổng demand = 12 → Vừa đủ

        // ===== Tạo ma trận khoảng cách đặc biệt =====
        int n = nodes.size();
        int[][] distanceMatrix = createSpecialDistanceMatrix(n);

        System.out.println("📊 Dữ liệu:");
        System.out.println("• Tổng demand: 12 người [1,2,2,3,2,2]");
        System.out.println("• Tổng capacity: 12 chỗ (2 xe × 6 chỗ)");
        System.out.println("• Thiết kế: Khách1,2,3 gần depot → Xe 1 sẽ đón [1,2,2] = 5 chỗ");
        System.out.println("• Kết quả dự kiến: Xe 2 chỉ đón được Khach4 [3] = 3 chỗ, bỏ sót Khach5,6 [2,2]");

        // ===== Hiển thị ma trận khoảng cách =====
        printDistanceMatrix(distanceMatrix, nodes);

        // ===== Giải bằng OriginalGreedyVRP =====
        OriginalGreedyVRP solver = new OriginalGreedyVRP(distanceMatrix, nodes, vehicles, 0, 7);
        solver.solve();
        solver.printSolution();
    }
    
    private static int[][] createSpecialDistanceMatrix(int n) {
        int[][] matrix = new int[n][n];
        
        // Định nghĩa khoảng cách từ Depot đến các node
        int[] depotDistances = {0, 5, 7, 9, 12, 35, 40, 10}; // [Depot, K1, K2, K3, K4, K5, K6, StartPoint]
        
        // Thiết lập khoảng cách từ/đến Depot và StartPoint
        for (int i = 0; i < n; i++) {
            matrix[0][i] = depotDistances[i];  // Depot đến các node
            matrix[i][0] = depotDistances[i];  // Các node về Depot
            matrix[i][7] = depotDistances[i] + 3; // Các node đến StartPoint
            matrix[7][i] = matrix[i][7];       // StartPoint đến các node
        }
        matrix[7][7] = 0; // StartPoint đến chính nó
        
        // Khoảng cách giữa các khách (nhóm gần: 1,2,3 và nhóm xa: 4,5,6)
        int[][] customerDistances = {
            {0, 3, 6, 29, 32, 35}, // Khach1 đến các khách khác
            {3, 0, 4, 26, 29, 32}, // Khach2 đến các khách khác  
            {6, 4, 0, 23, 26, 29}, // Khach3 đến các khách khác
            {29, 26, 23, 0, 25, 30}, // Khach4 đến các khách khác
            {32, 29, 26, 25, 0, 8},  // Khach5 đến các khách khác
            {35, 32, 29, 30, 8, 0}   // Khach6 đến các khách khác
        };
        
        // Điền ma trận khoảng cách giữa các khách
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 6; j++) {
                matrix[i][j] = customerDistances[i-1][j-1];
            }
        }
        
        return matrix;
    }
    
    private static void printDistanceMatrix(int[][] matrix, List<Node> nodes) {
        System.out.println("\n🗺️ MA TRẬN KHOẢNG CÁCH:");
        System.out.print("        ");
        for (Node node : nodes) {
            System.out.printf("%8s", node.name);
        }
        System.out.println();
        
        for (int i = 0; i < nodes.size(); i++) {
            System.out.printf("%8s", nodes.get(i).name);
            for (int j = 0; j < nodes.size(); j++) {
                System.out.printf("%8d", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }
}
