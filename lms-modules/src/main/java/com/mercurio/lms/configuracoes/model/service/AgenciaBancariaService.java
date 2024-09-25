package com.mercurio.lms.configuracoes.model.service;

import java.util.List;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.lms.configuracoes.model.AgenciaBancaria;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.dao.AgenciaBancariaDAO;

/**
 * @author JoseMR Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * @spring.bean id="lms.configuracoes.agenciaBancariaService"
 */
public class AgenciaBancariaService extends CrudService {
	
	
	private BancoService bancoService;
	
	/**
	 * Recupera uma instância de <code>AgenciaBancaria</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public AgenciaBancaria findById(Long id) {
		return (AgenciaBancaria)super.findById(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 *
	 * @param id indica a entidade que deverá ser removida.
	 */	
	public void removeById(Long id) {
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
	public java.io.Serializable store(AgenciaBancaria bean) {
		
		validaDigitoAgencia(bean);
		
		return super.store(bean);
	}
	
	private void validaDigitoAgencia(AgenciaBancaria bean) {
		
		
		if(bean.getNrDigito() == null || bean.getNrDigito().isEmpty()){
			Short nrBancoBrasil = Short.valueOf("1");
			Short nrBancoBradesco = Short.valueOf("237");
			
			Banco banco = bancoService.findById(bean.getBanco().getIdBanco());
			
			if(nrBancoBrasil.equals(banco.getNrBanco()) || nrBancoBradesco.equals(banco.getNrBanco())){
				throw new BusinessException("LMS-27128");
			}
		}
	}

	public void beforeRemoveById(Long id) {		
		super.beforeRemoveById(id);
	}
	
    /**
     * Atribui o DAO responsável por tratar a persistência dos dados deste serviço.
     * 
     * @param Instância do DAO.
     */
	public void setAgenciaBancariaDAO(AgenciaBancariaDAO dao){
		setDao( dao );
	}
	
	/**
     * Retorna o DAO deste serviço que é responsável por tratar a persistência dos dados deste serviço.
     *
     * @return Instância do DAO.
     */
	private AgenciaBancariaDAO getAgenciaBancariaDAO(){
		return (AgenciaBancariaDAO) getDao();
	}
	
	public void findNrAgencia(Short nrBanco, Short nrAgenciaBancaria){
		if(!getAgenciaBancariaDAO().findNrAgenciaBancaria(nrBanco,nrAgenciaBancaria))
		    throw new BusinessException("LMS-09022");	
		
	}
	
	/**
	 * Busca uma agência Bancária de acordo com os parâmetros
	 *
	 * @author José Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param idBanco Identificador do Banco
	 * @param nrDigito Número do dígito da Agência Bancária
	 * @param nrAgenciaBancaria Número da Agência Bancária
	 * @return AgenciaBancaria
	 */
	public AgenciaBancaria findAgenciaBancaria(
			Long idBanco, 
			String nrDigito,
			Short nrAgenciaBancaria) {
		
		return getAgenciaBancariaDAO().findAgenciaBancaria(
				idBanco, 
				nrDigito,
				nrAgenciaBancaria);
	}

	/**
	 * Busca uma agência Bancária de acordo com os parâmetros
	 *
	 * @author José Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param idBanco Identificador do Banco
	 * @param nrDigito Número do dígito da Agência Bancária
	 * @param nrAgenciaBancaria Número da Agência Bancária
	 * @return AgenciaBancaria
	 */
	public AgenciaBancaria findAgencia(
			Long idBanco, 
			Short nrAgenciaBancaria) {
		
		return getAgenciaBancariaDAO().findAgencia(
				idBanco, 
				nrAgenciaBancaria);
}

	public BancoService getBancoService() {
		return bancoService;
}

	public void setBancoService(BancoService bancoService) {
		this.bancoService = bancoService;
	}

}
