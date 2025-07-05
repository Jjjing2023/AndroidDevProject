package edu.northeastern.numad25sum_tianjingliu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrimeDirective extends AppCompatActivity {
    private static final String TAG = "PrimeDirective";
    private TextView currentText;
    private TextView latestText;
    private volatile boolean isSearching = false;
    private int currentNumber = 3;
    private Thread workerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prime_directive);

        currentText = findViewById(R.id.current);
        latestText = findViewById(R.id.latest);
    }

    public void findPrimesClick(View view) {
        // avoid situations when users keep clicking while searching
        if (isSearching) return;
        isSearching = true;

        // only start from 3 if user manually start searching again
        if (view != null){
            currentNumber = 3;
        }

        workerThread= new Thread(() -> {
            int i = currentNumber;
            Log.d("PrimeDirective", "Starting search at: " + currentNumber);
            while(isSearching){
                if(i%100 == 3){
                    final int current = i;
                    runOnUiThread(()->currentText.setText("Checking: " + current));
                }

                if(isPrime(i)){
                    final int prime = i;
                    runOnUiThread(()->latestText.setText("Latest Prime: "+prime));
                }
                i+=2;
                currentNumber = i;
            }
            runOnUiThread(() -> currentText.setText("Search stopped"));
        });
        workerThread.start();

    }
    public void terminateSearchClick(View view){
        isSearching = false;
    }
    private boolean isPrime(int number){
        for(int i = 2; i < number;i++){
            if(number%i==0)return false;
        }
        return true;
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);

        CheckBox pacifier = findViewById(R.id.switcher);
        outState.putBoolean("pacifier_checked", pacifier.isChecked());
        outState.putBoolean("searching", isSearching);
        outState.putInt("current_number", currentNumber);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        CheckBox pacifier = findViewById(R.id.switcher);
        pacifier.setChecked(savedInstanceState.getBoolean("pacifier_checked", false));

        currentNumber = savedInstanceState.getInt("current_number", currentNumber);

        if(savedInstanceState.getBoolean("searching", false)){
            findPrimesClick(null);
        }
    }

}