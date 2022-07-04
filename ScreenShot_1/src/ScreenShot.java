
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
/**
 * @author ZREO
 * ScreenShot 1.0
 * this is a simple screenshot by java
 */
public class ScreenShot extends JFrame implements MouseListener{

	private static final long serialVersionUID = -4307926428530522814L;
	JFrame jf;
	JLabel jl;
	JButton wholeScreenBtn;
	JButton anySizeBtn;
	JPanel jp;
	Robot rb;
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension dm = tk.getScreenSize();

	public static void main(String[] args) {
		// 程序启动对象
		new ScreenShot();
	}
	
	public ScreenShot() {
		 jf = new JFrame();
		 jp = new JPanel();
		 wholeScreenBtn = new JButton("全屏截图");
		 anySizeBtn = new JButton("矩形截图");
		 // 按钮禁用焦点
		 wholeScreenBtn.setFocusPainted(false);
		 anySizeBtn.setFocusPainted(false);
		 wholeScreenBtn.setBackground(Color.WHITE);
		 anySizeBtn.setBackground(Color.WHITE);
		 jp.add(wholeScreenBtn);
		 jp.add(anySizeBtn);
		 jf.add(jp);
		 jf.setLocationRelativeTo(null);
		 jf.setSize(230,70);
		 // 创建图标对象
	   	 ImageIcon icon =  new ImageIcon("image/icon1.jpg");
		 jf.setIconImage(icon.getImage());
		 jf.setTitle("截图");

		 jf.setVisible(true);
		 jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 jf.setResizable(false);
		 // 添加监听事件
		 wholeScreenBtn.addMouseListener(this);
		 anySizeBtn.addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {
		/*
		 *响应全屏截图事件
		 */
		if(e.getSource()== wholeScreenBtn){
			// 入口窗口不可见
			jf.setVisible(false);		
			try {
				rb = new Robot();
			} catch (AWTException eo) {
				eo.printStackTrace();
			}
			try {
				Thread.sleep(1000);
				// 返回显示器宽高
				Rectangle rc = new Rectangle(0,0,(int)dm.getWidth(),(int)dm.getHeight());
				// 调用机器人, 获取完整窗口像素大小
			    BufferedImage buImg = rb.createScreenCapture(rc);
			    // 获取windows桌面绝对路径
			    File homePath = FileSystemView.getFileSystemView().getHomeDirectory();
			    SimpleDateFormat dataFormat = new SimpleDateFormat("MMddHHmmss");
			    String dataStr = dataFormat.format(new Date());
			    // 输出图片
			    ImageIO.write(buImg, "png", new File(homePath+File.separator+dataStr+"catch.jpg"));
		        JOptionPane.showMessageDialog(null, "   截图已成功保存至桌面！");
		        // 显示主窗口
			    jf.setVisible(true);
			    
			} catch (InterruptedException | IOException ex) {
				ex.printStackTrace();
			}
		}
		
		/*
		 *响应矩形截图事件
		 */
		if(e.getSource()== anySizeBtn) {
			try {
				// 隐藏主窗口
				jf.setVisible(false);
				Thread.sleep(1000);
				// 创建矩形截图实例
				new RectangleShot();
			} catch (InterruptedException e1) {			
				e1.printStackTrace();
			}			
		}
  }
	
    /*
     * 未实现的方法
     */
	public void mousePressed(MouseEvent e1) {}
	public void mouseReleased(MouseEvent e2) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
