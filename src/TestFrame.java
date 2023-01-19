import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

/**
 * @author Duyuetian
 * {@code @create} 2023-01-09 18:53
 */
public class TestFrame {
    private static JFrame testFrame = new JFrame("Test Page");
    private static ArrayList<ArrayList<String>> wordsList;
    private static int wordNum = 0;
    private static int wordCounter = 0;
    private static int correctCount = 0;
    private static boolean next;
    private static boolean isRight = false;

    private static final Calendar cDate = new GregorianCalendar();

    public static final Font f = new Font("font", Font.PLAIN,new JLabel().getFont().getSize() + 15);

    private static MainFrame.Mode mode;

    private static final JButton confirmButton = new JButton("submit");
    private static final JButton nextButton = new JButton("next");
    private static final JLabel wordLabel = new JLabel();
    private static final JLabel sideLabel = new JLabel();
    private static final JLabel propertyLabel = new JLabel();
    private static final JLabel optionLabel = new JLabel();
    private static final JLabel counterLabel = new JLabel();
    private static final JTextField answerTextField = new JTextField(20);
    private static final JLabel rightLabel = new JLabel();
    private static final JLabel wrongLabel = new JLabel();
    private static final ImageIcon right = new ImageIcon("img/right.png");
    private static final ImageIcon wrong = new ImageIcon("img/wrong.png");
    private static JPanel picturePanel = new JPanel();

    static class PackedData{
        int wordCount;
        String word;
        String wordProperty;
        String wordMeaning;
        String optional;

        public PackedData(int wordCount, String word, String wordProperty, String wordMeaning, String optional) {
            this.wordCount = wordCount;
            this.word = word;
            this.wordProperty = wordProperty;
            this.wordMeaning = wordMeaning;
            this.optional = optional;
        }
    }

    private static PackedData packedData;

    public static void initTestFrame(MainFrame.Mode currentMode, int num, int current) {
        mode = currentMode;
        wordNum = num;
        HistoryRecords.writeHeader(cDate.getTime(), wordNum, mode);
        if(currentMode == MainFrame.Mode.STUDY_ENGLISH || currentMode == MainFrame.Mode.STUDY_CHINESE) {
            wordsList = WordsList.getSequentialWords(current);
        }
        else {
            wordsList = WordsList.getRandomWords(num);
        }

        wordCounter = 0;
        correctCount = 0;

        confirmButton.setFont(new Font("font", Font.BOLD,new JLabel().getFont().getSize() + 15));
        confirmButton.setBorderPainted(false);
        confirmButton.setContentAreaFilled(false);
        confirmButton.setOpaque(true);
        confirmButton.setHorizontalTextPosition(SwingConstants.CENTER);
        confirmButton.setIcon(new ImageIcon("img/button.png"));
        confirmButton.addActionListener(e -> {
            if(e.getActionCommand().equals(confirmButton.getActionCommand()) && !Objects.equals(answerTextField.getText(), "") && !next) {
                String answer = answerTextField.getText();
                //history record.
                next = true;
                isRight = false;
                if(currentMode == MainFrame.Mode.STUDY_ENGLISH || currentMode == MainFrame.Mode.TEST_ENGLISH) {
                    if(sideLabel.getText().equalsIgnoreCase(answer) ||
                            (sideLabel.getText().contains("/") &&
                                    (sideLabel.getText().split("/")[0].equalsIgnoreCase(answer) ||
                                            sideLabel.getText().split("/")[1].equalsIgnoreCase(answer)))) {
                        isRight = true;
                        rightLabel.setVisible(true);
                        correctCount++;
                    } else {
                        isRight = false;
                        wrongLabel.setVisible(true);
                    }
                } else {
                    if(sideLabel.getText().toLowerCase().contains(answer.toLowerCase()) && !sideLabel.getText().equals("/")
                    && !sideLabel.getText().equals(",") && !sideLabel.getText().equals("，")) {
                        isRight = true;
                        rightLabel.setVisible(true);
                        correctCount++;
                    } else {
                        isRight = false;
                        wrongLabel.setVisible(true);
                    }
                }
                sideLabel.setVisible(true);
                optionLabel.setVisible(true);
                HistoryRecords.writeContent(packedData.wordCount, packedData.word, packedData.wordProperty, packedData.wordMeaning, packedData.optional, answer, isRight);
            }
        });
        nextButton.setFont(confirmButton.getFont());
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setOpaque(true);
        nextButton.setHorizontalTextPosition(SwingConstants.CENTER);
        nextButton.setIcon(new ImageIcon("img/button.png"));
        nextButton.addActionListener(e -> {
            if(e.getActionCommand().equals(nextButton.getActionCommand()) && answerTextField.getText() != null && next) {
                if(wordCounter == num) {
                    TestFrame.closeTestFrame();
                    double correctRate = (double)correctCount / wordCounter;
                    String message = "本次测试共" + num + "个单词, \n正确 " + correctCount + " / " + num + " 个\n正确率为 " + correctRate*100 + "%";
                    JOptionPane.showConfirmDialog(null,message,"你已完成本次测试", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("img/end.png"));
                    HistoryRecords.writeEnd(correctCount, wordCounter);

                    int num1, num2;
                    try (BufferedReader bf = new BufferedReader(new FileReader("utils.txt"))) {
                        String[] s = bf.readLine().split("\\s+");
                        num1 = Integer.parseInt(s[0]);
                        num2 = Integer.parseInt(s[1]);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(currentMode == MainFrame.Mode.STUDY_ENGLISH) {
                        num1 = current + wordNum;
                    }
                    else if(currentMode == MainFrame.Mode.STUDY_CHINESE) {
                        num2 = current + wordNum;
                    }
                    try (BufferedWriter bf = new BufferedWriter(new FileWriter("utils.txt"))) {
                        bf.write(num1 + " " + num2);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    MainFrame.openMainFrame();
                } else {
                    rightLabel.setVisible(false);
                    wrongLabel.setVisible(false);
                    showLabels(currentMode);
                    answerTextField.setText("");
                }
                next = false;
            }
        });
        openTestFrame();
    }

    public static void openTestFrame() {
        rightLabel.setIcon(right);
        wrongLabel.setIcon(wrong);
        answerTextField.setText("");

        sideLabel.setFont(f);
        wordLabel.setFont(f);
        counterLabel.setFont(f);
        propertyLabel.setFont(f);
        optionLabel.setFont(f);
        answerTextField.setFont(f);
        rightLabel.setFont(f);
        wrongLabel.setFont(f);

        answerTextField.setFont(new Font("font", Font.BOLD,new JLabel().getFont().getSize() + 30));

        rightLabel.setVisible(true);
        wrongLabel.setVisible(true);

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(900, 800));

        JPanel leftMost = new JPanel();
        leftMost.setPreferredSize(new Dimension(500, 450));

        //counter label.
        counterLabel.setPreferredSize(new Dimension(150, 200));
        leftMost.add(counterLabel);
        //word label.
        wordLabel.setPreferredSize(new Dimension(300, 200));
        leftMost.add(wordLabel);
        //property label.
        propertyLabel.setPreferredSize(new Dimension(100, 200));
        leftMost.add(propertyLabel);
        //optional label.
        optionLabel.setPreferredSize(new Dimension(350, 200));
        leftMost.add(optionLabel);

        picturePanel.setLocation(525, 0);
        picturePanel.setPreferredSize(new Dimension(350, 350));
        picturePanel.setOpaque(true);
        picturePanel.add(rightLabel);
        picturePanel.add(wrongLabel);
        rightLabel.setVisible(false);
        wrongLabel.setVisible(false);

        JPanel textAndAnswer = new JPanel(new GridLayout(2, 1));
        textAndAnswer.setPreferredSize(new Dimension(450, 350));
        textAndAnswer.add(answerTextField);
        textAndAnswer.add(sideLabel);

        JPanel rightMost = new JPanel(new GridLayout(2, 1));
        rightMost.setPreferredSize(new Dimension(400, 350));
        rightMost.add(confirmButton);
        rightMost.add(nextButton);

        mainPanel.add(leftMost);
        mainPanel.add(picturePanel);
        mainPanel.add(textAndAnswer);
        mainPanel.add(rightMost);

        testFrame = new JFrame("Test Page");
        testFrame.add(mainPanel);
        testFrame.setLocation(50, 50);
        testFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        showLabels(mode);
        testFrame.pack();
        testFrame.setVisible(true);
    }

    public static void closeTestFrame() {
        testFrame.setVisible(false);
    }

    public static void showLabels(MainFrame.Mode currentMode) {
        ArrayList<String> list = wordsList.get(wordCounter);
        String word = list.get(0);
        String wordProperty = list.get(1);
        String meaning = list.get(2);
        String optional = list.size() > 3 ? list.get(3) : "    ";
        String counter;

        counter = (wordCounter + 1) + " / " + wordNum;
        propertyLabel.setText(wordProperty);
        optionLabel.setText(optional);
        counterLabel.setText(counter);
        if(currentMode == MainFrame.Mode.TEST_ENGLISH || currentMode == MainFrame.Mode.STUDY_ENGLISH) {
            wordLabel.setText(meaning);
            sideLabel.setText(word);
        } else {
            wordLabel.setText(word);
            sideLabel.setText(meaning);
        }
        wordLabel.setVisible(true);
        propertyLabel.setVisible(true);
        sideLabel.setVisible(false);
        optionLabel.setVisible(false);
        counterLabel.setVisible(true);
        wordCounter++;
        packedData = new PackedData(wordCounter, word, wordProperty, meaning, optional);
    }

    private static boolean isMeaning(String s) {
        return !Character.isAlphabetic(s.charAt(0)) || s.contains(".");
    }
}
