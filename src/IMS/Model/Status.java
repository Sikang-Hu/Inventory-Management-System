package IMS.Model;

public class Status {
  private String store;
  private String cat;
  private String item;
  private int stock;

  Status(String store, String cat, String item, int stock) {
    this.store = store;
    this.cat = cat;
    this.item = item;
    this.stock = stock;
  }

  public String getStore() {
    return this.store;
  }

  public String getCat() {
    return this.cat;
  }

  public String getItem() {
    return this.item;
  }

  public int getStock() {
    return this.stock;
  }

  @Override
  public String toString() {
    return "Status{" +
            "store='" + store + '\'' +
            ", cat='" + cat + '\'' +
            ", item='" + item + '\'' +
            ", stock=" + stock +
            '}';
  }
}