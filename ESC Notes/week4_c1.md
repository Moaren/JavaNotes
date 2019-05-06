## Week 4 Cohort 1
1. In code test, DO NOT try to use catch to avoid throwing exception. (Sometimes not everyone know the source code)
2. fail()是做什么的 - 强制在某个地方出错，一般放在Exception或者程序本来不应该到的地方。
3. ParameterizedTest - by writing small piece of code, can test many differnt paramters. EXP↓
```java
@RunWith(Parameterized.class)

public class ParameterizedTest {
	public int sum, a, b;
    
	// classic constructor
	public ParameterizedTest (int sum, int a, int b) { //expected value, arg1, arg2
    	this.sum = sum; 
    	this.a = a; 
    	this.b = b; 
    }

	 // sudiptac: decide the list of parameters to be sent to the class
   @Parameters public static Collection<Object[]> parameters() {
        return Arrays.asList (new Object [][] {{0, 0, 0}, {2, 1, 1}}); 
    }

	 // sudiptac: This test is invoked for each of the parameter sent via parameters()	
   @Test public void additionTest() { 
	   assertEquals(sum, Sum.sum(a, b)); 
   }
}
```
4. 