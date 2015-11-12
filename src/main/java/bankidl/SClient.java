/*
 * SClient.java
 *
 * Created on Nov 9, 2007, 2:09:59 PM
 *
 */
package bankidl;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CORBA.ORB;

/**
 *
 * @author vladimir
 */
public class SClient {

    static final String USAGE = "java bankidl.SClient <bank> <client> <value> <-ORBInitialPort port>";
    Account account;
    Bank bankobj;
    String bankname = "SEB";
    String clientname = "Vladimir Vlassov";
    float value = 100;

    public static void main(String[] args) {
        if ((args.length > 0) && args[0].equals("-h")) {
            System.out.println(USAGE);
            System.exit(0);
        }
        new SClient(args).run();
    }

    public SClient(String[] args) {
        if ((args.length > 5) || (args.length > 0) && args[0].equals("-h")) {
            System.out.println(USAGE);
            System.exit(0);
        }
        if (args.length > 2) {
            try {
                value = (new Float(args[2])).floatValue();
            } catch (NumberFormatException e) {
                System.out.println(USAGE);
                System.exit(0);
            }
        }
        if (args.length > 1) {
            clientname = args[1];
        }
        if (args.length > 0) {
            bankname = args[0];
        }
        try {
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);
            NameComponent nc = new NameComponent(bankname, "");
            NameComponent[] path = {nc};
            bankobj = BankHelper.narrow(ncRef.resolve(path));
        } catch (Exception se) {
            System.out.println("The runtime failed: " + se);
            se.printStackTrace();
            System.exit(0);
        }
        System.out.println("Connected to bank: " + bankname);
    }

    public void run() {
        try {
            account = bankobj.getAccount(clientname);
            if (account == null) {
                account = bankobj.newAccount(clientname);
            }
            account.deposit(value);
            System.out.println(clientname + "'s account: $" + account.balance());
        } catch (org.omg.CORBA.SystemException se) {
            System.out.println("The runtime failed: " + se);
            System.exit(0);
        } catch (bankidl.AccountPackage.rejected e) {
            System.out.println(e.reason);
            System.exit(0);
        } catch (bankidl.BankPackage.rejected e) {
            System.out.println(e.reason);
            System.exit(0);
        }
    }
}
