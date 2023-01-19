import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Duyuetian
 * {@code @create} 2023-01-09 18:53
 */
public class MainFrame {
    private static final JFrame mainFrame = new JFrame("IELTS Helper");

    public static final Font f = new Font("font", Font.PLAIN,new JLabel().getFont().getSize() + 10);


    enum Mode {
        STUDY_ENGLISH, STUDY_CHINESE, TEST_ENGLISH, TEST_CHINESE
    }

    public static Mode currentMode;

    public static void initMainFrame() throws IOException {

        WordsList.initWordsList();

        JButton studyEnglish = new JButton("学习模式-英语");
        JButton studyChinese = new JButton("学习模式-中文");
        JButton testEnglish = new JButton("抽背模式-英语");
        JButton testChinese = new JButton("抽背模式-中文");
        JButton history = new JButton("历史记录");
        JButton blame = new JButton("制作人员");

        studyChinese.setFont(f);
        studyEnglish.setFont(f);
        testChinese.setFont(f);
        testEnglish.setFont(f);
        history.setFont(f);
        blame.setFont(f);

        studyEnglish.addActionListener(e -> {
            if(e.getActionCommand().equals(studyEnglish.getActionCommand())) {
                currentMode = Mode.STUDY_ENGLISH;
                closeMainFrame();
                BufferedReader in;
                int currentNum;
                try {
                    in = new BufferedReader(new FileReader("utils.txt"));
                    currentNum = Integer.parseInt(
                                    Objects.requireNonNullElse(
                                        Objects.requireNonNullElse(
                                                in.readLine(), "0 0")
                                                    .split(" ")[0], "0"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                int num = Integer.parseInt(JOptionPane.showInputDialog(null, "请输入抽背的单词数量："));
                TestFrame.initTestFrame(currentMode, num, currentNum);
            }
        });

        studyChinese.addActionListener(e -> {
            if(e.getActionCommand().equals(studyChinese.getActionCommand())) {
                currentMode = Mode.STUDY_CHINESE;
                closeMainFrame();
                BufferedReader in;
                int currentNum;
                try {
                    in = new BufferedReader(new FileReader("utils.txt"));
                    currentNum = Integer.parseInt(
                            Objects.requireNonNullElse(
                                    Objects.requireNonNullElse(
                                            in.readLine(), "0 0")
                                                .split(" ")[1], "0"));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                int num = Integer.parseInt(JOptionPane.showInputDialog(null, "请输入抽背的单词数量："));
                JOptionPane.showConfirmDialog(null, "中文答案仅供参考，不用在意成绩，意思对即可哦~", "温馨提示", JOptionPane.OK_CANCEL_OPTION);
                TestFrame.initTestFrame(currentMode, num, currentNum);
            }
        });

        testEnglish.addActionListener(e -> {
            if(e.getActionCommand().equals(testEnglish.getActionCommand())) {
                currentMode = Mode.TEST_ENGLISH;
                int num = Integer.parseInt(JOptionPane.showInputDialog(null, "请输入抽背的单词数量："));
                closeMainFrame();
                TestFrame.initTestFrame(currentMode, num, 0);
            }
        });

        testChinese.addActionListener(e -> {
            if(e.getActionCommand().equals(testChinese.getActionCommand())) {
                currentMode = Mode.TEST_CHINESE;
                int num = Integer.parseInt(JOptionPane.showInputDialog(null, "请输入抽背的单词数量："));
                closeMainFrame();
                JOptionPane.showConfirmDialog(null, "中文答案仅供参考，不用在意成绩，意思对即可哦~", "温馨提示", JOptionPane.OK_CANCEL_OPTION);
                TestFrame.initTestFrame(currentMode, num, 0);
            }
        });

        history.addActionListener(e -> {
            if(e.getActionCommand().equals(history.getActionCommand())) {
                try {
                    HistoryRecords.loadHistoryFrame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        blame.addActionListener(e -> {
            if(e.getActionCommand().equals(blame.getActionCommand())) {
                JOptionPane.showConfirmDialog(null, "Code creator: Anotherbricki ", "制作人员", JOptionPane.OK_CANCEL_OPTION);
            }
        });

        JLabel title = new JLabel("IELTS Helper");
        title.setFont(new Font("myFont", Font.BOLD, 50));
        mainFrame.add(title);
        JPanel blank = new JPanel();
        blank.setPreferredSize(new Dimension(200, 400));
        mainFrame.add(blank, BorderLayout.WEST);
        JPanel buttons = new JPanel();
        buttons.setPreferredSize(new Dimension(700, 300));
        buttons.setLayout(new GridLayout(3, 2));
        buttons.add(studyEnglish);
        buttons.add(studyChinese);
        buttons.add(testEnglish);
        buttons.add(testChinese);
        buttons.add(history);
        buttons.add(blame);
        mainFrame.add(buttons, BorderLayout.SOUTH);
        mainFrame.setLocation(450, 150);
        mainFrame.setPreferredSize(new Dimension(700, 700));
        mainFrame.pack();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        openMainFrame();
    }

    public static void openMainFrame() {
        mainFrame.setVisible(true);
    }

    public static void closeMainFrame() {
        mainFrame.setVisible(false);
    }
}
