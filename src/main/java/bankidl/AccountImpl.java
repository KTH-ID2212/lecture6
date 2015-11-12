/*
 * AccountImpl.java
 *
 * Created on den 25 oktober 2007, 20:30
 *
 */
package bankidl;

import bankidl.AccountPackage.rejected;

/**
 *
 * @author vlad-adm
 */
public class AccountImpl extends _AccountImplBase {

    private float balance = 0;
    private String name = null;

    /**
     * Construct a persistently named object.
     */
    public AccountImpl(java.lang.String name) {
        super();
        this.name = name;
    }

    public void deposit(float value) throws rejected {
        if (value < 0) {
            throw new rejected("Rejected: Account " + name + ": Illegal value: " + value);
        }
        balance += value;
        System.out.println("Transaction: Account " + name + ": deposit: $"
                + value + ", balance: $" + balance);
    }

    public void withdraw(float value) throws rejected {
        if (value < 0) {
            throw new rejected("Rejected: Account " + name + ": Illegal value: " + value);
        }
        if ((this.balance - value) < 0) {
            throw new rejected("Rejected: Account " + name
                    + ": Negative balance on withdraw: "
                    + (balance - value));
        }
        balance -= value;
        System.out.println("Transaction: Account " + name + ": withdraw: $"
                + value + ", balance: $" + balance);
    }

    public float balance() {
        return balance;
    }
}
