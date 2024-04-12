package com.sila.controller.api;

import com.sila.model.Card;
import com.sila.model.CardItem;
import com.sila.model.User;
import com.sila.dto.request.ItemCardReq;
import com.sila.dto.request.CartItemReq;
import com.sila.service.CardService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;
    private final UserService userService;
    @PostMapping("/card-item/add")
    public ResponseEntity<CardItem> addItemToCard(@RequestHeader("Authorization") String jwt, @RequestBody ItemCardReq req) throws Exception {
        CardItem cardItem=cardService.addItemToCard(req,jwt);
        return new ResponseEntity<>(cardItem, HttpStatus.OK);
    }
    @PutMapping("/card-item/update")
    public ResponseEntity<CardItem> updateItemInCard(@RequestHeader("Authorization") String jwt, @RequestBody CartItemReq req) throws Exception {
        CardItem cardItem=cardService.updateCardItemQty(req.getCardItemId(),req.getQty());
        return new ResponseEntity<>(cardItem, HttpStatus.OK);
    }
    @DeleteMapping("/card-item/{cardItemId}/remove")
    public ResponseEntity<Card> removeItemInCard(@RequestHeader("Authorization") String jwt,@PathVariable Long cardItemId) throws Exception {
        Card card=cardService.removeItemFromCard(cardItemId, jwt);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
    @GetMapping("/card")
    public ResponseEntity<Card> getCardOfUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Card card=cardService.findCardByUserId(user.getId());
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
    @GetMapping("/card/clear")
    public ResponseEntity<Card> clearItemInCard(@RequestHeader("Authorization") String jwt) throws Exception {
        Card card = cardService.clearCard(jwt);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }
}
