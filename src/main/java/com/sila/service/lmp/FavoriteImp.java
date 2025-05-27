package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.response.FavoriteResponse;
import com.sila.model.Favorite;
import com.sila.repository.FavoriteRepository;
import com.sila.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteImp implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<FavoriteResponse> getMyFav() {
        List<Favorite> favorite =favoriteRepository.findAllByOwner(UserContext.getUser()).stream().toList();
        return favorite.stream().map(f->this.modelMapper.map(f,FavoriteResponse.class)).toList();
    }
}
