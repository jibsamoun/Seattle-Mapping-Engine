package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence word: terms) { // iterate thru entire terms
            overallRoot = addWord(overallRoot, word, 0); // at each iteration, add word to TST structure
        }
    }

    /**
     * Adds word to TST structure, by recursively adding each unique character to a node
     *
     * @param node The current node in the TST being traversed
     * @param word The word being traversed
     * @param index The index of each char in the word being traversed
     * @return Node the newly added node to the TST structure
     */
    private Node addWord(Node node, CharSequence word, int index) {
        char currentChar = word.charAt(index); // assign currentChar to first character of word

        if (node == null) { // if passed in node is null
            node = new Node(currentChar); // creates new node of current character
        }

        if (currentChar < node.data) { // if currentChar less than current node's char value
            node.left = addWord(node.left, word, index); // add new node w/ char to node's left child
        } else if (currentChar > node.data) { // if currentChar greater than current node's char value
            node.right = addWord(node.right, word, index); // add new node w/ char to node's right child
        } else if (index < word.length() - 1) { // curr char in word matches char in current node, but
            node.mid = addWord(node.mid, word, index + 1);  // there are more characters in the word to be traversed
        } else {
            node.isTerm = true; // currentChar matches char of current node, and its the last char of word being inserted
        }
        return node;
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> matches = new ArrayList<>();
        Node prefixNode = findPrefix(overallRoot, prefix, 0); // Node in TST corresponding to the end of the prefix

        if (prefixNode != null) {
            if (prefixNode.isTerm) { // if the prefix itself is a word
                matches.add(prefix); // add the prefix to match list
            }
            collectMatches(prefixNode.mid, prefix, matches); // otherwise recursively collect all matches
        }
        return matches;
    }

    /**
     * Finds node in TST that correlates to end of the prefix.
     *
     * @param node The current node during TST traversal
     * @param prefix The prefix that we are finding correlating node in TST
     * @param index The index of current character being traversed in prefix
     * @return Node The node correlating to end of prefix if it's found. Returns null
     * if prefix does not exist within TST
     */
    private Node findPrefix(Node node, CharSequence prefix, int index) {
        if (node == null) return node;

        char currChar = prefix.charAt(index); // current char @ index of prefix

        if (currChar < node.data) { // compares with char stored in current node
            return findPrefix(node.left, prefix, index); // traverses left child
        } else if (currChar > node.data) {
            return findPrefix(node.right, prefix, index); // traverses right child
        } else {
            if (index == prefix.length() - 1) { // if end of prefix is reached
                return node; // return node matching prefix
            } else {
                return findPrefix(node.mid, prefix, index + 1); // traverses middle child
            }
        }
    }

    /**
     * Collects all matches starting from middle child of prefix node.
     *
     * @param node The current node of TST being traversed
     * @param prefix Prefix accumulated during traversal
     * @param matches List of words that contain the prefix
     */
    private void collectMatches(Node node, CharSequence prefix, List<CharSequence> matches) {
        if (node != null) {
            collectMatches(node.left, prefix, matches); // traverse left subtree

            CharSequence currPrefix = prefix.toString() + node.data; // concatenate current node's char to prefix
            if (node.isTerm) { // match is found
                matches.add(currPrefix);
            }
            collectMatches(node.mid, currPrefix, matches); // traverse mid subtree

            collectMatches(node.right, prefix, matches); // traverse right subtree
        }
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
