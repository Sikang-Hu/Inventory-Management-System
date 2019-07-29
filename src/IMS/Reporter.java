package IMS;


import java.io.IOException;
import java.util.Scanner;

public class Reporter {

    private Scanner scan;
    private Appendable out;

    public Reporter(Readable rd, Appendable ap) {
        this.scan = new Scanner(rd);
        this.out = ap;
    }

    public void print() throws IOException {
        out.append(scan.nextLine());
    }
}
