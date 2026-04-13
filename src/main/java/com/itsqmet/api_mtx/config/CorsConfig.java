package com.itsqmet.api_mtx.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")//APLICA TODAS LAS RUTAS
                .allowedOrigins("http://localhost:4200")//PERMITE SOLICITUDES DESDE ANGULAR
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("*");//PERMITE TODOS LOS ENCABEZADOS
    }
}
