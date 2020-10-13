package dynamic_connectivity;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class QuickFindUF {

    private int[] id;

    public static void main(String[] args) {
        int N = StdIn.readInt();
        QuickFindUF uf = new QuickFindUF(N);
        while (!StdIn.isEmpty()){
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (!uf.connected(p,q)){
                uf.union(p,q);
                StdOut.println(p + " " + q);
            }
        }
    }

    public QuickFindUF(int N){
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    public boolean connected(int p, int q){
        return root(p) == root(q);
    }

    public void union(int p, int q){
        int rootP = root(p);
        int rootQ = root(q);
        id[rootP] = rootQ;
    }

    private int root(int i){
        while (i != id[i]) i = id[i];
        return i;
    }

}
