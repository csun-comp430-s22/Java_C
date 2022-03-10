# Java_C
A small compiler written in Java that compiles down to C.  It features OOP, bounds-checked arrays, and subtyping.
Student Name(s): Kevin Siraki, Michael Barseghian, Miher Jivalagian 
Language name: JavaC (named after the JDK compiler command)
Compiler Implementation Language and Reasoning: We decided to go with Java as our implementation language because all team members are familiar with/proficient in Java and it provides us with garbage collection.
Target Language: C
Language Description: The language will have a syntax that is quite comparable to Java syntax.  One added benefit of compiling from Java to C will be that we could utilize “structs” in C to emulate a class data type in Java.  We went with C as a target language as 2 of our group members are fairly adept in C and the syntactic structure of basic elements of both languages is not drastically different.
Planned Restrictions: We will not be including any optimizations to the Java language to make runtimes more comparable to C.  The runtime speed combined with the statically-typed nature of each language will be a restriction to the scalability of this language.  Moreover, the conversion of the destructor ‘~’ to C may be too daunting for us to accomplish as it would require several “dealloc” calls, the size of which may be too hard to determine at compile time.  Also, the language will not have access modifier (private, public, protected), and it will not feature floating-point numbers/arithmetic. Lastly, arrays will need to be of a fixed/predefined length and there will not be any typecasting for now. 
Computation Abstract Non-Trivial Feature: OOP. (Classes and methods, Class based inheritance)

Non-trivial Feature 2: Subtyping

Non-trivial Feature 3: Bounds-checked arrays

Work Planned for Custom Component: Bounds-checking for all array references made in a program.  For instance, if int arr[2] = {1, 2 }; and you do printf(“%d”,arr[2]); this would not generally throw a compilation error in C, but it will spit back some random memory address’s value.  However, our implementation will ensure proper bounds checking to alleviate some of the erratic behavior and security holes that may arise from such array references.




Syntax:

varname is any variable
classname is a class
methodname is a method
sname is a string
iname is an integer 
type[] is an array | heap allocated arrays
type ::= Int | Bool | Void | type[] |(default types)
	classname (user-defined type)
op ::= + | - | * | / | < | == | > | <= | >=  (nominal math/bool operations… no, |< is not a pipe…) 
exp ::= sname | iname | varname | true | false | new classname(exp*)| (expressions)
	new type[exp] | heap allocated arrays
	exp[exp] | reference an array index
            exp.length | length of array
	exp op exp | (arithmetic operation on expressions)
	exp.methodname(exp*) | (call a method on an expression, args are comma separated)
	this | refers only to the instance of which a constructor/method was called
	classname varname(exp*) | class
vardecl ::= type varname | (declare a default-type variable) 
stmt ::= vardecl = exp  | (syntax of variable declaration)
             while(exp) { stmt* } | (while loop with statement block)
	 { stmt* }   | statement block
 break; | break out of a loop
 exp.methodname(exp*) | (call a method on an expression, args are comma separated)
 return exp; | (return an expression/varname)
	 return; | (return void)
	 if (exp) stmt else stmt | conditional statements
 clog(exp) | (print without newline)
	 clogln(exp) | (print with newline)
	 varname = exp; | exp[exp] = exp; | variable assignment
methoddef ::= type methodname(vardecl*) { stmt* }| comma separate arguments
instancedecl  ::= vardec; |instance variable declaration (no access modifier)
classdef ::= class classname extends classname {
	   	instancedecl *
	            constructor(vardecl*) stmt | comma separate arguments  
	            methoddef*
	       }
program ::= classdef* | class needs a main method defined
