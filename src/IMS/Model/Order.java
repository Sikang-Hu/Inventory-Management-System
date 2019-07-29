package IMS.Model;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by Sikang on 2019-07-25.
 */
public class Order implements Serializable {
  private String supplier;
  private Date date;
  private List<Item> items = new ArrayList<>();
  private static Pattern supPtn = Pattern.compile("Suppiler: \\S+\n");
  private static Pattern itmPtn = Pattern.compile("(\\S+) (\\d+) (\\S)")


  public Order(String order) {
    try {
      Scanner scan = new Scanner(order);
      this.supplier = scan.next(supPtn);
      this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(scan.nextLine());
      while (scan.hasNextLine()) {

      }
    } catch (NumberFormatException e) {
      // also do nothing
    } catch (ParseException e) {

    }
  }

  public String getSupplier() {
    return this.supplier;
  }

  public double getTotal() {
    return this.items.stream().mapToDouble(Item::getPrice).sum();
  }

  public Date getDate() {
    return this.date;
  }

  public Iterator<Item> getItems() {
    return items.iterator();
  }

  void insertToDB() {


  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.supplier).append(",").append(this.date).append(",");
    for (Item i : items) {
      sb.append(i.toString()).append(",");
    }
    sb.deleteCharAt(sb.lastIndexOf(","));
    return sb.toString();
  }

}

class OrderList extends ArrayList<Order> implements Serializable {}
