# ISTD Info Sys programming week9

## Static Factory Method
A static factory method​​ is a static method in a class definition
that returns an instance of that class. (​Attention: this is not the factory design pattern
​) (original sentence from the instructor's slides)

Example

```java
public class Tea{
    private boolean sugar;
    private boolean milk;
    
    Tea(boolean sugar, bollean milk){
        this.sugar = sugar;
        this.milk = milk;
    }
    
    public static Tea neverMore(){
        return new Tea(true,true);
    }
    
    public static Tea foreverMore(){
        return new Tea(false,false);
    }
}
```
Then the method can be invoked like this:

```java
Tea tea = Tea.neverMore(); 
//Using ths function to replace the constructor.
//Not recommended for your health
```
Purposes of this:
1. Set constraints to the acess to the constuctor. (Somtimes the constructor can be even declared as private so the user can only use it through static factory method designed by you. Exp: singleton design pattern)
2. You can give your static factory method meaningful names to describe what you are doing.


# Toasts
The message jumps out and disappear for a while in Android app. That is called a toast.

The code recipe:
- Call the **static factory method** makeText() of the toast class.
- makeText() returns a Toast object. The show() instance is required to display the toast.

Example
```java
 Toast.makeText(MainActivity.this,R.string.warning_text,Toast.LENGTH_LONG).show();
 //Meaning of inputs:
 //1. Context object. (which Actiity will your toast be seen.)
 //2. String content (Hardcoded string is not recommended here. Usually this is an id.)
 //3. Duration. Toast.LENGTH_SHORT() 0, Toast.LENGTH_LONG 1 or other int defined by you
 ```
 
 **P.S.** The Toast class constructor is actually public. It is used when you want to customize
the design of your toast. Most of the time, there is no need to.

# Find view by ID & setText
```java
editTextValue = findViewById(R.id.editTextValue);

//Two different ways
textViewExchangeRate.setText(String.valueOf(ExchangeRate.calculateExchangeRate()));
textViewExchangeRate.setText(R.string.default_exchange_rate);
//        textViewExchangeRate.setText("2.95"); //Hardcoding the string is encouraged. This way not recommended.
```

# setOnClickListener
```java
buttonConvert = findViewById(R.id.buttonConvert);

buttonConvert.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String userInput = editTextValue.getText().toString();
        if(userInput.equals("")){
            //Two different methods
            Log.i(TAG,"empty string entered: "+ userInput);
            Log.i(TAG,getString(R.string.warning_text)); //This log text seems not to be shown in the real App.

            Toast.makeText(MainActivity.this,R.string.warning_text,Toast.LENGTH_LONG).show();
        }
        else{
            double result = Double.parseDouble(userInput)*R.string.default_exchange_rate;
            textViewResult.setText(String.valueOf(result));
        }

    }
});
```

# XML string example
```xml
<resources>
    <string name="app_name">Exchange Rate </string>
    <string name="action_settings">Settings</string>
    <string name="set_exchange_rate">Set Exchange Rate</string>
    <string name="main_activity_name">Convert Currency</string>
    <string name="default_exchange_rate">2.95</string>
    <string name="warning_text">Please enter a valid input.</string>

</resources>
```
"Name" here functions same as ID. The calling method is like *R.string.default_exchange_rate* .