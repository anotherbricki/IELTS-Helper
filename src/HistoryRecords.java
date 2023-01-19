import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * @author Duyuetian
 * {@code @create} 2023-01-09 18:54
 */
public class HistoryRecords {
    //TODO: Add a scroll bar in history frame.
    private static ArrayList<ArrayList<String>> recordList = new ArrayList<>();
    private static final ArrayList<String> tempRecord = new ArrayList<>();

    public HistoryRecords() {
    }

    public static void initHistoryRecords() throws IOException {
        recordList = new ArrayList<>();
        try(BufferedReader in = new BufferedReader(new FileReader("history.txt"))) {
            String s;
            while((s = in.readLine()) != null) {
                ArrayList<String> list = new ArrayList<>();
                list.add(s);
                int wordNum = Integer.parseInt(s.substring(s.indexOf("了")+1, s.indexOf("个")).strip());
                for (int i = 0; i <= wordNum; i++) {  //contains the end.
                    s = in.readLine();
                    list.add(s);
                }
                recordList.add(list);
            }
        }
    }

    /**
     * the format of record list:
     * every record is saved in an item of recordList, which is an ArrayList<String>.
     * format:
     * Header: index 0.
     * Content: index 1 ~ record.size() - 2.
     * End: index  record.size() - 1.
     * @throws IOException for init historyRecords
     */
    public static void loadHistoryFrame() throws IOException {
        initHistoryRecords();
        JFrame historyFrame = new JFrame("历史记录");
        JPanel[] panels = makePanels(recordList.size());
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        for (JPanel panel : panels) {
            historyPanel.add(panel);
            historyPanel.add(new JSeparator());
        }
        JScrollPane jScrollPane = new JScrollPane(historyPanel);
        historyFrame.getContentPane().add(jScrollPane);
        historyFrame.setLocation(50, 50);
        historyFrame.setPreferredSize(new Dimension(1200, 800));
        historyFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        historyFrame.pack();
        historyFrame.setVisible(true);
    }

    private static JPanel[] makePanels(int recordCount) {
        JPanel[] panels = new JPanel[recordCount];
        for (int i = 0; i < recordCount; i++) {
            panels[i] = new JPanel(new BorderLayout());
            ArrayList<String> list = recordList.get(i);
            JPanel labels = new JPanel(new GridLayout(2, 0));
            JLabel headLabel = new JLabel(list.get(0));
            JLabel endLabel = new JLabel(list.get(list.size()-1));
            headLabel.setFont(MainFrame.f);
            endLabel.setFont(MainFrame.f);
            labels.add(headLabel);
            labels.add(endLabel);
            JButton button = new JButton("查看详细历史记录");
            button.setFont(MainFrame.f);
            final int iFinalCopy = i;
            button.addActionListener(e -> {
                if(e.getActionCommand().equals(button.getActionCommand())) {
                    showInfoFrame(iFinalCopy);
                }
            });
            panels[i].add(labels);
            panels[i].add(button, BorderLayout.EAST);
        }
        return panels;
    }

    private static void showInfoFrame(int recordNumber) {
        JFrame infoFrame = new JFrame("详细历史记录");
        ArrayList<String> list = recordList.get(recordNumber);
        infoFrame.setLayout(new GridLayout(list.size()-2, 1));
        for (int i = 1; i < list.size()-1; i++) {
            JLabel label = new JLabel(list.get(i));
            label.setFont(MainFrame.f);
            infoFrame.add(label);
            //label.setPreferredSize(new Dimension(1000, 100));
        }
        infoFrame.setLocation(100, 100);
        infoFrame.setPreferredSize(new Dimension(1200, 800));
        infoFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        infoFrame.pack();
        infoFrame.setVisible(true);
    }

    public static void writeHeader(Date date, int wordNum, MainFrame.Mode mode) {
        StringBuilder sb;
        sb = new StringBuilder()
                .append(date.toString())
                .append(" 测试了%4d个单词".formatted(wordNum))
                .append(" 模式： ")
                .append(mode.toString())
                .append("\n");
        tempRecord.add(sb.toString());
    }

    public static void writeContent(int wordCount, String word, String wordProperty, String wordMeaning, String optional, String yourAnswer, boolean isRight) {
        StringBuilder sb;
        sb = new StringBuilder()
                .append("第")
                .append(String.format("%4d个单词: ", wordCount))
                .append(word)
                .append(" ")
                .append(Objects.requireNonNullElse(wordProperty, ""))
                .append(" ")
                .append(wordMeaning)
                .append(" ")
                .append(Objects.requireNonNullElse(optional, " "))
                .append("你的答案： ")
                .append(yourAnswer)
                .append(" ")
                .append(isRight ? "答案正确\n" : "答案错误\n");
        tempRecord.add(sb.toString());
    }

    public static void writeEnd(int correctNum, int wordNum) {
        double correctRate = ((double)correctNum / wordNum) * 100;
        StringBuilder sb;
        sb = new StringBuilder()
                .append("正确")
                .append(String.format("%4d个单词, ", correctNum))
                .append("正确率为 ")
                .append(correctRate)
                .append("%\n");
        tempRecord.add(sb.toString());
        try(BufferedWriter out = new BufferedWriter(new FileWriter("history.txt", true)))//append mode.
        {
            for (String s : tempRecord) {
                out.write(s);
            }
            tempRecord.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
