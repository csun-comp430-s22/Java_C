# Java_C
A small compiler written in Java that compiles down to C.  It features OOP, bounds-checked arrays, and subtyping.

Language Documentation
Java_C
Kevin Siraki, Michael Barseghian, Miher Jivalagian
Why this language, and why this language design?
Overall, our language aimed to solve the problem of undefined behavior in C as well as
add object-oriented features to C as well. Our initial non-trivial feature set was to implement
subtyping, OOP/inheritance, and bound-checked arrays to the language. We initially decided
upon PHP to Java since we are quite knowledgeable on PHP and Java, but ended up choosing
Java to C because we wanted the challenge of adding object oriented features to a procedural
language like C. Moreover, we were quite well versed on Java and build tools like Apache
Maven in the first place so we decided upon writing our compiler in this language. The crossover
between Java and C also appealed to us as it would ensure we have a working knowledge of both
languages meaning troubleshooting would come easier. Lastly, Java’s garbage collector would
make memory management issues less problematic for us as an added bonus.
Code snippets in your language highlighting features and edge cases, along with relevant
explanations (next page):
Demonstrating:
- print/print with newline statements->(“clog” / “clogln” in Java_C language)
- Looping structure (while)
- Variable declarations
- If/else statement
Demonstrating:
- Fields/member variables
- This pointer
Demonstrating:
- Our attempt to parse for arrays without an actual code generator to handle bounds
checking.
Demonstrating:
- Subtyping
- This Pointer
- Inheritance
- Member variables
- Fields
- Method calls
Known Limitations:
Initial Proposal Limitations:
We will not be including any optimizations to the Java language to make runtimes more
comparable to C. The runtime speed combined with the statically-typed nature of each language
will be a restriction to the scalability of this language. Moreover, the conversion of the
destructor ‘~’ to C may be too daunting for us to accomplish as it would require several
“dealloc” calls, the size of which may be too hard to determine at compile time. Also, the
language will not have access modifiers (private, public, protected), and it will not feature
floating-point numbers/arithmetic. Lastly, arrays will need to be of a fixed/predefined length and
there will not be any type casting for now.
New Limitations:
-Arrays are featured in our language, but the lack of a code generator renders the
bounds-checked aspect of the arrays to be purely theoretical (this can be seen in one of our
snippets)
-It is not easy to run code in our language because we do not have a code generator (this can be
seen at the end when we elaborate on how to run our compiler)
-Return statements have to be at the end of the method and there can only be one return token
found within a method.
-There are no floating point values/types
-There are no access modifiers present (as mentioned in our initial proposal)
-No pointer support
-Compiler itself is derived from GitHub examples from class, and has the limitation of several
nested if/else statements which may reduce speed overall.
-No destructor (~ tilde as mentioned in our initial proposal)
-No typecasting
-Overall, the biggest issue overall is the lack of any code generation to easily run a program in
our language without using it inline within a Java program.
Knowing what you know now, what would you do differently?
If we were to do this project again from scratch, one thing we would do is try to limit our
use of if/else statements a bit because there were quite a lot, especially in our Parser.
Furthermore, we would also try to create a more detailed high-level plan of each future
component before starting on any actual implementation because we had a lot of issues in
integrating code from the course GitHub when we were just using normal arrays and had to go
back and refactor our antiquated code to feature ArrayLists. We mainly did this after the fact
because some group members were having trouble adding their code to previously implemented
code as they were used to using arrays. We mostly overcame this while refactoring as we mostly
worked on Discord while sharing our screens. Next, a more high-level plan prior to
implementing a component would be helpful because it would allow for shorter and more
meaningful unit tests that are not as redundant as ours were. For instance, we had about 5 unit
tests just to test for the presence of array brackets to see if the user correctly wrote an
ArrayIndexStatement. This could have been fixed by initially making the entire
ArrayExpression itself and deriving from this in the ArrayIndexExp class. We also had a decent
amount of exceptions thrown and this made writing unit tests that reach 80% coverage quite
tedious because all of the exceptions and edge cases were not really easy to emulate. This could
have been avoided by getting rid of a lot of these as a person using the language was not likely to
make some of the errors we had exceptions for.
To summarize this section, we believe that having a more high-level plan prior to writing
the actual code for each component would help us in the long run as we would not have to
refactor the code as much and would be able to write more meaningful, less redundant, and
overall clearer code and unit tests.
How do I compile your compiler?
Our compiler is written in Java, so really any Java compiler would work. However, we
recommend using Maven along with the mvn compile command as our pom.xml dictates a
certain file structure for everything to work seamlessly.
How do I run your compiler?
Since we do not have a code generator, the main way to run our compiler would be to
write the program within either the ParserTest.java or TypecheckerTest.java in order to test out a
program or see any errors that may be throw from ill-typed code or syntax errors. This is an
example we used in our ParserTest.java:
(Formal Syntax Definition on Next Page)
Formal syntax definition:
varname is any variable
classname is a class
methodname is a method
sname is a string
iname is an integer
type[] is an array | heap allocated arrays
type ::= Int | Bool | Void | type[] |(default types)classname (user-defined type)
op ::= + | - | * | / | < | == | > | <= | >= (nominal math/bool operations… no, |< is not a pipe…)
exp ::= sname | iname | varname | true | false | new classname(exp*)| (expressions)
new type[exp] | heap allocated arrays
exp[exp] | reference an array index
exp.length | length of array
exp op exp | (arithmetic operation on expressions)
exp.methodname(exp*) | (call a method on an expression, args are comma separated)
this | refers only to the instance of which a constructor/method was called
classname varname(exp*) | class
vardecl ::= type varname | (declare a default-type variable)
stmt ::= vardecl = exp | (syntax of variable declaration)
while(exp) { stmt* } | (while loop with statement block)
{ stmt* } | statement block
break; | break out of a loop
exp.methodname(exp*) | (call a method on an expression, args are comma separated)
return exp; | (return an expression/varname)
return; | (return void)
if (exp) stmt else stmt | conditional statements
clog(exp) | (print without newline)
clogln(exp) | (print with newline)
varname = exp; | exp[exp] = exp; | variable assignment
methoddef ::= type methodname(vardecl*) { stmt* }| comma separate arguments
instancedecl ::= vardec; |instance variable declaration (no access modifier)
classdef ::= class classname extends classname {
instancedecl *
constructor(vardecl*) stmt | comma separate arguments
methoddef*
}
program ::= classdef* | class needs a main method entry point
