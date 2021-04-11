
import java.text.DecimalFormat;

public class ShiftCracker {
    private static final ShiftCracker instance = new ShiftCracker();
    public Port port;

    private int key;

    private static final DecimalFormat decimalFormat = new DecimalFormat("#0.00000");

    private ShiftCracker() {
        port = new Port();
    }

    public static ShiftCracker getInstance() {
        return instance;
    }

    private String decrypt(String encryptedMessage) {

        if (encryptedMessage.equals("")) {
            System.exit(0);
        }

        String source = encryptedMessage.trim().toUpperCase();

        char[] sourceText = new char[source.length()];
        int[] unicode = new int[source.length()];
        int[] unicodeCopy = new int[source.length()];

        for (int count = 0; count < source.length(); count++) {
            sourceText[count] = source.charAt(count);
        }

        String hex;
        int dec;

        for (int count = 0; count < sourceText.length; count++) {
            hex = Integer.toHexString(sourceText[count]);
            dec = Integer.parseInt(hex, 16);
            unicode[count] = dec;
            unicodeCopy[count] = dec;
        }

        StringBuilder builder = new StringBuilder();

        for (int shift = 1; shift <= 25; shift++) {
            String smartShifted = smartShift(shift, unicode, unicodeCopy);
            if (smartShifted != null){
                builder.append(smartShifted);
                builder.append("\n");
            }

        }

        return builder.toString();
    }

    public class Port {
        public String decrypt(String encryptedMessage) {
            return ShiftCracker.this.decrypt(encryptedMessage);
        }
    }


    private static String smartShift(int shift, int[] unicode, int[] unicodeCopy) {
        for (int x = 0; x <= unicode.length - 1; x++) {
            unicodeCopy[x] = unicode[x];

            if (unicode[x] >= 65 && unicode[x] <= 90) {
                unicodeCopy[x] += shift;
                if (unicodeCopy[x] > 90) {
                    unicodeCopy[x] -= 26;
                }
            }
        }

        String[] processed = new String[unicode.length];
        char[] finalProcess = new char[unicode.length];

        for (int count = 0; count < processed.length; count++) {
            processed[count] = Integer.toHexString(unicodeCopy[count]);
            int hexToInt = Integer.parseInt(processed[count], 16);
            char intToChar = (char) hexToInt;
            finalProcess[count] = intToChar;
        }

        double frequency = 0;
        double aFrequency = 0;
        double eFrequency = 0;
        double iFrequency = 0;
        double oFrequency = 0;
        double uFrequency = 0;

        for (char c : finalProcess) {
            frequency++;

            switch (c) {
                case 'A':
                    aFrequency++;
                    break;
                case 'E':
                    eFrequency++;
                    break;
                case 'I':
                    iFrequency++;
                    break;
                case 'O':
                    oFrequency++;
                    break;
                case 'U':
                    uFrequency++;
                    break;
                default:
                    break;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (char character : finalProcess) {
            stringBuilder.append(character);
        }

        if (eFrequency / frequency >= 0.05 || aFrequency / frequency >= 0.05 || iFrequency / frequency >= 0.05 || oFrequency / frequency >= 0.05 || uFrequency / frequency >= 0.05) {
            return (stringBuilder.toString());
        }
        return null;
    }
}