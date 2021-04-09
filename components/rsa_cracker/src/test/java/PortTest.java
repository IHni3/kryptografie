import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class PortTest {
    @Test
    public void portTest(){
        String encrypted = "QbEoOdE=";
        File keyfile = new File("/home/liam/Desktop/rsatestkeysmall");
        try {
            System.out.println(RsaCracker.getInstance().port.decrypt(encrypted, keyfile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
