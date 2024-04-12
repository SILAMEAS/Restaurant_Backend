package com.sila.lmp;

import com.sila.model.Card;
import com.sila.model.CardItem;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.CardItemRepository;
import com.sila.repository.CardRepository;
import com.sila.dto.request.ItemCardReq;
import com.sila.service.CardService;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CardImp implements CardService {
    private final CardRepository cardRepository;
    private final FoodService foodService;
    private final UserService userService;
    private final CardItemRepository cardItemRepository;
    private final RestaurantService restaurantService;

    @Override
    public CardItem addItemToCard(ItemCardReq req, String jwt) throws Exception {
        User user =userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        for(Food f: restaurant.getFoods()){
            if(!f.getId().equals(req.getFoodId())){
                throw new BadRequestException("Food Id : "+req.getFoodId()+" not have in restaurant id :"+req.getRestaurantId());
            }
        }
        Food food=foodService.findFoodById(req.getFoodId());
        Card card=cardRepository.findByCustomerId(user.getId());
        for(CardItem item:card.getItem()){
            if(item.getFood().equals(food)){
                int newQty=item.getQuantity()+req.getQty();
                return updateCardItemQty(item.getId(),newQty);


            }
        }
        CardItem newCardItem=new CardItem();
        newCardItem.setFood(food);
        newCardItem.setCard(card);
        newCardItem.setQuantity(req.getQty());
        newCardItem.setIngredients(req.getIngredients());
        newCardItem.setTotalPrice(req.getQty()*food.getPrice());
        CardItem saveCardItem=cardItemRepository.save(newCardItem);
        card.getItem().add(saveCardItem);
        return saveCardItem;
    }

    @Override
    public CardItem updateCardItemQty(Long card_item_id, int qty) throws Exception {
        Optional<CardItem> cardItems = cardItemRepository.findById(card_item_id);
        if(cardItems.isEmpty()){
            throw new BadRequestException("Card item is not found");
        }
        CardItem cardItemUpdated=cardItems.get();
        cardItemUpdated.setQuantity(qty);
        cardItemUpdated.setTotalPrice(cardItems.get().getFood().getPrice()*qty);
        return cardItemRepository.save(cardItemUpdated);
    }

    @Override
    public Card removeItemFromCard(Long cardItemId, String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Card card=cardRepository.findByCustomerId(user.getId());
        Optional<CardItem> cardItem=cardItemRepository.findById(cardItemId);
        if(cardItem.isEmpty()){
            throw new BadRequestException("Card Item id: "+cardItem+" is not found");
        }
        card.getItem().remove(cardItem.get());
        return cardRepository.save(card);
    }

    @Override
    public Long calculateCardTotal(Card card) throws Exception {
        Long total=0L;
        for (CardItem itemOfCard:card.getItem()){
            total+=itemOfCard.getTotalPrice();
        }
        return total;
    }

    @Override
    public Card findCardById(Long cardId) throws Exception {
        Optional<Card> foundCard=cardRepository.findById(cardId);
        if(foundCard.isEmpty()){
            throw new BadRequestException("Card  id: "+cardId+" is not found");
        }
        return foundCard.get();
    }

    @Override
    public Card findCardByUserId(Long userId) throws Exception {
        Card card =cardRepository.findByCustomerId(userId);
        card.setTotal(calculateCardTotal(card));
        return  card;
    }

    @Override
    public Card clearCard(String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Card userClearCard=findCardByUserId(user.getId());
        userClearCard.getItem().clear();
        return cardRepository.save(userClearCard);
    }
}
