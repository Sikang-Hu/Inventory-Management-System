package IMS.Model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Sikang on 2019-07-25.
 */
public class Order {
  private String supplier;
  private Date date;
  List<Item> items = new ArrayList<>();

//  protected void write() {
//    try(BufferedWriter wt = new BufferedWriter(new FileWriter(data, true))) {
//      wt.append(this.supplier).append(",");
//    } catch (IOException e) {
//      // do nothing
//    }
//  }

  public Order(File f) {
    String ln;
    try (BufferedReader rd = new BufferedReader(new FileReader(f))){
      this.supplier = rd.readLine();
      this.date = new SimpleDateFormat("dd/MM/yyyy").parse(rd.readLine());
      while ((ln = rd.readLine()) != null) {
        String[] item = ln.split(" ");
        this.items.add(new Item(item[0], Double.parseDouble(item[1])));
      }
    } catch (IOException e) {
      // also do nothing
    } catch (ParseException p) {
      System.out.println(p.getErrorOffset());
    }
  }

  protected void write(Appendable ap) throws IOException{
    ap.append(this.toString());
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
