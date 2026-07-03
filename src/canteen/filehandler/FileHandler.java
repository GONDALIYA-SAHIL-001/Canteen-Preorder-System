package canteen.filehandler;

import canteen.model.*;
import java.io.*;
import java.util.*;

public class FileHandler {

    private static final String STUDENTS_FILE = "src/canteen/data/students.txt";
    private static final String MENU_FILE     = "src/canteen/data/menu.txt";
    private static final String ORDERS_FILE   = "src/canteen/data/orders.txt";
    private static final String REPORT_FILE   = "src/canteen/data/report.txt";

    // ===================== STUDENTS =====================

    public static void saveStudents(HashMap<String, Student> students) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(STUDENTS_FILE))) {
            for (Student s : students.values()) {
                // Format: studentId,password,name,walletBalance
                bw.write(s.getStudentId() + "," +
                         s.getPassword()   + "," +
                         s.getName()       + "," +
                         s.getWalletBalance());
                bw.newLine();
            }
           
        } catch (IOException e) {
            System.out.println("[Error] Could not save students: " + e.getMessage());
        }
    }

    public static HashMap<String, Student> loadStudents() {
        HashMap<String, Student> students = new HashMap<>();
        File file = new File(STUDENTS_FILE);

        if (!file.exists()) {
            System.out.println("[Info] No students file found. Starting fresh.");
            return students;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(STUDENTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Legacy Format: id,name,balance
                // New Format: id,password,name,balance
                // Name can have spaces but NOT commas — split on commas

                String[] parts = line.split(",", 4);
                if (parts.length < 3) continue;

                String id, password, name;
                double balance;

                if (parts.length == 3) {
                    // Legacy format
                    id = parts[0].trim();
                    password = "1234"; // Default password for legacy accounts
                    name = parts[1].trim();
                    balance = Double.parseDouble(parts[2].trim());
                } else {
                    // New format
                    id = parts[0].trim();
                    password = parts[1].trim();
                    name = parts[2].trim();
                    balance = Double.parseDouble(parts[3].trim());
                }

                students.put(id, new Student(id, password, name, balance));
            }

        } catch (IOException e) {
            System.out.println("[Error] Could not load students: " + e.getMessage());
        }
        return students;
    }

    // ===================== MENU =====================

    public static void saveMenu(ArrayList<FoodItem> menu) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (FoodItem item : menu) {
                // Format: TYPE,name,basePrice,isAvailable,extraField
                if (item instanceof VegItem) {
                    VegItem v = (VegItem) item;
                    bw.write("VEG,"      +
                             item.getName()       + "," +
                             item.getBasePrice()  + "," +
                             item.isAvailable()   + "," +
                             v.isJain());
                } else if (item instanceof NonVegItem) {
                    NonVegItem nv = (NonVegItem) item;
                    bw.write("NONVEG,"   +
                             item.getName()       + "," +
                             item.getBasePrice()  + "," +
                             item.isAvailable()   + "," +
                             nv.getMeatType());
                } else if (item instanceof Beverage) {
                    Beverage b = (Beverage) item;
                    bw.write("BEVERAGE," +
                             item.getName()       + "," +
                             item.getBasePrice()  + "," +
                             item.isAvailable()   + "," +
                             b.getTemperature());
                }
                bw.newLine();
            }
           
        } catch (IOException e) {
            System.out.println("[Error] Could not save menu: " + e.getMessage());
        }
    }

    public static ArrayList<FoodItem> loadMenu() {
        ArrayList<FoodItem> menu = new ArrayList<>();
        File file = new File(MENU_FILE);

        if (!file.exists()) {
            System.out.println("[Info] No menu file found. Loading default menu.");
            return menu;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Split into max 5 parts (extra field may contain spaces/commas in future)
                String[] parts = line.split(",", 5);
                if (parts.length < 5) continue;

                String  type      = parts[0].trim();
                String  name      = parts[1].trim();
                double  price     = Double.parseDouble(parts[2].trim());
                boolean available = Boolean.parseBoolean(parts[3].trim());
                String  extra     = parts[4].trim();

                FoodItem item = null;
                switch (type) {
                    case "VEG":
                        item = new VegItem(name, price, Boolean.parseBoolean(extra));
                        break;
                    case "NONVEG":
                        item = new NonVegItem(name, price, extra);
                        break;
                    case "BEVERAGE":
                        item = new Beverage(name, price, extra);
                        break;
                }

                if (item != null) {
                    item.setAvailable(available);
                    menu.add(item);
                }
            }
            
        } catch (IOException e) {
            System.out.println("[Error] Could not load menu: " + e.getMessage());
        }
        return menu;
    }

    public static void saveOrders(ArrayList<Order> orders) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDERS_FILE))) {
            for (Order order : orders) {

                // ── Header line ──────────────────────────────────────────
                // orderId,studentId,totalAmount,status,tokenNumber,tokenUsed,timeSlot
                bw.write(order.getOrderId()              + "," +
                         order.getStudentId()            + "," +
                         order.getTotalAmount()          + "," +
                         order.getOrderStatus()          + "," +
                         order.getToken().getTokenNumber() + "," +
                         order.getToken().isUsed()       + "," +
                         order.getToken().getTimeSlot());
                bw.newLine();

                // ── Items line: group identical items as  name:count  ────
                // Example: "Dal Chawal: 2 | Chai:1"
                LinkedHashMap<String, Integer> countMap = new LinkedHashMap<>();
                for (FoodItem item : order.getItemList()) {
                    countMap.merge(item.getName(), 1, Integer::sum);
                }
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Integer> e : countMap.entrySet()) {
                    sb.append(e.getKey()).append(":").append(e.getValue()).append("|");
                }
                // Remove trailing "|"
                if (sb.length() > 0) sb.setLength(sb.length() - 1);
                bw.write(sb.toString());
                bw.newLine();

                // ── Separator ────────────────────────────────────────────
                bw.write("---");
                bw.newLine();
            }
           
        } catch (IOException e) {
            System.out.println("[Error] Could not save orders: " + e.getMessage());
        }
    }

    public static ArrayList<Order> loadOrders(ArrayList<FoodItem> menu) {
        ArrayList<Order> orders = new ArrayList<>();
        File file = new File(ORDERS_FILE);

        if (!file.exists()) {
            System.out.println("[Info] No orders file found. Starting fresh.");
            return orders;
        }

        int maxOrderId  = 0;
        int maxTokenNum = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ORDERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Skip blank lines and separator lines
                if (line.isEmpty() || line.equals("---")) continue;

                // ── Parse header ─────────────────────────────────────────
                // orderId,studentId,totalAmount,status,tokenNumber,tokenUsed,timeSlot
                // timeSlot can contain spaces ("12:00 PM") — split into max 7 parts
                String[] parts = line.split(",", 7);
                if (parts.length < 7) continue;

                int    savedOrderId  = Integer.parseInt(parts[0].trim());
                String studentId     = parts[1].trim();
                double total         = Double.parseDouble(parts[2].trim());
                String statusStr     = parts[3].trim();
                int    savedTokenNum = Integer.parseInt(parts[4].trim());
                boolean tokenUsed    = Boolean.parseBoolean(parts[5].trim());
                String timeSlot      = parts[6].trim();

                // ── Parse items line ──────────────────────────────────────
                // Format: "name:count|name:count|..."
                String itemLine = br.readLine();
                ArrayList<FoodItem> itemList = new ArrayList<>();

                if (itemLine != null && !itemLine.trim().isEmpty()) {
                    String[] entries = itemLine.trim().split("\\|");
                    for (String entry : entries) {
                        entry = entry.trim();
                        if (entry.isEmpty()) continue;

                        // Split "name:count" — name may contain ":"? No, but be safe
                        int colonIdx = entry.lastIndexOf(':');
                        String itemName;
                        int    qty;
                        if (colonIdx > 0) {
                            itemName = entry.substring(0, colonIdx).trim();
                            try {
                                qty = Integer.parseInt(entry.substring(colonIdx + 1).trim());
                            } catch (NumberFormatException ex) {
                                qty = 1;
                            }
                        } else {
                            itemName = entry;
                            qty = 1;
                        }

                        // Find matching menu item
                        FoodItem found = null;
                        for (FoodItem fi : menu) {
                            if (fi.getName().equalsIgnoreCase(itemName)) {
                                found = fi;
                                break;
                            }
                        }
                        // Add exactly qty copies
                        if (found != null) {
                            for (int i = 0; i < qty; i++) {
                                itemList.add(found);
                            }
                        }
                    }
                }

                // ── Rebuild Order & Token objects ─────────────────────────
                Token token = new Token(timeSlot);
                token.setTokenNumber(savedTokenNum);
                if (tokenUsed) token.markUsed();

                Order order = new Order(studentId, itemList, token);
                order.setOrderId(savedOrderId);
                order.setTotalAmount(total);
                order.setOrderStatus(OrderStatus.valueOf(statusStr));
                orders.add(order);

                // Track max IDs
                if (savedOrderId  > maxOrderId)  maxOrderId  = savedOrderId;
                if (savedTokenNum > maxTokenNum) maxTokenNum = savedTokenNum;

                // Read (and discard) the "---" separator line
                br.readLine();
            }

            // Sync static counters so new orders get fresh unique IDs
            Order.setOrderCounter(maxOrderId  + 1);
            Token.setTokenCounter(maxTokenNum + 1);

           
        } catch (IOException e) {
            System.out.println("[Error] Could not load orders: " + e.getMessage());
        }
        return orders;
    }


    public static void saveReport(String reportContent) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REPORT_FILE, false))) {
            bw.write("====== DAILY REPORT ======");
            bw.newLine();
            bw.write(reportContent);
            bw.newLine();
      
        } catch (IOException e) {
            System.out.println("[Error] Could not save report: " + e.getMessage());
        }
    }
}