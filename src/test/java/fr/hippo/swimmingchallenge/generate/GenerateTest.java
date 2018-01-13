package fr.hippo.swimmingchallenge.generate;

import org.junit.Test;

/**
 * Created by hippolyte on 1/12/18.
 */
public class GenerateTest {

    public static final int NB_CRENEAU = 30;

    @Test
    public void writeTimeSlot() {

        int id = 0;

        System.out.println("id;start_time;end_time;payed;reserved;line;running;version");

        for (int hour = 8; hour < 13; hour++) {
            for (int min = 0; hour == 12 ? min < 15: min < 60; min+=15) {
                for (int cre = 1; cre <= NB_CRENEAU; cre++) {
                    String debut = (hour < 10 ? "0" : "") + hour + ":" + (min < 10 ? "0" : "") + min;
                    int heureFin = (min == 45 ? hour + 1: hour);
                    String fin = (heureFin < 10 ? "0" : "") + heureFin + ":" + (min == 45 ? "00" : min + 15);
                    boolean run = id < 6 * NB_CRENEAU;
                    System.out.println(id++ + ";" + debut + ";" + fin + ";0;0;" + cre + ";" + run + ";0");
                }
            }
        }
    }

}
