package com.example.demo.Controller;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DAO.CustomerDAO;
import com.example.demo.Model.Customer;

@CrossOrigin(value = "*")
@RestController
public class HomeController {
	@Autowired
	private static Mqtt mqtt = new Mqtt();
	
	@PostMapping(value = "/")
	public void home(@RequestBody String s) {
		JSONObject jsonObject = new JSONObject(s);
		System.out.println(jsonObject.getString("name"));
		mqtt.setBienSoXe(jsonObject.getString("name"));
		System.out.println(s);
	}
	@GetMapping(value = "/")
	public String trangThai() {
		return mqtt.getTrangThaiViTri();
	}
	
	@PostMapping(value = "/timkiem")
	public Customer timKiem(@RequestBody String s) {
		Customer aCustomer = new Customer();
		CustomerDAO aCustomerDAO = new CustomerDAO();
		JSONObject jsonObject = new JSONObject(s);
		aCustomer.setBienSoXe(jsonObject.getString("name"));
		int viTri = aCustomerDAO.timKiemBangGiong(aCustomer);
		aCustomer.setViTri(viTri);
		System.out.println(aCustomer);
		return aCustomer;
	}
	
	@GetMapping(value = "/timkiem")
	public Customer timKiem() {
		int tk = mqtt.getTimKiemThe();
		Customer aCustomer = new Customer();
		aCustomer.setViTri(tk);
		mqtt.setTimKiemThe(0);
		return aCustomer;
	}
	@PostMapping(value = "/dong-mo")
	public void dongMo(@RequestBody String s) {
		JSONObject jsonObject = new JSONObject(s);
		String trangThai= jsonObject.getString("trangThai");
		mqtt.pub(trangThai, "dong_mo");
		Customer aCustomer = new Customer();
		aCustomer.setId(1);
	}
}
