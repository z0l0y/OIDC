package com.hust.service.Impl;

import com.hust.dto.AnimeDTO;
import com.hust.mapper.AnimeMapper;
import com.hust.po.AnimePO;
import com.hust.service.AnimeService;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hust.utils.Conversion.toAnimePO;

@Service
public class AnimeServiceImpl implements AnimeService {
    @Autowired
    private AnimeMapper animeMapper;

    @Override
    public Result addAnime(AnimeDTO animeDTO) {
        AnimePO animePO = toAnimePO(animeDTO);
        AnimePO exist = animeMapper.verifyAnimeExist(animePO);
        if (exist != null) {
            return Result.error("动漫已经存在！");
        }
        int rowsAffected = animeMapper.addAnime(animePO);
        if (rowsAffected > 0) {
            return Result.success("增加动漫成功！");
        } else {
            return Result.error("增加动漫失败！");
        }
    }

    @Override
    public Result showAnime() {
        AnimePO[] anime = animeMapper.getAllAnime();
        return Result.success(anime);
    }

    @Override
    public Result updateAnime(AnimeDTO animeDTO) {
        AnimePO animePO = toAnimePO(animeDTO);
        int rowsAffected = animeMapper.updateAnime(animePO);
        if (rowsAffected > 0) {
            return Result.success("修改动漫信息成功！");
        } else {
            return Result.error("修改动漫信息失败！");
        }
    }

    @Override
    public Result deleteAnime(AnimeDTO animeDTO) {
        AnimePO animePO = toAnimePO(animeDTO);
        int rowsAffected = animeMapper.deleteAnime(animePO);
        if (rowsAffected > 0) {
            return Result.success("删除动漫成功！");
        } else {
            return Result.error("删除动漫失败！");
        }
    }
}
