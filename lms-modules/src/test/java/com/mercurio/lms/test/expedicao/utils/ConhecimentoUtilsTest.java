package com.mercurio.lms.test.expedicao.utils;


import org.testng.Assert;
import org.testng.annotations.Test;

import com.mercurio.lms.expedicao.util.ConhecimentoUtils;

public class ConhecimentoUtilsTest {
	
	@Test
	public void testFormatDoctoServicoWithCTR(){
		Assert.assertEquals(ConhecimentoUtils.formatDoctoServico("CTR", "POA", 123456L, 123, 8), "C POA 00123456-123");
	}
	
	@Test
	public void testFormatDoctoServicoWithNFT(){
		Assert.assertEquals(ConhecimentoUtils.formatDoctoServico("NFT", "POA", 123456L, 123, 8), "N POA 00123456-123");
	}
	
	@Test
	public void testFormatConhecimento(){
		Assert.assertEquals(ConhecimentoUtils.formatConhecimento("POA", 1234L), "POA 00001234");
	}
	
	@Test
	public void testFormatConhecimentoInternacional(){
		Assert.assertEquals(ConhecimentoUtils.formatConhecimentoInternacional("USA", 123, 8765L), "USA.123.008765");
	}
	
}
