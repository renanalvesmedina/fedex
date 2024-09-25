package com.mercurio.lms.rest.workflow;

import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.rest.utils.Closure;
import com.mercurio.lms.rest.utils.ListResponseBuilder;
import com.mercurio.lms.tributos.dto.FluxoWorkFlowDto;
import com.mercurio.lms.workflow.model.dto.CloneDTO;
import com.mercurio.lms.workflow.model.dto.GrupoAprovadoresDTO;
import com.mercurio.lms.workflow.model.service.AcaoService;
import com.mercurio.lms.workflow.model.service.GrupoAprovadoresService;
import com.mercurio.lms.workflow.model.service.PerfilAprovadoresService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/workflow/workflow")
public class WorkflowRest {

	private static final Logger LOGGER = LogManager.getLogger(WorkflowRest.class);
	private static final String VALUE = "value";
	private static final String MENSAGEM = "mensagem";
	private static final String ERROR = "error";
	private static final String INFO = "info";
	private static final String USUARIO_NAO_CADASTRADO = "Usuário não encontrado: ";
	private static final String USUARIO_SEM_CONFIGURACAO = "Nenhuma configuração encontrada para usuário: ";
	private static final String PERMISSOES_EXISTENTES = "Este usuário já existe para todos os Grupos de Aprovadores.";
	private static final String ATRIBUICAO_PERMISSAO = " recebeu todos os vincúlos de ";
	private static final String VINCULO_EXCLUIDO = "Vinculo excluido com sucesso!";


	@InjectInJersey
	AcaoService acaoService;

	@InjectInJersey
	GrupoAprovadoresService grupoAprovadoresService;

	@InjectInJersey
	UsuarioService usuarioService;

	@InjectInJersey
	PerfilAprovadoresService perfilAprovadoresService;


	/**
	 * Retorna status do andamento do workflow.
	 *
	 * @param id
	 *
	 * @return Response
	 */
	@GET
	@Path("/findWorkflow")
	public Response findWorkflow(@QueryParam("id") Long id) {
		final List<Map<String, Object>> lista = acaoService.findPendencias(id);

		return new ListResponseBuilder()
		.findClosure(new Closure<List<Map<String,Object>>>() {

			@Override
			public List<Map<String, Object>> execute() {
				List<Map<String, Object>> listaWorkflow = new ArrayList<Map<String, Object>>();

				for (Map<String, Object> o : lista) {
					Map<String, Object> map = new HashMap<String, Object>();

					map.put("integrante", o.get("integrante"));
					map.put("dhLiberacao", o.get("dhLiberacao"));
					map.put("dhAcao", o.get("dhAcao"));
					map.put("obAcao", o.get("obAcao"));
					map.put("tpSituacaoAcao", ((VarcharI18n) o.get("tpSituacaoAcao.description")).getValue());

					listaWorkflow.add(map);
				}

				return listaWorkflow;

			}
		})
		.rowCountClosure(new Closure<Integer>() {

			@Override
			public Integer execute() {
				return lista.size();
			}

		})
		.suppressWarning()
		.build();
	}

	@POST
	@Path("findAllGruposAprovadores")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllGruposAprovadores(GrupoAprovadoresDTO filter){
		List retorno = grupoAprovadoresService.findAllGruposAprovadores(filter);
		return Response.ok(retorno).build();
	}

	@POST
	@Path("findByNomeUsuarios")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByNomeUsuarios(GrupoAprovadoresDTO filter){
		List retorno = grupoAprovadoresService.findByNomeUsuarios(filter);
		return Response.ok(retorno).build();
	}

	@POST
	@Path("findByDescGrupoAprovadores")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByDescGrupoAprovadores(GrupoAprovadoresDTO filter){
		List retorno = grupoAprovadoresService.findByDescGrupoAprovadores(filter);
		return Response.ok(retorno).build();
	}

	@POST
	@Path("findByNomeUsuarioAndDescGrupoAprovadores")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByNomeUsuarioAndDescGrupoAprovadores(GrupoAprovadoresDTO filter){
		List retorno = grupoAprovadoresService.findByNomeUsuarioAndDescGrupoAprovadores(filter);
		return Response.ok(retorno).build();
	}

	@POST
	@Path("removeByIds")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeByIds(@QueryParam("idsSelecionados") List<Long> idsSelecionados){
		Map<Object, String> mapDelete = new HashMap<>();
		mapDelete.put(VALUE, INFO);
		mapDelete.put(MENSAGEM, VINCULO_EXCLUIDO);

		grupoAprovadoresService.removeByIds(idsSelecionados);
		return Response.ok(mapDelete).build();
	}

	@DELETE
	@Path("removeById")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeById(@QueryParam("id") Long id){
		Map<Object, String> mapDelete = new HashMap<>();
		mapDelete.put(VALUE, INFO);
		mapDelete.put(MENSAGEM, VINCULO_EXCLUIDO);

		List idExcluir = new ArrayList();
		idExcluir.add(id);
		grupoAprovadoresService.removeByIds(idExcluir);
		return Response.ok(mapDelete).build();
	}

	@POST
	@Path("clonar")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response cloneUser(CloneDTO usuarios){

		Map<Object, String> mapClone = new HashMap<>();

		Usuario usuarioBase  = usuarioService.findUsuarioByLogin(usuarios.getBase());

		if (usuarioBase == null) {
			mapClone.put(VALUE, ERROR);
			mapClone.put(MENSAGEM, USUARIO_NAO_CADASTRADO.concat(usuarios.getBase()));
			return Response.ok(mapClone).build();
		}

		Usuario usuarioClone = usuarioService.findUsuarioByLogin(usuarios.getClone());
		if (usuarioClone == null) {
			mapClone.put(VALUE, ERROR);
			mapClone.put(MENSAGEM, USUARIO_NAO_CADASTRADO.concat(usuarios.getClone()));
			return Response.ok(mapClone).build();
		}

		List perfisBase  = grupoAprovadoresService.findByIdUsuarioPerfilUsuario(usuarioBase.getIdUsuario());
		if (perfisBase.isEmpty()) {
			mapClone.put(VALUE, ERROR);
			mapClone.put(MENSAGEM, USUARIO_SEM_CONFIGURACAO.concat(usuarios.getBase()));
			return Response.ok(mapClone).build();
		}

		List perfisClone = grupoAprovadoresService.findByIdUsuarioPerfilUsuario(usuarioClone.getIdUsuario());

		List persistence = null;

		try {
			persistence = grupoAprovadoresService.getListPersistence(perfisBase, perfisClone, usuarios.getSituacao(), usuarioClone.getIdUsuario());
			if(persistence.isEmpty()){
				mapClone.put(VALUE, ERROR);
				mapClone.put(MENSAGEM, PERMISSOES_EXISTENTES);
				return Response.ok(mapClone).build();
			}
		} catch (Exception e) {
			LOGGER.error("Falha ao preparar registros para persistir: " + e);
		}

		if(grupoAprovadoresService.storeGrupoAprovadores(persistence)) {
			mapClone.put(VALUE, INFO);
			mapClone.put(MENSAGEM, usuarios.getClone().concat(ATRIBUICAO_PERMISSAO).concat(usuarios.getBase()));
			return Response.ok(mapClone).build();
		}

		return Response.serverError().build();
	}

	@POST
	@Path("adicionargrupo")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addGrupoUser(Map<String, String> usuarioGrupo){

		Map<Object, String> mapClone = new HashMap<>();

		Usuario usuario =  usuarioService.findUsuarioByLogin(usuarioGrupo.get("usuario"));
		if (usuario == null) {
			mapClone.put(VALUE, ERROR);
			mapClone.put(MENSAGEM, USUARIO_NAO_CADASTRADO.concat(usuarioGrupo.get("usuario")));
			return Response.status(Response.Status.NOT_FOUND).entity(mapClone).build();
		}

		Long idPerfil = Long.valueOf(usuarioGrupo.get("perfil"));

		List validaAtribuicao = grupoAprovadoresService.findByIdUsuarioAndIdPerfilUsuario(usuario.getIdUsuario(), idPerfil);

		if(!validaAtribuicao.isEmpty()){
			mapClone.put(VALUE, ERROR);
			mapClone.put(MENSAGEM, "Usuário " + usuario.getNmUsuario() + " já possui o perfil selecionado!");
			return Response.ok(mapClone).build();
		}

		List persistence = grupoAprovadoresService.getPersistence(usuario.getIdUsuario(), idPerfil, usuarioGrupo.get("situacao"));

		if(grupoAprovadoresService.storeGrupoAprovadores(persistence)) {
			mapClone.put(VALUE, "info");
			mapClone.put(MENSAGEM, "Perfil atribuído ao usuário " + usuario.getNmUsuario());
			return Response.ok(mapClone).build();
		}

		return Response.serverError().build();
	}

	@GET
	@Path("findAllPerfis")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List findAllPerfis(){
		return perfilAprovadoresService.findAllPerfis();
	}

	@GET
	@Path("findUsuarioEqualLogin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List findUsuarioEqualLogin(@QueryParam("usuario") String usuario){
		return grupoAprovadoresService.findUsuarioEqualLogin(usuario);
	}

	@GET
	@Path("findSuggestUsuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List findSuggestUsuario(@QueryParam("login") String login){
		return grupoAprovadoresService.findSuggestUsuario(login);
	}

	@GET
	@Path("getValorDominio")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Map<String, String>> getValorDominio(){
		return grupoAprovadoresService.getValorDominio();
	}

	@GET
	@Path("/findPendenciaSituacaoWorkflow")
	public Response findPendenciaSituacaoWorkflow(@QueryParam("idDoctoServico") Long idDoctoServico) {

		final List<FluxoWorkFlowDto> listaSituacaoWorkflow = acaoService.findPendenciaSituacaoWorkflow(idDoctoServico);

		List<Map<String, Object>> listMap = new ArrayList<>();



		for(FluxoWorkFlowDto l : listaSituacaoWorkflow){
			Map<String, Object> map = new HashMap<>();
			map.put("integrante", l.getAprovador());
			map.put("dhLiberacao", l.getDhLiberacao());
			map.put("dhAcao", l.getDhAcao());

			final List<Map<String, Object>> lista = acaoService.findPendencias(l.getIdPendencia());

			for (Map<String, Object> o : lista) {
				if(o.containsKey("tpSituacaoAcao.description")){
					VarcharI18n tpSituacaoAcao = (VarcharI18n) o.get("tpSituacaoAcao.description");
					map.put("tpSituacaoAcao", tpSituacaoAcao.getValue());
				} else {
					map.put("tpSituacaoAcao", " ");
				}
			}

			map.put("obAcao", l.getObAcao());

			listMap.add(map);
		}

		return new ListResponseBuilder()
				.findClosure(() -> listMap)
				.rowCountClosure(() -> listMap.size())
				.suppressWarning()
				.build();
	}
}
