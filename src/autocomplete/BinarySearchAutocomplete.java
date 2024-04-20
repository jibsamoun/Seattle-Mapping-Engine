package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
        Collections.sort(elements, CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        // Figure out how to do this recursively/while loop
        int start = Collections.binarySearch(elements, prefix, CharSequence::compare); // returns first existence of prefix in elements
        List<CharSequence> matches = new ArrayList<>();
        if (start < 0) {
            start = -(start + 1);
        }
        for (int i = start; i < elements.size(); i++) { // iterate from word containing prefix until end of elements
            CharSequence word = elements.get(i);
            if (Autocomplete.isPrefixOf(prefix, word)) { // if word contains prefix
                matches.add(word); // add to matches list
            }
        }
        return matches;
    }
}
