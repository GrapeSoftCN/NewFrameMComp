package Test;

import common.java.httpServer.booter;
import common.java.nlogger.nlogger;

public class TestMComp {
    public static void main(String[] args) {
        booter booter = new booter();
        try {
            System.out.println("MComp");
            System.setProperty("AppName", "MComp");
            booter.start(1007);
        } catch (Exception e) {
            nlogger.logout(e);
        }
    }
}
