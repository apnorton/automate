package automate;

//A variable consists of a name and a datatype
public class Var {
  //These associate primitive types with their associated "null" element... DON'T alter one without altering the other!
  //String is not a primitive (for this program) because s1 == s2 does not behave as .equals() should)
  private static String[] primitives = {"int", "long", "float", "double", "boolean", "char"};
  private static String[] nullElement = {"0", "0", "0.0", "0.0", "false", "'0'"};
  
  private String type;
  private String name;
  
  public Var(String type, String name) {
    this.type = type;
    this.name = name;
  }
  
  public String getName() { return name; }
  public String getType() { return type; }
  
  //Returns true if this is a primitive type, false otherwise.
  public boolean isPrimitive() {
    boolean retVal = false;
    
    //Linearly check all primitives
    for(String s : primitives) {
      retVal |= s.equals(this.type);
    }
    
    return retVal;
  }
  
  //Returns the "null element" for the type
  public String getNull() {
    if (!isPrimitive()) {
      return "new " + this.type + "()";
    }
    else {
      //Find the index of the primitive this is
      int i;
      for (i = 0; i < primitives.length && !primitives[i].equals(this.type); i++)
        ;
        
      return nullElement[i];
    }
  }
  
  @Override
  public String toString() { return type + " " + name; }
}
