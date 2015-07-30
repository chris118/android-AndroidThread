package com.xiaopeng.androidthread;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    TextView mInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInfo = (TextView)this.findViewById(R.id.info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Boolean isRun = true;
    int counter = 0;

    Handler mThreadHandler;

    Handler mHandler = new Handler(){
        // handler ��UI�̴߳��������Կ��Ը���UI
        public void handleMessage(android.os.Message msg) {
            //super.handleMessage(msg);

            Bundle b = msg.getData();
            int c = b.getInt("counter");
            mInfo.setText(String.valueOf(c));
        }
    };

    public void start_clicked(View v){

//        // ��ʽһ
//        MyTread thread = new MyTread();
//        thread.start();

//        //��ʽ��
//        MyTask task = new MyTask();
//        Thread thread = new Thread(task,"my thread");
//        thread.start();

         //��ʽ��
        //��дRunnable��ʽ
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isRun){
                    Message msgToUI = mHandler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putInt("counter", counter);
                    msgToUI.setData(b);
                    mHandler.sendMessage(msgToUI);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    counter++;
                }
            }
        }).start();
    }
    public void stop_clicked(View v){
        isRun = false;
    }


    public void startThread_clicked(View v){

        //��дRunnable��ʽ
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();// �������̵߳�Looper�������ڽ�����Ϣ,�ڷ����߳�����û��looper�������ڴ���handlerǰһ��Ҫʹ��prepare()����һ��Looper
                mThreadHandler = new Handler() { // mThreadHandler �ڹ����̴߳���
                    public void handleMessage(Message msg) {

                        // ��ʱ����
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // �������ʱ���� ֪ͨUI�̸߳���UI
                        Message msgToUI = mHandler.obtainMessage();
                        Bundle b = new Bundle();
                        b.putInt("counter", 5000);
                        msgToUI.setData(b);
                        mHandler.sendMessage(msgToUI);
                    }
                };
                Looper.loop();//����һ����Ϣѭ�������̲߳����˳�
            }
        }).start();


//        HandlerThread handlerThread = new HandlerThread("xxx");
//        handlerThread.start(); //����HandlerThread��һ��Ҫ�ǵ�start()
//        mThreadHandler = new Handler(handlerThread.getLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//
//                 //��ʱ����
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                // �������ʱ���� ֪ͨUI�̸߳���UI
//                Message msgToUI = mHandler.obtainMessage();
//                Bundle b = new Bundle();
//                b.putInt("counter", 5000);
//                msgToUI.setData(b);
//                mHandler.sendMessage(msgToUI);
//
//                super.handleMessage(msg);
//            }
//        };
    }
    public void sendMessageToThread_clicked(View v) {
        // UI�߳������߳�ͨ�� mThreadHandler�����̷߳���sendMessage
        mThreadHandler.sendEmptyMessage(0);
    }
    public void stopThread_clicked(View v){
        mThreadHandler.getLooper().quit(); //ʹ�����Ҫͣ��looper���ͷ��߳�
    }



    public void handlerPost_clicked(View v){
      //   post �÷���ʾ
        Handler h = new Handler(this.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                mInfo.setText("Come from postDelayed");
            }
        }, 5000);
    }

    public void synchronized_clicked(View v){
        synchronizedTask task = new synchronizedTask();
        new Thread(task,"A").start();
        new Thread(task,"B").start();
    }
    public class synchronizedTask implements Runnable {

        @Override
        public void run() {
            synchronized (this) {
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                }
            }
        }
    }



    public  class MyTread extends  Thread{

        @Override
        public  void run() {
            while (isRun){
                Message msgToUI = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putInt("counter", counter);
                msgToUI.setData(b);
                mHandler.sendMessage(msgToUI);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
            }
        }
    }

    public class MyTask implements Runnable {

        @Override
        public void run() {
            while (isRun){
                Message msgToUI = mHandler.obtainMessage();
                Bundle b = new Bundle();
                b.putInt("counter", counter);
                msgToUI.setData(b);
                mHandler.sendMessage(msgToUI);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                counter++;
            }
        }
    }
}
