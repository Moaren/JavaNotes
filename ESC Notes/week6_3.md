- Design a Thread-Safe Class
    - 1. Identify the variables that from the object's state;
    - 2. Identify the requiements that constrain that state variables;
    - 3. Establish a policy for managing concurrent access to the objects state.
    - 4. Implement the policy.
- locking policy: from variable to lock
    - same variable: always use the same lock decide    
    - variables that are related to each other: If you try to maintain the relationship between two variables, you should try to put them under the same lock. (how to judge their relations: looking their requirements)
    - 
    - 