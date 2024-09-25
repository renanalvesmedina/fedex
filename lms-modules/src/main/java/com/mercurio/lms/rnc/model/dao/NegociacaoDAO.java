package com.mercurio.lms.rnc.model.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.lms.rnc.model.Negociacao;
import com.mercurio.lms.rnc.model.OcorrenciaNaoConformidade;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class NegociacaoDAO extends BaseCrudDao<Negociacao, Long>
{

	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return Negociacao.class;
    }

	protected void initFindByIdLazyProperties(Map map) {
		map.put("usuario",FetchMode.JOIN);
		map.put("filial",FetchMode.JOIN);
		map.put("filial.pessoa",FetchMode.JOIN);
		map.put("ocorrenciaNaoConformidade",FetchMode.JOIN);
		map.put("ocorrenciaNaoConformidade.motivoAberturaNc",FetchMode.JOIN);
		map.put("ocorrenciaNaoConformidade.filialByIdFilialLegado",FetchMode.JOIN);
		map.put("ocorrenciaNaoConformidade.naoConformidade",FetchMode.JOIN);
		map.put("ocorrenciaNaoConformidade.naoConformidade.filial",FetchMode.JOIN);
	} 

	/**
	 * Busca as negociações feitas por o id de um usuario
	 * Deve receber como parametro o id do usuário no map e ids das 
	 * negociações que estão sendo alteradas
	 * 
	 * @param map com os criterios 
	 * @return list com as negocições feitas pelo usuário do id informado
	 */
	public List findByUsuario(Map map){

        Long idUsuario = (Long) map.get("idUsuario");
        List idsNegociacao = (ArrayList) map.get("idsNegociacao");

        DetachedCriteria dc = DetachedCriteria.forClass(Negociacao.class)
        	.setFetchMode("usuario.idUsuario", FetchMode.JOIN)
        	.add(Restrictions.and(Restrictions.in("idNegociacao", idsNegociacao), Restrictions.eq("usuario.idUsuario", idUsuario)));

        return super.findByDetachedCriteria(dc);

	}
	
	/**
	 * find paginated personalizado
	 * @param idNaoConformidade
	 * @param idOcorrenciaNaoConformidade
	 * @param fd
	 * @return
	 */
	public ResultSetPage findPaginatedCustom(Long idNaoConformidade, Long idOcorrenciaNaoConformidade, FindDefinition fd) {
		StringBuffer sql = new StringBuffer()
		.append("select new map(")
		.append("n.idNegociacao as idNegociacao, ")
		.append("onc.nrOcorrenciaNc as nrOcorrenciaNc, ")
		.append("manc.dsMotivoAbertura as dsMotivoAbertura, ")
		.append("onc.nrRncLegado as nrRncLegado, ")
		.append("filialLegado.sgFilial as sgFilialLegado, ")
		.append("n.dsNegociacao as dsNegociacao, ")
		.append("u.nmUsuario as nmUsuario, ")
		.append("n.dhNegociacao as dhNegociacao) ");
		
		List param = new ArrayList();

		sql.append(addCustomCriteria(idNaoConformidade, idOcorrenciaNaoConformidade, param));
		sql.append("order by onc.nrOcorrenciaNc asc, "+PropertyVarcharI18nProjection.createProjection("manc.dsMotivoAbertura")+" asc, n.dhNegociacao.value desc ");
		return getAdsmHibernateTemplate().findPaginated(sql.toString(), fd.getCurrentPage(), fd.getPageSize(), param.toArray());
	}


	/**
	 * @param idNaoConformidade
	 * @param idOcorrenciaNaoConformidade
	 * @param dc
	 */
	private StringBuffer addCustomCriteria(Long idNaoConformidade, Long idOcorrenciaNaoConformidade, List param) {
		StringBuffer sql = new StringBuffer()
		.append("from ")
		.append(Negociacao.class.getName()).append(" as n ")
		.append("inner join n.usuario as u ")
		.append("inner join n.ocorrenciaNaoConformidade as onc ")
		.append("inner join onc.motivoAberturaNc as manc ")
		.append("inner join onc.filialByIdFilialLegado as filialLegado ")
		.append("where 1=1 ");

		if (idNaoConformidade != null) {
			sql.append("and onc.naoConformidade.id = ? ");
			param.add(idNaoConformidade);
		}
		if (idOcorrenciaNaoConformidade != null) {
			sql.append("and onc.id = ? ");
			param.add(idOcorrenciaNaoConformidade);
		}
		return sql;
	}
	
	/**
	 * row count personalizado 
	 * @param idNaoConformidade
	 * @param idOcorrenciaNaoConformidade
	 * @return
	 */
	public Integer getRowCountCustom(Long idNaoConformidade, Long idOcorrenciaNaoConformidade) {
		List param = new ArrayList();
		StringBuffer sql = addCustomCriteria(idNaoConformidade, idOcorrenciaNaoConformidade, param);
        return getAdsmHibernateTemplate().getRowCountForQuery(sql.toString(), param.toArray());
	}
	
	/**
	 * Obtém a quantidade de negociações por documento de servico
	 * @param idNaoConformidade
	 * @return
	 */
	public Integer getRowCountNegociacoesByIdNaoConformidade(Long idNaoConformidade) {
		StringBuffer sb = new StringBuffer()
		.append("from "+OcorrenciaNaoConformidade.class.getName()+" onc ")
		.append("join onc.naoConformidade nc ")
		.append("join onc.negociacoes ")
		.append("where nc.id = ?");		
		return getAdsmHibernateTemplate().getRowCountForQuery(sb.toString(), new Object[]{idNaoConformidade});
	}
}