package com.lbi.util;

import com.vividsolutions.jts.geom.*;

import org.junit.Test;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class testVectorTile {
    private static final Random RANDOM = new Random();
    public static final GeometryFactory GEO_FACTORY=new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),4326);
    @Test
    public void test(){
        /*Collection<Geometry> usa=getPoints(
                createPoint("Cougar"),
                createPoint("Raccoon"),
                createPoint("Beaver"),
                createPoint("Wolf"),
                createPoint("Bear"),
                createPoint("Coyote"));
        JtsLayer layer = new JtsLayer("United States of America", usa);
        JtsMvt mvt = new JtsMvt(singletonList(layer));
        final byte[] encoded = MvtEncoder.encode(mvt);
        String str=new String(encoded);
        System.out.println(str);*/
    }
    /*private Collection<Geometry> getPoints(Point... points) {
        return asList(points);
    }
    private Point createPoint(String name) {
        Coordinate coord = new Coordinate( (int) (RANDOM.nextDouble() * 4095),
                (int) (RANDOM.nextDouble() * 4095));
        Point point = GEO_FACTORY.createPoint(coord);

        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("id", name.hashCode());
        attributes.put("name", name);
        point.setUserData(attributes);

        return point;
    }*/
}
