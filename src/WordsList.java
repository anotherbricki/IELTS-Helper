import java.io.*;
import java.util.*;

/**
 * @author Duyuetian
 * {@code @create} 2023-01-09 18:21
 */
public class WordsList {
    /**
     * format of wordlist:
     * index 0 : word
     * index 1 : wordProperty
     * index 2 : meanings
     * index 3 : optional field.
     */
    private static final List<ArrayList<String>> wordsList = new ArrayList<>();
    private static final Random randomGenerator = new Random();
    public static final int WORDS_COUNT = 2426;

    public static void initWordsList() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("words.txt"));
        String[] items;
        try(in) {
            for (int i = 0; i < WORDS_COUNT; i++) {
                items = in.readLine().split("\\s+");
                items[0] = items[0].replaceAll("-", " ").replace('*', '-'); //word process.
                items[1] = items[1].contains("null") ? "    " : items[1];           //property process.
                if(items.length > 3) {
                    StringBuilder options = new StringBuilder();
                    for (int j = 3; j < items.length; j++) {
                        options.append(items[j]).append(" ");
                    }
                    items[3] = options.toString();                          //option process.
                }
                wordsList.add(new ArrayList<>(List.of(items)));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public static void printWordsList() {
        for (ArrayList<String> strings : wordsList) {
            for (String string : strings) {
                System.out.printf("%-20s", string);
            }
            System.out.println();
        }
    }

    /**
     * *
     * @param wordsNum num of words to be tested.
     * @return a sublist of words, whose size is wordsNum.
     */
    public static ArrayList<ArrayList<String>> getRandomWords(int wordsNum) {
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        HashSet<Integer> wordNumSet = new HashSet<>();
        randomGenerator.setSeed(randomGenerator.nextLong());
        for (int i = 0; i < wordsNum; i++) {
            int randNumber;
            do {
                randNumber = Math.abs(randomGenerator.nextInt()) % WORDS_COUNT;
            } while (wordNumSet.contains(randNumber));
            wordNumSet.add(randNumber);
            result.add(wordsList.get(randNumber));
        }
        return result;
    }

    /**
     * *
     * @param start the word next to the last tested word.
     * @return a sublist of
     */
    public static ArrayList<ArrayList<String>> getSequentialWords(int start) {
        ArrayList<ArrayList<String>> result = new ArrayList<>(wordsList.subList(start, wordsList.size()));
        result.addAll(wordsList.subList(0, start));
        return result;
    }
}
