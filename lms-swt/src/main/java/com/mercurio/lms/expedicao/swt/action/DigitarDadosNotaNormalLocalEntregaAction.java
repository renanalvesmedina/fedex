/**
 * 
 */
package com.mercurio.lms.expedicao.swt.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Cep;
import com.mercurio.lms.configuracoes.model.EnderecoPessoa;
import com.mercurio.lms.configuracoes.model.TipoLogradouro;
import com.mercurio.lms.configuracoes.model.param.PesquisarCepParam;
import com.mercurio.lms.configuracoes.model.service.CepService;
import com.mercurio.lms.configuracoes.model.service.EnderecoPessoaService;
import com.mercurio.lms.configuracoes.model.service.TipoLogradouroService;
import com.mercurio.lms.expedicao.model.Conhecimento;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.util.session.SessionUtils;

/**
 * @author Luis Carlos Poletto
 *
 * Não inserir documentação após ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este serviço.
 * @spring.bean id="lms.expedicao.swt.digitarDadosNotaNormalLocalEntregaAction"
 */
public class DigitarDadosNotaNormalLocalEntregaAction extends CrudAction {
	
	private PaisService paisService;
	private UnidadeFederativaService unidadeFederativaService;
	private MunicipioService municipioService;
	private CepService cepService;
	private TipoLogradouroService tipoLogradouroService;
	private EnderecoPessoaService enderecoPessoaService;
	
	public List findLookupPais(Map criteria) {
		List result = paisService.findPaisLookup(criteria);
		if (result != null && result.size() == 1) {
			Map pais = (Map) result.get(0);
			pais.put("ufs", findUnidadeFederativaByPais((Long) pais.get("idPais")));
		}
		return result;
	}
	
	public List findUnidadeFederativaByPais(Map criteria){
		return findUnidadeFederativaByPais((Long) criteria.get("idPais"));
	}

	public List findCepLookup(Map criteria) {
		PesquisarCepParam param = new PesquisarCepParam();
		param.setIdPais((Long) criteria.get("idPais"));
		param.setIdMunicipio((Long) criteria.get("idMunicipio"));
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

	public List findTipoLogradouro(Map criteria){
		return tipoLogradouroService.find(criteria);
	}
	
	public List findMunicipioLookup(Map criteria) {
		List municipios = municipioService.findLookup(criteria);
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
	
	public Map findPaisUsuarioLogado(){
		Pais pais = SessionUtils.getPaisSessao();
		Map result = new HashMap();
		result.put("nmPais", pais.getNmPais().getValue());
		result.put("idPais", pais.getIdPais());
		result.put("ufs", findUnidadeFederativaByPais(pais.getIdPais()));
	 	return result;
	}

	/**
	 * Busca os endereços de entrega vigentes cadastrados para 
	 * um determinado cliente
	 * 
	 * autor Pedro Henrique Jatobá
	 * 23/12/2005
	 * @param dados
	 * @return
	 */
	public List findEnderecosEntrega(Map dados){
		List retorno = new ArrayList();
		List ids = new ArrayList();
		Conhecimento conhecimento = (Conhecimento) dados.get("conhecimento");

		if (conhecimento.getClienteByIdClienteRedespacho() != null) {
			ids.add(conhecimento.getClienteByIdClienteRedespacho().getIdCliente());
		} else if (dados != null && dados.get("idDestinatario") != null) {
			Long idDest = (Long) dados.get("idDestinatario");
			ids.add(idDest);
		}
		
		if (ids.size() > 0) {
			List enderecosDest = enderecoPessoaService.findByIdPessoaTpEnderecoLocalEntrega(ids,"ENT");
			for (Iterator iter = enderecosDest.iterator(); iter.hasNext();) {
				Map enderecoDest = (Map) iter.next();
				String enderecoCompleto = "";
				// Tipo de Logradouro
				VarcharI18n dsTipoLogradouro = (VarcharI18n) enderecoDest.get("dsTipoLogradouro");
				if (dsTipoLogradouro != null) {
					enderecoCompleto = dsTipoLogradouro.getValue() + " ";
				}
				// Endereco + Numero
				enderecoCompleto += enderecoDest.get("dsEndereco") + ", " + enderecoDest.get("nrEndereco");
				// Complemento
				String dsComplemento = (String) enderecoDest.get("dsComplemento");
				if (dsComplemento != null)
					enderecoCompleto += " - " + dsComplemento;
				// Bairro
				String dsBairro = (String) enderecoDest.get("dsBairro");
				if (dsBairro != null)
					enderecoCompleto += " - " + dsBairro;
				// CEP + Municipio + UF + Pais
				enderecoCompleto += " - CEP:" + enderecoDest.get("nrCep") + " - " + enderecoDest.get("nmMunicipio") + " - " + enderecoDest.get("sgUnidadeFederativa") + " - " + enderecoDest.get("nmPais");
				
				enderecoDest.put("dsEnderecoPessoa", enderecoCompleto);
				enderecoDest.put("cepCriteria", enderecoDest.get("nrCep"));
				enderecoDest.put("ufs", findUnidadeFederativaByPais((Long) enderecoDest.get("idPais")));
				
				retorno.add(enderecoDest);
			}
		}
		return retorno;
	}

	/**
	 * Verifica se já existe algum local de entrega salvo na sessão
	 * e retorna um Mapa com os dados que serão carregados na tela
	 * autor Pedro Henrique Jatobá
	 * 23/12/2005
	 * @return
	 */
	public Map findInSession(Map dados) {
		Map retorno = new HashMap();
		Conhecimento conhecimento = (Conhecimento) dados.get("conhecimento");

		if (conhecimento.getEnderecoPessoa() != null){
			EnderecoPessoa enderecoPessoa = conhecimento.getEnderecoPessoa();
			retorno.put("idEnderecoPessoa",enderecoPessoa.getIdEnderecoPessoa());

			Municipio municipio = conhecimento.getMunicipioByIdMunicipioEntrega();
			if (municipio != null) {
				retorno.put("idMunicipio", municipio.getIdMunicipio());
				retorno.put("nmMunicipio", municipio.getNmMunicipio());
				
				UnidadeFederativa uf = municipio.getUnidadeFederativa();
				if (uf != null) {
					retorno.put("idUnidadeFederativa", uf.getIdUnidadeFederativa());
					
					Pais pais = uf.getPais();
					if (pais != null) {
						retorno.put("idPais", pais.getIdPais());
						retorno.put("nmPais", pais.getNmPais().getValue());
					}
					retorno.put("ufs", findUnidadeFederativaByPais(pais.getIdPais()));
				}
			}		
			retorno.put("dsLocalEntrega", conhecimento.getDsLocalEntrega());
			retorno.put("dsBairro", conhecimento.getDsBairroEntrega());
			retorno.put("dsComplemento", conhecimento.getDsComplementoEntrega());
			retorno.put("dsEndereco", enderecoPessoa.getDsEndereco());
			retorno.put("nrEndereco", conhecimento.getNrEntrega());
			
			if (enderecoPessoa.getTipoLogradouro() != null) {
				retorno.put("idTipoLogradouro", enderecoPessoa.getTipoLogradouro().getIdTipoLogradouro());
			}
			
			if (conhecimento.getNrCepEntrega() != null) {
				retorno.put("cepCriteria", conhecimento.getNrCepEntrega());	
				retorno.put("nrCep", conhecimento.getNrCepEntrega());
			}
		}    			   			    	
		return retorno;
	}

	/**
	 * Armazena os dados fornecidos na sessão 
	 * 
	 * autor Pedro Henrique Jatobá
	 * 23/12/2005
	 * @param parametros
	 */
	public Map storeInSession(Map params) {
		Conhecimento conhecimento = (Conhecimento) params.get("conhecimento");
		EnderecoPessoa enderecoPessoa = new EnderecoPessoa();
		conhecimento.setEnderecoPessoa(enderecoPessoa);

		enderecoPessoa.setIdEnderecoPessoa(null);
		Long idEnderecoPessoa = (Long) params.get("idEnderecoPessoa");
		if (idEnderecoPessoa != null) {
			enderecoPessoa.setIdEnderecoPessoa(idEnderecoPessoa);
		}

		Long idMunicipio = (Long) params.get("idMunicipio");
		if(idMunicipio != null) {
			Municipio municipio = municipioService.findById(idMunicipio);
			conhecimento.setMunicipioByIdMunicipioEntrega(municipio);
			enderecoPessoa.setMunicipio(municipio);

			TipoLogradouro tipoLogradouro = new TipoLogradouro();
			Long idTipoLogradouro = (Long) params.get("idTipoLogradouro");
			tipoLogradouro.setIdTipoLogradouro(idTipoLogradouro);
			tipoLogradouro.setDsTipoLogradouro(new VarcharI18n(MapUtils.getString(params, "dsTipoLogradouro")));
			
			enderecoPessoa.setTipoLogradouro(tipoLogradouro);
			enderecoPessoa.setDsEndereco((String) params.get("dsEndereco"));

			conhecimento.setTipoLogradouroEntrega(tipoLogradouro);
			conhecimento.setDsLocalEntrega((String) params.get("dsLocalEntrega"));
			conhecimento.setDsBairroEntrega((String) params.get("dsBairro"));
			conhecimento.setDsComplementoEntrega((String) params.get("dsComplemento"));
			conhecimento.setDsEnderecoEntrega((String) params.get("dsEndereco"));
			conhecimento.setNrEntrega(params.get("nrEndereco").toString());
			String nrCep = (String) params.get("nrCep");
			if(StringUtils.isNotBlank(nrCep)) {
				conhecimento.setNrCepEntrega(nrCep);
			}
		}
		Map result = new LinkedHashMap<String, Object>();
		result.put("conhecimento", conhecimento);
		return result;
	}

	/*
	 * METODOS PRIVADOS
	 */
	private List findUnidadeFederativaByPais(Long idPais){
		TypedFlatMap criteria = new TypedFlatMap();
		criteria.put("pais.idPais", idPais.toString());
		criteria.put("tpSituacao", "A");
		return unidadeFederativaService.findByPais(criteria);
	}
	
	/*
	 * GETTERS E SETTERS
	 */
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
	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}
}
