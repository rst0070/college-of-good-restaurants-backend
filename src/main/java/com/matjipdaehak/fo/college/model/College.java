package com.matjipdaehak.fo.college.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class College {

    @JsonProperty("college_id")
    private int collegeId;
    @JsonProperty("college_name")
    private String collegeName;
    @JsonProperty("college_mail_domain")
    private String collegeMailDomain;
    @JsonProperty("distance_limit_km")
    private double distanceLimitKm;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("latitude")
    private double latitude;

}
