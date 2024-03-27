package com.hust.controller;

import com.hust.dto.CollectDTO;
import com.hust.dto.SearchDTO;
import com.hust.service.CollectionService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collection")
public class CollectionController {
    @Autowired
    private CollectionService collectionService;

    // 突然想到state那个表可以定时清除，写好gmt_create,gmt_modified我们就可以根据是否过期来定期删除过期的数据

    /**
     * 动漫都没有何来收藏一说，所以我去实现了基本的增删改查接口，使得项目整体的逻辑自洽，这里我们不用担心用户瞎选收藏类型，这是前端兄弟们做的，搞一个表单不会出现奇怪的值
     *
     * @return 返回收藏的结果，成功或失败
     */
    @PostMapping("/create")
    public Result collectAnime(@RequestBody CollectDTO collectDTO) {
        return collectionService.collectAnime(collectDTO);
    }

    /**
     * 返回用户的收藏列表（灵感来自于B站的那个番剧收藏）
     */
    @PostMapping("/show")
    public Result showMyCollections(@RequestBody CollectDTO collectDTO) {
        return collectionService.showMyCollections(collectDTO);
    }

    /**
     * 对用户提供的keyword模糊搜索，得到收藏
     */
    @PostMapping("/search")
    public Result searchMyCollections(@RequestBody SearchDTO searchDTO) {
        return collectionService.searchMyCollections(searchDTO);
    }
}
