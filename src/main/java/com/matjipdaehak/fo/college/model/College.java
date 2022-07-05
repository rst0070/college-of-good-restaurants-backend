package com.matjipdaehak.fo.college.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class College {

    @JsonProperty("college_id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private int collegeId;

    @JsonProperty("college_name")
    private String collegeName;

    @JsonProperty("college_mail_domain")
    private String collegeMailDomain;

    @JsonProperty("distance_limit_km")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private double distanceLimitKm;

    @JsonProperty("longitude")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private double longitude;

    @JsonProperty("latitude")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private double latitude;

}
