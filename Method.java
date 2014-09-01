package automate;

import java.util.*;

//A method consists of an access restriction, return type, name, and list of arguments
public class Method {
  private String access;
  private String name;
  private String type;
  private List<Var> args;
  
  public Method(String access, String type, String name, List<Var> args) {
    this.access = access;
    this.name = name;
    this.type = type;
    this.args = args;
  }
  
  public Method(String access, String type, String name) {
    this(access, type, name, new ArrayList<Var>());
  }
  
  @Override
  public String toString() { 
    String argStr = args.toString();
    
    return String.format("%s %s %s(%s)", access, type, name, argStr);
  }
}