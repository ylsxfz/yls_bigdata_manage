package com.bigdata.yls_bigdata_hbase.hbase_phoenix.integration;

/**
 * @Author yls
 * @Description 测试实体类
 * @Date 2020/3/28 14:33
 * @return
 **/
public class USPopulation {
	private String state;
	private String city;
	private String population;
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPopulation() {
		return population;
	}

	public void setPopulation(String population) {
		this.population = population;
	}

	@Override
	public String toString() {
		return "USPopulation [state=" + state + ", city=" + city + ", population=" + population + "]";
	}


}
