package IMS;

import java.io.*;

/**
 * This class stores all the facilities for the Inventory Management System.
 */
public class IMSUtil {


    /**
     * Write OBJ to given file F, the object should be Serializable
     * @param obj
     * @param f
     */
    public static void writeObject(Object obj, File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(obj);
            o.close();
        } catch (IOException e) {
            throw new IMSException(e.getMessage());
        }
    }

    /**
     * Read a object of T from given file File
     * @param file
     * @param expectedClass
     * @param <T>
     * @return
     */
    public static <T extends Serializable> T readObject(File file, Class<T> expectedClass){
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new BufferedInputStream(
                            new FileInputStream(file)));
            T result = expectedClass.cast(in.readObject());
            in.close();
            return result;
        } catch (ClassNotFoundException | IOException
        | ClassCastException e) {
            throw new IMSException(e.getMessage());
        }
    }

}
