package com.example.demo.Controller;

import com.example.demo.DAO.CustomerDAO;
import com.example.demo.Model.Customer;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;

import static com.hivemq.client.mqtt.MqttGlobalPublishFilter.ALL;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.CharBuffer;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Configuration
public class Mqtt {
	private static String bienSoXe = null;
	private static String trangThaiViTri = "";
	private static boolean check = false;
	private static int timKiemThe = 0;
	private static int idXe = 0;
	static final String host = "7d293d0a813a4e7cb21f434d78c3e8a9.s1.eu.hivemq.cloud";
	static final String username = "laam2002";
	static final String password = "Laam2002";
	static Mqtt5BlockingClient client;

	CustomerDAO aCustomerDAO = new CustomerDAO();

	public Mqtt() {
		client = MqttClient.builder().useMqttVersion5().serverHost(host).serverPort(8883).sslWithDefaultConfig()
				.buildBlocking();

		// connect to HiveMQ Cloud with TLS and username/pw
		client.connectWith().simpleAuth().username(username).password(UTF_8.encode(password)).applySimpleAuth().send();
		System.out.println("Connected successfully");
		client.subscribeWith().topicFilter("esp32_data").send();
		System.out.println(123);
	}

	@Bean
	public String sub() {
		client.toAsync().publishes(ALL, publish -> {
			String string = UTF_8.decode(publish.getPayload().get()).toString();

			System.out.println("Received message: " + publish.getTopic() + " -> " + string);
			JSONObject jsonObject = new JSONObject(string);
			String topic = jsonObject.getString("topic");
			if (topic.equals("esp32_data")) {
				int den1 = jsonObject.getInt("mot");
				int den2 = jsonObject.getInt("hai");
				String the = jsonObject.getString("cardID");
				if (den1 == 0) {
					pub("Mo", "servo");
					Customer customer = new Customer();
					customer.setBienSoXe(bienSoXe);
					customer.setThe(the);
					customer.setRaVao(1);
					customer.setTrangThai(1);
					System.out.println(customer);
					idXe = aCustomerDAO.addXe(customer);
					check = true;
					System.out.println(idXe + " " + check);
					System.out.println(publish.getTopic());

				}
				if (den2 == 0) {
					Customer aCustomer = new Customer();
					aCustomer.setBienSoXe(bienSoXe);
					aCustomer.setThe(the);
					Boolean dungBoolean = aCustomerDAO.out(aCustomer);
					if (dungBoolean) {
						pub("Mo", "servo");
					}
					
				}
				if (den2 == 1 && den1 == 1) {
					Customer aCustomer = new Customer();
					aCustomer.setThe(the);
					timKiemThe = aCustomerDAO.timKiemBangThe(aCustomer);
				}

			}
			if (topic.equals("trangThaiViTri")) {
				trangThaiViTri = string;
			}
			if (topic.equals("vao_chuong") && check) {
				check = false;
				int buong = jsonObject.getInt("id");
				Customer aCustomer = new Customer();
				aCustomer.setId(idXe);
				aCustomer.setViTri(buong);
				aCustomerDAO.updateBuong(aCustomer);
			}

		});
		return "";
	}

	public  int getTimKiemThe() {
		return timKiemThe;
	}

	public  void setTimKiemThe(int timKiemThe) {
		Mqtt.timKiemThe = timKiemThe;
	}

	public void pub(String s, String tp) {
		client.publishWith().topic(tp).payload(UTF_8.encode(s)).send();
	}

	public String getBienSoXe() {
		return bienSoXe;
	}

	public void setBienSoXe(String bienSoXe) {
		this.bienSoXe = bienSoXe;
	}

	public String getTrangThaiViTri() {
		return trangThaiViTri;
	}

	public void setTrangThaiViTri(String trangThaiViTri) {
		Mqtt.trangThaiViTri = trangThaiViTri;
	}

}
