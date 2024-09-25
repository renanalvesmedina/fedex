package com.mercurio.lms.tributos.model.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.tributos.model.DescontoInssCarreteiro;
import com.mercurio.lms.tributos.model.dao.DescontoInssCarreteiroDAO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Classe de servi�o para CRUD:
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * servi�o.
 * 
 * @spring.bean id="lms.tributos.descontoInssCarreteiroService"
 */
public class DescontoInssCarreteiroService extends CrudService<DescontoInssCarreteiro, Long> {

	private FilialService filialService;
	private ProprietarioService proprietarioService;

	public List<Map<String, Object>> findRecibosOutrasEmpresas(TypedFlatMap tfm) {
		List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
		
		// 	Verificar se Filial, Propriet�rio ou N�mero recibo foram informados, caso contr�rio mostrar mensagem "LMS-24037" 
		if(tfm.getLong("filial.idFilial") == null    
				&& tfm.getLong("proprietario.idProprietario") == null   
				&& tfm.getString("nrRecibo") == null){
			throw new BusinessException("LMS-24037", new Object[]{"Filial", "Propriet�rio", "N�mero Recibo"});
		}
		
		List<DescontoInssCarreteiro> listDescontoInssCarreteiro = getDescontoInssCarreteiroDAO().findRecibosOutrasEmpresas(tfm);

		for (DescontoInssCarreteiro descontoInssCarreteiro : listDescontoInssCarreteiro) {
			Map<String, Object> map = new HashMap<String, Object>();

			map.put("idDescontoInssCarreteiro", descontoInssCarreteiro.getIdDescontoInssCarreteiro());
			map.put("nrIdentificacao", FormatUtils.formatIdentificacao(descontoInssCarreteiro.getProprietario().getPessoa()));
			map.put("nmProprietario", descontoInssCarreteiro.getProprietario().getPessoa().getNmPessoa());
			map.put("dtEmissaoRecibo", descontoInssCarreteiro.getDtEmissaoRecibo());
			map.put("nrRecibo", descontoInssCarreteiro.getNrRecibo());
			map.put("vlInss", descontoInssCarreteiro.getVlInss());
			map.put("vlRemuneracao", descontoInssCarreteiro.getVlRemuneracao());
			//Identificador empregador � opcional
			if (descontoInssCarreteiro.getTpIdentificacao() != null) {
				map.put("tpIdentificador", descontoInssCarreteiro.getTpIdentificacao().getValue());
			}	
			map.put("nrIdentificador", descontoInssCarreteiro.getNrIdentEmpregador());
			map.put("dsEmpresa", descontoInssCarreteiro.getDsEmpresa());

			/* A filial � facultativa */
			if (descontoInssCarreteiro.getFilial() != null) {
				map.put("sgFilial", descontoInssCarreteiro.getFilial().getSgFilial());
				map.put("nmFilial", descontoInssCarreteiro.getFilial().getPessoa().getNmFantasia());
			}

			toReturn.add(map);
		}

		return toReturn;
	}

	public Integer getRowCountRecibosOutrasEmpresas() {
		return getDescontoInssCarreteiroDAO().getRowCountRecibosOutrasEmpresas();
	}	

	/**
	 * Busca o valor total do INSS do propriet�rio informado, no mes/ano
	 * informado.<BR>
	 *
	 * @author Robson Edemar Gehl
	 * @param idPessoa
	 *            propriet�rio
	 * @param dtBase
	 *            mes e ano
	 * @return valor total do inss
	 */
	public BigDecimal findTotalValorINSS(Long idPessoa, YearMonthDay dtBase) {
		return getDescontoInssCarreteiroDAO().findTotalValorINSS(idPessoa, dtBase);
	}

	/**
	 * Busca uma lista de descontoInss a partir de um proprietario.
	 * 
	 * @param idProprietario
	 *            propriet�rio
	 * @param dtBase
	 *            mes e ano
	 * @return List de DescontoInss
	 */
	public List findByProprietario(Long idProprietario, YearMonthDay dtBase) {
		return getDescontoInssCarreteiroDAO().findByProprietario(idProprietario, dtBase);
	}

	/**
	 * Recupera uma inst�ncia de <code>DescontoInssCarreteiro</code> a partir do
	 * ID.
	 *
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public DescontoInssCarreteiro findById(java.lang.Long id) {
		return (DescontoInssCarreteiro) super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id
	 *            indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids
	 *            lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	protected DescontoInssCarreteiro beforeInsert(DescontoInssCarreteiro bean) {
		// LMS-5590
		validaIdentificacaoEmpregador(bean);
		validaValorInss(bean);
		validaValorRemuneracao(bean);
		validaRecibo(bean);
		validaValorInssRemuneracao(bean);

		bean.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		return super.beforeInsert(bean);
	}

	private void validaIdentificacaoEmpregador(DescontoInssCarreteiro bean) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("tpIdentificacao", bean.getTpIdentificacao().getValue());
		map.put("nrIdentificacao", bean.getNrIdentEmpregador());

		proprietarioService.validateCpfCnpj(new TypedFlatMap(map));
		
		}

	private void validaValorInss(DescontoInssCarreteiro bean) {
		// valida��o do valor do INSS
		if (BigDecimal.ZERO.compareTo(bean.getVlInss()) >= 0) {
			throw new BusinessException("LMS-24051", new Object[] { "Valor do INSS" });
		}
	}

	private void validaValorRemuneracao(DescontoInssCarreteiro bean) {
		// valida��o do valor da remunera��o
		if (BigDecimal.ZERO.compareTo(bean.getVlRemuneracao()) >= 0 ) {
			throw new BusinessException("LMS-24051", new Object[] { "Valor remunera��o" });
		}
	}

	private void validaRecibo(DescontoInssCarreteiro bean) {
		// valida��o do n�mero do recibo
		if (StringUtils.isEmpty(bean.getNrRecibo())) {
			throw new BusinessException("LMS-24051", new Object[] { "N�mero recibo" });
		}

		// valida��o da data de emiss�o do recibo
		if (JTDateTimeUtils.getDataAtual().compareTo(bean.getDtEmissaoRecibo()) < 0) {
			throw new BusinessException("LMS-23008");
		}
	}

	private void validaValorInssRemuneracao(DescontoInssCarreteiro bean) {
		// se o Valor do INSS for maior que o Valor da remunera��o apresenta a
		// mensagem LMS-24036
		if (bean.getVlInss().compareTo(bean.getVlRemuneracao()) > 0) {
			throw new BusinessException("LMS-24036");
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contr�rio.
	 *
	 * @param bean
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	// FIXME corrigir para retornar o ID
	public DescontoInssCarreteiro store(DescontoInssCarreteiro bean) {
		beforeInsert(bean);
		super.store(bean);
		return bean;
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
	 * servi�o.
	 * 
	 * @param Inst�ncia
	 *            do DAO.
	 */
	public void setDescontoInssCarreteiroDAO(DescontoInssCarreteiroDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
	 * dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private DescontoInssCarreteiroDAO getDescontoInssCarreteiroDAO() {
		return (DescontoInssCarreteiroDAO) getDao();
	}

	public FilialService getFilialService() {
		return filialService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

}