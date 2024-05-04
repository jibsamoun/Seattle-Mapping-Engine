package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class UnsortedArrayMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the element-priority pairs in no specific order.
     */
    private final List<PriorityNode<E>> elements;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        elements = new ArrayList<>();
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public UnsortedArrayMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        elements.add(new PriorityNode<>(element, priority));
    }

    @Override
    public boolean contains(E element) {
        for (PriorityNode node : elements) {
            if (node.getElement().equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getPriority(E element) {
        for (PriorityNode node : elements) {
            if (node.getElement().equals(element)) {
                return node.getPriority();
            }
        }
        throw new NoSuchElementException("Element does not exist!");
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<E> minimum = elements.get(0); // automatically set first element as minimum
        for (PriorityNode<E> node : elements) {
            if (node.getPriority() < minimum.getPriority()) { // if curr node < minimum
                minimum = node; // set new minimum
            }
        }
        // after iterating thru elements, we're left w/ smallest element
        return minimum.getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<E> minimum = elements.get(0);
        int minimumIndex = 0;
        for (int i = 0; i < elements.size(); i++) { // finds minimum node
            PriorityNode<E> node = elements.get(i);
            if (node.getPriority() < minimum.getPriority()) {
                minimum = node;
                minimumIndex = i; // finds index of minimum node
            }
        }
        elements.remove(minimumIndex); // remove element at the given index
        return minimum.getElement();
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        for (PriorityNode<E> node : elements) {
            if (node.getElement().equals(element)) {
                node.setPriority(priority);
            }
        }
    }

    @Override
    public int size() {
        return elements.size();
    }
}
