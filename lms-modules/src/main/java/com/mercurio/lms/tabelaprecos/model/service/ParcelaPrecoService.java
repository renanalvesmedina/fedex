package com.mercurio.lms.tabelaprecos.model.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.session.SessionContext;
import com.mercurio.lms.municipios.model.Empresa;
import com.mercurio.lms.tabelaprecos.model.ParcelaPreco;
import com.mercurio.lms.tabelaprecos.model.dao.ParcelaPrecoDAO;
import com.mercurio.lms.tabelaprecos.model.dao.TabelaPrecoParcelaDAO;
import com.mercurio.lms.util.session.SessionKey;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.tabelaprecos.parcelaPrecoService"
 */
public class ParcelaPrecoService extends CrudService<ParcelaPreco, Long> {
	private TabelaPrecoParcelaDAO tabelaPrecoParcelaDAO;

	public void setTabelaPrecoParcelaDAO(TabelaPrecoParcelaDAO tabelaPrecoParcelaDAO) {
		this.tabelaPrecoParcelaDAO = tabelaPrecoParcelaDAO;
	}

	/**
	 * Recupera uma instância de <code>ParcelaPreco</code> a partir do ID.
	 * 
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	@Override
	public ParcelaPreco findById(java.lang.Long id) {
		return (ParcelaPreco) super.findById(id);
	}

	@Override
	public ParcelaPreco findByIdInitLazyProperties(Long id, boolean initializeLazyProperties) {
		return (ParcelaPreco)super.findByIdInitLazyProperties(id, initializeLazyProperties);
	}
	
	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id indica a entidade que deverá ser removida.
	 */
	@Override
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga várias entidades através do Id.
	 * 
	 * @param ids lista com as entidades que deverão ser removida.
	 */
	@Override
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List<Long> ids) {
		super.removeByIds(ids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mercurio.adsm.framework.model.CrudService#beforeStore(java.lang.Object)
	 */
	@Override
	protected ParcelaPreco beforeStore(ParcelaPreco bean) {
		// Se campo 'Tipo de parcela' igual a "S" (Serviço Adicional)
		// deve informar o campo 'Serviço adicional'
		if(bean.getTpParcelaPreco().getValue().equals("S")) {
			if( (bean.getServicoAdicional() == null) || (bean.getServicoAdicional().getIdServicoAdicional().longValue() < 1) ) {
				throw new BusinessException("LMS-30009");
			}
		} else {
			bean.setServicoAdicional(null);
		}
		// Se campo 'Indicador de cálculo' igual a "VU" (Valor por Unidade de Medida)
		// deve informar o campo 'Unidade de medida'
		if(bean.getTpIndicadorCalculo().getValue().equals("VU")) {
			if( (bean.getUnidadeMedida() == null) || (bean.getUnidadeMedida().getIdUnidadeMedida().longValue() < 1) ) {
				throw new BusinessException("LMS-30010");
			}
		} else {
			bean.setUnidadeMedida(null);
		}

		// obtem a empresa do usuário logado
		Empresa empresa = (Empresa) SessionContext.get(SessionKey.EMPRESA_KEY);
		bean.setEmpresa(empresa);

		return super.beforeStore(bean);
	}

	@Override
	protected ParcelaPreco beforeUpdate(ParcelaPreco bean) {
		ParcelaPreco parcelaPrecoNew = bean;
		//cria filtro para pesquisar TabelaPrecoParcela
		Map<String, Object> filter = new HashMap<String, Object>();
		filter.put("parcelaPreco.idParcelaPreco", parcelaPrecoNew.getIdParcelaPreco());
		//retorna numero de ocorrências na TabelaPrecoParcela
		Integer nrTabelaPrecoParcelaCount = this.tabelaPrecoParcelaDAO.getRowCount(filter);
		if(nrTabelaPrecoParcelaCount.intValue() > 0) {
			//busca o pojo original para comparar e
			//garantir que nenhum outro campo seja alterado
			ParcelaPreco parcelaPreco = this.findById(parcelaPrecoNew.getIdParcelaPreco());
			//testa se algum campo diferente de tpSituacao foi alterado
			EqualsBuilder equals = new EqualsBuilder();
			equals.append(parcelaPreco.getTpParcelaPreco(), parcelaPrecoNew.getTpParcelaPreco());
			equals.append(parcelaPreco.getServicoAdicional(), parcelaPrecoNew.getServicoAdicional());
			equals.append(parcelaPreco.getTpPrecificacao(), parcelaPrecoNew.getTpPrecificacao());
			equals.append(parcelaPreco.getDsParcelaPreco().getValue(), parcelaPrecoNew.getDsParcelaPreco().getValue());
			equals.append(parcelaPreco.getNmParcelaPreco().getValue(), parcelaPrecoNew.getNmParcelaPreco().getValue());
			equals.append(parcelaPreco.getBlIncideIcms(), parcelaPrecoNew.getBlIncideIcms());
			equals.append(parcelaPreco.getBlEmbuteParcela(), parcelaPrecoNew.getBlEmbuteParcela());
			equals.append(parcelaPreco.getTpIndicadorCalculo(), parcelaPrecoNew.getTpIndicadorCalculo());
			equals.append(parcelaPreco.getUnidadeMedida(), parcelaPrecoNew.getUnidadeMedida());
			//lança exception se algum campo diferente de tpSituacao foi alterado
			if(!equals.isEquals()) {
				throw new BusinessException("LMS-30006");
			}
			//caso contrário, altera o campo tpSituacao no pojo original
			parcelaPreco.getTpSituacao().setValue(parcelaPrecoNew.getTpSituacao().getValue());
			return super.beforeUpdate(parcelaPreco);
		}
		//se não existem ocorrências na TabelaPrecoParcela retorna normalmente
		return super.beforeUpdate(bean);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	@Override
	public java.io.Serializable store(ParcelaPreco bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância do DAO.
	 */
	public void setParcelaPrecoDAO(ParcelaPrecoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private ParcelaPrecoDAO getParcelaPrecoDAO() {
		return (ParcelaPrecoDAO) getDao();
	}

	public Map<String, Object> findParcelaByCdParcelaPreco(String cdParcelaPreco) {
		return getParcelaPrecoDAO().findParcelaByCdParcelaPreco(cdParcelaPreco);
	}

	public ParcelaPreco findByCdParcelaPreco(String cdParcelaPreco) {
		return getParcelaPrecoDAO().findByCdParcelaPreco(cdParcelaPreco);
	}

	public List<Map<String, Object>> findParcelaByTpParcelaPreco(Long idTabelaPreco, String tpParcelaPreco) {
		return getParcelaPrecoDAO().findParcelaByTpParcelaPreco(idTabelaPreco, tpParcelaPreco);
	}

	public ParcelaPreco findByIdServicoAdicional(Long idServicoAdicional) {
		return getParcelaPrecoDAO().findByIdServicoAdicional(idServicoAdicional);
	}
	
	public List<ParcelaPreco> findGeneralidadesExcluindoAlgunsTipos(Long idTabelaPreco, Long[] idsAExcluir) {
		return getParcelaPrecoDAO().findGeneralidadesExcluindoAlgunsTipos(idTabelaPreco, idsAExcluir);
	}

	public List<Map<String, Object>> findServicosAdicionaisParcela() {
		return getParcelaPrecoDAO().findServicosAdicionaisParcela();
	}

	public Long findIdParcelaByCdParcelaPreco(String cdParcelaPreco) {
		Map<String, Object> map = getParcelaPrecoDAO().findParcelaByCdParcelaPreco(cdParcelaPreco);
		return (Long)map.get("idParcelaPreco");
	}
	
	public List<Map<String, Object>> findParcelaPreco() {
		return getParcelaPrecoDAO().findParcelasAtivas();
	}
	
	public List<Map<String, Object>> findServicosAdicionaisFrete(Long idDoctoServico){
		return getParcelaPrecoDAO().findServicosAdicionaisFrete(idDoctoServico);
	}
	
	public ParcelaPreco findParcelaPrecoByDescParcela(String nm_parcela_preco){
		return getParcelaPrecoDAO().findParcelaPrecoByDescParcela(nm_parcela_preco);
	}
	
	public List<ParcelaPreco> findParcelaPrecoByIdTabelaPrecoParaMarkup(Long idTabelaPreco, String tpPrecificacao){
		return getParcelaPrecoDAO().findParcelaPrecoByIdTabelaPrecoParaMarkup(idTabelaPreco, tpPrecificacao);
	}
}