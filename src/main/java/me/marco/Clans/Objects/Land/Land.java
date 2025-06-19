package me.marco.Clans.Objects.Land;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.List;

public class Land {

    private int x;
    private int z;

    public Land(Chunk chunk){
        this.x = chunk.getX();
        this.z = chunk.getZ();
    }

    public Land(int x, int z){
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public static List<Land> convertSQLToLand(String data){
        if(data == null || data.equals("")) return new ArrayList<Land>();
        List<Land> landList = new ArrayList<Land>();
        if(StringUtils.countMatches(data, " ") >= 1){
            String[] splitLand = data.split(" ");
            for(String land : splitLand){
                String[] xz = land.split(",");
                Integer x = Integer.parseInt(xz[0]);
                Integer z = Integer.parseInt(xz[1]);
                landList.add(new Land(x, z));
            }
        }else{
            String[] xz = data.split(",");
            Integer x = Integer.parseInt(xz[0]);
            Integer z = Integer.parseInt(xz[1]);
            landList.add(new Land(x, z));
        }
        return landList;
    }

}
