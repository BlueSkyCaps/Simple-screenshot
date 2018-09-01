
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
	// 获取默认工具包
	Toolkit tk = Toolkit.getDefaultToolkit();
	// 获取显示器像素大小
	Dimension dm = tk.getScreenSize();

	public static void main(String[] args) {
		// 创建程序启动对象
		new ScreenShot();
	}
	
	public ScreenShot() {
		 // 创建窗口对象
		 jf = new JFrame();
		 // 创建面板对象
		 jp = new JPanel();
		 // 创建按钮对象
		 wholeScreen = new JButton("全屏截图");
		 anySize = new JButton("矩形截图");
		 // 按钮禁用焦点
		 wholeScreen.setFocusPainted(false);
		 anySize.setFocusPainted(false);
		 // 设置按钮背景色
		 wholeScreen.setBackground(Color.WHITE);		  
		 anySize.setBackground(Color.WHITE);
		 // 将按钮添加至面板
		 jp.add(wholeScreen);
		 jp.add(anySize);
		 // 将面板添加在窗口对象
		 jf.add(jp);
		 // 设置居中显示
		 jf.setLocationRelativeTo(null);
		 jf.setSize(230,70);
		 // 创建图标对象
	   	 ImageIcon icon =  new ImageIcon("image/icon1.jpg");
	   	 // 设置图标
		 jf.setIconImage(icon.getImage());
		 jf.setTitle("截图");
		 // 显示窗口实例
		 jf.setVisible(true);
		 // 设置程序默认退出模式
		 jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 // 禁用大小
		 jf.setResizable(false);
		 // 添加监听事件
		 wholeScreen.addMouseListener(this);
		 anySize.addMouseListener(this);
		
	}
	
	public void mouseClicked(MouseEvent e) {
		/*
		 *响应全屏截图事件逻辑 
		 */
		if(e.getSource()== wholeScreen){
			// 入口窗口不可见
			jf.setVisible(false);		
			try {
				// 获取机器人对象
				rb = new Robot();
			} catch (AWTException eo) {
				eo.printStackTrace();
			}
			try {
				// 线程停止一秒
				Thread.sleep(1000);
				// 返回显示器宽高
				Rectangle rc = new Rectangle(0,0,(int)dm.getWidth(),(int)dm.getHeight());
				// 调用机器人, 获取完整窗口像素
			    BufferedImage buimg = rb.createScreenCapture(rc);
			    // 获取windows桌面绝对路径
			    File filePath2 = FileSystemView.getFileSystemView().getHomeDirectory();
			    // 获取日期对象
			    SimpleDateFormat fmt2 = new SimpleDateFormat("MMddHHmmss");
			    // 格式化日期显示格式
			    String fdate2 = fmt2.format(new Date());
//			    System.out.println(filePath2);
			    // 输出图片
			    ImageIO.write(buimg, "png", new File(filePath2+File.separator+fdate2+"catch.jpg"));
		        JOptionPane.showMessageDialog(null, "   截图已成功保存至桌面！");
		        // 显示主窗口
			    jf.setVisible(true);
			    
			} catch (InterruptedException | IOException ex) {
				ex.printStackTrace();
			}
		}
		/*
		 *响应矩形截图事件逻辑 
		 */
		if(e.getSource()==anySize) {			
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
	public void showCorPanel() {}

}
