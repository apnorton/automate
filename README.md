automate
========

Tools for the automatic creation of Java code. (In progress...)

I have often heard it said "if you are bored coding, then you're doing tasks that you should have automated."  This is my first attempt at automating my code development process.

ClassCreator
============

Currently, the only tool in this package is the ClassCreator.  In academic settings, it is common for an instructor to specify a class name, as well as several fields and methods that should be in the class.  The "ClassCreator" program converts an input file of fields and method headers into a compilable Java program (with method skeletons, accessors, mutators, and simple constructors).

To use this program, create a text (*.txt) file with the desired classname.  (Capitalization is important.)  Inside this file, list the fields and methods you want the class to have.  For example, the file could contain the following five lines:
field: - String name
field: - int age
field: - double height
method: + void haveBirthday()
method: + String sayHello(String toWhom)

Given this input, the program will create a 60 line, compilable Java program.  A constructor with name, age, and height arguments is created in full (method body, not just skeleton), as well as skeletons for sayHello(String toWhom) and haveBirthday().  All private and protected fields generate getters and setters.  Finally, toString() and equals(Object) methods are created with their method bodies.  (The default equals(Object) method returns true if and only if all fields are equal, and the default toString() method displays all field values and names.)

ClassCreator Input Format
=========================

Each line of the program has five "groups", designated below:
<category>: <access character> <type> <name>(<optional method arguments>)

The <category> must be either "field" or "method."
The <access character> is one of [+x-], where + corresponds to "public" access, x to "protected" access, and - to "private" access.
The <type> is a classname or primative type.  (Note: The program doesn't check to see if a class is real, it just assumes you've typed correctly.)
The <name> is the name of the field or method.
In parentheses following the name, methods arguments (type and name) can be listed in comma-separated format.
