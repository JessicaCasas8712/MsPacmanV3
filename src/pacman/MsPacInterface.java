package pacman;

import utilities.JEasyFrame;
import utilities.ElapsedTimer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import stats.StatisticalSummary;

/**
 * User: Simon
 * Date: 03-Mar-2007
 * Time: 08:50:37
 *
 *  This class computes the distance to the nearest wall in each direction.
 *
 *  This allows the PacMan to see which way it could go -
 *  so if the distance from the pacman to the nearest wall in any direction was
 *  below a threshold, then that would not be a feasible direction.
 *
 *  This is done with a OneD array, is to have offsets of
 * +/- 1 and +/- width
 *  too close in any particular direction, then that would be detected
 */

public class MsPacInterface {
    // delay between each screen capture
    static int delay = 10;
    static boolean display = true;
    static int[] rect = new int[4];
    public static void main(String[] args) throws Exception {
    	 String windowName = "Ms. Pac-Man";
         
         try {
            rect = GetWindowRect.getRect(windowName);
            top =  rect[1] + 57;
            left = rect[0] + 4;;
            System.out.println(top + " - " + left);
         } catch (GetWindowRect.WindowNotFoundException e) {
            e.printStackTrace();
         } catch (GetWindowRect.GetWindowRectException e) {
            e.printStackTrace();
         }
        MsPacInterface ms = new MsPacInterface();
        StatisticalSummary ss = new StatisticalSummary();
        PacMover pm = new PacMover();
        DirectionComponent dc = DirectionComponent.easyUse();
        PacAgent pa = new LeftRight();

        TestMonitor tm = new TestMonitor();
        while(true) {
            ElapsedTimer t = new ElapsedTimer();

            int[] pix = ms.getPixels();

            ms.analyseComponents(pix);
            // System.out.println(pix.length);
            ss.add(t.elapsed());
            int action = ms.ce.gs.agent.move(ms.ce.gs);
            // System.out.println(t);
            // int action = pa.move(ms.ce.gs);
            pm.move(action);
            tm.log(action, ms.ce.gs);
            if (display) dc.update(action);
            Thread.sleep(delay);
            // pm.randMove();0
        }
        // System.out.println(ss);
    }

    public void analyseComponents(int[] pix) {
        ce.gs.reset();
        ArrayList<Drawable> al = ce.consume(pix, colors);
        // System.out.println("Components " + al);

        if (display) sd.updateObjects(al);
    }

    static int top;//270;
    static int left;  //570;
    public static int width = 223;
    public static int height = 275;
    int[] pixels;
    Robot robot;
    SimpleExtractor ce;
    SimpleDisplay sd;

    // these are the integers corresponding to the pixel colours for each game object
    static int blinky = -65536;
    static int pinky = -18689;
    static int inky = -16711681;
    static int sue = -18859;
    static int pacMan = -256;
    static int edible = -14408449;
    static int pill = -2434305;

    static HashSet<Integer> colors = new HashSet<Integer>();
    static {
        colors.add(blinky);
        colors.add(pinky);
        colors.add(inky);
        colors.add(sue);
        colors.add(pacMan);
        colors.add(edible);
        colors.add(pill);
    }

    public MsPacInterface() throws Exception {
        robot = new Robot();
        pixels = new int[width * height];
        ce = new SimpleExtractor(width, height);
        sd = new SimpleDisplay(width, height);
        new JEasyFrame(sd, "Extracted", true);
    }

    public int[] getPixels() {
        BufferedImage im = robot.createScreenCapture(new Rectangle(left, top, width, height));
        im.getRGB(0, 0, width, height, pixels, 0, width);
        return pixels;
    }
}
