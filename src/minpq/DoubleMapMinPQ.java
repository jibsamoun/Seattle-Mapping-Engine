package minpq;

import java.util.*;

/**
 * {@link TreeMap} and {@link HashMap} implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class DoubleMapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link NavigableMap} of priority values to all elements that share the same priority values.
     */
    private final NavigableMap<Double, Set<E>> priorityToElement;
    /**
     * {@link Map} of elements to their associated priority values.
     */
    private final Map<E, Double> elementToPriority;

    /**
     * Constructs an empty instance.
     */
    public DoubleMapMinPQ() {
        priorityToElement = new TreeMap<>();
        elementToPriority = new HashMap<>();
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public DoubleMapMinPQ(Map<E, Double> elementsAndPriorities) {
        priorityToElement = new TreeMap<>();
        elementToPriority = new HashMap<>(elementsAndPriorities);
        for (Map.Entry<E, Double> entry : elementToPriority.entrySet()) {
            E element = entry.getKey();
            double priority = entry.getValue();
            if (!priorityToElement.containsKey(priority)) {
                priorityToElement.put(priority, new HashSet<>());
            }
            Set<E> elementsWithPriority = priorityToElement.get(priority);
            elementsWithPriority.add(element);
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        if (!priorityToElement.containsKey(priority)) { // if priority DNE in TreeMap
            priorityToElement.put(priority, new HashSet<>()); // add priority as new key in TreeMap
        }
        Set<E> elementsWithPriority = priorityToElement.get(priority); // returns set of elements w/ given priority
        elementsWithPriority.add(element); // adds element to set of elements w/ same priority for the TreeMap
        elementToPriority.put(element, priority); // adds element and priority as (key,value) pair in HashMap
    }

    @Override
    public boolean contains(E element) {
        return elementToPriority.containsKey(element);
    }

    @Override
    public double getPriority(E element) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain element");
        }
        return elementToPriority.get(element);
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToElement.firstKey();
        Set<E> elementsWithMinPriority = priorityToElement.get(minPriority);
        return firstOf(elementsWithMinPriority);
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToElement.firstKey(); // first key of TreeMap
        Set<E> elementsWithMinPriority = priorityToElement.get(minPriority); // set of elements w/ min priority
        E element = firstOf(elementsWithMinPriority); // returns an element w/ min priority
        elementsWithMinPriority.remove(element); // removes element w/ min priority from set
        if (elementsWithMinPriority.isEmpty()) { // if we removed last element from set of min priority elements
            priorityToElement.remove(minPriority); // removes min priority as a key from TreeMap
        }
        elementToPriority.remove(element); // remove min element from TreeMap
        return element;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        // elementToPriority = HashMap of elements to their associated priority value
        double oldPriority = elementToPriority.get(element); // returns old priority value of given element
        if (priority != oldPriority) { // if new priority does not equal old priority
            Set<E> elementsWithOldPriority = priorityToElement.get(oldPriority); // returns set of element w/ old priority
            elementsWithOldPriority.remove(element); // remove element from set
            if (elementsWithOldPriority.isEmpty()) { // if we removed last element from set
                priorityToElement.remove(oldPriority); // remove old priority as a key from TreeMap
            }
            elementToPriority.remove(element); // remove given element as key from Hashmap
            add(element, priority); // adds element with new priority into Hashmap
            // also adds element with new priority into correct set for TreeMap
        }
    }

    @Override
    public int size() {
        return elementToPriority.size();
    }

    /**
     * Returns any one element from the given iterable.
     *
     * @param it the iterable of elements.
     * @return any one element from the given iterable.
     */
    private E firstOf(Iterable<E> it) {
        return it.iterator().next();
    }
}
