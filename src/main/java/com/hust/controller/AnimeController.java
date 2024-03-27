package com.hust.controller;

import com.hust.dto.AnimeDTO;
import com.hust.service.AnimeService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anime")
public class AnimeController {
    @Autowired
    private AnimeService animeService;

    /**
     * 动漫的信息在哪来，我们是不是应该先有增加动漫的接口，然后才可以有这么多动漫的信息？虽然不用实现，但是我觉得这个还是很有必要的，
     * 这个整体的动漫逻辑架构一定要完整。这里我就不设置过滤器了，要相信工作人员不会对我们的动漫信息做出奇怪的事情
     * 但是我们要注意在add之前，由于可能是多个人合作，所以可能会重复add，我们要先确认有没有再往里面加
     *
     * @return 返回是否添加成功，成功为1，失败为0
     */
    @PostMapping("/add")
    public Result addAnime(@RequestBody AnimeDTO animeDTO) {
        return animeService.addAnime(animeDTO);
    }

    /**
     * 想了一下，其实修改动漫的信息一般我们也不用很麻烦，我们只用修改动漫的集数就行了（因为动漫更新的时候似乎就只有集数会发生变化）
     *
     * @param animeDTO 包含动漫集数信息
     * @return 返回是否修改成功，成功为1，失败为0
     */
    @PostMapping("/update")
    public Result updateAnime(@RequestBody AnimeDTO animeDTO) {
        return animeService.updateAnime(animeDTO);
    }

    /**
     * 显示全部的动漫信息
     *
     * @return 所有的动漫信息
     */
    @GetMapping("/show")
    public Result showAnime() {
        return animeService.showAnime();
    }

    /**
     * 删除采用软删除
     *
     * @param animeDTO 包含动漫的name
     * @return 返回是否删除成功，成功为1，失败为0
     */
    @PostMapping("/delete")
    public Result deleteAnime(@RequestBody AnimeDTO animeDTO) {
        return animeService.deleteAnime(animeDTO);
    }

}
