package com.example.lab4_tmcmanus3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button start_button;
    private Button stop_button;
    private TextView download_progress;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start_button = findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {startDownload(view);}
        });
        stop_button = findViewById(R.id.stop_button);
        stop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {stopDownload(view);}
        });
        download_progress = findViewById(R.id.download_progress);

    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                start_button.setText("DOWNLOADING...");
                download_progress.setText("Download Progress: 0%");
            }
        });

        for (int downloadProgress = 10; downloadProgress <= 100; downloadProgress+=10) {
            Log.d(TAG, "Download Progress: " + downloadProgress + "%");
            if(stopThread) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        start_button.setText("Start");
                        download_progress.setText("");
                    }
                });
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    download_progress.setText("Download Progress: " + finalDownloadProgress + "%");
                }
            });
            if(downloadProgress == 100) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                start_button.setText("Start");
                download_progress.setText("");
            }
        });
    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }

    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }
}