import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author BlueSky
 * @date 2023/3/22
 */
public class Common {
    /**
     * 通用保存图片到本地
     *
     * @param saveImage       要保存到本地的缓冲区图片
     * @param topComponentSrc 当前引起保存图片操作所来源的组件对象，如JFrame或JWindow
     * @throws IOException
     */
    public static void saveShot(BufferedImage saveImage, Window topComponentSrc) throws IOException {
        // 获取windows桌面绝对路径
        File filePath = FileSystemView.getFileSystemView().getHomeDirectory();

        SimpleDateFormat fmt = new SimpleDateFormat("MM-dd-HH-mm-ss");

        String dateFormat = fmt.format(new Date());
        System.out.println(filePath);
        // 输出图片到本地
        ImageIO.write(saveImage, "jpg", new File(filePath + File.separator + dateFormat + "catch.jpg"));
        // 输出图片到剪切板
        saveImageToClipboard(saveImage);
        showMessageDialogTopHandler(topComponentSrc, "截图已成功保存至桌面和剪切板\r\nctrl+v即可快速发图。");
    }

    /**
     * 处理保存图片后的信息弹窗显示逻辑。
     *
     * @param topComponentSrc 当前引起保存图片操作所来源的组件对象
     * @param message         文本
     */
    private static void showMessageDialogTopHandler(Window topComponentSrc, String message) {
        // 将来源组件置顶，这样之后的showMessageDialog设置了来源组件后，才能让弹窗在最顶层显示
        topComponentSrc.setAlwaysOnTop(true);
        // 获取来源组件的原大小和位置
        Dimension originSize = topComponentSrc.getSize();
        Point originLocation = topComponentSrc.getLocation();
        // 开启一个线程
        Thread appThread = new Thread(() -> {
            try {
                /*
                    // 此线程内部停0.5s，因为这个时间点调用线程已经显示弹窗。
                    // 但是因为来源组件有可能是全屏的jWindow，这个时候jWindow置顶覆盖住了后面的弹窗，
                    // 导致用户无法响应弹窗。所以此线程临时将来源组件置顶取消，并且设置大小为0
                 */
                Thread.sleep(500);
                topComponentSrc.setAlwaysOnTop(false);
                topComponentSrc.setSize(0, 0);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        appThread.start();
        // 主线程显示了弹窗并且阻塞等待用户响应
        JOptionPane.showMessageDialog(topComponentSrc, message);
        // 弹窗响应完毕，恢复来源组件的原大小和位置
        topComponentSrc.setSize(originSize);
        topComponentSrc.setLocation(originLocation);
    }

    /**
     * 使用数据传输TransferData，将图片源存储到系统剪切板中。
     * @param image 要存储的图片源，通常是缓冲区图像-BufferedImage
     */
    public static void saveImageToClipboard(final Image image) {

        Transferable trans = new Transferable() {
            @Override
            public Object getTransferData(DataFlavor flavor) {
                if (isDataFlavorSupported(flavor)) {
                    return image;
                }
                try {
                    throw new UnsupportedFlavorException(flavor);
                } catch (UnsupportedFlavorException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{DataFlavor.imageFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return DataFlavor.imageFlavor.equals(flavor);
            }
        };
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(trans, null);
    }
}
