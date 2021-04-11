import java.math.BigInteger;
public class Sieve {
    private static Sieve instance = new Sieve();
    public Port port;

    public Sieve(){
        port = new Port();
    }

    public static Sieve getInstance() {
        return instance;
    }

    private BigInteger sieve(BigInteger n) {
        QuadraticThieve qt = new QuadraticThieve(n);
        qt.start();
        while (qt.getGcd() == null){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return qt.getGcd();
    }

    public class Port {
        public String version() {
            return null;
        }

        public String sieve(BigInteger n){
            var sieved = Sieve.this.sieve(n);
            if (sieved.equals(-1)){
                return null;
            } else {
                return sieved.toString();
            }
        }
    }
}