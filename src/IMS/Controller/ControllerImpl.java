package IMS.Controller;

import IMS.IMSException;
import IMS.Model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.function.Consumer;

public class ControllerImpl implements Controller {
    private BufferedReader input;
    private Appendable ap;
    private InventoryModel model;
    private static int bufLen = 128;
    private static String lineSeparator = System.getProperty("line.separator");
    private HashMap<Command.Type, Consumer<String[]>> commands = new HashMap<>();

    public ControllerImpl(InventoryModel model) {
        this(new InputStreamReader(System.in), System.out, model);
    }

    public ControllerImpl(Reader rd, Appendable ap, InventoryModel model) {
        Objects.requireNonNull(rd, "Input source is null!");
        Objects.requireNonNull(ap, "Output source is null!");
        this.input = new BufferedReader(rd, bufLen);
        this.ap = ap;
        this.model = model;
    }

    /**
     * Insert an item into the system, the clients need to specify its name, category, unit price.
     *
     */
    @Override
    public void insertItem(String... item) {
        if (illegalArgs(3, item)) {
            return;
        }
        try {
            this.model.insertItem(item[0], item[1], Double.parseDouble(item[2]));
            this.appendTo("Item: %s has been inserted!\n", item[0]);
        } catch (NumberFormatException e) {
            throw new IMSException(e.getMessage());
        }
    }


    public void queryItem(String... name) {
        if (illegalArgs(1, name)) {
            return;
        }

        Item i = this.model.getItem(name[0]);
        this.appendTo(i.toString());

    }

    public void insertVendor(String... vendor) {
        if (illegalArgs(5, vendor)) {
            return;
        }
        this.model.insertVendor(vendor[0], vendor[1], vendor[2], Integer.parseInt(vendor[3]), vendor[4]);

    }

    public void getSoldItem(String... vendor) {
        if (illegalArgs(1, vendor)) {
            return;
        }
        Set<Item> set = this.model.getSoldItems(vendor[0]);
        for (Item i : set) {
            this.appendTo(i.toString());
            this.appendTo("\n");
        }
    }

    public void getVendor(String... vendor) {
        if (illegalArgs(1, vendor)) {
            return;
        }
        List<Vendor> vendors = this.model.getVendor(vendor[0]);
        for (Vendor v : vendors) {
            this.appendTo(v.toString());
            this.appendTo("\n");
        }
    }

    public void insertStore(String... store) {
        if (illegalArgs(3, store)) {
            return;
        }
        this.model.insertStore(store[0], store[1], Integer.parseInt(store[2]));
    }

    public void getStore(String... store) {
        if (illegalArgs(1, store)) {
            return;
        }
        List<RetailStore> list = this.model.getStores(store[0]);
        for (RetailStore r : list) {
            this.appendTo(list.toString());
        }
    }

    // TODO: a individual class for INVENTORY STATUS?
    public void getStatus(String... para) {
        List<Status> list = this.model.getInvStatus();
        for (Status s : list) {
            this.appendTo(s.toString());
            this.appendTo("\n");
        }

    }

    public void insertOrder(String... path) {

    }

    /**
     * Activate the command user interface processor of this inventory management system.
     */
    public void commandUserInterface() {
        this.appendTo("Inventory Management System v 0.1.0 :\r\n");
        while (true) {
            String ln;
            try {
                ln = this.input.readLine();
                Command c = Command.parseCommand(ln);
                commands.get(c.getType()).accept(c.getOperands());
            } catch (IOException e) {
                this.appendTo(e.getMessage());
                System.exit(1);
            } catch (IMSException ie) {
                this.appendTo(ie.getMessage());
                this.appendTo("\n");
            } catch (NumberFormatException e) {
                e.printStackTrace();
                this.appendTo(e.getMessage());
            }
        }
    }

    /**
     * Check whether the given input has correct number of input
     * @param size
     * @param args
     * @return
     */
    private boolean illegalArgs(int size, String[] args) {
        if (args.length != size) {
            this.appendTo("Invalid number of input!\n");
            return true;
        }
        return false;
    }


    private void appendTo(String str) {
     try {
         this.ap.append(str);
     } catch (IOException e) {
         throw new IMSException(e.getMessage());
     }
    }

    private void appendTo(String format, Object... str) {
        try {
            this.ap.append(String.format(format, str));
        } catch (IOException e) {
            throw new IMSException(e.getMessage());
        }
    }

    {
        commands.put(Command.Type.GET_ITEM, this::queryItem);
        commands.put(Command.Type.INSERT_ITEM, this::insertItem);


        commands.put(Command.Type.INSERT_VENDOR, this::getVendor);
        commands.put(Command.Type.GET_VENDOR, this::getVendor);
        commands.put(Command.Type.GET_SOLD_ITEM, this::getSoldItem);

        commands.put(Command.Type.INSERT_SALE, this::insertVendor);
        commands.put(Command.Type.GET_SALE, this::insertVendor);
        commands.put(Command.Type.STATUS, this::getStatus);

        commands.put(Command.Type.EXIT, i->System.exit(0));
        commands.put(Command.Type.QUIT, i->System.exit(1));
    }

}

