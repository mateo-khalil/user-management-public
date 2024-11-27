package lqqd.asur;

import lqqd.asur.utils.ConsoleColors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class AsurApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(AsurApplication.class, args);
        } catch (Exception e) {
            System.err.println("ERROR: The application failed to start due to a database connection issue.");
            System.err.println("Please ensure that the database is running and the connection details are correct.");
            e.printStackTrace();
            System.exit(1);
        }
    }


    // This method will run the SQL script when the application starts
    @Bean
    public CommandLineRunner runSqlScript(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                ScriptUtils.executeSqlScript(connection, new ClassPathResource("data.sql"));
                System.out.println(ConsoleColors.GREEN + "INFO: SQL script executed successfully." + ConsoleColors.RESET);
            } catch (Exception e) {
                System.err.println("ERROR: Failed to execute SQL script.");
                e.printStackTrace();
            }
        };
    }
}
