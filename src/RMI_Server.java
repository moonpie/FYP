
import java.rmi.Naming;
import finalyearproject.FYP;
import java.math.BigInteger;

/**
 *
 * @author Eoin Mooney - 0648396
 */
public class RMI_Server
{
    /**
     *
     */
    public RMI_Server()
    {
        try
        {
            RMI fyp = new RMI_Impl();
            Naming.rebind("rmi://localhost:1099/FYP_RMI_Service", fyp);

            System.out.println("Server Up ...");

            BigInteger g, p, A, K;
            int b, level;
            byte k[] = null;
            boolean ready = false;

            while((ready = fyp.checkKeyReady_Server()) != true)
                for(int i = 0; i < 1000; i++)
                    ready = false;

            BigInteger params[] = fyp.getKey_Server();
            g = params[0];
            p = params[1];
            A = params[2];
            b = params[3].intValue();
            level = params[4].intValue();

            System.out.println("I have the parameters");

            BigInteger B = (g.pow(b).mod(p));

            K = (A.pow(b)).mod(p);
            k = K.toByteArray();

            System.out.println("g: " + g + "\n"
                    + "p: " + p + "\n"
                    + "A: " + A + "\n"
                    + "B: " + B + "\n"
                    + "K: " + K + "\n"
                    + "size: " + k.length);

            FYP server = new FYP();

            server.setSecurityLevel(level);
            server.setSecretKeyBytes(k);

            System.out.println("... Ready");

        }
        catch (Exception e)
        {
            System.out.println("Trouble: " + e);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        new RMI_Server();
        // TODO code application logic here
    }
}
