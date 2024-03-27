package com.hust.service.Impl;

import com.hust.dto.CollectDTO;
import com.hust.dto.SearchDTO;
import com.hust.mapper.CollectionMapper;
import com.hust.po.CollectionPO;
import com.hust.po.RatingPO;
import com.hust.service.CollectionService;
import com.hust.utils.Result;
import com.hust.vo.CollectionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hust.utils.Conversion.*;

@Service
public class CollectionServiceImpl implements CollectionService {
    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public Result collectAnime(CollectDTO collectDTO) {
        CollectionPO collectionPO = toCollectionPO(collectDTO);
        RatingPO ratingPO = toRatingPO(collectDTO);
        int rowsAffected = collectionMapper.insertCollection(collectionPO);
        int rowsAffected1 = collectionMapper.insertRating(ratingPO);
        if (rowsAffected > 0 && rowsAffected1 > 0) {
            return Result.success("添加收藏成功！");
        } else {
            return Result.error("添加收藏失败！");
        }
    }

    @Override
    public Result showMyCollections(CollectDTO collectDTO) {
        CollectionPO[] collectionPOS = collectionMapper.showMyAllCollections(collectDTO.getUsername());
        List<CollectionVO> collections = new ArrayList<>();
        for (CollectionPO collectPO : collectionPOS) {
            CollectionVO collectionVO = toCollectionVO(collectPO);
            collections.add(collectionVO);
        }
        return Result.success(collections);
    }

    @Override
    public Result searchMyCollections(SearchDTO searchDTO) {
        CollectionPO[] myCollections = collectionMapper.searchMyCollections(searchDTO.getUsername(), searchDTO.getKeyword());
        List<CollectionVO> collections = new ArrayList<>();
        for (CollectionPO collectPO : myCollections) {
            CollectionVO collectionVO = toCollectionVO(collectPO);
            collections.add(collectionVO);
        }
        return Result.success(collections);
    }
}
