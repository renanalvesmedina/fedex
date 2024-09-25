package com.mercurio.lms.contasreceber.model.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import br.com.tntbrasil.integracao.domains.financeiro.BoletoDMN;
import br.com.tntbrasil.integracao.domains.financeiro.CreditoBancario;
import br.com.tntbrasil.integracao.domains.jms.Queues;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.configuracoes.model.service.UsuarioLMSService;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.Boleto;
import com.mercurio.lms.contasreceber.model.ComposicaoPagamentoRedeco;
import com.mercurio.lms.contasreceber.model.CreditoBancarioEntity;
import com.mercurio.lms.contasreceber.model.Redeco;
import com.mercurio.lms.contasreceber.model.dao.CreditoBancarioDAO;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService;
import com.mercurio.lms.integracao.model.service.IntegracaoJmsService.JmsMessageSender;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

public class CreditoBancarioService extends CrudService<CreditoBancarioEntity, Long> {

	private Logger log = LogManager.getLogger(this.getClass());
	private FilialService filialService;
	private UsuarioLMSService usuarioLmsService;
	private BancoService bancoService;
	private CreditoBancarioValidatorService creditoBancarioValidatorService;
	private DomainValueService domainValueService;
	private UsuarioService usuarioService;
	private CreditoBancarioPopulateService creditoBancarioPopulateService;
	private IntegracaoJmsService integracaoJmsService;
	private RedecoService redecoService;
	private ComposicaoPagamentoRedecoService composicaoPagamentoRedecoService;

	private static final long USUARIO_INTEGRACAO = 5000L;
	
	@Override
	public CreditoBancarioEntity findById(Long id) {
		return getCreditoBancarioDAO().findByIdComTotal(id);
	}
	
	public java.io.Serializable store(Map<String, Object> map, Usuario usuario) {
		UsuarioLMS usuarioLms = usuarioLmsService.findById(usuario.getIdUsuario());
		CreditoBancarioEntity creditoBancario = createCreditoBancario(map, usuarioLms);
		storeCreditoBancario(creditoBancario);
		
		return creditoBancario;
	}

	private CreditoBancarioEntity storeCreditoBancario(CreditoBancarioEntity creditoBancario) {
		validateForEdition(creditoBancario);
		
		getCreditoBancarioDAO().store(creditoBancario, true);
		
		return creditoBancario;
	}
	
	public CreditoBancarioEntity storeCreditoBancario(CreditoBancario creditoBancario){
		CreditoBancarioEntity creditoBancarioEntity = convertCreditoBancarioToEntity(creditoBancario);
		creditoBancarioEntity = storeCreditoBancario(creditoBancarioEntity);
		return creditoBancarioEntity;
	}


	private void validateForEdition(CreditoBancarioEntity creditoBancario) {
		if (creditoBancario.getIdCreditoBancario() != null) {
			if (creditoBancario.getTpOrigem().equals("A") || creditoBancario.getTpOrigem().equals("E")) {
				throw new BusinessException("LMS-36336");
			}
		}
	}
	
	private CreditoBancarioEntity createCreditoBancario(Map<String, Object> map, UsuarioLMS usuario) {
		Filial filial = filialService.findById((Long) map.get("filial"));
		Banco banco = bancoService.findById((Long) map.get("banco"));
		
		Long id = (Long) map.get("idCreditoBancario");
		YearMonthDay dtCredito = (YearMonthDay) map.get("dtCredito");
		BigDecimal vlCredito = (BigDecimal) map.get("vlCredito");
		DomainValue tpModalidade = (DomainValue) map.get("tpModalidade");
		DomainValue tpOrigem = (DomainValue) map.get("tpOrigem");
		DomainValue tpClassificacao = (DomainValue) map.get("tpClassificacao");
		DomainValue tpSituacao = (DomainValue) map.get("tpSituacao");
		String dsCpfCnpj = (String) map.get("dsCpfCnpj");
		String dsNomeRazaoSocial = (String) map.get("dsNomeRazaoSocial");
		String dsBoleto = (String) map.get("dsBoleto");
		String obCreditoBancario = (String) map.get("obCreditoBancario");
		DateTime dhAlteracao = JTDateTimeUtils.getDataHoraAtual();

		return new CreditoBancarioEntity(id, filial, banco, usuario, dtCredito,
				vlCredito, tpModalidade, tpOrigem, tpClassificacao, tpSituacao, dsCpfCnpj,
				dsNomeRazaoSocial, dsBoleto, obCreditoBancario, dhAlteracao);
	}

	@Override
	public List<CreditoBancarioEntity> find(Map map) {
		validateIfThereIsAtLeastOneFieldFilled(map);
		
		return getCreditoBancarioDAO().findComTotal(map);
	}

	private void validateIfThereIsAtLeastOneFieldFilled(Map map) {
		boolean isThereOneFieldFilled = verifyIfThereIsAtLeastOneFieldFilled(map);
		
		if (!isThereOneFieldFilled) {
			throw new BusinessException("LMS-00055");
		}
	}
	
	private boolean verifyIfThereIsAtLeastOneFieldFilled(Map map) {
		for (Object key : map.keySet()) {
			Object value = map.get(key);
			
			if (value != null) {
				
				if (value instanceof String) {
					if (!((String) value).isEmpty()) {
						return true;
					}
				} else {
					return true;	
				}
				
			}
		}
		
		return false;
	}
	
	@Override
	public void removeById(Long id) {
		CreditoBancarioEntity creditoBancario = this.findById(id);
		
		if (!"D".equals(creditoBancario.getTpSituacao().getValue())) {
			throw new BusinessException("LMS-36336");
		}
		
		getCreditoBancarioDAO().removeById(id);
	}
	
	public void setCreditoBancarioDAO(CreditoBancarioDAO dao) {
		setDao(dao);
	}

	public CreditoBancarioDAO getCreditoBancarioDAO() {
		return (CreditoBancarioDAO) getDao();
	}

	public FilialService getFilialService() {
		return filialService;
	}
	public BancoService getBancoService() {
		return bancoService;
	}
	public UsuarioLMSService getUsuarioLmsService() {
		return usuarioLmsService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}
	public void setUsuarioLmsService(UsuarioLMSService usuarioLmsService) {
		this.usuarioLmsService = usuarioLmsService;
	}

	public Integer findCount(Map<String, Object> map) {
		return getCreditoBancarioDAO().findCountLote(map);
	}

	public Integer findCountLote(Map<String, Object> map) {
		return getCreditoBancarioDAO().findCountLote(map);
	}

	public CreditoBancario executeIncluirCreditoBancario(CreditoBancario creditoBancario){

		creditoBancarioValidatorService.validateInput(creditoBancario);
		
		creditoBancario = creditoBancarioPopulateService.populateCreditoBancario(creditoBancario);
		
		creditoBancarioValidatorService.validate(creditoBancario); 
		
		CreditoBancarioEntity creditoPersistido = storeCreditoBancario(creditoBancario);
		
		return convertCreditoBancarioEntityToCreditoBancario(creditoPersistido);
	}

	

	private CreditoBancario convertCreditoBancarioEntityToCreditoBancario(
			CreditoBancarioEntity creditoPersistido) {
		
		DateTime dhAlteracao = creditoPersistido.getDhAlteracao();
		
		return CreditoBancario.createCreditoBancario(
				creditoPersistido.getIdCreditoBancario(), 
				creditoPersistido.getFilial().getIdFilial(), 
				creditoPersistido.getFilial().getSgFilial(),
				creditoPersistido.getBanco().getIdBanco(), 
				creditoPersistido.getBanco().getNrBanco().toString(), 
				creditoPersistido.getDtCredito(), 
				creditoPersistido.getVlCredito(), 
				creditoPersistido.getTpModalidade().getValue(),
				creditoPersistido.getTpOrigem().getValue(), 
				creditoPersistido.getTpSituacao().getValue(), 
				creditoPersistido.getDsCpfCnpj(), 
				creditoPersistido.getDsNomeRazaoSocial(), 
				creditoPersistido.getDsBoleto(),
				creditoPersistido.getObCreditoBancario(), 
				creditoPersistido.getUsuario().getIdUsuario(), 
				creditoPersistido.getUsuario().getUsuarioADSM().getLogin(), 
				new YearMonthDay( dhAlteracao.getYear(), dhAlteracao.getMonthOfYear(), dhAlteracao.getDayOfMonth()), 
				new ArrayList<String>());
	}


	private CreditoBancarioEntity convertCreditoBancarioToEntity(
			CreditoBancario creditoBancario) {
		
		Filial filial = filialService.findById(creditoBancario.getIdFilial());
		Banco banco = bancoService.findById(creditoBancario.getIdBanco());
		
		Long id = creditoBancario.getIdCreditoBancario();
		
		YearMonthDay dtCredito = creditoBancario.getDtCredito();
		BigDecimal vlCredito = creditoBancario.getVlCredito();
		DomainValue tpModalidade = new DomainValue(creditoBancario.getTpModalidade());
		DomainValue tpOrigem = new DomainValue(creditoBancario.getTpOrigem());
		DomainValue tpSituacao = new DomainValue(creditoBancario.getTpSituacao());
		String dsCpfCnpj = creditoBancario.getDsCPF_CNPJ();
		String dsNomeRazaoSocial = creditoBancario.getDsNomeRazaoSocial();
		String dsBoleto = creditoBancario.getDsBoleto();
		String obCreditoBancario = creditoBancario.getObCreditoBancario();
		DateTime dhAlteracao = JTDateTimeUtils.getDataHoraAtual();

		UsuarioLMS usuario = usuarioLmsService.findById(creditoBancario.getIdUsuario());

		return new CreditoBancarioEntity(id, filial, banco, usuario, dtCredito,
				vlCredito, tpModalidade, tpOrigem, tpSituacao, 
				dsCpfCnpj, dsNomeRazaoSocial, dsBoleto, obCreditoBancario, dhAlteracao);
	}
	
	public void setCreditoBancarioValidatorService(
			CreditoBancarioValidatorService creditoBancarioValidatorService) {
		this.creditoBancarioValidatorService = creditoBancarioValidatorService;
	}

	public ResultSetPage findPaginated(Map<String, Object> map,
			Integer currentPage, Integer pageSize) {
		
		validateIfThereIsAtLeastOneFieldFilled(map);
		
		return getCreditoBancarioDAO().findPaginated(map, currentPage, pageSize);
	}
	
	public ResultSetPage findPaginatedAba(Map<String, Object> map, Integer currentPage, Integer pageSize) {
		validateIfThereIsAtLeastOneFieldFilled(map);

		return getCreditoBancarioDAO().findPaginatedAba(map, currentPage, pageSize);
	}

	public void setCreditoBancarioPopulateService(
			CreditoBancarioPopulateService creditoBancarioPopulateService) {
		this.creditoBancarioPopulateService = creditoBancarioPopulateService;
	}

	public DomainValueService getDomainValueService() {
		return domainValueService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}

	public CreditoBancarioEntity findByIdComposicaoPagamento(Long id) {
		CreditoBancarioEntity creditoBancario = getCreditoBancarioDAO().findByIdComposicaoPagamento(id);
		if(creditoBancario.getBanco()!=null){
			creditoBancario.getBanco().getNmBanco();
		}
		return creditoBancario;
	}
	
	public void rotinaInclusaoCreditoBancario(BoletoDMN boletoDmn, Boleto boleto, String mensagem) {
		Usuario usuario = getUsuario();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("filial", extractFilial(boleto));
		map.put("banco", extractBanco(boletoDmn, boleto));
		map.put("dtCredito", boletoDmn.getDtMovimento());
		map.put("vlCredito", calculaValorCredito(boletoDmn));
		map.put("tpModalidade", new DomainValue("BO"));
		map.put("tpOrigem", new DomainValue("R"));
		map.put("tpSituacao", new DomainValue("D"));
		map.put("dsBoleto", extractDsBoleto(boletoDmn, boleto));
		map.put("obCreditoBancario", getMessage(mensagem));

		this.store(map, usuario);
	}

	private Usuario getUsuario() {
		if (SessionUtils.getFilialSessao() == null) {
			return usuarioService.findById(USUARIO_INTEGRACAO);
		}
		
		return SessionUtils.getUsuarioLogado();
	}

	private String extractDsBoleto(BoletoDMN boletoDmn, Boleto boleto) {
		return boleto == null || boleto.getNrBoleto() == null ? boletoDmn.getNrBoleto() : boleto.getNrBoleto();
	}

	private BigDecimal calculaValorCredito(BoletoDMN boletoDmn) {
		return boletoDmn.getVlTotal().subtract(boletoDmn.getVlAbatimento()).subtract(boletoDmn.getVlDesconto()).add(boletoDmn.getVlJuros());
	}

	private Long extractBanco(BoletoDMN boletoDmn,
			Boleto boleto) {
		
		return boleto != null && boleto.getCedente() != null 
				? boleto.getCedente().getAgenciaBancaria().getBanco().getIdBanco() 
				: bancoService.findByNrBanco(boletoDmn.getNrBanco().toString()).getIdBanco();
	}

	private long extractFilial(Boleto boleto) {
		final Long matrizId = 361L;
		
		return boleto == null || boleto.getFatura() == null ? matrizId  : boleto.getFatura().getFilialByIdFilial().getIdFilial();
	}

	
	public Map<String, Object> executeAlocarCreditos(Map params) {
		Redeco redeco = redecoService.findById(MapUtils.getLong(params, "idRedeco"));
		Long idRedeco = redeco.getIdRedeco();		

		Map<String, Object> findSomatorio = redecoService.findSomatoriosRedeco(idRedeco);
		BigDecimal vlLiquido = (BigDecimal) findSomatorio.get("vl_total_comp_pagto");
		BigDecimal vlTotalRecebido = (BigDecimal) findSomatorio.get("vl_total_recebido");
		
		int processados = 0;
		BigDecimal utilizado = new BigDecimal(0);

		if (CompareUtils.eq(vlTotalRecebido, vlLiquido)) {
			throw new BusinessException("LMS-36377");

		} else if (CompareUtils.gt(vlTotalRecebido, vlLiquido)) {
			//Processar linha a linha retornada na consulta afim de incluir novas composições com base nos créditos retornados.
			//Iniciar na primeira linha e assim consecutivamente, até alcançar o valor necessário
			BigDecimal vlSaldoPagar = vlTotalRecebido.subtract(vlLiquido);
			ComposicaoPagamentoRedeco composicaoPagamentoRedeco = new ComposicaoPagamentoRedeco();

			List<CreditoBancarioEntity> creditosBancarios = getCreditoBancarioDAO().getCreditosSomaFilterSqlQuery(params);
			for (CreditoBancarioEntity creditoBancario : creditosBancarios) {
				BigDecimal vlSaldoCreditoBanc = creditoBancario.getSaldo();
				if (CompareUtils.gt(vlSaldoCreditoBanc, BigDecimal.ZERO) && CompareUtils.le(vlSaldoCreditoBanc, vlSaldoPagar)) {
					//incluir um lançamento na tabela de composições (COMPOSICAO_PAGAMENTO_REDECO) com o 
					//valor do saldo no campo VL_PAGAMENTO e a data do crédito no campo DT_PAGAMENTO.
					composicaoPagamentoRedeco = new ComposicaoPagamentoRedeco();
					composicaoPagamentoRedeco.setVlPagamento(vlSaldoCreditoBanc);
					composicaoPagamentoRedeco.setDtPagamento(creditoBancario.getDtCredito());
					composicaoPagamentoRedeco.setBanco(creditoBancario.getBanco());
					composicaoPagamentoRedeco.setCreditoBancario(creditoBancario);
					composicaoPagamentoRedeco.setFilial(creditoBancario.getFilial());
					composicaoPagamentoRedeco.setRedeco(redeco);
					composicaoPagamentoRedeco.setTpComposicaoPagamentoRedeco(new DomainValue("B"));
					composicaoPagamentoRedecoService.store(composicaoPagamentoRedeco);

					vlSaldoPagar = vlSaldoPagar.subtract(vlSaldoCreditoBanc);
					utilizado = utilizado.add(vlSaldoCreditoBanc);
					processados++;

				} else if (CompareUtils.gt(vlSaldoCreditoBanc, vlSaldoPagar)) {
					composicaoPagamentoRedeco = new ComposicaoPagamentoRedeco();
					composicaoPagamentoRedeco.setVlPagamento(vlSaldoPagar);
					composicaoPagamentoRedeco.setDtPagamento(creditoBancario.getDtCredito());
					composicaoPagamentoRedeco.setBanco(creditoBancario.getBanco());
					composicaoPagamentoRedeco.setCreditoBancario(creditoBancario);
					composicaoPagamentoRedeco.setFilial(creditoBancario.getFilial());
					composicaoPagamentoRedeco.setRedeco(redeco);
					composicaoPagamentoRedeco.setTpComposicaoPagamentoRedeco(new DomainValue("B"));
					composicaoPagamentoRedecoService.store(composicaoPagamentoRedeco);
					
					utilizado = utilizado.add(vlSaldoPagar);
					vlSaldoPagar = vlSaldoPagar.subtract(vlSaldoCreditoBanc);
					processados++;
					
				}

				if (CompareUtils.le(vlSaldoPagar, BigDecimal.ZERO)) {
					return finished(processados, utilizado);
					
				}
			}
		}
		//LMS-36378 - Processamento finalizado. Alocados {0} créditos bancários, totalizando R$ {1}
		return finished(processados, utilizado);
		
	}
	
	private Map<String, Object> finished(int processados, BigDecimal utilizado) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("processados", processados);
		result.put("vlUtilizado", utilizado.setScale(2, RoundingMode.UNNECESSARY));
		
		return result;
		
	}
	
	public void executeLiberarCreditoBancario(Long idCreditoBancario,
			YearMonthDay dataDeCorte) {
		validateUsuarioMatriz();
		validateDataCorte(dataDeCorte);
		
		List<CreditoBancarioEntity> creditosBancarios  = getCreditoBancarioDAO().findCreditosBancariosALiberar(dataDeCorte);
		if(creditosBancarios.size() < 1){
			return;
		}
		
		JmsMessageSender jmsMessageSender = integracaoJmsService.createMessage(Queues.FINANCEIRO_CREDITO_BANCARIO);
		for(CreditoBancarioEntity entity : creditosBancarios) {
			entity.setTpSituacao(CreditoBancarioEntity.TP_SITUACAO_LIBERADO);
			jmsMessageSender.addMsg(convertCreditoBancarioEntityToCreditoBancario(entity));			
		}
		
		this.storeAll(creditosBancarios);
		integracaoJmsService.storeMessage(jmsMessageSender);		
	}
	
	/**
	 * Jira LMSA-2234
	 * 
	 * @param dtCorte
	 * @return List<Object[]>
	 */
	public List<Map<String, Object>> findCreditosBancariosByDataCorte(String dtCorte) {
		List<Object[]> dadosCredtosBancarios = getCreditoBancarioDAO().findCreditosBancariosByDataCorte(dtCorte);
		List<Map<String, Object>> creditosBancarios = new ArrayList<Map<String,Object>>();
			
		//converte o retorno
		SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat toDMA = new SimpleDateFormat("dd/MM/yyyy");
		for (Object[] object : dadosCredtosBancarios) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idCreditoBancario", object[0]);
			map.put("filial", object[1]);
			map.put("banco", object[2]);
			map.put("nmBanco", object[3]);
			String dtCredito = null;
			if (null != object[4]) {
				try {
					dtCredito = toDMA.format(fromDB.parse((String)object[4]));
				} catch (ParseException e) {
					log.error(e);
				}
			}
			map.put("dtCredito", dtCredito != null ? dtCredito : null);
			map.put("tpModalidade", object[5]);
			map.put("tpOrigem", object[6]);
			map.put("tpClassificacao", object[15]);
			map.put("tpSituacao", object[7]);
			map.put("dsCpfCnpj", object[8]);
			map.put("dsNomeRazaoSocial", object[9]);
			map.put("dsBoleto", object[10]);
			map.put("obCreditoBancario", object[11]);
			map.put("vlCredito", object[12] != null ? NumberFormat.getNumberInstance().format(object[12]) : null);
			map.put("vlUtilizado", object[13] != null ? NumberFormat.getNumberInstance().format(object[13]) : null);
			map.put("vlSaldo", object[14] != null ? NumberFormat.getNumberInstance().format(object[14]) : null);

			creditosBancarios.add(map);
		}

		return creditosBancarios;
		
	}

	
	private void validateUsuarioMatriz() {
		if (!"MTZ".equals(SessionUtils.getFilialSessao().getSgFilial())) {
			throw new BusinessException("LMS-00071");
		}
	}
	
	private void validateDataCorte(YearMonthDay dataDeCorte) {
		if (isDataMaior(dataDeCorte, JTDateTimeUtils.getDataAtual())) {
			throw new BusinessException("LMS-36350");
		}
	}
	
	private boolean isDataMaior(YearMonthDay dataDeCorte,
			YearMonthDay dataAtual) {
		
		return JTDateTimeUtils.comparaData(dataDeCorte, dataAtual) > 0;
	}
	
	public void setIntegracaoJmsService(IntegracaoJmsService integracaoJmsService) {
		this.integracaoJmsService = integracaoJmsService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}

	public void setRedecoService(RedecoService redecoService) {
		this.redecoService = redecoService;
	}

	public void setComposicaoPagamentoRedecoService(ComposicaoPagamentoRedecoService composicaoPagamentoRedecoService) {
		this.composicaoPagamentoRedecoService = composicaoPagamentoRedecoService;
	}
}
