
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
/**
 * @author ZREO
 * ScreenShot 1.0
 * this is a simple screenshot by java
 */
public class ScreenShot extends JFrame implements MouseListener{
	public static JFrame jf;
	private final JButton wholeScreenBtn;
	private final JButton anySizeBtn;
	private final JButton secondWholeScreenBtn;
	private final JButton secondAnySizeBtn;
	private final JPanel jp;
	private Robot rb;
	private final Toolkit tk = Toolkit.getDefaultToolkit();
	private Dimension dm = tk.getScreenSize();

	public static void main(String[] args) {
		// 程序启动对象
		new ScreenShot();
	}
	
	public ScreenShot() {
		 jf = new JFrame();
		jf.setFocusable(true);
		jp = new JPanel();

		 wholeScreenBtn = new JButton("全屏");
		 anySizeBtn = new JButton("矩形");
		secondWholeScreenBtn = new JButton("倒计时全屏(5s)");
		 secondAnySizeBtn = new JButton("倒计时矩形(5s)");
		 // 按钮禁用焦点
		 wholeScreenBtn.setFocusPainted(false);
		 anySizeBtn.setFocusPainted(false);
		secondWholeScreenBtn.setFocusPainted(false);
		secondAnySizeBtn.setFocusPainted(false);

		 wholeScreenBtn.setBackground(Color.WHITE);
		 anySizeBtn.setBackground(Color.WHITE);
		secondWholeScreenBtn.setBackground(Color.WHITE);
		secondAnySizeBtn.setBackground(Color.WHITE);

		 jp.add(wholeScreenBtn);
		 jp.add(anySizeBtn);
		 jp.add(secondWholeScreenBtn);
		 jp.add(secondAnySizeBtn);
		 jf.add(jp);
		 jf.setLocationRelativeTo(null);
		 jf.setSize(420,80);
		 // 创建图标对象
	   	 Image icon =  new ImageIcon(Objects.requireNonNull(this.getClass().getResource("image/screenshot.png"))).getImage();
		 jf.setIconImage(icon);
		 jf.setTitle("截图");
		 jf.setVisible(true);
		 jf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		 jf.setResizable(false);
		 // 添加监听事件
		 wholeScreenBtn.addMouseListener(this);
		 anySizeBtn.addMouseListener(this);
		secondWholeScreenBtn.addMouseListener(this);
		secondAnySizeBtn.addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e) {
		/*
		 *响应全屏截图事件
		 */
		if(e.getSource()== wholeScreenBtn){
			wholeScreenBtnClickRun();
		}
		
		/*
		 *响应矩形截图事件
		 */
		if(e.getSource()== anySizeBtn) {
			// anySizeStart(null);
			// BufferedImage bufferedImage = wholeScreenSaveRun();
			startFocusWake(null).execute();
		}
		/*
		 *响应倒计时全屏截图事件
		 */
		if(e.getSource()== secondWholeScreenBtn) {
			Thread appThread = new Thread(() -> {
				try {
					SwingUtilities.invokeAndWait(secondAnySizeThreadRunM);
				} catch (InterruptedException | InvocationTargetException ex) {
					throw new RuntimeException(ex);
				}
				// 上述线程执行完毕后，自动回到调用线程继续执行
				wholeScreenBtnClickRun();
			});
			appThread.start();
		}

		/*
		 *响应倒计时矩形截图事件
		 */
		if(e.getSource()== secondAnySizeBtn) {
			Thread appThread = new Thread(() -> {
				try {
					SwingUtilities.invokeAndWait(secondAnySizeThreadRunM);
				} catch (InterruptedException | InvocationTargetException ex) {
					throw new RuntimeException(ex);
				}
				// 上述线程执行完毕后，自动回到调用线程继续执行
				startFocusWake(null).execute();
			});
			appThread.start();
		}
  }

	/**
	 * 当全屏截图按钮（包括倒计时全屏截图）被点击时，响应全屏截图事件
	 */
	private void wholeScreenBtnClickRun() {
		BufferedImage globalBufferedImage = wholeScreenSaveRun();
		try {
			Common.saveShot(globalBufferedImage, jf);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		// 显示主窗口
		jf.setVisible(true);
	}

	/**
	 * 开始进行矩形截图
	 * @throws InterruptedException
	 */
	private static void anySizeStart(BufferedImage globalBufferedImage) throws InterruptedException {
		// 隐藏入口主窗口
		jf.setVisible(false);
		Thread.sleep(1000);
		// 创建矩形截图实例
		new RectangleShot(globalBufferedImage);
	}

	/**
	 * 截全屏开始
	 *
	 * @return
	 */
	private BufferedImage wholeScreenSaveRun() {
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
			Rectangle rc = new Rectangle(0, 0, (int)dm.getWidth(), (int)dm.getHeight());
			// 调用机器人, 获取完整窗口像素大小
			MultiResolutionImage multiResolutionScreenCapture = rb.createMultiResolutionScreenCapture(rc);
			List<Image> resolutionVariants = multiResolutionScreenCapture.getResolutionVariants();
			Image bufferedImage;
			/*
				// 使用multiResolutionScreenCapture获取屏幕截图，这是jdk11的新方法，将生成1个或多个结果，
				// 相比createScreenCapture，它考虑了设备缩放的情况。若设备没有缩放，通常返回一个结果。若有缩放（笔记本常见），
				// 通常返回大于一个的结果。
			 */
			if (resolutionVariants.size() > 1) {
				// 存在缩放情况，返回高分辨率(设备正常分辨率)的图像结果，而不是获取第一个低分辨率（系统按比例调整，非设置正常分辨率）的模糊图像
				bufferedImage = resolutionVariants.get(1);
			} else {
				// 不存在缩放情况，直接返回图像结果
				bufferedImage = resolutionVariants.get(0);
			}
			return (BufferedImage) bufferedImage;
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 倒计时截图所用到的线程函数
	 * @throws InterruptedException
	 */
	final Runnable secondAnySizeThreadRunM = () -> {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	};

	private SwingWorker<Void, BufferedImage> startFocusWake(BufferedImage bufferedImage) {
		SwingWorker<Void, BufferedImage> wake = new SwingWorker<Void, BufferedImage>() {
			@Override
			protected Void doInBackground() throws Exception {
				anySizeStart(bufferedImage);
				return null;
			}
		};
		return wake;
	}

	/*
     * 未实现的方法
     */
	public void mousePressed(MouseEvent e1) {}
	public void mouseReleased(MouseEvent e2) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
