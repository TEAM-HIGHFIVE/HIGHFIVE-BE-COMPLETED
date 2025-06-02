package advancedweb.project.datacollector.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
@RequiredArgsConstructor
public class BatchMetadataCleaner implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;
    private final MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) {
        clearBatchMetadata();
        clearMongoCollection();
    }

    private void clearBatchMetadata() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
        for (String table : new String[]{
                "BATCH_STEP_EXECUTION_CONTEXT",
                "BATCH_STEP_EXECUTION",
                "BATCH_JOB_EXECUTION_CONTEXT",
                "BATCH_JOB_EXECUTION_PARAMS",
                "BATCH_JOB_EXECUTION",
                "BATCH_JOB_INSTANCE"
        }) {
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                if (count != null && count > 0) {
                    jdbcTemplate.execute("TRUNCATE TABLE " + table);
                    System.out.println("✅ Truncated table: " + table);
                } else {
                    System.out.println("⏭ Skipped table (empty): " + table);
                }
            } catch (Exception e) {
                System.err.println("⚠️ Error accessing table " + table + ": " + e.getMessage());
            }
        }
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    private void clearMongoCollection() {
        try {
            if (mongoTemplate.collectionExists("welfare")) {
                long count = mongoTemplate.getCollection("welfare").countDocuments();
                if (count > 0) {
                    mongoTemplate.dropCollection("welfare");
                    System.out.println("✅ Dropped MongoDB collection: welfare");
                } else {
                    System.out.println("⏭ Skipped MongoDB collection (empty): welfare");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error checking MongoDB collection: " + e.getMessage());
        }
    }
}
