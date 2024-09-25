package com.mercurio.lms.expedicao.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.expedicao.model.CartaCorrecao;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.dao.CartaCorrecaoDAO;
import com.mercurio.lms.expedicao.util.ConhecimentoUtils;
import com.mercurio.lms.sim.ConstantesSim;
import com.mercurio.lms.sim.model.service.IncluirEventosRastreabilidadeInternacionalService;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.cartaCorrecaoService"
 */
public class CartaCorrecaoService extends CrudService<CartaCorrecao, Long> {
	private IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService;

	public void setIncluirEventosRastreabilidadeInternacionalService(IncluirEventosRastreabilidadeInternacionalService incluirEventosRastreabilidadeInternacionalService) {
		this.incluirEventosRastreabilidadeInternacionalService = incluirEventosRastreabilidadeInternacionalService;
	}

	/**
	 * Recupera uma inst�ncia de <code>CartaCorrecao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public CartaCorrecao findById(java.lang.Long id) {
		return (CartaCorrecao)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */
	public void removeById(java.lang.Long id) {
		super.removeById(id);
	}

	/**
	 * Apaga v�rias entidades atrav�s do Id.
	 *
	 * @param ids lista com as entidades que dever�o ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contr�rio.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(CartaCorrecao bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setCartaCorrecaoDAO(CartaCorrecaoDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private CartaCorrecaoDAO getCartaCorrecaoDAO() {
		return (CartaCorrecaoDAO) getDao();
	}

	public Integer getRowCountCartaCorrecao(TypedFlatMap criteria) {
		return getCartaCorrecaoDAO().getRowCountCartaCorrecao(criteria);
	}

	public ResultSetPage findPaginatedCartaCorrecao(TypedFlatMap criteria) {
		return getCartaCorrecaoDAO().findPaginatedCartaCorrecao(criteria);
	}

	public void storeCartaCorrecao(CartaCorrecao cc) {
		/** Executa Store */
		store(cc);

		/** Inclui Evento Carta de Correcao */
		CtoInternacional ctoInternacional = cc.getCtoInternacional();
		String nrCrt = ConhecimentoUtils.formatConhecimentoInternacional(ctoInternacional.getSgPais(),
				ctoInternacional.getNrPermisso(),				
				ctoInternacional.getNrCrt());
		incluirEventosRastreabilidadeInternacionalService.generateEventoDocumento(
				 ConstantesSim.EVENTO_EMISSAO_CARTA_CORRECAO_CRT
				,ctoInternacional.getIdDoctoServico()
				,SessionUtils.getFilialSessao().getIdFilial()
				,nrCrt
				,JTDateTimeUtils.getDataHoraAtual()
				,null
				,null
				,ctoInternacional.getTpDocumentoServico().getValue());
	}

	public Map findCartaCorrecaoById(Long id) {
		return getCartaCorrecaoDAO().findCartaCorrecaoById(id);
	}
}