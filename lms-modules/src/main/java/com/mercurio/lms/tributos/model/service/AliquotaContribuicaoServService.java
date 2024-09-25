package com.mercurio.lms.tributos.model.service;

import java.util.List;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.fretecarreteiroviagem.model.SimulacaoReajusteRota;
import com.mercurio.lms.tributos.model.AliquotaContribuicaoServ;
import com.mercurio.lms.tributos.model.dao.AliquotaContribuicaoServDAO;

/**
 * Classe de serviço para CRUD:   
 *
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.tributos.aliquotaContribuicaoServService"
 */
public class AliquotaContribuicaoServService extends CrudService<AliquotaContribuicaoServ, Long> {
	
	
	/**
     * Busca as aliquotas contribuicao Servico de acordo com os critérios de pesquisa
     */
	public ResultSetPage findPaginated(TypedFlatMap tfm) {
		return getAliquotaContribuicaoServDAO().findPaginated(tfm,FindDefinition.createFindDefinition(tfm));
	}
	
	 /**
     * Utiliza a mesma query da listagem para contar quantos registros serão exibidos
     * @param tfm Critérios de pesquisa
     * @return Inteiro que representa o número de registros a serem exibidos
     */
	public Integer getRowCount(TypedFlatMap tfm) {
		return getAliquotaContribuicaoServDAO().getRowCount(tfm);
	}
	
	/**
	 * Busca Aliquota vigente de acordo com parametros.<BR>
	 *@author Robson Edemar Gehl
	 * @param idPessoa
	 * @param idServicoAdicional <opcional> pode ser null 
	 * @param idServicoTributo <opcional> pode ser null
	 * @param dtBase
	 * @param tpImposto
	 * @return
	 */
	public AliquotaContribuicaoServ findByUnique(Long idPessoa, Long idServicoAdicional, 
			Long idServicoTributo, YearMonthDay dtBase, String tpImposto){
		return getAliquotaContribuicaoServDAO().findByUnique(idPessoa ,idServicoAdicional, idServicoTributo, dtBase, tpImposto);
	}
	
	
    protected AliquotaContribuicaoServ beforeStore(AliquotaContribuicaoServ bean) {
		if ( this.verificaExisteVigenciaAliquotaContribuicaoServ((AliquotaContribuicaoServ)bean)) {
			throw new BusinessException("LMS-00047");
		}
		return super.beforeStore(bean);
	}

    private boolean verificaExisteVigenciaAliquotaContribuicaoServ(AliquotaContribuicaoServ bean){
    	return this.getAliquotaContribuicaoServDAO().verificaExisteVigencia(bean);
    }
	
    @Override
    protected AliquotaContribuicaoServ beforeUpdate(AliquotaContribuicaoServ bean) {
    	getAliquotaContribuicaoServDAO().deleteFilhos(((AliquotaContribuicaoServ) bean).getIdAliquotaContribuico());
    	return super.beforeUpdate(bean);
    }
	
	/**
	 * Recupera uma instância de <code>AliquotaContribuicaoServ</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
    public AliquotaContribuicaoServ findById(java.lang.Long id) {
        return (AliquotaContribuicaoServ)super.findById(id);
    }

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */
    public void removeById(java.lang.Long id) {
    	getAliquotaContribuicaoServDAO().deleteFilhos(id);
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
    public void removeByIds(List<Long> ids) {
		for (Long id : ids) {
			removeById(id);
    }
    }

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso contrário.
	 *
	 * @param bean entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
    public java.io.Serializable store(AliquotaContribuicaoServ bean) {
        return super.store(bean);
    }

    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
    public void setAliquotaContribuicaoServDAO(AliquotaContribuicaoServDAO dao) {
        setDao( dao );
    }
    
    /**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
    private AliquotaContribuicaoServDAO getAliquotaContribuicaoServDAO() {
        return (AliquotaContribuicaoServDAO) getDao();
    }
    
    /**
     * Busca quantos registros existem em aliquotaContribuicaoServ de acordo com os critérios passados por parâmetro
     * @param tpImposto Tipo de imposto (ISS, INSS, etc)
     * @param idServicoAdicional Identificador do Serviço Adicional
     * @param idServicoTributo Identificador do Serviço Tributo
     * @param idCliente Identificador do Cliente
     * @param dtBase Data Base
     * @return Quantidade de registro de AliquotaContribuicaoServ resultantes da pesquisa
     */
    public AliquotaContribuicaoServ findAliquotaContribuicaoServ(String tpImposto, Long idServicoAdicional, Long idServicoTributo, Long idCliente, YearMonthDay dtBase, Long idMunicipioIncidencia){        
        return this.getAliquotaContribuicaoServDAO().findAliquotaContribuicaoServ(tpImposto, idServicoAdicional, idServicoTributo, idCliente, dtBase, idMunicipioIncidencia);
    }
   }