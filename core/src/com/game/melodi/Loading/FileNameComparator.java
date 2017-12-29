package com.game.melodi.Loading;

import java.io.File;
import java.util.Comparator;

/**
 * Created by Paradox on 6/7/2017.
 */

public class FileNameComparator implements Comparator<File> {
    /** Must be JRE 1.8 or later**/
    @Override
    public int compare(File file1, File file2){
        return String.CASE_INSENSITIVE_ORDER.compare(file1.getName(),file2.getName());
    }
}
