package IMS.Controller;
import IMS.IMSException;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a class representing a valid command. A command consists of two parts: its type (i.e.
 * what kind of operation, add, query or etc.), and its operands, which are the essential arguments
 * taken by this command.
 */
class Command {

  /**
   * Type of this command, which is an enum.
   */
  private Type type;

  /**
   * An array of string that stores all the arguments of this command.
   */
  private String[] operands;

  static final String stringArg = "(?:(\\w+)|(?:\'([^']+)\'))";

  static final String doubleArg = "(-?\\d+(?:\\.\\d{0,2})?)";

  private static final String zipCode = "(\"[0-9]{5}\")";
  private static final String id = "(\"[0-9]{10}\")";
  private static final String date = "^((0|1)\\d{1})/((0|1|2)\\d{1})/((19|20)\\d{2})";


  /**
   * Construct a new command object with given type and arguments.
   *
   * @param t the Type of the command
   * @param o the arguments of the command
   */
  private Command(Type t, String[] o) {
    this.type = t;
    this.operands = o;
  }

  /**
   * This is an enum defining all kinds of supported commands. Every type of command specifies the
   * format of the command by regex, and stores it as an instance of Pattern. In this case, the
   * recognition of command is case-insensitive.
   */
  enum Type {


    // insert cat cat_name cat_description
    INSERT_CATEGORY(Command.assemble("insert cat", Command.stringArg, Command.stringArg)),

    //get cat cat_name
    GET_CATEGORY("get cat " + Command.stringArg),


    /**
     * Insert command, which insert an entities to the database the format should be {
     * @code insert <entities> <name> <cat> <unit price>}.
     */
    INSERT_ITEM("insert item " + Command.stringArg + " " + Command.stringArg + " " +Command.doubleArg),

    /**
     * Get command, query a entities from the database the format should be {@code get <entities> <name>/<<item_id>}.
     */
    GET_ITEM("get item " +  "(" + Command.stringArg + "|"+  Command.id + ")"),


    //insert vendor ven_name address state zipcode
    INSERT_VENDOR("insert vendor " + Command.stringArg + " " + Command.stringArg
            + " " + Command.stringArg + " "+ "[0-9]{5}" + " " + Command.zipCode),

    //get vendor ven_name/ven_id
    GET_VENDOR("get vendor " + "(" + Command.stringArg + "|"+  Command.id + ")"),

    //get sold items ven_name
    GET_SOLD_ITEM("get sold items " + Command.stringArg),

    //add item ven_name fileName_of_items.txt
    ADD_SOLD_ITEM(Command.assemble("add item", Command.stringArg, "[^\\s.]+\\.txt")),



    //insert store address state zip
    INSERT_STORE("insert store " + Command.stringArg + " " + Command.stringArg + " " +Command.zipCode),

    //get store name/id
    GET_STORE("get store " + Command.stringArg),

    //status store_id/null item_name/null cat_name/null
    STATUS("status" + Command.id + Command.stringArg + Command.stringArg),


    INSERT_TRANSACTION("insert (order|sale) ((?:\\S+)\\.odr)"),

    //get item in transaction order/sale order_id/sale_id
    GET_ITEM_IN_TRANSAC("get item in transaction (order|sale) " + Command.id),
    //get order/sale customer/vendor/store customer_id/vendor_name/store_id MM/dd/yyyy
    GET_TRANSACTION_BY_CONDITION("get (order|sale) (customer|vendor|store) " + "("
            + Command.id + "|" + Command.stringArg + ")" + Command.date),
    //get transaction  by id sale/order sale_id/order_id
    GET_TRANSACTION_BY_ID("get transaction by id (order|sale) " + Command.id),

    //delivered order_id
    UPDATE_DELIVERY("delivered " + Command.id),

    //return sale_id item_name, quantity
    RETURN_SALE("return " + Command.id + Command.stringArg + Command.id),


    /**
     * exit/quit the system.
     */
    QUIT,EXIT;

    /**
     * The pattern that commands of this type need to follow.
     */
    private Pattern pattern;



    /**
     * Construct a new Type that has no definition of format. The pattern will be its string value
     * by default.
     */
    Type() {
      this.pattern = Pattern.compile("(?i)" + this.toString() + "$");
    }

    /**
     * Construct a new Type that has a definition of format, and will compile the regex into string
     * and store it.
     *
     * @param regex the definition of the format of this kind of command
     */
    Type(String regex) {
      this.pattern = Pattern.compile("(?i)" + regex + "$");
    }


  }

  /**
   * Parse a given command into string, determine which kind of command it is and extract its
   * arguments. If the given command does not fit any Type, it will throw an exception.
   *
   * @param command the command being parsed
   * @return a command object resulting from the given string
   * @throws IllegalArgumentException if given string does not fit any Type
   */
  static Command parseCommand(String command) throws IMSException {
    for (Type t : Type.values()) {
      Matcher m = t.pattern.matcher(command);
      if (m.matches()) {
        List<String> l = new ArrayList<>();
        String temp;
        for (int i = 0; i < m.groupCount(); i++) {
          temp = m.group(i + 1);
          if (temp != null) {
            l.add(temp);
          }
        }
        return new Command(t, l.toArray(new String[0]));
      }
    }
    throw new IMSException("Invalid Input: " + command);
  }

  static String assemble(String... args) {
    return String.join("\\s+", args);
  }

  /**
   * Get the Type of this command.
   *
   * @return the Type enum of this command
   */
  Type getType() {
    return this.type;
  }

  /**
   * Get the operands, also arguments, of this command.
   *
   * @return a string array containing all the arguments of this command in the order in which they
   * were input.
   */
  String[] getOperands() {
    return this.operands;
  }
}