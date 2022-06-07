package com.myhotels.hotelinventory;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagement
public class HotelInventoryServiceApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelInventoryServiceApiApplication.class, args);
	}

	@Bean
	public OpenAPI openApiConfig() {
		return new OpenAPI().info(apiInfo());
	}

	private Info apiInfo() {
		Info info = new Info();
		info.title("MyHotels Reservation System API")
				.version("v.1.0").description("MyHotels is a mid-size global chain of hotels " +
						"that has its own Central Reservation System (CRS)");
		return info;
	}
}
