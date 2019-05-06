# Week5_c2
## Issues with thread
Multi-threaded programs could have race conditions, visibility issues, deadlocks

## Race Conditions
- Key reason: many common operations in programming are actually not atomic. The problem will become more obvious whenusing multi threading.
- Example: ++
### Basic Solution
- How to solve: use AtomicXX (只需把原有的参数改为atomic即可 不放例子了)
- But this won't work if the program invloves other more complex operations(↓ example:get and set)
```java
if (SecondError.amount.get() >= 1000) {
			System.oSecondErrorut.println("I am doing something.");			
			SecondError.amount.set(SecondError.amount.get()-1000);;
			whatIGot = 1000;
		}
```
### A bit more complex solution
- How to solve: use lock
- 小hint：java中的值传递与引用传递（后面会用到）https://my.oschina.net/u/3687664/blog/2875998
- TLDR: java中之存只值传递 但是如果传递的是对象 传递后对对象的相应改变会反应到原对象上（但是这仍然属于值传递的范围之内 因为对象指向的地址没有发生变化）
- Example ↓:
```java
	public void run () {
		synchronized (amount) {
			if (amount.intValue() >= 1000) {
				System.out.println("I am doing something.");			
				amount.addAndGet(-1000);
				whatIGot = 1000;
			}		
		}
	}	
```
### No busy waiting: wait() and notify()
Example↓
```java
package Week8;

public class BufferFixed {
	public int SIZE;	
	private Object[] objects;
	private int count = 0;

	public BufferFixed (int size) {
		SIZE = size;
		objects = new Object[SIZE];
	}
	
	public synchronized void addItem (Object object) throws Exception {
		while (count == SIZE-1) {
			wait();
		}
		objects[count] = object;
		count++;
		notifyAll();
	}
	
	public synchronized Object removeItem() throws Exception {
		while (count == 0) {
			wait();
		}
		count--;
		Object object = objects[count];
		notifyAll();
		return object;
	}
}
```
## Visibility
Solutions:
- Volatile: just add a 'volatile' modifier before the parameter that should be visible
- lock(可以考虑把需要保证visible的参数作为lock)（重要！！！）
- TLDR: 从底下的例子可以看出，visibility指的是在程序运行的每个阶段 某个变量对其他线程都保持可见性，所以仅仅只在程序开始和结束运行之间加lock是不够的
```java
public class ExperimentFix2 {
    private static int MY_INT = 0;
    private static final Object obj = new Object();
    
    public static void main(String[] args) throws InterruptedException
    {
        new ChangeListener().start();
        System.out.println("Waiting two seconds so the JIT will probably optimize ChangeListener");
        Thread.sleep(2000);
 
        new ChangeMaker().start();
    }
 
    static class ChangeListener extends Thread {
        public void run() {
        	int local_value;
            synchronized (obj) {
            	local_value = MY_INT;
            }
            while (  local_value< 5){
                synchronized (obj) {
                if( local_value!= MY_INT){
                    System.out.println("Got Change for MY_INT : "+ MY_INT);
                     local_value= MY_INT;
                }
                }
            }
        }
    }
 
    static class ChangeMaker extends Thread{
        public void run() {
 
            int local_value = MY_INT;
            while (MY_INT <5){
                System.out.println("Incrementing MY_INT to " + (local_value + 1));
                synchronized (obj) {
                	MY_INT = ++local_value;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }
    }
}
```
## Deadlock
Deadlock is the situation when two or more threads are both waiting for the others to complete, forever.
- Common Reason:
```java

import java.util.*;

public class DemonstrateDeadlock {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ACCOUNTS = 5;

    public static void main(String[] args) {
        final Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < accounts.length; i++)
            accounts[i] = new Account();

        for (int i = 0; i < NUM_THREADS; i++)
            new TransferThread().start();
    }    accounts
}

class TransferThread extends Thread {
    private static final int NUM_ITERATIONS = 100;
    private Account[] accounts;
    
    public TransferThread (Account[] accounts) {
    	this.accounts = accounts;
    }
    
    public void run() {
    	final Random rnd = new Random();
        for (int i = 0; i < NUM_ITERATIONS; i++) {
            int fromAcct = rnd.nextInt(accounts.length);
            int toAcct = rnd.nextInt(accounts.length);
            int amount = rnd.nextInt(1000);
            transferMoney(accounts[fromAcct], accounts[toAcct], amount);
        }
    }
    
    public void transferMoney (Account from, Account to, int amount) {
        synchronized (from) {
        	synchronized (to) {
        		if (from.getBalance() < amount) {
        			System.out.println("Insufficient Fund");
        		}
        		else {
        			from.debit(amount);
        			to.credit(amount);
        		}
        	}
        }
    }
}

class Account {
//...省略
}
```
- TLDR: sychronized那里用了两个lock 可能会在获取两个lock之间卡住

- Common Reason 2:
```java

import java.util.HashSet;
import java.util.Set;


class Taxi {
    private Point location, destination;
    private final Dispatcher dispatcher;

    public Taxi(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

	public synchronized Point getLocation() {
        return location;
    }

    public synchronized void setLocation(Point location) {
        this.location = location;
        if (location.equals(destination))
            dispatcher.notifyAvailable(this);
    }

    public synchronized Point getDestination() {
        return destination;
    }
}

class Dispatcher {
    private final Set<Taxi> taxis;
    private final Set<Taxi> availableTaxis;

    public Dispatcher() {
        taxis = new HashSet<Taxi>();
        availableTaxis = new HashSet<Taxi>();
    }

    public synchronized void addTaxi(Taxi taxi) {
        taxis.add(taxi);
    }

    public synchronized void notifyAvailable(Taxi taxi) {
        availableTaxis.add(taxi);
    }

    public synchronized Image getImage() {
        Image image = new Image();
        for (Taxi t : taxis)
            image.drawMarker(t.getLocation());
        return image;
    }
}

class Image {
    public void drawMarker(Point p) {
    }
}

class Point {
	
}
```
TLDR: 核心理由 - Taxi和Dispather都不止一个instance！！！在class层面上允许两个class互相等对方（getLocation与notifyAvailiable）直接后果可能就是三个class形成deadlock

## From Scratch: Identtifying Requirements
### Example
```java

public class MyStackThreadSafeComplete {
	private final int maxSize;
	private long[] stackArray; //guarded by "this"
	private int top; //invariant: top < stackArray.length && top >= -1; guarded by "this"
	
	//pre-condition: s > 0
	//post-condition: maxSize == s && top == -1 && stackArray != null
	public MyStackThreadSafeComplete(int s) { //Do we need "synchronized" for the constructor?
		maxSize = s;
	    stackArray = new long[maxSize];
	    top = -1;
	}
	
	//pre-condition: top >= 0
	//post-condition: the top element is removed
	public synchronized long pop() {
		long toReturn; 
		
		while (isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		toReturn = stackArray[top--];
		notifyAll();			
	    return toReturn;
	}	

	//pre-condition: true
	//post-condition: the elements are un-changed. the return value is true iff the stack is empty.
	public synchronized boolean isEmpty() {
		return (top == -1);
	}

	public synchronized long peek(){
		long toReturn;

		while(isEmpty()) {
			try {
				wait();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		toReturn = stackArray[top];
		notifyAll();
		return toReturn;
	}

	public synchronized boolean isFull() {
		return (top == maxSize - 1);
	}
}
```
## Never Add a "synchronized" before the constructor!!!
## Extending Class
- Using a private object as a lock is both flexible and risky. It means the lock can be used outside that class. The object implemented with a private object lock can not be used as a lock itself.(exp: concurrentHashMap)
- static method also cannot be modified by 'synchronized'
## Composition
TLDR: 把所有method前加上synchronized的修饰语
```java
//this is done through composition
public class SafeStack<E> {
	private Stack<E> stack = new Stack<E>();
	
	public synchronized boolean pushIfNotFull (E e) {
		if (stack.size() != stack.capacity()) {
			stack.push(e);
			return true;
		}
		
		return false;
	}
	
	public synchronized E popIfNotEmpty () {
		if (!stack.isEmpty()) {
			return stack.pop();
		}
		
		return null;
	}
	
	public synchronized boolean empty () {
		return stack.isEmpty();
	}
	
	public synchronized boolean add (E e) {
		return stack.add(e);
	}
	
	//the following are other methods in Stack class which are omitted.
}
//this is done through client-side locking
class SafeStack2<E> {
	public Stack<E> stack = new Stack<E>();
	
	public boolean pushIfNotFull (E e) {
		synchronized (stack) {
			if (stack.size() != stack.capacity()) {
				stack.push(e);
				return true;
			}
		
			return false;
		}
	}
	
	public E popIfNotEmpty () {
		synchronized (stack) {
			if (!stack.isEmpty()) {
				return stack.pop();
			}
		
			return null;
		}
	}
}
```
## Encapsulating Data
TLDR: 不能直接把参数设成public 也不能把参数的get method设置成public（赋值给引用对象之后效果一样）三重封印↓
```java

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//is this class thread-safe?
public class TrackerFixed {
	//@guarded by this
	private final Map<String, MutablePoint> locations;
	
	//the reference locations, is it going to be an escape?
	public TrackerFixed(Map<String, MutablePoint> locations) {
		this.locations = depeClone(locations);
	}
	
	//is this an escape?
	public synchronized Map<String, MutablePoint> getLocations () {
		return deepClone(locations);
	}
	
	//is this an escape?
	public synchronized MutablePoint getLocation (String id) {
		MutablePoint loc = locations.get(id);
		return new MutablePoint(loc.x, loc.y);
	}
	
	public synchronized void setLocation (String id, int x, int y) {
		MutablePoint loc = locations.get(id);
		
		if (loc == null) {
			throw new IllegalArgumentException ("No such ID: " + id);			
		}
		
		loc.x = x;
		loc.y = y;
	}
	
	private static Map<String, MutablePoint> deepClone (Map<String, MutablePoint> map) {
		Map<String, MutablePoint> toReturn = new ConcurrentHashMap<String, MutablePoint> ();
		for (String key: map.keySet()) {
			MutablePoint newPoint = new MutablePoint(map.get(key).x, map.get(key).y); 
			toReturn.put(key, newPoint);
		}
		
		return toReturn;
	}
}

class MutablePoint {
	public int x, y;

	public MutablePoint (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public MutablePoint (MutablePoint p) {
		this.x = p.x;
		this.y = p.y;
	}
}
```


