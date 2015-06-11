package com.alifesoftware.instaassignment.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by anujsaluja on 6/10/15.
 */
public class Utils {
    public static boolean isNullOrEmpty(String str) {
        if(str == null ||
                str.isEmpty()) {
            return true;
        }

        return false;
    }

    public static String streamToString(InputStream inputStream) {
        String response = "";

        if (inputStream != null) {
            Scanner scanner = new Scanner(inputStream);
            response = scanner.useDelimiter("\\A").next();
            scanner.close();
        }

        return response;
    }
}
