package com.chinobot.aiuas.bot_collect.info.util.kml;

import java.util.List;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;

public class KmlPolygon {
	
	private List<Coordinate> points;
    private String name;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public List<Coordinate> getPoints() {
        return points;
    }
 
    public void setPoints(List<Coordinate> points) {
        this.points = points;
    }

}
