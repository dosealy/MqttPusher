package de.eclipsemagazin.mqtt.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.internal.MemoryPersistence;

/**
 * @author neymarrr
 */
public class MQTTService extends Service {
    //��Ϣ��������URL
    public static final String BROKER_URL = "tcp://192.168.191.6:1883";
    //�ͻ���ID��������ʶһ���ͻ������Ը��ݲ�ͬ�Ĳ���������
    public static final String clientId = "android-client";
    //���ĵ�������
    public static final String TOPIC = "test";
    //mqtt�ͻ�����
    private MqttClient mqttClient;
    //mqtt����������
    private MqttConnectOptions options;  
    
    private String userName = "admin";
	private String passWord = "password";
	   

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        try {
            //�ڷ���ʼʱnewһ��mqttClientʵ�����ͻ���IDΪclientId������������˵���ǳ־û��ͻ��ˣ������null���Ƿǳ־û�
        	mqttClient = new MqttClient(BROKER_URL, clientId, new MemoryPersistence());

        	// MQTT����������  
    		options = new MqttConnectOptions();  
    		// �����Ƿ����session,�����������Ϊfalse��ʾ�������ᱣ���ͻ��˵����Ӽ�¼����������Ϊtrue��ʾÿ�����ӵ������������µ�������� 
    		//������֮������Ϊfalseʱ���Կͻ��˿��Խ���������Ϣ
    		options.setCleanSession(false);  
    		// �������ӵ��û���  
    		options.setUserName(userName);  
    		// �������ӵ�����  
    		options.setPassword(passWord.toCharArray());  
    		// ���ó�ʱʱ�� ��λΪ��  
    		options.setConnectionTimeout(10);  
    		// ���ûỰ����ʱ�� ��λΪ�� ��������ÿ��1.5*20���ʱ����ͻ��˷��͸���Ϣ�жϿͻ����Ƿ����ߣ������������û�������Ļ���  
    		options.setKeepAliveInterval(20);  
    		// ���ûص�  �ص����˵��������
    		mqttClient.setCallback(new PushCallback(this));  
    		MqttTopic topic = mqttClient.getTopic(TOPIC);  
    		//setWill�����������Ŀ����Ҫ֪���ͻ����Ƿ���߿��Ե��ø÷������������ն˿ڵ�֪ͨ��Ϣ    
    		options.setWill(topic, "close".getBytes(), 2, true); 
    		//mqtt�ͻ������ӷ�����
    		mqttClient.connect(options);
    		
            //mqtt�ͻ��˶�������
    		//��mqtt����QoS����ʶ��������
    		//QoS=0ʱ��������෢��һ�Σ��п��ܶ�ʧ
    		//QoS=1ʱ���������ٷ���һ�Σ��п����ظ�
    		//QoS=2ʱ������ֻ����һ�Σ�����ȷ����Ϣֻ����һ�Ρ�
    		int[] Qos  = {1};  
    		String[] topic1 = {TOPIC};  
    		mqttClient.subscribe(topic1, Qos); 


        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        try {
            mqttClient.disconnect(0);
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(), "Something went wrong!" + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
