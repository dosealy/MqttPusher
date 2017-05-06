package de.eclipsemagazin.mqtt.push;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;


public class PushCallback implements MqttCallback {

    private ContextWrapper context;

    public PushCallback(ContextWrapper context) {

        this.context = context;
    }

    @Override
    public void connectionLost(Throwable cause) {
        //���ӶϿ�ʱ�Ļص�������������������������
    }

    @SuppressLint("NewApi")
	@Override
    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
    	//������Ϣ����ʱ�Ļص�����
        final NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        final Notification notification; 
        

        final Intent intent = new Intent(context, BlackIceActivity.class);
        final PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 0);
       
        Notification.Builder builder = new Notification.Builder(context)  
                .setAutoCancel(true)  
                .setContentTitle("Message")  
                .setContentText(new String(message.getPayload()) + " ")  
                .setContentIntent(activity)  
                .setSmallIcon(R.drawable.snow)  
                .setWhen(System.currentTimeMillis())  
                .setOngoing(true);  
        notification=builder.getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.number += 1;
        notificationManager.notify(0, notification);
    }

    @Override
    public void deliveryComplete(MqttDeliveryToken token) {
        //�ɹ�����ĳһ��Ϣ��Ļص�����
    }
}
