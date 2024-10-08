package com.mercurio.lms.contratacaoveiculos.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.mercurio.adsm.framework.report.ReportActionSupport;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTranspRodoMotorista;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporteRodoviario;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspProprietarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTranspRodoMotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteRodoviarioService;
import com.mercurio.lms.contratacaoveiculos.model.service.MeioTransporteService;
import com.mercurio.lms.contratacaoveiculos.model.service.MotoristaService;
import com.mercurio.lms.contratacaoveiculos.model.service.ProprietarioService;
import com.mercurio.lms.contratacaoveiculos.report.EmitirFichaCadastroReguladoraService;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.service.FilialService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;
import com.mercurio.lms.util.session.SessionUtils;
import com.mercurio.lms.vendas.model.service.SegmentoMercadoService;


/**
 * Generated by: ADSM ActionGenerator
 *  
 * N�o inserir documenta��o ap�s ou remover a tag do XDoclet a seguir.
 * O valor do <code>id</code> informado abaixo deve ser utilizado para referenciar este servi�o.
 * @spring.bean id="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction"
 */

public class EmitirFichaCadastroReguladoraAction extends ReportActionSupport {
	
	private MotoristaService motoristaService;
	private ProprietarioService proprietarioService;
	private MeioTransporteRodoviarioService meioTransporteRodoviarioService;
	private MeioTransporteService meioTransporteService;
	private MeioTranspProprietarioService meioTranspProprietarioService;
	private EmitirFichaCadastroReguladoraService emitirFichaCadastroReguladoraService;
	private SegmentoMercadoService segmentoMercadoService;
	private FilialService filialService;
	private MeioTranspRodoMotoristaService meioTranspRodoMotoristaService;
	
	public MeioTranspProprietarioService getMeioTranspProprietarioService() {
		return meioTranspProprietarioService;
	}

	public void setMeioTranspProprietarioService(
			MeioTranspProprietarioService meioTranspProprietarioService) {
		this.meioTranspProprietarioService = meioTranspProprietarioService;
	}

	public MeioTransporteRodoviarioService getMeioTransporteRodoviarioService() {
		return meioTransporteRodoviarioService;
	}

	public void setMeioTransporteRodoviarioService(
			MeioTransporteRodoviarioService meioTransporteRodoviarioService) {
		this.meioTransporteRodoviarioService = meioTransporteRodoviarioService;
	}
	
	public ProprietarioService getProprietarioService() {
		return proprietarioService;
	}

	public void setProprietarioService(ProprietarioService proprietarioService) {
		this.proprietarioService = proprietarioService;
	}

	public MotoristaService getMotoristaService() {
		return motoristaService;
	}

	public void setMotoristaService(MotoristaService motoristaService) {
		this.motoristaService = motoristaService;
	}

	public EmitirFichaCadastroReguladoraService getEmitirFichaCadastroReguladoraService() {
		return emitirFichaCadastroReguladoraService;
	}

	public void setEmitirFichaCadastroReguladoraService(EmitirFichaCadastroReguladoraService emitirFichaCadastroReguladoraService) {
		this.reportServiceSupport = emitirFichaCadastroReguladoraService;
	}
	
	public List findLookupMotorista(TypedFlatMap criteria) {
		return getMotoristaService().findLookupAsPaginated(criteria);
	}
	
	public List findLookupProprietario(Map criteria) {
		return getProprietarioService().findLookup(criteria);
	}
	
	private Map findInfoMeioTransporte(Long id) {
		Map map1 = new HashMap();
		Map map2 = new HashMap();
		TypedFlatMap mapNovo = new TypedFlatMap();
		
		map1 = getMeioTransporteService().findInfoMeioTransporte(id);
		mapNovo.putAll(map1);
		mapNovo.put("idMeioTransporte",id);
		
		map2 = getMeioTranspProprietarioService().findProprietarioByMeioTransporte(id);
		if(map2 != null && !map2.isEmpty()) {
			String tpIdent = ((Map)map2.get("tpIdentificacao")).get("value").toString();
			String nrIdent= FormatUtils.formatIdentificacao(tpIdent,map2.get("nrIdentificacao").toString());
			((Map)((Map)map2.get("proprietario")).get("pessoa")).put("nrIdentificacao",nrIdent);
			mapNovo.putAll(map2);
		}
		return mapNovo;
	}
	
	public TypedFlatMap findInfoUsuarioLogado() {
		Filial filial = SessionUtils.getFilialSessao();
		TypedFlatMap retorno = new TypedFlatMap();
		retorno.put("filialO.idFilial",filial.getIdFilial());
		retorno.put("filialO.sgFilial",filial.getSgFilial());
		retorno.put("filialO.pessoa.nmFantasia",filial.getPessoa().getNmFantasia());
		return retorno;
	}

	public List findLookupFilial(Map criterias) {
		return filialService.findLookupFilial(criterias);
	}

	public List findComboSegmentoMercado(Map criteria) {
		return segmentoMercadoService.find(criteria); 
	}

	public List findLookupMeioTransporteRodoviario(Map criteria) {
		Map<String, Object> meioTransporte = MapUtils.getMap(criteria, "meioTransporte");
		String nrFrota = MapUtils.getString(meioTransporte, "nrFrota");
		if(StringUtils.isNotBlank(nrFrota)) {
			meioTransporte.put("nrFrota", FormatUtils.formatNrFrota(nrFrota));
		}
		List result = getMeioTransporteRodoviarioService().findLookup(criteria);
		if (result.size() == 1) {
			Long id = ((MeioTransporteRodoviario)result.get(0)).getIdMeioTransporte();
			result.clear();
			
			if (id!= null)
				result.add(findInfoMeioTransporte(id));
		}
		return result;
	}

	public List findLookupSemiReboque(Map criteria) {
		List result = getMeioTransporteRodoviarioService().findLookup(criteria);
		if (result.size() == 1) {
			Long id = ((MeioTransporteRodoviario)result.get(0)).getIdMeioTransporte();
			result.clear();
			
			if (id!= null)
				result.add(findInfoMeioTransporte(id));
		}
		return result;
	}
	
	public TypedFlatMap findMotoristaByMeioTransporte(TypedFlatMap criterias) {
		Long idMeioTransporte = criterias.getLong("idMeioTransporte");
		if (idMeioTransporte != null) {
			MeioTranspRodoMotorista mtrm = meioTranspRodoMotoristaService.findRelacaoVigente(null,idMeioTransporte,JTDateTimeUtils.getDataAtual());
			if (mtrm != null) {
				Motorista motorista = mtrm.getMotorista();
				Pessoa pessoa = motorista.getPessoa();
				TypedFlatMap result = new TypedFlatMap();
				result.put("motorista.idMotorista",motorista.getIdMotorista());
				result.put("motorista.pessoa.nmPessoa",pessoa.getNmPessoa());
				String nrIdentificacao = FormatUtils.formatIdentificacao(
						pessoa.getTpIdentificacao(),pessoa.getNrIdentificacao());
				result.put("motorista.pessoa.nrIdentificacao",nrIdentificacao);
				result.put("motorista.pessoa.nrIdentificacaoFormatado",nrIdentificacao);
				return result;
			}
		}
		return null;		
	}
	
	
	public MeioTransporteService getMeioTransporteService() {
		return meioTransporteService;
	}
	
	public void setMeioTransporteService(MeioTransporteService meioTransporteService) {
		this.meioTransporteService = meioTransporteService;
	}

	public void setSegmentoMercadoService(
			SegmentoMercadoService segmentoMercadoService) {
		this.segmentoMercadoService = segmentoMercadoService;
	}

	public void setFilialService(FilialService filialService) {
		this.filialService = filialService;
	}

	public void setMeioTranspRodoMotoristaService(
			MeioTranspRodoMotoristaService meioTranspRodoMotoristaService) {
		this.meioTranspRodoMotoristaService = meioTranspRodoMotoristaService;
	}
	
}
