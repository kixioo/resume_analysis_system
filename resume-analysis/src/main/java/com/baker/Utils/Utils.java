package com.baker.Utils;

import com.baker.pojo.TwoParamObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.baker.Service.Impl.ResumeServiceImpl.*;

public class Utils {
        private static final int MAX_WIDTH = 100;  // 设置最大宽度
        private static final int MAX_HEIGHT = 100;  // 设置最大高度

        public static byte[] compressAndResize(MultipartFile imageFile) throws IOException {
            // 读取原始图片
            BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());

            // 计算新的尺寸
            int originalWidth = originalImage.getWidth();
            int originalHeight = originalImage.getHeight();
            int newWidth = originalWidth;
            int newHeight = originalHeight;
            if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
                // 根据最大宽度和最大高度等比例缩放图片
                double scaleFactor = Math.min(1.0 * MAX_WIDTH / originalWidth, 1.0 * MAX_HEIGHT / originalHeight);
                newWidth = (int) (originalWidth * scaleFactor);
                newHeight = (int) (originalHeight * scaleFactor);
            }

            // 创建新的缩放后的图片
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
            resizedImage.getGraphics().drawImage(scaledImage, 0, 0, null);

            // 将缩放后的图片转换为字节数组
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(resizedImage, "jpeg", outputStream);
            byte[] compressedImageData = outputStream.toByteArray();

            // 关闭流
            IOUtils.closeQuietly(outputStream);

            return compressedImageData;
        }


}
