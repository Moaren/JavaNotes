# week6
## How to fix deadlock
### a. Always try to use open call. (don’t acquire more than one lock)
```java
    public void setLocation(Point location) {
	boolean reachedDestination;
	synchronized (this) {
        this.location = location;
        reachedDestination = location.equals(destination);
	}

    if (reachedDestination) {
        dispatcher.notifyAvailable(this);
    }
} 
```
- TLDR:关键在于去掉了原来的sychronized修饰语，将这个方程中的lock变成了多段
- In this case, the syhronized is limited to local variable only to avoid multiple locks

### b. All threads acquire the locks they need in a fixed global order
- Key thinking: fix the order by some standard like the value of memeory address)
- 注意以下identityHashCode这个函数的应用！
```java
//abcde | cfe will not result in deadlock
//abcde | efc will result in deadlock
	public void transferMoney (Account fromAccount, Account toAccount, int amount) throws Exception {		
	int fromHash = System.identityHashCode(fromAccount);
	int toHash = System.identityHashCode(toAccount);
	
	//*Compare the address value of from and to function and decided their order based on that
	if (fromHash < toHash) {
		synchronized (fromAccount) {
			synchronized (toAccount) {
				transfer(fromAccount, toAccount, amount);
			}
		}
	}
	else if (fromHash > toHash) {
		synchronized (toAccount) {
			synchronized (fromAccount) {
				transfer(fromAccount, toAccount, amount);
			}
		}			
	}
	else {
		synchronized (tieLock) {
			synchronized (fromAccount) {
				synchronized (toAccount) {
					transfer(fromAccount, toAccount, amount);
				}
			}
		}
	}
}
```
### c. Use the timed tryLock feature of the explicit Lock class instead of intrinsic locking.
```java
class Philosopher extends Thread {
	private final int index;
	private final Fork left;
	private final Fork right;

	public Philosopher (int index, Fork left, Fork right) {
		this.index = index;
		this.left = left;
		this.right = right;
	}

	public void run() {
		Random randomGenerator = new Random();
		try {
			while (true) {
				Thread.sleep(randomGenerator.nextInt(100)); //not sleeping but thinking
				System.out.println("Phil " + index + " finishes thinking.");

				left.lock.lock();//The phil will definitely pick up the left fork first
				left.pickup();
				System.out.println("Phil " + index + " picks up left fork.");
				Thread.sleep(1000);//The test set for deadlock

				if(right.lock.tryLock()){ //Check if the right lock is avliable
					//If so, finish eating and put down both forks. Unlock left and rihgt
					right.pickup();
					System.out.println("Phil " + index + " picks up right fork.");
					Thread.sleep(randomGenerator.nextInt(100)); //eating
					System.out.println("Phil " + index + " finishes eating.");
					left.putdown();
					left.lock.unlock();
					System.out.println("Phil " + index + " puts down left fork.");
					right.putdown();
					right.lock.unlock(); //
					System.out.println("Phil " + index + " puts down right fork.");
				}
				else{//If the right fork is currently not avaliable, give up eating this time and put down left fork
					left.putdown();
					System.out.println("Phil " + index + " puts down left fork.");
					left.lock.unlock();
					System.out.println("Phil " + index + " give up eating this time. ");
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Don't disturb me while I am sleeping, well, thinking.");
		}
	}
}

class Fork {
	private final int index;
	private boolean isAvailable = true;
	public ReentrantLock lock = new ReentrantLock();

	public Fork (int index) {
		this.index = index;
	}

	public synchronized void pickup () throws InterruptedException {
		while (!isAvailable) {
			wait();
		}
		isAvailable = false;
		notifyAll();
	}

	public synchronized void putdown() throws InterruptedException {
		while (isAvailable) {
			wait();
		}
		isAvailable = true;
		notifyAll();
	}

	public String toString () {
		if (isAvailable) {
			return "Fork " + index + " is available.";
		}
		else {
			return "Fork " + index + " is NOT available.";
		}
	}
}
```

- Testing does not work much for sychronization problem.

## Avoid Deadlock from Design
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



	
	
	