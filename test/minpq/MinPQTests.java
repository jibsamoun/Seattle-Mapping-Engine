package minpq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link MinPQ} implementations.
 *
 * @see MinPQ
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MinPQTests {
    /**
     * Returns an empty {@link MinPQ}.
     *
     * @return an empty {@link MinPQ}
     */
    public abstract <E> MinPQ<E> createMinPQ();

    @Test
    public void wcagIndexAsPriority() throws FileNotFoundException {
        File inputFile = new File("data/wcag.tsv");
        MinPQ<String> reference = new DoubleMapMinPQ<>();
        MinPQ<String> testing = createMinPQ();
        Scanner scanner = new Scanner(inputFile);
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split("\t", 2);
            int index = Integer.parseInt(line[0].replace(".", ""));
            String title = line[1];
            reference.add(title, index);
            testing.add(title, index);
        }
        while (!reference.isEmpty()) {
            assertEquals(reference.removeMin(), testing.removeMin());
        }
        assertTrue(testing.isEmpty());
    }

    @Test
    public void randomPriorities() {
        int[] elements = new int[1000];
        for (int i = 0; i < elements.length; i = i + 1) {
            elements[i] = i;
        }
        Random random = new Random(373);
        int[] priorities = new int[elements.length];
        for (int i = 0; i < priorities.length; i = i + 1) {
            priorities[i] = random.nextInt(priorities.length);
        }

        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();
        for (int i = 0; i < elements.length; i = i + 1) {
            reference.add(elements[i], priorities[i]);
            testing.add(elements[i], priorities[i]);
        }

        for (int i = 0; i < elements.length; i = i+1) {
            int expected = reference.removeMin();
            int actual = testing.removeMin();

            if (expected != actual) {
                int expectedPriority = priorities[expected];
                int actualPriority = priorities[actual];
                assertEquals(expectedPriority, actualPriority);
            }
        }
    }

    @Test
    public void randomTestingInt() {
        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();

        int iterations = 10000;
        int maxElement = 1000;
        Random random = new Random();
        for (int i = 0; i < iterations; i += 1) {
            int element = random.nextInt(maxElement);
            double priority = random.nextDouble();
            reference.addOrChangePriority(element, priority);
            testing.addOrChangePriority(element, priority);
            assertEquals(reference.peekMin(), testing.peekMin());
            assertEquals(reference.size(), testing.size());
            for (int e = 0; e < maxElement; e += 1) {
                if (reference.contains(e)) {
                    assertTrue(testing.contains(e));
                    assertEquals(reference.getPriority(e), testing.getPriority(e));
                } else {
                    assertFalse(testing.contains(e));
                }
            }
        }
        for (int i = 0; i < iterations; i += 1) {
            boolean shouldRemoveMin = random.nextBoolean();
            if (shouldRemoveMin && !reference.isEmpty()) {
                assertEquals(reference.removeMin(), testing.removeMin());
            } else {
                int element = random.nextInt(maxElement);
                double priority = random.nextDouble();
                reference.addOrChangePriority(element, priority);
                testing.addOrChangePriority(element, priority);
            }
            if (!reference.isEmpty()) {
                assertEquals(reference.peekMin(), testing.peekMin());
                assertEquals(reference.size(), testing.size());
                for (int e = 0; e < maxElement; e += 1) {
                    if (reference.contains(e)) {
                        assertTrue(testing.contains(e));
                        assertEquals(reference.getPriority(e), testing.getPriority(e));
                    } else {
                        assertFalse(testing.contains(e));
                    }
                }
            } else {
                assertTrue(testing.isEmpty());
            }
        }
    }

    @Test
    public void largeScaleReportAnalyzerSimulation() throws IOException {
        // Read WCAG tags from the file
        List<String> wcagTags = readWCAGTagsFromFile("data/wcag.tsv");

        // Create reference and testing implementations of MinPQ
        MinPQ<String> reference = createMinPQ();
        MinPQ<String> testing = createMinPQ();

        // Simulate counting exactly 10,000 WCAG tags
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            String randomTag = wcagTags.get(random.nextInt(wcagTags.size()));
            double randomPriority = random.nextDouble();
            reference.addOrChangePriority(randomTag, randomPriority);
            testing.addOrChangePriority(randomTag, randomPriority);
        }

        // Remove all tags and compare the remove orders
        List<String> removeOrder = new ArrayList<>();
        while (!reference.isEmpty()) {
            String removedTagReference = reference.removeMin();
            String removedTagTesting = testing.removeMin();
            assertEquals(removedTagReference, removedTagTesting);
            removeOrder.add(removedTagReference);
        }
    }

    private List<String> readWCAGTagsFromFile(String filePath) throws IOException {
        List<String> wcagTags = new ArrayList<>();
        File inputFile = new File(filePath);
        try (Scanner scanner = new Scanner(inputFile)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("\t", 2);
                String index = "wcag" + line[0].replace(".", "");
                wcagTags.add(index);
            }
        }
        return wcagTags;
    }

    private static final int TOP_TAGS_COUNT = 3;
    @Test
    public void modifiedLargeScaleReportAnalyzerSimulation() throws IOException {
        // Read WCAG tags from the file
        List<String> wcagTags = readWCAGTagsFromFile("data/wcag.tsv");

        // Identify the top N most commonly-reported tags
        Map<String, Integer> topTags = getTopTags(wcagTags, TOP_TAGS_COUNT);

        // Create reference and testing implementations of MinPQ
        MinPQ<String> reference = createMinPQ();
        MinPQ<String> testing = createMinPQ();

        // Simulate counting WCAG tags with upweighted top tags
        Random random = new Random();
        for (int i = 0; i < 10000; i++) {
            String randomTag;
            if (i < TOP_TAGS_COUNT * 100) {
                // Upweight top tags
                randomTag = selectRandomTopTag(topTags, random);
            } else {
                // Randomly select other tags
                randomTag = wcagTags.get(random.nextInt(wcagTags.size()));
            }
            double randomPriority = random.nextDouble();
            reference.addOrChangePriority(randomTag, randomPriority);
            testing.addOrChangePriority(randomTag, randomPriority);
        }

        // Remove all tags and compare the remove orders
        while (!reference.isEmpty()) {
            assertEquals(reference.removeMin(), testing.removeMin());
        }
    }


    private Map<String, Integer> getTopTags(List<String> wcagTags, int topCount) {
        // Count occurrences of each tag
        Map<String, Integer> tagCounts = new HashMap<>();
        for (String tag : wcagTags) {
            tagCounts.put(tag, tagCounts.getOrDefault(tag, 0) + 1);
        }

        // Sort tags by frequency in descending order
        List<Map.Entry<String, Integer>> sortedTags = new ArrayList<>(tagCounts.entrySet());
        sortedTags.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Select top N tags
        Map<String, Integer> topTags = new LinkedHashMap<>();
        for (int i = 0; i < Math.min(topCount, sortedTags.size()); i++) {
            topTags.put(sortedTags.get(i).getKey(), sortedTags.get(i).getValue());
        }
        return topTags;
    }

    private String selectRandomTopTag(Map<String, Integer> topTags, Random random) {
        // Randomly select a top tag with probability proportional to its frequency
        int totalFrequency = topTags.values().stream().mapToInt(Integer::intValue).sum();
        int randomIndex = random.nextInt(totalFrequency);
        int cumulativeFrequency = 0;
        for (Map.Entry<String, Integer> entry : topTags.entrySet()) {
            cumulativeFrequency += entry.getValue();
            if (randomIndex < cumulativeFrequency) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Failed to select a random top tag.");
    }
}

