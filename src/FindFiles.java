/*
* Riyaz Shaikh | 20616383 | r4shaikh | CS349 A1
*  */

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.getProperty;

public class FindFiles {
    public static final String helpOption = "-help";
    public static boolean recursive = false;
    public static boolean dir = false;
    public static boolean ext = false;
    public static boolean reg = false;
    public static String [] extensions = null;
    static String [] validOptions = {"-help", "-reg", "-r", "-dir", "-ext"};
    public static boolean fileFound = false;

    public static void main(String[] args) {

        FindFiles.recursive = false;

        String dirParam = null;

        // check if there's a minimum number of options
        if(args.length == 0) {
            printNoFileError();
            printHelp();
            System.exit(0);
        }
        // print help if the user asks for help
        else if (Arrays.asList(args).contains(FindFiles.helpOption)){
                printHelp();
                System.exit(0);
        }
        // check if file passed
        else if (args[0].startsWith("-")){
            printNoFileError();
            printHelp();
            System.exit(0);
        }

        String fileToFind = args[0];

        // Parse options and prepare for execution of finding files
        for(int i = 1; i < args.length; ++i){

            if(args[i].equals("-reg")){
                FindFiles.reg = true;
            }
            else if(args[i].equals("-r")){
                FindFiles.recursive = true;
            }
            else if(args[i].equals("-dir")){
                FindFiles.dir = true;
                if(args.length > i + 1 && !Arrays.asList(FindFiles.validOptions).contains(args[i+1])){
                    dirParam = args[i+1];
                } else {
                    System.out.println("Error: please supply a directory. See -help option for more details.");
                    System.exit(0);
                }
                ++i;
            }
            else if(args[i].equals("-ext")){
                FindFiles.ext = true;
                if(args.length > i + 1 && !Arrays.asList(FindFiles.validOptions).contains(args[i+1])){
                    //extract extensions
                    FindFiles.extensions = args[i+1].split(",");
                } else {
                    System.out.println("Error: please supply arguments for -ext option. See -help option for more details.");
                    System.exit(0);
                }
                ++i;
            } else if (args[i].startsWith("-") || !args[i].startsWith("-")) {
                //case $> java FindFiles x s or $> java FindFiles x -s or $> java FindFiles x -dir d g
                printInvalidOptions(args[i]);
                System.exit(0);
            } else {
                // catch all errors
                System.out.println("Error: please see usage details below.");
                printHelp();
                System.exit(0);
            }
        }

        // Execute functionality
        // At this point all errors have been checked for and we are good to go
        findMatches(dirParam,fileToFind);
        if(!FindFiles.fileFound){
            System.out.println("Unable to find file: " + fileToFind);
        }

    }


    static void findMatches(String dirName, String fileToFind) {
        File directory;
        if(dirName != null) {
            directory = new File(dirName);
        }
        else {
            directory = new File("./");
        }

        File[] allFiles = directory.listFiles();

        // Progress bar
        System.out.println("Searching for file in " + directory.getAbsolutePath());

        if(allFiles != null) {
            // extensions case
            if(FindFiles.extensions != null) {
                    for (File file : allFiles) {
                            if (file.isFile()) {
                                for(String extension: FindFiles.extensions){
                                    if(file.getName().matches( fileToFind + "." + extension + "$")) {
                                        System.out.println("    File found here: " + file.getAbsolutePath());
                                        FindFiles.fileFound = true;
                                    }
                                }
                            } else if (file.isDirectory() && file.canRead() && FindFiles.recursive) {
                                findMatches(file.getAbsolutePath(), fileToFind);
                            } else if(!file.canRead()) {
                                System.out.println("You do not have permissions to access folder: '" + file.getName() + "'. Skipping Folder.");
                            }
                }
            } else {
                for (File file : allFiles) {
                        if (file.isFile()) {
                            if(file.getName().matches(fileToFind)){
                                System.out.println("    File found here: " + file.getAbsolutePath());
                                FindFiles.fileFound = true;
                            }
                        } else if (file.isDirectory() && file.canRead() && FindFiles.recursive) {
                            findMatches(file.getAbsolutePath(), fileToFind);
                        } else if(!file.canRead()){
                            System.out.println("You do not have permission to access folder: '" + file.getName() + "'. Skipping Folder.");
                        }
                }
            }
        }
    }

    static void printInvalidOptions(String invalidOption) {
        System.out.println("Error: '"+invalidOption + "' is an invalid option. \nPlease supply one of the following valid options: " +
                "-help, -dir, -r, -reg, -ext. Use the -help option for more details on the options.");
    }

    static void printNoFileError(){
        System.out.println("Error: please enter the file you wish to search for. Usage details below.");
    }

    static void printHelp() {
        System.out.println("Usage :: Java FindFiles filetofind [-option arg(s)]" +
                "\n-help " +
                "\n    Display this help page." +
                "\n-reg" +
                "\n    Find the files using [filetofind] argument as a regular expression." +
                "\n-r" +
                "\n    Find the files in subfolders." +
                "\n-dir [directory]" +
                "\n    Find the files in the specified directory [directory]. Default directory is the calling directory." +
                "\n-ext [ext1,ext2,...]" +
                "\n    Find the files matching [filetofind] and with the given extensions [ext1, ext2, ...].");
    }
}
