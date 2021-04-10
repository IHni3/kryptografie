import java.io.File;

public interface IShift {
    String version();

    String encrypt(String plainMessage, File keyfile);

    String decrypt(String encryptedMessage, File keyfile);

    void enableDebuggingMode();
}