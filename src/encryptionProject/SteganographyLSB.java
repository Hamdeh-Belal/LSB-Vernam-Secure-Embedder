package encryptionProject;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class SteganographyLSB {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public SteganographyLSB() {
	}
	
	public Mat LSB(Mat coverImage, byte[] cipherMessage, long seed) {
		ZigZagTraversal zigZagTraversal = new ZigZagTraversal();
		List<Mat> channels = new ArrayList<>();
		Core.split(coverImage, channels);
		Mat blueChannel = channels.get(0);
		
		String sizeBinary = Integer.toBinaryString(cipherMessage.length);
		String seedBinary = Long.toBinaryString(seed);
		String sizeAndSeedAndOpcode = sizeBinary + "00000001" + seedBinary + "00000001";
		int row = 0;
		int column = 0;
		boolean timeTomoveToNextCol = true;
		int digonalMove = -1;
		double[] pixel;
		int pixelValue;
		String binaryRepresentationOfChar;
		int i = 0;
		// this for loop will embed the length of the secret message in bits and
		// "0000000100000001"
		while (i < (coverImage.width() * coverImage.height()) && i < sizeAndSeedAndOpcode.length()) {
			pixel = blueChannel.get(row, column);
			pixelValue = (int) pixel[0];

			if (sizeAndSeedAndOpcode.charAt(i) == '1') {
				pixelValue |= 1;
			} else {
				pixelValue &= ~1;
			}

			blueChannel.put(row, column, pixelValue);

			List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
					timeTomoveToNextCol, coverImage.height(), coverImage.width());
			row = (int) newValuesAfterMove.get(0);
			column = (int) newValuesAfterMove.get(1);
			digonalMove = (int) newValuesAfterMove.get(2);
			timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
			i++;
			
		}
		int cIndex = 0;
		// this two for loop will embed the secret message in bits
		while (i < (coverImage.width() * coverImage.height()) && cIndex < cipherMessage.length) {
			binaryRepresentationOfChar = String.format("%8s", Integer.toBinaryString(cipherMessage[cIndex] & 0xFF))
					.replace(' ', '0');
			int j = 0;
			while (j < 8 && i < (coverImage.width() * coverImage.height())) {

				pixel = blueChannel.get(row, column);
				//System.out.println("N1 " + row + " N2 " + column);
				pixelValue = (int) pixel[0];
				if (binaryRepresentationOfChar.charAt(j) == '1') {
					pixelValue |= 1;//
				} else {
					pixelValue &= ~1;
				}

				blueChannel.put(row, column, new double[] { pixelValue });
				List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
						timeTomoveToNextCol, coverImage.height(), coverImage.width());
				row = (int) newValuesAfterMove.get(0);
				column = (int) newValuesAfterMove.get(1);
				digonalMove = (int) newValuesAfterMove.get(2);
				timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
				i++;
				j++;
			}
			cIndex++;
		}
		Core.merge(channels, coverImage);
		if (cIndex < cipherMessage.length) {
			return LSBGreen(coverImage, cipherMessage, cIndex);
		} else {
			return coverImage;
		}
	}

	private Mat LSBGreen(Mat coverImage, byte[] cipherMessage, int cIndex) {
		ZigZagTraversal zigZagTraversal = new ZigZagTraversal();
		List<Mat> channels = new ArrayList<>();
		Core.split(coverImage, channels);
		Mat greenChannel = channels.get(1);

		int row = 0;
		int column = 0;
		boolean timeTomoveToNextCol = true;
		int digonalMove = -1;
		double[] pixel;
		int pixelValue;
		String binaryRepresentationOfChar;
		int i = 0;
		// this two for loop will embed the secret message in bits
		while (i < (coverImage.width() * coverImage.height()) && cIndex < cipherMessage.length) {
			binaryRepresentationOfChar = String.format("%8s", Integer.toBinaryString(cipherMessage[cIndex] & 0xFF))
					.replace(' ', '0');
			//System.out.println(binaryRepresentationOfChar);
			int j = 0;
			while (j < 8 && i < (coverImage.width() * coverImage.height())) {

				pixel = greenChannel.get(row, column);
				pixelValue = (int) pixel[0];
				if (binaryRepresentationOfChar.charAt(j) == '1') {
					pixelValue |= 1;//
				} else {
					pixelValue &= ~1;
				}

				greenChannel.put(row, column, new double[] { pixelValue });
				List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
						timeTomoveToNextCol, coverImage.height(), coverImage.width());
				row = (int) newValuesAfterMove.get(0);
				column = (int) newValuesAfterMove.get(1);
				digonalMove = (int) newValuesAfterMove.get(2);
				timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
				i++;
				j++;
			}
			cIndex++;
		}
		Core.merge(channels, coverImage);
		if (cIndex < cipherMessage.length) {
			return LSBRed(coverImage, cipherMessage, cIndex);
		} else {
			return coverImage;
		}
	}

	private Mat LSBRed(Mat coverImage, byte[] cipherMessage, int cIndex) {
		ZigZagTraversal zigZagTraversal = new ZigZagTraversal();
		List<Mat> channels = new ArrayList<>();
		Core.split(coverImage, channels);
		Mat redChannel = channels.get(2);

		int row = 0;
		int column = 0;
		boolean timeTomoveToNextCol = true;
		int digonalMove = -1;
		double[] pixel;
		int pixelValue;
		String binaryRepresentationOfChar;
		int i = 0;
		// this two for loop will embed the secret message in bits
		while (i < (coverImage.width() * coverImage.height()) && cIndex < cipherMessage.length) {
			binaryRepresentationOfChar = String.format("%8s", Integer.toBinaryString(cipherMessage[cIndex] & 0xFF))
					.replace(' ', '0');
			//System.out.println(binaryRepresentationOfChar);
			int j = 0;
			while (j < 8 && i < (coverImage.width() * coverImage.height())) {

				pixel = redChannel.get(row, column);
				pixelValue = (int) pixel[0];
				if (binaryRepresentationOfChar.charAt(j) == '1') {
					pixelValue |= 1;//
				} else {
					pixelValue &= ~1;
				}

				redChannel.put(row, column, new double[] { pixelValue });
				List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
						timeTomoveToNextCol, coverImage.height(), coverImage.width());
				row = (int) newValuesAfterMove.get(0);
				column = (int) newValuesAfterMove.get(1);
				digonalMove = (int) newValuesAfterMove.get(2);
				timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
				i++;
				j++;
			}
			cIndex++;
		}
		Core.merge(channels, coverImage);
		return coverImage;
	}

}
