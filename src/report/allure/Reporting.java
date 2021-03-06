package report.allure;

import java.io.File;
import java.io.IOException;

import com.google.common.collect.ImmutableMap;

public class Reporting {
	
	public static void genrateAllureReport() throws IOException, InterruptedException {
		String PATH = System.getProperty("user.dir") + File.separator;
		File reportDir = new File(PATH + File.separator + "Allure-Report");
		reportDir.mkdir();				
		Runtime.getRuntime().exec(String.format("/usr/local/bin/allure generate -c -o %s", reportDir));
        File temp = new File(PATH + File.separator + "TempReport");
        if(temp.exists()) {
            Runtime.getRuntime().exec(String.format("rm -r %s", temp));
        }
        Thread.sleep(2000);
        temp.mkdir();
        Thread.sleep(2000);
        copyDirectory(reportDir,temp);
		Thread.sleep(2000);
        Runtime.getRuntime().exec(String.format("cp -R %s %s", reportDir,temp));
		//remove all files/folders from history folder of allure_result
        File historyFolder = new File(PATH + File.separator + "allure-results/history");
        if(historyFolder.exists()) {
            Runtime.getRuntime().exec(String.format("rm -r %s", historyFolder));
            Thread.sleep(2000);
        }
        File hFolder = new File(PATH + File.separator + "Allure-Report/history/");
		Runtime.getRuntime().exec(String.format("cp -R %s %s", hFolder,historyFolder));		
	}	
	
	public static void copyDirectory(File sourceLocation , File targetLocation)throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        }
    }
	
	public static void cleanAllureResults() throws IOException {
		String PATH = System.getProperty("user.dir") + File.separator;
		File dirAllureResult = new File(PATH + File.separator + "allure-results");
		if (dirAllureResult.exists()) {
			// remove all files except history folder
			for (File file : dirAllureResult.listFiles()) {
				if (!file.getName().equals("history")) { // || !file.getName().equals("environment.properties")
					file.delete();
				}
			}
		}
		File dirResult = new File(PATH + File.separator + "Allure-Report");
		if (dirResult.exists()) {
			// remove Report folder and its contents
			Runtime.getRuntime().exec(String.format("rm -r %s", dirResult));
		}

		// Set environment
		AllureEnvironmentWriter.allureEnvironmentWriter(
				ImmutableMap.<String, String>builder().put("Browser", "Chrome").put("Browser.Version", "70.0.3538.77")
						.put("URL", "https://demo.nopcommerce.com/").build(),
				System.getProperty("user.dir") + "/allure-results/");
	}

}
