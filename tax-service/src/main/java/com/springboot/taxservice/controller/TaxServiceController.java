package com.springboot.taxservice.controller;

import com.springboot.taxservice.model.TaxServiceResource;
import com.springboot.taxservice.service.TaxServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
//@RequestMapping(value = "/taxservice")
public class TaxServiceController {
	/**
	 * getTaxService function calculates before and after tax and retrieves taxRate or taxAmount value from DB, based on recieved stringified JSON parameter and returns data in object TaxService.
	 * @param incomingStringifiedJson contains traderId, playedAmount and odd
	 * @return ResponseEntity containing TaxService object
	 */
	@Autowired
	private TaxServiceService taxService;
	@GetMapping("/taxservice")
	public ResponseEntity<TaxServiceResponse> getTaxService(@RequestBody TaxServiceResource taxServiceResource) {
		try {
			TaxServiceResponse taxServiceResponse = taxService.calculateTaxData(taxServiceResource);
			return new ResponseEntity<>(taxServiceResponse, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}
	
	
}
