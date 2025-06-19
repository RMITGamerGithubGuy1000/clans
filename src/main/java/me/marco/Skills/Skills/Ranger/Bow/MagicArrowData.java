package me.marco.Skills.Skills.Ranger.Bow;

import me.marco.Client.Client;
import org.bukkit.Location;

public class MagicArrowData {

    private Client shooter;
    private MagicArrowType magicArrowType;
    private Location shotFrom;

    public MagicArrowData(Client shooter, Location shotFrom, MagicArrowType magicArrowType){
        this.shooter = shooter;
        this.magicArrowType = magicArrowType;
        this.shotFrom = shotFrom;
    }

    public Location getShotFrom() {
        return shotFrom;
    }

    public Client getShooter() {
        return shooter;
    }

    public MagicArrowType getMagicArrowType() {
        return magicArrowType;
    }
}
