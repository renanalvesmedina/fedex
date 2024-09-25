package com.mercurio.lms.rest.contasareceber.mantermodelosmensagens;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.DateTime;

import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.service.UsuarioService;
import com.mercurio.lms.contasreceber.model.DadoModeloMensagem;
import com.mercurio.lms.contasreceber.model.ModeloMensagem;
import com.mercurio.lms.contasreceber.model.service.DadoModeloMensagemService;
import com.mercurio.lms.contasreceber.model.service.EnviarFaturasEmailService;
import com.mercurio.lms.contasreceber.model.service.ModeloDeMensagemService;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto.ModelosDadosMensagensDTO;
import com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto.ModelosMensagensDTO;
import com.mercurio.lms.rest.contasareceber.mantermodelosmensagens.dto.ModelosMensagensFilterDTO;
import com.mercurio.lms.util.JTFormatUtils;
import com.mercurio.lms.util.session.SessionUtils;

@Path("/contasareceber/modelosMensagens")
public class ModelosMensagensRest extends
		BaseCrudRest<ModelosMensagensDTO, ModelosMensagensDTO, ModelosMensagensFilterDTO> {

	@InjectInJersey
	ModeloDeMensagemService modeloDeMensagemService;

	@InjectInJersey
	UsuarioService usuarioService;

	@InjectInJersey
	DadoModeloMensagemService dadoModeloMensagemService;

	
	@InjectInJersey
	EnviarFaturasEmailService enviarFaturasEmailService;

	@Override
	protected ModelosMensagensDTO findById(Long id) {
		ModeloMensagem modelo = modeloDeMensagemService.findById(id);

		ModelosMensagensDTO modeloDto = new ModelosMensagensDTO();
		modeloDto.setDcModeloAssunto(modelo.getDcModeloAssunto());
		modeloDto.setDsModeloMensagem(modelo.getDsModeloMensagem());
		modeloDto.setDcModeloCorpo(modelo.getDcModeloCorpo());
		modeloDto.setDhAlteracao(modelo.getDhAlteracao());
		if (modelo.getDtVigenciaFinal() != null)
			modeloDto.setDtVigenciaFinalY(modelo.getDtVigenciaFinal());
		if (modelo.getDtVigenciaInicial() != null)
			modeloDto.setDtVigenciaInicialY(modelo.getDtVigenciaInicial());
		modeloDto.setTpModeloMensagem(modelo.getTpModeloMensagem());
		com.mercurio.lms.configuracoes.model.Usuario user = usuarioService
				.findById(modelo.getUsuario().getIdUsuario());
		modeloDto.setUsuario(new UsuarioDTO(user.getIdUsuario(), user
				.getNmUsuario(), user.getNrMatricula()));
		modeloDto.setId(modelo.getIdModeloMensagem());
		modeloDto.setIdModeloMensagem(modelo.getIdModeloMensagem());
		return modeloDto;
	}

	@Override
	protected void removeById(Long id) {
	}

	@Override
	protected void removeByIds(List<Long> ids) {
	}

	@Override
	protected List<ModelosMensagensDTO> find(ModelosMensagensFilterDTO filter) {
		List<ModeloMensagem> modelos = modeloDeMensagemService.findModelosDeMensagem(filter.getDtVigencia(),filter.getTpModeloMensagem());

		if (modelos.isEmpty())
			return new ArrayList<ModelosMensagensDTO>();

		List<ModelosMensagensDTO> retorno = new ArrayList<ModelosMensagensDTO>();
		for (ModeloMensagem modelo : modelos) {
			ModelosMensagensDTO modeloDto = new ModelosMensagensDTO();
			modeloDto.setDcModeloAssunto(modelo.getDcModeloAssunto());
			modeloDto.setDsModeloMensagem(modelo.getDsModeloMensagem());
			modeloDto.setDcModeloCorpo(modelo.getDcModeloCorpo());
			modeloDto.setDhAlteracao(modelo.getDhAlteracao());
			if (modelo.getDtVigenciaFinal() != null)
				modeloDto.setDtVigenciaFinal(JTFormatUtils.format(modelo.getDtVigenciaFinal(), "dd/MM/yyyy"));
			if (modelo.getDtVigenciaInicial() != null)
				modeloDto.setDtVigenciaInicial(JTFormatUtils.format(modelo.getDtVigenciaInicial(), "dd/MM/yyyy"));
			modeloDto.setTpModeloMensagem(modelo.getTpModeloMensagem());
			modeloDto.setId(modelo.getIdModeloMensagem());
			modeloDto.setIdModeloMensagem(modelo.getIdModeloMensagem());
			retorno.add(modeloDto);
		}
		return retorno;
	}

	@Override
	protected Integer count(ModelosMensagensFilterDTO filter) {
		return 1;
	}

	@POST
	@Path("/findDadoModeloMensagem")
	public Response findDadoModeloMensagem(@QueryParam("id") Long id) {
		DadoModeloMensagem dadoModeloMensagem = dadoModeloMensagemService
				.findById(id);
		if (dadoModeloMensagem == null)
			return Response.noContent().build();

		ModelosDadosMensagensDTO dto = new ModelosDadosMensagensDTO();
		dto.setDescricao(dadoModeloMensagem.getDsDadoModeloMensagem());
		dto.setIdModeloMensagem(dadoModeloMensagem.getIdModeloMensagem()
				.getIdModeloMensagem());
		dto.setConteudo(dadoModeloMensagem.getDsConteudoDadoModMens());
		dto.setId(dadoModeloMensagem.getIdDadoModeloMensagem());
		dto.setNomeDado(dadoModeloMensagem.getNmDadoModeloMensagem());
		dto.setTpDado(dadoModeloMensagem.getTpDadoModeloMensagem());
		return Response.ok(dto).build();
	}

	@POST
	@Path("/findDadosModeloMensagem")
	public Response findDadosModeloMensagem(
			@QueryParam("id") Long idModeloMensagem) {
		Map criteria = new HashMap<String, Object>();
		criteria.put("idModeloMensagem.idModeloMensagem", idModeloMensagem);
		List<DadoModeloMensagem> list = dadoModeloMensagemService
				.find(criteria);
		if (list.isEmpty())
			return Response.noContent().build();

		List<Map<String, Object>> ll = new ArrayList<Map<String, Object>>();
		for (DadoModeloMensagem dado : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", dado.getIdDadoModeloMensagem());
			map.put("nome", dado.getNmDadoModeloMensagem());
			map.put("tipo", dado.getTpDadoModeloMensagem()
					.getDescriptionAsString());
			map.put("descricao", dado.getDsDadoModeloMensagem());
			map.put("conteudo", dado.getDsConteudoDadoModMens());
			ll.add(map);
		}
		Integer count = list.size();
		return getReturnFind(ll, count);
	}

	@POST
	@Path("storeDadosModeloMensagem")
	@SuppressWarnings("unchecked")
	public Response storeDadosModeloMensagem(ModelosDadosMensagensDTO bean)
			throws IOException {
		return Response.ok(dadoModeloMensagemService.store(bean.build()))
				.build();
	}

	@POST
	@Path("storeFiles")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@SuppressWarnings("unchecked")
	public Response storeFiles(FormDataMultiPart formDataMultiPart)
			throws IOException {
		/* Popula o modelo/dto/entidade/map/etc */
		ModelosMensagensDTO bean = getModelFromForm(formDataMultiPart,
				ModelosMensagensDTO.class, "data");

		/* Popula o campo com a quantidade de arquivos. */
		String dcCorpoExist = getModelFromForm(formDataMultiPart, String.class,
				"dcCorpoExist");
		String dcAssuntoExist = getModelFromForm(formDataMultiPart,
				String.class, "dcAssuntoExist");

		if (dcCorpoExist != null) {
			bean.setDcModeloCorpo(getCharacterLobUserTypeFromForm(
					formDataMultiPart, "dcCorpo"));
		}
		if (dcAssuntoExist != null) {
			bean.setDcModeloAssunto(getCharacterLobUserTypeFromForm(
					formDataMultiPart, "dcAssunto"));
		}

		bean.setDhAlteracao(new DateTime());
		bean.setUsuario(new UsuarioDTO(SessionUtils.getUsuarioLogado()
				.getIdUsuario(),
				SessionUtils.getUsuarioLogado().getNmUsuario(), SessionUtils
						.getUsuarioLogado().getNrMatricula()));

		return Response.ok(modeloDeMensagemService.store(bean.build())).build();
	}

	@Override
	protected Long store(ModelosMensagensDTO bean) {
		return 1l;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
}
