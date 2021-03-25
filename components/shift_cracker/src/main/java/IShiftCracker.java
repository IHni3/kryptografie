public interface IShiftCracker {
    String version();

    String decrypt(String encryptedMessage);
}