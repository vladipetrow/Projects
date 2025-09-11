package com.example.workproject1.repositories.mysql;

import com.example.workproject1.repositories.SubscriptionTierRepository;
import com.example.workproject1.repositories.models.SubscriptionTierDAO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MySQLSubscriptionTierRepository implements SubscriptionTierRepository {
    private final JdbcTemplate jdbc;

    public MySQLSubscriptionTierRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<SubscriptionTierDAO> getAllTiers() {
        return jdbc.query(Queries.GET_ALL_TIERS, (rs, rowNum) -> fromResultSet(rs));
    }

    @Override
    public List<SubscriptionTierDAO> getTiersByType(String tierType) {
        return jdbc.query(Queries.GET_TIERS_BY_TYPE, (rs, rowNum) -> fromResultSet(rs), tierType);
    }

    @Override
    public SubscriptionTierDAO getTierByName(String tierName) {
        return jdbc.queryForObject(Queries.GET_TIER_BY_NAME, (rs, rowNum) -> fromResultSet(rs), tierName);
    }

    @Override
    public SubscriptionTierDAO getTierById(int id) {
        return jdbc.queryForObject(Queries.GET_TIER_BY_ID, (rs, rowNum) -> fromResultSet(rs), id);
    }

    private SubscriptionTierDAO fromResultSet(ResultSet rs) throws SQLException {
        return SubscriptionTierDAO.builder()
                .id(rs.getInt("id"))
                .tierName(rs.getString("tier_name"))
                .tierType(rs.getString("tier_type"))
                .price(rs.getDouble("price"))
                .maxPosts(rs.getInt("max_posts"))
                .has24_7Support(rs.getBoolean("has_24_7_support"))
                .description(rs.getString("description"))
                .createdAt(rs.getTimestamp("created_at") != null ? 
                    rs.getTimestamp("created_at").toLocalDateTime() : null)
                .build();
    }

    static class Queries {
        public static final String GET_ALL_TIERS = "SELECT * FROM subscription_tiers ORDER BY tier_type, price";
        public static final String GET_TIERS_BY_TYPE = "SELECT * FROM subscription_tiers WHERE tier_type = ? ORDER BY price";
        public static final String GET_TIER_BY_NAME = "SELECT * FROM subscription_tiers WHERE tier_name = ?";
        public static final String GET_TIER_BY_ID = "SELECT * FROM subscription_tiers WHERE id = ?";
    }
}


