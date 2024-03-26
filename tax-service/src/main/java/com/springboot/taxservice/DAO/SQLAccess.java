package com.springboot.taxservice.DAO;

import java.sql.*;

import com.springboot.taxservice.model.Trader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * This class is used as data access object. It contains methods for fetching data from DB.
 * @author Matej
 * 
 */
@Component
public class SQLAccess {
    private static final Logger logger = LoggerFactory.getLogger(SQLAccess.class);
    private Connection connect = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public SQLAccess () throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/tax_data?user=root&password=root"); //hardcoded credentials for demo purposes
        }
        catch (Exception e) {
            throw e;
        }
    }
    /**
	 * Executes query via PreparedStatement that queries for TAX_RATE, TAX_AMOUNT and TAX_TYPE columns in
	 * DB based on provided traderId. Returns Trader object
	 * @param  traderId id of a trader whose data is needed
	 * @return 			object Trader with data
	 */
    public Trader getTraderInfoFromDB(int traderId) throws Exception {
        try {
            preparedStatement = connect.prepareStatement("SELECT TAX_RATE, TAX_AMOUNT, TAX_TYPE_ID FROM TRADER WHERE ID = ?");
            preparedStatement.setInt(1, traderId);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            
            return new Trader(resultSet.getBigDecimal("TAX_RATE"), resultSet.getBigDecimal("TAX_AMOUNT"), resultSet.getInt("TAX_TYPE_ID"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    @PreDestroy
    public void destroy() throws SQLException {
        if (connect != null) {
            connect.close();
        }
    }
}