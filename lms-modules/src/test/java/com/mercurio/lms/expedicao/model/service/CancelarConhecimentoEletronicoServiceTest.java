package com.mercurio.lms.expedicao.model.service;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.testng.annotations.Test;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.expedicao.model.DoctoServico;
import com.mercurio.lms.expedicao.model.MonitoramentoDocEletronico;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.mocks.ConhecimentoServiceMocker;
import com.mercurio.lms.municipios.model.Filial;

import static org.testng.Assert.assertTrue;

public class CancelarConhecimentoEletronicoServiceTest {

	private static final String SG_FILIAL = "sgFilial";
	private static final DateTime DATE_TIME = DateTimeFormat.forPattern("yyyy-M-d").parseDateTime("2014-1-1");
	private static final long NR_PROTOCOLO = 0L;
	private static final String NR_CHAVE = "1234";
	private static final String PESSOA_NR_IDENTIFICACAO = "12345678910";
	private static final long ID_DOCTO_SERVICO = 0L;
	private static final long ID_FILIAL = 0L;

	private MonitoramentoDocEletronico getMonitoramentoDocEletronicoCTE() {
		MonitoramentoDocEletronico monitoramentoDocEletronico = new MonitoramentoDocEletronico();
		monitoramentoDocEletronico.setDoctoServico(getDoctoServico());
		monitoramentoDocEletronico.setNrChave(NR_CHAVE);
		monitoramentoDocEletronico.setNrProtocolo(NR_PROTOCOLO);
		return monitoramentoDocEletronico;
	}
	
	private DoctoServico getDoctoServico() {
		DoctoServico doctoServico = new DoctoServico();
		doctoServico.setTpDocumentoServico(new DomainValue(ConstantesExpedicao.CONHECIMENTO_ELETRONICO));
		doctoServico.setDhEmissao(DATE_TIME);
		doctoServico.setIdDoctoServico(ID_DOCTO_SERVICO);
		doctoServico.setFilialByIdFilialOrigem(getFilial());
		return doctoServico;
	}

	private Filial getFilial() {
		Filial filial = new Filial();
		filial.setIdFilial(ID_FILIAL);
		filial.setSgFilial(SG_FILIAL);
		filial.setPessoa(getPessoa());
		return filial;
	}

	private Pessoa getPessoa() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNrIdentificacao(PESSOA_NR_IDENTIFICACAO);
		return pessoa;
	}

	private ConhecimentoService getConhecimentoServiceMock() {
		return new ConhecimentoServiceMocker().findById(0L, null).mock();
	}
/*
	@Test
	public void cancelarTest() {
		CancelarConhecimentoEletronicoService service = new CancelarConhecimentoEletronicoService();
		
		service.setConhecimentoService(getConhecimentoServiceMock());

		MonitoramentoDocEletronico monitoramentoDocEletronico = getMonitoramentoDocEletronicoCTE();
		service.cancelar(monitoramentoDocEletronico);
		assertTrue(new DomainValue("B").equals(monitoramentoDocEletronico.getTpSituacaoDocumento()));
	}
*/
}
