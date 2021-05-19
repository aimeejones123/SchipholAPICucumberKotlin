package com.schipol.hooks

import io.restassured.RestAssured
import io.restassured.config.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan

@ComponentScan("com.schipol.*")
class AppConfiguration {

//    any class that you want to use as a bean that doesn't have a Given, When or Then in it will need to be created here'

}
