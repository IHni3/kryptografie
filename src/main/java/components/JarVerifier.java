/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package components;

import configuration.Configuration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JarVerifier {
    public static boolean verify(String jarFile) {
        boolean isComponentAccepted = false;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(Configuration.instance.jdkPath + "jarsigner", "-verify", jarFile);
            Process process = processBuilder.start();
            process.waitFor();

            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("verified")) {
                    isComponentAccepted = true;
                }
            }

        } catch (Exception e) {
            Configuration.instance.getLogger().printInfo("a problem occurred while verifying " + jarFile);
        }
        return isComponentAccepted;
    }
}
