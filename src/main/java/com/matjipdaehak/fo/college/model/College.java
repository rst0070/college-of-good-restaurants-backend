package com.matjipdaehak.fo.college.model;

import lombok.Data;

@Data
public class College {

    private int collegeId;
    private String collegeName;
    private String collegeMailDomain;
    private double distanceLimitKm;
    private double longitude;
    private double latitude;

}
