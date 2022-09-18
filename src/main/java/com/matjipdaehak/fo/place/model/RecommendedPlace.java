package com.matjipdaehak.fo.place.model;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class RecommendedPlace {

    @JsonProperty("recommendation")
    private String recommendation;
    @JsonProperty("places")
    private List<ExtendedPlace> places;
}
