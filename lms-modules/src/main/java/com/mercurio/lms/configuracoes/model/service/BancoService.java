package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.core.security.model.MethodSecurity;
import com.mercurio.adsm.core.security.model.ServiceSecurity;
import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.lms.configuracoes.model.Banco;
import com.mercurio.lms.configuracoes.model.dao.BancoDAO;

/**
 * @author JoseMR Classe de servi�o para CRUD:
 * 
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este servi�o.
 * @spring.bean id="lms.configuracoes.bancoService"
 */
@ServiceSecurity
public class BancoService extends CrudService<Banco, Long> {

	/**
	 * Recupera uma inst�ncia de <code>Banco</code> a partir do ID.
	 *
	 * @param id representa a entidade que deve ser localizada.
	 * @return Inst�ncia que possui o id informado.
	 */
	public Banco findById(Long id) {
		return (Banco)super.findById(id);
	}

	/**
	 * Apaga uma entidade atrav�s do Id.
	 *
	 * @param id indica a entidade que dever� ser removida.
	 */	
	public void removeById(Long id) {
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
	public java.io.Serializable store(Banco bean) {
		return super.store(bean);
	}
	
    /**
     * Atribui o DAO respons�vel por tratar a persist�ncia dos dados deste servi�o.
     * 
     * @param Inst�ncia do DAO.
     */
	public void setBancoDAO(BancoDAO dao){
		setDao( dao );
	}
	
	/**
     * Retorna o DAO deste servi�o que � respons�vel por tratar a persist�ncia dos dados deste servi�o.
     *
     * @return Inst�ncia do DAO.
     */
	private BancoDAO getBancoDAO(){
		return (BancoDAO) getDao();
	}
	
	public void findNrBanco(Short nrBanco){
		if(!getBancoDAO().findNrBanco(nrBanco))
			throw new BusinessException("LMS-09021");
		
	}
	
	/**
	 * Busca um Banco pelo seu n�mero
	 *
	 * @author Jos� Rodrigo Moraes
	 * @since 06/12/2006
	 *
	 * @param nrBanco N�mero do Banco
	 * @return Banco
	 */
	@MethodSecurity(processGroup = "configuracoes", 
					processName = "bancos.findByNrBanco",
					authenticationRequired=false)
	public Banco findByNrBanco( String nrBanco ){
		return getBancoDAO().findByNrBanco( Short.valueOf( nrBanco ));
	}
	
	@Override
	@MethodSecurity(processGroup = "configuracoes", 
			processName = "bancos.findPaginated",
			authenticationRequired=false)
	public ResultSetPage findPaginated(Map criteria) {
		return super.findPaginated(criteria);
	}

	@Override
	@MethodSecurity(processGroup = "configuracoes", 
			processName = "bancos.find",
			authenticationRequired=false)
	public List find(Map criteria) {
		return super.find(criteria);
	}
	
	/**
	 * Carrega a entidade Banco de acordo com o n�mero e o idPais.
	 * M�todo criado para a integra��o.
	 * 
	 * Hector Julian Esnaola Junior
	 * 13/02/2008
	 *
	 * @param nrBanco
	 * @param idPais
	 *
	 * @return Banco
	 *
	 */
	public Banco findBancoByNrBancoAndIdPais (Short nrBanco, Long idPais) {
		return getBancoDAO().findBancoByNrBancoAndIdPais(nrBanco, idPais);
	}
	
}
