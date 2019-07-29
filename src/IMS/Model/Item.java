package IMS.Model;

import IMS.IMSException;
import IMS.IMSUtil;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Sikang on 2019-07-25.
 */
public class Item implements Serializable {
  private String name;
  private String category;
  private Double price;
  private static String dbAddr = ".item";



  public Item(String name, String category, double unitPrice) {
    this.name = name;
    this.category = category;
    this.price = unitPrice;
  }

  public String getName() {
    return this.name;
  }

  public String getCategory() {
    return this.category;
  }

  public double getPrice() {
    return this.price;
  }

  /**
   * Insert this item into item table
   */
  void insertToDB() {
    File itemDB = Item.checkInit();
    HashMap<String, Item> items = IMSUtil.readObject(itemDB, ItemMap.class);
    if (items.containsKey(this.name)) {
      throw new IMSException("Item: %s is already in the database", this.name);
    } else {
      items.put(this.name, this);
      IMSUtil.writeObject(items, itemDB);
    }
  }

  /**
   * Fetch out the item named NAME
   * @param name
   * @return
   */
  static Item getItem(String name) {
    File itemDB = Item.checkInit();
    HashMap<String, Item> items = IMSUtil.readObject(itemDB, ItemMap.class);
    Item result = items.get(name);
    if (result == null) {
      throw new IMSException("Item: %s does not exist in the system", name);
    } else {
      return result;
    }
  }

  @Override
  public String toString() {
    return String.format("%s, %s, %.2f", this.name, this.category, this.price);
  }

  private static File checkInit() {
    File itemDB = new File(Item.dbAddr);
    if (!itemDB.exists() || itemDB.isDirectory()) {
      IMSUtil.writeObject(new ItemMap(), itemDB);
    }
    return itemDB;
  }

}

/**
 * Wrap the generic HashMap to get away from trivial warning.
 */
class ItemMap extends HashMap<String, Item> implements Serializable {

}


