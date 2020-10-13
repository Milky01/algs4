package data_structure;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item> {

    private Node<Item> first; //begin
    private int n;  //size of stack

    private static class Node<Item>{
        Item item;
        Node<Item> next;
    }

    Stack(){
        first = null;
        n = 0;
    }

    void push(Item item){
        Node<Item> old = first;
        first = new Node<>();
        first.item = item;
        first.next = null;
        first.next = old;
        n++;
    }

    Item pop(){
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = first.item;
        first = first.next;
        n--;
        return item;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    public Item peek(){
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return first.item;
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
        Stack<String> stack = new Stack<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-"))
                stack.push(item);
            else if (!stack.isEmpty())
                StdOut.print(stack.pop() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
    }
}
