package minpq;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

/**
 * {@link PriorityQueue} implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class HeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link PriorityQueue} storing {@link PriorityNode} objects representing each element-priority pair.
     */
    private final PriorityQueue<PriorityNode<E>> pq;

    /**
     * Constructs an empty instance.
     */
    public HeapMinPQ() {
        pq = new PriorityQueue<>(Comparator.comparingDouble(PriorityNode::getPriority));
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public HeapMinPQ(Map<E, Double> elementsAndPriorities) {
        pq = new PriorityQueue<>(elementsAndPriorities.size(), Comparator.comparingDouble(PriorityNode::getPriority));
        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        pq.offer(new PriorityNode<>(element, priority));
    }

    @Override
    public boolean contains(E element) {
        return pq.contains(new PriorityNode<>(element, 0));
    }

    @Override
    // iterate thru pq w/ for-each loop, check if element equals argument then return priority
    public double getPriority(E element) {
        for (PriorityNode<E> node : pq) {
            if (node.getElement().equals(element)) {
                return node.getPriority();
            }
        }
        return 0.0; // if element does not exist in PQ
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<E> min = null;
        for (PriorityNode<E> node : pq) {
            if (min == null || node.getPriority() < min.getPriority()) {
                min = node;
            }
        }
        return min.getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return pq.poll().getElement(); // removes and returns the head of pq
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }

        PriorityNode<E> updatedNode = new PriorityNode<E>(element, priority);
        // Since PriorityNode objects are only equal if their elements are equal,
        // we can remove the updatedNode object because it has the same element,
        // as our target priorityNode. Thus, by removing the updatedNode object
        // it will remove the target PriorityNode
        pq.remove(updatedNode); // removes node w/ current priority
        pq.offer(updatedNode); // inserts new node w/ updated priority to ensure its in the correct position
    }

    @Override
    public int size() {
        return pq.size();
    }
}
