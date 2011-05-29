package be.libis.lias.toolbox;

import java.util.Random;

public class RandomString {

  public String        symbols = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+-=[]{};:<>?,./\\|";

  private final Random random  = new Random();
  private final char[] buffer;

  public RandomString(int length) {
    if (length < 1)
      throw new IllegalArgumentException("length < 1: " + length);
    buffer = new char[length];
  }

  public String nextString() {
    for (@SuppressWarnings("unused")
    char c : buffer) {
      c = symbols.charAt(random.nextInt(symbols.length()));
    }
    return new String(buffer);
  }

}
