package IMS.Model;

public class RetailStore {



  private int storeId;
  private String storeAddress;
  private String StoreState;
  private int storeZip;


    public RetailStore(int storeId, String storeAddress, String StoreState, int storeZip) {
      this.storeId = storeId;
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
      return storeId;
    }
    private void setStoreId(int storeId){
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
  public int getStoreZip() {
    return storeZip;
  }

  public void setStoreZip(int storeZip) {
    this.storeZip = storeZip;
  }




}
