import minpq.MinPQ;
import minpq.UnsortedArrayMinPQ;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Display the most commonly-reported WCAG recommendations.
 */
public class ReportAnalyzer {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("data/wcag.tsv");
        Map<String, String> wcagDefinitions = new LinkedHashMap<>();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            String index = "wcag" + line[0].replace(".", "");
            String title = line[1];
            wcagDefinitions.put(index, title);
        }

        Pattern re = Pattern.compile("wcag\\d{3,4}");
        List<String> wcagTags = Files.walk(Paths.get("data/reports"))
                .map(path -> {
                    try {
                        return Files.readString(path);
                    } catch (IOException e) {
                        return "";
                    }
                })
                .flatMap(contents -> re.matcher(contents).results())
                .map(MatchResult::group)
                .toList();

        // TODO: Display the most commonly-reported WCAG recommendations using MinPQ
         throw new UnsupportedOperationException();
        // Count occurrences of each unique WCAG tag
//        Map<String, Integer> tagCounts = new HashMap<>();
//        for (String tag : wcagTags) {
//            tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
//        }
//
//        // Use UnsortedArrayMinPQ to find the top 3 most commonly-reported WCAG recommendations
//        MinPQ<Map.Entry<String, Integer>> minPQ = new UnsortedArrayMinPQ<>();
//        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
//            minPQ.add(entry);
//            // Keep only top 3 tags
//            if (minPQ.size() > 3) {
//                minPQ.removeMin();
//            }
//        }
//
//        // Display the top 3 most commonly-reported WCAG recommendations
//        System.out.println("Top 3 most commonly-reported WCAG recommendations:");
//        while (!minPQ.isEmpty()) {
//            Map.Entry<String, Integer> entry = minPQ.removeMin();
//            String tag = entry.getKey();
//            int count = entry.getValue();
//            String description = wcagDefinitions.get(tag);
//            System.out.println(description + " (Tag: " + tag + ", Count: " + count + ")");
//        }
    }
}