package com.pier.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public final class MatrixToImageWriter {

	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private MatrixToImageWriter() {
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	private static void writeToStream(BitMatrix matrix, String format, HttpServletResponse resp) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		// byte[] b =
		// ((DataBufferByte)image.getData().getDataBuffer()).getData();
		// System.out.println("write to stream");
		// System.out.println(b);
		// boolean flag=false;
		try {
			ImageIO.write(image, format, resp.getOutputStream());
		}
		catch (Exception e) {
			System.out.println("format is not suit");
			// throw new IOException("Could not write an image of format " +
			// format);
		}
	}

	public static void WriteImageStream(String url, int height, int width, HttpServletResponse resp)
			throws IOException {
		Map<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 1);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "jpg", resp);
		}
		catch (WriterException e) {
			e.printStackTrace();
		}
	}

}
