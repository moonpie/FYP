/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.math.BigInteger;

/**
 *
 * @author eoin
 */
public interface RMI extends java.rmi.Remote
{
    public BigInteger generateKey_Client(BigInteger g, BigInteger p, BigInteger A, int level)
            throws java.rmi.RemoteException;
    public boolean checkKeyReady_Server()
            throws java.rmi.RemoteException;
    public BigInteger[] getKey_Server()
            throws java.rmi.RemoteException;
}
