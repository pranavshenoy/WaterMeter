package com.slateandpencil.watermeter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyService extends Service {
    private Socket client;
    private InetAddress ip;
    private int port;
    private int t_num;
    private mythread mt;
    public MyService() {

    }

    private class mythread extends Thread{
        @Override
        public void run(){
            SQLiteDatabase sb=openOrCreateDatabase("watermeter",MODE_PRIVATE,null);
            Cursor resultset=sb.rawQuery("select num,ip,port from tank where enable=1",null);
            while (!isInterrupted()){
                while(resultset.moveToNext()){
                    t_num=Integer.parseInt(resultset.getString(0));
                    try {
                        ip=InetAddress.getByName(resultset.getString(1));
                        port=Integer.parseInt(resultset.getString(2));
                        client=new Socket(ip,port);
                        ObjectInputStream objectinputstream=new ObjectInputStream(client.getInputStream());
                        int lvl=Integer.parseInt((objectinputstream.readObject()).toString());
                    }
                    catch (IOException|ClassNotFoundException e){
                        e.printStackTrace();
                    }

                }

            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        if(mt.isAlive()) {
            mt.interrupt();
            try {
                mt.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        mt = new mythread();
        mt.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mt.interrupt();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
   /* public class InfoPasser{
        int checkpoint;
        String msg;
    }
    public class TankConnect extends AsyncTask<Void, Void, InfoPasser> {
        private ObjectInputStream objectInputStream;
        @Override
        protected InfoPasser doInBackground(Void... params){
            InfoPasser infoPasser=new InfoPasser();
            infoPasser.checkpoint=0;
            infoPasser.msg="null";
            if(!isCancelled()){
                try{
                    ip=InetAddress.getByName("192.168.0.101");
                    infoPasser.checkpoint=2;
                    client=new Socket(ip,9137);
                    infoPasser.checkpoint=3;
                    objectInputStream=new ObjectInputStream(client.getInputStream());
                    infoPasser.checkpoint=4;
                    infoPasser.msg=(objectInputStream.readObject()).toString();
                    infoPasser.checkpoint=5;
                }
                catch(UnknownHostException e){
                    e.printStackTrace();
                }
                catch (IOException e){
                    e.printStackTrace();
                }

                catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        return infoPasser;
        }
        @Override
        protected void onPostExecute(InfoPasser infoPasser){
            Toast.makeText(MyService.this,""+count, Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this,""+infoPasser.checkpoint, Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this,infoPasser.msg, Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MyService.this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Data Arrived")
                            .setContentText(infoPasser.msg)
                            .setDefaults(Notification.DEFAULT_ALL);
            Intent resultIntent = new Intent(MyService.this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(11, mBuilder.build());
        }
    }*/
}
