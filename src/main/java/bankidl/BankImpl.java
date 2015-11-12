/*
 * BankImpl.java
 *
 * Created on den 25 oktober 2007, 20:24
 *
 */
package bankidl;

import java.util.Hashtable;
import bankidl.BankPackage.rejected;

/**
 *
 * @author vlad-adm
 */
public class BankImpl extends _BankImplBase {

    private String bankname = null;
    private Hashtable accounts = new Hashtable();

    /**
     * Construct a persistently named object.
     */
    public BankImpl(String name) {
        super();
        bankname = name;
    }

    public Account newAccount(String name) throws rejected {
        AccountImpl account = (AccountImpl) accounts.get(name);
        if (account != null) {
            System.out.println("Account [" + name + "] exists!!!");
            throw new rejected("Rejected: Bank: "
                    + this.bankname
                    + " Account for: "
                    + name
                    + " already exists: "
                    + account);
        }
        account = new AccountImpl(name);
        accounts.put(name, account);
        System.out.println("Bank: " + bankname
                + " Account: " + name
                + " Created for " + name);

        return (Account) account;
    }

    public Account getAccount(java.lang.String name) {
        return (Account) accounts.get(name);
    }

    public boolean deleteAccount(String name) {
        AccountImpl account = (AccountImpl) accounts.get(name);
        if (account == null) {
            return false;
        }
        accounts.remove(name);
        System.out.println("Bank: " + this.bankname
                + " Account for " + name + " has been deleted");
        return true;
    }
}
