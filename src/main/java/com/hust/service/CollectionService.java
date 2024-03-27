package com.hust.service;

import com.hust.dto.CollectDTO;
import com.hust.dto.SearchDTO;
import com.hust.utils.Result;

public interface CollectionService {
    Result collectAnime(CollectDTO collectDTO);

    Result showMyCollections(CollectDTO collectDTO);

    Result searchMyCollections(SearchDTO searchDTO);
}
