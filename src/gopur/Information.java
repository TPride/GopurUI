package gopur;

import java.io.File;

public interface Information {
    String NOWPATH = System.getProperty("user.dir").concat(File.separator);
    String VERSION = "0.0.7";
    String[] AUTHORS = { "TPride" };
}
