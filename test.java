import java.util.*;

/*
 * File: test.java
 * Author:
 * Email:
 * Desciption:
 */

public class test {
  //////  Fields:  //////
  protected String myString;
  private String name;

  //////  Methods:  //////
  public int fib(int n) {
    return 0;
  }

  public void killIndex(int n, String name) {

  }

  //////  ToString:  //////
  @Override
  public String toString() {
    return ("[ myString=" + myString + "name=" + name + "]");
  }

  //////  Equals:  //////
  @Override
  public boolean equals(Object o) {
    //Take care of trivialities:
    if (o == null || this.getClass() != o.getClass()) return false;

    test that = (test) o; //Cast to current type
    boolean retVal = ((this.myString).equals(that.myString)) || ((this.name).equals(that.name));

    return retVal;
  }

  //////  Getters:  //////
  public String getMyString() { return this.myString; }
  public String getName() { return this.name; }

  //////  Setters:  //////
  public String setMyString(String myString) { this.myString = myString; }
  public String setName(String name) { this.name = name; }

}
