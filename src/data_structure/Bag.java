package data_structure;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item> {

    private Node<Item> first; //begin
    private int n;  //size of bag

    private static class Node<Item>{
        Item item;
        Node<Item> next;
    }

    Bag(){
        first = null;
        n = 0;
    }

    void add(Item item){
        Node<Item> old = first;
        first = new Node<>();
        first.item = item;
        first.next = null;
        first.next = old;
        n++;
    }

    boolean isEmpty(){
        return n==0;
    }

    int size(){
        return n;
    }

    @Override
    public Iterator<Item> iterator() {
        return new LinkedIterator(first);
    }

    private class LinkedIterator implements Iterator<Item>{

        private Node<Item> current;

        public LinkedIterator(Node<Item> first){
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current == null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Bag<String> bag = new Bag<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (item.equals("--1")) break;
            bag.add(item);
        }

        StdOut.println("size of bag = " + bag.size());
        for (String s : bag) {
            StdOut.println(s);
        }
    }
}
