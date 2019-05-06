# week5_c1_ThreadBasics
## How to design multi-thread system
1. Use thread + run mathid
2. Use thread + runnable class


## Thread Code Examples
### Thread Class
```java
class Thread1 extends Thread {
	private double K, a, t, value;
	
	public Thread1 (double K, double a, double t) { //constructor
		this.K = K;
		this.a = a;
		this.t = t;
	}
	
	public void run () { //The runnable part in thread class
		value = 2*K*a*t;
	}
	
	public double getValue() {
		return value;
	}
}
```

### Thread Call and Join
```java
public class HelloWorldThread {
	public static void main (String[] args) {
		double K = 20, a = 1.2, t = 2;
		
		Thread2 thread2 = new Thread2(a,t); //Thread class initialization
		Thread1 thread1 = new Thread1(K, a, t);
		thread1.start(); //Thread Start
		thread2.start();
		
		try {
			thread1.join(); //Thread join - thread result allocation
			thread2.join();
			
			System.out.println("Value of Expression: " + thread1.getValue()*thread2.getValue());
		}
		catch (InterruptedException e) {
			System.out.println("A thread didn't finish!");
		}
	}
}
```

## Functions summary
```java
//Thread赋值
Threadx thread = new Threadx(a,t); //通过constructor赋值
thread.start() //开始thread.run中的内容
thread.join() //收集thread运行结果
```

## Usage of runnable
Summary: implement了runnable的interface可以直接用于Thread的生成 不需要额外extends Thread
```
public class RunnableExample {    
    public static void main(String[] args) throws Exception {
    	int[] array = {1,2,3,4,5,6,7,8,9};
    	Summer summer1 = new Summer (array, 0, array.length/2+1);
    	Summer summer2 = new Summer (array, array.length/2+1, array.length);
    	
    	Thread thread1 = new Thread(summer1);
    	Thread thread2 = new Thread(summer2);
		thread1.start();
		thread2.start();
		
		try {
			thread1.join();
			thread2.join();
			
			System.out.println("The sum is: " + (summer1.getSum() + summer2.getSum()));
		}
		catch (InterruptedException e) {
			System.out.println("A thread didn't finish!");
		}
    }
}

class Summer implements Runnable {
    	int[] array;
    	int lower;
    	int upper;
    	int sum = 0;
    	
    	public Summer (int[] array, int lower, int upper) {
    		this.array = array;
    		this.lower = lower;
    		this.upper = upper;
    	}
    	
    	public void run() {
    		for (int i = lower; i < upper; i++) {
    			sum += array[i];
    		}
    	}
    	
    	public int getSum() {
    		return sum;
    	}
}

```

## Stop a thread
- The only way is to use interrput.
- TLDR: put isInterrupted() in the Thread class or use InterruptedException in the Thread class
- Examples:
```
//Example 1
package Week5;
public class InterruptExample1 {    
	public static void main(String args[]){
		AThread t1=new AThread();
		t1.start();
		try{
			Thread.sleep(1000);
			System.out.println("interrupting");
			t1.interrupt();
			
			if (t1.isInterrupted()) {
				System.out.println("t1 is interrupted");				
			}
		}
		catch (Exception e) {
			System.out.println("Exception captured.");
		}
	}
}

class AThread extends Thread{
	public void run(){
		try{
			System.out.println("running");
			Thread.sleep(10000);
			System.out.println("done sleeping");
		}
		catch (InterruptedException e){
			System.out.println("InterruptedException caught");
			//throw new RuntimeException("Thread interrupted...");
		}
	}
}
```
```
//Example 2
package Week5;
class InterruptExample2 extends Thread{  
	private int id; 

	public InterruptExample2 (int id) {
		this.id = id; 
	}
	
	public void run(){  
		for(int i=1;i<=10000000;i++){  
			if (this.isInterrupted()) {
				break;
			}
			System.out.println("thread " + id + " printing " + i);    
		}//end of for loop  
	}  
  
	public static void main(String args[]) throws InterruptedException{  
  		InterruptExample2 t2=new InterruptExample2(2);    
		t2.start();
		Thread.sleep(100);
		t2.interrupt();  	
		System.out.println("thread is interrupted? " + t2.isInterrupted());	
		System.out.println("thread 2 is alive? " + t2.isAlive());	
	}  
}  
```

