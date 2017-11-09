import javax.swing.*;
import java.awt.event.*;
import java.io.*;


public class Main extends JFrame implements ActionListener {

    private static File opened_file, other_file, write_file;
    private static JLabel PATH;
    private static JPanel buttonPanel;
    private static JButton ZH, UH, EX;

    public JPanel contentPane() {
        JPanel titlePanel, scorePanel;
        JLabel path;

        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(null);

        titlePanel = new JPanel();
        titlePanel.setLayout(null);
        titlePanel.setLocation(90, 20);
        titlePanel.setSize(170, 70);
        totalGUI.add(titlePanel);

        scorePanel = new JPanel();
        scorePanel.setLayout(null);
        scorePanel.setLocation(270, 20);
        scorePanel.setSize(700, 60);
        totalGUI.add(scorePanel);

        path = new JLabel("选中的文件:");
        path.setLocation(0, 0);
        path.setSize(100, 30);
        path.setHorizontalAlignment(0);
        titlePanel.add(path);

        PATH = new JLabel("");
        PATH.setLocation(0, 0);
        PATH.setSize(200, 30);
        PATH.setHorizontalAlignment(0);
        scorePanel.add(PATH);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setLocation(10, 100);
        buttonPanel.setSize(5200, 150);
        totalGUI.add(buttonPanel);

        ZH = new JButton("开始压缩");
        ZH.setLocation(190, 20);
        ZH.setSize(120, 30);
        ZH.addActionListener(this);
        buttonPanel.add(ZH);

        UH = new JButton("开始解压");
        UH.setLocation(370, 20);
        UH.setSize(120, 30);
        UH.addActionListener(this);
        buttonPanel.add(UH);

        EX = new JButton("退出");
        EX.setLocation(130, 100);
        EX.setSize(250, 30);
        EX.addActionListener(this);
        buttonPanel.add(EX);

        totalGUI.setOpaque(true);
        return totalGUI;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ZH) {
            try {
                if (opened_file.isDirectory()) {
                    File file = new File(opened_file.getPath());
                    File[] files = file.listFiles();
                    if (files.length == 0)
                        JOptionPane.showMessageDialog(null, "所选文件夹为空，请重新选择。", "状态", JOptionPane.PLAIN_MESSAGE);
                    else {
                        Compress compress = new Compress(opened_file.getPath(), write_file.getPath() + "/");
                        JOptionPane.showMessageDialog(null, "压缩成功", "状态", JOptionPane.PLAIN_MESSAGE);
                    }
                } else {
                    File file1 = new File(opened_file.getPath());
                    if (file1.length() == 0)
                        JOptionPane.showMessageDialog(null, "所选文件为空，请重新选择。", "状态", JOptionPane.PLAIN_MESSAGE);
                    else {
                        long startTime = System.currentTimeMillis();
                        File file = new File(write_file.getPath() + "/" + opened_file.getName() + ".wang");
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        zip.beginHzipping(opened_file, fileOutputStream);
                        fileOutputStream.close();
                        long endTime = System.currentTimeMillis();
                        long time = endTime - startTime;
                        System.out.println(time/1000);
                        JOptionPane.showMessageDialog(null, "压缩成功", "状态", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            other_file = new File(opened_file.getPath() + ".wang");
        } else if (e.getSource() == UH) {
            try {
                String fileName = opened_file.getName();
                String[] name = fileName.split("\\.");
                if (name[1].equals("wangFolder")) {
                    DeCompress deCompress = new DeCompress(opened_file.getPath(), write_file.getPath() + "/");
                } else {
                    long startTime = System.currentTimeMillis();
                    String s = opened_file.getName();
                    s = s.substring(0, s.length() - 5);
                    File output = new File(write_file.getPath() + "/" + s);
                    InputStream inputStream = new FileInputStream(opened_file);
                    OutputStream outputStream = new FileOutputStream(output);
                    unzip.beginHunzipping(inputStream, outputStream);
                    long endTime = System.currentTimeMillis();
                    long time = endTime - startTime;
                    System.out.println(time / 1000);
                }
                JOptionPane.showMessageDialog(null, "解压缩成功", "状态", JOptionPane.PLAIN_MESSAGE);
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } else if (e.getSource() == EX) {
            System.exit(0);
        }
    }

    private static void createAndShowGUI() {


        // JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("文件压缩器");

        // Create and set up the content pane.
        Main demo = new Main();
        frame.setContentPane(demo.contentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(350, 170, 550, 300);

        frame.setVisible(true);

        JButton openFile = new JButton("选择文件(夹)");
        openFile.setLocation(20, 0);
        openFile.setSize(120, 30);

        JButton openFiles = new JButton("选择输出目录");
        openFiles.setLocation(20, 40);
        openFiles.setSize(120, 30);
        buttonPanel.add(openFile);
        buttonPanel.add(openFiles);
        openFile.addActionListener(
                event -> {
                    JFileChooser jc = new JFileChooser();
                    jc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    jc.showOpenDialog(null);
                    opened_file = jc.getSelectedFile();
                    PATH.setText(opened_file.getPath());
                    System.out.println(opened_file.getPath());
                });
        openFiles.addActionListener(
                e -> {
                    JFileChooser jFileChooser = new JFileChooser();
                    jFileChooser.setFileSelectionMode(jFileChooser.DIRECTORIES_ONLY);
                    jFileChooser.showOpenDialog(null);
                    write_file = jFileChooser.getSelectedFile();
                }
        );
    }

    public static void main(String[] args) {
        Runnable updateAComponent = () -> createAndShowGUI();
        SwingUtilities.invokeLater(updateAComponent);
    }
}
