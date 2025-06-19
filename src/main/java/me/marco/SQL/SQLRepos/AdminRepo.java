package me.marco.SQL.SQLRepos;

import me.marco.Admin.AdminClans.AdminClan;
import me.marco.Base.Core;
import me.marco.Clans.Objects.Land.Land;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.BooleanStatementValue;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.SQLRepo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AdminRepo extends SQLRepo {

    private String name;

    private final String createString = "CREATE TABLE adminClans (id INT NOT NULL AUTO_INCREMENT, " +
            "Name VARCHAR(255), " +
            "Creator VARCHAR(255), " +
            "Land VARCHAR(255), " +
            "SafeZone BOOLEAN," +
            "PRIMARY KEY (id))";

    public AdminRepo(Core instance) {
        super(instance);
        this.name = "adminClans";
        this.createRepo();
    }

    public void loadData() {
        String query = "SELECT * FROM adminClans";
        try {
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("Name");
                String creator = rs.getString("Creator");
                String land = rs.getString("Land");
                boolean safeZone = rs.getBoolean("SafeZone");
                AdminClan adminClan = new AdminClan(
                        name,
                        UUID.fromString(creator),
                        Land.convertSQLToLand(land),
                        safeZone
                );
                getInstance().getClanManager().addClan(adminClan);
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

    public void createClan(AdminClan adminClan) {
        String query = "INSERT INTO adminClans (Name, Creator, SafeZone) VALUES (?, ?, ?)";
        Statement statement = new Statement(query,
                new StringStatementValue(adminClan.getName()),
                new StringStatementValue(adminClan.getOwner().toString()),
                new BooleanStatementValue(adminClan.isSafe()));
        getSQL().addQuery(new Query(statement));
    }

    public void deleteClan(AdminClan adminClan) {
        String query = "DELETE FROM adminClans WHERE name=?";
        Statement statement = new Statement(query,
                new StringStatementValue(adminClan.getName()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateLand(AdminClan adminClan) {
        String query = "UPDATE adminClans SET land=? WHERE name=?";
        Statement statement = new Statement(query,
                new StringStatementValue(adminClan.landToString()),
                new StringStatementValue(adminClan.getName()));
        getSQL().addQuery(new Query(statement));
    }

    public void updateSafeZone(AdminClan adminClan){
        String query = "UPDATE adminClans SET safezone=? WHERE name=?";
        Statement statement = new Statement(query,
                new BooleanStatementValue(adminClan.isSafe()),
                new StringStatementValue(adminClan.getName()));
        getSQL().addQuery(new Query(statement));
    }

}
