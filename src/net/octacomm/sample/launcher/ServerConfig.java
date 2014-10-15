package net.octacomm.sample.launcher;

import net.octacomm.sample.service.ProxyService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("file:resources/netty.properties")
@EnableScheduling
public class ServerConfig {

	@Bean
	public ProxyService usnTcpServer() {
		return new ProxyService();		
	}
}
