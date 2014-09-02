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
  private int someNumber;

  //////  Methods:  //////
  public int fib(int n) {
    return 0;
  }

  public void killIndex(int n, String name) {

  }

  public String randomString() {
    return new String();
  }

  //////  ToString:  //////
  @Override
  public String toString() {
    return ("[ myString=" + myString + "name=" + name + "someNumber=" + someNumber + "]");
  }

  //////  Equals:  //////
  @Override
  public boolean equals(Object o) {
    //Take care of trivialities:
    if (o == null || this.getClass() != o.getClass()) return false;

    test that = (test) o; //Cast to current type
    boolean retVal = ((this.myString).equals(that.myString)) || ((this.name).equals(that.name)) || (this.someNumber == that.someNumber);

    return retVal;
  }

  //////  Getters:  //////
  public String getMyString() { return this.myString; }
  public String getName() { return this.name; }
  public int getSomeNumber() { return this.someNumber; }

  //////  Setters:  //////
  public void setMyString(String myString) { this.myString = myString; }
  public void setName(String name) { this.name = name; }
  public void setSomeNumber(int someNumber) { this.someNumber = someNumber; }

}
