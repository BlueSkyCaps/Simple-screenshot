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
	 * note: 如果用JPanel添加JLabel来实现的话，窗口顶部会出现边框栏。这是JPanel在JWindow中导致的。
	 * 不需要JPanel来绘制，直接使用JLabel来实现可以避免此问题。
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
		// 启动
		new RectangleShot();
	}
	public RectangleShot (){
		// 将标签对象添加至窗口
	    jw.add(jl);	   
	    // 获取屏幕大小
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
	    // 设置窗口显示位置
	    jw.setBounds(0, 0, d.width, d.height);
	    jw.setVisible(true);   	   
	    // 添加监听
	    jl.addMouseMotionListener(this);
	    robot = null;
		try {
			// 创建机器人实例
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		// 获取屏幕像素
	    image = robot.createScreenCapture(new Rectangle(0, 0, d.width,d.height));
	    jl.setIcon(new ImageIcon(image));
	    jl.addMouseListener(new MouseAdapter() {			
	    	
			public void mousePressed(MouseEvent e) {
				//重新截图时移除上次的jl,防止矩形框顶层覆盖
				jw.remove(jl);
				jl.setIcon(new ImageIcon(image));
				jw.add(jl);  // 再次载入最先截图
				jw.repaint();  // 重绘窗口
				// 获取指针坐标
				originX=e.getX();
				originY=e.getY();						
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// 获取释放坐标
				releaseX = e.getX();
				releaseY = e.getY();
				g.setColor(Color.red);
				// 绘制矩形显示区域
				g.drawRect(originX-1, originY-1, releaseX-originX, releaseY-originY);
				// 获取矩形宽高
				clearW = releaseX-originX;
				clearH = releaseY-originY;
				// 存储临时截图
				saveImage = robot.createScreenCapture(new Rectangle(
						originX,originY, clearW-1,clearH-1));
				// 返回响应代码
				int option = JOptionPane.showConfirmDialog(null, ""
						+ "           是否保存截图？", null, 0);
				if(option==1) {
					// 重新截图
					jw.setVisible(true);
					mousePressed(e);					
				}
				else if(option==0){
					// 返回最终像素
					try {
						// 保存截图
						saveShot();
						// 显示主窗口
						jw.dispose();
						new ScreenShot().jf.setVisible(true);
					} catch (IOException e1) {
						System.out.println("method: saveShot() can't call");
						e1.printStackTrace();
					}
				}
		        else {		        	
		        	// 显示主窗口
		        	jw.dispose(); 	
		        	new ScreenShot().jf.setVisible(true);
		        }	
			}
	    });	    
	}

	/*
	 * 响应鼠标拖拽事件
	 */
	public void mouseDragged(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();	
		// 返回标签组件当前的绘制对象
		g = jl.getGraphics();
		g.setColor(Color.red);
		System.out.println("拖拽");		
	}
	
	public void saveShot() throws IOException {
		 // 获取windows桌面绝对路径
		 File filePath = FileSystemView.getFileSystemView().getHomeDirectory();
		 // 获取日期对象
		 SimpleDateFormat fmt = new SimpleDateFormat("MMddHHmmss");
		 // 格式化日期对象
	     String fdate = fmt.format(new Date());
	     System.out.println(filePath);
	     // 输出图片
		 ImageIO.write(saveImage, "png", new File(filePath+File.separator+fdate+"catch.jpg"));
		 JOptionPane.showMessageDialog(null, "      截图已成功保存至桌面！");
	}

	public void mouseMoved(MouseEvent e) {}
}
