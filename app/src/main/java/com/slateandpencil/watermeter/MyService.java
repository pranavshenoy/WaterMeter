package com.slateandpencil.watermeter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
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
    public MyService() {

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
        new TankConnect().execute();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    public class InfoPasser{
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
            try{
                ip=InetAddress.getByName("192.168.1.3");
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
        return infoPasser;
        }
        @Override
        protected void onPostExecute(InfoPasser infoPasser){
            Toast.makeText(MyService.this,""+infoPasser.checkpoint, Toast.LENGTH_SHORT).show();
            Toast.makeText(MyService.this,infoPasser.msg, Toast.LENGTH_SHORT).show();
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MyService.this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Data Arrived")
                            .setContentText(infoPasser.msg);
                            //.setDefaults(Notification.DEFAULT_ALL);
// Creates an explicit intent for an Activity in your app
            Intent resultIntent = new Intent(MyService.this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(MyService.this);
// Adds the back stack for the Intent (but not the Intent itself)
            stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(11, mBuilder.build());
        }
    }
}
