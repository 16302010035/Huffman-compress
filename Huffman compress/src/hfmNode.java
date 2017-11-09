public class hfmNode implements Comparable<hfmNode> {
    hfmNode leftChild;
    hfmNode rightChild;
    public String code;
    public int Bite;
    public int key;

    public int compareTo(hfmNode T) {
        if (this.key < T.key)
            return -1;
        if (this.key > T.key)
            return 1;
        return 0;
    }
}