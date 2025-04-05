package com.sila.controller.admin;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.response.UserRes;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    @GetMapping("")
    public ResponseEntity<EntityResponseHandler<UserRes>> findUserRoleIsAdmin(
            @RequestHeader("Authorization") String jwt,@RequestParam(defaultValue = "1") Integer pageNo,
                                                           @RequestParam(defaultValue = "10") Integer pageSize,
                                                           @RequestParam(defaultValue = "id") String sortBy,
                                                           @RequestParam(defaultValue = "ASC") String sortOrder,
                                                           @RequestParam(required = false) String search) throws Exception {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()),sortBy));
        userService.findUserHasRoleAdmin(jwt);
        return new ResponseEntity<>(userService.listUser(pageable,search), HttpStatus.OK);
    }
}
