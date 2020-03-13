package gopur.uiFunc.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import java.io.File;

/**
 * Created by TPride on 2020/2/16.
 */
public final class Zip {
    public static boolean isZip(File file) {
        if (!file.exists() || file.isDirectory() || (file.isFile() && !file.getName().contains("."))) return false;
        return file.getName().contains(".") ? file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).equals("zip") : false;
    }

    public static boolean isValidZip(File file) {
        try {
            return new ZipFile(file).isValidZipFile();
        } catch (ZipException e) {
            return false;
        }
    }

    public static boolean isEncrypted(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || file.isDirectory())
                return false;
            ZipFile zipFile = new ZipFile(file);
            if (zipFile.isValidZipFile())
                return zipFile.isEncrypted();
            return false;
        } catch (ZipException e) {
            return false;
        }
    }

    public static boolean zip(String srcFile, String dest, String password) {
        try {
            File srcfile = new File(srcFile);
            String destname = destFileName(srcfile, dest);

            ZipParameters par = new ZipParameters();
            par.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            par.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            ZipFile zipfile = new ZipFile(destname);
            zipfile.setFileNameCharset("UTF-8");

            if (password != null) {
                par.setEncryptFiles(true);
                par.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
                par.setPassword(password);
            }

            if (srcfile.isDirectory())
                zipfile.addFolder(srcfile, par);
            else
                zipfile.addFile(srcfile, par);
            return true;
        } catch (ZipException e) {
            return false;
        }
    }

    public static boolean unzip(String zipFile, String destino, String password) {
        try {
            File file = new File(zipFile);
            if (!file.exists() || file.isDirectory())
                return false;
            ZipFile zFile = new ZipFile(zipFile);
            zFile.setFileNameCharset("UTF-8");

            if (!zFile.isValidZipFile()) {
                return false;
            }

            if (new File(destino).isFile()) {
                return false;
            }

            if (zFile.isEncrypted()) {
                if (password == null) {
                    return false;
                }
                zFile.setPassword(password);
            }
            zFile.extractAll(destino);
            return true;
        } catch (ZipException e) {
            return false;
        }
    }

    private static String destFileName(File srcFile, String destino) {
        if (destino == null) {
            if (!srcFile.isDirectory()) {
                String filename = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));
                destino = srcFile.getParent().concat(File.separator).concat(filename).concat(".zip");
            } else
                destino = srcFile.getParent().concat(File.separator).concat(srcFile.getName()).concat(".zip");
        } else {
            paths(destino);
            if (destino.endsWith(File.separator)) {
                String filename;

                if (srcFile.isDirectory())
                    filename = srcFile.getName();
                else
                    filename = srcFile.getName().substring(0, srcFile.getName().lastIndexOf("."));

                destino += filename.concat(".zip");
            }
        }
        return destino;
    }

    private static void paths(String dest) {
        File destDir;
        if (dest.endsWith(File.separator))
            destDir = new File(dest);
        else
            destDir = new File(dest.substring(0, dest.lastIndexOf(File.separator)));

        if (!destDir.exists())
            destDir.mkdirs();
    }
}