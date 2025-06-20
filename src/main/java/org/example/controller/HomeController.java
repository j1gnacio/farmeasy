package org.example.controller;

import org.example.config.ViewNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping(ViewNames.ROOT_URL)
    public String home() {
        return ViewNames.INDEX_VIEW;
    }
}