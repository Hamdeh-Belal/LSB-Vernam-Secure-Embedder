
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class VernamCipherEncryption {
	
	private long seed;
	
	public VernamCipherEncryption() {
	}

	public byte[] keyGenerator(int length) throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		seed = System.currentTimeMillis() % 1000;
		random.setSeed(seed); // Setting the seed for reproducibility
		byte[] key = new byte[length];
		random.nextBytes(key); // Generate random bytes
		return key;
	}

	public byte[] one_time_pad_encr(String plainText) throws NoSuchAlgorithmException {
		byte[] plainTextByteArray = plainText.getBytes(StandardCharsets.UTF_8);

		byte[] key = keyGenerator(plainTextByteArray.length);

		byte[] cipherTextAsByte = new byte[plainTextByteArray.length];

		for (int i = 0; i < plainTextByteArray.length; i++) {
			cipherTextAsByte[i] = (byte) (plainTextByteArray[i] ^ key[i]);
		}

		return cipherTextAsByte;
	}

	public long getSeed() {
		return seed;
	}
	
}
