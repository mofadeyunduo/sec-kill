package com.example.seckill.controller;

import com.example.seckill.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/sec-kill")
@RestController
public class SecKillController {

    @Autowired
    private SecKillService secKillService;

    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public String secKillInit(@RequestParam Integer number) {
        secKillService.init(number);
        return "OK";
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String secKill(@RequestParam Integer userId, @RequestParam Integer goodsId) {
        return secKillService.secKill(userId, goodsId) ? "OK" : "SOLD OUT";
    }

}
