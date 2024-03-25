package com.springboot.taxservice;

import com.springboot.taxservice.DAO.SQLAccess;
import com.springboot.taxservice.model.TaxServiceResource;
import com.springboot.taxservice.model.TaxServiceResponse;
import com.springboot.taxservice.model.Trader;
import com.springboot.taxservice.service.TaxServiceService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TaxServiceServiceTest {
	TaxServiceService taxServiceService;
	@Mock
	SQLAccess db;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		taxServiceService = Mockito.spy(new TaxServiceService(db));
	}
	@Test
	public void calculateTaxData_generalTax_taxRate() throws Exception {
		TaxServiceResource taxServiceResource = new TaxServiceResource(1, BigDecimal.valueOf(5), BigDecimal.valueOf(3.2));
		when(db.getTraderInfoFromDB(1)).thenReturn(new Trader (BigDecimal.valueOf(10), null, 0));

		TaxServiceResponse taxServiceResponse = taxServiceService.calculateTaxData(taxServiceResource);

		assertEquals(new BigDecimal("14.40"), taxServiceResponse.getPossibleReturnAmount());
		assertEquals(new BigDecimal("16.00"), taxServiceResponse.getPossibleReturnAmountBefTax());
		assertEquals(new BigDecimal("14.40"), taxServiceResponse.getPossibleReturnAmountAfterTax());
	}

	@Test
	public void calculateTaxData_generalTax_taxAmount() throws Exception {
		TaxServiceResource taxServiceResource = new TaxServiceResource(2, BigDecimal.valueOf(5), BigDecimal.valueOf(3.2));
		when(db.getTraderInfoFromDB(2)).thenReturn(new Trader (null, BigDecimal.valueOf(1), 0));

		TaxServiceResponse taxServiceResponse = taxServiceService.calculateTaxData(taxServiceResource);

		assertEquals(new BigDecimal("15.00"), taxServiceResponse.getPossibleReturnAmount());
		assertEquals(new BigDecimal("16.00"), taxServiceResponse.getPossibleReturnAmountBefTax());
		assertEquals(new BigDecimal("15.00"), taxServiceResponse.getPossibleReturnAmountAfterTax());
	}

	@Test
	public void calculateTaxData_winningsTax_taxRate() throws Exception {
		TaxServiceResource taxServiceResource = new TaxServiceResource(3, BigDecimal.valueOf(5), BigDecimal.valueOf(3.2));
		when(db.getTraderInfoFromDB(3)).thenReturn(new Trader (BigDecimal.valueOf(12), null, 1));

		TaxServiceResponse taxServiceResponse = taxServiceService.calculateTaxData(taxServiceResource);

		assertEquals(new BigDecimal("14.68"), taxServiceResponse.getPossibleReturnAmount());
		assertEquals(new BigDecimal("16.00"), taxServiceResponse.getPossibleReturnAmountBefTax());
		assertEquals(new BigDecimal("14.68"), taxServiceResponse.getPossibleReturnAmountAfterTax());
	}

	@Test
	public void calculateTaxData_winningsTax_taxAmount() throws Exception {
		TaxServiceResource taxServiceResource = new TaxServiceResource(3, BigDecimal.valueOf(5), BigDecimal.valueOf(3.2));
		when(db.getTraderInfoFromDB(3)).thenReturn(new Trader (null, BigDecimal.valueOf(2), 1));

		TaxServiceResponse taxServiceResponse = taxServiceService.calculateTaxData(taxServiceResource);

		assertEquals(new BigDecimal("14.00"), taxServiceResponse.getPossibleReturnAmount());
		assertEquals(new BigDecimal("16.00"), taxServiceResponse.getPossibleReturnAmountBefTax());
		assertEquals(new BigDecimal("14.00"), taxServiceResponse.getPossibleReturnAmountAfterTax());
	}



}
