package com.sila.controller.admin;

import com.sila.annotation.PreAuthorization;
import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.response.UserResponse;
import com.sila.service.UserService;
import com.sila.utlis.contants.PaginationDefaults;
import com.sila.utlis.enums.USER_ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin User Controller", description = "Admin operations related to User")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorization({USER_ROLE.ROLE_ADMIN})
    public ResponseEntity<EntityResponseHandler<UserResponse>> findUserRoleIsAdmin(
            @RequestHeader("Authorization") String jwt, @RequestParam(defaultValue = PaginationDefaults.PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = PaginationDefaults.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = PaginationDefaults.SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationDefaults.SORT_ORDER) String sortOrder,
            @RequestParam(required = false) String search) throws Exception {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortBy));
        return new ResponseEntity<>(userService.list(pageable, search), HttpStatus.OK);
    }
}
