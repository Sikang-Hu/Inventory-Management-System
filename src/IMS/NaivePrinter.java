package IMS;

import IMS.Model.CategoryDTO;
import IMS.Model.ItemDTO;
import IMS.Model.ItemInTransaction;
import IMS.Model.RetailStoreDTO;
import IMS.Model.SaleDTO;
import IMS.Model.Status;
import IMS.Model.SupplyOrderDTO;
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

    public String printItemInTranscation(ItemInTransaction... item) {
        StringBuilder sb = new StringBuilder();
        sb.append("Item: \n");
        for (ItemInTransaction i : item) {
            sb.append("ID: ").append(i.getItem_id()).append("\n")
                    .append("Quantity: ").append(i.getOrder_quantity()).append("\n")
                    .append("Price: ").append(i.getUnit_cost()).append("\n");
        }
        return sb.toString();
    }

    public String printOrder(SupplyOrderDTO ... orders) {
        StringBuilder sb = new StringBuilder();
        sb.append("Item: \n");
        for (SupplyOrderDTO i : orders) {
            sb.append("ID: ").append(i.getOrder_id()).append("\n")
                    .append("ven_id: ").append(i.getVen_id()).append("\n")
                    .append("store_id").append(i.getStore_id()).append("\n")
                    .append("order_date").append(i.getOrder_date()).append("\n")
                    .append("delivery_date").append(i.getDelivery_date()).append("\n");
        }
        return sb.toString();
    }

    public String printSale(SaleDTO ... sales) {
        StringBuilder sb = new StringBuilder();
        sb.append("Item: \n");
        for (SaleDTO i : sales) {
            sb.append("ID: ").append(i.getSale_id()).append("\n")
                    .append("customer_id: ").append(i.getCustomer_id()).append("\n")
                    .append("store_id").append(i.getStore_id()).append("\n")
                    .append("order_date").append(i.getSaleDate()).append("\n");
        }
        return sb.toString();
    }

    public String printStatus(Status... statuses) {
        StringBuilder sb = new StringBuilder();
        sb.append("Status: \n");
        for (Status i : statuses) {
            sb.append("Store: ").append(i.getStore()).append("\n")
                    .append("cat: ").append(i.getCat()).append("\n")
                    .append("item:").append(i.getItem()).append("\n")
                    .append("stock:").append(i.getStock()).append("\n");
        }
        return sb.toString();
    }

}
