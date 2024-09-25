package com.mercurio.lms.configuracoes.model.service;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudService;
import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.EmpresaUsuario;
import com.mercurio.lms.configuracoes.model.FilialUsuario;
import com.mercurio.lms.configuracoes.model.RegionalUsuario;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.UsuarioLMS;
import com.mercurio.lms.configuracoes.model.dao.EmpresaUsuarioDAO;
import com.mercurio.lms.municipios.model.Empresa;

/**
 * Classe de serviço para CRUD:
 * 
 * 
 * Não inserir documentação após ou remover a tag do XDoclet a seguir. O valor
 * do <code>id</code> informado abaixo deve ser utilizado para referenciar
 * este serviço.
 * 
 * @spring.bean id="lms.configuracoes.empresaUsuarioService"
 */
public class EmpresaUsuarioService extends CrudService<EmpresaUsuario, Long> {

	private RegionalUsuarioService regionalUsuarioService;

	private FilialUsuarioService filialUsuarioService;

	private EmpresaUsuarioService empresaUsuarioService;

	public void setFilialUsuarioService(
			FilialUsuarioService filialUsuarioService) {
		this.filialUsuarioService = filialUsuarioService;
	}

	public void setEmpresaUsuarioService(
			EmpresaUsuarioService empresaUsuarioService) {
		this.empresaUsuarioService = empresaUsuarioService;
	}

	public void setRegionalUsuarioService(
			RegionalUsuarioService regionalUsuarioService) {
		this.regionalUsuarioService = regionalUsuarioService;
	}

	/**
	 * Recupera uma instância de <code>Autoridade</code> a partir do ID.
	 * 
	 * @param id
	 *            representa a entidade que deve ser localizada.
	 * @return Instância que possui o id informado.
	 */
	public EmpresaUsuario findById(java.lang.Long id) {
		return (EmpresaUsuario) getEmpresaUsuarioDAO().findById(id);
	}

	public List findByIdUsuario(java.lang.Long id) {
		return getEmpresaUsuarioDAO().findByIdUsuario(id);
	}

	public int findEmpresaPadrao( Long id ){
		Empresa empresaP = findByIdUsuarioEmpresaPadrao(id );
		if( empresaP != null ){
			Usuario usuario = new Usuario();
			usuario.setIdUsuario( id );
			EmpresaUsuario empresaU = findByEmpresaUsuario( empresaP, usuario );
			if( empresaU != null && empresaU.getIdEmpresaUsuario() != null ){
				return empresaU.getIdEmpresaUsuario().intValue();
			}
		}
		return 0;
	}
	
	/**
	 * Busca Empresa Padrão através de um UsuarioLMS.
	 * 
	 * @param id
	 * @return
	 */
	public Empresa findByIdUsuarioEmpresaPadrao(java.lang.Long id) {
		return getEmpresaUsuarioDAO().findByIdUsuarioEmpresaPadrao(id);
	}

	/**
	 * Apaga uma entidade através do Id.
	 * 
	 * @param id
	 *            indica a entidade que deverá ser removida.
	 */
	public void removeById(java.lang.Long id) {

		EmpresaUsuario empresaUsuario = empresaUsuarioService.findById( id );
		
		if( empresaUsuario != null ){
			/* Remove todas as regionais do usuario ligado a uma empresa. */
			regionalUsuarioService.removeByIdEmpresaUsuario( empresaUsuario.getIdEmpresaUsuario() );
	
			/* Remove todas as Filiais do Usuario ligado a uma empresa. */
			filialUsuarioService.removeByIdEmpresaUsuario( empresaUsuario.getIdEmpresaUsuario() );
		}

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

		for (Iterator it = ids.iterator(); it.hasNext();) {
			EmpresaUsuario empresaUsuario = empresaUsuarioService.findById((Long) it.next());
			
			if( empresaUsuario != null ){
				if( empresaUsuario.getUsuario() != null ){
					/* Remove todas as regionais do usuario. */
					regionalUsuarioService.removeByIdEmpresaUsuario(empresaUsuario.getIdEmpresaUsuario() );
	
					/* Remove todas as Filiais do Usuario. */
					filialUsuarioService.removeByIdEmpresaUsuario(empresaUsuario.getIdEmpresaUsuario() );
				}
			}

		}

		super.removeByIds(ids);
	}

	/**
	 * Insere, caso o id seja <code>null</code> ou atualiza uma entidade, caso
	 * contrário.
	 * 
	 * @param empresaUsuario
	 *            entidade a ser armazenada.
	 * @return entidade que foi armazenada.
	 */
	public java.io.Serializable store(EmpresaUsuario empresaUsuario, boolean isEmpresaPadrao) {
		if (empresaUsuario.getIdEmpresaUsuario() != null) {
			if( empresaUsuario.getBlIrrestritoFilial() != null && empresaUsuario.getBlIrrestritoFilial().equals(true) ){
				regionalUsuarioService.removeByIdEmpresaUsuario( empresaUsuario.getIdEmpresaUsuario() );
				filialUsuarioService.removeByIdEmpresaUsuario( empresaUsuario.getIdEmpresaUsuario() );
			}else{
				mergeFilialUsuario( empresaUsuario );
				mergeRegionalUsuario( empresaUsuario );
			}
		}

		/* Grava a EmpresaUsuario */
		Serializable id = this.store(empresaUsuario);
		
	 	if ( isEmpresaPadrao )	{
	 		// Atualiza a empresa padrão do UsuarioLMS.
	 		UsuarioLMS usuarioLMS = (UsuarioLMS) this.get(UsuarioLMS.class, empresaUsuario.getUsuario().getIdUsuario());
	 		usuarioLMS.setEmpresaPadrao(empresaUsuario.getEmpresa());
	 		getEmpresaUsuarioDAO().store(usuarioLMS, true);
	 	}
 	
		return id;
	}

	/**	
	 * Faz um merge dos itens que estão na list com os do banco dados.
	 * @param bean
	 * @return EmpresaUsuario
	 */
	private void mergeFilialUsuario( EmpresaUsuario bean ){
		List filiaisUsuarioBean = bean.getFiliaisUsuario();	
		if(filiaisUsuarioBean != null && filiaisUsuarioBean.size() > 0 ){
			List filiaisUsuario = filialUsuarioService.findByIdEmpresaUsuarioSemFetch( bean.getIdEmpresaUsuario() );
			for (int i = 0; i < filiaisUsuario.size(); i++) {
				boolean manter = false;			
				Long element = (Long) filiaisUsuario.get( i );
				for (int j = 0; j < filiaisUsuarioBean.size(); j++) {
					FilialUsuario elementBean = (FilialUsuario) filiaisUsuarioBean.get( j );			
					if( element.equals( elementBean.getIdFilialUsuario() ) ){
						manter = true;
						break;
					}
				}
				if( !manter ){
					filialUsuarioService.removeById( element);
				}			
			}
		}else{
			filialUsuarioService.removeByIdEmpresaUsuario( bean.getIdEmpresaUsuario() );
		}
		
	}
	
	/**
	 * Faz um merge dos itens que estão na list com os do banco dados.
	 * @param bean
	 * @return EmpresaUsuario
	 */
	private void mergeRegionalUsuario( EmpresaUsuario bean  ){
		List regionaisUsuarioBean = bean.getRegionalUsuario();
		if( regionaisUsuarioBean != null && regionaisUsuarioBean.size() > 0 ){
			List regionaisUsuario = regionalUsuarioService.findByIdEmpresaUsuarioSemFetch( bean.getIdEmpresaUsuario() );
			for (int i = 0; i < regionaisUsuario.size(); i++) {
				boolean manter = false;			
				Long element = (Long) regionaisUsuario.get(i);
				for (int y = 0; y < regionaisUsuarioBean.size(); y++) {
					RegionalUsuario elementBean = (RegionalUsuario) regionaisUsuarioBean.get(y);			
					if( element.equals( elementBean.getIdRegionalUsuario() ) ){
						manter = true;
						break;
					}
				}
				if( !manter ){
					//regionalUsuarioService.removeByIdEmpresaUsuario( bean.getIdEmpresaUsuario() );				
					regionalUsuarioService.removeById( element );
				}			
			}
		}else{
			List regionaisUsuario = regionalUsuarioService.findByIdEmpresaUsuarioSemFetch( bean.getIdEmpresaUsuario() );
			for (int i = 0; i < regionaisUsuario.size(); i++) {
				Long element = (Long) regionaisUsuario.get(i);
				regionalUsuarioService.removeById( element );
			}
		}
	}
	
	/**
	 * Atribui o DAO responsável por tratar a persistência dos dados deste
	 * serviço.
	 * 
	 * @param Instância
	 *            do DAO.
	 */
	public void setEmpresaUsuarioDAO(EmpresaUsuarioDAO dao) {
		setDao( dao );
	}

	/**
	 * Retorna o DAO deste serviço que é responsável por tratar a persistência
	 * dos dados deste serviço.
	 * 
	 * @return Instância do DAO.
	 */
	private EmpresaUsuarioDAO getEmpresaUsuarioDAO() {
		return (EmpresaUsuarioDAO) getDao();
	}

	public boolean findAcessoIrrestritoFilial(Usuario usuario, Empresa empresa) {
		return isIrrestritoFilial(usuario, empresa);
	}
	
	private boolean isIrrestritoFilial(Usuario usuario, Empresa empresa) {

		return getEmpresaUsuarioDAO().isIrrestritoFilial(usuario, empresa);
	}

	public EmpresaUsuario findByEmpresaUsuario(Empresa empresa, Usuario usuario) {

		return getEmpresaUsuarioDAO().findByEmpresaUsuario(empresa, usuario);
	}

	public ResultSetPage findPaginated(TypedFlatMap criteria) {
		return getEmpresaUsuarioDAO().findPaginated(criteria,
				FindDefinition.createFindDefinition(criteria));
	}

	public Integer getRowCount(TypedFlatMap criteria) {
		return getEmpresaUsuarioDAO().getRowCount(criteria);
	}

	public ResultSetPage findPaginatedFilialRegionalByEmpresa(Map criteria) {

		Long idEmpresaUsuario = MapUtils.getLong(criteria, "idEmpresaUsuario",
				null);

		if (idEmpresaUsuario == null)
			return null;

		return getEmpresaUsuarioDAO()
				.findPaginatedFilialRegional(idEmpresaUsuario,
						FindDefinition.createFindDefinition(criteria));

	}

	public Integer getRowCountFilialRegionalByEmpresa(Map criteria) {

		Long idEmpresaUsuario = MapUtils.getLong(criteria, "idEmpresaUsuario",
				null);

		if (idEmpresaUsuario == null)
			return Integer.valueOf(0);

		return getEmpresaUsuarioDAO().getRowCountFilialRegional(
				idEmpresaUsuario);

	}
	
	public EmpresaUsuario findByIdUsuarioUsingEmpresaPadrao(Long id) {
		return getEmpresaUsuarioDAO().findByIdUsuarioUsingEmpresaPadrao(id);
	}

}