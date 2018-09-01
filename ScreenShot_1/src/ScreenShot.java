
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileSystemView;

public class ScreenShot extends JFrame implements MouseListener{
	/*
	 * @author ZREO
	 * @name ScreenShot 1.0
	 * @describe:this is a simple screen shot by java for 1.8u
	 */
	private static final long serialVersionUID = -4307926428530522814L;
	JFrame jf;
	JLabel jl;
	JButton wholeScreen,anySize,bd,be;
	JPanel jp;
	Robot rb;
	int mouseX,mouseY,releaseX,releaseY;
	// ��ȡĬ�Ϲ��߰�
	Toolkit tk = Toolkit.getDefaultToolkit();
	// ��ȡ��ʾ�����ش�С
	Dimension dm = tk.getScreenSize();

	public static void main(String[] args) {
		// ����������������
		new ScreenShot();
	}
	
	public ScreenShot() {
		 // �������ڶ���
		 jf = new JFrame();
		 // ����������
		 jp = new JPanel();
		 // ������ť����
		 wholeScreen = new JButton("ȫ����ͼ");
		 anySize = new JButton("���ν�ͼ");
		 // ��ť���ý���
		 wholeScreen.setFocusPainted(false);
		 anySize.setFocusPainted(false);
		 // ���ð�ť����ɫ
		 wholeScreen.setBackground(Color.WHITE);		  
		 anySize.setBackground(Color.WHITE);
		 // ����ť��������
		 jp.add(wholeScreen);
		 jp.add(anySize);
		 // ���������ڴ��ڶ���
		 jf.add(jp);
		 // ���þ�����ʾ
		 jf.setLocationRelativeTo(null);
		 jf.setSize(230,70);
		 // ����ͼ�����
	   	 ImageIcon icon =  new ImageIcon("image/icon1.jpg");
	   	 // ����ͼ��
		 jf.setIconImage(icon.getImage());
		 jf.setTitle("��ͼ");
		 // ��ʾ����ʵ��
		 jf.setVisible(true);
		 // ���ó���Ĭ���˳�ģʽ
		 jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 // ���ô�С
		 jf.setResizable(false);
		 // ��Ӽ����¼�
		 wholeScreen.addMouseListener(this);
		 anySize.addMouseListener(this);
		
	}
	
	public void mouseClicked(MouseEvent e) {
		/*
		 *��Ӧȫ����ͼ�¼��߼� 
		 */
		if(e.getSource()== wholeScreen){
			// ��ڴ��ڲ��ɼ�
			jf.setVisible(false);		
			try {
				// ��ȡ�����˶���
				rb = new Robot();
			} catch (AWTException eo) {
				eo.printStackTrace();
			}
			try {
				// �߳�ֹͣһ��
				Thread.sleep(1000);
				// ������ʾ�����
				Rectangle rc = new Rectangle(0,0,(int)dm.getWidth(),(int)dm.getHeight());
				// ���û�����, ��ȡ������������
			    BufferedImage buimg = rb.createScreenCapture(rc);
			    // ��ȡwindows�������·��
			    File filePath2 = FileSystemView.getFileSystemView().getHomeDirectory();
			    // ��ȡ���ڶ���
			    SimpleDateFormat fmt2 = new SimpleDateFormat("MMddHHmmss");
			    // ��ʽ��������ʾ��ʽ
			    String fdate2 = fmt2.format(new Date());
//			    System.out.println(filePath2);
			    // ���ͼƬ
			    ImageIO.write(buimg, "png", new File(filePath2+File.separator+fdate2+"catch.jpg"));
		        JOptionPane.showMessageDialog(null, "   ��ͼ�ѳɹ����������棡");
		        // ��ʾ������
			    jf.setVisible(true);
			    
			} catch (InterruptedException | IOException ex) {
				ex.printStackTrace();
			}
		}
		/*
		 *��Ӧ���ν�ͼ�¼��߼� 
		 */
		if(e.getSource()==anySize) {			
			try {
				// ����������
				jf.setVisible(false);
				Thread.sleep(1000);
				// �������ν�ͼʵ��
				new RectangleShot();
			} catch (InterruptedException e1) {			
				e1.printStackTrace();
			}			
		}
  }
	
    /*
     * δʵ�ֵķ���
     */
	public void mousePressed(MouseEvent e1) {}
	public void mouseReleased(MouseEvent e2) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void showCorPanel() {}

}
