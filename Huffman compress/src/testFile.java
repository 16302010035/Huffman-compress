import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class testFile {
    public static void main(String[] args) {
        String file1 = "/home/wangkai/桌面/test/test4 - large file";
        File file = new File(file1);
        File[] files = file.listFiles();
        int nums = files.length;
        for (int i = 0; i < nums; i++) {
            try {
                System.out.print("文件名:"+files[i].getName()+"    ");
                System.out.print("源文件大小:"+files[i].length()+"    ");
                long startTime = System.currentTimeMillis();
                String write = "/home/wangkai/桌面/afterZip/";
                File Write = new File(write+files[i].getName()+".wang");
                FileOutputStream outputStream = new FileOutputStream(Write);
                Compress compress = new Compress(files[i].getPath(),write);
                zip.beginHzipping(files[i], outputStream);outputStream.close();
                long endTime = System.currentTimeMillis();
                System.out.print("压缩文件大小:"+Write.length()+"   ");
                double index = (double) Write.length() / (double) files[i].length();
                int temp = (int)(index * 1000);
                index = (double)temp / 10;
                System.out.print("压缩率:" + index+"%"+"   ");
                long time = endTime - startTime;
                System.out.println("时间:"+time);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
