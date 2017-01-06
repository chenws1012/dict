package com.lvmama.tnt.dict.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenwenshun on 2016/12/30.
 */
//@RestController
@RequestMapping("/hello")
public class HelloController {
   private  Logger log = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/say")
   public String say(){
       log.info("hello.................");
        return "hello";
   }
}
