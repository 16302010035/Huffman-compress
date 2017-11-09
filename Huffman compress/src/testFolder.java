import java.io.File;
import java.io.IOException;

public class testFolder {
    private static int length = 0;

    public static void main(String[] args) {
        String open = "/home/wangkai/桌面/test/test2 - folder";
        String write = "/home/wangkai/桌面/afterZip/";
        File file = new File(open);
        File[] files = file.listFiles();
        int nums = files.length;
        for (int i = 0; i < nums; i++) {
            try {
                System.out.println(files[i].getPath());
                System.out.print("源文件名:"+files[i].getName()+"    ");
                System.out.print("源文件大小:"+getLength(files[i])+"     ");
                long startTime = System.currentTimeMillis();
                Compress compress = new Compress(files[i].getPath(),write);
                long endTime = System.currentTimeMillis();
                File endFIle = new File(write+files[i].getName()+".wangFolder");
                System.out.print("压缩后文件大小:"+endFIle.length()+"     ");
                double index = (double)endFIle.length() /(double)getLength(files[i]);
                int temp = (int)(index * 1000);
                index = (double)temp / 10;
                System.out.print("压缩率:"+index+"%"+"    ");
                long time = endTime - startTime;
                System.out.println("时间:"+time+"    ");
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public static int getLength(File file) {
        if (file.isDirectory()) {
            for (File zf : file.listFiles()) {
                if (!zf.isDirectory()) length += zf.length();
                else getLength(zf);
            }
        }
        return length;
    }

}
