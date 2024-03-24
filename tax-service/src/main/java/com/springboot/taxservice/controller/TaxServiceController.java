package com.springboot.taxservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONObject;
import java.math.BigDecimal;

import com.springboot.taxservice.DAO.SQLAccess;
import com.springboot.taxservice.model.TaxServiceResponse;
import com.springboot.taxservice.model.Trader;

/**
 * TaxServiceController class is a RestController for tax related operations.
 * @author Matej
 *
 */
@RestController
@RequestMapping(value = "/taxservice")
public class TaxServiceController {
	/**
	 * getTaxService function calculates before and after tax and retrieves taxRate or taxAmount value from DB, based on recieved stringified JSON parameter and returns data in object TaxService.
	 * @param incomingStringifiedJson contains traderId, playedAmount and odd
	 * @return ResponseEntity containing TaxService object
	 */
	@GetMapping("/{incomingStringifiedJson}")
	public ResponseEntity<TaxServiceResponse> getTaxService(@PathVariable("incomingStringifiedJson") String incomingStringifiedJson) {
		try {
			//Get data from request
			JSONObject jsonData = new JSONObject(incomingStringifiedJson);
			int traderId = jsonData.getInt("traderId");
			BigDecimal playedAmount = jsonData.getBigDecimal("playedAmount");
			BigDecimal odd = jsonData.getBigDecimal("odd");
			
			//DB data retrieve
            SQLAccess db = new SQLAccess();	
            Trader trader = db.getTraderInfoFromDB(traderId);
			
            TaxServiceResponse taxServiceResponse = new TaxServiceResponse();
            taxServiceResponse.setPossibleReturnAmountBefTax(playedAmount.multiply(odd));
            
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
            		BigDecimal winningsAmount = taxServiceResponse.getPossibleReturnAmountBefTax().subtract(playedAmount);
            		BigDecimal winningsAmountAfterTax = winningsAmount.subtract(winningsAmount.multiply(taxRate));
            		taxServiceResponse.setPossibleReturnAmountAfterTax(winningsAmountAfterTax.add(playedAmount));
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
			return new ResponseEntity<>(taxServiceResponse, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	
}
