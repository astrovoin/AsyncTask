package com.astro.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editText1;
    EditText editText2;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    make();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void make() {
        // Создаем объект асинхронной задачи (передаем ей слушатель)
        Requester requester = new Requester();
        // Запускаем асинхронную задачу
        requester.execute();
    }

    public class Requester extends AsyncTask<Void, Long, Long> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            textView.setText((getString(R.string.begin)));
        }


        @Override
        protected void onProgressUpdate(Long... values) {

            super.onProgressUpdate(values);
            progressBar.setVisibility(ProgressBar.VISIBLE); // не работает
        }


        // Выполнить таск в фоновом потоке
        @Override
        protected Long doInBackground(Void... Params) {

            Long b;
            try {

                Ackermann ackermann = new Ackermann();
                b = ackermann.get(Long.parseLong(String.valueOf(editText1.getText())), Long.parseLong(String.valueOf(editText2.getText())));
                return b;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }

        // Выдать результат (работает в основном потоке UI)
        @Override
        protected void onPostExecute(Long content) {
            textView.setText(String.valueOf(content));
            progressBar.setVisibility(ProgressBar.INVISIBLE);
        }
    }


    private static class Ackermann {

        private long ack(long m, long n) {


            return m == 0 ? n + 1 : n == 0 ? ack(m - 1, 1) : ack(m - 1, ack(m, n - 1));

            // Этот кусок работает также. переполнение стека
              /* if (m == 0) {
                    return n + 1;
                }
                else if (m>0 && n == 0) {
                    return  ack(m-1,1);
                }
                else if (m>0 && n>0){
                    return  ack(m-1,ack(m,n-1));
                }

                return 0;
                */

        }

        private long get(long m, long n) {
            if (m < 0 || n < 0) {
                throw new IllegalArgumentException();
            }

            return ack(m, n);
        }
    }

}

