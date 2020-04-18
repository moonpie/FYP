
import cipher.RC5;
import finalyearproject.FYP;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.spec.DHParameterSpec;
import java.util.Random;
import java.util.Scanner;
/**
 *
 * @author Eoin Mooney - 0648396
 */
public class RMI_Client
{
    public RMI_Client()
    {
        
    }


    /**
     * 
     * @param sizeBytes
     * @return
     */
    // Returns an array of 3 values.
    // The first number is the prime modulus P.
    // The second number is the base generator G.
    // The third number is bit size of the random exponent L.
    public static BigInteger[] genDhParams(int sizeBytes)
    {
        try
        {
            // Create the parameter generator for a 1024-bit DH key pair
            AlgorithmParameterGenerator paramGen = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(8 * sizeBytes);
            System.out.println("Parameter Generator Generated");
            // Generate the parameters
            AlgorithmParameters params = paramGen.generateParameters();
            DHParameterSpec dhSpec
                = (DHParameterSpec)params.getParameterSpec(DHParameterSpec.class);
            System.out.println("Parameters Generated");

            // Return the three values in an array
            BigInteger ret[] = new BigInteger[3];
            ret[0] = dhSpec.getP(); 
            ret[1] = dhSpec.getG(); 
            ret[2] = BigInteger.valueOf(dhSpec.getL());
            return ret;
        }
        catch (NoSuchAlgorithmException e)
        {
        }
        catch (InvalidParameterSpecException e)
        {
        }
        return null;
    }

    /**
     * 
     */
    static void printMenu(FYP proj)
    {
        boolean quit = false, valid = false;
        Scanner input = new Scanner(System.in);
        String choice = new String("");

        /*
         */

        do
        {
            System.out.println("----------------------------------------");
            System.out.println("Main Menu:\n"
                    + "1) Login to SFTP\n"
                    + "2) Quit\n");
            System.out.print("Please enter a choice: ");

            choice = input.next();
            switch (choice.charAt(0))
            {
                case '1' :
                    callCommand(proj);
                    break;
                case '2' :
                    break;
                default :
                    System.out.println("\nInvalid Selection: "
                            + choice.charAt(0)
                            + "\nPlease try again.\n");
            }
            do
            {
                System.out.print("\nAre you sure? (y/n) ");

                choice = input.next();
                switch (choice.charAt(0))
                {
                    case ('y' | 'Y') :
                        valid = true;
                        quit = true;
                        break;
                    case ('n' | 'N') :
                        valid = true;
                        quit = false;
                        break;
                    default :
                        valid = false;
                        System.out.println("\nInvalid Input: "
                                + choice.charAt(0)
                                + "\nPlease try again.");
                }
            }
            while (!valid);
        }
        while (!quit);
    }


    static void callCommand(FYP proj)
    {
        //pass through stdin and stdout
        //catch put and get keywords
        //encrypt and decrypt accordingly
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        BigInteger g, p, A, B, K;
        int a;
        byte k[] = null;

        if(args.length < 1 || args.length > 1)
        {
            System.out.println("Please define a Security Level (0-9) or -h for help");
        }
        else if(args[0].equals("-h"))
        {
            System.out.println("Level\tRounds\tKeySize(Bytes)");
            for (int i = 0; i <= 9; i++)
                System.out.println(i + "\t" + RC5.Security[0][i] + "\t" + RC5.Security[1][i]);
        }
        else
        {
            try
            {
                if (Integer.parseInt(args[0]) >= 0 || Integer.parseInt(args[0]) <= 9)
                {
                    System.out.println("Nice Choice!");
                    BigInteger[] Params = genDhParams(RC5.Security[1][Integer.parseInt(args[0])]);
                    p = Params[1];
                    g = Params[0];

                    Random r = new Random();
                    a = r.nextInt(10000);

                    System.out.println("a = " + a);

                    A = (g.pow(a)).mod(p);

                    System.out.println("Connecting ...");

                    try
                    {
                        RMI fyp = (RMI)
                                Naming.lookup("rmi://localhost/FYP_RMI_Service");

                        System.out.println("Connected");

                        B = fyp.generateKey_Client(g, p, A, Integer.parseInt(args[0]));

                        System.out.println("Successful sharing!");

                        K = (B.pow(a)).mod(p);
                        k = K.toByteArray();

                        System.out.println("Key Generated");

                        System.out.println("g: " + g + "\n"
                                + "p: " + p + "\n"
                                + "A: " + A + "\n"
                                + "B: " + B + "\n"
                                + "K: " + K + "\n"
                                + "size: " + k.length);

                        FYP client = new FYP();

                        client.setSecurityLevel(Integer.parseInt(args[0]));
                        client.setSecretKeyBytes(k);

                        printMenu(client);
                        
                    }
                    catch (MalformedURLException murle)
                    {
                        System.out.println();
                        System.out.println("MalformedURLException");
                        System.out.println(murle);
                    }
                    catch (RemoteException re)
                    {
                        System.out.println();
                        System.out.println("RemoteException");
                        System.out.println(re);
                    }
                    catch (NotBoundException nbe)
                    {
                        System.out.println();
                        System.out.println("NotBoundException");
                        System.out.println(nbe);
                    }
                }
                else
                {
                    System.out.println("Please define a Security Level (0-9) or -h for help");
                }
            }
            catch (NumberFormatException nfe)
            {
                System.out.println("Please define a Security Level (0-9) or -h for help");
            }
        }
    }
}
