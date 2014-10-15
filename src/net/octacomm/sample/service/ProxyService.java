package net.octacomm.sample.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import net.octacomm.network.NioTcpProxyServer;

@Service
public class ProxyService {

	@Autowired
	private Environment env;

	@PostConstruct
	public void init() {
		String proxySettings = env.getProperty("proxy.setting", String.class);
		String[] settings = proxySettings.split(",");
		for (String info : settings) {
			String[] proxies = info.split("-");
			int localPort = Integer.parseInt(proxies[0]);
			String remoteHost = proxies[1];
			int remotePort = Integer.parseInt(proxies[2]);
					
			NioTcpProxyServer tcpServer = new NioTcpProxyServer(localPort, remoteHost, remotePort);
			tcpServer.init();
		}
	}
}
