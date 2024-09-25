package com.mercurio.lms.municipios.model.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.configuracoes.model.service.ParametroGeralService;
import com.mercurio.lms.municipios.model.DivisaoGrupoClassificacao;
import com.mercurio.lms.municipios.model.GrupoClassificacao;
import com.mercurio.lms.municipios.model.dao.DivisaoGrupoClassificacaoDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.municipios.divisaoGrupoClassificacaoService"
 */
public class DivisaoGrupoClassificacaoService extends CrudService<DivisaoGrupoClassificacao, Long> {
	private ParametroGeralService parametrosGeraisService;

	public ParametroGeralService getParametrosGeraisService() {
		return parametrosGeraisService;
	}

	public void setParametrosGeraisService(
			ParametroGeralService parametrosGeraisService) {
		this.parametrosGeraisService = parametrosGeraisService;
	}

	protected DivisaoGrupoClassificacao beforeStore(DivisaoGrupoClassificacao bean) {
		DivisaoGrupoClassificacao  divisaoGrupoClassificacao = (DivisaoGrupoClassificacao)bean;
		GrupoClassificacao grupoClassificacao = (GrupoClassificacao)getDivisaoGrupoClassificacaoDAO().getAdsmHibernateTemplate().get(GrupoClassificacao.class, divisaoGrupoClassificacao.getGrupoClassificacao().getIdGrupoClassificacao());
		if (! "A".equals(grupoClassificacao.getTpSituacao().getValue())) {
			throw new BusinessException("LMS-29017");
		}
		getDivisaoGrupoClassificacaoDAO().getAdsmHibernateTemplate().evict(grupoClassificacao);
		divisaoGrupoClassificacao.getGrupoClassificacao().setTpSituacao(null);
		return super.beforeStore(bean);
	}
	
	public List find(Map criteria) {
    	return getDivisaoGrupoClassificacaoDAO().find(criteria);
    }

	/**
	 * Recupera uma instância de <code>DivisaoGrupoClassificacao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws
	 */
    public DivisaoGrupoClassificacao findById(java.lang.Long id) {
        return (DivisaoGrupoClassificacao)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

	/**
	 * Apaga várias entidades através do Id.
	 *
	 * @param ids lista com as entidades que deverão ser removida.
	 *
	 *
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(DivisaoGrupoClassificacao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setDivisaoGrupoClassificacaoDAO(DivisaoGrupoClassificacaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private DivisaoGrupoClassificacaoDAO getDivisaoGrupoClassificacaoDAO() {
        return (DivisaoGrupoClassificacaoDAO) getDao();
    }
    
    /**
     * Método que retorna as divisões de grupo de classificação associadas a um grupo específico
     * @param criteria
     * @return
     */
    public List findByGrupoClassificacao(Map criteria) {
        Long idGrupoClassificacao = Long.valueOf(getParametrosGeraisService().findDsConteudoByNmParametro(GrupoClassificacao.ID_GRUPO_CLASSIFICACAO_DESCONTOS));
        List resp = getDivisaoGrupoClassificacaoDAO().findByIdGrupoClassificacao(idGrupoClassificacao);
    	for (Iterator iter = resp.iterator(); iter.hasNext();) {
			Map aux = (Map) iter.next();
			String dsGrupoClassificacao = ((VarcharI18n) aux.remove("dsGrupoClassificacao")).getValue();
			String dsDivisaoGrupoClassificacao = ((VarcharI18n) aux.remove("dsDivisaoGrupoClassificacao")).getValue();
			aux.put("dsGrupoDivisao",dsGrupoClassificacao + " - " + dsDivisaoGrupoClassificacao);
		}
        return resp;   
    }
	
}