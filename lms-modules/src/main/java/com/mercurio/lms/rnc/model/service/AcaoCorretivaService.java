package com.mercurio.lms.rnc.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.AcaoCorretiva;
import com.mercurio.lms.rnc.model.dao.AcaoCorretivaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.rnc.acaoCorretivaService"
 */
public class AcaoCorretivaService extends CrudService<AcaoCorretiva, Long> {

    /**
     * Recupera uma instância de <code>AcaoCorretiva</code> a partir do ID.
     * 
     * @param id
     *            representa a entidade que deve ser localizada.
     * @return Instância que possui o id informado.
     * @throws 
     */
    public AcaoCorretiva findById(java.lang.Long id) {
        return (AcaoCorretiva) super.findById(id);
    }
    
    /**
     * Retorna uma list ordenada pelo campo dsAcaoCorretiva
     * 
     * @param criteria
     * @return list ordenada
     */
    public List findOrderByAcaoCorretiva(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsAcaoCorretiva:asc");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
     * Busca List com as Ações Corretivas relacionados com as 'Ocorrencia Não Conformidade'
     * 
     * @param criteria ids de 'Ocorrencia Não Conformidade'
     * @return List com os Ações Corretivas
     */
	public List findAcoesCorretivasByOcorrenciaNC(List criteria) {
		return this.getAcaoCorretivaDAO().findAcoesCorretivasByOcorrenciaNC(criteria);
	}

    /**
     * Busca List com as Ações Corretivas relacionados com uma 'Ocorrencia Não Conformidade'
     * 
     * @param criteria id da 'Ocorrencia Não Conformidade'
     * @return List com os Ações Corretivas
     */
	public List findAcoesCorretivasByIdOcorrenciaNC(Long idOcorrenciaNC) {
		return this.getAcaoCorretivaDAO().findAcoesCorretivasByIdOcorrenciaNC(idOcorrenciaNC);
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

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
     * contrário.
     * 
     * @param bean
     *            entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(AcaoCorretiva bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste
     * serviço.
     * 
     * @param Instância
     *            do DAO.
     */
    public void setAcaoCorretivaDAO(AcaoCorretivaDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência
     * dos dados deste serviço.
     * 
     * @return Instância do DAO.
     */
    private AcaoCorretivaDAO getAcaoCorretivaDAO() {
        return (AcaoCorretivaDAO) getDao();
    }
}