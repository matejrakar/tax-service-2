package com.springboot.taxservice.controller;

import com.springboot.taxservice.model.TaxServiceResource;
import com.springboot.taxservice.model.TaxServiceResponse;
import com.springboot.taxservice.service.TaxServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TaxServiceController class is a RestController for tax related operations.
 * @author Matej
 *
 */
@RestController
public class TaxServiceController {
	@Autowired
	private TaxServiceService taxService;
	@GetMapping("/taxservice")
	public ResponseEntity<TaxServiceResponse> getTaxService(@RequestBody TaxServiceResource taxServiceResource) throws Exception{
			TaxServiceResponse taxServiceResponse = taxService.calculateTaxData(taxServiceResource);
			return new ResponseEntity<>(taxServiceResponse, HttpStatus.OK);
		}
	}

