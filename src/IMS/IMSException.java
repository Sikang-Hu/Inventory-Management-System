package IMS;

/**
 * This class represents exceptions indicating some kind of user error.
 */
public class IMSException extends RuntimeException{

  public IMSException() {}


    public IMSException(String msg) {
        super(msg);
    }

    public IMSException(String format, Object... objs) {
        super(String.format(format, objs));
    }
}

