
package finalyearproject;

import cipher.RC5;
import java.io.*;

/**
 *
 * @author Eoin Mooney - 0648396
 */
public class FYP
{
    private RC5 algorithm;                      // Instance of RC5 algorithm
    private String oldFilePath, newFilePath;    // Paths for files
    private long[] oldFileData, newFileData;    // Arrays for file data

    /**
     * Constructor
     * Creates a new FYP instance with an RC5-32 algorithm
     */
    public FYP()
    {
        this.algorithm = new RC5(32);
        oldFilePath = null;
        oldFileData = null;
        newFilePath = null;
        newFileData = null;
    }

    /**
     * Checks if secret key and paths are set
     *
     * @return A boolean array indicating what settings are set
     */
    public boolean[] checkSettings()
    {
        boolean ret[] = {true, true};

        if (algorithm.get_S() == null)
            ret[0] = false;
        if (oldFilePath == null || newFilePath == null)
            ret[1] = false;

        return ret;
    }

    /**
     * Returns the security level info (rounds and key size) for a given level
     *
     * @param level The security level wanted
     * @return An array containing the number of rounds and key size
     */
    public int[] getSecurityInfo(int level)
    {
        int[] ret = {0,0};
        ret = algorithm.getLevelInfo(level);
        return ret;
    }

    /**
     * Returns the current security level
     *
     * @return the current security level
     */
    public int getSecurityLevel()
    {
        int ret;
        ret = algorithm.get_secLevel();
        return ret;
    }

    /**
     * Sets the security level to the given level
     *
     * @param level The new security level
     */
    public void setSecurityLevel(int level)
    {
        algorithm.setLevel(level);
    }

    /**
     * Checks to make sure the provided key is the correct length
     *
     * @param key Secret Key to check length of
     * @return difference between actual key length and wanted length
     */
    int checkKeyLength(String key)
    {
        int length = getSecurityInfo(getSecurityLevel())[1];
        byte k[] = null;

        if (key.length() < length)
            return (key.length() - length);
        else if (key.length() > length)
            return (key.length() - length);
        else
            return 0;
    }

    /**
     * Sets the secret key to the given key key
     *
     * @param key String containing the secret key
     */
    void setSecretKey(String key)
    {
        byte k[] = null;

        k = key.getBytes();

        algorithm.setKey(k);
    }

    /**
     * Sets the secret key to the given key k
     *
     * @param k Byte array containing the secret key
     */
    public void setSecretKeyBytes(byte[] k)
    {
        algorithm.setKey(k);
    }

    /**
     * Returns a string containing the secret key
     *
     * @return String containing the secret key
     */
    String getSecretKey()
    {
        return new String(algorithm.get_k());
    }

    /**
     * sets the FYP attributes oldFilePath and newFilePath to oldPath and
     * newPath respectively
     *
     * @param oldPath string containing the source path
     * @param newPath string containing the destination path
     */
    void setPaths(String oldPath, String newPath)
    {
        oldFilePath = oldPath;
        newFilePath = newPath;
    }

    /**
     * Returns an array containing the file paths
     *
     * @return array containing file paths
     */
    String[] getPaths()
    {
        String ret[] = {oldFilePath, newFilePath};

        return ret;
    }

    /**
     * Returns the data from filePath in an array of 2w blocks
     *
     * @param filePath
     * @return an array of 2w sized blocks containing the data of the file
     * @throws IOException
     */
    long[] readFile(String filePath) throws IOException
    {
        File file = new File(filePath);
        FileInputStream in = null;
        byte temp[] = null;
        long fileData[] = null;
        int offset = 0, numRead = 0;

        try
        {
            in = new FileInputStream(file);

            if (file.length() > Integer.MAX_VALUE)
            {
                throw new IOException ("File is too big to parse");
            }

            temp = new byte[(int)file.length()];

            while ( offset < temp.length &&
                    (numRead=in.read(temp, offset, temp.length - offset)) >= 0)
            {
                offset += numRead;
            }

            if (offset < temp.length)
            {
                throw new IOException("Could not completely read file "+file.getName());
            }

            fileData = (temp.length % 8 == 0)
                    ? new long[temp.length / 8] : new long[(temp.length / 8 )+ 1];

            for (int i = 0, j; i < fileData.length; i++)
            {
                fileData[i] = 0;

                for (j = 0; j <= 7; j++)
                {
                    fileData[i] <<= 8;
                    if (((i * 8) + j) < temp.length)
                        fileData[i] += (temp[(i * 8) + j] & 0xFF);
                    else
                        fileData[i] += 0;
                }
            }
        }
        finally
        {
            if (in != null)
                in.close();
        }
        return fileData;
    }

    /**
     * Writes the data from fileData to a new file at filePath
     *
     * @param filePath the path of the new file
     * @param fileData the data to be written
     * @throws IOException
     */
    void writeFile(String filePath, long[] fileData) throws IOException
    {
        File file = new File(filePath);
        FileOutputStream out = null;
        byte temp[] = null;
        int i, tempLength = 0;

        if (file.isFile())
            file.delete();

        file.createNewFile();

        try
        {
            out = new FileOutputStream(file, false);

            temp = new byte[fileData.length * 8];
            for (i = 0; i < temp.length; i++)
                temp[i] = 0;

            for (i = 0; i < temp.length; i++)
            {
                int maskOffset = 7 - (i % 8);
                temp[i] = (byte) ((fileData[i/8] >>> (8 * maskOffset)) & 0xFF);
            }

            tempLength = temp.length;

            for (i = temp.length; temp[i-1] == 0; i--)
                tempLength = i;

            out.write(temp, 0, tempLength);
        }
        finally
        {
            if (out != null)
                out.close();
        }
    }

    /**
     * Steps through oldFileData array and encrypts each 2w block then stores
     * the encrypted data in newFileData
     */
    void encryptFile()
    {
        int i;

        try
        {
            oldFileData = readFile(oldFilePath);
        }
        catch (IOException ioe)
        {
            System.out.println();
            System.out.println("IOException");
            System.out.println(ioe);
        }
        newFileData = new long[oldFileData.length];

        for (i = 0; i < newFileData.length; i++)
            newFileData[i] = 0;

        for (i = 0; i < oldFileData.length; i++)
        {
            algorithm.setData(oldFileData[i]);
            algorithm.encrypt();
            newFileData[i] = algorithm.getData();
        }

        try
        {
            writeFile(newFilePath, newFileData);
        }
        catch (IOException ioe)
        {
            System.out.println();
            System.out.println("IOException");
            System.out.println(ioe);
        }
    }

    /**
     * Steps through oldFileData array and decrypts each 2w block then stores
     * the decrypted data in newFileData
     */
    void decryptFile()
    {
        int i;

        try
        {
            oldFileData = readFile(oldFilePath);
        }
        catch (IOException ioe)
        {
            System.out.println();
            System.out.println("IOException");
            System.out.println(ioe);
        }

        newFileData = new long[oldFileData.length];

        for (i = 0; i < newFileData.length; i++)
            newFileData[i] = 0;

        for (i = 0; i < oldFileData.length; i++)
        {
            algorithm.setData(oldFileData[i]);
            algorithm.decrypt();
            newFileData[i] = algorithm.getData();
        }

        try
        {
            writeFile(newFilePath, newFileData);
        }
        catch (IOException ioe)
        {
            System.out.println();
            System.out.println("IOException");
            System.out.println(ioe);
        }
    }
}
