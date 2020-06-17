package com.bigdata.yls_apply.webservice.serice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * @Author yls
 * @Description webservice的接口
 * @Date 2020/3/28 14:16
 * @return
 **/
@WebService(name="InitWebService",//暴露服务名称
		targetNamespace="http://service.webservice.yls_apply.bigdata.com/"//命名空间，一般是接口的倒序
		)
public interface InitWebService {
	@WebMethod
	public String sayHello(@WebParam(name = "userName") String name);
}
