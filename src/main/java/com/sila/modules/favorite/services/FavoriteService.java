package com.sila.modules.favorite.services;

import com.sila.modules.favorite.dto.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    List<FavoriteResponse> getMyFav();
}
