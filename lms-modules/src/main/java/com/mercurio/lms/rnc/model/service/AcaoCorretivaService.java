package com.mercurio.lms.rnc.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.AcaoCorretiva;
import com.mercurio.lms.rnc.model.dao.AcaoCorretivaDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * 
 * @spring.bean id="lms.rnc.acaoCorretivaService"
 */
public class AcaoCorretivaService extends CrudService<AcaoCorretiva, Long> {

    /**
     * Recupera uma inst�ncia de <code>AcaoCorretiva</code> a partir do ID.
     * 
     * @param id
     *            representa a entidade que deve ser localizada.
     * @return Inst�ncia que possui o id informado.
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
     * Busca List com as A��es Corretivas relacionados com as 'Ocorrencia N�o Conformidade'
     * 
     * @param criteria ids de 'Ocorrencia N�o Conformidade'
     * @return List com os A��es Corretivas
     */
	public List findAcoesCorretivasByOcorrenciaNC(List criteria) {
		return this.getAcaoCorretivaDAO().findAcoesCorretivasByOcorrenciaNC(criteria);
	}

    /**
     * Busca List com as A��es Corretivas relacionados com uma 'Ocorrencia N�o Conformidade'
     * 
     * @param criteria id da 'Ocorrencia N�o Conformidade'
     * @return List com os A��es Corretivas
     */
	public List findAcoesCorretivasByIdOcorrenciaNC(Long idOcorrenciaNC) {
		return this.getAcaoCorretivaDAO().findAcoesCorretivasByIdOcorrenciaNC(idOcorrenciaNC);
	}	
	
    /**
     * Apaga uma entidade atrav�s do Id.
     * 
     * @param id
     *            indica a entidade que dever� ser removida.
     */
    public void removeById(java.lang.Long id) {
        super.removeById(id);
    }

    /**
     * Apaga v�rias entidades atrav�s do Id.
     * 
     * @param ids
     *            lista com as entidades que dever�o ser removida.
     * 
     *
     */
	@ParametrizedAttribute(type = java.lang.Long.class)
    public void removeByIds(List ids) {
        super.removeByIds(ids);
    }

    /**
     * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
     * contr�rio.
     * 
     * @param bean
     *            entidade a ser armazenada.
     * @return entidade que foi armazenada.
     */
    public java.io.Serializable store(AcaoCorretiva bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste
     * servi�o.
     * 
     * @param Inst�ncia
     *            do DAO.
     */
    public void setAcaoCorretivaDAO(AcaoCorretivaDAO dao) {
        setDao( dao );
    }

    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia
     * dos dados deste servi�o.
     * 
     * @return Inst�ncia do DAO.
     */
    private AcaoCorretivaDAO getAcaoCorretivaDAO() {
        return (AcaoCorretivaDAO) getDao();
    }
}