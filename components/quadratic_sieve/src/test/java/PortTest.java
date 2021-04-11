import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigInteger;

public class PortTest {

    @Test
    public void portTest(){
        System.out.println(Sieve.getInstance().port.sieve(new BigInteger("378382676407")));
    }
}
