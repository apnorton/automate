package automate;

import java.util.ArrayList;
import java.util.List;

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
  
  //Neutral return for a function is the same as the neutral value of a variable of the same type
  public String getNull() { return (new Var(type, name)).getNull(); }
  
  //Tests for void return type
  public boolean isVoid() { return this.type.equals("void") || this.type.equals(""); } //The latter condition is when this method is a constructor
  
  @Override
  public String toString() { 
    StringBuilder argStr = new StringBuilder();
    
    if (args.size() > 0) {
      for (int i = 0; i < args.size() - 1; i++) {
        argStr.append(args.get(i).toString());
        argStr.append(", ");
      }
      //append the last element of the list *without* a comma following it.
      argStr.append(args.get(args.size()-1).toString()); 
    }
    
    return String.format("%s %s %s(%s)", access, type, name, argStr);
  }
}
