package io.ycj28c.uitest.util;

import java.io.File;

public class DirectoryUtil {
	
	/**
	 * delete the empty directory
	 * @param dir: the target directory
	 */
	public static void doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
    }

    /**
     * recursively remove all the file and the files in sub folder
     * @param dir: the target directory
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete(); //delete the empty folder
    }

    /**
     * test
     * @param args
     */
    public static void main(String[] args) {
        doDeleteEmptyDir("new_dir1");
        String newDir2 = "new_dir2";
        boolean success = deleteDir(new File(newDir2));
        if (success) {
            System.out.println("Successfully deleted populated directory: " + newDir2);
        } else {
            System.out.println("Failed to delete populated directory: " + newDir2);
        }     
    }
}
