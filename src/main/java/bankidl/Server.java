/*
 * Server.java
 *
 * Created on den 25 oktober 2007, 20:38
 *
 */
package bankidl;

import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;
import org.omg.CORBA.ORB;

/**
 *
 * @author vlad-adm
 */
public class Server {

    public static void main(String args[]) {
        if (args.length != 3) {
            System.out.println("usage: java Server <bankname> <-ORBInitialPort port>");
            System.exit(1);
        }
        try {
            ORB orb = ORB.init(args, null);
            BankImpl bankRef = new BankImpl(args[0]);
            orb.connect(bankRef);
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContext ncRef = NamingContextHelper.narrow(objRef);
            NameComponent nc = new NameComponent(args[0], "");
            NameComponent path[] = {nc};
            ncRef.rebind(path, bankRef);
            Object sync = new Object();
            synchronized (sync) {
                sync.wait();
            }
        } catch (Exception e) {
        }
    }
}
