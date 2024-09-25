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
 * Classe de serviço para CRUD:
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar este
 * serviço.
 * 
 * @spring.bean id="lms.tributos.descontoInssCarreteiroService"
 */
public class DescontoInssCarreteiroService extends CrudService<DescontoInssCarreteiro, Long> {

	private FilialService filialService;
	private ProprietarioService proprietarioService;

	public List<Map<String, Object>> findRecibosOutrasEmpresas(TypedFlatMap tfm) {
		List<Map<String, Object>> toReturn = new ArrayList<Map<String, Object>>();
		
		// 	Verificar se Filial, Proprietário ou Número recibo foram informados, caso contrário mostrar mensagem "LMS-24037" 
		if(tfm.getLong("filial.idFilial") == null    
				&& tfm.getLong("proprietario.idProprietario") == null   
				&& tfm.getString("nrRecibo") == null){
			throw new BusinessException("LMS-24037", new Object[]{"Filial", "Proprietário", "Número Recibo"});
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
			//Identificador empregador é opcional
			if (descontoInssCarreteiro.getTpIdentificacao() != null) {
				map.put("tpIdentificador", descontoInssCarreteiro.getTpIdentificacao().getValue());
			}	
			map.put("nrIdentificador", descontoInssCarreteiro.getNrIdentEmpregador());
			map.put("dsEmpresa", descontoInssCarreteiro.getDsEmpresa());

			/* A filial é facultativa */
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
	 * Busca o valor total do INSS do proprietário informado, no mes/ano
	 * informado.<BR>
	 *
	 * @author Robson Edemar Gehl
	 * @param idPessoa
	 *            proprietário
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
	 *            proprietário
	 * @param dtBase
	 *            mes e ano
	 * @return List de DescontoInss
	 */
	public List findByProprietario(Long idProprietario, YearMonthDay dtBase) {
		return getDescontoInssCarreteiroDAO().findByProprietario(idProprietario, dtBase);
	}

	/**
	 * Recupera uma instância de <code>DescontoInssCarreteiro</code> a partir do
	 * ID.
	 *
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public DescontoInssCarreteiro findById(java.lang.Long id) {
		return (DescontoInssCarreteiro) super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids
	 *            lista com as entidades que deverão ser removida.
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
		// validação do valor do INSS
		if (BigDecimal.ZERO.compareTo(bean.getVlInss()) >= 0) {
			throw new BusinessException("LMS-24051", new Object[] { "Valor do INSS" });
		}
	}

	private void validaValorRemuneracao(DescontoInssCarreteiro bean) {
		// validação do valor da remuneração
		if (BigDecimal.ZERO.compareTo(bean.getVlRemuneracao()) >= 0 ) {
			throw new BusinessException("LMS-24051", new Object[] { "Valor remuneração" });
		}
	}

	private void validaRecibo(DescontoInssCarreteiro bean) {
		// validação do número do recibo
		if (StringUtils.isEmpty(bean.getNrRecibo())) {
			throw new BusinessException("LMS-24051", new Object[] { "Número recibo" });
		}

		// validação da data de emissão do recibo
		if (JTDateTimeUtils.getDataAtual().compareTo(bean.getDtEmissaoRecibo()) < 0) {
			throw new BusinessException("LMS-23008");
		}
	}

	private void validaValorInssRemuneracao(DescontoInssCarreteiro bean) {
		// se o Valor do INSS for maior que o Valor da remuneração apresenta a
		// mensagem LMS-24036
		if (bean.getVlInss().compareTo(bean.getVlRemuneracao()) > 0) {
			throw new BusinessException("LMS-24036");
		}
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
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
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setDescontoInssCarreteiroDAO(DescontoInssCarreteiroDAO dao) {
		setDao(dao);
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 *
	 * @return Instância do DAO.
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