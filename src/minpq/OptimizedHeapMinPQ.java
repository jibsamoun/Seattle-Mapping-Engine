package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public OptimizedHeapMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        elementsToIndex = new HashMap<>(elementsAndPriorities.size());
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }

        elements.add(new PriorityNode<>(element, priority));
        int index = elements.size() - 1;
        elementsToIndex.put(element, index);
        swim(index);
    }

    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public double getPriority(E element) {
        int nodeIndex = elementsToIndex.get(element);
        return elements.get(nodeIndex).getPriority();
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(0).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        E minimumNode = elements.get(0).getElement(); // min node is the root
        int lastIndex = elements.size() - 1; // index of last leaf
        exch(0, lastIndex); // swap root and last leaf
        elements.remove(lastIndex); // remove the last leaf
        elementsToIndex.remove(minimumNode);
        sink(0); // sink new root to its proper place
        return minimumNode;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        int index = elementsToIndex.get(element);
        double lastPriority = elements.get(index).getPriority();
        elements.get(index).setPriority(priority);
        if (priority < lastPriority) {
            swim(index);
        } else {
            sink(index);
        }
    }

    @Override
    public int size() {
        return elements.size();
    }

    /**
     * This method  compares the priority of the element with its parent and
     * swaps it with the parent if the parent's priority is greater.
     * This process continues until the element is in its correct
     * position in the heap.
     *
     * @param k the index of the element to swim up in the heap
     */
    private void swim(int k) {
        while (k > 0 && greater(parent(k), k)) {
            exch(k, parent(k)); // exchange k with its parent
            k = parent(k);
        }
    }

    /**
     * This method compares the priority of the element with
     *  its children and swaps it with the smaller child until
     * it satisfies the heap property. This process continues until the element
     * is in its correct position in the heap.
     *
     * @param k the index of the element to sink down in the heap
     */
    private void sink(int k) {
        while (leftChild(k) < elements.size()) {
            int j = leftChild(k);
            if (j < elements.size() - 1 && greater(j, j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            exch(k, j);
            k = j;
        }
    }


    /**
     * Compares the priority of two elements at the specified indices in the heap.
     *
     * @param i the index of the first element
     * @param j the index of the second element
     * @return true if the priority of the element at index i is greater than the priority of the element at index j; otherwise, false
     */
    private boolean greater(int i, int j) {
        return elements.get(i).getPriority() > elements.get(j).getPriority();
    }

    /**
     * Exchanges the positions of two elements in the heap.
     *
     * @param i the index of the first element to be swapped
     * @param j the index of the second element to be swapped
     */
    private void exch(int i, int j) {
        Collections.swap(elements, i, j); // swaps elements at given index
        elementsToIndex.put(elements.get(i).getElement(), i); // updates index in map to reflect new position
        elementsToIndex.put(elements.get(j).getElement(), j); // updates index in map to reflect new position
    }

    /**
     * Calculates the index of the left child of a node in the heap.
     *
     * @param i the index of the parent node
     * @return the index of the left child of the parent node
     */
    private int leftChild(int i) {
        return 2 * i + 1;
    }

    /**
     * Calculates the index of the parent node of a node in the heap.
     *
     * @param i the index of the child node
     * @return the index of the parent node of the given child node
     */
    private int parent(int i) {
        return (i - 1) / 2;
    }
}
