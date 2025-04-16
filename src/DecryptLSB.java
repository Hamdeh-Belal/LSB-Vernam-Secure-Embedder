
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import javafx.scene.control.Alert;

public class DecryptLSB {
	
	private long seed;
	
	public DecryptLSB() {
	}

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/*
	 * extract the hidden message from the image using zigzag distribution first
	 * will extract the length of the secret message second after encountering
	 * "0000000100000001" bits will stop extracting length of the secret message and
	 * start extracting the secret message itself third extract secret message
	 * 
	 * @params (Mat image)
	 * 
	 * @return String the secret message
	 * 
	 */
	public String extractLSB(Mat stegoImage) throws NoSuchAlgorithmException {
		ZigZagTraversal zigZagTraversal = new ZigZagTraversal();
		List<Mat> channels = new ArrayList<>();
		Core.split(stegoImage, channels);
		Mat blueChannel = channels.get(0);

		int row = 0;
		int column = 0;
		boolean timeTomoveToNextCol = true;
		int digonalMove = -1;
		double[] pixel;
		int pixelValue;

		int messageLength = 0;
		StringBuilder bits = new StringBuilder();
		int counter = 0;
		String previous7Bits;
		int i = 0;
		// this loop will extract the length of the secret message
		while (true) {
			if (counter == 2) {
				break;
			}
			pixel = blueChannel.get(row, column);
			pixelValue = (int) pixel[0];
			int lsb = pixelValue & 1;
			bits.append(lsb);
			if (lsb == 1 && bits.length() > 8) {
				previous7Bits = bits.substring(bits.length() - 8, bits.length() - 1);
				if (previous7Bits.equals("0000000")) {
					counter++;
					if(counter == 1) {
						bits.delete(bits.length() - 8, bits.length());
						try {
							messageLength = Integer.parseInt(bits.toString(), 2);
						}catch(NumberFormatException e) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
                			alert.setContentText("This image does not appear to contain a secret message.");
                			alert.showAndWait();
                			return null;
						}
						bits.setLength(0);
					}else {
						bits.delete(bits.length() - 8, bits.length());
						seed = Long.parseLong(bits.toString(), 2);
						bits.setLength(0);
					}
				}
			}
			List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
					timeTomoveToNextCol, stegoImage.height(), stegoImage.width());
			row = (int) newValuesAfterMove.get(0);
			column = (int) newValuesAfterMove.get(1);
			digonalMove = (int) newValuesAfterMove.get(2);
			timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
			i++;
		}
		StringBuilder binaryRepresentationOfChar;

		byte[] cipherText = new byte[messageLength];
		int cIndex = 0;
		// those two loops will extract the secret message
		while (i < ( stegoImage.width() * stegoImage.height() ) && cIndex < messageLength) {
			binaryRepresentationOfChar = new StringBuilder();
			int j = 0;
			while (j < 8 && i < ( stegoImage.width() * stegoImage.height() )) {
				pixel = blueChannel.get(row, column);
				pixelValue = (int) pixel[0];

				int lsb = pixelValue & 1;
				binaryRepresentationOfChar.append(lsb);

				List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
						timeTomoveToNextCol, stegoImage.height(), stegoImage.width());
				row = (int) newValuesAfterMove.get(0);
				column = (int) newValuesAfterMove.get(1);
				digonalMove = (int) newValuesAfterMove.get(2);
				timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
				i++;
				j++;
			}
			if(binaryRepresentationOfChar.length() == 0) {
				System.out.println("Empty");
			}else {
				int byteValue = Integer.parseInt(binaryRepresentationOfChar.toString(), 2);
				cipherText[cIndex] = (byte) byteValue;
				cIndex++;
			}
		}
		VernamCipherDecryption cipherDecryption = new VernamCipherDecryption(seed);
		Core.merge(channels, stegoImage);
		if (cIndex < messageLength) {
			return extractLSBGreen(stegoImage, cipherText, cIndex, messageLength);
		} else {
			return cipherDecryption.one_time_pad_decr(cipherText);
		}
	}

	private String extractLSBGreen(Mat stegoImage, byte[] cipherTextCont, int cIndex, int messageLength)
			throws NoSuchAlgorithmException {
		ZigZagTraversal zigZagTraversal = new ZigZagTraversal();
		List<Mat> channels = new ArrayList<>();
		Core.split(stegoImage, channels);
		Mat greenChannel = channels.get(1);

		StringBuilder binaryRepresentationOfChar;

		int row = 0;
		int column = 0;
		boolean timeTomoveToNextCol = true;
		int digonalMove = -1;
		double[] pixel;
		int pixelValue;

		int i = 0;
		// those two loops will extract the secret message
		while (i < ( stegoImage.width() * stegoImage.height() ) && cIndex < messageLength) {
			binaryRepresentationOfChar = new StringBuilder();
			int j = 0;
			while (j < 8 && i < ( stegoImage.width() * stegoImage.height() )) {
				pixel = greenChannel.get(row, column);
				pixelValue = (int) pixel[0];

				int lsb = pixelValue & 1;
				binaryRepresentationOfChar.append(lsb);

				List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
						timeTomoveToNextCol, stegoImage.height(), stegoImage.width());
				row = (int) newValuesAfterMove.get(0);
				column = (int) newValuesAfterMove.get(1);
				digonalMove = (int) newValuesAfterMove.get(2);
				timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
				i++;
				j++;
			}
			if(binaryRepresentationOfChar.length() == 0) {
				
			}else {
				int byteValue = Integer.parseInt(binaryRepresentationOfChar.toString(), 2);
				cipherTextCont[cIndex] = (byte) byteValue;
				cIndex++;
			}
		}
		VernamCipherDecryption cipherDecryption = new VernamCipherDecryption(seed);
		Core.merge(channels, stegoImage);
		if (cIndex < messageLength) {
			return extractLSBRed(stegoImage, cipherTextCont, cIndex, messageLength);
		} else {
			return cipherDecryption.one_time_pad_decr(cipherTextCont);
		}
	}

	private String extractLSBRed(Mat stegoImage, byte[] cipherTextCont, int cIndex, int messageLength)
			throws NoSuchAlgorithmException {
		ZigZagTraversal zigZagTraversal = new ZigZagTraversal();
		List<Mat> channels = new ArrayList<>();
		Core.split(stegoImage, channels);
		Mat redChannel = channels.get(2);

		StringBuilder binaryRepresentationOfChar;

		int row = 0;
		int column = 0;
		boolean timeTomoveToNextCol = true;
		int digonalMove = -1;
		double[] pixel;
		int pixelValue;

		int i = 0;
		// those two loops will extract the secret message
		while (i < ( stegoImage.width() * stegoImage.height() ) && cIndex < messageLength) {
			binaryRepresentationOfChar = new StringBuilder();
			int j = 0;
			while (j < 8 && i < ( stegoImage.width() * stegoImage.height() )) {
				pixel = redChannel.get(row, column);
				pixelValue = (int) pixel[0];

				int lsb = pixelValue & 1;
				binaryRepresentationOfChar.append(lsb);

				List<Object> newValuesAfterMove = zigZagTraversal.zigZagTraversal(row, column, digonalMove,
						timeTomoveToNextCol, stegoImage.height(), stegoImage.width());
				row = (int) newValuesAfterMove.get(0);
				column = (int) newValuesAfterMove.get(1);
				digonalMove = (int) newValuesAfterMove.get(2);
				timeTomoveToNextCol = (boolean) newValuesAfterMove.get(3);
				i++;
				j++;
			}
			if(binaryRepresentationOfChar.length() == 0) {
				
			}else {
				int byteValue = Integer.parseInt(binaryRepresentationOfChar.toString(), 2);
				cipherTextCont[cIndex] = (byte) byteValue;
				cIndex++;
			}
		}
		VernamCipherDecryption cipherDecryption = new VernamCipherDecryption(seed);
		Core.merge(channels, stegoImage);
		return cipherDecryption.one_time_pad_decr(cipherTextCont);
	}

}
