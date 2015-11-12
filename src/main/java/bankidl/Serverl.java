/*
 * Serverl.java
 *
 * Created on Nov 9, 2007, 2:50:16 PM
 *
 */
package bankidl;

import org.omg.CORBA.ORB;
import java.io.File;
import java.io.FileWriter;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextHelper;

/**
 *
 * @author vladimir
 */
public class Serverl {

    public static final String USAGE = "usage: java bankidl.Serverl bankname dir";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(USAGE);
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
            File dir = new File(args[1]);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String filename = dir + Character.toString(File.separatorChar) + args[0] + ".ior";
            //System.out.println(filename);
            File file = new File(filename);
            file.createNewFile();
            file.deleteOnExit();
            FileWriter writer = new FileWriter(file);
            writer.write(orb.object_to_string(bankRef));
            writer.close();
            Object sync = new Object();
            synchronized (sync) {
                sync.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(USAGE);
            System.exit(1);
        }
    }
}
