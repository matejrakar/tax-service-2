package com.springboot.taxservice.service;

import com.springboot.taxservice.DAO.SQLAccess;
import com.springboot.taxservice.model.TaxServiceResponse;
import com.springboot.taxservice.model.Trader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springboot.taxservice.model.TaxServiceResource;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

@Component
public class TaxServiceService {
    private static final Logger logger = LoggerFactory.getLogger(TaxServiceService.class);
    private static final String INSUFFICIENT_RESOURCE = "Insufficient resource";
    private SQLAccess db;
    public TaxServiceService(@Autowired SQLAccess db) {
        this.db = db;
    }
    /**
     * calculateTaxData function calculates before and after tax and retrieves taxRate or taxAmount value from DB, based on recieved TaxServiceResource from request body and returns data in object TaxServiceResponse.
     * @param taxServiceResource contains traderId, playedAmount and odd
     * @return TaxServiceResponse object containing response data
     */
    public TaxServiceResponse calculateTaxData (TaxServiceResource taxServiceResource) throws Exception {
        Trader trader = db.getTraderInfoFromDB(taxServiceResource.getTraderId());

        TaxServiceResponse taxServiceResponse = new TaxServiceResponse();
        taxServiceResponse.setPossibleReturnAmountBefTax(taxServiceResource.getPlayedAmount().multiply(taxServiceResource.getOdd()));

        if(trader.getTaxTypeId() == 0) {
            /*General taxation*/
            if(trader.getTaxRate() != null) {
                /*If taxRate is available, we tax per rate*/
                /*Equation: possibleReturnAmount = possibleReturnAmountBefTax - taxRate%*/
                BigDecimal taxRate = trader.getTaxRate().divide(BigDecimal.valueOf(100));
                BigDecimal tax = taxRate.multiply(taxServiceResponse.getPossibleReturnAmountBefTax());
                taxServiceResponse.setPossibleReturnAmountAfterTax(taxServiceResponse.getPossibleReturnAmountBefTax().subtract(tax));
                taxServiceResponse.setPossibleReturnAmount(taxServiceResponse.getPossibleReturnAmountAfterTax());
                taxServiceResponse.setTaxRate(trader.getTaxRate());
            }
            else if (trader.getTaxAmount() != null) {
                /*If taxAmount is available, we tax per amount*/
                /*Equation: possibleReturnAmount = possibleReturnAmountBefTax - taxAmount*/
                BigDecimal taxAmount = trader.getTaxAmount();
                taxServiceResponse.setPossibleReturnAmountAfterTax(taxServiceResponse.getPossibleReturnAmountBefTax().subtract(taxAmount));
                taxServiceResponse.setPossibleReturnAmount(taxServiceResponse.getPossibleReturnAmountAfterTax());
                taxServiceResponse.setTaxAmount(trader.getTaxAmount());
            }
            else {
                logger.error(INSUFFICIENT_RESOURCE);
                throw new InsufficientResourcesException();
            }
        }
        else if(trader.getTaxTypeId() == 1) {
            /*Winnings taxation*/
            if(trader.getTaxRate() != null) {
                /*If taxRate is available, we tax per rate*/
                /*Equation: possibleReturnAmount = playedAmount + ((possibleReturnAmountBefTax - playedAmount) - taxRate%)*/
                BigDecimal taxRate = trader.getTaxRate().divide(BigDecimal.valueOf(100));
                BigDecimal winningsAmount = taxServiceResponse.getPossibleReturnAmountBefTax().subtract(taxServiceResource.getPlayedAmount());
                BigDecimal winningsAmountAfterTax = winningsAmount.subtract(winningsAmount.multiply(taxRate));
                taxServiceResponse.setPossibleReturnAmountAfterTax(winningsAmountAfterTax.add(taxServiceResource.getPlayedAmount()));
                taxServiceResponse.setPossibleReturnAmount(taxServiceResponse.getPossibleReturnAmountAfterTax());
                taxServiceResponse.setTaxRate(trader.getTaxRate());
            }
            else if (trader.getTaxAmount() != null) {
                /*If taxAmount is available, we tax per amount*/
                /*Equation: possibleReturnAmount = playedAmount + (possibleReturnAmountBefTax - playedAmount - taxAmount)*/
                BigDecimal taxAmount = trader.getTaxAmount();
                taxServiceResponse.setPossibleReturnAmountAfterTax(taxServiceResponse.getPossibleReturnAmountBefTax().subtract(taxAmount));
                taxServiceResponse.setPossibleReturnAmount(taxServiceResponse.getPossibleReturnAmountAfterTax());
                taxServiceResponse.setTaxAmount(trader.getTaxAmount());
            }
            else {
                logger.error(INSUFFICIENT_RESOURCE);
                throw new InsufficientResourcesException();
            }
        }
        else {
            logger.error(INSUFFICIENT_RESOURCE);
            throw new InsufficientResourcesException();
        }
        taxServiceResponse.roundDataToTwoDecimalPlaces();
        return taxServiceResponse;
    }
}


