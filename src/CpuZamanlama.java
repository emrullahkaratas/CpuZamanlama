
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Process {
    String id;
    int arrivalTime;
    int burstTime;
    String priority;

    int completionTime;
    int turnaroundTime;
    int waitingTime;

    public Process(String id, int arrivalTime, int burstTime, String priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class CpuZamanlama extends JFrame {

    private DefaultTableModel tableModel;
    private JLabel resultLabel;

    public CpuZamanlama() {
        setTitle("FCFS İşlemci Zamanlayıcı - Emrullah");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLayout(new BorderLayout());

        String[] columnNames = {"Process ID", "Arrival Time", "Burst Time", "Priority", "Completion", "Turnaround", "Waiting"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        resultLabel = new JLabel("Hesaplanıyor...");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(resultLabel);
        add(bottomPanel, BorderLayout.SOUTH);

       
        processData();
    }

    private void processData() {
       
        String csvData = """
Process_ID,Arrival_Time,CPU_Burst_Time,Priority
P001,0,4,high
P002,2,7,normal
P003,4,10,low
P004,6,13,high
P005,8,16,normal
P006,10,19,low
P007,12,2,high
P008,14,5,normal
P009,16,8,low
P010,18,11,high
P011,20,14,normal
P012,22,17,low
P013,24,20,high
P014,26,3,normal
P015,28,6,low
P016,30,9,high
P017,32,12,normal
P018,34,15,low
P019,36,18,high
P020,38,1,normal
P021,40,4,low
P022,42,7,high
P023,44,10,normal
P024,46,13,low
P025,48,16,high
P026,50,19,normal
P027,52,2,low
P028,54,5,high
P029,56,8,normal
P030,58,11,low
P031,60,14,high
P032,62,17,normal
P033,64,20,low
P034,66,3,high
P035,68,6,normal
P036,70,9,low
P037,72,12,high
P038,74,15,normal
P039,76,18,low
P040,78,1,high
P041,80,4,normal
P042,82,7,low
P043,84,10,high
P044,86,13,normal
P045,88,16,low
P046,90,19,high
P047,92,2,normal
P048,94,5,low
P049,96,8,high
P050,98,11,normal
P051,100,14,low
P052,102,17,high
P053,104,20,normal
P054,106,3,low
P055,108,6,high
P056,110,9,normal
P057,112,12,low
P058,114,15,high
P059,116,18,normal
P060,118,1,low
P061,120,4,high
P062,122,7,normal
P063,124,10,low
P064,126,13,high
P065,128,16,normal
P066,130,19,low
P067,132,2,high
P068,134,5,normal
P069,136,8,low
P070,138,11,high
P071,140,14,normal
P072,142,17,low
P073,144,20,high
P074,146,3,normal
P075,148,6,low
P076,150,9,high
P077,152,12,normal
P078,154,15,low
P079,156,18,high
P080,158,1,normal
P081,160,4,low
P082,162,7,high
P083,164,10,normal
P084,166,13,low
P085,168,16,high
P086,170,19,normal
P087,172,2,low
P088,174,5,high
P089,176,8,normal
P090,178,11,low
P091,180,14,high
P092,182,17,normal
P093,184,20,low
P094,186,3,high
P095,188,6,normal
P096,190,9,low
P097,192,12,high
P098,194,15,normal
P099,196,18,low
P100,198,1,high
""";

        List<Process> processes = parseCSV(csvData);
        calculateFCFS(processes);
        updateTable(processes);
    }

   

    private List<Process> parseCSV(String data) {
        List<Process> list = new ArrayList<>();
        Scanner scanner = new Scanner(data);
        if (scanner.hasNextLine()) scanner.nextLine(); 

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(",");
            String id = parts[0];
            int arrival = Integer.parseInt(parts[1]);
            int burst = Integer.parseInt(parts[2]);
            String priority = parts[3];
            list.add(new Process(id, arrival, burst, priority));
        }
        scanner.close();
        return list;
    }

    private void calculateFCFS(List<Process> processes) {
        int currentTime = 0;
        for (Process p : processes) {
            if (currentTime < p.arrivalTime) {
                currentTime = p.arrivalTime;
            }
            p.completionTime = currentTime + p.burstTime;
            p.turnaroundTime = p.completionTime - p.arrivalTime;
            p.waitingTime = p.turnaroundTime - p.burstTime;
            currentTime = p.completionTime;
        }
    }


    private void updateTable(List<Process> processes) {
        double totalWait = 0;
        double totalTurnaround = 0;

        for (Process p : processes) {
            totalWait += p.waitingTime;
            totalTurnaround += p.turnaroundTime;

        
            Object[] rowData = {
                    p.id,
                    p.arrivalTime,
                    p.burstTime,
                    p.priority,
                    p.completionTime,
                    p.turnaroundTime,
                    p.waitingTime
            };
            tableModel.addRow(rowData);
        }

        
        double avgTat = totalTurnaround / processes.size();
        double avgWt = totalWait / processes.size();

        resultLabel.setText(String.format("Ortalama Turnaround Time: %.2f  |  Ortalama Waiting Time: %.2f", avgTat, avgWt));
    }


    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            CpuZamanlama gui = new CpuZamanlama();
            gui.setVisible(true);
        });
    }
}


