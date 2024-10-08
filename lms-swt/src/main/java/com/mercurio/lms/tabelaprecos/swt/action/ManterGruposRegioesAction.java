package com.mercurio.lms.tabelaprecos.swt.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.mercurio.adsm.framework.annotations.ParametrizedAttribute;
import com.mercurio.adsm.framework.model.CrudAction;
import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.PaginatedQuery;
import com.mercurio.adsm.framework.model.ResultSetPage;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.municipios.model.Municipio;
import com.mercurio.lms.municipios.model.Pais;
import com.mercurio.lms.municipios.model.UnidadeFederativa;
import com.mercurio.lms.municipios.model.service.MunicipioService;
import com.mercurio.lms.municipios.model.service.PaisService;
import com.mercurio.lms.municipios.model.service.UnidadeFederativaService;
import com.mercurio.lms.tabelaprecos.model.GrupoRegiao;
import com.mercurio.lms.tabelaprecos.model.TabelaPreco;
import com.mercurio.lms.tabelaprecos.model.service.GrupoRegiaoService;
import com.mercurio.lms.tabelaprecos.model.service.TabelaPrecoService;

/**
 * Generated by: ADSM ActionGenerator
 * 
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.tabelaprecos.swt.manterGruposRegioesAction"
 */
public class ManterGruposRegioesAction extends CrudAction {

	private GrupoRegiaoService grupoRegiaoService;
	private UnidadeFederativaService unidadeFederativaService;
	private MunicipioService municipioService;
	
	private TabelaPrecoService tabelaPrecoService;
	private PaisService paisService;
	
	public Map findById(Long id) {
		GrupoRegiao grupo = grupoRegiaoService.findById(id);
		
		Map<String,Object> retorno = new HashMap<String, Object>();		
		retorno.put("idGrupoRegiao", grupo.getIdGrupoRegiao());
		retorno.put("dsGrupoRegiao", grupo.getDsGrupoRegiao());
		
		retorno.put("idTabelaPreco", grupo.getTabelaPreco().getIdTabelaPreco());
		retorno.put("dsDescricao", grupo.getTabelaPreco().getDsDescricao());	
		retorno.put("tabelaPrecoString", grupo.getTabelaPreco().getTabelaPrecoString());
		
		retorno.put("idUnidadeFederativa", grupo.getUnidadeFederativa().getIdUnidadeFederativa());
		retorno.put("sgUnidadeFederativa", grupo.getUnidadeFederativa().getSgUnidadeFederativa());
		retorno.put("nmUnidadeFederativa", grupo.getUnidadeFederativa().getNmUnidadeFederativa());
		
		retorno.put("idPais", grupo.getUnidadeFederativa().getPais().getIdPais());
		retorno.put("nmPais", grupo.getUnidadeFederativa().getPais().getNmPais());
		
		retorno.put("isTabelaPrecoEfetivada", grupo.getTabelaPreco().getBlEfetivada());
		
		//---- Nullable fields
		retorno.put("vlAjusteMinimo", grupo.getVlAjusteMinimo() != null ? grupo.getVlAjusteMinimo() : "");
		retorno.put("vlAjustePadrao", grupo.getVlAjustePadrao() != null ? grupo.getVlAjustePadrao() : "");
		retorno.put("tpAjuste", grupo.getTpAjuste() != null ? grupo.getTpAjuste().getValue() : "");
		retorno.put("tpValorAjuste", grupo.getTpValorAjuste() != null ? grupo.getTpValorAjuste().getValue() : "");

		//CQ 28633
		retorno.put("vlAjustePadraoAdvalorem", grupo.getVlAjustePadraoAdvalorem() != null ? grupo.getVlAjustePadraoAdvalorem() : "");
		retorno.put("vlAjusteMinimoAdvalorem", grupo.getVlAjusteMinimoAdvalorem() != null ? grupo.getVlAjusteMinimoAdvalorem() : "");
		retorno.put("tpAjusteAdvalorem", grupo.getTpAjusteAdvalorem() != null ? grupo.getTpAjusteAdvalorem().getValue() : "");
		retorno.put("tpValorAjusteAdvalorem", grupo.getTpValorAjusteAdvalorem() != null ? grupo.getTpValorAjusteAdvalorem().getValue() : "");
		//----
		
		return retorno;
	}
	
	public Map<String, Long> store(Map map) {		
		GrupoRegiao grupo = new GrupoRegiao();
		
		grupo.setIdGrupoRegiao(MapUtils.getLong(map, "idGrupoRegiao"));
		grupo.setDsGrupoRegiao(MapUtils.getString(map, "dsGrupoRegiao"));
		TabelaPreco tabela = new TabelaPreco();
		tabela.setIdTabelaPreco(MapUtils.getLong(map, "idTabelaPreco"));
		grupo.setTabelaPreco(tabela);
		
		UnidadeFederativa uf = new UnidadeFederativa();
		uf.setIdUnidadeFederativa(MapUtils.getLong(map, "idUnidadeFederativa"));
		grupo.setUnidadeFederativa(uf);
					
		//---- Nullable fields
		if (map.get("vlAjusteMinimo") != null)
			grupo.setVlAjusteMinimo((BigDecimal) map.get("vlAjusteMinimo"));
		
		if (map.get("vlAjustePadrao") != null)
			grupo.setVlAjustePadrao((BigDecimal) map.get("vlAjustePadrao"));
		
		if (MapUtils.getString(map, "tpAjuste") != null)
			grupo.setTpAjuste(new DomainValue(MapUtils.getString(map, "tpAjuste")));
		
		if (MapUtils.getString(map, "tpValorAjuste") != null)
			grupo.setTpValorAjuste(new DomainValue(MapUtils.getString(map, "tpValorAjuste")));

		//CQ 28633
		if (map.get("vlAjustePadraoAdvalorem") != null)
			grupo.setVlAjustePadraoAdvalorem((BigDecimal) map.get("vlAjustePadraoAdvalorem"));

		if (map.get("vlAjusteMinimoAdvalorem") != null)
			grupo.setVlAjusteMinimoAdvalorem((BigDecimal) map.get("vlAjusteMinimoAdvalorem"));
		
		if (MapUtils.getString(map, "tpAjusteAdvalorem") != null)
			grupo.setTpAjusteAdvalorem(new DomainValue(MapUtils.getString(map, "tpAjusteAdvalorem")));
		
		if (MapUtils.getString(map, "tpValorAjusteAdvalorem") != null)
			grupo.setTpValorAjusteAdvalorem(new DomainValue(MapUtils.getString(map, "tpValorAjusteAdvalorem")));
		grupoRegiaoService.store(grupo);
		
		return this.findById(grupo.getIdGrupoRegiao());
	}

	public void generateGruposRegioesAlterandoTabelaPreco(Map params) throws Throwable{
		TabelaPreco tabelaPrecoToCopy = tabelaPrecoService.findByIdTabelaPreco(MapUtils.getLong(params, "idOldTabelaPreco"));
		TabelaPreco newTabelaPreco = tabelaPrecoService.findByIdTabelaPreco(MapUtils.getLong(params, "idTabelaPreco"));

		grupoRegiaoService.generateGruposRegioesAlterandoTabelaPreco(tabelaPrecoToCopy.getIdTabelaPreco(), newTabelaPreco.getIdTabelaPreco());
	}
	
	public ResultSetPage findPaginated(Map criteria) {
		ResultSetPage rsp = grupoRegiaoService.findPaginated(new PaginatedQuery(criteria));
		List<GrupoRegiao> listaReturned = rsp.getList();
		
		//Nova lista que ser� inserida no ResultSetPage acima
		List<Map<String,Object>> lista = new ArrayList<Map<String,Object>>();
		
		for (GrupoRegiao grupo : listaReturned) {
			TypedFlatMap tpm = new TypedFlatMap();					
			
			tpm.put("idGrupoRegiao", grupo.getIdGrupoRegiao().longValue());
			tpm.put("idUnidadeFederativa", grupo.getUnidadeFederativa().getIdUnidadeFederativa());
			tpm.put("sgUnidadeFederativa", grupo.getUnidadeFederativa().getSgUnidadeFederativa());
			tpm.put("dsGrupoRegiao", grupo.getDsGrupoRegiao());
			tpm.put("tabelaPrecoString", grupo.getTabelaPreco().getTabelaPrecoString());

			if(grupo.getTabelaPreco().getBlEfetivada()) {
				tpm.put("blEfetivada","/image/checked.gif");
			}
			
			//---- Nullable fields
			tpm.put("tpAjuste", grupo.getTpAjuste() != null ? grupo.getTpAjuste().getDescription() : "");
			tpm.put("tpValorAjuste", grupo.getTpValorAjuste() != null ? grupo.getTpValorAjuste().getDescription() : "");
			tpm.put("vlAjustePadrao", grupo.getVlAjustePadrao() != null ? grupo.getVlAjustePadrao() : "");
			tpm.put("vlAjusteMinimo", grupo.getVlAjusteMinimo() != null ? grupo.getVlAjusteMinimo() : "");
			
			//CQ 28633
			tpm.put("vlAjustePadraoAdvalorem", grupo.getVlAjustePadraoAdvalorem() != null ? grupo.getVlAjustePadraoAdvalorem() : "");
			tpm.put("vlAjusteMinimoAdvalorem", grupo.getVlAjusteMinimoAdvalorem() != null ? grupo.getVlAjusteMinimoAdvalorem() : "");
			tpm.put("tpAjusteAdvalorem", grupo.getTpAjusteAdvalorem() != null ? grupo.getTpAjusteAdvalorem().getDescription() : "");
			tpm.put("tpValorAjusteAdvalorem", grupo.getTpValorAjusteAdvalorem() != null ? grupo.getTpValorAjusteAdvalorem().getDescription() : "");
			//----
			
			lista.add(tpm);
	}
	
		rsp.setList(lista);
		
		return rsp;
	}
	
	public void removeById(Long id) {
		grupoRegiaoService.removeById(id);
	}
	
	@Override
	@ParametrizedAttribute(type=Long.class)
	public void removeByIds(List ids) {
		grupoRegiaoService.removeByIds(ids);
	}

	//Popula o combo UF
    public List findUnidadeFederativa(Map criteria) {
		if(criteria.get("idPais")!= null){
			Map pais = new HashMap();
			pais.put("idPais", criteria.get("idPais"));
			criteria.put("pais", pais);
    }	
    
		List lista = unidadeFederativaService.findLookup(criteria);
		
		if(!lista.isEmpty() && lista.size()==1){
			UnidadeFederativa uf = (UnidadeFederativa)lista.get(0);
			Map mapa = new HashMap();
			mapa.put("nmUnidadeFederativa", uf.getNmUnidadeFederativa());
			mapa.put("idUnidadeFederativa", uf.getIdUnidadeFederativa());
			mapa.put("sgUnidadeFederativa", uf.getSgUnidadeFederativa());
			if (uf.getMunicipio() != null) {
				mapa.put("idMunicipio", uf.getMunicipio().getIdMunicipio());
				mapa.put("nmMunicipio", uf.getMunicipio().getNmMunicipio());
			}
			lista.add(mapa);
			lista.remove(uf);
		}
		return lista;
	}		
    
	public List findLookupPais(Map criteria){
		List lista = paisService.findLookup(criteria);
		if(!lista.isEmpty() && lista.size()==1){
			Pais pais = (Pais)lista.get(0);
			Map mapa = new HashMap();
			mapa.put("nmPais", pais.getNmPais().toString());
			mapa.put("idPais", pais.getIdPais());
			lista.add(mapa);
			lista.remove(pais);
		}
		return lista;
	}
	
	public List findLookupMunicipio(Map criteria){
		
		if(criteria.get("idPais")!= null){
			Map idPais = new HashMap();
			Map pais = new HashMap();
			idPais.put("idPais", criteria.get("idPais"));
			pais.put("pais", idPais);
			criteria.put("unidadeFederativa", pais);
		}
		criteria.remove("idPais");
		
		if(criteria.get("idUnidadeFederativa")!= null){
			Map idUnidadeFederativa = new HashMap();
			idUnidadeFederativa.put("idUnidadeFederativa", criteria.get("idUnidadeFederativa"));
			criteria.put("unidadeFederativa", idUnidadeFederativa);
		}
		criteria.remove("idUnidadeFederativa");
		
		List lista = municipioService.findLookup(criteria);
		if(!lista.isEmpty() && lista.size()==1){
			Municipio municipio = (Municipio)lista.get(0);
			Map mapa = new HashMap();
			mapa.put("nmMunicipio", municipio.getNmMunicipio());
			mapa.put("idMunicipio", municipio.getIdMunicipio());
			lista.add(mapa);
			lista.remove(municipio);
		}
		return lista;
	}
	
	//Popula o combo Grupo Regiao
    public List findGruposRegioesCombo() {
		List<GrupoRegiao> grupo = grupoRegiaoService.findGruposRegiao();

		List ret = new ArrayList<GrupoRegiao>();
		for (GrupoRegiao grupoRegiao : grupo) {
			Map map = new HashMap();

			map.put("idGrupoRegiao", grupoRegiao.getIdGrupoRegiao());
			map.put("dsGrupoRegiao", grupoRegiao.getDsGrupoRegiao());

			ret.add(map);
	}

		return ret;
	}

	
	public List findTabelaPreco(Map criteria) {
		return tabelaPrecoService.findLookup(criteria);
	}
    
	//----- Getters und Setters --------//
	public void setUnidadeFederativaService(
			UnidadeFederativaService unidadeFederativaService) {
		this.unidadeFederativaService = unidadeFederativaService;
	}

	public void setTabelaPrecoService(TabelaPrecoService tabelaPrecoService) {
		this.tabelaPrecoService = tabelaPrecoService;
	}

	public void setGrupoRegiaoService(GrupoRegiaoService grupoRegiaoService) {
		this.grupoRegiaoService = grupoRegiaoService;
	}
	
	public void setPaisService(PaisService paisService) {
		this.paisService = paisService;
	}

	public void setMunicipioService(MunicipioService municipioService) {
		this.municipioService = municipioService;
}
}
