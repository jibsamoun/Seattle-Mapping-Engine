import minpq.MinPQ;
import minpq.OptimizedHeapMinPQ;
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
        // throw new UnsupportedOperationException();
        // Count occurrences of each unique WCAG tag
        MinPQ<String> wcagPQ = new UnsortedArrayMinPQ<>();
        for (String tag : wcagTags) {
            if (wcagPQ.contains((tag))) {
                wcagPQ.changePriority(tag, wcagPQ.getPriority(tag) - 1);
            } else {
                wcagPQ.add(tag, -1);
            }
        }

        for (int i = 0; i < 3; i++) {
            String tag = wcagPQ.removeMin();
            System.out.println(wcagDefinitions.get(tag));
        }
    }
}