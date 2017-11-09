import java.io.*;

public class DeCompress {
    private String in;
    private String out;
    private File input;
    private File output;
    private InputStream inputStream;

    DeCompress(String in, String out) throws IOException {
        this.in = in;
        this.out = out;
        this.input = new File(in);
        this.output = new File(out);
        this.inputStream = new FileInputStream(input);
        filesDeCompress(input, output, 0, input.length());
    }

    public String filesDeCompress(File input, File output, long CurrentLocation, long NextLocation) throws IOException {
        if (!this.output.exists()) {
            return "OutFolder is not exist";
        } else if (!this.output.isDirectory()) {
            return "OutPutPath is not a folder";
        } else {
            this.inputStream = new FileInputStream(input);
            inputStream.skip(CurrentLocation);
            byte[] fileNameLength = new byte[4];
            inputStream.read(fileNameLength);
            int length = byte4ToInt(fileNameLength, 0);
            if (length > 100000) {
                byte[] filesNumber = new byte[4];
                this.inputStream = new FileInputStream(input);
                inputStream.skip(NextLocation - 4);
                inputStream.read(filesNumber);
                int filesLocation = byte4ToInt(filesNumber, 0);
                this.inputStream = new FileInputStream(input);
                inputStream.skip(NextLocation - 4 - filesLocation);
                long[] Locations = new long[filesLocation / 8];
                for (int i = 0; i < filesLocation / 8; i++) {
                    byte[] Location = new byte[8];
                    inputStream.read(Location);
                    Locations[i] = BytesToLong(Location);
                }
                this.inputStream = new FileInputStream(input);
                inputStream.skip(4 + CurrentLocation);
                byte[] fileNumber1 = new byte[4];
                inputStream.read(fileNumber1);
                int filesNumber1 = byte4ToInt(fileNumber1, 0);
                byte[] filename = new byte[length - 100000];
                inputStream.read(filename);
                String fileName = new String(filename);
                StringBuilder add = new StringBuilder(output.getAbsolutePath());
                add.append("/" + fileName);
                File create = new File(add.toString());
                if (!create.exists()) {
                    create.mkdirs();
                } else {
                    throw new IllegalArgumentException("Folder has existed");
                }
                inputStream.close();
                for (int i = 0; i < filesNumber1; i++) {
                    if (i == 0) {
                        filesDeCompress(this.input, create, (long) (CurrentLocation + 8 + length - 100000), Locations[0]);
                        continue;
                    }
                    if (i <= filesNumber1 - 1) {
                        filesDeCompress(this.input, create, Locations[i - 1], Locations[i]);
//                    } else {
//                        filesDeCompress(this.input, create, Locations[i - 1], 0);
                    }
                }
            } else {
                byte[] filename = new byte[length];
                inputStream.read(filename);
                StringBuilder add = new StringBuilder(output.getAbsolutePath());
                String addString = new String(filename);
                add.append("/" + addString);
                String createPath = add.toString();
                File create = new File(createPath);
                if (!create.exists()) {
                    create.createNewFile();
                }
                OutputStream outputStream = new FileOutputStream(create);
                unzip.beginHunzipping(this.inputStream, outputStream);
                return "DeCompress Succeed";
            }
        }

        return "DeCompress failed";
    }

    public static int byte4ToInt(byte[] bytes, int off) {
        int b0 = bytes[off] & 0xFF;
        int b1 = bytes[off + 1] & 0xFF;
        int b2 = bytes[off + 2] & 0xFF;
        int b3 = bytes[off + 3] & 0xFF;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }


    public long BytesToLong(byte[] buffer) {
        long values = 0;
        for (int i = 0; i < 8; i++) {
            values <<= 8;
            values |= (buffer[i] & 0xff);
        }
        return values;
    }

}
