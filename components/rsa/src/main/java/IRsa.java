import java.io.File;
import java.io.FileNotFoundException;

public interface IRsa {
    String version();

    String encrypt(String plainMessage, File publicKeyfile) throws FileNotFoundException;

    String decrypt(String encryptedMessage, File privateKeyfile) throws FileNotFoundException;

    void enableDebuggingMode();
}