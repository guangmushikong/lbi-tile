package com.lbi.web.dao;


import com.lbi.web.model.Admin_Region;
import com.lbi.web.model.Tile;
import com.lbi.web.util.TileSystem;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNoneEmpty;


@Repository(value="tileDao")
public class TileDao {
    @Resource(name="jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public static final GeometryFactory GEO_FACTORY=new GeometryFactory(new PrecisionModel(PrecisionModel.FLOATING),4326);

    public List<Admin_Region> getCityRegionList(Tile tile){
        List<Admin_Region> list=null;
        try{
            Envelope enve= TileSystem.TileXYToBounds(tile);
            Geometry grid=GEO_FACTORY.toGeometry(enve);
            StringBuilder sb=new StringBuilder();
            String[] fields={"adcode","name","st_astext(geom) as wkt"};
            sb.append("select ").append(StringUtils.join(fields,',')).append(" from s_ods_city_simplify");
            sb.append(" where st_intersects(st_geomfromtext(?,4326),geom)");
            list=jdbcTemplate.query(
                    sb.toString(),
                    new Object[]{grid.toText()},
                    new int[]{Types.VARCHAR},
                    new RowMapper<Admin_Region>() {
                        public Admin_Region mapRow(ResultSet rs, int i) throws SQLException {
                            Admin_Region u=new Admin_Region();
                            u.setCode(rs.getString("adcode"));
                            u.setName(rs.getString("name"));
                            u.setWkt(rs.getString("wkt"));
                            return u;
                        }
                    });
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return list;
    }

}
