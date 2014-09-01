package automate;

/*
 * This is just a test for the regex string I am using for parsing each line of input.
 */
 
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class RegexTest {
  public static void main(String[] args) throws Exception{
    Scanner fin = new Scanner(new File("test.txt"));
    //The below regex describes the format: "<category>: <access char> <type> <name>(<arglist-optional>)"
    String myReg = "(?<cat>field|method|constructor):\\s*(?<acs>[+x-])\\s*(?<typ>\\w*)\\s*(?<nam>\\w*)\\s*(?<args>\\(.*?\\))?\\s*";
    Pattern p = Pattern.compile(myReg);
    
    while(fin.hasNext()) {
      String line = fin.nextLine();
      Matcher m = p.matcher(line);
      System.out.println(line);
      System.out.printf("Matches? %s\n", m.matches());      
      System.out.println(m.group("cat"));
      
    }
  }
}