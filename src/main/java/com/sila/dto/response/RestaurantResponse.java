package com.sila.dto.response;

import com.sila.model.Address;
import com.sila.model.ContactInformation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponse implements Serializable {
    private Long id;
    private String name;
    private String description;
    private List<String> images;
    private String openingHours;
    private transient ContactInformation contactInformation;
    private boolean open;
    private transient Address address;

}