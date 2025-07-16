package datastructures;

import model.Drug;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MinHeap {
    private List<Drug> heap;

    public MinHeap() {
        heap = new ArrayList<>();
    }

    private int parent(int index) { return (index - 1) / 2; }
    private int leftChild(int index) { return 2 * index + 1; }
    private int rightChild(int index) { return 2 * index + 2; }

    public void insert(Drug drug) {
        heap.add(drug);
        siftUp(heap.size() - 1);
    }

    private void siftUp(int index) {
        while (index > 0 && heap.get(parent(index)).stockLevel > heap.get(index).stockLevel) {
            Collections.swap(heap, index, parent(index));
            index = parent(index);
        }
    }

    public Drug peekMin() {
        if (heap.isEmpty()) return null;
        return heap.get(0);
    }

    public Drug extractMin() {
        if (heap.isEmpty()) return null;
        Drug min = heap.get(0);
        Drug last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return min;
    }

    private void siftDown(int index) {
        int minIndex = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < heap.size() && heap.get(left).stockLevel < heap.get(minIndex).stockLevel) {
            minIndex = left;
        }
        if (right < heap.size() && heap.get(right).stockLevel < heap.get(minIndex).stockLevel) {
            minIndex = right;
        }
        if (minIndex != index) {
            Collections.swap(heap, index, minIndex);
            siftDown(minIndex);
        }
    }

    public void updateStock(String drugCode, int newStock) {
        for (Drug drug : heap) {
            if (drug.code.equals(drugCode)) {
                drug.stockLevel = newStock;
                heapify();
                break;
            }
        }
    }

    private void heapify() {
        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }
}
