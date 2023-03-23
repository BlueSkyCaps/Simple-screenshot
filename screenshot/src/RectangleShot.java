import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.MultiResolutionImage;
import java.io.IOException;
import java.util.List;

public class RectangleShot extends JWindow implements MouseMotionListener{

	private final JWindow jw = new JWindow();
	Graphics g;
	private  Robot robot;
	private BufferedImage _bufferIconImage, _saveImage;
	private int originX,originY,releaseX,releaseY,endX,endY;
	private int clearW,clearH;
	private final JLabel jl = new JLabel();

	public RectangleShot (BufferedImage bufferedImage) throws InterruptedException {
		jLabelMouseEventSet();
		jl.addMouseMotionListener(this);
		jw.setFocusable(true);
	    jw.add(jl);


	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();


		robot = null;
		try {
			// 创建机器人实例
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		if (bufferedImage != null){
			// 有传入的缓冲区截图则使用
			/* !!! 矩形、倒计时矩形都没有传入的缓冲区截图进行截图，所以此条件永远不会执行，转到else */
			_bufferIconImage = bufferedImage;
		}else {
			// 没有则获取全屏幕像素

			/*
			// !!! 目前在缩放了的设备中进行矩形截图将会模糊，因为它的像素被等比例缩小了。虽然createMultiResolutionScreenCapture可以返回
			// 高分辨率(设备正常分辨率)的图像结果，但是需要将此结果设置给jWindow置顶显示以在此基础上拖拽截图。而此图像结果已经大于设备缩放
			// 了的设备像素大小，而jWindow组件目前无法全屏设置为图像结果的高分辨率像素，所以导致图像被放大显示。
			// 默认resolutionVariants.get(0)都获取第一个低分辨率（系统按比例调整，非设置正常分辨率）的模糊图像
			// 注意：在未设置缩放的设备中，不存在上述问题，resolutionVariants.get(0)就是原正常分辨率图像
			*/
			MultiResolutionImage multiResolutionScreenCapture = robot.createMultiResolutionScreenCapture(new Rectangle(0, 0, d.width,d.height));
			List<Image> resolutionVariants = multiResolutionScreenCapture.getResolutionVariants();
			_bufferIconImage = (BufferedImage) resolutionVariants.get(0);
			jw.setBounds(0, 0, _bufferIconImage.getWidth(), _bufferIconImage.getHeight());

		}
		Thread.sleep(1000);
	    jl.setIcon(new ImageIcon(_bufferIconImage));
		//
		jw.setVisible(true);
		/*
		* 显示后 设置窗口前置 可以开始拖拽
		* 避免倒计时准备过程中程序窗口变成非活动窗口丢失焦点导致程序无法响应拖拽
		*  */
		jw.setAlwaysOnTop(true);
	}

	/**
	 * 给jLabel设置监听响应事件处理逻辑<br><br/>
	 * JLabel被jWindow包裹，jLabel用icon显示截图，并且响应具体的鼠标点击事件<br><br/>
	 * 而鼠标运动(拖拽)事件则由jWindow监听，因为此类继承jWindow实现MouseMotionListener
	 */
	private void jLabelMouseEventSet() {
		jl.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				//重新截图时移除上次的jl,防止矩形框顶层覆盖
				jw.remove(jl);
				jl.setIcon(new ImageIcon(_bufferIconImage));
				jw.add(jl);  // 再次载入最新截图
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

				g.drawRect(originX-1, originY-1, releaseX-originX, releaseY-originY);
				// 获取矩形宽高
				clearW = releaseX-originX;
				clearH = releaseY-originY;
				/*
					// 此处最好判断是否缩放了分辨率，因为是在临时全屏置顶图片上再次截图。如果不使用高分辨率(设备正常分辨率)的图像结果，
					// 则缩放了分辨率的设备图像结果将再次模糊。
				*  */
				MultiResolutionImage multiResolutionScreenCapture = robot.createMultiResolutionScreenCapture(new Rectangle(
					originX,originY, clearW-1,clearH-1));
				List<Image> resolutionVariants = multiResolutionScreenCapture.getResolutionVariants();
				/*
					// 使用multiResolutionScreenCapture获取屏幕截图，这是jdk11的新方法，将生成1个或多个结果，
					// 相比createScreenCapture，它考虑了设备缩放的情况。若设备没有缩放，通常返回一个结果。若有缩放（笔记本常见），
					// 通常返回大于一个的结果。
				 */
				if (resolutionVariants.size() > 1) {
					// 存在缩放情况，返回高分辨率(设备正常分辨率)的图像结果，而不是获取第一个低分辨率（系统按比例调整，非设置正常分辨率）的模糊图像
					_saveImage = (BufferedImage) resolutionVariants.get(1);
				} else {
					// 不存在缩放情况，直接返回图像结果
					_saveImage = (BufferedImage) resolutionVariants.get(0);
				}
				// 取消临时的窗口前置设置
				jw.setAlwaysOnTop(false);

				int option = JOptionPane.showConfirmDialog(null, ""
					+ "是否保存截图？", null, 0);
				if(option==1) {
					// 重新截图
					jw.setVisible(true);
					mousePressed(e);
				}
				else if(option==0){
					// 返回最终像素
					try {
						// 保存截图
						Common.saveShot(_saveImage, jw);
						// 显示主窗口
						jw.dispose();
						ScreenShot.jf.setVisible(true);
					} catch (IOException e1) {
						System.out.println("method: saveShot() can't call");
						e1.printStackTrace();
					}
				}
				else {
					jw.dispose();
					new ScreenShot();
				}
			}
		});
	}

	/*
	 * 响应鼠标拖拽事件
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		endX = e.getX();
		endY = e.getY();
		// 返回标签组件当前的绘制对象
		g = jl.getGraphics();
		g.setColor(Color.red);
		// System.out.println("拖拽");
	}


	@Override
	public void mouseMoved(MouseEvent e){
		/**
		 * empty
		 */
	}
}
