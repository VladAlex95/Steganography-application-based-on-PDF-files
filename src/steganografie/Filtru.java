package steganografie;

import java.io.File;
import javax.swing.filechooser.FileFilter;


public class Filtru extends FileFilter{
    private final String[] okFileExtensions = new String[] { "jpg", "jpeg", "png", "gif" };

    @Override
    public boolean accept(File f) {
        if(f.isDirectory())
            return true;
        for (String extension : okFileExtensions) {
            if (f.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "doar imagini";
    }

    
}
