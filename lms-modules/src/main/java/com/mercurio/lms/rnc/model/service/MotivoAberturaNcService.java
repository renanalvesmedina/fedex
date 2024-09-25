package com.mercurio.lms.rnc.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.Setor;
import com.mercurio.lms.rnc.model.MotivoAberturaNc;
import com.mercurio.lms.rnc.model.dao.MotivoAberturaNcDAO;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.rnc.motivoAberturaNcService"
 */
public class MotivoAberturaNcService extends CrudService<MotivoAberturaNc, Long> {
	
	/**
	 * Recupera uma instância de <code>MotivoAberturaNc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public MotivoAberturaNc findById(java.lang.Long id) {
        return (MotivoAberturaNc)super.findById(id);
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
    public java.io.Serializable store(MotivoAberturaNc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setMotivoAberturaNcDAO(MotivoAberturaNcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private MotivoAberturaNcDAO getMotivoAberturaNcDAO() {
        return (MotivoAberturaNcDAO) getDao();
    }
    

    /**
     * Localiza uma lista MotivoAberturaNc de um determinado Setor.
     *  
     * @return Lista de resultados
     */
    public List findMotivoAberturaNcBySetor() {
    	Setor setorUsuario = SessionUtils.getSetor();
    	return getMotivoAberturaNcDAO().findMotivoAberturaNcBySetor(setorUsuario.getIdSetor());
    }
    
    /**
     * Método para retornar uma list ordenada.
     * Utilizado em combobox.
     * @param criteria
     * @return
     */
    public List findOrderByDsMotivoAbertura(Map criteria){
        List campoOrdenacao = new ArrayList(1);
        campoOrdenacao.add("dsMotivoAbertura");
        return getDao().findListByCriteria(criteria, campoOrdenacao);
    }
    
    /**
     * Obtém a quantidade de Motivos de Abertura de Nao Conformidadde de acordo com o idNaoConformidade e o atributo blPermissao
     * @param idNaoConformidade
     * @param blPermissao
     * @return
     */
    public Integer getRowCountMotivosAberturaByIdNaoConformidadeBlPermissao(Long idNaoConformidade, boolean blPermissao) {
    	return getMotivoAberturaNcDAO().getRowCountMotivosAberturaByIdNaoConformidadeBlPermissao(idNaoConformidade, Boolean.valueOf(blPermissao));
    }
    
	public Boolean findDoctoServicoMCCT(long criteria){
		List lista = getMotivoAberturaNcDAO().findNrChavePorNr(criteria);
		return !getMotivoAberturaNcDAO().findDoctoServicoMCCT(criteria).isEmpty() && (lista != null && !lista.isEmpty()); 
    }
	
	public List findNrChave(long criteria){
		return getMotivoAberturaNcDAO().findNrChavePorId(criteria);
	}
	
	public MotivoAberturaNc findMotivoAberturaNcByIdDoctoServico(Long idDoctoServico) {
		return getMotivoAberturaNcDAO().findMotivoAberturaNcByIdDoctoServico(idDoctoServico);
	}
}