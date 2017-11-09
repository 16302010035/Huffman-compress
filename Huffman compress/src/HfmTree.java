import java.util.PriorityQueue;

public class HfmTree {
    private String[] saveCode = new String[300];
    private int cnt;
    private int[] counts = new int[300];
    private hfmNode Root;
    private PriorityQueue<hfmNode> pq = new PriorityQueue<hfmNode>();
    private int i;

    HfmTree() {
    }

    HfmTree(String[] saveCode, int[] counts, int cnt,hfmNode Root,PriorityQueue<hfmNode> pq) {
        this.saveCode = saveCode;
        this.counts = counts;
        this.cnt = cnt;
        this.Root = Root;
        this.pq = pq;
    }

    public  void createTree() {

        hfmNode min1, min2;

        if (cnt == 0) {
            return;
        } else if (cnt == 1) {
            for (i = 0; i < 300; i++)
                if (counts[i] != 0) {
                    saveCode[i] = "0";
                    break;
                }
            return;
        }

        while (pq.size() != 1) {
            hfmNode father = new hfmNode();
            min1 = pq.poll();
            min2 = pq.poll();
            father.leftChild = min1;
            father.rightChild = min2;
            father.key = min1.key + min2.key;
            pq.add(father);
        }
        Root = pq.poll();
    }

    public void getCode() {
        if (cnt > 1) getHfmCode(Root, "");
    }

    public void getHfmCode(hfmNode root, String s) {
        root.code = s;
        if ((root.leftChild == null) && (root.rightChild == null)) {
            saveCode[root.Bite] = s;
            return;
        }
        if (root.leftChild != null)
            getHfmCode(root.leftChild, s + "0");
        if (root.rightChild != null)
            getHfmCode(root.rightChild, s + "1");
    }
    public int  Makepq() {
        int i;
        pq.clear();

        for (i = 0; i < 300; i++) {
            if (counts[i] != 0) {
                hfmNode Temp = new hfmNode();
                Temp.Bite = i;
                Temp.key = counts[i];
                Temp.leftChild = null;
                Temp.rightChild = null;
                pq.add(Temp);
                cnt++;
            }

        }
        return cnt;
    }


}
