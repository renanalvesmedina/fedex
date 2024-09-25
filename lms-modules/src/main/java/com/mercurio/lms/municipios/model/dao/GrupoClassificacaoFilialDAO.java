package com.mercurio.lms.municipios.model.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.BaseCrudDao;
import com.mercurio.lms.municipios.model.GrupoClassificacaoFilial;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.JTVigenciaUtils;

/**
 * DAO pattern.   
 *
 * Esta classe fornece acesso a camada de dados da aplicação
 * através do suporte ao Hibernate em conjunto com o Spring.
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * @spring.bean 
 */
public class GrupoClassificacaoFilialDAO extends BaseCrudDao<GrupoClassificacaoFilial, Long>
{

	
	protected void initFindPaginatedLazyProperties(Map fetchModes) {
		fetchModes.put("divisaoGrupoClassificacao", FetchMode.JOIN);
		fetchModes.put("divisaoGrupoClassificacao.grupoClassificacao",FetchMode.JOIN);
		fetchModes.put("filial",FetchMode.JOIN);
		fetchModes.put("filial.pessoa",FetchMode.JOIN);
	}
	/**
	 * Nome da classe que o DAO é responsável por persistir.
	 */
    protected final Class getPersistentClass() {
        return GrupoClassificacaoFilial.class;
    }

   protected void initFindByIdLazyProperties(Map fetchModes) {
   		fetchModes.put("divisaoGrupoClassificacao", FetchMode.JOIN);
   		fetchModes.put("divisaoGrupoClassificacao.grupoClassificacao",FetchMode.JOIN);
   		fetchModes.put("filial",FetchMode.JOIN);
   		fetchModes.put("filial.pessoa",FetchMode.JOIN);
   }
   
      
   //uma filial nao pode estar vigente em duas divisoes de um mesmo grupo
   public boolean verificaFilialGrupoClassificacao(GrupoClassificacaoFilial grupoClassificacaoFilial){
	   DetachedCriteria dc = createDetachedCriteria();
	   if(grupoClassificacaoFilial.getIdGrupoClassificacaoFilial() != null){
		   dc.add(Restrictions.ne("idGrupoClassificacaoFilial",grupoClassificacaoFilial.getIdGrupoClassificacaoFilial()));
	   }
	   dc = JTVigenciaUtils.getDetachedVigencia(dc,grupoClassificacaoFilial.getDtVigenciaInicial(),grupoClassificacaoFilial.getDtVigenciaFinal());
	   
	   dc.add(Restrictions.eq("filial.idFilial",grupoClassificacaoFilial.getFilial().getIdFilial()));
	   
	   DetachedCriteria dcDivisaoGrupoClassificacao = dc.createCriteria("divisaoGrupoClassificacao");
	   
	   DetachedCriteria dcGrupoClassificacao = dcDivisaoGrupoClassificacao.createCriteria("grupoClassificacao");
	   
	   dcGrupoClassificacao.add(Restrictions.eq("idGrupoClassificacao",grupoClassificacaoFilial.getDivisaoGrupoClassificacao().getGrupoClassificacao().getIdGrupoClassificacao()));
	   return findByDetachedCriteria(dcGrupoClassificacao).size()>0;
	   
   }
   
   /**
    * 
    * autor Julio Cesar Fernandes Corrêa
    * 24/01/2006
    * @param idFilial
    * @param idGrupoClassificacao
    * @return
    */
	public Long findIdDivisaoByFilialGrupoClassificacao(Long idFilial, Long idGrupoClassificacao) {
		YearMonthDay hoje = JTDateTimeUtils.getDataAtual();
		DetachedCriteria dc = DetachedCriteria.forClass(getPersistentClass(),"gcf")
			.createAlias("gcf.divisaoGrupoClassificacao", "d")
			.createAlias("d.grupoClassificacao", "gc")
			.setProjection(Projections.property("d.id"))
			.add(Restrictions.le("gcf.dtVigenciaInicial", hoje))
			.add(Restrictions.ge("gcf.dtVigenciaFinal", hoje))
			.add(Restrictions.eq("gc.id", idGrupoClassificacao))
			.add(Restrictions.eq("gcf.filial.id", idFilial))
			.add(Restrictions.eq("d.tpSituacao", "A"))
			.add(Restrictions.eq("gc.tpSituacao", "A"));
		List l = findByDetachedCriteria(dc);
		if(!l.isEmpty())
			return (Long)l.get(0);
		return null;
	}
   
}