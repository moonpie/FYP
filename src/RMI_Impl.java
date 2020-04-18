
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.util.Random;

/**
 *
 * @author Eoin Mooney - 0648396
 */
public class RMI_Impl
        extends java.rmi.server.UnicastRemoteObject
        implements RMI
{
    BigInteger g, p, A;
    int b, level = -1;
    
    /**
     * 
     * @throws java.rmi.RemoteException
     */
    public RMI_Impl()
            throws java.rmi.RemoteException
    {
        super();
    }

    public BigInteger generateKey_Client(BigInteger g, BigInteger p, BigInteger A, int level)
            throws java.rmi.RemoteException
    {
        Random r = new Random();
        b = r.nextInt(10000);

        System.out.println("I'm running now!");

        BigInteger B = (g.pow(b)).mod(p);

        this.g = g;
        this.p = p;
        this.A = A;
        this.level = level;

        return B;
    }

    public boolean checkKeyReady_Server()
            throws java.rmi.RemoteException
    {
        if(level == -1)
            return false;
        else
            return true;
    }

    public BigInteger[] getKey_Server()
            throws java.rmi.RemoteException
    {
        BigInteger ret[] = new BigInteger[5];

        ret[0] = g;
        ret[1] = p;
        ret[2] = A;
        ret[3] = BigInteger.valueOf(b);
        ret[4] = BigInteger.valueOf(level);

        return ret;
    }
}
