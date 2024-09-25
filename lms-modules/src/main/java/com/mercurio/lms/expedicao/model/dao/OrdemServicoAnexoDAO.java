package com.mercurio.lms.expedicao.model.dao;

import java.util.List;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.expedicao.model.OrdemServicoAnexo;

public class OrdemServicoAnexoDAO extends BaseCrudDao<OrdemServicoAnexo, Long> {

	@SuppressWarnings("rawtypes")
	@Override
	protected Class getPersistentClass() {
		return OrdemServicoAnexo.class;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrdemServicoAnexo> findByOrdemServico(Long idOrdemServico) {
		StringBuilder query = new StringBuilder()
		.append("from " + getPersistentClass().getName() + " as anexo ")
		.append("left join fetch anexo.parcelaPreco pp ")
		.append("where ")
		.append("  anexo.ordemServico.idOrdemServico = ? ");
				
		return getAdsmHibernateTemplate().find(query.toString(), new Object[]{idOrdemServico});			
	}

	/**
	 * LMS-6538 - Busca página de <tt>OrdemServicoAnexo</tt> de determinada
	 * <tt>PreFaturaServicoItem</tt>, incluindo dados de <tt>ParcelaPreco</tt>
	 * para cada anexo.
	 * 
	 * @param findDefinition
	 *            definições de página da grid para busca dos dados
	 * @param idPreFaturaServicoItem
	 *            id da <tt>PreFaturaServicoItem</tt>
	 * @return página de <tt>OrdemServicoAnexo</tt> incluindo
	 *         <tt>ParcelaPreco</tt> relacionada
	 */
	@SuppressWarnings("unchecked")
	public ResultSetPage<OrdemServicoAnexo> findByPreFaturaServico(FindDefinition findDefinition, Long idPreFaturaServicoItem) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT osa ")
				.append("FROM OrdemServicoAnexo osa, PreFaturaServico pfs ")
				.append("JOIN FETCH osa.parcelaPreco pp ")
				.append("JOIN osa.ordemServico.ordemServicoItens osi ")
				.append("JOIN pfs.preFaturaServicoItens pfsi ")
				.append("WHERE osi.idOrdemServicoItem = pfsi.ordemServicoItem.idOrdemServicoItem ")
				.append("AND pp.idParcelaPreco = osi.parcelaPreco.idParcelaPreco ")
				.append("AND pfsi.idPreFaturaServicoItem = ? ");
		return getAdsmHibernateTemplate().findPaginated(hql.toString(), findDefinition.getCurrentPage(),
				findDefinition.getPageSize(), new Object[] { idPreFaturaServicoItem });
	}

	/**
	 * LMS-6538 - Busca quantidade de <tt>OrdemServicoAnexo</tt> de determinada
	 * <tt>PreFaturaServicoItem</tt>.
	 * 
	 * @param idPreFaturaServicoItem
	 *            id da <tt>PreFaturaServicoItem</tt>
	 * @return quantidade de <tt>OrdemServicoAnexo</tt>
	 */
	public Long findCountByPreFaturaServico(Long idPreFaturaServicoItem) {
		StringBuilder hql = new StringBuilder()
				.append("SELECT COUNT(osa) ")
				.append("FROM OrdemServicoAnexo osa, PreFaturaServico pfs ")
				.append("JOIN osa.ordemServico.ordemServicoItens osi ")
				.append("JOIN pfs.preFaturaServicoItens pfsi ")
				.append("WHERE osi.idOrdemServicoItem = pfsi.ordemServicoItem.idOrdemServicoItem ")
				.append("AND osa.parcelaPreco.idParcelaPreco = osi.parcelaPreco.idParcelaPreco ")
				.append("AND pfsi.idPreFaturaServicoItem = ? ");
		return (Long) getAdsmHibernateTemplate().findUniqueResult(hql.toString(), new Object[] { idPreFaturaServicoItem });
	}

}
