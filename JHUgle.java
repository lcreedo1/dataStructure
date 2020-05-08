/**
 * Liam Creedon, lcreedo1, lcreedo1@jhu.edu
 * Kevin Cameron, kcamer12, kcamer12@jhu.edu
 */

package hw8;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.io.FileNotFoundException;

/**
 * Basic search engine class.
 * Maps urls and keywords to a HashMap.
 * User inputs can be queried to produce specific searches.
 */
public final class JHUgle {

    /** Make checkstyle happy. */
    private JHUgle() { }

    /**
     * Fills HashMap with content from file.
     * @param filename Name of file.
     * @param e HashMap to be filled.
     * @return Filled HashMap.
     */
    private static HashMap<String, ArrayList<String>> fillFile(String filename,
        HashMap<String, ArrayList<String>> e) {
        try {
            File jhugle = new File(filename);
            Scanner fs = new Scanner(jhugle);

            // read through text file finding all search keywords
            // map urls to each keyword in a HashMap
            while (fs.hasNextLine()) {

                String url = fs.nextLine();
                String words = fs.nextLine();

                // array list holding keys as split from 'words'
                ArrayList<String> keys =
                    new ArrayList<String>(Arrays.asList(words.split(" ")));

                // array list of urls to be put in engine
                ArrayList<String> urls = new ArrayList<String>();
                urls.add(url);

                // adds each keyword in keys
                for (String k : keys) {
                    if (!e.has(k)) { // k is a new entry
                        e.insert(k, urls);
                    }
                    else { // k already exists

                        // urls already associated with k
                        ArrayList<String> temp = e.get(k);

                        // add newest url to new list
                        temp.add(url);

                        // add updated list and key to HashMap
                        e.put(k, temp);
                    }
                }
            }
            fs.close();
            return e;

        } catch (FileNotFoundException ex) {
            System.out.println("File not found.");
        }
        return null;
    }

    /**
     * Main johnson.
     * @param args Command line arguments containing data file.
     * @throws Exception if command line does not contain a file.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("Command line must contain file"
                + " name.");
        }

        // initializes scanner, File, and HashMap
        String file = args[0];
        HashMap<String, ArrayList<String>> engine =
            new HashMap<String, ArrayList<String>>();

        engine = fillFile(file, engine);
        System.out.println("Index Created");


        // collect users input from keyboard
        // run while input is not !
        Scanner sc = new Scanner(System.in);
        String in = " ";
        int numSearches = 0;
        boolean run = true;
        Set<String> one = new TreeSet<String>();
        Set<String> two = new TreeSet<String>();
        System.out.print("> ");

        while (run && sc.hasNext()) {

            in = sc.next();
            switch (in) {

                case "!" :
                    run = false;
                    break;

                case "?" :
                    if (!one.isEmpty()) {
                        for (String val : one) {
                            System.out.println(val);
                        }
                        one.clear();
                        two.clear();
                        numSearches = 0;
                        System.out.print("> ");
                    } else {
                        //System.err.println("Stack is empty.");
                        System.out.print("> ");
                    }
                    break;

                case "&&" :
                    if (numSearches > 1) {
                        Set<String> and = new TreeSet<String>(one);
                        and.retainAll(two);
                        one = and;
                        System.out.print("> ");
                    } else {
                        //System.err.println("Not enough arguments "
                        //+ "for function call.");
                        System.out.print("> ");
                    }
                    break;

                case "||" :
                    if (numSearches > 1) {
                        Set<String> or = new TreeSet<String>(one);
                        or.addAll(two);
                        one = or;
                        System.out.print("> ");
                    }  else {
                        //System.err.println("Not enough arguments "
                        //+ "for function call.");
                        System.out.print("> ");
                    }
                    break;

                default:
                    try {
                        ArrayList<String> found = engine.get(in);
                        if (numSearches == 0) {
                            for (String item : found) {
                                one.add(item);
                            }
                        }
                        for (String item : found) {
                            two.add(item);
                        }
                        one.addAll(two);
                        numSearches++;
                        System.out.print("> ");
                    } catch (IllegalArgumentException e) {
                        //System.err.println("No urls found for that keyword.");
                        System.out.print("> ");
                    }
                    break;
            }
        }
        sc.close();
    }
}
