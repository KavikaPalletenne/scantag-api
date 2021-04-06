package xyz.scantag.dev.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplicationController {

    @RequestMapping("/")
    public String Welcome() {

        return "index";
    }
}
