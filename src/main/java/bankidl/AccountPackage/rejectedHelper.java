package bankidl.AccountPackage;

/**
 * bankidl/AccountPackage/rejectedHelper.java . Generated by the IDL-to-Java compiler (portable),
 * version "3.2" from bank.idl den 25 oktober 2007 kl 20:51 CEST
 */
abstract public class rejectedHelper {

    private static String _id = "IDL:bankidl/Account/rejected:1.0";

    public static void insert(org.omg.CORBA.Any a, bankidl.AccountPackage.rejected that) {
        org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
        a.type(type());
        write(out, that);
        a.read_value(out.create_input_stream(), type());
    }

    public static bankidl.AccountPackage.rejected extract(org.omg.CORBA.Any a) {
        return read(a.create_input_stream());
    }

    private static org.omg.CORBA.TypeCode __typeCode = null;
    private static boolean __active = false;

    synchronized public static org.omg.CORBA.TypeCode type() {
        if (__typeCode == null) {
            synchronized (org.omg.CORBA.TypeCode.class) {
                if (__typeCode == null) {
                    if (__active) {
                        return org.omg.CORBA.ORB.init().create_recursive_tc(_id);
                    }
                    __active = true;
                    org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember[1];
                    org.omg.CORBA.TypeCode _tcOf_members0 = null;
                    _tcOf_members0 = org.omg.CORBA.ORB.init().create_string_tc(0);
                    _members0[0] = new org.omg.CORBA.StructMember(
                            "reason",
                            _tcOf_members0,
                            null);
                    __typeCode = org.omg.CORBA.ORB.init().create_exception_tc(bankidl.AccountPackage.rejectedHelper.id(), "rejected", _members0);
                    __active = false;
                }
            }
        }
        return __typeCode;
    }

    public static String id() {
        return _id;
    }

    public static bankidl.AccountPackage.rejected read(org.omg.CORBA.portable.InputStream istream) {
        bankidl.AccountPackage.rejected value = new bankidl.AccountPackage.rejected();
        // read and discard the repository ID
        istream.read_string();
        value.reason = istream.read_string();
        return value;
    }

    public static void write(org.omg.CORBA.portable.OutputStream ostream, bankidl.AccountPackage.rejected value) {
        // write the repository ID
        ostream.write_string(id());
        ostream.write_string(value.reason);
    }

}
