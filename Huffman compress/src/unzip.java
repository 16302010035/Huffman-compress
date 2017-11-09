import java.io.*;
import java.util.PriorityQueue;

public class unzip {
    private static PriorityQueue<hfmNode> pq1 = new PriorityQueue<hfmNode>();
    private static int[] counts1 = new int[300];
    private static String[] saveCode1 = new String[300];
    private static String[] btost = new String[300];
    private static String bigone;
    private static String temp;
    private static int exbits1;
    private static int putit;
    private static int cntu;


    private static hfmNode Root;


    public static void initHunzipping() {
        int i;
        if (Root != null)
            freeTree(Root);
        for (i = 0; i < 300; i++)
            counts1[i] = 0;
        for (i = 0; i < 300; i++)
            saveCode1[i] = "";
        pq1.clear();
        bigone = "";
        temp = "";
        exbits1 = 0;
        putit = 0;
        cntu = 0;
    }

    public static void freeTree(hfmNode now) {

        if (now.leftChild == null && now.rightChild == null) {
            now = null;
            return;
        }
        if (now.leftChild != null)
            freeTree(now.leftChild);
        if (now.rightChild != null)
            freeTree(now.rightChild);
    }


    public static void readfreq1(InputStream inputStream) {

        int key, i;
        Byte by;
        try {
            DataInputStream data_in = new DataInputStream(inputStream);
            cntu = data_in.readInt();

            for (i = 0; i < cntu; i++) {
                by = data_in.readByte();
                key = data_in.readInt();
                counts1[to(by)] = key;
            }

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }


        HfmTree hfmTree1 = new HfmTree(saveCode1,counts1,cntu,Root,pq1);
        cntu = hfmTree1.Makepq();
        hfmTree1.createTree();
        hfmTree1.getCode();

        for (i = 0; i < 256; i++) {
            if (saveCode1[i] == null)
                saveCode1[i] = "";
        }
    }


    public static void createbin() {
        int i, j;
        String t;
        for (i = 0; i < 256; i++) {
            btost[i] = "";
            j = i;
            while (j != 0) {
                if (j % 2 == 1)
                    btost[i] += "1";
                else
                    btost[i] += "0";
                j /= 2;
            }
            t = "";
            for (j = btost[i].length() - 1; j >= 0; j--) {
                t += btost[i].charAt(j);
            }
            btost[i] = t;
        }
        btost[0] = "0";
    }

    public static int got() {
        int i;

        for (i = 0; i < 256; i++) {
            if (saveCode1[i].compareTo(temp) == 0) {
                putit = i;
                return 1;
            }
        }
        return 0;

    }

    public static int to(Byte b) {
        int ret = b;
        if (ret < 0) {
            ret = ~b;
            ret = ret + 1;
            ret = ret ^ 255;
            ret += 1;
        }
        return ret;
    }

    public static String makeeight(String b) {
        String ret = "";
        int i;
        int len = b.length();
        for (i = 0; i < (8 - len); i++)
            ret += "0";
        ret += b;
        return ret;
    }

    public static void readbin(InputStream inputStream, OutputStream outputStream) {
        int ok, bt;
        Byte b;
        int j, i;
        bigone = "";
        try {
            DataOutputStream data_out = new DataOutputStream(outputStream);
            DataInputStream data_in = new DataInputStream(inputStream);
            try {
                exbits1 = data_in.readInt();
                System.out.println(exbits1);

            } catch (EOFException eof) {
                System.out.println("End of File");
            }
            byte[] by = new byte[12];
            data_in.read(by);
            while (!isover(by)) {
                try {
                    b = by[0];
                    bt = to(b);
                    bigone += makeeight(btost[bt]);

                    while (true) {
                        ok = 1;
                        temp = "";
                        for (i = 0; i < bigone.length() - exbits1; i++) {
                            temp += bigone.charAt(i);
                            if (got() == 1) {
                                data_out.write(putit);
                                ok = 0;
                                String s = "";
                                for (j = temp.length(); j < bigone.length(); j++) {
                                    s += bigone.charAt(j);
                                }
                                bigone = s;
                                break;
                            }
                        }

                        if (ok == 1)
                            break;
                    }
                    b = data_in.readByte();
                    for (int t = 0; t < 11; t++) {
                        by[t] = by[t + 1];
                    }
                    by[11] = b;

                } catch (EOFException eof) {
                    System.out.println("wocuole");
                    System.out.println("End of File");
                    break;
                }
            }
            data_out.close();
            outputStream.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }

    }

    public static boolean isover(byte[] by) {
        String res = new String(by);
        return (res.equals("polarwangkai"));
    }


    public static void beginHunzipping(InputStream inputStream,OutputStream outputStream) {
        initHunzipping();
        readfreq1(inputStream);
        createbin();

        readbin(inputStream, outputStream);
        initHunzipping();
    }

}