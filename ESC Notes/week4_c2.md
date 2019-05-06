# Week4 Chohort2 
- Black Box Testing: test the requirements without codes
    - based on specification (requriement)
    - to cover as many specifications as possible
- White Box Testing: Tests based on the code
    - based on code
    - cover as much implmentation behavior as possible

## Black Box Testing
- invariant: a condition that is always true
- low-level specification: test a certain calculation function
- high-level specification: complex test (error url; UI operation)
- Black box test cosideration: run the simplest test first (exp: choose the simplest string when testing user case function) ...
- Draw state diagram: draw the most normal test first
- Test Design Policy / How to design a test
    - Every use case should be covered (picture) 
    - more likely many tests for a single use case(use case is complex)
- equivalence class partitioning: 所有预期将表现得“类似“的数据 (exp: all valid email addresses for a email address validity function)
    - exp: sorting-random array; sorted array; exactly in the reverse oder; with duplicates(quite important: <= or <); with negatives
    - reverse a list: 回文式(p开头的 派林壮), empty list, a list with only one element
    - boundayr analysis/corner case: the case that lies on the boundary between two expected cases (exp: su@@sutd.edu.sg)
- ==***BlackBox/Fuctional Test Design**==
   - Check use case Diagrams. For every use case, there must be at least one test.
   - Evey Test should relate to some use case
   - For evey use case, find input space for the respective tests and perform equivalence class partitioning. 
   - For each equivalence class, find middle and boundary value
 - BlackBox testing: only useful to find whether the software could perform a certain function. (Will discuss more in week 10)
 
## White Box Testing
- Control Flow Graph - for advanced testing
- Method Coverage: methods, tests
    - For every method, there is at least one test - Method Coverage: 100%
    - Method Coverage: tested methods/ all methods
    - The difference with the use case is that all the use cases must be covered. So black box test is more strict
    - method == function (exp: in the example given, any test with taht function will give a 100% method coveage)