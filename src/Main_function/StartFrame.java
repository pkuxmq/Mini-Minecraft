package Main_function;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import Main_function.*;

public class StartFrame extends JFrame {
    public void test1(){


       JFrame jf = new JFrame("Mini Minecraft");
        jf.setSize(960, 600);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // 创建内容面板，指定布局为 null，则使用绝对布局
        JPanel panel = new JPanel(null);

        ImageIcon icon=new ImageIcon("./background.jpg");
        //Image im=new Image(icon);
        //将图片放入label中
        JLabel label=new JLabel(icon);
        label.setBounds(0,0,jf.getWidth(),jf.getHeight());
        jf.getLayeredPane().add(label,new Integer(Integer.MIN_VALUE));
        JPanel j=(JPanel)jf.getContentPane();
        j.setOpaque(false);


        // 创建按钮
        JButton btn01 = new JButton("开始游戏");
        // 设置按钮的坐标
        btn01.setLocation(380, 300);
        // 设置按钮的宽高
        btn01.setSize(200, 50);
        panel.add(btn01);
        btn01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取到的事件源就是按钮本身
                jf.dispose();
               Main_function.getpressed(1);
               return;
            }
        });

        // 创建按钮
        JButton btn02 = new JButton("读取存档");
        // 设置按钮的界限(坐标和宽高)，设置按钮的坐标为(50, 100)，宽高为 100, 50
        btn02.setBounds(380, 400, 200, 50);
        panel.add(btn02);

        btn02.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取到的事件源就是按钮本身

                return;
            }
        });
        // 显示窗口
        panel.setOpaque(false);
        jf.setContentPane(panel);
     //   jf.pack();
        jf.setLocationRelativeTo(null);
        jf.setVisible(true);
    }

}

