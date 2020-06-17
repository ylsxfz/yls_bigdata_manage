package com.bigdata.yls_apply.webservice.conf;

import com.bigdata.yls_apply.webservice.serice.InitWebService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class WebServiceConf extends SpringBootServletInitializer{
	@Autowired
	private Bus bus;
	
	@Autowired
	private InitWebService initWebService;

	/**
	 * @Author yls
	 * @Description webservie注册
	 * @Date 2020/3/28 14:15
	 * @return javax.xml.ws.Endpoint
	 **/
	@Bean
	public Endpoint endpoint() {
		//测试地址：http://localhost:8080/services/YLSInitWebService?wsdl
		EndpointImpl endpoint = new EndpointImpl(bus,initWebService);
		endpoint.publish("/YLSInitWebService");
		return endpoint;
	}
}
