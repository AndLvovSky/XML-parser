package com.andlvovsky.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.net.URLDecoder;

public class ResourceReader {

    public static String getFilename(String filename) {
        ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(filename);
        try {
            filename = URLDecoder.decode(url.getPath(), "UTF-8");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return filename;
    }

    public static String readText(String filename) {
        String correctFilename = getFilename(filename);
        String text = "";
        try(BufferedReader reader =
                    new BufferedReader(new FileReader(correctFilename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                text += line;
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return text;
    }

}