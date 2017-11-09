import java.io.*;

public class Compress {
    private String in;
    private String out;
    private File input;
    private File output;
    private OutputStream outputStream;
    private String inputname;
    private File outFile;


    Compress(String in, String out) throws IOException {
        this.in = in;
        this.out = out;
        input = new File(in);
        output = new File(out);
        inputname = input.getName();
        String outfile = "";
        outfile = CreateFile(out, inputname);
        outFile = new File(outfile);
        outputStream = new FileOutputStream(outFile, true);
        filesCompress(input, output);


    }

    public void filesCompress(File input, File output) throws IOException {
        if (input != null && input.exists() && input.isDirectory() &&
                output != null && output.exists() && output.isDirectory()) {
            File[] files = input.listFiles();
            long[] filesNumber = new long[files.length];
            String Name = input.getName();
            byte[] folderName = (Name).getBytes();
            outputStream = new FileOutputStream(outFile, true);
            int nameLength = folderName.length + 100000;
            outputStream.write(intToByte4(nameLength));
            int fileNumber = files.length;
            outputStream.write(intToByte4(fileNumber));
            outputStream.write(folderName);
            for (int i = 0; i < files.length; i++) {
                filesCompress(files[i], this.output);
                filesNumber[i] = this.outFile.length();
            }
            outputStream = new FileOutputStream(outFile, true);
            for (int i = 0; i < filesNumber.length; i++) {
                byte[] fileBegin = LongToBytes(filesNumber[i]);
                outputStream.write(fileBegin);
            }
            int filesNumberLength = filesNumber.length * 8;
            byte[] fileEnd = intToByte4(filesNumberLength);
            outputStream.write(fileEnd);
            outputStream.close();
        } else if (input != null && input.exists() && output != null && output.exists() && output.isDirectory()) {
            String inputName = input.getName();
            byte[] l = inputName.getBytes();
            int nameLength = l.length;
            outputStream = new FileOutputStream(outFile, true);
            outputStream.write(intToByte4(nameLength));
            outputStream.write(l);
            zip.beginHzipping(input, outputStream);
        }
    }

    public static String CreateFile(String filename, String filepath) {
        Boolean bool = false;
        String fileNameTemp = filename + "/" + filepath + ".wangFolder";
        File file = new File(fileNameTemp);
        try {
            if (!file.exists()) {
                file.createNewFile();
                bool = true;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Create failed");
        }
        if (bool) {
            return fileNameTemp;
        } else {
            return "Create failed";
        }
    }

    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
    }


    public byte[] LongToBytes(long values) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            int offset = 64 - (i + 1) * 8;
            buffer[i] = (byte) ((values >> offset) & 0xff);
        }
        return buffer;
    }


}
