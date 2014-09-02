package automate;

import java.util.*;
import java.io.*;
import java.util.regex.*;

public class ClassCreator {
  //Regex parsing tools
  //The below regex describes the format: "<category>: <access char> <type> <name>(<arglist-optional>)"
  private static final String linePatternStr = "(?<cat>field|method|constructor):\\s*(?<acs>[+x-])\\s*(?<typ>\\w*)\\s*(?<nam>\\w*)\\s*(\\((?<args>.*?)\\))?\\s*";
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
      pw.println(); //Skip a between fields and methods
      
      //Body of class
      writeMethods(pw);
      
      //Getters and Setters
      writeGetters(pw);
      pw.println();
      writeSetters(pw);
      
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
    pw.println("\n/* File: " + classname + ".java\n  * Author:\n  * Email:\n  * Desciption:\n  */\n");
    
    //Class declaration
    pw.println("public class " + classname + " {");
  }
  
  private static void writeFields(PrintWriter pw) {    
    //Print out all fields
    for (Field f : fields) {
      //Indent, code, and semicolon!
      pw.println(tab + f.toString() + ";");
    }
  }
  
  //Write a method skeleton for 
  private static void writeMethods(PrintWriter pw) {
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
  
  //Write common overrides (toString() and equals());
  private static void writeOverrides() {
  }
  
  //Writes a getter/setter for all 
  private static void writeGetters(PrintWriter pw) {
    pw.println(tab + "//Getters:");
    for (Field f : fields) {
      if (f.getAccess().equals("public")) continue; //I don't need get/set for public variables!
      
      //Method name is "get" + (capitalized field name)
      String methodName = "get" + Character.toUpperCase(f.getName().charAt(0)) + (f.getName().substring(1));
      pw.println(tab + "public " + f.getType() + " " + methodName + "() { return this." + f.getName() + "; }");
    }
  }
  
  private static void writeSetters(PrintWriter pw) {
    pw.println(tab + "//Setters:");
    for (Field f : fields) {
      if (f.getAccess().equals("public")) continue; //I don't need get/set for public variables!
      
      //Method name is "set" + (capitalized field name)
      String methodName = "set" + Character.toUpperCase(f.getName().charAt(0)) + (f.getName().substring(1));
      pw.println(tab + "public " + f.getType() + " " + methodName + "(" + f.getType() + " " + f.getName() + ") {");
      pw.println(tab + tab + "this." + f.getName() + " = " + f.getName() + ";\n" + tab + "}\n");
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