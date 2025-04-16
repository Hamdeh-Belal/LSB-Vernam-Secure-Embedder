
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class VernamCipherDecryption {
	
	private long seed;
	
	public VernamCipherDecryption(long seed) {
		this.seed = seed;
	}

	public byte[] keyGenerator(int length) throws NoSuchAlgorithmException {
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(seed); // Setting the seed for reproducibility
		byte[] key = new byte[length];
		random.nextBytes(key); // Generate random bytes
		return key;
	}

	public String one_time_pad_decr(byte[] cipherText) throws NoSuchAlgorithmException {

		byte[] key = keyGenerator(cipherText.length);

		byte[] plaintTextAsByte = new byte[cipherText.length];

		for (int i = 0; i < cipherText.length; i++) {
			plaintTextAsByte[i] = (byte) (cipherText[i] ^ key[i]);
		}

		String plainTextAsString = new String(plaintTextAsByte, StandardCharsets.UTF_8);

		return plainTextAsString;

	}

}
