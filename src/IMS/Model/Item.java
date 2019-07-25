package IMS.Model;

/**
 * Created by Sikang on 2019-07-25.
 */
public class Item {
  String name;
  double price;

  public Item(String name, double price) {
    this.name = name;
    this.price = price;
  }

  @Override
  public String toString() {
    return String.format("%s %.2f", this.name, this.price);
  }
}
