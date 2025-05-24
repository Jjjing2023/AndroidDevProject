package edu.northeastern.numad25sum_tianjingliu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuicCalc extends AppCompatActivity {
    private TextView display;
    private String currentInput ="";
    private Boolean resetOnNextInput = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quic_calc);

        //initialize display
        display=findViewById(R.id.textView);

        // Set click listeners for number buttons
        int[] numberButtonIds = {
           R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5,
                R.id.button_6, R.id.button_7, R.id.button_8, R.id.button_9
        };

        for (int id: numberButtonIds){
            findViewById(id).setOnClickListener(v->onNumberButtonClick((Button)v));
        }

        // Set click listeners for operation buttons
        findViewById(R.id.button_add).setOnClickListener(v->onOperatorButtonClick("+"));
        findViewById(R.id.button_minus).setOnClickListener(v->onOperatorButtonClick("-"));
        findViewById(R.id.button_equal).setOnClickListener(v->onEqualButtonClick());
        findViewById(R.id.button_del).setOnClickListener(v->onDeleteButtonClick());


    }

    private void onNumberButtonClick(Button button){
        if(resetOnNextInput){
            currentInput="";
            resetOnNextInput=false;
        }

        String digit = button.getText().toString();
        currentInput+=digit;
        updateDisplay();
    }

    private void onOperatorButtonClick(String operator){
        if(currentInput.isEmpty() && !operator.equals("-")){
            // don't allow operators first, except minus for negative numbers
            return;
        }

        // don't allow consecutive operators
        if (!currentInput.isEmpty() && isOperator(currentInput.charAt(currentInput.length() -1))) {
            return;
        }

        currentInput+=operator;
        updateDisplay();
        resetOnNextInput=false;

    }

    private void onEqualButtonClick(){
        if(currentInput.isEmpty()){
            return;
        }

        try{
            // evaluate the expression
            int result = evaluateExpression(currentInput);
            currentInput = String.valueOf(result);
            updateDisplay();
            resetOnNextInput=true;
        }catch (Exception e){
            display.setText("Error");
            currentInput = "";
            resetOnNextInput=true;
        }

    }

    private void onDeleteButtonClick(){
        if(!currentInput.isEmpty()){
            currentInput=currentInput.substring(0, currentInput.length()-1);
            if(currentInput.isEmpty()){
                display.setText("Calc");
            }else{
                updateDisplay();
            }
        }


    }
    private void updateDisplay(){
        display.setText(currentInput);
    }

    private boolean isOperator(char c){
        return c == '+' || c == '-';
    }

    private int evaluateExpression(String expression){
        // Handle negative numbers
//        expression = expression.replaceAll("--", "+");

        // Handle leading negative numbers
        if (expression.startsWith("-")) {
            expression = "0" + expression;
        }

        // split into numbers and operators
        String[] numbers = expression.split("[+-]");
//        String[] operators = expression.split("[0-9.]+");
        String[] operators = expression.split("[0-9]+");

        if(numbers.length == 0) return 0;

        int result = Integer.parseInt(numbers[0]);

        for (int i =1; i < numbers.length;i++){
            String operator = operators[i];
            int num = Integer.parseInt(numbers[i]);

        switch (operator){
            case "+":
                result += num;
                break;
            case "-":
                result -= num;
                break;
        }
        }
         return result;
    }

}