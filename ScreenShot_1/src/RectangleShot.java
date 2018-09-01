import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;
import javax.swing.filechooser.FileSystemView;

import static java.lang.Thread.sleep;

public class RectangleShot extends JWindow implements MouseMotionListener{
 
	/**
	 * note: �����JPanel���JLabel��ʵ�ֵĻ������ڶ�������ֱ߿���������JPanel��JWindow�е��µġ�
	 * ����ҪJPanel�����ƣ�ֱ��ʹ��JLabel��ʵ�ֿ��Ա�������⡣
	 */
	
	private static final long serialVersionUID = 653148943638627173L;
	JWindow jw = new JWindow();
	Graphics g;
	Robot robot;
	BufferedImage image,saveImage;
	int originX,originY,releaseX,releaseY,endX,endY; 
	int clearW,clearH;
    JLabel jl = new JLabel(); 
    int ifClose = 1;

	public static void main(String[] args) {
		// ����
		new RectangleShot();
	}
	public RectangleShot (){
		// ����ǩ�������������
	    jw.add(jl);	   
	    // ��ȡ��Ļ��С
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
	    // ���ô�����ʾλ��
	    jw.setBounds(0, 0, d.width, d.height);
	    jw.setVisible(true);   	   
	    // ��Ӽ���
	    jl.addMouseMotionListener(this);
	    robot = null;
		try {
			// ����������ʵ��
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		// ��ȡ��Ļ����
	    image = robot.createScreenCapture(new Rectangle(0, 0, d.width,d.height));
	    jl.setIcon(new ImageIcon(image));
	    jl.addMouseListener(new MouseAdapter() {			
	    	
			public void mousePressed(MouseEvent e) {
				//���½�ͼʱ�Ƴ��ϴε�jl,��ֹ���ο򶥲㸲��
				jw.remove(jl);
				jl.setIcon(new ImageIcon(image));
				jw.add(jl);  // �ٴ��������Ƚ�ͼ
				jw.repaint();  // �ػ洰��
				// ��ȡָ������
				originX=e.getX();
				originY=e.getY();						
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// ��ȡ�ͷ�����
				releaseX = e.getX();
				releaseY = e.getY();
				g.setColor(Color.red);
				// ���ƾ�����ʾ����
				g.drawRect(originX-1, originY-1, releaseX-originX, releaseY-originY);
				// ��ȡ���ο��
				clearW = releaseX-originX;
				clearH = releaseY-originY;
				// �洢��ʱ��ͼ
				saveImage = robot.createScreenCapture(new Rectangle(
						originX,originY, clearW-1,clearH-1));
				// ������Ӧ����
				int option = JOptionPane.showConfirmDialog(null, ""
						+ "           �Ƿ񱣴��ͼ��", null, 0);
				if(option==1) {
					// ���½�ͼ
					jw.setVisible(true);
					mousePressed(e);					
				}
				else if(option==0){
					// ������������
					try {
						// �����ͼ
						saveShot();
						// ��ʾ������
						jw.dispose();
						new ScreenShot().jf.setVisible(true);
					} catch (IOException e1) {
						System.out.println("method: saveShot() can't call");
						e1.printStackTrace();
					}
				}
		        else {		        	
		        	// ��ʾ������
		        	jw.dispose(); 	
		        	new ScreenShot().jf.setVisible(true);
		        }	
			}
	    });	    
	}

	/*
	 * ��Ӧ�����ק�¼�
	 */
	public void mouseDragged(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();	
		// ���ر�ǩ�����ǰ�Ļ��ƶ���
		g = jl.getGraphics();
		g.setColor(Color.red);
		System.out.println("��ק");		
	}
	
	public void saveShot() throws IOException {
		 // ��ȡwindows�������·��
		 File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
		 // ��ȡ���ڶ���
		 SimpleDateFormat fmt = new SimpleDateFormat("MMddHHmmss");
		 // ��ʽ�����ڶ���
	     String fdate = fmt.format(new Date());
	     System.out.println(filePath);
	     // ���ͼƬ
		 ImageIO.write(saveImage, "png", new File(filePath+File.separator+fdate+"catch.jpg"));
		 JOptionPane.showMessageDialog(null, "      ��ͼ�ѳɹ����������棡");
	}

	public void mouseMoved(MouseEvent e) {}
}
