---
title: 并查集(Union Find)学习
date: 2020-09-11
tags:
  - Algorithms
  - 并查集
  - 数据结构与算法
author: Milky
---

# 并查集(Union Find)

## 引入

现在给出N个对象，对这些对象有两种操作：

- **合并union(p,q)** 将任意两个对象互相连接
- **查询isConnected(p,q)** 询问任意两个对象之间是否连通

![](img\并查集(Union Find)-1.gif)

“p与q相连”，假设这里的**相连**是一种**等价关系**，这就意味着它有

- 自反性：p与p是相连的
- 对称行：p与q是相连的，q与p也是相连的
- 传递性：如果p与r相连，r与q相连，那么p与q也相连

当有了等价关系，一个对象和连接的集合就被分成一个或多个子集，这些子集就是**连通分量**

连通分量的概念：连通分量就是相互连接的对象的最大集合，因此，一个图是连通的当且仅当它只具有一个连通分量。

如下图包含三个连通分量，{0},{1,4,5},{2,3,6,7}

![](img\连通分量.png)

了解了连通分量后，我们就可以通过维护连通分量来提高操作的效率，连接与询问两个操作就变为

- **合并union(p,q)** 将包含两个对象的连通分量替换为其并集
- **查询isConnected(p,q)** 查询两个对象是否在同一个连通分量中

如下图

![](img\合并连通分量.png)

## Quick Find算法

​	有了上面的思考，我们很容易想到，用一个长度为N的数组来维护这些连通分量，N个元素分别代表N个对象，每个元素的值为该元素**所在连通分量的id**

如下图

![](img\Quick Find-1.png)

**union(1,6)**后

![](img\Quick Find-2.png)

​	可以很直观的看到，执行合并操作时，只需将一个连通分量中**所有**元素的值更新为与另一个元素所在连通分量的id即可；执行查询操作时，只需返回数组中对应两个元素的值是否相等

### 实现

代码如下

```java
public class QuickFindUF {

    private int[] id;

    //初始化数组，每个对象与自己连通
    public QuickFindUF(int N){
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    //是否连通
    public boolean isConnected(int p, int q){
        return id[p] == id[q];
    }

    public void union(int p, int q){
        //两个对象对应的连通分量id
        int pid = id[p], qid = id[q];
        for(int i = 0; i < id.length; i++)
            //将一个对象的连通分量id变为另外一个的连通分量id
            if(id[i] == pid) id[i] = qid;
    }
    
}
```

### 分析

可以分析该算法需要操作或访问数组的次数

|            | 初始化 | 合并 | 查询 |
| ---------- | ------ | ---- | ---- |
| quick-find | N      | N    | 1    |

​	当要处理N个对象的N个合并操作时，计算复杂度需要N^2，显然，对于大规模数据，这样的时间复杂度是不被接受的。当我们要处理10^9个对象的10^9次合并时，需要10^18次操作，即使是现在的计算机CPU读写RAM速度大概是10GB/s的数量级，也需要用几百天或者几年的时间完成。

​	我们应该考虑是否有更高效的方法改进我们的算法

## Quick Union算法

​	之前的算法在合并操作的时间复杂度比较大，我们考虑将连通的节点用父子关系穿起来，数组中的每个元素保存它的父节点，根节点的父节点是它本身。这样我们的数组就可以看作一片森林，一个连通分量就是一棵树，执行合并操作时，只需将元素所在的树的根节点指向另一棵树的根节点。查询树的根节点也十分简单，只需询问元素的父节点，直至一个指向自身的节点就是根节点。

如下图

3的父节点是4，4的父节点是9，9是根节点

5的父节点是6，6是根节点

合并时只需将9指向6或6指向9即可

![image-20200913091835950](img\Quick Union-1.png)

相同例子演示

![](img\Quick Union-3.gif)

### 实现

```java
public class QuickFindUF {

    private int[] id;
	
    //初始化
    public QuickFindUF(int N){
        id = new int[N];
        for (int i = 0; i < N; i++) {
            id[i] = i;
        }
    }

    //查询是否连通
    public boolean isConnected(int p, int q){
        return root(p) == root(q);
    }

    //合并
    public void union(int p, int q){
        //分别查找根节点
        int rootP = root(p);
        int rootQ = root(q);
        //将q的根节点作为p的根节点
        id[rootP] = rootQ;
    }

    //查找根节点
    private int root(int i){
        //不停往上查找，直至找到指向自身的根节点
        while (i != id[i]) i = id[i];
        return i;
    }

}
```

### 分析

考虑最坏情况，树的深度有可能为N，比较Quick Find和Quick Union，那么出现了如下的复杂度局面。

|             | 初始化 | 合并 | 查询 |
| ----------- | ------ | ---- | ---- |
| quick-find  | N      | N    | 1    |
| quick-union | N      | N*   | N    |

​	合并虽然只需要操作一次数组，但是多了查找的操作，而查询则多了不停访问查找根节点的时间，最坏情况下两者都可以达到N次

​	也就是说，考虑到N次的union操作，Quick Union也需要N^2的计算复杂度，依旧是个不好的方法，Quick Find无法规模化的问题依旧存在。

## Union Find算法

​	对上面的Quick Union算法分析，我们可以得知花费的时间在于查找树的根节点上，这个花费与树的深度有关，我们可以从这里入手，考虑尽可能的降低树的深度。

​	从演示图可以看出，树深度的增加总是发生在高的树并入矮的树时或者相同高度的树合并时，所以，我们想到可以记录各树的高度，合并时进比较，避免将高的树并入矮的树增加高度，并在高度改变时更新这些数据。

对比图

![](img\Union Find-1.png)

### 实现

```java
public class UF {

    private int[] parent;
    //记录树的层数
    private int[] size;
    private int count;

    public UF(int N){
        if (N < 0) throw new IllegalArgumentException();
        count = N;
        parent = new int[N];
        size = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            //初始化节点深度
            size[i] = 1;
        }
    }

    //合并
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

    //查询连通
    public boolean isConnected(int p, int q){
        return find(p) == find(q);
    }

    //查找根节点
    public int find(int p){
        //不停往上查找，直至找到指向自身的根节点
        while (i != parent[i]) i = id[i];
        return i;
    }

    public int count(){
        return count;
    }
}

```

