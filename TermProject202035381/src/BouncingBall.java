import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.*;
import java.util.Random;

public class BouncingBall extends JPanel implements ActionListener, MouseListener {
	Random random = new Random();
    private int x = random.nextInt(400)+150;
    private int y = random.nextInt(400)+150;
    private double xSpeed = random.nextInt(2)==1 ?1.5:-1.5;
    private double ySpeed = random.nextInt(2)==1 ?1.5:-1.5;
    
    private final int diameter = 100;
    private BufferedImage ballImage; // 이미지를 저장할 BufferedImage 변수
    private BufferedImage newImage; // New image for when HP reaches 0
    private int hp = 10; // 초기 HP 값
    private boolean speedIncreased = false; // 속도 증가 여부를 확인하는 변수
    private long startTime; // 게임 시작 시간을 저장할 변수
    private static final String TIME_RECORD_FILE = "time_records.txt";
    private boolean timeRecorded = false; // Flag to track whether time has been recorded
    private JButton returnButton;
    
    public BouncingBall() {
        Timer timer = new Timer(5, this);
        timer.start();
        addMouseListener(this); // 마우스 이벤트를 받기 위해 MouseListener를 추가합니다.

        // 게임 시작 시간을 기록합니다.
        startTime = System.currentTimeMillis();
        // 이미지를 로드합니다.
        try {
            ballImage = ImageIO.read(new File("images/ball.png")); // 이미지 파일의 경로를 지정합니다.
            newImage = ImageIO.read(new File("images/new_ball.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(null); // Use null layout to place button manually
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 이미지를 그립니다.
        if (ballImage != null) {
            g.drawImage(hp>0 ? ballImage : newImage, x, y, diameter, diameter, this);
        }
        // HP 바를 그립니다.
        int barWidth = 200; // HP 바의 너비
        int barHeight = 20; // HP 바의 높이
        int barX = 200; // HP 바의 X 좌표
        int barY = 10; // HP 바의 Y 좌표

        g.setColor(Color.GRAY); // 배경 색상을 설정합니다.
        g.fillRect(barX, barY, barWidth, barHeight); // HP 바의 배경을 그립니다.

        g.setColor(Color.RED); // HP 색상을 설정합니다.
        g.fillRect(barX, barY, hp * 2, barHeight); // 현재 HP를 기반으로 HP 바를 그립니다.

        g.setColor(Color.BLACK); // 테두리 색상을 설정합니다.
        g.drawRect(barX, barY, barWidth, barHeight); // HP 바의 테두리를 그립니다.
        
        long currentTime = System.currentTimeMillis();
        int elapsedTime = (int) ((currentTime - startTime) / 1000); // 경과 시간을 초 단위로 변환합니다.

        g.drawString("Time: " + elapsedTime + "s", 
        		barX + barWidth + 10, barY + barHeight - 5); // 경과 시간을 HP 바 오른쪽에 표시합니다.
    }

    public void actionPerformed(ActionEvent e) {
        // HP가 90 이하이고 속도가 아직 증가하지 않았을 때 속도를 증가시킵니다.
        if (hp <= 5 && !speedIncreased) {
            xSpeed *= 2; // 속도를 현재 속도의 3배로 증가시킵니다.
            ySpeed *= 2;
            speedIncreased = true; // 속도가 증가했음을 표시합니다.
        }

        if (x + xSpeed < 0 || x + xSpeed > getWidth() - diameter) {
            xSpeed = -xSpeed; //*(0.5+Math.random()*1.5)
        }
        if (y + ySpeed < 0 || y + ySpeed > getHeight() - diameter) {
            ySpeed = -ySpeed;
        }
        x += xSpeed;
        y += ySpeed;
        repaint();
        
        if (hp == 0 && !timeRecorded) { // Check if HP is 0 and time has not been recorded yet
            recordTime();
            timeRecorded = true; // Set the flag to true after recording time
            showReturnButton();
        }
    }

    // 마우스 클릭 이벤트를 처리합니다.
    public void mouseClicked(MouseEvent e) {
        // 클릭한 위치와 공의 중심점 사이의 거리를 계산합니다.
        int centerX = x + diameter / 2;
        int centerY = y + diameter / 2;
        int distanceX = e.getX() - centerX;
        int distanceY = e.getY() - centerY;
        double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);

        // 공 안에 클릭한 경우에만 HP를 감소시킵니다.
        if (distance <= diameter / 2) {
            if (hp > 0) {
                hp -= 1; // HP를 1 감소시킵니다.
            }
            repaint();
        }
    }

    // 다음 메서드들은 MouseListener 인터페이스의 다른 메서드들입니다.
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    private void recordTime() {
        long currentTime = System.currentTimeMillis();
        int elapsedTime = (int) ((currentTime - startTime) / 1000);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TIME_RECORD_FILE, true))) {
            writer.write(elapsedTime + "s\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showReturnButton() {
        returnButton = new JButton("Return to Main Menu");
        returnButton.setBounds(250, 300, 200, 50); // Position the button manually
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.getWindowAncestor(BouncingBall.this).dispose(); // Close the current frame
                new MainFrame(); // Open the MainFrame
            }
        });
        add(returnButton);
        revalidate();
        repaint();
    }
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 400);
//        frame.add(new BouncingBall());
//        frame.setVisible(true);
//    }
}