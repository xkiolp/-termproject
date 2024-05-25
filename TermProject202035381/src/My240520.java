import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;

public class My240520 {

    public static void main(String[] args) {
        MainFrame mf = new MainFrame();
    }

}

class MainFrame extends JFrame {
	
	private static final String TIME_RECORD_FILE = "time_records.txt";
	
    MainFrame() {
        setTitle("괴물 잡기");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        
        setSize(700, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.ORANGE); // 배경색 설정

        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // FlowLayout으로 변경하고 가운데 정렬
       

        // JButton 생성 및 추가
        JLabel imageLabel = null;
        try {
            BufferedImage originalImage = ImageIO.read(new File("images/ball.png")); // Change to your image path
            Image scaledImage = originalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Resize to 100x100
            ImageIcon icon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(icon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton button1 = new JButton("시작하기");
        JButton button2 = new JButton("점수판");
        JButton button3 = new JButton("종료");
       
        Dimension buttonSize = new Dimension(400, 100);
        button1.setPreferredSize(buttonSize);
        button2.setPreferredSize(buttonSize);
        button3.setPreferredSize(buttonSize);
        
        //시작버튼 리스너
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
                Frame2 frame2 = new Frame2();
                
            }
        });
        
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRecordedTimes();
            }
        });
        
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        // JButton을 JPanel에 추가
        if (imageLabel != null) {
            mainPanel.add(imageLabel);
        }
        mainPanel.add(button1);
        mainPanel.add(button2);
        mainPanel.add(button3);

        add(mainPanel); // JPanel을 JFrame에 추가

        setVisible(true);
    }
    
    private void displayRecordedTimes() {
        StringBuilder times = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(TIME_RECORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                times.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, times.toString(), "Recorded Times", JOptionPane.INFORMATION_MESSAGE);
    }
}

class Frame2 extends JFrame {
    Frame2() {
        setTitle("Frame 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLocationRelativeTo(null);
        JFrame frame = new JFrame();
		add(new BouncingBall());
        setVisible(true);
        

    }
}

