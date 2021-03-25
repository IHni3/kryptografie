import java.io.File;
import java.io.FileNotFoundException;

public interface IRsaCracker {
    String version();

    String decrypt(String encryptedMessage, File publicKeyfile) throws FileNotFoundException;
}