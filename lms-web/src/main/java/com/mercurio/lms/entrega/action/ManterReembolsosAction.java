package com.mercurio.lms.entrega.action;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.service.AgenciaBancariaService;
import com.mercurio.lms.configuracoes.model.service.BancoService;
import com.mercurio.lms.entrega.model.ChequeReembolso;
import com.mercurio.lms.entrega.model.service.ReciboReembolsoService;
import com.mercurio.lms.entrega.report.EmitirReciboReembolsoService;
import com.mercurio.lms.expedicao.model.service.ConhecimentoService;
import com.mercurio.lms.expedicao.model.service.CtoInternacionalService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.pendencia.model.service.MdaService;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.entrega.manterReembolsosAction"
 */

public class ManterReembolsosAction extends ReportActionSupport {
	private ClienteService clienteService;
	private FilialService filialService;
	private DomainValueService domainValueService;
	private ConhecimentoService conhecimentoService;
	private CtoInternacionalService ctoInternacionalService;
	private MdaService mdaService;
	private BancoService bancoService;
	private AgenciaBancariaService agenciaBancariaService;
	private EmitirReciboReembolsoService emitirReciboReembolsoService;
	private ReciboReembolsoService reciboReembolsoService;

	public void removeById(java.lang.Long id) {
		reciboReembolsoService.removeById(id);
	}

	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		reciboReembolsoService.removeByIds(ids);
	}

	public Map<String, Object> findById(Long id) {
		return reciboReembolsoService.findForUpdate(id);
	}

	public List<Map<String, Object>> findLookupCLiente(Map<String, Object> criteria) {
		return clienteService.findLookup(criteria);
	}

	public List<Map<String, Object>> findLookupFilial(TypedFlatMap criteria) {
		return filialService.findLookupFilial(criteria);
	}

	public List findTipoDocumentoServico(Map<String, Object> criteria) {
		List<String> dominiosValidos = new ArrayList<String>();
		dominiosValidos.add("CTR");
		dominiosValidos.add("CRT");
		dominiosValidos.add("NFT");
		dominiosValidos.add("MDA");
		dominiosValidos.add("NTE");
		dominiosValidos.add("CTE");

		List retorno = domainValueService.findByDomainNameAndValues("DM_TIPO_DOCUMENTO_SERVICO", dominiosValidos);
		return retorno;
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return reciboReembolsoService.findPaginated(criteria);
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return reciboReembolsoService.getRowCount(criteria);
	}

	public List<Map<String, Object>> findLookupServiceDocumentFilialRRE(Map<String, Object> criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List<Map<String, Object>> findLookupServiceDocumentFilialCTR(Map<String, Object> criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List<Map<String, Object>> findLookupServiceDocumentFilialCRT(Map<String, Object> criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List<Map<String, Object>> findLookupServiceDocumentFilialMDA(Map<String, Object> criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}
	
	public List<Map<String, Object>> findLookupServiceDocumentFilialNTE(Map<String, Object> criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List<Map<String, Object>> findLookupServiceDocumentFilialCTE(Map<String, Object> criteria) {
		return findLookupFilialByDocumentoServico(criteria);
	}

	public List<Map<String, Object>> findLookupFilialByDocumentoServico(Map<String, Object> criteria) {
		List<Filial> list = filialService.findLookup(criteria);
		List<Map<String, Object>> retorno = new ArrayList<Map<String, Object>>();
		for(Filial filial : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("idFilial", filial.getIdFilial());
			map.put("sgFilial", filial.getSgFilial());
			map.put("pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
			retorno.add(map);
		}
		return retorno;
	}

	public List findLookupServiceDocumentNumberRRE(Map<String, Object> criteria) {
		List listaRecibo = reciboReembolsoService.findLookup(criteria);
		return listaRecibo;
	}

	public List findLookupServiceDocumentNumberCTR(Map<String, Object> criteria) {
		return conhecimentoService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberNFT(Map<String, Object> criteria) {
		return conhecimentoService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberCRT(Map<String, Object> criteria) {
		return ctoInternacionalService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberMDA(Map<String, Object> criteria) {
		return mdaService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberNTE(Map<String, Object> criteria) {
		return conhecimentoService.findLookup(criteria);
	}

	public List findLookupServiceDocumentNumberCTE(Map<String, Object> criteria) {
		return conhecimentoService.findLookup(criteria);
	}

	public Serializable storeCheques(TypedFlatMap map) {
		Long idDoctoServico = map.getLong("idDoctoServico");

		List<ChequeReembolso> chequesReembolso = new ArrayList<ChequeReembolso>();
		// Lista de cheques recebida da tela 'Manter Reembolsos', tela do LMS.
		List<TypedFlatMap> cheques = map.getList("chequeReembolso");
		if (cheques != null) {
			for(TypedFlatMap cheque : cheques) {
				Integer nrCheque = cheque.getInteger("nrCheque");
				BigDecimal vlCheque = cheque.getBigDecimal("valorCheque");
				YearMonthDay dtCheque = cheque.getYearMonthDay("data");
				if(nrCheque == null
					&& vlCheque == null
					&& dtCheque == null
				) {
					continue;
				}
				ChequeReembolso chequeReembolso = new ChequeReembolso();
				chequeReembolso.setIdChequeReembolso(cheque.getLong("id"));
				chequeReembolso.setNrBanco(cheque.getInteger("nrBanco"));
				chequeReembolso.setNrAgencia(cheque.getInteger("nrAgencia"));
				chequeReembolso.setDvAgencia(cheque.getString("dvAgencia"));
				chequeReembolso.setNrCheque(nrCheque);
				chequeReembolso.setVlCheque(vlCheque);
				chequeReembolso.setDtCheque(cheque.getYearMonthDay("data"));
				chequesReembolso.add(chequeReembolso);
			}
		}

		return reciboReembolsoService.storeCheques(idDoctoServico, chequesReembolso);
	}

	public void validaNrBanco(Short nrBanco) {
		bancoService.findNrBanco(nrBanco);
	}

	public void validaNrAgencia(TypedFlatMap map) {
		Short nrBanco = map.getShort("nrBanco");
		Short nrAgencia = map.getShort("nrAgencia");
		agenciaBancariaService.findNrAgencia(nrBanco,nrAgencia);
	}

	public List<Map<String, Object>> findCheques(TypedFlatMap map) {
		return reciboReembolsoService.findCheques(map);
	}

	public Integer getRowCountCheques(TypedFlatMap criteria) {
		return reciboReembolsoService.getRowCountCheques(criteria);
	}

	public TypedFlatMap findFilialUsuarioLogado() {
		TypedFlatMap typedFlatMap = new TypedFlatMap();
		Filial filial = SessionUtils.getFilialSessao();

		typedFlatMap.put("filialByIdFilialOrigem.sgFilial", filial.getSgFilial());
		typedFlatMap.put("filialByIdFilialOrigem.pessoa.nmFantasia", filial.getPessoa().getNmFantasia());
		typedFlatMap.put("filialByIdFilialOrigem.idFilial", filial.getIdFilial());
		typedFlatMap.put("idFilialSessao", filial.getIdFilial());
		typedFlatMap.put("sgFilialSessao", filial.getSgFilial());

		return typedFlatMap;
	}

	@Override
	public File execute(TypedFlatMap parameters) throws Exception {
		parameters.put("idReciboReembolso",parameters.getLong("idDoctoServico"));
		parameters.put("isReemitir", Boolean.TRUE);
		this.reportServiceSupport = emitirReciboReembolsoService;		

		return super.execute(parameters);
	}

	public boolean validaTotalCheques(TypedFlatMap infCheques) {
		return reciboReembolsoService.verifyTotalCheques(infCheques);
	}

	public void setReciboReembolsoService(ReciboReembolsoService reciboReembolsoService) {
		this.reciboReembolsoService = reciboReembolsoService;
	}
	public void setEmitirReciboReembolsoService(EmitirReciboReembolsoService emitirReciboReembolsoService) {
		this.emitirReciboReembolsoService = emitirReciboReembolsoService;
	}
	public void setAgenciaBancariaService(AgenciaBancariaService agenciaBancariaService) {
		this.agenciaBancariaService = agenciaBancariaService;
	}
	public void setConhecimentoService(ConhecimentoService conhecimentoService) {
		this.conhecimentoService = conhecimentoService;
	}
	public void setCtoInternacionalService(CtoInternacionalService ctoInternacionalService) {
		this.ctoInternacionalService = ctoInternacionalService;
	}
	public void setMdaService(MdaService mdaService) {
		this.mdaService = mdaService;
	}
	public void setDomainValueService(DomainValueService domainValueService) {
		this.domainValueService = domainValueService;
	}
	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.clienteService = clienteService;
	}
	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}

}
