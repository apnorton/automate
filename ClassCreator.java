package automate;

import java.util.*;
import java.io.*;
import java.util.regex.*;

public class ClassCreator {
  //Regex parsing tools
  //The below regex describes the format: "<category>: <access char> <type> <name>(<arglist-optional>)"
  private static final String linePattern = "(?<cat>field|method|constructor):\\s*(?<acs>[+x-])\\s*(?<typ>\\w*)\\s*(?<nam>\\w*)\\s*(?<args>\\(.*?\\))?\\s*";
  private static final Pattern p = Pattern.compile(linePattern);
  
  //Globals
  private static List<Field> fields = new ArrayList<Field>();
  private static List<Method> methods = new ArrayList<Method>();
  
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
    }
    catch (Exception ex) {  //Catch-all.
      System.out.println("Exception thrown.  All we know is:");
      System.out.println(ex.getMessage());
      System.exit(1);
    }
    
    System.out.println("Fields: " + fields);
    System.out.println("Methods: " + methods);
    
  }
  
  private static void readFile(Scanner fin) throws Exception {
    int lineNum = 0;
    while (fin.hasNext()) {
      lineNum++; //Keep track of line numbers for debugging.
      
      String currLine = fin.nextLine();
      parseLine(currLine, lineNum);
    }
  }
  
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
  
  private static void parseLine(String s, int num) throws Exception {
    //Generate regex matcher for parsing line.
    Matcher m = p.matcher(s);
    
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
      Method newMethod = new Method(getAccessString(accessChar), type, name);
      methods.add(newMethod);
    }
    //TODO: deal with constructors
  } 
  
  private static String parseArgs(String args) {
    
  }

  //A variable consists of a name and a datatype
  private static class Var {
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
  
  //A field consists of a variable and an access restriction
  private static class Field {
    private Var variable;
    private String access;
    
    public Field(String access, Var variable) {
      this.access = access;
      this.variable = variable;
    }
    
    public Field(String access, String type, String name) {
      this(access, new Var(type, name));
    }
    
    @Override 
    public String toString() { return access + " " + variable; }
  }
  
  //A method consists of an access restriction, return type, name, and list of arguments
  private static class Method {
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
}