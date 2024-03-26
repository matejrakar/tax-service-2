package com.springboot.taxservice;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.springboot.taxservice.model.TaxServiceError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.InsufficientResourcesException;
import java.sql.SQLException;

@ControllerAdvice("com.springboot.taxservice")
public class TaxServiceExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(TaxServiceExceptionHandler.class);

    @ExceptionHandler(CommunicationsException.class)
    protected ResponseEntity<TaxServiceError> handleDatabaseConnectionError (CommunicationsException ce){
        TaxServiceError response = new TaxServiceError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database connection error.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<TaxServiceError> handleDatabaseQueryError (SQLException se){
        TaxServiceError response = new TaxServiceError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Database query error.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InsufficientResourcesException.class)
    protected ResponseEntity<TaxServiceError> handleInsufficientResourcesException (InsufficientResourcesException ire){
        TaxServiceError response = new TaxServiceError(HttpStatus.BAD_REQUEST.value(), "Insufficient resources");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<TaxServiceError> handleGeneralException (RuntimeException re){
        logger.error("Service error: ", re.getMessage());
        TaxServiceError response = new TaxServiceError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Endpoint wasn't able to perform requested operation. Contact support.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
