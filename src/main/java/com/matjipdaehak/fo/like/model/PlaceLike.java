package com.matjipdaehak.fo.like.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceLike {

    @JsonProperty("place_id")
    private int placeId;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("like_date")
    private Date likeDate;
}
