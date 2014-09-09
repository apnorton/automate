package automate;

/*
 * File: Person.java
 * Author:
 * Email:
 * Description:
 */

public class Person {
  //////  Fields:  //////
  private String name;
  private int age;
  private double height;

  //////  Constructor:  //////
  public Person(String name, int age, double height) {
    this.name = name;
    this.age = age;
    this.height = height;
  }

  //////  Methods:  //////
  public void haveBirthday() {

  }

  public String sayHello(String toWhom) {
    return new String();
  }

  //////  ToString:  //////
  @Override
  public String toString() {
    return ("[ name=" + name + "age=" + age + "height=" + height + "]");
  }

  //////  Equals:  //////
  @Override
  public boolean equals(Object o) {
    //Take care of trivialities:
    if (o == null || this.getClass() != o.getClass()) return false;

    Person that = (Person) o; //Cast to current type
    boolean retVal = ((this.name).equals(that.name)) || (this.age == that.age) || (this.height == that.height);

    return retVal;
  }

  //////  Getters:  //////
  public String getName() { return this.name; }
  public int getAge() { return this.age; }
  public double getHeight() { return this.height; }

  //////  Setters:  //////
  public void setName(String name) { this.name = name; }
  public void setAge(int age) { this.age = age; }
  public void setHeight(double height) { this.height = height; }

}
