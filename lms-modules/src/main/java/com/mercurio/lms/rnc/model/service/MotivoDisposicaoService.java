package com.mercurio.lms.rnc.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.MotivoDisposicao;
import com.mercurio.lms.rnc.model.dao.MotivoDisposicaoDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.motivoDisposicaoService"
 */
public class MotivoDisposicaoService extends CrudService<MotivoDisposicao, Long> {


	/**
	 * Recupera uma instância de <code>MotivoDisposicao</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 * @throws 
	 */
    public MotivoDisposicao findById(java.lang.Long id) {
        return (MotivoDisposicao)super.findById(id);
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
    public java.io.Serializable store(MotivoDisposicao bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotivoDisposicaoDAO(MotivoDisposicaoDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotivoDisposicaoDAO getMotivoDisposicaoDAO() {
        return (MotivoDisposicaoDAO) getDao();
    }
    
    /**
     * Busca List com os Motivos Disposição relacionados com as 'Ocorrencia Não Conformidade'
     * 
     * @param criteria ids de 'Ocorrencia Não Conformidade'
     * @return List com os Motivos Disposição
     */
    public List findMotivoDisposicaoByOcorrenciaNC(List criteria) {
    	return this.getMotivoDisposicaoDAO().findMotivoDisposicaoByOcorrenciaNC(criteria);
    }
    
    /**
     * Método para retornar uma list ordenada.
     * Utilizado em combobox.
     * @param criteria
     * @return
     */
    public List findOrderByDsMotivo(Map criteria){
        List campoOrdenacao = new ArrayList();
        campoOrdenacao.add("dsMotivo:asc");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    public MotivoDisposicao findMotivoDisposicaoByDsMotivoDisposicao(String dsMotivoDisposicao){
    	return this.getMotivoDisposicaoDAO().findMotivoDisposicaoByDsMotivoDisposicao(dsMotivoDisposicao);
    }
}