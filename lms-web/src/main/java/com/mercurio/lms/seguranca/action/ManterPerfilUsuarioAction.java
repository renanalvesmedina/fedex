package com.mercurio.lms.seguranca.action;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.pojo.Perfil;
import com.mercurio.adsm.framework.model.pojo.PerfilUsuario;
import com.mercurio.adsm.framework.model.pojo.UsuarioADSM;
import com.mercurio.adsm.framework.model.service.PerfilService;
import com.mercurio.adsm.framework.model.service.PerfilUsuarioService;
import com.mercurio.adsm.framework.model.service.UsuarioADSMService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.seguranca.manterPerfilUsuarioAction"
 */
public class ManterPerfilUsuarioAction extends CrudAction {

	/**Variavel para que se use a UsuarioService*/
	private UsuarioService usuarioService;
	
	/** Variavel para que se use a PerfilService*/
	private PerfilService perfilService;
	
	private UsuarioADSMService usuarioADSMService;	
	
	/** Metodo responsavel por montar a loockup de usuarios
	 * @param criteria
	 * @return List com os usuarios
	 */
	public List findLookupUsuario(TypedFlatMap criteria){		
		return this.usuarioService.findLookup(criteria);	
	}
	
	
	/** Metodo responsavel por montar a loockup de perfil
	 * @param criteria
	 * @return List com os perfis
	 */
	public List findLookupPerfil(TypedFlatMap criteria){
		return this.perfilService.findLookup(criteria);	
	}
	
	
	public List findLookupUsuarioAdsm(TypedFlatMap criteria) {
		
		List result = this.usuarioADSMService.findLookup(criteria);		
		
		ArrayList newList = null;		
		if (result!=null && result.size()>0)
		{
			
			ListIterator lit = result.listIterator();
			newList = new ArrayList();
			while(lit.hasNext())
			{
				UsuarioADSM usuarioAdsm = (UsuarioADSM)lit.next(); 
				HashMap newMap = new HashMap();
				newMap.put("login",usuarioAdsm.getLogin());
				newMap.put("idUsuario",usuarioAdsm.getIdUsuario());

				newList.add(newMap);				
			}
		}
		return newList;

	}


	/** Metodo responsavel por executar a consulta de acordo com os parametros da tela 	
	 * @param criteria
	 * @return 
	 */
	public ResultSetPage findPaginated(TypedFlatMap criteria) {		
		criteria.put("user.idUsuario",criteria.getLong("hidUsuarioADSM"));		
		return getPerfilUsuarioService().findPaginated(criteria);
	}
	
	/** Necessario reescrever quando se implementa findPaginated
	 * Retorna o numero de linhas de acordo com a consulta realizada com os parametros da tela
	 * @param criteria
	 * @return numeros de linhas
	 */
	public Integer getRowCount(TypedFlatMap criteria) {
		return getPerfilUsuarioService().getRowCount(criteria);
	}

	/**
	 * 
	 * @param idUsuario
	 * @return
	 */
	public List findByIdUsuarioPerfilUsuario( Long idUsuario ){
		return getPerfilUsuarioService().findByIdUsuarioPerfilUsuario( idUsuario );
	}
	
	/**Metodo responsavel por gravar o registro, � necessario se fazer um mapeamento
	 * 
	 * @param mapBean
	 * @return
	 */
	public Serializable store(TypedFlatMap mapBean) {
		
		PerfilUsuario perfilUsuario = new PerfilUsuario();
		
		perfilUsuario.setIdPerfilUsuario(mapBean.getLong("idPerfilUsuario"));
		
		Perfil perfil = new Perfil();
		perfil.setIdPerfil( mapBean.getLong("perfil.idPerfil") );
		
		Usuario usuario = new Usuario();
		usuario.setIdUsuario( mapBean.getLong( "usuario.idUsuario" ) );
		/**
		 * @author Micka�l Jalbert
		 * @since 12/06/2006
		 * 
		 * Modifica��o tempor�ria at� a arquitetura determinar uma solu��o para usar a tabela 
		 * perfil_usuario com o usu�rio LMS
		 * 
		 * 
		 * */
		
		//TODO Ver com arquitetura (Julio)
		perfilUsuario.setUsuario( usuario );
		perfilUsuario.setPerfil( perfil );

		return  getPerfilUsuarioService().store( perfilUsuario );
	}

	
	/**Metodo responsavel por remover um item de acordo com o id passado por parametro. Utilizado na tela de cad
	 * @param id
	 */
	public void removeById(java.lang.Long id) {
		getPerfilUsuarioService().removeById(id);
	}

	
	/** Metodo responsavel por remover uma colecao de itens de acordo com os ids passados. Utilizado na tela de list
	 *	  
	 */
	@ParametrizedAttribute(type = java.lang.Long.class)
	public void removeByIds(List ids) {
		getPerfilUsuarioService().removeByIds(ids);
	}

	
	/**Metodo responsavel por procurar um registro de acordo com o id. Utilizado na transicao da tela de list para
	 * cad.
	 * @param id
	 * @return
	 */
	public Map findById(Long id) {			
		return getPerfilUsuarioService().findByIdMap(id);
	}	


	//Get e Set para os servi�os utilizados ############
	

	/** Setando a  defaultService para PerfilUsuarioService
	 * @param perfilUsuarioService */
	public void setPerfilUsuarioService(PerfilUsuarioService perfilUsuarioService){
		this.defaultService = perfilUsuarioService;
	}

	/** Retornando a PerfilUsuarioService que esta na defaultService
	 * @return PerfilUsuarioService */
	public PerfilUsuarioService getPerfilUsuarioService(){
		return ( ( PerfilUsuarioService ) defaultService );
	}
	
	
	public void setPerfilService(PerfilService perfilService) {
		this.perfilService = perfilService;
	}
	
	public PerfilService getPerfilService() {
		return perfilService;
	}

	
	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
	
	public UsuarioService getUsuarioService() {
		return usuarioService;
	}
	
	public UsuarioADSMService getUsuarioADSMService() {
		return usuarioADSMService;
	}

	public void setUsuarioADSMService(UsuarioADSMService usuarioADSMService) {
		this.usuarioADSMService = usuarioADSMService;
	}
}