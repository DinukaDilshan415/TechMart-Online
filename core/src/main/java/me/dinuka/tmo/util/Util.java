/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package me.dinuka.tmo.util;

/**
 *
 * @author E
 */
public class Util {

  public static final String LOCAL_BASE_URL = "http://localhost:8080";
  public static final String BASE_URL = "https://e720-2402-d000-a500-1ccd-d118-d721-1df1-70c1.ngrok-free.app";

    public static String generateCode() {
        int random = (int) (Math.random() * 100000);
        return String.format("%06d", random);
    }

    public static boolean isCodeValid(String code) {
        return code.matches("^\\d{4,5}");
    }
}
