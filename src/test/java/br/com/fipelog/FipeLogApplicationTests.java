package br.com.fipelog;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FipeLogApplicationTests {

	@Test
	public void calcPorcentual() {
		
		BigDecimal origem = new BigDecimal("23982.00");
		BigDecimal atual = new BigDecimal("22745.00");
		
		BigDecimal res = atual.divide(origem, RoundingMode.HALF_EVEN);
		
		res = BigDecimal.ONE.subtract(res).multiply(new BigDecimal(100));
		
		assertEquals(new BigDecimal("5.00"), res);
	}

}

