package IMS.Controller;

import IMS.IMSException;
import IMS.Model.*;
import IMS.NaivePrinter;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControllerImpl implements Controller {
    private BufferedReader input;
    private Appendable ap;
    private InventoryModel model;
    private NaivePrinter pr;

    private static int bufLen = 128;
    private static String lineSeparator = System.getProperty("line.separator");


    private HashMap<Command.Type, Consumer<String[]>> commands = new HashMap<>();

    public ControllerImpl(InventoryModel model) {
        this(new InputStreamReader(System.in), System.out, model, new NaivePrinter());
    }

    public ControllerImpl(Reader rd, Appendable ap, InventoryModel model, NaivePrinter pr) {
        Objects.requireNonNull(rd, "Input source is null!");
        Objects.requireNonNull(ap, "Output source is null!");
        this.input = new BufferedReader(rd, bufLen);
        this.ap = ap;
        this.model = model;
        this.pr = pr;
    }

    public void insertCat(String... cat) {
        illegalArgs(2, cat);
        this.model.insertCategory(cat[0], cat[1]);
    }

    public void getCat(String... cat) {
        CategoryDTO category;
        illegalArgs(1, cat);
        try {
            int i = Integer.parseInt(cat[0]);
            category = this.model.getCatByName(i);
        } catch (NumberFormatException e) {
            category = this.model.getCatByName(cat[0]);
        }
        this.appendTo(this.pr.printCategory(category));
    }

    /**
     * Insert an item into the system, the clients need to specify its name, category, unit price.
     *
     */
    @Override
    public void insertItem(String... item) {
        illegalArgs(3, item);
        try {
            this.model.insertItem(item[0], item[1], Double.parseDouble(item[2]));
            this.appendTo("Item: %s has been inserted!\n", item[0]);
        } catch (NumberFormatException e) {
            throw new IMSException(e.getMessage());
        }
    }




    public void queryItem(String... name) {
        ItemDTO item;
        illegalArgs(1, name);
        try {
            int i = Integer.parseInt(name[0]);
            item = this.model.getItemByID(i);
        } catch (NumberFormatException e) {
            item = this.model.getItemByName(name[0]);
        }
        this.appendTo(this.pr.printItem(item));

    }

    public void insertVendor(String... vendor) {
        illegalArgs(5, vendor);
        this.model.insertVendor(vendor[0], vendor[1], vendor[2], Integer.parseInt(vendor[3]), vendor[4]);

    }

    public void getVendor(String... vendor) {
        VendorDTO vendorDTO;
        illegalArgs(1, vendor);
        try {
            int i = Integer.parseInt(vendor[0]);
            vendorDTO = this.model.getVendorByID(i);
        } catch (NumberFormatException e) {
            vendorDTO = this.model.getVendorByName(vendor[0]);
        }
        this.appendTo(pr.printVendor(vendorDTO));
    }


    // For user-friendly accept input <name> <cat> <price>
    public void addSoldItem(String... item) {
        illegalArgs(2, item);
        try (BufferedReader rd = new BufferedReader(new FileReader(item[1]))) {
            List<ItemDTO> l = new ArrayList<>();
            Pattern format = Pattern.compile(Command.assemble(Command.stringArg, Command.stringArg, Command.doubleArg));
            String ln;
            while ((ln = rd.readLine()) != null) {
                Matcher m = format.matcher(ln);
                if (m.matches()) {
                    l.add(new ItemDTO(m.group(2), m.group(1), Double.parseDouble(m.group(3))));
                }
            }
            this.model.addSoldItem(item[0], l);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
    }

    public void getSoldItem(String... vendor) {
        illegalArgs(1, vendor);
        Set<ItemDTO> set = this.model.getSoldItems(vendor[0]);
        this.appendTo(this.pr.printItem(set.toArray(new ItemDTO[0])));
    }



    public void insertStore(String... store) {
        illegalArgs(3, store);
        this.model.insertStore(store[0], store[1], Integer.parseInt(store[2]));
    }

    // TODO: get all stores
    public void getStore(String... store_id) {
        illegalArgs(1, store_id);
        int i = Integer.parseInt(store_id[0]);
        RetailStoreDTO store = this.model.getStoresByID(i);
        this.appendTo(this.pr.printStore(store));
    }


    // TODO: more general
    public void getStatus(String... para) {
        List<Status> list = this.model.getInvStatus();
        for (Status s : list) {
            this.appendTo(s.toString());
            this.appendTo("\n");
        }

    }

    // TODO: split to regex which
    public void insertTransaction(String... trans) {
        illegalArgs(2, trans);
        try (BufferedReader rd = new BufferedReader(new FileReader(trans[1]))) {
            List<ItemInTransaction> items = new ArrayList<>();
            Pattern format = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(-?\\d+(?:\\.\\d{0,2})?)$");
            String ln = rd.readLine();
            String[] order = ln.split("\\s+");
            Date date = null;
            try {
                date = new SimpleDateFormat("mm/dd/yyyy").parse(order[3]);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new IMSException(e.getMessage());
            }
            while ((ln = rd.readLine()) != null) {
                Matcher m = format.matcher(ln);
                if (m.matches()) {
                    items.add(new ItemInTransaction(Integer.parseInt(m.group(1)),
                            Integer.parseInt(m.group(2)), Double.parseDouble(m.group(3))));
                }
            }
            if (trans[0].equals("order")) {
                this.model.insertOrder(order[0], Integer.parseInt(order[1]), date, items);
            } else if (trans[1].equals("sale")) {
                this.model.insertSale(Integer.parseInt(order[0]), Integer.parseInt(order[1]), date, items);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IMSException(e.getMessage());
        }
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
    private void illegalArgs(int size, String[] args) {
        if (args.length != size) {
            throw new IMSException("Invalid number of input!\n");
        }
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
        commands.put(Command.Type.INSERT_CATEGORY, this::insertCat);
        commands.put(Command.Type.GET_CATEGORY, this::getCat);


        commands.put(Command.Type.GET_ITEM, this::queryItem);
        commands.put(Command.Type.INSERT_ITEM, this::insertItem);


        commands.put(Command.Type.INSERT_VENDOR, this::getVendor);
        commands.put(Command.Type.GET_VENDOR, this::getVendor);
        commands.put(Command.Type.GET_SOLD_ITEM, this::getSoldItem);

        commands.put(Command.Type.INSERT_TRANSACTION, this::insertTransaction);

        commands.put(Command.Type.INSERT_SALE, this::insertVendor);
        commands.put(Command.Type.GET_SALE, this::insertVendor);
        commands.put(Command.Type.STATUS, this::getStatus);

        commands.put(Command.Type.EXIT, i->System.exit(0));
        commands.put(Command.Type.QUIT, i->System.exit(1));
    }


}

