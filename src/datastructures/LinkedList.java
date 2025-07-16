package datastructures;

import java.util.ArrayList;
import java.util.List;

import model.Transaction;

public class LinkedList {
    private class Node {
        Transaction data;
        Node next;

        Node(Transaction data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;

    public LinkedList() {
        head = null;
    }

    public void add(Transaction transaction) {
        Node newNode = new Node(transaction);
        if (head == null) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public List<Transaction> toList() {
        List<Transaction> result = new ArrayList<>();
        Node current = head;
        while (current != null) {
            result.add(current.data);
            current = current.next;
        }
        return result;
    }
}
