## Synchronizer
- A synchronizer is an object that coordinates the control flow of threads based on its state.
Semaphore,CyclicBarrier,CountDownLatch,Phaser
## Semaphores
```java
public class SemaphoreExample {	
	  private final Semaphore roomOrganizer=new Semaphore(3, true); //true: first come, first serve

	  public static void main(String[] args) {
		  System.out.println("lalal");		  SemaphoreExample test=new SemaphoreExample();
		  test.mystart();		  
	  }
	  
	  public void mystart() {
		  for(int i=0; i<10; i++) {
				 People people=new People();
				 people.start();
			 }
	  }
	  
	  class People extends Thread {
		  public void run() {
			  try {
				  roomOrganizer.acquire();
			  } catch (InterruptedException e) {
				  System.out.println("received InterruptedException");
				  return;
			  }
		
			  System.out.println("Thread "+this.getId()+" starts to use the room");
		
			  try {
				  sleep(1000);
			  } catch (InterruptedException e) {		  
			  }
		
			  System.out.println("Thread "+this.getId()+" leaves the room\n");
			  roomOrganizer.release();
		  }
	  }
}
```
## Cyclic Barriers
```java
public class BarrierExample {

	private static class Task implements Runnable {
        private CyclicBarrier barrier;
        
        public Task(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is waiting on barrier");
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " has crossed the barrier");
            } catch (Exception ex) {
                Logger.getLogger(BarrierExample.class.getName()).log(Level.SEVERE, null, ex);
            }       
        }
    }

    public static void main(String args[]) {
    	//A CyclicBarrier supports an optional Runnable command that is run once per barrier point, 
    	//after the last thread in the party arrives, but before any threads are released. This 
    	//barrier action is useful for updating shared-state before any of the parties continue.
        final CyclicBarrier cb = new CyclicBarrier(3, new Runnable(){
            public void run(){
                //This task will be executed once all thread reaches barrier
                System.out.println("All parties are arrived at barrier, lets play");
            }
        });

        //starting each of thread
        Thread t1 = new Thread(new Task(cb), "Thread 1");
        Thread t2 = new Thread(new Task(cb), "Thread 2");
        Thread t3 = new Thread(new Task(cb), "Thread 3");

        t1.start();
        t2.start();
        t3.start();  
    }
}
```
## MyCyclicBarrier
```java

public class MyCyclicBarrierAnswer {
	private int count = 0;
	private int initialcount = 0;
	private Runnable torun;

	public MyCyclicBarrierAnswer(int count, Runnable torun) {
		this.count = count;
		initialcount = count;
		this.torun = torun;
	}

	public MyCyclicBarrierAnswer(int count) {
		this.count = count;
		initialcount = count;
	}

	// complete the implementation below.
	// hint: use wait(), notifyAll()
	public synchronized void await() throws Exception {
		count--;
		if (count > 0) {
			wait();
		} else {
			notifyAll();
			if (torun != null) {
				torun.run();
			}
			count = initialcount;
		}
	}
}
```
## CountDownLatch
```java
public class CountDownLatchExample {
	public static void main(String args[]) {
	    final CountDownLatch latch = new CountDownLatch(3);
	    Thread cacheService = new Thread(new Service("CacheService", 1000, latch));
	    Thread alertService = new Thread(new Service("AlertService", 1000, latch));
	    Thread validationService = new Thread(new Service("ValidationService", 1000, latch));
	      
	    cacheService.start(); //separate thread will initialize CacheService
	    alertService.start(); //another thread for AlertService initialization
	    validationService.start();      
	    try{
	        latch.await();  //main thread is waiting on CountDownLatch to finish
	        Thread.sleep(1000);
	        System.out.println("All services are up, Application is starting now");
	    }catch(InterruptedException ie){
	        ie.printStackTrace();
	    }  }	  }

class Service implements Runnable{
	private final String name;
	private final int timeToStart;
	private final CountDownLatch latch;
	  
	public Service(String name, int timeToStart, CountDownLatch latch){
	    this.name = name;
	    this.timeToStart = timeToStart;
	    this.latch = latch;
	}
	public void run() {
	    try {
	    	Thread.sleep(timeToStart);
	    } catch (InterruptedException ex) {
	        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
	    }
	    System.out.println( name + " is Up");
	    latch.countDown(); //reduce count of CountDownLatch by 1
	    System.out.println( name + " has passed countdown");
	}  }
```
## Phaser
```java

public class PhaserExample
{
    public static void main(String[] args) throws InterruptedException
    {
		  Phaser phaser = new Phaser();
		  phaser.register();//register self... phaser waiting for 1 party (thread)
                  int phasecount = phaser.getPhase();
 		  System.out.println("Phasecount is "+phasecount);
		  new PhaserExample().testPhaser(phaser,2000);//phaser waiting for 2 parties
		  new PhaserExample().testPhaser(phaser,4000);//phaser waiting for 3 parties
		  new PhaserExample().testPhaser(phaser,6000);//phaser waiting for 4 parties
		  //now that all threads are initiated, we will de-register main thread 
		  //so that the barrier condition of 3 thread arrival is meet.
		  phaser.arriveAndDeregister();
		  System.out.println(phaser.getRegisteredParties() + " have registered.");
          Thread.sleep(10000);
          phasecount = phaser.getPhase();
 		  System.out.println("Phasecount is "+phasecount);

    }

    private void testPhaser(final Phaser phaser,final int sleepTime)
    {
		phaser.register();
		System.out.println(phaser.getRegisteredParties() + " have registered.");
		new Thread(){
				@Override
				public void run() 
					{
						  try
							{
							   System.out.println(Thread.currentThread().getName()+" arrived");
							   phaser.arriveAndAwaitAdvance();//threads register arrival to the phaser.
							   //phaser.arrive();
							   //System.out.println(Thread.currentThread().getName()+" pass arrived");
							   Thread.sleep(sleepTime);
							}

						   catch (InterruptedException e)
						   {
							  e.printStackTrace();
						   }
						System.out.println(Thread.currentThread().getName()+" after passing barrier");
					}
			   }.start();
    }
}
```
1. CountDownLatch
    - Created with a fixed number of threads
    - Cannot be reset
    - Allow threads to wait (method await) or continue with its execution (method countdown())
2. Cyclic Barrier 
    - Can be reset.
    - Does not provide a method for the threads to advance. - - The threads have to wait till all the threads arrive.
    - Created with fixed number of threads.
3. Phaser 
    - Number of threads need not be known at Phaser creation time. They can be added dynamically.
    - Can be reset and hence is, reusable.
    - Allows threads to wait (method arriveAndAwaitAdvance()) or continue with its execution(method arrive()).
    - Supports multiple Phases.
## Testing for Concurrency
1. 识别需求：看代码
2. unit test:随便瞎杰宝写unit test
3. test for concurrency:写涉及多线程的测试项（用多线程去完成某个任务看会不会出错）
## Additional Synchronization
```java

public class PutTakeTest {
    protected static final ExecutorService pool = Executors.newCachedThreadPool();
    protected CyclicBarrier barrier;
    protected final BoundedBuffer<Integer> bb;
    protected final int nTrials, nPairs;
    protected final AtomicInteger putSum = new AtomicInteger(0); //for testing
    protected final AtomicInteger takeSum = new AtomicInteger(0);//for testing

    public static void main(String[] args) throws Exception {
        new PutTakeTest(10, 10, 100000).test(); // sample parameters
        pool.shutdown();
    }
    
    public PutTakeTest(int capacity, int npairs, int ntrials) {
        this.bb = new BoundedBuffer<Integer>(capacity);
        this.nTrials = ntrials;
        this.nPairs = npairs;
        this.barrier = new CyclicBarrier(npairs * 2 + 1);
    }

    void test() {
        try {
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await(); // wait for all threads to be ready
            barrier.await(); // wait for all threads to finish
            assert(putSum.get() == takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    class Producer implements Runnable {
        public void run() {
            try {
                int seed = (this.hashCode() ^ (int) System.nanoTime());
                int sum = 0;
                barrier.await();
                for (int i = nTrials; i > 0; --i) {
                    bb.put(seed);
                    sum += seed;
                    seed = xorShift(seed);
                }
                putSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class Consumer implements Runnable {
        public void run() {
            try {
                barrier.await();
                int sum = 0;
                for (int i = nTrials; i > 0; --i) {
                    sum += bb.take();
                }
                takeSum.getAndAdd(sum);
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```