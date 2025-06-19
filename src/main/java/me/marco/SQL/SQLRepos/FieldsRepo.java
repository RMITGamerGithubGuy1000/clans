package me.marco.SQL.SQLRepos;

import me.marco.Base.Core;
import me.marco.SQL.Objects.Query;
import me.marco.SQL.Objects.Statement;
import me.marco.SQL.Objects.StatementValues.IntegerStatementValue;
import me.marco.SQL.Objects.StatementValues.StringStatementValue;
import me.marco.SQL.SQLRepo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FieldsRepo extends SQLRepo {

    private String name;
    private World world;

    private final String createString = "CREATE TABLE fields (id INT NOT NULL AUTO_INCREMENT, " +
            "blockX VARCHAR(255), " +
            "blockY VARCHAR(255), " +
            "blockZ VARCHAR(255), " +
            "material VARCHAR(255), " +
            "PRIMARY KEY (id))";

    public FieldsRepo(Core instance) {
        super(instance);
        this.name = "fields";
        this.createRepo();
        this.world = getInstance().getServer().getWorld("world");
    }

    public void loadData() {
        String query = "SELECT * FROM fields";
        try {
            PreparedStatement ps = this.getSQL().prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double bX = Double.valueOf(rs.getString("blockX"));
                double bY = Double.valueOf(rs.getString("blockY"));
                double bZ = Double.valueOf(rs.getString("blockZ"));
                Material material = Material.valueOf(rs.getString("material"));
                Block block = this.world.getBlockAt(new Location(this.world, bX, bY, bZ));
                block.setType(material);
                getInstance().getFieldsManager().addFieldBlock(block);
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

    public void createFieldBlock(Block block) {
        String query = "INSERT INTO fields (blockX, blockY, blockZ, material) VALUES (?, ?, ?, ?)";
        Statement statement = new Statement(query,
                new IntegerStatementValue(block.getX()),
                new IntegerStatementValue(block.getY()),
                new IntegerStatementValue(block.getZ()),
                new StringStatementValue(block.getType().toString()));
        getSQL().addQuery(new Query(statement));
    }

    public void removeFieldBlock(Block block) {
        String query = "DELETE FROM fields WHERE blockX=? AND blockY=? AND blockZ=?";
        Statement statement = new Statement(query,
                new IntegerStatementValue(block.getX()),
                new IntegerStatementValue(block.getY()),
                new IntegerStatementValue(block.getZ()));
        getSQL().addQuery(new Query(statement));
    }

}
