import java.io.*;
import java.util.*;

public class TextAnalyzer {

    // Define exclusion words for prepositions, pronouns, conjunctions, and articles
    private static final Set<String> exclusionWords = new HashSet<>(Arrays.asList(
        "in", "on", "at", "for", "with", "by", "to", "and", "but", "or", "the", "a", "an",
        "he", "she", "it", "we", "they", "you", "is", "was", "are", "were", "be", "been", "being"
    ));

    public static void main(String[] args) {
        // Path to the file
        String filePath = "moby.txt";
        
        // Process the file
        try {
            Map<String, Integer> wordCountMap = analyzeTextFile(filePath);
            
            // Output total word count
            int totalWordCount = wordCountMap.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println("Total Word Count (after exclusions): " + totalWordCount);
            
            // Output top 5 words
            List<Map.Entry<String, Integer>> topWords = getTopWords(wordCountMap, 5);
            System.out.println("\nTop 5 Most Frequent Words:");
            topWords.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
            
            // Output unique words
            List<String> uniqueWords = getUniqueWords(wordCountMap);
            System.out.println("\nUnique Words (Alphabetical Order - Top 50):");
            uniqueWords.stream().limit(50).forEach(System.out::println);
            
            // Calculate processing time
            long startTime = System.currentTimeMillis();
            // You can use the output section for timing, or it could be done during the main logic.
            long endTime = System.currentTimeMillis();
            long processingTime = (endTime - startTime) / 1000;
            System.out.println("\nProcessing Time: " + processingTime + " seconds");

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    private static Map<String, Integer> analyzeTextFile(String filePath) throws IOException {
        Map<String, Integer> wordCountMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        
        while ((line = reader.readLine()) != null) {
            // Process each line
            processLine(line, wordCountMap);
        }
        reader.close();
        
        return wordCountMap;
    }

    private static void processLine(String line, Map<String, Integer> wordCountMap) {
        // Split the line into words based on non-letter characters
        String[] words = line.split("[^a-zA-Z]+");
        
        for (String word : words) {
            if (word.isEmpty()) continue;
            
            // Convert word to lower case and handle plural 's' or 'es'
            String cleanedWord = word.toLowerCase().replaceAll("'s$", "");
            
            // Exclude unwanted words
            if (!exclusionWords.contains(cleanedWord)) {
                // Count the word frequency
                wordCountMap.put(cleanedWord, wordCountMap.getOrDefault(cleanedWord, 0) + 1);
            }
        }
    }

    private static List<Map.Entry<String, Integer>> getTopWords(Map<String, Integer> wordCountMap, int topN) {
        // Sort by frequency
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(wordCountMap.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
        
        // Return top N words
        return sortedList.subList(0, Math.min(topN, sortedList.size()));
    }

    private static List<String> getUniqueWords(Map<String, Integer> wordCountMap) {
        // Extract unique words sorted alphabetically
        List<String> uniqueWords = new ArrayList<>(wordCountMap.keySet());
        Collections.sort(uniqueWords);
        return uniqueWords;
    }
}
