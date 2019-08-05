package IMS.Model;

import java.util.Date;

public class SupplyOrderDTO {
  private int order_id;
  private int ven_id;
  private int store_id;
  private Date order_date;
  private Date delivery_date;


  public SupplyOrderDTO( int order_id, int vendor, int store,
                      Date order_date, Date delivery_date) {
    this.order_id = order_id;
    this.store_id = store;
    this.ven_id = vendor;
    this.order_date = order_date;
    this.delivery_date = delivery_date;
  }

  public SupplyOrderDTO( int order_id, int vendor, int store, Date order_date) {
    this(order_id, vendor, store, order_date, null);
  }


  public SupplyOrderDTO( int vendor, int store, Date order_date) {
    this(-1, vendor, store, order_date, null);
  }

  public Date getDelivery_date() {
    return delivery_date;
  }

  public Date getOrder_date() {
    return order_date;
  }

  public int getOrder_id() {
    return order_id;
  }

  public int getStore_id() {
    return store_id;
  }

  public int getVen_id() {
    return ven_id;
  }

  public void setDelivery_date(Date delivery_date) {
    this.delivery_date = delivery_date;
  }

  public void setOrder_date(Date order_date) {
    this.order_date = order_date;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public void setStore_id(int store) {
    this.store_id = store;
  }

  public void setVen_id(int vendor) {
    this.ven_id = vendor;
  }
}
