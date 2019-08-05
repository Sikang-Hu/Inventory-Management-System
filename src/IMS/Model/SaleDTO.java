package IMS.Model;

import java.util.Date;


public class SaleDTO {
  private int sale_id;
  private Integer customer_id;
  private int store_id;
  private Date saleDate;


  SaleDTO(int sale_id, int store_id, Integer customer_id, Date date) {
    this.sale_id = sale_id;
    this.customer_id = customer_id;
    this.store_id = store_id;
    this.saleDate = date;
  }

  SaleDTO(int store_id, Integer customer_id, Date date) {
    this(-1, store_id, customer_id, date);
  }

  public int getSale_id() {
    return sale_id;
  }

  public int getStore_id() {
    return store_id;
  }

  public Date getSaleDate() {
    return saleDate;
  }

  public Integer getCustomer_id() {
    return customer_id;
  }

  public void setStore_id(int store_id) {
    this.store_id = store_id;
  }

  public void setCustomer_id(Integer customer_id) {
    this.customer_id = customer_id;
  }

  public void setSale_id(int sale_id) {
    this.sale_id = sale_id;
  }

  public void setSaleDate(Date saleDate) {
    this.saleDate = saleDate;
  }
}
