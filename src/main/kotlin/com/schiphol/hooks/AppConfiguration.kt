package com.schiphol.hooks

import org.springframework.context.annotation.ComponentScan

@ComponentScan("com.schiphol.*")
class AppConfiguration {

    // any class that you want to use as a bean that doesn't have a Given, When or Then in it will need to be created here'
}
