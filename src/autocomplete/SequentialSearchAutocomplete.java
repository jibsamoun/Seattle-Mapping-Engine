package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
        
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> p = new ArrayList<>();

        if (prefix == null || prefix.isEmpty()) {
            return p;
        }

        for (CharSequence word : elements) {
            if (Autocomplete.isPrefixOf(prefix, word)) {
                p.add(word);
            }
        }
        return p;
    }
}
