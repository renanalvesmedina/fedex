package com.mercurio.lms.expedicao.model.service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.expedicao.model.ServAdicionalDocServ;
import com.mercurio.lms.expedicao.model.dao.ServAdicionalDocServDAO;
import com.mercurio.lms.expedicao.util.ConstantesExpedicao;
import com.mercurio.lms.util.CompareUtils;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.servAdicionalDocServService"
 */
public class ServAdicionalDocServService extends CrudService<ServAdicionalDocServ, Long> {
	private ConfiguracoesFacade configuracoesFacade;

	/**
	 * Recupera uma inst�ncia de <code>ServAdicionalDocServ</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public ServAdicionalDocServ findById(java.lang.Long id) {
		return (ServAdicionalDocServ)super.findById(id);
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
	public java.io.Serializable store(ServAdicionalDocServ bean) {
		return super.store(bean);
	}

	/**
	 * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 * 
	 * @param Inst�ncia do DAO.
	 */
	public void setServAdicionalDocServDAO(ServAdicionalDocServDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
	 *
	 * @return Inst�ncia do DAO.
	 */
	private ServAdicionalDocServDAO getServAdicionalDocServDAO() {
		return (ServAdicionalDocServDAO) getDao();
	}

	public Integer getRowCountByIdCotacao(Long idCotacao) {
		return getServAdicionalDocServDAO().getRowCountByIdCotacao(idCotacao);
	}

	public List findIdsByIdDoctoServico(Long idDoctoServico) {
		return getServAdicionalDocServDAO().findIdsByIdDoctoServico(idDoctoServico);
	}

	public List findByCotacao(Long idCotacao) {
		return getServAdicionalDocServDAO().findByCotacao(idCotacao);
	}

	public List findNFServicoAdicionalValores(Long idDoctoServico){
		return getServAdicionalDocServDAO().findNFServicoAdicionalValores(idDoctoServico);
	}

	public ServAdicionalDocServ findByIdDoctoServico(Long idDoctoServico) {
		return getServAdicionalDocServDAO().findByIdDoctoServico(idDoctoServico);
	}

	public ResultSetPage findPaginatedByIdCotacao(Long idCotacao, Map criteria){
		FindDefinition def = FindDefinition.createFindDefinition(criteria);
		return getServAdicionalDocServDAO().findPaginatedByIdCotacao(idCotacao, def);
	}

	public Map findServById(Long id){
		return getServAdicionalDocServDAO().findServById(id);
	}

	public BigDecimal findVlMercadoriaReembolsoByDoctoServico(Long idDoctoServico){
		return getServAdicionalDocServDAO().findVlMercadoriaReembolsoByDoctoServico(idDoctoServico);
	}

	public Integer findQtChequesReembolsoByDoctoServico(Long idDoctoServico){
		return getServAdicionalDocServDAO().findQtChequesReembolsoByDoctoServico(idDoctoServico);
	}

	public List findServAdicionaisDocServByIdDoctoServico(Long idDoctoServico){
		return getServAdicionalDocServDAO().findServAdicionaisDocServByIdDoctoServico(idDoctoServico);
	}

	public void validadeServicoAdicionalDoctoServico(List servicosAdicionaisDoctoServico, ServAdicionalDocServ servAdicionalDocServ) {
		if(servAdicionalDocServ.getDtPrimeiroCheque() != null) {
			YearMonthDay dataAtual = JTDateTimeUtils.getDataAtual();
			if(CompareUtils.lt(servAdicionalDocServ.getDtPrimeiroCheque(), dataAtual)) {
				throw new BusinessException("LMS-04049");
			}
		}
		if(ConstantesExpedicao.CD_ARMAZENAGEM.equals(servAdicionalDocServ.getCdParcelaPreco())
				&& (servAdicionalDocServ.getQtDias() != null)
		) {
			Integer nrDiasMinimo = IntegerUtils.getInteger((BigDecimal) configuracoesFacade.getValorParametro(ConstantesExpedicao.NR_MINIMO_DIAS_ARMAZENAGEM));
			if(CompareUtils.lt(servAdicionalDocServ.getQtDias(), nrDiasMinimo)) {
				throw new BusinessException("LMS-04014");
			}
		}

		Long idServicoAdicionalNovo = servAdicionalDocServ.getServicoAdicional().getIdServicoAdicional();
		if(servicosAdicionaisDoctoServico != null) {
			for (Iterator iter = servicosAdicionaisDoctoServico.iterator(); iter.hasNext();) {
				ServAdicionalDocServ servAdicionalDocServAux = (ServAdicionalDocServ) iter.next();
				Long idServicoAdicionalAux = servAdicionalDocServAux.getServicoAdicional().getIdServicoAdicional();
				if(CompareUtils.eq(idServicoAdicionalAux, idServicoAdicionalNovo)
					&& (servAdicionalDocServ.getIdServAdicionalDocServ() == null || CompareUtils.ne(servAdicionalDocServAux.getIdServAdicionalDocServ(), servAdicionalDocServ.getIdServAdicionalDocServ()))
				) {
					throw new BusinessException("LMS-04056");
				}
			}
		}
	}

	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}

}