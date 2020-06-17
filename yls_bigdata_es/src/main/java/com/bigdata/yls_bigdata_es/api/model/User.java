package com.bigdata.yls_bigdata_es.api.model;

/**
 * @Author yls
 * @Description 实体测试类
 * @Date 2020/3/28 14:18
 * @return
 **/
public class User {
    private String id;
    private String username;
    private String address;
    private String age;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", address='" + address + '\'' +
				", age='" + age + '\'' +
				'}';
	}
}
