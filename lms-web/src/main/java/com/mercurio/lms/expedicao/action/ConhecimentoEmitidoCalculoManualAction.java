package com.mercurio.lms.expedicao.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Servico;
import com.mercurio.lms.configuracoes.model.service.ServicoService;
import com.mercurio.lms.expedicao.report.ConhecimentoEmitidoCalculoManualService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.conhecimentoEmitidoCalculoManualAction"
 */

public class ConhecimentoEmitidoCalculoManualAction extends ReportActionSupport {
	
	private ConhecimentoEmitidoCalculoManualService conhecimentoEmitidoCalculoManualService;
	private ServicoService servicoService;
	private ConfiguracoesFacade configuracoesFacade;

	@Override
	public java.io.File execute(TypedFlatMap parameters) throws Exception {
		reportServiceSupport = conhecimentoEmitidoCalculoManualService;
		return super.execute(parameters);
	}

	public Map getBasicData(){
		Filial filial = SessionUtils.getFilialSessao();
		boolean isMatriz = SessionUtils.isFilialSessaoMatriz();
		TypedFlatMap dadosUsuario = new TypedFlatMap();

		dadosUsuario.put("dataInicial", JTDateTimeUtils.convertFrameworkDateToFormat(JTDateTimeUtils.getDataAtual().toString(),"dd/MM/yyyy"));
		dadosUsuario.put("dataFinal", 	JTDateTimeUtils.convertFrameworkDateToFormat(JTDateTimeUtils.getDataAtual().toString(),"dd/MM/yyyy"));

		if(!isMatriz){
			TypedFlatMap emp = new TypedFlatMap();
			emp.put("tpEmpresa", filial.getEmpresa().getTpEmpresa().getValue());
			dadosUsuario.put("empresa", emp);

			TypedFlatMap fil = new TypedFlatMap();
			fil.put("idFilial", filial.getIdFilial());
			fil.put("sgFilial", filial.getSgFilial());
			dadosUsuario.put("filial", fil);

			TypedFlatMap pessoa = new TypedFlatMap();
			pessoa.put("nmFantasia", filial.getPessoa().getNmFantasia());
			fil.put("pessoa", pessoa);
		}
		return dadosUsuario;
	}

   	public List findServico(Map criteria) {
		List retorno = new ArrayList();
		List listServicos = servicoService.find(criteria);
		for (Iterator iter = listServicos.iterator(); iter.hasNext();) {
			TypedFlatMap map = new TypedFlatMap();
			Servico servico = (Servico) iter.next();
			map.put("idServico", servico.getIdServico());
			map.put("dsServico", servico.getDsServico());
			map.put("tpSituacao.value", servico.getTpSituacao().getValue());
			retorno.add(map);
		}
		return retorno;
	}
	
	
	public ConhecimentoEmitidoCalculoManualService getConhecimentoEmitidoCalculoManualService() {
		return conhecimentoEmitidoCalculoManualService;
	}

	public void setConhecimentoEmitidoCalculoManualService(
			ConhecimentoEmitidoCalculoManualService conhecimentoEmitidoCalculoManualService) {
		this.conhecimentoEmitidoCalculoManualService = conhecimentoEmitidoCalculoManualService;
	}

	public ServicoService getServicoService() {
		return servicoService;
	}

	public void setServicoService(ServicoService servicoService) {
		this.servicoService = servicoService;
	}

	public ConfiguracoesFacade getConfiguracoesFacade() {
		return configuracoesFacade;
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
}