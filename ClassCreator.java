package automate;

import java.util.*;
import java.io.*;
import java.util.regex.*;

public class ClassCreator {
  //Regex parsing tools
  //The below regex describes the format: "<category>: <access char> <type> <name>(<arglist-optional>)"
  private static final String linePatternStr = "(?<cat>field|method):\\s*(?<acs>[+x-])\\s*(?<typ>\\w*)\\s*(?<nam>\\w*)\\s*(\\((?<args>.*?)\\))?\\s*";
  private static final Pattern linePattern = Pattern.compile(linePatternStr);
  private static final Pattern argPattern = Pattern.compile("[,\\s]*(?<typ>\\w+)\\s*(?<nam>\\w+)");
  
  //Globals
  private static List<Field> fields = new ArrayList<Field>();
  private static List<Method> methods = new ArrayList<Method>();
  private static String tab = "  "; //Tab width = 2 spaces.
  
  public static void main(String[] args) {
    String classname;
    Scanner fin = null;
    PrintWriter pw = null;
    
    //Check for command line args, then find the class name
    if (args.length != 1) {
      System.out.println("Usage: java automate.ClassCreator filename");
      System.out.println("It appears you have not provided a filename to load, please try again.");
      System.exit(1);
    }
    classname = args[0];
    
    //Set up reader/writers.    
    try {
      fin = new Scanner(new File(classname + ".txt"));
      pw = new PrintWriter(classname + ".java");
    }
    catch (Exception ex) {
      System.out.println("Something went wrong reading or writing to the filesystem.");
      System.out.println("Please ensure a text file exists with the name you provided.");
      System.exit(1);
    }
    
    //Actually attempt to parse the file
    try {
      readFile(fin);
      System.out.println("Input file parsed.");
      
      //Write file header
      writeTopMatter(pw, classname);
      writeFields(pw);
      pw.println(); //Skip a between fields and constructor
      writeConstructor(classname, pw);
      
      
      //Body of class
      writeMethods(pw);
      writeToString(pw);
      writeEquals(classname, pw);
      
      //Getters and Setters
      writeGetters(pw);
      pw.println();
      writeSetters(pw);
      pw.println();
      
      //Close out the class definition
      pw.println("}");
      
      pw.close();
    }
    catch (Exception ex) {  //Catch-all.
      System.out.println("Exception thrown.  All we know is:");
      System.out.println(ex.getMessage());
      System.exit(1);
    }
    
    System.out.println("Fields: " + fields);
    System.out.println("Methods: " + methods);
    
  }
  
  //Writes the "top matter"--common imports, comment template, and open class
  private static void writeTopMatter(PrintWriter pw, String classname) {
    //Imports
    pw.println("import java.util.*;");
    
    //Comment header
    pw.println("\n/*\n * File: " + classname + ".java\n * Author:\n * Email:\n * Desciption:\n */\n");
    
    //Class declaration
    pw.println("public class " + classname + " {");
  }
  
  private static void writeFields(PrintWriter pw) {   
    pw.println(tab + "//////  Fields:  //////");
    //Print out all fields
    for (Field f : fields) {
      //Indent, code, and semicolon!
      pw.println(tab + f.toString() + ";");
    }
  }
  
  //Writes a basic constructor, where all fields are specified.
  private static void writeConstructor(String classname, PrintWriter pw) {
    //Print start of constructor
    pw.println(tab + "//////  Constructor:  //////");
    pw.print(tab + "public " + classname + "(");

    //Print constructor arguments
    if (fields.size() > 0) {
      for (int i = 0; i < fields.size() - 1; i++) {
        pw.print(fields.get(i).getVar().toString());
        pw.print(", ");
      }
      //append the last element of the list *without* a comma following it.
      pw.print(fields.get(fields.size()-1).getVar().toString()); 
    }
    pw.println(") {");
    
    //Print all the assignments within the constructor
    for(Field f : fields)
      pw.println(tab + tab + "this." + f.getName() + " = " + f.getName() + ";");
    
    pw.println(tab + "}");
    pw.println();
  }
  
  //Write a method skeleton for all methods
  private static void writeMethods(PrintWriter pw) {
    pw.println(tab + "//////  Methods:  //////");
    for(Method m : methods) {
      pw.println(methodToCode(m));
      pw.println();
    }
  }
  
  private static String methodToCode(Method m) {
    StringBuilder sb = new StringBuilder();
    sb.append(tab); sb.append(m.toString()); sb.append(" {\n");
    
    if (!m.isVoid()) {
      sb.append(tab + tab + "return ");
      sb.append(m.getNull()); //return the "null"/"neutral" element for this method
      sb.append(';');
    }
    sb.append('\n' + tab + "}");
    
    return sb.toString();
  }
  
  //Creates a basic "toString" method
  private static void writeToString(PrintWriter pw) {
    if (fields.size() == 0) return; //Don't write a toString method if there's no fields!
    
    pw.println(tab + "//////  ToString:  //////");
    pw.println(tab + "@Override");
    pw.println(tab + "public String toString() {");
    pw.print(tab + tab + "return (\"[ ");
    
    for (Field f : fields) {
      pw.print(f.getName() + "=\" + " + f.getName() + " + \"");
    }
    pw.println("]\");");
    pw.println(tab + "}\n");
  }
  
  //Creates a basic "equals" method
  private static void writeEquals(String classname, PrintWriter pw) {
    if (fields.size() == 0) return; //Don't write an equals method if there's no fields!
    
    //Boilerplate
    pw.println(tab + "//////  Equals:  //////");
    pw.println(tab + "@Override");
    pw.println(tab + "public boolean equals(Object o) {");
    pw.println(tab + tab + "//Take care of trivialities:");
    pw.println(tab + tab + "if (o == null || this.getClass() != o.getClass()) return false;\n");
    
    pw.println(tab + tab + classname + " that = (" + classname + ") o; //Cast to current type");
    
    //Set up a return value
    pw.print(tab + tab + "boolean retVal = ");
    
    //a.equals(b) if and only if all fields of a match all fields of b
    Iterator<Field> fI = fields.iterator();
    Field f = null;
    while(fI.hasNext()) {
      f = fI.next();
      
      if (f.isPrimative())
        pw.print("(this." + f.getName() + " == that." + f.getName() + ")");
      else
        pw.print("((this." + f.getName() + ").equals(that." + f.getName() + "))");
      
      if (fI.hasNext()) 
        pw.print(" || ");
    }
    pw.println(";\n");
    
    pw.println(tab + tab + "return retVal;");
    pw.println(tab + "}\n");
  }
  
  //Writes a getter/setter for all 
  private static void writeGetters(PrintWriter pw) {
    pw.println(tab + "//////  Getters:  //////");
    for (Field f : fields) {
      if (f.getAccess().equals("public")) continue; //I don't need get/set for public variables!
      
      //Method name is "get" + (capitalized field name)
      String methodName = "get" + Character.toUpperCase(f.getName().charAt(0)) + (f.getName().substring(1));
      pw.println(tab + "public " + f.getType() + " " + methodName + "() { return this." + f.getName() + "; }");
    }
  }
  
  private static void writeSetters(PrintWriter pw) {
    pw.println(tab + "//////  Setters:  //////");
    for (Field f : fields) {
      if (f.getAccess().equals("public")) continue; //I don't need get/set for public variables!
      
      //Method name is "set" + (capitalized field name)
      String methodName = "set" + Character.toUpperCase(f.getName().charAt(0)) + (f.getName().substring(1));
      pw.print(tab + "public void " + methodName + "(" + f.getType() + " " + f.getName() + ") {");
      pw.println(" this." + f.getName() + " = " + f.getName() + "; }");
    }
  }
  
  //Get all user input.
  private static void readFile(Scanner fin) throws Exception {
    int lineNum = 0;
    while (fin.hasNext()) {
      lineNum++; //Keep track of line numbers for debugging.
      
      String currLine = fin.nextLine();
      parseLine(currLine, lineNum);
    }
  }
    
  //Takes in one line of input and appends the data I care about to the appropriate lists
  private static void parseLine(String s, int num) throws Exception {
    //Generate regex matcher for parsing line.
    Matcher m = linePattern.matcher(s);
    
    if(!m.matches()) throw new Exception("Parse error, line " + num);
    
    //match ALL the things!
    String category = m.group("cat"); //Find category: field, method, or constructor
    String accessChar = m.group("acs"); //Find access character
    String type = m.group("typ"); //Find datatype
    String name = m.group("nam"); //Find name of the thing
    String argString = m.group("args"); //Find the arg list (if it exists)
    
    if (category.equals("field")) { //Add a field to the list of fields
      Field newField = new Field(getAccessString(accessChar), type, name);
      fields.add(newField);
    }
    else if (category.equals("method")) { //Add a method to the list of methods
      List<Var> args = parseArgs(argString);
      Method newMethod = new Method(getAccessString(accessChar), type, name, args);
      methods.add(newMethod);
    }
    //TODO: deal with constructors
  } 
  
  //Takes in a string, like "int test, long mine" and converts to a list of Vars
  private static List<Var> parseArgs(String argStr) {
    List<Var> args = new ArrayList<Var>();
    Matcher m = argPattern.matcher(argStr);
    
    //Add Var's to the list until you can't find any more
    while (m.find()) {
      Var newVar = new Var(m.group("typ"), m.group("nam"));
      args.add(newVar);
    }
    
    return args;
  }
  
  // Converts the +x- character to a string "public," "private," or "protected."
  private static String getAccessString(String accessChar) {
    String accessStr;
    if (accessChar.equals("-")) 
      accessStr = "private";
    else if (accessChar.equals("x")) 
      accessStr = "protected";
    else
      accessStr = "public";
      
    return accessStr;
  }
}