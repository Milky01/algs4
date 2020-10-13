package dynamic_connectivity;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class UF {

    private int[] parent;
    private int[] size;
    private int count;

    public static void main(String[] args) {
        int N = StdIn.readInt();
        UF uf = new UF(N);
        while (!StdIn.isEmpty()){
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (!uf.connected(p,q)){
                uf.union(p,q);
                StdOut.println(p + " " + q);
            }
        }
    }

    public UF(int N){
        if (N < 0) throw new IllegalArgumentException();
        count = N;
        parent = new int[N];
        size = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public void union(int p, int q){
        //查找根节点
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return;
        parent[rootP] = rootQ;
        //小树合并到大树
        if (size[rootP] > size[rootQ]) parent[rootQ] = rootP;
        else if (size[rootP] < size[rootQ]) parent[rootP] = rootQ;
        else {
            //高度相同的树合并，深度增加
            parent[rootQ] = rootP;
            size[rootP]++;
        }
        count--;
    }

    public boolean connected(int p, int q){
        return find(p) == find(q);
    }

    public int find(int p){
        while (p != parent[p]) {
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public int count(){
        return count;
    }
}
