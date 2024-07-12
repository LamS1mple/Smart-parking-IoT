package com.example.demo.Model;



public class Customer {
	private int id;
	private String bienSoXe;
	private String the;
	private int raVao;
	private int trangThai;
	private String thoiGian;
	private int viTri;
	public int getViTri() {
		return viTri;
	}

	public void setViTri(int viTri) {
		this.viTri = viTri;
	}

	public String getBienSoXe() {
		return bienSoXe;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setBienSoXe(String bienSoXe) {
		this.bienSoXe = bienSoXe;
	}
	public String getThe() {
		return the;
	}
	public void setThe(String the) {
		this.the = the;
	}
	public int getRaVao() {
		return raVao;
	}
	public void setRaVao(int raVao) {
		this.raVao = raVao;
	}
	public int getTrangThai() {
		return trangThai;
	}
	public void setTrangThai(int trangThai) {
		this.trangThai = trangThai;
	}
	public String getThoiGian() {
		return thoiGian;
	}
	public void setThoiGian(String thoiGian) {
		this.thoiGian = thoiGian;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", bienSoXe=" + bienSoXe + ", the=" + the + ", raVao=" + raVao + ", trangThai="
				+ trangThai + ", thoiGian=" + thoiGian + ", viTri=" + viTri + "]";
	}

	
	
}
