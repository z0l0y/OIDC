package com.hust.controller;

import com.hust.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CollectionController {
    // 突然想到state那个表可以定时清除，写好gmt_create,gmt_modified我们就可以根据是否过期来定期删除过期的数据
/*    @PostMapping("/collect")
    public Result collectAnime(@RequestBody )*/
}
