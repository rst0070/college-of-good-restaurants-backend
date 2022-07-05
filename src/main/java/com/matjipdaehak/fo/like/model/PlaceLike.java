package com.matjipdaehak.fo.like.model;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int placeId;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("like_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date likeDate;
}
