package io.github.kuggek.editor.utils;

import java.util.LinkedList;

public class Ordering {
    
    private LinkedList<Integer> order;

    public Ordering() {
        order = new LinkedList<>();
    }

    /**
     * Adds an ID to the given index in the order, shifting the rest of the IDs to the right.
     * Clamps the index to the bounds of the order. If the ID already exists in the order, it is
     * removed from its current position.
     * @param index the index to add the ID to
     * @param id the ID to add
     */
    public void add(int index, int id) {
        if (index < 0) {
            index = 0;
        } else if (index > order.size()) {
            index = order.size();
        }

        if (order.contains(id)) {
            order.remove(id);
        }

        order.add(index, id);
    }

    /**
     * Adds an ID to the end of the order, removing it from its current position if it exists.
     * @param id the ID to add
     */
    public void add(int id) {
        if (order.contains(id)) {
            order.remove(id);
        }

        order.add(id);
    }

    /**
     * Get the ID at the given index.
     * @param index the index
     * @return the ID at the given index
     */
    public Integer get(int index) {
        return order.get(index);
    }

    /**
     * Get the index of the given ID.
     * @param id the ID
     * @return the index of the ID or -1 if it is not in the order
     */
    public Integer indexOf(int id) {
        return order.indexOf(id);
    }

    /**
     * Moves the ID at the given index up one position in the order. If the ID is already at the top,
     * it is not moved and it's index (0) is returned.
     * @param id the ID to move up
     * @return the new index of the ID
     */
    public Integer moveUp(int id) {
        int index = order.indexOf(id);
        if (index > 0) {
            order.remove(index);
            int newIndex = index - 1;
            order.add(newIndex, id);
            return newIndex;
        }
        return index;
    }

    /**
     * Moves the ID at the given index down one position in the order. If the ID is already at the bottom,
     * it is not moved and it's index is returned.
     * @param id the ID to move down
     * @return the new index of the ID
     */
    public Integer moveDown(int id) {
        int index = order.indexOf(id);
        if (index < order.size() - 1) {
            order.remove(index);
            int newIndex = index + 1;
            order.add(newIndex, id);
            return newIndex;
        }
        return index;
    }

    public void remove(int id) {
        order.remove(id);
    }

    public int size() {
        return order.size();
    }

    public void clear() {
        order.clear();
    }
}
