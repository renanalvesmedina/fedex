package com.mercurio.lms.rnc.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.rnc.model.CausaNaoConformidade;
import com.mercurio.lms.rnc.model.dao.CausaNaoConformidadeDAO;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;

/**
 * Classe de servi�o para CRUD:   
 *
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.rnc.causaNaoConformidadeService"
 */
public class CausaNaoConformidadeService extends CrudService<CausaNaoConformidade, Long> {


	/**
	 * Recupera uma inst�ncia de <code>CausaNaoConformidade</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 * @throws 
	 */
    public CausaNaoConformidade findById(java.lang.Long id) {
        return (CausaNaoConformidade)super.findById(id);
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
    public java.io.Serializable store(CausaNaoConformidade bean) {
        return super.store(bean);
    }

    /**
     * Busca List com os Motivos Disposi��o relacionados com as 'Ocorrencia N�o Conformidade'
     * 
     * @param criteria List de ids de 'Ocorrencia N�o Conformidade'
     * @return List com os Motivos Disposi��o
     */
	public List findCausasNaoConformidadeByOcorrenciaNC(List criteria) {
		return this.getCausaNaoConformidadeDAO().findCausasNaoConformidadeByOcorrenciaNC(criteria);
	}
    
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
    public void setCausaNaoConformidadeDAO(CausaNaoConformidadeDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
    private CausaNaoConformidadeDAO getCausaNaoConformidadeDAO() {
        return (CausaNaoConformidadeDAO) getDao();
    }
    
    /**
     * Localiza uma lista de resultados a partir dos crit�rios de busca 
     * informados. Permite especificar regras de ordena��o.
     * 
     * @param criterions Crit�rios de busca.
     * @param lista com criterios de ordena��o. Deve ser uma java.lang.String no formato
     * 	<code>nomePropriedade:asc</code> ou <code>associacao_.nomePropriedade:desc</code>.
     * A utiliza��o de <code>asc</code> ou <code>desc</code> � opcional sendo o padr�o <code>asc</code>.
     * @return Lista de resultados sem pagina��o.
     */ 
    public List findListByCriteria(Map criteria, List campoOrdenacao) {
    	return getCausaNaoConformidadeDAO().findListByCriteria(criteria, campoOrdenacao);
    }
    
	/**
	 * Solicita��o CQPRO00005285 da integra��o.
	 * M�todo que retorna uma instancia da classe CausaNaoConformidadeconforme de acordo com a descri��o passada por par�metro.
	 * @param dsCausaNaoConformidade
	 * @return
	 */
	public CausaNaoConformidade findByDsCausaNaoConformidade(String dsCausaNaoConformidade){
		return getCausaNaoConformidadeDAO().findByDsCausaNaoConformidade(dsCausaNaoConformidade);
	}

}