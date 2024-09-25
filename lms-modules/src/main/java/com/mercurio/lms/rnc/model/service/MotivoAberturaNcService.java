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
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.motivoAberturaNcService"
 */
public class MotivoAberturaNcService extends CrudService<MotivoAberturaNc, Long> {
	
	/**
	 * Recupera uma inst�ncia de <code>MotivoAberturaNc</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
    public MotivoAberturaNc findById(java.lang.Long id) {
        return (MotivoAberturaNc)super.findById(id);
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
    public java.io.Serializable store(MotivoAberturaNc bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setMotivoAberturaNcDAO(MotivoAberturaNcDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
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
     * M�todo para retornar uma list ordenada.
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
     * Obt�m a quantidade de Motivos de Abertura de Nao Conformidadde de acordo com o idNaoConformidade e o atributo blPermissao
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