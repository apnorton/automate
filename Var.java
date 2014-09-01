package automate;

import java.util.*;

//A variable consists of a name and a datatype
public class Var {
  private String type;
  private String name;
  
  public Var(String type, String name) {
    this.type = type;
    this.name = name;
  }
  
  public String getName() { return name; }
  public String getType() { return type; }
  
  @Override
  public String toString() { return type + " " + name; }
}