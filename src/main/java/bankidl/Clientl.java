/*
 * Clientl.java
 * 
 * Created on Nov 9, 2007, 3:52:29 PM
 * 
 */
package bankidl;

import org.omg.CORBA.ORB;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.util.StringTokenizer;

/**
 *
 * @author vladimir
 */
public class Clientl {

    static final String USAGE = "java bankidl.Client url <-ORBInitialPort port>";
    Account account;
    Bank bankobj;
    String bankname = "SEB";
    String clientname;

    public static void main(String[] args) {

        if ((args.length > 0) && args[0].equals("-h")) {
            System.out.println(USAGE);
            System.exit(0);
        }

        new Clientl(args).run();
    }

    public Clientl(String[] args) {

        if (args.length < 1) {
            System.out.println(USAGE);
            System.exit(1);
        }

        try {
            URL bankURL = new URL(args[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) bankURL.getContent()));
            ORB orb = ORB.init(args, null);
            org.omg.CORBA.Object objRef = orb.string_to_object(in.readLine());
            bankobj = BankHelper.narrow(objRef);
        } catch (Exception se) {
            System.out.println("The runtime failed: " + se);
            se.printStackTrace();
            System.exit(0);
        }

        System.out.println("Connected to bank: " + bankname);
    }

    public void run() {

        BufferedReader d
                = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print(clientname + "@" + bankname + ">");
            try {
                String str = d.readLine();
                if (str != null) {
                    Command command = parse(str);
                    if (command == null) {
                        continue;
                    }
                    execute(command);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final static String[] commands = {
        "newAccount",
        "getAccount",
        "deleteAccount",
        "deposit",
        "withdraw",
        "balance",
        "quit",
        "help"
    };

    private final static int ILLEGAL = -1,
            NEW_ACCOUNT = 0,
            GET_ACCOUNT = 1,
            DELETE_ACCOUNT = 2,
            DEPOSIT = 3,
            WITHDRAW = 4,
            BALANCE = 5,
            QUIT = 6,
            HELP = 7;

    Command parse(String str) {

        StringTokenizer st = new StringTokenizer(str);
        if (st.countTokens() == 0) {
            return null;
        }
        String command = null, name = null;
        float value = 0;
        int opcode = ILLEGAL, n = 0;
        while (st.hasMoreTokens()) {
            switch (n) {
                case 0:
                    command = st.nextToken();
                    for (int i = 0; i < commands.length; i++) {
                        if (commands[i].equals(command)) {
                            opcode = i;
                            break;
                        }
                    }
                    if (opcode == ILLEGAL) {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;
                case 1:
                    name = st.nextToken();
                    break;
                case 2:
                    try {
                        value = (new Float(st.nextToken())).floatValue();
                    } catch (NumberFormatException e) {
                        System.out.println("Illegal value");
                        return null;
                    }
                    break;
                default:
                    st.nextToken();
            }
            n++;
        }
        return new Command(opcode, name, value);
    }

    void execute(Command command) {

        int opcode = command.opcode;

        switch (opcode) {
            case QUIT:
                System.exit(1);
            case HELP:
                for (int i = 0; i < commands.length; i++) {
                    System.out.println(commands[i]);
                }
                return;
        }
        // all further commands require a name to be specified
        String name = command.name;
        if (name == null) {
            name = clientname;
        }
        if (name == null) {
            System.out.println("name is not specified");
            return;
        }
        try {
            if (opcode == NEW_ACCOUNT) {
                clientname = name;
                bankobj.newAccount(name);
                return;
            }
            if (opcode == DELETE_ACCOUNT) {
                clientname = name;
                bankobj.deleteAccount(name);
                return;
            }
            // all further commands require a bank.Account refernce
            Account acc = bankobj.getAccount(name);
            if (acc == null) {
                System.out.println("No account for" + name);
                return;
            } else {
                account = acc;
                clientname = name;
            }
            switch (opcode) {
                case GET_ACCOUNT:
                    System.out.println(account);
                    break;
                case DEPOSIT:
                    account.deposit(command.value);
                    break;
                case WITHDRAW:
                    account.withdraw(command.value);
                    break;
                case BALANCE:
                    System.out.println("balance: $" + account.balance());
                    break;
                default:
                    System.out.println("Illegal command");
            }
        } catch (bankidl.BankPackage.rejected e) {
            System.out.println(e.reason);
        } catch (bankidl.AccountPackage.rejected e) {
            System.out.println(e.reason);
        } catch (org.omg.CORBA.SystemException se) {
            System.out.println("The runtime failed: " + se);
        }
    }
}

/*class Command {

 protected int opcode;
 protected String name;
 protected float value;

 public Command(int opcode, String name, float value) {
 this.opcode = opcode;
 this.name = name;
 this.value = value;
 }
 }*/
