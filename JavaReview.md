## 1. What is JAVA?
Java is a high-level programming language and is platform independent.

## 2. What are the features in JAVA?
- OOP concepts:
    - Object-oriented
    - Inheritance
    - Encapsulation
    - Polymorphism
    - Abstraction
- Platform independent
- High performance: 
    - JIT (Just In Time compiler) enables high performance in Java. JIT converts the bytecode into machine language and then JVM starts the execution. (never learnt before)
- Multi-threaded:
    - A flow of execution is known as a Thread. JVM creates a thread which is called main thread. The user can create multiple threads by extending the thread class or by implementing Runnable interface.

## 3. How does Java enable high performance? (R)
Java uses Just In Time compiler to enable high performance. JIT is used to convert the instructions into bytecodes.

## 4. What are the JAVA IDE's?
Eclipse, Android Studio and NetBeans are the IDE’s of JAVA.

## 5. What is an Object?
An instance of a class is called object. The object has state and behavior.
Whenever the JVM reads the "new()" keyword then it will create an instance of that class.

(In class) Object == Class
```java
Addion add = new Addition(); //Object creation
```

## 6. What is Inheritance?
Inheritance means one class can extend to another class. So that the codes can be reused from one class to another class.

Existing class is called the super class while the derived class is called a sub class.

==Inheritance== is applicable for ==public== and ==protected== members only. ==Private members can't== be inherited.

## 7. Will the constructor be inherited?
```java
public class JavaReveiewExercise {
    public static void main(String[] args) {
        Sub_class a = new Sub_class();
        System.out.println(a.a);
    }
}

//# a. Can constructor be inherited in sub class?

class Super_class{
    int a;
    public Super_class() {
        System.out.println("This is in super class");
        a = 1;
    }
}

class Sub_class extends Super_class{
    public Sub_class() {
        super();
        System.out.println("This is a sub class");
        a = 2;
    }
}

//# a. OUTPUT:
    //This is in super class
    //This is a sub class
    //2
```
So constructor will be default inherited in sub class. ==(By calling super();)== Even if the variable is rewritten, the constructor of the super class will be executed one time before.

## 8. What is Encapsulation?
Purposes:
- Protects the cdoe from others.
- Code maintainability.

~ | inside the class | inside the package | sub class | other
:-:|:-:|:-:|:-:|:-:
public | √ | √ | √ | √
protected | √ | √ | √ | ×
default | √ | √ | × | ×
private | √ | × | × | ×

For encapsulation purpose, we need to make all the instance variables as ==private== and create ==setter== and ==getter== for those variables.

## 9. What is Polymorphism?


## ※10. Difference between Abstract class and Interface.
Difference between Abstract Class and Interface are as follows:
- Abstract Class:
    - Have a default constructor and it is called whenever the concrete subclass is instanitiated.
    - Contains Abstract methods as well as Non-Abstract methods.
    - The class which extends the abstract class should not require implementing all the methods, only Abstract methods need to be implemented in the concrete sub-class.
    - Abstract Class contains instance variables.
- Interface
    - Doesn't have any constructor and couldn't be instantiated.
    - Can only contain abstract methods and those methods should be declared.
    - Classes which implement the interface should provide the implementation for all the methods.
    - The interface contains onlt constants.