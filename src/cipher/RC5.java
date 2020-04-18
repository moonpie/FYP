
package cipher;

/**
 * RC5 class contains all the methods needed to create
 * an RC5 algorithm.
 *
 * @author Eoin Mooney - 0648396
 */
public class RC5
{
    private int w = 0;  // Word Size
    private int r = 0;  // Number of Rounds
    private int b = 0;  // Bytes in Secret Key
    private int t = 0;  // Words in Expanded Key
    private byte k[];   // Secret Key
    private int S[];    // Expanded Key
    private int A = 0;  // 1st Word of data
    private int B = 0;  // 2nd Word of data

    private int secLevel = 0;   // Index for Security[]
    public static int Security[][] = {
                                {12,16,24,36,52,72,96,124,152,255}, //Rounds
                                {64,64,72,80,88,96,104,112,120,128} //KeySize
                                };

    /**
     * Constructor
     * Creates an instance of RC5 with word size set to 32
     */
    public RC5()
    {
        w = 32;
    }

    /**
     * Constructor
     * Creates an instacne of RC5 with word size set to w
     * @param w word size in bits
     */
    public RC5(int w)
    {
        // w bit Words
        this.w = w;
    }
    
    /**
     * Sets the RC5 instance to security level: level
     * Initialises the k and S arrays
     *
     * @param level the security level to be set to
     */
    public void setLevel(int level)
    {
        int i;

        r = 0;
        b = 0;
        t = 0;
        k = null;
        S = null;

        secLevel = level;
        r = Security[0][level];
        b = Security[1][level];

        k = new byte[b];
        for(i = 0; i < b; i++)
            k[i] = 0x00;

        t = 2 * (r + 1);

        S = new int[t];
        for(i = 0; i < t; i++)
            S[i] = 0;
    }

    /**
     * Sets the RC5 secret key array to k
     *
     * @param k the byte array containing the secret key
     */
    public void setKey(byte[] k)
    {
        this.k = k;

        expandKey();
    }

    /**
     * Sets the 2w data block into the A and B attributes
     *
     * @param data the data to be operated on
     */
    public void setData(long data)
    {
        long temp = data;

        A = (int) ((temp >>> 32) & 0xFFFFFFFF);
        B = (int) (temp & 0xFFFFFFFF);
    }

    /**
     * Returns the word size of the object
     *
     * @return the RC5 attribute w
     */
    public int get_w()
    {
        return w;
    }

    /**
     * Returns the number of rounds
     *
     * @return the RC5 attribute r
     */
    public int get_r()
    {
        return r;
    }

    /**
     * Returns the key size in bytes
     *
     * @return the RC5 attribute b
     */
    public int get_b()
    {
        return b;
    }

    /**
     * Returns the size of the expanded key in words
     *
     * @return the RC5 attribute t
     */
    public int get_t()
    {
        return t;
    }

    /**
     * Returns the byte array containing the secret key
     *
     * @return the RC5 byte array k
     */
    public byte[] get_k()
    {
        return k;
    }

    /**
     * Returns the word array containing the expanded secret key
     *
     * @return the RC5 word array S
     */
    public int[] get_S()
    {
        return S;
    }

    /**
     * Returns the word containing the first word of data
     *
     * @return the RC5 word A
     */
    public int get_A()
    {
        return A;
    }

    /**
     * Returns the word containing the second word of data
     *
     * @return the RC5 word B
     */
    public int get_B()
    {
        return B;
    }

    /**
     * Returns the 2w block of data
     *
     * @return the RC5 words A and B in 2w block
     */
    public long getData()
    {
        long data = 0x0000000000000000L;

        data = ((long) A) & 0x00000000FFFFFFFFL;
        data = data << 32;
        data = data | (((long) B) & 0x00000000FFFFFFFFL);

        return data;
    }

    /**
     * Returns the current security level
     *
     * @return the RC5 attribute secLevel
     */
    public int get_secLevel()
    {
        return secLevel;
    }

    /**
     * Gets the info (rounds and key size) for a given
     * security level
     *
     * @param level the level to return info for
     * @return an array containing the number of rounds and key size for level:
     * level
     */
    public int[] getLevelInfo(int level)
    {
        int[] ret = {0,0};

        ret[0] = Security[0][level];
        ret[1] = Security[1][level];

        return ret;
    }

    /**
     * The RC5 Key Expansion Algorithm
     */
    private void expandKey()
    {
        int c, i, j;
        int L[] = null;

        int u = w / 8;

        //
        if(b < 1)
            c = 1;
        else if(b % u == 0)
            c = (b / u);
        else
            c = (b / u) + 1;

        L = new int[c];
        for(i = 0; i < c; i++)
            L[i] = 0;

        for(i = (b - 1); i >= 0; i--)
            L[i/u] = (L[i/u] << 8) + (k[i] & 0xFF);

        //
        int P = 0xB7E15163;
        int Q = 0x9E3779B9;

        S[0] = P;
        for(i = 0; i < (t - 1); i++)
            S[i + 1] = S[i] + Q;

        //
        i = 0;
        j = 0;
        int X = 0;
        int Y = 0;

        int count, limit;

        limit = (t < c) ? c : t;

        limit *= 3;
        count = 1;

        while(count <= limit)
        {
            X = S[i] = Integer.rotateLeft((S[i] + (X + Y)), 3);
            Y = L[j] = Integer.rotateLeft((L[j] + (X + Y)), (X + Y));

            i = (i + 1) % t;
            j = (j + 1) % c;

            count++;
        }
    }

    /**
     * The RC5 Encryption Algorithm
     */
    public void encrypt()
    {
        int i;

        A = A + S[0];
        B = B + S[1];

        for(i = 1; i <= r; i++)
        {
            A = Integer.rotateLeft((A ^ B), B) + S[2 * i];
            B = Integer.rotateLeft((B ^ A), A) + S[(2 * i) + 1];
        }
    }

    /**
     * The RC5 Decryption Algorithm
     */
    public void decrypt()
    {
        int i;

        for(i = r; i > 0; i--)
        {
            B = Integer.rotateRight ((B - S[(2 * i) + 1]), A) ^ A;
            A = Integer.rotateRight ((A - S[2 * i]), B) ^ B;
        }

        B = B - S[1];
        A = A - S[0];
    }
}