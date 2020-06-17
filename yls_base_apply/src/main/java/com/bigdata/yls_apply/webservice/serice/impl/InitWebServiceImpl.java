package com.bigdata.yls_apply.webservice.serice.impl;

import com.bigdata.yls_apply.webservice.serice.InitWebService;
import org.springframework.stereotype.Component;

import javax.jws.WebService;


@WebService(name="InitWebService",//暴露服务名称
		targetNamespace="http://service.webservice.cluster.bigdata.spring.com/",//命名空间，一般是接口的倒序
		endpointInterface="com.bigdata.yls_apply.webservice.serice.InitWebService"
)
@Component
public class InitWebServiceImpl implements InitWebService {

	@Override
	public String sayHello(String name) {
		return "hello,"+name;
	}

}
