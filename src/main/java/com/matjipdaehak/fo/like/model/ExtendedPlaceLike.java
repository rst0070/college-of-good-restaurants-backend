package com.matjipdaehak.fo.like.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.matjipdaehak.fo.place.model.ExtendedPlace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PlaceLike의 정보와 그와 연결된 place정보를 모두 포함하도록 설계한 객체
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtendedPlaceLike {
    @JsonProperty("like_data")
    private PlaceLike likeData;
    @JsonProperty("place_data")
    private ExtendedPlace placeData;
}
