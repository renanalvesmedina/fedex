package com.mercurio.lms.services.cliente;

import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.util.IntegerUtils;
import com.mercurio.lms.util.LongUtils;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.vendas.model.Cliente;
import com.mercurio.lms.vendas.model.service.ClienteService;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/cliente")
public class ClienteRest {
	public static final String TP_PESSOA = "J";

	@InjectInJersey
	private ClienteService clienteService;

	@SuppressWarnings("unchecked")
	@POST
	@Path("findClienteSuggest")
	public Response findEmpresaSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		Integer idCliente = MapUtils.getInteger(data, "idCliente");
		List<String> notINrIdentificacao = (List<String>) MapUtils.getObject(data, "notINrIdentificacao");

		return this.findCliente(value, IntegerUtils.hasValue(idCliente) ? idCliente.longValue() : null, notINrIdentificacao);
	}

	private Response findCliente(String value, Long idCliente, List<String> notINrIdentificacao) {
		if (StringUtils.isNotBlank(value)) {

			Map<String, String> param = mountParamFindClienteSuggest(value);

			 List<Cliente> list = clienteService
					 				.findClienteSuggest(param.get("nrIdentificacao"), param.get("nmPessoa"),
					 									idCliente, TP_PESSOA, notINrIdentificacao);

			return Response.ok(parseToDTO(list)).build();

		} else if (LongUtils.hasValue(idCliente)) {
			 List<Cliente> list = clienteService.findClienteSuggest(null, null, idCliente, TP_PESSOA, notINrIdentificacao);

			return Response.ok(parseToDTO(list)).build();
		}

		return Response.ok().build();
	}

	/**
	 * Seta valores no dto para retornar
	 * Responsavel por
	 * @param list
	 * @return
	 */
	private  List<ClienteSugestSimpleDTO> parseToDTO(List<Cliente> list) {
		List<ClienteSugestSimpleDTO> lstClienteSugestSimpleDTO = new ArrayList<ClienteSugestSimpleDTO>();
		ClienteSugestSimpleDTO clienteSugestSimpleDTO = null;

		for (Cliente cliente : list) {
			//pega dados da empresa
			clienteSugestSimpleDTO = new ClienteSugestSimpleDTO();
			clienteSugestSimpleDTO.setId(cliente.getPessoa().getIdPessoa());
			clienteSugestSimpleDTO.setNmPessoa(cliente.getPessoa().getNmPessoa());
			clienteSugestSimpleDTO.setNrIdentificacao(cliente.getPessoa().getNrIdentificacao());

			//pega dados da filial
			clienteSugestSimpleDTO.setIdFilial(cliente.getFilialByIdFilialComercialSolicitada().getIdFilial());
			clienteSugestSimpleDTO.setSgFilial(
						cliente.getFilialByIdFilialComercialSolicitada().getSgFilial());

			clienteSugestSimpleDTO.setNmFantasiaFilial(
							cliente.getFilialByIdFilialComercialSolicitada().getPessoa().getNmFantasia());

			lstClienteSugestSimpleDTO.add(clienteSugestSimpleDTO);
		}

		return lstClienteSugestSimpleDTO;
	}

	@SuppressWarnings("unchecked")
	@POST
	@Path("findAllClienteSuggest")
	public Response findAllClienteSuggest(Map<String, Object> data) {
		String value = MapUtils.getString(data, "value");
		Integer idCliente = MapUtils.getInteger(data, "idCliente");
		List<String> notINrIdentificacao = (List<String>) MapUtils.getObject(data, "notINrIdentificacao");

		return this.findAllCliente(value, IntegerUtils.hasValue(idCliente) ? idCliente.longValue() : null, notINrIdentificacao);
	}

	private Response findAllCliente(String value, Long idCliente, List<String> notINrIdentificacao) {
		if (StringUtils.isNotBlank(value)) {

			Map<String, String> param = mountParamFindClienteSuggest(value);

			 List<Cliente> list = clienteService
					 				.findAllClienteSuggest(param.get("nrIdentificacao"), param.get("nmPessoa"),
					 									   idCliente, TP_PESSOA, notINrIdentificacao);

			return Response.ok(parseToDTO(list)).build();

		} else if (LongUtils.hasValue(idCliente)) {
			 List<Cliente> list = clienteService.findAllClienteSuggest(null, null, idCliente, TP_PESSOA, notINrIdentificacao);

			return Response.ok(parseToDTO(list)).build();
		}

		return Response.ok().build();
	}

	private Map<String, String> mountParamFindClienteSuggest(String value){

		Map<String, String> param = new HashMap<String, String>();
		String aux = PessoaUtils.clearIdentificacao(value);
		if (StringUtils.isNumeric(aux)) {
			value = aux;
		}

		String nrIdentificacao = null;
		String nmPessoa = null;

		if (StringUtils.isNumeric(value) && value.length() >= 8 && value.length() <= 14) {
			nrIdentificacao = value;
		} else {
			nmPessoa = value;
		}

		param.put("nrIdentificacao", nrIdentificacao);
		param.put("nmPessoa", nmPessoa);

		return param;
	}

	@GET
	@Path("findAllPaths")
	public Response findDiretoriosClientes() {
		return Response.ok(clienteService.getDiretorioCliente()).build();
	}

}
