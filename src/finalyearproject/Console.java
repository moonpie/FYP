
package finalyearproject;

import java.util.Scanner;
import java.io.*;

/**
 *
 * @author Eoin Mooney - 0648396
 */
public class Console {

    Scanner input;      // Scanner for standard in
    private FYP proj;   // Link instance of FYP to Console

    /**
     * Constructor
     * Creates new console instance with scanner for standard in and an instance
     * of FYP
     */
    public Console()
    {
        input = new Scanner(System.in);
        proj = new FYP();
    }

    /**
     * Prints the primitive GUI menu
     */
    void printMenu()
    {
        String choice = new String("");

        /*
         */

        do
        {
            System.out.println("----------------------------------------");
            System.out.println("Main Menu:\n"
                    + "1) Select Security Level\n"
                    + "2) Set Secret Key\n"
                    + "3) Select File\n"
                    + "4) Select Operation\n");
            System.out.print("Please enter a choice (1-4) or q to quit: ");

            choice = input.next();
            switch (choice.charAt(0))
            {
                case '1' :
                    selectSecurityLevel();
                    break;
                case '2' :
                    setSecretKey();
                    break;
                case '3' :
                    selectFiles();
                    break;
                case '4' :
                    selectOperation();
                    break;
                case ('q' | 'Q') :
                    break;
                default :
                    System.out.println("\nInvalid Selection: "
                            + choice.charAt(0)
                            + "\nPlease try again.\n");
            }
        }
        while (choice.charAt(0) != 'q' && choice.charAt(0) != 'Q');
    }

    /**
     *
     */
    void selectSecurityLevel()
    {
        int level;
        boolean valid = false, check = false;
        String ok = new String("");
        String choice = new String("");

        /*
         */

        System.out.println("----------------------------------------");
        System.out.println("Current Security Level: "
                + proj.getSecurityLevel()
                + "\n\tRounds: "
                + proj.getSecurityInfo(proj.getSecurityLevel())[0]
                + "\n\tKey Size: "
                + proj.getSecurityInfo(proj.getSecurityLevel())[1]);

        while (!valid)
        {
            System.out.print("\nPlease select a security level (0-9): ");

            choice = input.next() + " ";
            switch (choice.charAt(0))
            {
                case '0' :
                    level = 0;
                    break;
                case '1' :
                    level = 1;
                    break;
                case '2' :
                    level = 2;
                    break;
                case '3' :
                    level = 3;
                    break;
                case '4' :
                    level = 4;
                    break;
                case '5' :
                    level = 5;
                    break;
                case '6' :
                    level = 6;
                    break;
                case '7' :
                    level = 7;
                    break;
                case '8' :
                    level = 8;
                    break;
                case '9' :
                    level = 9;
                    break;
                default :
                    level = -1;
            }

            if (level >= 0 && level <= 9)
                valid = true;
            else
            {
                valid = false;
                System.out.println("\nInvalid Selection");
                System.out.println("Please try again.");
            }

            if (valid)
            {
                System.out.println("\nChosen Security Level: "
                        + level
                        + "\n\tRounds: "
                        + proj.getSecurityInfo(level)[0]
                        + "\n\tKey Size: "
                        + proj.getSecurityInfo(level)[1]);

                do
                {
                    System.out.print("\nIs this ok? (y/n) ");

                    ok = input.next();
                    switch (ok.charAt(0))
                    {
                        case ('y' | 'Y') :
                            check = true;
                            proj.setSecurityLevel(level);
                            break;
                        case ('n' | 'N') :
                            valid = false;
                            check = true;
                            System.out.println();
                            break;
                        default :
                            check = false;
                            System.out.println("\nInvalid Input: "
                                    + ok.charAt(0)
                                    + "\nPlease try again.");      
                    }
                }
                while (!check);
            }
        }
    }

    /**
     *
     */
    void setSecretKey()
    {
        int test;
        boolean valid = false;
        String ok = new String("");
        String key = new String("");
        
        /* Check if Security Level is set
         *  Respond
         * Get User Input
         *  Check Input is valid & respond
         * Confirm User Satisfied
         *  Check Input is valid & respond
         * finalyearproject.FYP.setSecretKey
         */
        
        System.out.println("----------------------------------------");
        System.out.println("Current Security Level: "
                + proj.getSecurityLevel()
                + "\n\tRounds: "
                + proj.getSecurityInfo(proj.getSecurityLevel())[0]
                + "\n\tKey Size: "
                + proj.getSecurityInfo(proj.getSecurityLevel())[1]);

        do
        {
            System.out.print("\nIs this ok? (y/n) ");

            ok = input.next();
            switch (ok.charAt(0))
            {
                case ('y' | 'Y') :
                    valid = true;
                    proj.setSecurityLevel(proj.getSecurityLevel());
                    break;
                case ('n' | 'N') :
                    valid = true;
                    selectSecurityLevel();
                    break;
                default :
                    valid = false;
                    System.out.println("\nInvalid Input: "
                            + ok.charAt(0)
                            + "\nPlease try again.");
            }
        }
        while (!valid);

        do
        {
            System.out.println("Please enter your "
                    + proj.getSecurityInfo(proj.getSecurityLevel())[1]
                    + " byte Secret Key:");
            key = input.next();

            test = proj.checkKeyLength(key);

            if (test != 0)
                System.out.println("\nSecret Key wrong size ("
                        + (proj.getSecurityInfo(proj.getSecurityLevel())[1] + test)
                        + " bytes)\nPlease try again.\n");
            else
                do
                {
                    System.out.print("\nAre you sure? (y/n) ");

                    ok = input.next();
                    switch (ok.charAt(0))
                    {
                        case ('y' | 'Y') :
                            valid = true;
                            proj.setSecretKey(key);
                            break;
                        case ('n' | 'N') :
                            test = 1;
                            valid = true;
                            System.out.println();
                            break;
                        default :
                            valid = false;
                            System.out.println("\nInvalid Input: "
                                    + ok.charAt(0)
                                    + "\nPlease try again.");
                    }
                }
                while (!valid);
        }
        while (test != 0);
    }

    /**
     *
     */
    void selectFiles()
    {
        String oldPath = new String("");
        String newPath = new String("");
        String in = new String("");
        File check = null;
        boolean ok = false;
        boolean valid = false;


        /* 
         */

        System.out.println("----------------------------------------");
        do
        {
            do
            {
                System.out.println("Please choose a source file to operate on:");
                oldPath = input.next();

                check = new File(oldPath);

                if (!check.isFile())
                {
                    System.out.println("\nSorry, file does not exist");
                    System.out.println("Please try again");
                }
                else if (!check.canRead())
                {
                    System.out.println("\nSorry, file cannot be opened for reading");
                    System.out.println("Please try again");
                }
            }
            while (!check.isFile() || !check.canRead());

            System.out.println();

            do
            {
                System.out.println("Please choose a filename for the encrypted/decrypted file:");
                newPath = input.next();

                check = new File(newPath);

                if (oldPath.equals(newPath))
                {
                    System.out.println("\nFiles cannot have the same name and location\n");
                    valid = false;
                }
                else if (check.isFile())
                {
                    System.out.print("\nFile exists");

                    if (check.canWrite())
                    {
                        System.out.println(" and will be overwritten");
                        valid = true;
                    }
                    else
                    {
                        System.out.println(".\nSorry, file cannot be opened for writing");
                        System.out.println("Please try again");
                        valid = false;
                    }
                }
                else if (!check.exists())
                {
                    System.out.print("\nFile does not exist");

                    try
                    {
                        if(check.createNewFile())
                        {
                            System.out.println(" and will be created");
                            check.delete();
                            valid = true;
                        }
                        else
                        {
                            System.out.println(".\nSorry, file cannot be created");
                            System.out.println("Please try again");
                            valid = false;
                        }
                    }
                    catch (IOException ioe)
                    {
                        System.out.println();
                        System.out.println("IOException");
                        System.out.println(ioe);
                    }
                }
                else
                {
                    System.out.println("Sorry, location is not a file");
                    System.out.println("Please try again");
                    valid = false;
                }
            }
            while (!valid);

            System.out.println("\nOriginal File:\n\t"
                    + oldPath);
            System.out.println("Encrypted/Decrypted File:\n\t"
                    + newPath);
            do
            {
                System.out.print("\nIs this ok? (y/n) ");

                in = input.next();
                switch (in.charAt(0))
                {
                    case ('y' | 'Y') :
                        valid = true;
                        ok = true;
                        proj.setPaths(oldPath, newPath);
                        break;
                    case ('n' | 'N') :
                        valid = true;
                        ok = false;
                        break;
                    default :
                        valid = false;
                        System.out.println("\nInvalid Input: "
                                + in.charAt(0)
                                + "\nPlease try again.");
                }
            }
            while (!valid);
        }
        while (!ok);
    }

    /**
     * 
     * @return
     */
    int selectOperation()
    {
        String choice = new String("");
        String in = new String("");
        boolean valid;
        boolean settings[] = new boolean[2];
        /*
         */

        settings = proj.checkSettings();
        if (!settings[0])
        {
            System.out.println("----------------------------------------");
            System.out.println("Secret Key has not been set!");
            setSecretKey();
        }
        if (!settings[1])
        {
            System.out.println("----------------------------------------");
            System.out.println("Filenames have not been set!");
            selectFiles();
        }

        do
        {
            System.out.println("----------------------------------------");
            System.out.println("Operation Menu:\n"
                    + "1) Encrypt\n"
                    + "2) Decrypt\n");
            System.out.print("Please enter a choice (1 or 2): ");

            choice = input.next();

            if (choice.charAt(0) == '1' || choice.charAt(0) == '2')
            {
                valid = true;
            }
            else
            {
                valid = false;
                System.out.println("\nInvalid Selection: "
                        + choice.charAt(0)
                        + "\nPlease try again.\n");
            }
        }
        while (!valid);

        System.out.println("Security Level:\n\t" + proj.getSecurityLevel());
        System.out.println("Secret Key:\n\t" + proj.getSecretKey());
        System.out.println("Source File:\n\t" + proj.getPaths()[0]);
        if (choice.charAt(0) == '1')
            System.out.println("Encrypted File:\n\t" + proj.getPaths()[1]);
        else //choice.charAt(0) == '2'
            System.out.println("Decrypted File:\n\t" + proj.getPaths()[1]);
        do
        {
            System.out.print("\nIs this ok? (y/n) ");

            in = input.next();
            switch (in.charAt(0))
            {
                case ('y' | 'Y') :
                    valid = true;
                    break;
                case ('n' | 'N') :
                    valid = true;
                    return 1;
                default :
                    valid = false;
                    System.out.println("\nInvalid Input: "
                            + in.charAt(0)
                            + "\nPlease try again.");
            }
        }
        while (!valid);

        if (choice.charAt(0) == '1')
        {
            System.out.print("\nEncrypting .");
            proj.encryptFile();
            System.out.println(".. Done!\n");
        }
        else //choice.charAt(0) == '2'
        {
            System.out.print("\nDecrypting .");
            proj.decryptFile();
            System.out.println(".. Done!\n");
        }

        return 0;
    }
}
