package automate;

//A field consists of a variable and an access restriction
public class Field {
  private Var variable;
  private String access;
  
  public Field(String access, Var variable) {
    this.access = access;
    this.variable = variable;
  }
  
  public Field(String access, String type, String name) {
    this(access, new Var(type, name));
  }
  
  public String getType() { return variable.getType(); }
  public String getName() { return variable.getName(); }
  public String getAccess() { return access; }
  public Var getVar() { return variable; }
  
  public boolean isPrimative() { return variable.isPrimitive(); }
  public String getNull() { return variable.getNull(); }
  
  @Override 
  public String toString() { return access + " " + variable; }
}