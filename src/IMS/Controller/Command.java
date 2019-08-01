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

    /**
     * Insert command, which insert an entities to the database the format should be {
     * @code insert <entities> <name> <cat> <unit price>}.
     */
    INSERT_ITEM("insert item (?:(\\w+)|(?:\'([^']+)\')) (?:(\\w+)|(?:\'([^']+)\')) (-?\\d+(?:\\.\\d{0,2})?)"),

    /**
     * Get command, query a entities from the database the format should be {@code get <entities> <name>}.
     */
    GET_ITEM("get item (?:(\\w+)|(?:\'([^']+)\'))"),

    /**
     *
     */
    INSERT_ORDER("insert order ((?:\\S+)\\.odr)"),

    GET_VENDOR("get vendor (?:(\\w+)|(?:\'([^']+)\'))"),

    GET_SOLD_ITEM("get sold items (?:(\\w+)|(?:\'([^']+)\'))"),

    INSERT_SALE,

    INV_STATUS,
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