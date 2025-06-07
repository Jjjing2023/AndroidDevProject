package edu.northeastern.numad25sum_tianjingliu;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PrimeDirective extends AppCompatActivity {
    private TextView currentText;
    private TextView latestText;
    private volatile boolean isSearching = false;
    private Thread workerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.prime_directive);
        currentText = findViewById(R.id.current);
        latestText = findViewById(R.id.latest);

    }

    public void findPrimesClick(View view) {
        if (isSearching) return;
        isSearching = true;

        workerThread= new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 3;
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
                }
                runOnUiThread(() -> currentText.setText("Search stopped"));
            }
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

}