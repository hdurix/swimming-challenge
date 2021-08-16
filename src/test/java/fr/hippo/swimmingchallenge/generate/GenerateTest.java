package fr.hippo.swimmingchallenge.generate;

import org.junit.Test;

/**
 * Created by hippolyte on 1/12/18.
 */
public class GenerateTest {

    public static final int NB_CRENEAU = 20;

    @Test
    public void writeTimeSlot() {

        int id = 0;

        System.out.println("INSERT INTO public.timeslot (id, start_time, end_time, payed, reserved, line, running, version) VALUES");

        for (int hour = 16; hour < 19; hour++) {
            for (int min = 0; hour == 18 ? min < 45: min < 60; min+=15) {
                for (int cre = 1; cre <= NB_CRENEAU; cre++) {
                    String debut = (hour < 10 ? "0" : "") + hour + ":" + (min < 10 ? "0" : "") + min;
                    int heureFin = (min == 45 ? hour + 1: hour);
                    String fin = (heureFin < 10 ? "0" : "") + heureFin + ":" + (min == 45 ? "00" : min + 15);
                    boolean run = hour == 18 && min == 30;
                    System.out.println("(" + id++ + ",'" + debut + "','" + fin + "',false,false," + cre + "," + run + ",0),");
                }
            }
        }
    }

}
