package me.marco.CustomEntities;

import me.marco.Base.Core;
import org.bukkit.Chunk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomEntityManager {

    private Core instance;

    public CustomEntityManager(Core instance){
        this.instance = instance;
    }

    private HashMap<Chunk, List<CustomEntity>> chunkMap = new HashMap<>();

    public void addCustomEntity(CustomEntity customEntity){
        Chunk chunk = customEntity.getHomeLoc().getChunk();
        if(chunkMap.containsKey(chunk)){
            chunkMap.get(chunk).add(customEntity);
            return;
        }
        List<CustomEntity> customEntities = new ArrayList<CustomEntity>();
        customEntities.add(customEntity);
        this.chunkMap.put(chunk, customEntities);
    }

    public boolean isCustomEntityChunk(Chunk chunk){
        return this.chunkMap.containsKey(chunk);
    }

    public void respawnEntities(Chunk chunk){
        List<CustomEntity> list = this.chunkMap.get(chunk);
        for(CustomEntity customEntity : list){
            customEntity.checkRespawn();
        }
    }

    public Core getInstance() {
        return instance;
    }
}
