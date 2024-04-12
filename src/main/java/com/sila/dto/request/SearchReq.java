package com.sila.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchReq {
  private String search;
  private Boolean sessional;
  private Boolean vegeterain;

}
