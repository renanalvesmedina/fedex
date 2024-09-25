package com.mercurio.lms.rest.fretecarreteiroviagem.manterrecibosdeoutrasempresas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.service.DomainValueService;
import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.adsm.rest.BaseCrudRest;
import com.mercurio.lms.annotation.InjectInJersey;
import com.mercurio.lms.configuracoes.model.Usuario;
import com.mercurio.lms.contratacaoveiculos.model.Proprietario;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.configuracoes.UsuarioDTO;
import com.mercurio.lms.rest.contratacaoveiculos.ProprietarioDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.tributos.model.DescontoInssCarreteiro;
import com.mercurio.lms.tributos.model.service.DescontoInssCarreteiroService;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.util.JTDateTimeUtils;

/**
 * Rest responsável pelo controle da tela manter recibo de outras empresas.
 * 
 * @author anoguez
 *
 */
@Path("/manterrecibosdeoutrasempresas/recibosDeOutrasEmpresas")
public class RecibosDeOutrasEmpresasRest extends BaseCrudRest<RecibosDeOutrasEmpresasDTO, RecibosDeOutrasEmpresasDTO, RecibosDeOutrasEmpresasFiltroDTO>{

	private static final String DM_TIPO_IDENTIFICACAO_PESSOA = "DM_TIPO_IDENTIFICACAO_PESSOA";
	@InjectInJersey

	private DescontoInssCarreteiroService descontoInssCarreteiroService;
	@InjectInJersey

	private DomainValueService domainValueService; 

	@Override
	protected RecibosDeOutrasEmpresasDTO findById(Long id) {
		DescontoInssCarreteiro descontoInssCarreteiro = descontoInssCarreteiroService.findById(id);
		
		RecibosDeOutrasEmpresasDTO recibo = new RecibosDeOutrasEmpresasDTO();
		
		if(descontoInssCarreteiro != null){
			
			recibo.setId(descontoInssCarreteiro.getIdDescontoInssCarreteiro());
			
			ProprietarioDTO proprietario = new ProprietarioDTO();
			proprietario.setId(descontoInssCarreteiro.getProprietario().getIdProprietario());
			proprietario.setIdProprietario(descontoInssCarreteiro.getProprietario().getIdProprietario());
			proprietario.setTpProprietario(descontoInssCarreteiro.getProprietario().getTpProprietario());
			proprietario.setNrIdentificacao(FormatUtils.formatIdentificacao(descontoInssCarreteiro.getProprietario().getPessoa()));
			proprietario.setNmPessoa(descontoInssCarreteiro.getProprietario().getPessoa().getNmPessoa());
			recibo.setProprietario(proprietario);
			
			recibo.setNrRecibo(descontoInssCarreteiro.getNrRecibo());
			recibo.setVlInss(descontoInssCarreteiro.getVlInss());
			recibo.setVlRemuneracao(descontoInssCarreteiro.getVlRemuneracao());
			recibo.setDhInclusao(descontoInssCarreteiro.getDhInclusao());			
			
			UsuarioDTO usuario = new UsuarioDTO();
			usuario.setId(descontoInssCarreteiro.getUsuario().getIdUsuario());
			usuario.setIdUsuario(descontoInssCarreteiro.getUsuario().getIdUsuario());
			usuario.setNmUsuario(descontoInssCarreteiro.getUsuario().getNmUsuario());
			recibo.setUsuario(usuario);

			recibo.setDtEmissaoRecibo(descontoInssCarreteiro.getDtEmissaoRecibo());
			
			recibo.setDsEmpregador(descontoInssCarreteiro.getDsEmpresa());
			// O tipo identificador é facultativo
			if(descontoInssCarreteiro.getTpIdentificacao() != null) {
				recibo.setTpIdentificacaoEmpregador(getTpIdentificacaoMap(descontoInssCarreteiro));
			}
			
			// O número identificador é facultativo
			if(descontoInssCarreteiro.getNrIdentEmpregador() != null) {
				recibo.setNrIdentificacaoEmpregador(descontoInssCarreteiro.getNrIdentEmpregador());
			}
			
			// A filial é facultativa
			if (descontoInssCarreteiro.getFilial() != null) {
				FilialSuggestDTO filial = new FilialSuggestDTO();
				filial.setIdFilial(descontoInssCarreteiro.getFilial().getIdFilial());
				filial.setId(descontoInssCarreteiro.getFilial().getIdFilial());
				filial.setSgFilial(descontoInssCarreteiro.getFilial().getSgFilial());
				filial.setNmFilial(descontoInssCarreteiro.getFilial().getPessoa().getNmFantasia());
				recibo.setFilial(filial);
			}			
		}
		
		return recibo;
	}

	@Override
	protected void removeById(Long arg0) {
		descontoInssCarreteiroService.removeById(arg0);
	}

	@Override
	protected void removeByIds(List<Long> arg0) {
		descontoInssCarreteiroService.removeByIds(arg0);
	}

	@Override
	protected Long store(RecibosDeOutrasEmpresasDTO bean) {		
		
		DescontoInssCarreteiro descontoInssCarreteiro = new DescontoInssCarreteiro();		
		
		if(bean.getId() != null){
			descontoInssCarreteiro =  descontoInssCarreteiroService.findById(bean.getId());
		}
		
		// Set Proprietario
		Proprietario proprietario = new Proprietario();
		proprietario.setIdProprietario(bean.getProprietario().getIdProprietario());
		descontoInssCarreteiro.setProprietario(proprietario);
		
		// Set Filial
		Filial filial = new Filial();
		filial.setIdFilial(bean.getFilial().getIdFilial());
		descontoInssCarreteiro.setFilial(filial);		
		
		descontoInssCarreteiro.setDtEmissaoRecibo(bean.getDtEmissaoRecibo());
		
		// se for uma edição não pode alterar a data e hora de inclusão
		if(bean.getDhInclusao() == null) {
			descontoInssCarreteiro.setDhInclusao(JTDateTimeUtils.getDataHoraAtual());
		}
		
		descontoInssCarreteiro.setVlInss(bean.getVlInss());		
		descontoInssCarreteiro.setNrRecibo(bean.getNrRecibo());
		descontoInssCarreteiro.setVlRemuneracao(bean.getVlRemuneracao());
		
		descontoInssCarreteiro.setDsEmpresa((String.valueOf(bean.getDsEmpregador())));
		descontoInssCarreteiro.setTpIdentificacao(getDomainValue(bean.getTpIdentificacaoEmpregador()));
		descontoInssCarreteiro.setNrIdentEmpregador(bean.getNrIdentificacaoEmpregador());
		
		// LMS-5590			
		// Set Usuário logado	
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(bean.getUsuario().getIdUsuario());
		descontoInssCarreteiro.setUsuario(usuario);
		
		descontoInssCarreteiroService.store(descontoInssCarreteiro);
		
		return descontoInssCarreteiro.getIdDescontoInssCarreteiro();
	}
	
	@Override
	protected List<RecibosDeOutrasEmpresasDTO> find(RecibosDeOutrasEmpresasFiltroDTO filter) {
		List<Map<String, Object>> list = descontoInssCarreteiroService.findRecibosOutrasEmpresas(getTypedFlatMap(filter));
		return convertToRecibosDeOutrasEmpresasDTO(list);
	}

	@Override
	protected Integer count(RecibosDeOutrasEmpresasFiltroDTO filter) {
		return descontoInssCarreteiroService.getRowCount(getTypedFlatMap(filter));
	}
	
	private TypedFlatMap getTypedFlatMap(RecibosDeOutrasEmpresasFiltroDTO filter) {
		
		TypedFlatMap toReturn = super.getTypedFlatMapWithPaginationInfo(filter);
		
		if (filter.getProprietario() != null) {
			toReturn.put("proprietario.idProprietario", filter.getProprietario().getIdProprietario());
		}
		if (filter.getFilial() != null) {
			toReturn.put("filial.idFilial", filter.getFilial().getIdFilial());
		}
		
		toReturn.put("dsEmpresa", filter.getDsEmpresa());
		toReturn.put("tipoIdentificadorEmpregador.dsIdentificador", filter.getTipoIdentificadorEmpregador() == null
				? null : filter.getTipoIdentificadorEmpregador().getValue());
		toReturn.put("nrEmpregador", filter.getNrEmpregador());
		toReturn.put("nrRecibo", filter.getNrRecibo());
		toReturn.put("vlInss", filter.getVlInss());
		toReturn.put("dtEmissaoReciboInicial", formataData(filter.getDtEmissaoReciboInicial()));
		toReturn.put("dtEmissaoReciboFinal", formataData(filter.getDtEmissaoReciboFinal()));
		
		return toReturn;
	}
	
	private List<RecibosDeOutrasEmpresasDTO> convertToRecibosDeOutrasEmpresasDTO (List<Map<String, Object>> list) {
		
		List<RecibosDeOutrasEmpresasDTO> recibos = new ArrayList<RecibosDeOutrasEmpresasDTO>();
		
		for (Map<String, Object> map : list) {
			
			RecibosDeOutrasEmpresasDTO recibo = new RecibosDeOutrasEmpresasDTO();
			
			recibo.setId((Long)map.get("idDescontoInssCarreteiro"));
			
			ProprietarioDTO proprietario = new ProprietarioDTO();
			proprietario.setNrIdentificacao((String)map.get("nrIdentificacao"));
			proprietario.setNmPessoa((String)map.get("nmProprietario"));
			recibo.setProprietario(proprietario);
			
			recibo.setDsEmpregador((String)map.get("dsEmpresa"));
			if (map.containsKey("tpIdentificador")) {
				Map<String, Object> tpIdentificacaoEmpregador = new HashMap<String, Object>();
				tpIdentificacaoEmpregador.put("tpIdentificacaoEmpregador", (String)map.get("tpIdentificador"));
				recibo.setTpIdentificacaoEmpregador(tpIdentificacaoEmpregador);
			}
			recibo.setNrIdentificacaoEmpregador((String)map.get("nrIdentificador"));
			
			
			recibo.setDtEmissaoRecibo((YearMonthDay) map.get("dtEmissaoRecibo"));
			recibo.setNrRecibo((String)map.get("nrRecibo"));
			recibo.setVlInss((BigDecimal)map.get("vlInss"));
			recibo.setVlRemuneracao((BigDecimal)map.get("vlRemuneracao"));
			
			if (map.containsKey("sgFilial")) {
				FilialSuggestDTO filial = new FilialSuggestDTO();
				filial.setSgFilial((String)map.get("sgFilial"));
				filial.setNmFilial((String)map.get("nmFilial"));
				recibo.setFilial(filial);
			}
			
			recibos.add(recibo);
		}
		
		return recibos;
	}
	
	/**
	 * LMS-5590
	 * 
	 * formata a data para o padrão de inserção na base de dados
	 * @param data
	 * @return
	 */
	private YearMonthDay formataData(String data) {
		
		if(data == null)
			return null;
		
		if(data.contains("/")) {
			String split[] = data.split("/");
			return new YearMonthDay(split[2]+"-"+split[1]+"-"+split[0]);
		}
		
		return new YearMonthDay(data);
	}
	
	/**
	 * Compõe um DomainValue para uma entidade. 
	 * 
	 * @param values
	 * @return DomainValue
	 */
	@SuppressWarnings("unchecked")
	private DomainValue getDomainValue(Object values) {		
		if(values == null){
			return null;
		}
		
		Map<String, Object> domainValues = (Map<String, Object>) values;
		
		return domainValueService.findDomainValueByValue(DM_TIPO_IDENTIFICACAO_PESSOA, domainValues.get("dsIdentificador").toString());		
	}
	

	/**
	 * LMS-5590
	 * 
	 * Método responsável por retornar o domínio utilizado na combobox de tipo do identificador do empregador
	 * @param descontoInssCarreteiro
	 * @return
	 */
	private Map<String, Object> getTpIdentificacaoMap(DescontoInssCarreteiro descontoInssCarreteiro) {
		Map<String, Object> tpIdentificacao = new HashMap<String, Object>();
		
		if(descontoInssCarreteiro.getTpIdentificacao() != null) {
			List domainName = new ArrayList<String>(); 
			domainName.add(descontoInssCarreteiro.getTpIdentificacao().getValue());
			List<DomainValue> domain = domainValueService.findByDomainNameAndValues(DM_TIPO_IDENTIFICACAO_PESSOA, domainName);
			
			tpIdentificacao.put("idIdentificador", domain.get(0).getId());
			tpIdentificacao.put("dsIdentificador", domain.get(0).getValue());
		}
		
		return tpIdentificacao;
	}
}
