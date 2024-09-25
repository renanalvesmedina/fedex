package com.mercurio.lms.expedicao.swt.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mercurio.adsm.framework.BusinessException;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.ConfiguracoesFacade;
import com.mercurio.lms.configuracoes.model.Cep;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.configuracoes.model.RamoAtividade;
import com.mercurio.lms.configuracoes.model.param.PesquisarCepParam;
import com.mercurio.lms.configuracoes.model.service.CepService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.RamoAtividadeService;
import com.mercurio.lms.configuracoes.model.service.TipoLogradouroService;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.PessoaUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.ClienteService;

/**
 * Generated by: ADSM ActionGenerator
 *
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.expedicao.swt.cadastrarClientesAction"
 */

public class CadastrarClientesAction extends CrudAction {
	private PaisService paisService;
	private UnidadeFederativaService unidadeFederativaService;
	private MunicipioService municipioService;
	private CepService cepService;
	private TipoLogradouroService tipoLogradouroService;
	private EnderecoPessoaService enderecoPessoaService;
	private ConfiguracoesFacade configuracoesFacade;
	private RamoAtividadeService ramoAtividadeService;

	public List findLookupPais(Map criteria) {
		List result = paisService.findPaisLookup(criteria);
		if (result != null && result.size() == 1) {
			Map pais = (Map) result.get(0);
			pais.put("ufs", findUnidadeFederativaByPais((Long) pais.get("idPais")));
		}
		return result;
	}

	public Serializable store(Map parameters) {
		TypedFlatMap dados = new TypedFlatMap();
		dados.putAll(parameters);
		return getClienteService().saveCliente(dados, Boolean.TRUE);
	}

	public Serializable storeSemValidacaoTributaria(Map parameters) {
		TypedFlatMap dados = new TypedFlatMap();
		dados.putAll(parameters);
		return getClienteService().saveCliente(dados, Boolean.FALSE);
	}
	
	public Serializable saveClienteBasico(TypedFlatMap parameters) {
		return getClienteService().saveCliente(parameters, Boolean.TRUE);
		}

	public List findCepLookup(Map criteria) {
		PesquisarCepParam param = new PesquisarCepParam();
		param.setIdPais((Long) criteria.get("idPais"));
		param.setNrCep(criteria.get("cepCriteria") + "%");
		
		List<Cep> ceps = cepService.findCepLookup(param);
		if (ceps != null) {
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Cep cep : ceps) {
				Map<String, Object> mapCep = new HashMap<String, Object>();
				mapCep.put("idPais", cep.getMunicipio().getUnidadeFederativa().getPais().getIdPais());
				mapCep.put("nmPais", cep.getMunicipio().getUnidadeFederativa().getPais().getNmPais().getValue());
				mapCep.put("idUnidadeFederativa", cep.getMunicipio().getUnidadeFederativa().getIdUnidadeFederativa());
				mapCep.put("idMunicipio", cep.getMunicipio().getIdMunicipio());
				mapCep.put("nmMunicipio", cep.getMunicipio().getNmMunicipio());
				mapCep.put("cepCriteria", cep.getNrCep());
				mapCep.put("dsBairro", cep.getNmBairro());
				mapCep.put("dsEndereco", cep.getNmLogradouro());
				mapCep.put("nrCep", cep.getNrCep());
				mapCep.put("dsTipoLogradouro", cep.getDsTipoLogradouro());
				mapCep.put("ufs", findUnidadeFederativaByPais(cep.getMunicipio().getUnidadeFederativa().getPais().getIdPais()));
				result.add(mapCep);
			}
			return result;
		}
		return null;
	}

	public List findTipoLogradouro(Map criteria) {
		return tipoLogradouroService.find(criteria);
	}

	public List findMunicipioLookup(Map criteria) {		
		Map mapPais = new HashMap();
		mapPais.put("idPais", criteria.get("idPais"));		
		Map mapUf = new HashMap();
		mapUf.put("pais", mapPais);
		mapUf.put("idUnidadeFederativa", criteria.get("idUnidadeFederativa"));		
		Map mapCriteria = new HashMap();
		mapCriteria.put("unidadeFederativa", mapUf);
		mapCriteria.put("tpSituacao", criteria.get("tpSituacao"));
		mapCriteria.put("nmMunicipio", criteria.get("nmMunicipio"));

		List municipios = municipioService.findLookup(mapCriteria);
		List result = new ArrayList();
		if (municipios != null && !municipios.isEmpty()) {
			for (int i = 0; i < municipios.size(); i++) {
				Municipio municipio = (Municipio) municipios.get(i);
				Map map = new HashMap();
				map.put("idMunicipio", municipio.getIdMunicipio());
				map.put("nmMunicipio", municipio.getNmMunicipio());
				map.put("idUnidadeFederativa", municipio.getUnidadeFederativa().getIdUnidadeFederativa());
				map.put("idPais", municipio.getUnidadeFederativa().getPais().getIdPais());
				map.put("nmPais", municipio.getUnidadeFederativa().getPais().getNmPais());
				map.put("ufs", findUnidadeFederativaByPais(municipio.getUnidadeFederativa().getPais().getIdPais()));
				result.add(map);
			}
		}
		return result;
	}

	public Map findPessoa(Map criteria) {
	 	String tpIdentificacao = (String) criteria.get("tpIdentificacao");
	 	String nrIdentificacao = PessoaUtils.clearIdentificacao((String) criteria.get("nrIdentificacao"));
	 	String tpPessoa = (String) criteria.get("tpPessoa");
	 	Pessoa pessoa = configuracoesFacade.getPessoa(nrIdentificacao, tpIdentificacao);
	 	Map p = new HashMap();
	 	if(pessoa != null) {
	 		Long idPessoa = pessoa.getIdPessoa();
	 		Integer total = getClienteService().getRowCountByIdPessoa(idPessoa);
	 		if(total != null && total.intValue() > 0)
	 			throw new BusinessException("LMS-40003");
	 		String origem = (String)criteria.get("origem");
	 		Map endereco = null;
	 		if("exp".equalsIgnoreCase(origem)) {
	 			if("F".equals(tpPessoa)) {
	 				endereco = enderecoPessoaService.findByIdPessoaPrioridade(idPessoa, new String[]{"RES", "COM"});
	 			} else
	 				endereco = enderecoPessoaService.findByIdPessoaPrioridade(idPessoa, new String[]{"COM"});
	 		} else if("col".equalsIgnoreCase(origem)) {
	 			if("F".equals(tpPessoa)) {
	 				endereco = enderecoPessoaService.findByIdPessoaPrioridade(idPessoa, new String[]{"COL", "RES", "COM"});
	 			} else
	 				endereco = enderecoPessoaService.findByIdPessoaPrioridade(idPessoa, new String[]{"COL", "COM"});
	 		}
			p.put("idPessoa", idPessoa);
			p.put("nmPessoa", pessoa.getNmPessoa());
			
			if (endereco != null) {
				Map municipio = (Map) endereco.get("municipio");
				Map unidadeFederativa = (Map) municipio.get("unidadeFederativa");
				Map pais = (Map) unidadeFederativa.get("pais");
				Map tipoLogradouro = (Map) endereco.get("tipoLogradouro");
				
				if (pais != null) {
					p.put("idPais", pais.get("idPais"));
					p.put("nmPais", ((VarcharI18n) pais.get("nmPais")).getValue());
					p.put("ufs", findUnidadeFederativaByPais((Long) pais.get("idPais")));
				}
				if (unidadeFederativa != null) {
					p.put("idUnidadeFederativa", unidadeFederativa.get("idUnidadeFederativa"));
				}
				if (municipio != null) {
					p.put("idMunicipio", municipio.get("idMunicipio"));
					p.put("nmMunicipio", municipio.get("nmMunicipio"));
				}
				if (tipoLogradouro != null) {
					p.put("idTipoLogradouro", tipoLogradouro.get("idTipoLogradouro"));
				}
				p.put("idEndereco", endereco.get("idEnderecoPessoa"));
				p.put("cepCriteria", endereco.get("nrCep"));
				p.put("nrCep", endereco.get("nrCep"));
				p.put("dsBairro", endereco.get("dsBairro"));
				p.put("dsEndereco", endereco.get("dsEndereco"));
				p.put("nrEndereco", endereco.get("nrEndereco"));
			}
	 	}
	 	return p;
	}

	public Map findPaisUsuarioLogado(){
		Pais pais = SessionUtils.getPaisSessao();
		Map result = new HashMap();
		result.put("nmPais", pais.getNmPais().getValue());
		result.put("idPais", pais.getIdPais());
		result.put("ufs", findUnidadeFederativaByPais(pais.getIdPais()));
	 	return result;
	}
	
	public List findRamosAtividades(Map criteria) {
		List ramosAtividades = ramoAtividadeService.findCombo(criteria);
		List result = new ArrayList<Map>();
		if (ramosAtividades != null && !ramosAtividades.isEmpty()) {
			for (int i = 0; i < ramosAtividades.size(); i++) {
				RamoAtividade ramoAtividade = (RamoAtividade) ramosAtividades.get(i);
				Map<String, Object> ramo = new HashMap<String, Object>();
				ramo.put("idRamoAtividade", ramoAtividade.getIdRamoAtividade());
				ramo.put("dsRamoAtividade", ramoAtividade.getDsRamoAtividade());
				result.add(ramo);
			}
		}
		return result;
	}

	public List findTpSituacaoTributaria(Map criteria) {
		List ramosAtividades = ramoAtividadeService.findCombo(criteria);
		List result = new ArrayList<Map>();
		if (ramosAtividades != null && !ramosAtividades.isEmpty()) {
			for (int i = 0; i < ramosAtividades.size(); i++) {
				RamoAtividade ramoAtividade = (RamoAtividade) ramosAtividades.get(i);
				Map<String, Object> ramo = new HashMap<String, Object>();
				ramo.put("idRamoAtividade", ramoAtividade.getIdRamoAtividade());
				ramo.put("dsRamoAtividade", ramoAtividade.getDsRamoAtividade());
				result.add(ramo);
			}
		}
		return result;
	}
	
	public List findUnidadeFederativaByPais(Long idPais){
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("pais.idPais", idPais.toString());
		criteria.put("tpSituacao", "A");
		return unidadeFederativaService.findByPais(criteria);
	}

	/*
	 * Metodos privados
	 */
	private ClienteService getClienteService() {
		return (ClienteService)this.defaultService;
	}
	public void setClienteService(ClienteService clienteService) {
		this.defaultService = clienteService;
	}
	public void setEnderecoPessoaService(EnderecoPessoaService enderecoPessoaService) {
		this.enderecoPessoaService = enderecoPessoaService;
	}
	public void setTipoLogradouroService(TipoLogradouroService tipoLogradouroService) {
		this.tipoLogradouroService = tipoLogradouroService;
	}
	public void setCepService(CepService cepService) {
		this.cepService = cepService;
	}
	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
	}
	public void setUnidadeFederativaService(UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
	public void setConfiguracoesFacade(ConfiguracoesFacade configuracoesFacade) {
		this.configuracoesFacade = configuracoesFacade;
	}
	public void setRamoAtividadeService(RamoAtividadeService ramoAtividadeService) {
		this.ramoAtividadeService = ramoAtividadeService;
	}
}
