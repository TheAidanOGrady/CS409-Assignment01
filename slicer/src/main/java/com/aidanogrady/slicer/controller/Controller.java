package com.aidanogrady.slicer.controller;

import com.aidanogrady.slicer.model.ClassInfo;
import com.aidanogrady.slicer.view.Input;
import com.aidanogrady.slicer.view.Output;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The controller class is the main hub of the system. It controls the flow of
 * the program, taking the user input and acting upon it.
 *
 * @author Aidan O'Grady
 * @since 0.1
 */
public class Controller {
    /**
     * The directory of the project to be analysed.
     */
    private File directory;

    /**
     * The list of files in the directory.
     */
    private List<File> files;

    /**
     * Constructor
     *
     * @param path - the path of the source code's directory.
     */
    public Controller(String path) {
        this.directory = new File(path);
        final String[] SUFFIX = {"java"};
        Collection<File> col = FileUtils.listFiles(directory, SUFFIX, true);
        files = new ArrayList<File>(col);
    }

    /**
     * Starts the controller.
     */
    public void start() {
        Output.print("You have chosen the directory:");
        Output.print(directory);
        Output.minorLineBreak();
        try {
            loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The main loop of the program, allowing the user to view different files
     * until they wish to quit the program.
     */
    private void loop() throws Exception {
        ClassInfo info = new ClassInfo();;
        File chosen;
        int lineNo;

        String name;
        int start;
        int end;
        while (true) {
            chosen = chooseFile();
            info.analyse(chosen);
            name = info.getClassName();
            start = info.getStartLine();
            end = info.getEndLine();
            Output.print("Slicing class: " + name);
            Output.print("Line: (" + start + "-" + end + "): ");
            lineNo = Input.getInteger();
            Output.print("Slicing " + name + " from line " + lineNo);
            Output.lineBreak();
        }
    }

    /**
     * Prompts the user to choose a file to be analysed.
     *
     * @return file to be analysed.
     */
    private File chooseFile() {
        Output.print("Which file would you like to analyse?");
        Output.print("Enter 0 if you would like to quit.");

        // Just come cleaning up to remove directory from list of files within
        // directory.
        List<String> fileList = new ArrayList<String>();
        for (File file : files) {
            String name = file.toString();
            name = name.replace(directory.toString(), "");
            fileList.add(name);
        }
        Output.printList(fileList);

        int response = 0;
        try {
            response = Input.getInteger();
            return files.get(response - 1);
        } catch(IndexOutOfBoundsException e) { // We got an invalid number
            if (response == 0) {
                Output.print("Goodbye.");
                System.exit(0);
            }
            Output.integerException(response + "");
            return chooseFile();
        }
    }
}
