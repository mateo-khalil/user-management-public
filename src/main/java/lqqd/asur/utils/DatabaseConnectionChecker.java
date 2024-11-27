package lqqd.asur.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DatabaseConnectionChecker implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            try (Connection connection = ((DataSource) bean).getConnection()) {
                System.out.println("Database connection verified successfully.");
            } catch (SQLException e) {
                System.err.println("ERROR: Unable to connect to the database. Please ensure the database is running.");
                System.exit(1);
            }
        }
        return bean;
    }
}
