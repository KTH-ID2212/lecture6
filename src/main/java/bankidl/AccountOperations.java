package bankidl;

/**
 * bankidl/AccountOperations.java . Generated by the IDL-to-Java compiler (portable), version "3.2"
 * from bank.idl den 25 oktober 2007 kl 20:51 CEST
 */
public interface AccountOperations {

    float balance();

    void deposit(float value) throws bankidl.AccountPackage.rejected;

    void withdraw(float value) throws bankidl.AccountPackage.rejected;
} // interface AccountOperations