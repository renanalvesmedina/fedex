package com.mercurio.lms.expedicao.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.service.TelefoneEnderecoService;
import com.mercurio.lms.expedicao.model.CtoInternacional;
import com.mercurio.lms.expedicao.model.DespachanteCtoInt;
import com.mercurio.lms.expedicao.model.service.DespachanteCtoIntService;
import com.mercurio.lms.expedicao.util.CtoInternacionalUtils;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.ClienteDespachante;
import com.mercurio.lms.vendas.model.Despachante;
import com.mercurio.lms.vendas.model.service.ClienteDespachanteService;

/**
 * Generated by: Giuliano Costa
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.manterCRTDespachantesAction"
 */
public class ManterCRTDespachantesAction extends CrudAction {
	private DespachanteCtoIntService despachanteCtoIntService;
	private ClienteDespachanteService clienteDespachanteService;
	private TelefoneEnderecoService telefoneEnderecoService;

	public List findDespachantesCtoIntGrid(TypedFlatMap criteria){
		return prepareListToGrid(clienteDespachanteService.findByIdCliente(criteria.getLong("idCliente")));
	}

	private List prepareListToGrid(List foundeds){
		List retorno = new ArrayList();
		List idsDespachantes = findDespachantesCtoInt();

		if(foundeds != null && !foundeds.isEmpty()){
			for(int i = 0; i < foundeds.size(); i++){
				ClienteDespachante clienteDespachante = (ClienteDespachante) foundeds.get(i);
				Despachante despachante = clienteDespachante.getDespachante();
				Pessoa pessoa = despachante.getPessoa();
				
				Long idPessoa = pessoa.getIdPessoa();
				Long idEnderecoPessoa = pessoa.getEnderecoPessoa().getIdEnderecoPessoa();
				String tpPessoa = pessoa.getTpPessoa().getValue();
				String tpTelefone = tpPessoa.equals("J") ? "C" : "R";
				String nrTelefone = getTelefone(idPessoa, tpTelefone, idEnderecoPessoa);

				TypedFlatMap mapRootRetorno = new TypedFlatMap();

				mapRootRetorno.put("idDespachante", despachante.getIdDespachante());
				mapRootRetorno.put("isChecked", idsDespachantes.contains(despachante.getIdDespachante()) ? "S" : "N");
				mapRootRetorno.put("tpLocal", clienteDespachante.getTpLocal());
				mapRootRetorno.put("pessoa.nmPessoa", pessoa.getNmPessoa());
				mapRootRetorno.put("pessoa.enderecoPessoa.nrTelefone", nrTelefone);

				retorno.add(mapRootRetorno);
			}
		}
		return retorno;
	}

	private String getTelefone(Long idPessoa, String tpTelefone, Long idEnderecoPessoa){
		Map telefoneEndereco = telefoneEnderecoService.findTelefoneEnderecoByPessoaTelefoneEnderecoPessoa(idPessoa, tpTelefone, idEnderecoPessoa);
		String retorno =  "";

		if(telefoneEndereco != null && !telefoneEndereco.isEmpty()){
			String ddd = (String)telefoneEndereco.get("nrDdd");
			String ddi = (String)telefoneEndereco.get("nrDdi");
			String nrTelefone = (String)telefoneEndereco.get("nrTelefone");
			retorno =  FormatUtils.formatTelefone(nrTelefone, ddd, ddi);
		}

		return retorno;
	}

	public void storeInSession(TypedFlatMap parameters){
		List ids = parameters.getList("ids");
		CtoInternacional ctoInternacional = getCtoInternacionalInSession();
		List despachantesCtoInternacional = ctoInternacional.getDespachantesCtoInternacional();

		if(despachantesCtoInternacional != null) despachantesCtoInternacional.clear();

		if(ids != null && !ids.isEmpty()){
			for(int i = 0 ; i < ids.size(); i++){
				DespachanteCtoInt despachanteCtoInt = new DespachanteCtoInt();
				Despachante despachante = new Despachante();
				despachante.setIdDespachante(Long.valueOf((String)ids.get(i)));
				despachanteCtoInt.setDespachante(despachante);

				ctoInternacional.addDespachantesCtoInternacional(despachanteCtoInt);
			}
		}

		CtoInternacionalUtils.setCtoInternacionalInSession(ctoInternacional);
	}

	public List findDespachantesCtoInt(){
		CtoInternacional ctoInternacional = getCtoInternacionalInSession();
		List despachantesCtoInternacional = ctoInternacional.getDespachantesCtoInternacional();
		List retorno = new ArrayList();

		if(despachantesCtoInternacional != null && !despachantesCtoInternacional.isEmpty()){
			for(int i = 0; i < despachantesCtoInternacional.size(); i++){
				DespachanteCtoInt despachanteCtoInt = (DespachanteCtoInt) despachantesCtoInternacional.get(i);
				Despachante despachante = despachanteCtoInt.getDespachante();
				retorno.add(despachante.getIdDespachante());
			}
		}

		return retorno;
	}

	private CtoInternacional getCtoInternacionalInSession(){
		CtoInternacional ctoInternacional = CtoInternacionalUtils.getCtoInternacionalInSession();
		
		List despachantesCtoInternacional = ctoInternacional.getDespachantesCtoInternacional();
		if(ctoInternacional.getIdDoctoServico() != null && despachantesCtoInternacional == null) {
			List despachantesCtoInt = despachanteCtoIntService.findByIdCtoInternacional(ctoInternacional.getIdDoctoServico());
			ctoInternacional.setDespachantesCtoInternacional(despachantesCtoInt);
		}

		return ctoInternacional;
	}

	public Serializable getIdCtoInternacionalInSession(){
		CtoInternacional ctoInternacional = getCtoInternacionalInSession();

		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("idCtoInternacional", ctoInternacional.getIdDoctoServico());

		return retorno;
	}

	//Setters
	public void setDespachanteCtoIntService(
			DespachanteCtoIntService despachanteCtoIntService) {
		this.despachanteCtoIntService = despachanteCtoIntService;
	}

	public void setClienteDespachanteService(
			ClienteDespachanteService clienteDespachanteService) {
		this.clienteDespachanteService = clienteDespachanteService;
	}

	public void setTelefoneEnderecoService(
			TelefoneEnderecoService telefoneEnderecoService) {
		this.telefoneEnderecoService = telefoneEnderecoService;
	}
}