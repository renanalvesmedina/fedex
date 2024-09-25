package com.mercurio.lms.rnc.model.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.adsm.framework.model.hibernate.PropertyVarcharI18nProjection;
import com.mercurio.adsm.framework.util.SqlTemplate;
import com.mercurio.lms.rnc.model.MotivoDisposicao;
import com.mercurio.lms.util.AliasToNestedBeanResultTransformer;

/**
 * DAO pattern.
 * 
 * Esta classe fornece acesso a camada de dados da aplicação através do suporte
 * ao Hibernate em conjunto com o Spring. Não inserir documentação após ou
 * remover a tag do XDoclet a seguir.
 * 
 * @spring.bean
 */
public class MotivoDisposicaoDAO extends BaseCrudDao<MotivoDisposicao, Long> {

    /**
     * Nome da classe que o DAO é responsável por persistir.
     */
    protected final Class getPersistentClass() {
        return MotivoDisposicao.class;
    }
    
	/**
	 * Verifica se um registro já foi cadastrado com a mesma descrição. Retornando <bold>true</bold> se já existe um registro.
	 * @param motivoDisposicao
	 * @return Boolean
	 */
	public Boolean verificaExistenciaRegistro(MotivoDisposicao motivoDisposicao){
		DetachedCriteria dc = createDetachedCriteria();
		dc.add(
			Restrictions.and(
				Restrictions.ne("idMotivoDisposicao", motivoDisposicao.getIdMotivoDisposicao()),
				Restrictions.eq("dsMotivo", motivoDisposicao.getDsMotivo())
			));

		return Boolean.valueOf( !findByDetachedCriteria(dc).isEmpty());
	}
	
	/**
     * Busca List com os Motivos Disposição relacionados com as 'Ocorrencia Não Conformidade'
     * Tem como default a busca de motivos disposicao que tenham o parametro 'somente automatico'
     * igual a false
     * 
     * @param criteria ids de 'Ocorrencia Não Conformidade'
     * @return List com os Motivos Disposição
     */
	public List findMotivoDisposicaoByOcorrenciaNC(List idsOcorrenciaNC) {

		String ids = new String();
		for (Iterator iter = idsOcorrenciaNC.iterator(); iter.hasNext();) {
			ids = ids + ((Long)iter.next()).toString();
			if (iter.hasNext()) ids = ids + ", ";
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select new map( ");
		sql.append("md.id as idMotivoDisposicao, ");
		sql.append(""+PropertyVarcharI18nProjection.createProjection("md.dsMotivo")+" as dsMotivo )");
		sql.append("from MotivoDisposicao md, ");
		sql.append("MotAberturaMotDisposicao mamd, ");
		sql.append("MotivoAberturaNc manc, ");
		sql.append("OcorrenciaNaoConformidade onc ");
		sql.append("where mamd.motivoDisposicao = md ");
		sql.append("and mamd.motivoAberturaNc = manc ");
		sql.append("and onc.motivoAberturaNc = manc ");
		sql.append("and md.blSomenteAutomatico = 'N' ");
		sql.append("and onc.id in (" + ids + ") ");
		sql.append("order by "+PropertyVarcharI18nProjection.createProjection("md.dsMotivo")+"");
		List list = getAdsmHibernateTemplate().find(sql.toString());
		list = new AliasToNestedBeanResultTransformer(MotivoDisposicao.class).transformListResult(list);
		return list;
	}
	
	/**
	 * Busca Motivo Disposicao conforme sua descrição
	 * @param dsMotivoDisposicao
	 * @return
	 */
	public MotivoDisposicao findMotivoDisposicaoByDsMotivoDisposicao(String dsMotivoDisposicao){
		SqlTemplate hql = new SqlTemplate();
		
		hql.addProjection("md");		
		hql.addFrom(MotivoDisposicao.class.getName() + " md ");		
		hql.addCriteria("md.dsMotivo", "like", "%".concat(dsMotivoDisposicao).concat("%"));
		
		return (MotivoDisposicao) getAdsmHibernateTemplate().findUniqueResult(hql.getSql(), hql.getCriteria());		
	}
}