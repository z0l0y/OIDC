package com.hust.service;

import com.hust.dto.AnimeDTO;
import com.hust.utils.Result;

public interface AnimeService {
   Result addAnime(AnimeDTO animeDTO);

   Result showAnime();

   Result updateAnime(AnimeDTO animeDTO);

   Result deleteAnime(AnimeDTO animeDTO);
}
