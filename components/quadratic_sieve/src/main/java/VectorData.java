import java.math.BigInteger;
import java.util.BitSet;

public class VectorData {
    public final BitSet vector;
    public final long position;
    public int bigPrimeIndex = -1;

    public BigInteger x;
    public BigInteger y;

    VectorData(BitSet vector, long position) {
        this.vector = vector;
        this.position = position;
    }

    public String toString() {
        return vector.toString();
    }

    public int hashCode() {
        return vector.toString().hashCode();
    }

    public boolean equals(Object obj) {
        return obj instanceof VectorData && vector.toString().equals(((VectorData) obj).vector.toString());
    }
}