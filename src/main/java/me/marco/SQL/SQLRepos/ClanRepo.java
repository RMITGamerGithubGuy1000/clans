package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.Clans.Objects.Clan.Clan;
import me.marco.Clans.Objects.Land.Land;
import me.marco.Client.Client;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.SQLRepo;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ClanRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE clans (id INT NOT NULL AUTO_INCREMENT, " +
            "Name VARCHAR(255), " +
            "Owner VARCHAR(255), " +
            "Land VARCHAR(255), " +
            "Home VARCHAR(255), " +
            "PRIMARY KEY (id))";

    public ClanRepo(Core instance) {
        super(instance);
        this.name = "clans";
        this.createRepo();
    }

    public Location homeFromString(String homeString){
        // x y z pitch yaw
        String[] split = homeString.split("\\,");
        double x = Double.valueOf(split[0]);
        double y = Double.valueOf(split[1]);
        double z = Double.valueOf(split[2]);
        float yaw = Float.valueOf(split[4]);
        float pitch = Float.valueOf(split[3]);

        return new Location(getInstance().getServer().getWorld("world"), x, y, z, yaw, pitch);
    }

    public String stringFromHome(Location home){
        // x y z pitch yaw
        String locString = "";
        locString += home.getX() + ",";
        locString += home.getY() + ",";
        locString += home.getZ() + ",";
        locString += home.getPitch() + ",";
        locString += home.getYaw() + "";
        return locString;
    }

    public void loadData() {
        String query = "SELECT * FROM clans";
        try {
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("Name");
                String owner = rs.getString("Owner");
                String land = rs.getString("Land");
                String home = rs.getString("Home");
                Location homeLoc = home == null ? null : homeFromString(home);
                Clan clan = new Clan(
                        name,
                        UUID.fromString(owner),
                        Land.convertSQLToLand(land),
                        homeLoc
                );
                getInstance().getClanManager().addClan(clan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createRepo() {
        try {
            if (!this.getSQL().tableExists(this.name)) {
                System.out.println("Table " + this.name + " doesn't exist... Creating now!");
                System.out.println(createString);
                this.getSQL().executePreparedStatement(createString);
            } else {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createClan(Clan clan) {
        String query = "INSERT INTO clans (Name, Owner) VALUES (?, ?)";
        Statement statement = new Statement(query,
                new StringStatementValue(clan.getName()),
                new StringStatementValue(clan.getOwner().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void deleteClan(Clan clan) {
        String query = "DELETE FROM clans WHERE name=?";
        Statement statement = new Statement(query,
                new StringStatementValue(clan.getName()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateLand(Clan clan) {
        String query = "UPDATE clans SET land=? WHERE name=?";
        Statement statement = new Statement(query,
                new StringStatementValue(clan.landToString()),
                new StringStatementValue(clan.getName()));
        getSQL().addQuery(new Query(statement));
    }

    public void setClanOwner(Clan clan, Client owner) {
        String query = "UPDATE clans SET owner=? WHERE name=?";
        Statement statement = new Statement(query,
                new StringStatementValue(owner.getUUID().toString()),
                new StringStatementValue(clan.getName()));
        getSQL().addQuery(new Query(statement));
    }

    public void setClanHome(Clan clan, Location home){
        String query = "UPDATE clans SET home=? WHERE name=?";
        Statement statement = new Statement(query,
                new StringStatementValue(stringFromHome(home)),
                new StringStatementValue(clan.getName()));
        getSQL().addQuery(new Query(statement));
    }

}
