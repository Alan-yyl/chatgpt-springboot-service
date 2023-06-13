package com.cyl.ctrbt.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Alan
 * @Date: 2023/6/13 17:10
 */
 @RestController
 @RequestMapping("/testController")
public class TestController {
 @RequestMapping("/test")
  public String test(){
    return "success";
  }
}
