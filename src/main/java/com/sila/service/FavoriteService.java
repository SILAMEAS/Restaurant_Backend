package com.sila.service;

import com.sila.dto.response.FavoriteResponse;

import java.util.List;

public interface FavoriteService {

    List<FavoriteResponse> getMyFav();
}
