package utils;

import java.io.File;

public class MachineSearch {

  
  /**
   * Method to search for the given filename in a specific directory
   *
   * @param dir given dir to find the fileName
   * @param fileName the name of fileName which is in the given dir
   * @return location of the fileName
   */

  public String searchMachineForFile(String dir, String fileName) {
    String filePath = "";
    boolean found = false;
    File fileDir = new File(dir);
    File[] roots = fileDir.listFiles();
    for (int i = 0; i < roots.length; i++) {
      try {
        if (roots[i].listFiles() == null) {
          if (roots[i].isDirectory()) {
            String directory = roots[i].getAbsolutePath();
            filePath = searchDirectory(directory, fileName);
            if (!filePath.equals("")) {
              found = true;
              break;
            }
          }
          if (roots[i].getName().equals(fileName)) {
            try {
              filePath = roots[i].getCanonicalPath();
              found = true;
              break;
            } catch (Exception e) {
              System.err.println(e.getMessage());
            }
          }
        } else {
          for (File f : roots[i].listFiles()) {
            if (f.isDirectory()) {
              String directory = f.getAbsolutePath();
              filePath = searchDirectory(directory, fileName);
              if (!filePath.equals("")) {
                found = true;
                break;
              }
            }
            if (f.getName().equals(fileName)) {
              try {
                filePath = f.getCanonicalPath();
                found = true;
                break;
              } catch (Exception e) {
                System.err.println(e.getMessage());
              }
            }
          }
        }
      } catch (Exception e) {
        continue;
      }
      if (found) {
        break;
      }
    }
    return filePath;
  }


  public boolean checkIfFileExists(String dir, String fileName) {
    String filePath = "";
    boolean found = false;
    File fileDir = new File(dir);
    File[] roots = fileDir.listFiles();
    //      File[] roots = File.listRoots();
    for (int i = 0; i < roots.length; i++) {
      try {
        if (roots[i].listFiles() == null) {
          if (roots[i].isDirectory()) {
            String directory = roots[i].getAbsolutePath();
            filePath = searchDirectory(directory, fileName);
            if (!filePath.equals("")) {
              found = true;
              break;
            }
          }
          if (roots[i].getName().equals(fileName)) {
            try {
              filePath = roots[i].getCanonicalPath();
              found = true;
              break;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        } else {
          for (File f : roots[i].listFiles()) {
            if (f.isDirectory()) {
              String directory = f.getAbsolutePath();
              filePath = searchDirectory(directory, fileName);
              if (!filePath.equals("")) {
                found = true;
                break;
              }
            }
            if (f.getName().equals(fileName)) {
              try {
                filePath = f.getCanonicalPath();
                found = true;
                break;
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        }
      } catch (Exception e) {
        continue;
      }
      if (found) {
        break;
      }
    }
    return found;
  }


  /**
   * Method to search for the given filename in a specific directory
   *
   * @param dir given dir to find the fileName
   * @param name the name of fileName which is in the given dir
   * @return location of the fileName
   */
  public String serachMachineForFileWhichStartWith(String dir, String name) {
    String filePath = "";
    File fileDir = new File(dir);
    File[] roots = fileDir.listFiles();
    for (int i = 0; i < roots.length; i++) {
      try {
        for (File f : roots[i].listFiles()) {
          if (f.isDirectory()) {
            String directory = f.getAbsolutePath();
            filePath = searchDirNameWhichStartWith(directory, name);
            if (!filePath.equals("")) {
              break;
            }
          }
          if (f.getName().equals(name)) {
            try {
              filePath = f.getCanonicalPath();
              break;
            } catch (Exception e) {
              System.err.println(e.getMessage());
            }
          }
        }
      } catch (Exception e) {
        continue;
      }
    }
    return filePath;
  }

  /**
   * Method to search for the given filename in a specific directory
   *
   * @param filename Object of the directory to be searched into
   * @param filename Name of the file to be searched
   * @return Path of the file to be searched or an empty string if the file was not found
   * @author Ankit
   */
  private String searchDirectory(String dir, String filename) throws Exception {
    File f = new File(dir);
    File[] subFiles = f.listFiles();
    if (subFiles == null) {
      return "";
    }
    String path = "";
    for (File fi : subFiles) {
      if (fi.isDirectory()) {
        String directory = fi.getAbsolutePath();
        path = searchDirectory(directory, filename);
        if (!path.equals("")) {
          break;
        }
      } else if (fi.getName().equals(filename)) {
        try {
          path = fi.getCanonicalPath();
          System.out.println("Match found : " + path);
          break;
        } catch (Exception e) {
          System.err.println(e.getMessage());
          throw new Exception(filename + " is not found in the given : " + f.getAbsolutePath());
        }
      }
    }
    return path;
  }

  /**
   * Method to search for the given filename which has partial name in a specific directory
   *
   * @param filename Object of the directory to be searched into
   * @param filename Name of the file to be searched
   * @return Path of the file to be searched or an empty string if the file was not found
   * @author Ankit
   */
  private String searchDirNameWhichStartWith(String dir, String filename) throws Exception {
    File f = new File(dir);
    File[] subFiles = f.listFiles();
    if (subFiles == null) {
      return "";
    }
    String path = "";
    for (File fi : subFiles) {
      if (fi.isDirectory()) {
        String directory = fi.getAbsolutePath();
        path = searchDirNameWhichStartWith(directory, filename);
        if (!path.equals("")) {
          break;
        }
      } else if (fi.getName().startsWith(filename)) {
        try {
          path = fi.getCanonicalPath();
          System.out.println("Match found : " + path);
          break;
        } catch (Exception e) {
          System.err.println(e.getMessage());
          throw new Exception(filename + " is not found in the given : " + f.getAbsolutePath());
        }
      }
    }
    return path;
  }
}
