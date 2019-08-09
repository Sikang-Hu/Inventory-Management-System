package IMS.Model;

public class RetailStoreDTO {
  private int storeId;
  private String storeAddress;
  private String StoreState;
  private String storeZip;


  public RetailStoreDTO(int storeId, String storeAddress, String StoreState, String storeZip) {
    this.storeId = storeId;
    this.storeAddress = storeAddress;
    this.StoreState = StoreState;
    this.storeZip = storeZip;
  }

  public RetailStoreDTO(String storeAddress, String StoreState, String storeZip) {
    this.storeId = -1;
    this.storeAddress = storeAddress;
    this.StoreState = StoreState;
    this.storeZip = storeZip;
  }

  @Override
  public String toString() {
    return "Doctor{" +
            "storeId=" + storeId +
            ", storeAddress='" + storeAddress + '\'' +
            ", StoreState='" + StoreState + '\'' +
            ", storeZip=" + storeZip +
            '}';
  }

  public int getStoreId() {
    return this.storeId;
  }

  private void setStoreId(int storeId) {
    this.storeId = storeId;
  }


  public String getStoreAddress() {
    return storeAddress;
  }

  public void setStoreAddress(String storeAddress) {
    this.storeAddress = storeAddress;
  }

  public String getStoreState() {
    return StoreState;
  }

  public void setStoreState(String StoreState) {
    this.StoreState = StoreState;
  }

  public String getStoreZip() {
    return storeZip;
  }

  public void setStoreZip(String storeZip) {
    this.storeZip = storeZip;
  }
}
