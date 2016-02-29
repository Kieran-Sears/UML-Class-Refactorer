/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DesignPatterns;

/**
 *
 * @author Kieran
 */
public class PatternAnalyser {
    
    /*
1. Creating an object by specifying a class explicitly. Specifying a class name when you create an object commits 
you to a particular implementation instead of a particular interface. This commitment can complicate future changes.
To avoid it, create objects indirectly. 
Design patterns: Abstract Factory ( 87 ), Factory Method ( 107 ), Prototype ( 117 ). 

2. Dependence on specific operations. When you specify a particular operation, you commit to one way of satisfying a request.
By avoiding hard-coded requests, you make it easier to change the way a request gets satisfied both at compile-time and at run-time.
Design patterns: Chain of Responsibility ( 223 ), Command ( 233 ). 

3. Dependence on hardware and software platform. External operating system interfaces and application programming
interfaces (APIs) are different on different hardware and software platforms. Software that depends on a particular 
platform will be harder to port to other platforms. It may even be difficult to keep it up to date on its native platform.
It’s important therefore to design your system to limit its platform dependencies. 
Design patterns: Abstract Factory ( 87 ), Bridge ( 151 ). 

4. Dependence on object representations or implementations. Clients that know how an object is represented, stored, located,
or implemented might need to be changed when the object changes. Hiding this information from clients keeps changes from cascading.
Design patterns: Abstract Factory ( 87 ), Bridge ( 151 ), Memento ( 283 ), Proxy ( 207 ). 

5. Algorithmic dependencies. Algorithms are often extended, optimized, and replaced during development and reuse. 
Objects that depend on an algorithm will have to change when the algorithm changes. Therefore algorithms that are likely 
to change should be isolated. 
Design patterns: Builder ( 97 ), Iterator ( 257 ), Strategy ( 315 ), Template Method ( 325 ), Visitor ( 331 ). 

6. Tight coupling. Classes that are tightly coupled are hard to reuse in isolation, since they depend on each other. Tight coupling
leads to monolithic systems, where you can’t change or remove a class without understanding and changing many other classes. 
The system becomes a dense mass that’s hard to learn, port, and maintain. Loose coupling increases the probability that a class can
be reused by itself and that a system can be learned, ported, modified, and extended more easily. Design patterns use techniques such
as abstract coupling and layering to promote loosely coupled systems. 
Design patterns: Abstract Factory ( 87 ), Bridge ( 151 ), Chain of Responsibility ( 223 ), Command ( 233 ), Facade ( 185 ), Mediator ( 273 ), Observer ( 293 ). 

7. Extending functionality by subclassing. Customizing an object by subclassing often isn’t easy. Every new class has a fixed 
implementation overhead (initialization, finalization, etc.). Defining a subclass also requires an in-depth understanding of the 
parent class. For example, overriding one operation might require overriding another. An overridden operation might be required to 
call an inherited operation. And subclassing can lead to an explosion of classes, because you might have to introduce many new subclasses
for even a simple extension. Object composition in general and delegation in particular provide flexible alternatives to inheritance
for combining behavior. New functionality can be added to an application by composing existing objects in new ways rather than by defining
new subclasses of existing classes. On the other hand, heavy use of object composition can make designs harder to understand. 
Many design patterns produce designs in which you can introduce customized functionality just by defining one subclass and composing
its instances with existing ones. 
Design patterns: Bridge ( 151 ), Chain of Responsibility ( 223 ), Composite ( 163 ), Decorator ( 175 ), Observer ( 293 ), Strategy ( 315 ). 

8. Inability to alter classes conveniently. Sometimes you have to modify a class that can’t be modified conveniently. Perhaps you need
the source code and don’t have it (as may be the case with a commercial class library). Or maybe any change would require modifying lots
of existing subclasses. Design patterns offer ways to modify classes in such circumstances.
Design patterns: Adapter ( 139 ), Decorator ( 175 ), Visitor ( 331 ).

These examples reflect the flexibility that design patterns can help you build into your software.
How crucial such flexibility is depends on the kind of software you’re building. Let’s look at the role
design patterns play in the development of three broad classes of software: application programs, toolkits,
and frameworks. 

(Vlissides et al., 1995)

 */
    
    public void findMatchingPattern(){
    // get components
        // find dependencies
            // match against pattern templates
                // 
    }
    
}
