package IMS.Controller;

import java.io.InputStreamReader;
import java.util.Objects;

public class ControllerImpl {

    private Readable rd;
    private Appendable ap;

    public ControllerImpl() {
        this(new InputStreamReader(System.in), System.out);
    }

    public ControllerImpl(Readable rd, Appendable ap) {
        Objects.requireNonNull(rd, "Input source is null!");
        Objects.requireNonNull(ap, "Output source is null!");
        this.rd = rd;
        this.ap = ap;
    }


}
