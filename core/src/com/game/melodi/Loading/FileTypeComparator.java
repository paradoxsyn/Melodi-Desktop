package com.game.melodi.Loading;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Paradox on 6/7/2017.
 */

public class FileTypeComparator implements Comparator<File> {
    /** Must be JRE 1.8 or later**/
    @Override
    public int compare(File file1, File file2){
        if(file1.isDirectory() && file2.isFile())
            return -1;
        if(file1.isDirectory() && file2.isDirectory()) {
            return 0;
        }
        if(file1.isFile() && file2.isFile()){
            return 0;
        }
        return 1;
    }
}
