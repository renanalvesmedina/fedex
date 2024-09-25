package com.mercurio.lms.configuracoes.model.service;

import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.FindDefinition;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.model.service.AutoridadeService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.dao.UsuarioDAO;
import com.mercurio.lms.municipios.dto.ContatoDto;

public interface UsuarioService extends com.mercurio.adsm.framework.security.model.service.UsuarioService {

	Usuario findUsuarioByLogin(String login);
	Usuario findUsuarioByLoginIdFedex(String loginIdFedex);
	Object[] findUsuarioClienteByLogin(String login);
	Usuario findById(java.lang.Long id);
	Usuario findByNrMatricula(String nrMatricula);
	
	List<Usuario> findUsuariosByPerfil(Long idPerfil, Long idFilial, Long idEmpresa);
	List<Usuario> findSubstitutoByUsuarios(List<Usuario> idsUsuario, Long idPerfil, Long idFilial, Long idEmpresa);
	List<Usuario> findSubstitutoByUsuario(Long idUsuario, Long idFilial, Long idEmpresa);
	List<Usuario> findSubstitutoByPerfil(Long idPerfil, Long idFilial, Long idEmpresa);
	
	@Deprecated
	ResultSetPage findPaginated(Map criteria);
	@Deprecated
	Integer getRowCount(Map criteria);
	@Deprecated
	void removeById(java.lang.Long id);
	@Deprecated
	void removeByIds(List ids);

	Boolean verificaAcessoFilialRegionalUsuarioLogado(Long idFilial);
	Boolean validateAcessoFilialRegionalUsuario(Long idFilial);
	Boolean validateAcessoFilialRegionalUsuario(Long idFilial, Long idUsuario);
	

	List<Map<String, Object>> findLookupUsuarioFuncionario(Long idUsuario, String nrMatricula,
									  Long idFilial, String cdFuncao, 
									  String cdCargo, String cdSetor,
									  boolean joinFuncionario);
	
	List<Map<String, Object>> findLookupUsuarioFuncionario(
			Long idUsuario, String login, String nrMatricula, 
			Long idFilial, String cdFuncao, String cdCargo, 
			String cdSetor, String nrCpf, boolean joinFuncionario);
	
	List findSuggestUsuarioPromotor (String nrMatricula, String nmFuncionario);
	
	List<Map<String, Object>> findLookupUsuarioPromotor(
			Long idUsuario, String login, String nrMatricula, 
			Long idFilial, String cdSetor, String nrCpf, boolean joinFuncionario);
	
	List<Map<String, Object>> findLookupUsuario(TypedFlatMap parametros);
	List<Map<String, Object>> findLookupMotoristaInstrutor(TypedFlatMap parametros);
	List findLookupFuncionarioPromotor(String nrMatricula);
	List findLookupByNrMatricula(String nrMatricula);
	ResultSetPage findPaginatedCustom(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String cdFuncao, String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario, FindDefinition findDef);
	Integer getRowCountCustom(Long idFilial, String nrMatricula,String nmFuncionario, String cdCargo, String cdFuncao,String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario);
	ResultSetPage findPaginatedPromotor(Long idFilial, String nrMatricula, String nmFuncionario, String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario, FindDefinition findDef);
	Integer getRowCountPromotor(Long idFilial, String nrMatricula,String nmFuncionario, String tpSituacaoFuncionario, String cdSetor, String nrCpf, boolean joinFuncionario);
	ResultSetPage findPaginatedPromotor(Long idFilial, String nrMatricula, String nmFuncionario, String cdCargo, String cdFuncao, String tpSituacaoFuncionario, FindDefinition findDef);
	Integer getRowCountPromotor(Long idFilial, String nrMatricula,String nmFuncionario, String cdCargo, String cdFuncao,String tpSituacaoFuncionario);

	TypedFlatMap findMeuPerfil();
	void loadMeuPerfil(TypedFlatMap m);
	void storeMeuPerfil(TypedFlatMap m);
	public Boolean findUsuarioHasPerfil(Long idUsuario, Long idPerfil);
	public Boolean findUsuarioHasPerfil(Long idUsuario, String dsPerfil);
	
	List find(Map criteria);
	List findLookup(Map criteria);
	Map findReport(Map criteria);
	
	void setAutoridadeService(AutoridadeService autoridadeService);
	void setClienteUsuarioService(ClienteUsuarioService clienteUsuarioService);
	void setEmpresaUsuarioService(EmpresaUsuarioService empresaUsuarioService);
	void setFuncionarioService(FuncionarioService funcionarioService);
	void setUsuarioLmsService(UsuarioLMSService usuarioLmsService);
	void setUsuarioDAO(UsuarioDAO dao);
	void setSessionContentLoaderService(SessionContentLoaderService sessionContentLoaderService);
	void setConfiguracoesFacade(ConfiguracoesFacade cf);

	AutoridadeService getAutoridadeService();
	ClienteUsuarioService getClienteUsuarioService();
	EmpresaUsuarioService getEmpresaUsuarioService();
	FuncionarioService getFuncionarioService();
	ConfiguracoesFacade getConfiguracoesFacade();
	ContatoDto findContato(Long idCliente);
	
}