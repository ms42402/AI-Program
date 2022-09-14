package com.visualizer.visualizationapp;

import java.util.ArrayList;

/**
 * Indirect binary heap.
 */

public class BinaryMinHeap<E extends Comparable<? super E>> {
    private ArrayList<E> _list;

    public BinaryMinHeap() {
        _list = new ArrayList<>();
    }

    public boolean add(E element) {
        if (_list.contains(element)) {
            return false;
        }

        _list.add(element);
        int index = _list.indexOf(element);
        int parentIndex = 0;

        while (index != 0) {
            parentIndex = (index - 1) / 2;

            if (_list.get(parentIndex).compareTo(element) <= 0) {
                return true;
            }

            swap(parentIndex, index);

            index = parentIndex;
        }

        return true;
    }

    public E poll() {
        if (_list.size() == 0) {
            return null;
        }

        if (_list.size() == 1) {
            return _list.remove(0);
        }

        swap(0, _list.size() - 1);
        E root = _list.remove(_list.size() - 1);
        heapify(0);

        return root;
    }

    public boolean remove(E element) {
        if (!_list.contains(element)) {
            return false;
        }

        int index = _list.indexOf(element);

        swap(index, _list.size() - 1);
        _list.remove(_list.size() - 1);
        heapify(index);

        return true;
    }

    public boolean contains(E element) {
        return _list.contains(element);
    }

    private void swap(int first, int second) {
        E n1 = _list.get(first);
        E n2 = _list.get(second);

        _list.set(first, n2);
        _list.set(second, n1);
    }

    private void heapify(int index) {
        int leftChild = 2*index + 1;
        int rightChild = 2*index + 2;
        int min = index;

        if (leftChild < _list.size() && _list.get(leftChild).compareTo(_list.get(min)) < 0) {
            min = leftChild;
        }

        if (rightChild < _list.size() && _list.get(rightChild).compareTo(_list.get(min)) < 0) {
            min = rightChild;
        }

        if (min != index) {
            swap(index, min);
            heapify(min);
        }
    }

    public boolean isEmpty() {
        return _list.isEmpty();
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < _list.size(); i++) {
            sb.append(i + ": ");
            sb.append(_list.get(i).toString() + "\n");
        }

        return sb.toString();
    }

}

