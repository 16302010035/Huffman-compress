import java.io.*;
import java.util.PriorityQueue;


public class zip {

    private static PriorityQueue<hfmNode> pq = new PriorityQueue<hfmNode>();
    private static int[] counts = new int[300];
    private static String[] saveCode = new String[300];
    private static int exbits;
    private static byte bt;
    private static int cnt; // number of different characters


    static hfmNode Root;

    public static void CalFreq(File file) {
        Byte bt;
        try {
            FileInputStream file_input = new FileInputStream(file);
            DataInputStream data_in = new DataInputStream(file_input);
            while (true) {
                try {

                    bt = data_in.readByte();
                    counts[to(bt)]++;
                } catch (EOFException eof) {
//                    System.out.println("End of File");
                    break;
                }
            }
            file_input.close();
            data_in.close();
        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }
        file = null;
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

    public static void initHzipping() {
        int i;
        cnt = 0;
        if (Root != null)
            freeTree(Root);
        for (i = 0; i < 300; i++)
            counts[i] = 0;
        for (i = 0; i < 300; i++)
            saveCode[i] = "";
        pq.clear();
    }

    public static void freeTree(hfmNode root) {

        if (root.leftChild == null && root.rightChild == null) {
            root = null;
            return;
        }
        if (root.leftChild != null)
            freeTree(root.leftChild);
        if (root.rightChild != null)
            freeTree(root.rightChild);
    }


    public static void fakezip(File filei) {

        File fileo;
        fileo = new File("fakezipped.txt");
        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);
            PrintStream ps = new PrintStream(fileo);

            while (true) {
                try {
                    bt = data_in.readByte();
                    ps.print(saveCode[to(bt)]);
                } catch (EOFException eof) {
//                    System.out.println("End of File");
                    break;
                }
            }

            file_input.close();
            data_in.close();
            ps.close();

        } catch (IOException e) {
            System.out.println("IO Exception =: " + e);
        }

    }

    public static void realzip(String fname, OutputStream outputStream) {
        File filei;
        int i;
        Byte btt;

        filei = new File(fname);


        try {
            FileInputStream file_input = new FileInputStream(filei);
            DataInputStream data_in = new DataInputStream(file_input);

            DataOutputStream data_out = new DataOutputStream(outputStream);

            data_out.writeInt(cnt);
            for (i = 0; i < 256; i++) {
                if (counts[i] != 0) {
                    btt = (byte) i;
                    data_out.write(btt);
                    data_out.writeInt(counts[i]);
                }
            }
            long texbits;
            texbits = filei.length() % 8;
            texbits = (8 - texbits) % 8;
            exbits = (int) texbits;
            data_out.writeInt(exbits);
            while (true) {
                try {
                    bt = 0;
                    byte ch;
                    for (exbits = 0; exbits < 8; exbits++) {
                        ch = data_in.readByte();
                        bt *= 2;
                        if (ch == '1')
                            bt++;
                    }

                    data_out.write(bt);
                } catch (EOFException eof) {
                    int x;
                    if (exbits != 0) {
                        for (x = exbits; x < 8; x++) {
                            bt *= 2;
                        }
                        data_out.write(bt);
                    }

                    exbits = (int) texbits;
//                    System.out.println("extrabits: " + exbits);
//                    System.out.println("End of File");
                    break;
                }
            }
            String s = "polarwangkai";
            byte[] by = s.getBytes();
            data_out.write(by);
            data_in.close();
            file_input.close();

        } catch (IOException e) {
            System.out.println("IO exception = " + e);
        }
        filei.delete();
    }

    public static void beginHzipping(File file,OutputStream outputStream) {
        initHzipping();

        CalFreq(file);

        HfmTree hfmTree = new HfmTree(saveCode,counts,cnt,Root,pq);
        cnt = hfmTree.Makepq();
        hfmTree.createTree();
        hfmTree.getCode();

        fakezip(file);

        realzip("fakezipped.txt", outputStream);

        initHzipping();
    }
}