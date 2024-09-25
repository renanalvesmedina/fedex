package com.mercurio.lms.sgr.model.service;

import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_ABRANGENCIA_NACIONAL;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_COLETA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_ENTREGA;
import static com.mercurio.lms.sgr.util.ConstantesGerRisco.TP_OPERACAO_VIAGEM;

import java.math.BigDecimal;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.mercurio.lms.expedicao.model.NaturezaProdutoBuilder;
import com.mercurio.lms.municipios.model.FilialBuilder;
import com.mercurio.lms.municipios.model.MunicipioBuilder;
import com.mercurio.lms.municipios.model.PaisBuilder;
import com.mercurio.lms.municipios.model.UnidadeFederativaBuilder;
import com.mercurio.lms.sgr.dto.DoctoServicoDTO;
import com.mercurio.lms.sgr.dto.PlanoGerenciamentoRiscoDTO;
import com.mercurio.lms.sgr.model.DoctoServicoDTOBuilder;
import com.mercurio.lms.sgr.model.EnquadramentoRegra;
import com.mercurio.lms.sgr.model.EnquadramentoRegraBuilder;
import com.mercurio.lms.sgr.model.PlanoGerenciamentoRiscoDTOBuilder;
import com.mercurio.lms.vendas.model.ClienteBuilder;

public class VerificacaoGeralTest {

	private PlanoGerenciamentoRiscoService service;

	@BeforeTest
	public void beforeTest() {
		service = new PlanoGerenciamentoRiscoService();
	}

	@Test(description = "Múltiplas regras de enquadramento para o controle de carga")
	public void testDuplicados() {
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(DoctoServicoDTOBuilder.newDoctoServicoDTO()
						.nacional()
						.build())
				.regraGeral(EnquadramentoRegraBuilder.newEnquadramentoRegra()
						.nacional()
						.regraGeral()
						.build())
				.build();

		service.executeVerificacaoGeral(plano);
		Assert.assertNull(plano.getDuplicados());

		plano.getRegrasGeral().add(EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.nacional()
				.regraGeral()
				.build());

		service.executeVerificacaoGeral(plano);
		Assert.assertEquals(plano.getDuplicados().size(), 1);
		Assert.assertEquals(plano.getDuplicados().iterator().next().getTpAbrangencia(), TP_ABRANGENCIA_NACIONAL);
	}

	@Test(description = "Regras de enquadramento relacionadas ao controle de carga")
	public void testDocumentosRegra() {
		EnquadramentoRegra regra = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.nacional()
				.coleta()
				.filialOrigem(FilialBuilder.POA())
				.filialOrigem(FilialBuilder.SAO())
				.filialDestino(FilialBuilder.POA())
				.filialDestino(FilialBuilder.SAO())
				.regraGeral()
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(DoctoServicoDTOBuilder.newDoctoServicoDTO()
						.valor(10)
						.remetente(ClienteBuilder.APPLE_COMPUTER_00623904000173())
						.destinatario(ClienteBuilder.NATURA_COSMETICOS_71673990003788())
						.nacional()
						.naturezaProduto(NaturezaProdutoBuilder.ELETRO_ELETRONICOS())
						.coleta()
						.paisOrigem(PaisBuilder.BRASIL())
						.paisDestino(PaisBuilder.ESTADOS_UNIDOS())
						.unidadeFederativaOrigem(UnidadeFederativaBuilder.RS())
						.unidadeFederativaDestino(UnidadeFederativaBuilder.SP())
						.municipioOrigem(MunicipioBuilder.PORTO_ALEGRE())
						.municipioDestino(MunicipioBuilder.SAO_PAULO())
						.filialOrigem(FilialBuilder.POA())
						.filialDestino(FilialBuilder.SAO())
						.build())
				.regraGeral(regra)
				.build();

		service.executeVerificacaoGeral(plano);
		Assert.assertEquals(plano.getDocumentosRegra().size(), 1);
		Assert.assertTrue(plano.getDocumentosRegra().containsKey(regra));
		DoctoServicoDTO documento = plano.getDocumentosRegra().get(regra).iterator().next();
		Assert.assertEquals(documento.getTpAbrangencia(), TP_ABRANGENCIA_NACIONAL);
		Assert.assertEquals(documento.getTpOperacao(), TP_OPERACAO_COLETA);
		Assert.assertEquals(documento.getIdPaisOrigem(), PaisBuilder.BRASIL().getIdPais());
		Assert.assertEquals(documento.getIdPaisDestino(), PaisBuilder.ESTADOS_UNIDOS().getIdPais());
		Assert.assertEquals(documento.getIdUnidadeFederativaOrigem(), UnidadeFederativaBuilder.RS().getIdUnidadeFederativa());
		Assert.assertEquals(documento.getIdUnidadeFederativaDestino(), UnidadeFederativaBuilder.SP().getIdUnidadeFederativa());
		Assert.assertEquals(documento.getIdMunicipioOrigem(), MunicipioBuilder.PORTO_ALEGRE().getIdMunicipio());
		Assert.assertEquals(documento.getIdMunicipioDestino(), MunicipioBuilder.SAO_PAULO().getIdMunicipio());
		Assert.assertEquals(documento.getIdFilialOrigem(), FilialBuilder.POA().getIdFilial());
		Assert.assertEquals(documento.getSgFilialOrigem(), FilialBuilder.POA().getSgFilial());
		Assert.assertEquals(documento.getIdFilialDestino(), FilialBuilder.SAO().getIdFilial());
		Assert.assertEquals(documento.getSgFilialDestino(), FilialBuilder.SAO().getSgFilial());
	}

	@Test(description = "Totalização dos valores de mercadoria por regra de enquadramento")
	public void testTotalRegra() {
		EnquadramentoRegra regra = EnquadramentoRegraBuilder.newEnquadramentoRegra()
				.regraGeral()
				.build();
		PlanoGerenciamentoRiscoDTO plano = PlanoGerenciamentoRiscoDTOBuilder.newPlanoGerenciamentoRiscoDTO()
				.documento(DoctoServicoDTOBuilder.newDoctoServicoDTO()
						.build())
				.regraGeral(regra)
				.build();

		plano.sumTotalTpOperacao(TP_OPERACAO_COLETA, BigDecimal.ONE.divide(BigDecimal.TEN));
		plano.sumTotalTpOperacao(TP_OPERACAO_ENTREGA, BigDecimal.ONE);
		plano.sumTotalTpOperacao(TP_OPERACAO_VIAGEM, BigDecimal.TEN);

		service.executeVerificacaoGeral(plano);
		Assert.assertEquals(plano.getTotaisRegra().size(), 1);
		Assert.assertTrue(plano.getTotaisRegra().containsKey(regra));
		Assert.assertEquals(plano.getTotaisRegra().get(regra), BigDecimal.valueOf(11.1));
	}

}
