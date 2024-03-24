package com.springboot.taxservice.service;

import com.springboot.taxservice.DAO.SQLAccess;
import com.springboot.taxservice.model.TaxServiceResponse;
import com.springboot.taxservice.model.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.springboot.taxservice.model.TaxServiceResource;
import java.math.BigDecimal;

@Component
public class TaxServiceService {
    private SQLAccess db;
    public TaxServiceService(@Autowired SQLAccess db) {
        this.db = db;
    }

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
        }
        taxServiceResponse.roundDataToTwoDecimalPlaces();
        return taxServiceResponse;
    }
}


