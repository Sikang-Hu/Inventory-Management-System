package IMS;

import IMS.Model.CategoryDTO;
import IMS.Model.ItemDTO;
import IMS.Model.RetailStoreDTO;
import IMS.Model.VendorDTO;

public class NaivePrinter {

    public String printCategory(CategoryDTO... cat) {
        StringBuilder sb = new StringBuilder();
        sb.append("Category: \n");
        for (CategoryDTO c : cat) {
            sb.append("ID: ").append(c.getCategoryID()).append("\n")
                    .append("Name: ").append(c.getCategoryName()).append("\n")
                    .append("Description: ").append(c.getCategoryDescription()).append("\n");
        }
        return sb.toString();
    }

    public String printItem(ItemDTO... item) {
        StringBuilder sb = new StringBuilder();
        sb.append("Item: \n");
        for (ItemDTO i : item) {
            sb.append("ID: ").append(i.getItemId()).append("\n")
                    .append("Name: ").append(i.getItemName()).append("\n")
                    .append("Category: ").append(i.getCategoryName()).append("\n")
                    .append("MSRP: ").append(i.getItemPrice()).append("\n");
        }
        return sb.toString();
    }

    public String printVendor(VendorDTO... vendor) {
        StringBuilder sb = new StringBuilder();
        sb.append("Vendor: \n");
        for (VendorDTO v : vendor) {
            sb.append("ID: ").append(v.getVendorID()).append("\n")
                    .append("Name: ").append(v.getVendorName()).append("\n")
                    .append("Address: ").append(v.getVendorAddress()).append(" ")
                    .append(v.getVendorState()).append(" ").append(v.getVendorZip()).append("\n")
                    .append("Description: ").append(v.getVendorDescription()).append("\n");
        }
        return sb.toString();
    }

    public String printStore(RetailStoreDTO... store) {
        StringBuilder sb = new StringBuilder();
        sb.append("Store: \n");
        for (RetailStoreDTO s : store) {
            sb.append("ID: ").append(s.getStoreId())
                    .append("\n")
                    .append("Address: ").append(s.getStoreAddress()).append(" ")
                    .append(s.getStoreState()).append(" ").append(s.getStoreZip()).append("\n");

        }
        return sb.toString();
    }

}
