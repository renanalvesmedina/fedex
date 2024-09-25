package com.mercurio.lms.rest.sgr;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.lms.carregamento.model.ControleCarga;
import com.mercurio.lms.configuracoes.model.Pessoa;
import com.mercurio.lms.contratacaoveiculos.model.MeioTransporte;
import com.mercurio.lms.contratacaoveiculos.model.Motorista;
import com.mercurio.lms.municipios.model.Aeroporto;
import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.municipios.model.RotaColetaEntrega;
import com.mercurio.lms.rest.carregamento.dto.ControleCargaSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MeioTransporteSuggestDTO;
import com.mercurio.lms.rest.contratacaoveiculos.dto.MotoristaSuggestDTO;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.AeroportoSuggestDTO;
import com.mercurio.lms.rest.municipios.dto.RotaColetaEntregaSuggestDTO;
import com.mercurio.lms.rest.vendas.dto.ClienteSuggestDTO;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;

public abstract class BaseDTOBuilder {

	protected static AeroportoSuggestDTO buildAeroportoSuggestDTO(Aeroporto bean) {
		AeroportoSuggestDTO dto = new AeroportoSuggestDTO();
		dto.setId(bean.getIdAeroporto());
		dto.setSgAeroporto(bean.getSgAeroporto());
		dto.setNmAeroporto(bean.getPessoa().getNmPessoa());
		return dto;
	}

	protected static ClienteSuggestDTO buildClienteSuggestDTO(Cliente bean) {
		ClienteSuggestDTO dto = new ClienteSuggestDTO();
		dto.setIdCliente(bean.getIdCliente());
		dto.setNmPessoa(bean.getPessoa().getNmPessoa());
		dto.setNrIdentificacao(FormatUtils.formatIdentificacao(bean.getPessoa()));
		return dto;
	}

	protected static ControleCargaSuggestDTO buildControleCargaSuggestDTO(ControleCarga bean) {
		ControleCargaSuggestDTO dto = new ControleCargaSuggestDTO();
		dto.setId(bean.getIdControleCarga());
		dto.setNrControleCarga(bean.getNrControleCarga());
		dto.setDhGeracao(bean.getDhGeracao());
		dto.setSgFilial(bean.getFilialByIdFilialOrigem().getSgFilial());
		return dto;
	}

	protected static FilialSuggestDTO buildFilialSuggestDTO(Filial bean) {
		FilialSuggestDTO dto = new FilialSuggestDTO();
		dto.setId(bean.getIdFilial());
		dto.setSgFilial(bean.getSgFilial());
		dto.setNmFilial(bean.getPessoa().getNmFantasia());
		return dto;
	}

	protected static MeioTransporteSuggestDTO buildMeioTransporteSuggestDTO(MeioTransporte bean) {
		MeioTransporteSuggestDTO dto = new MeioTransporteSuggestDTO();
		dto.setId(bean.getIdMeioTransporte());
		dto.setNrIdentificador(bean.getNrIdentificador());
		dto.setNrFrota(bean.getNrFrota());
		return dto;
	}

	protected static MotoristaSuggestDTO buildMotoristaSuggestDTO(Motorista bean) {
		MotoristaSuggestDTO dto = new MotoristaSuggestDTO();
		dto.setId(bean.getIdMotorista());
		dto.setNrIdentificacao(FormatUtils.formatIdentificacao(bean.getPessoa()));
		dto.setNmPessoa(bean.getPessoa().getNmPessoa());
		return dto;
	}

	protected static Pessoa buildPessoa(String nrIdentificacao, String nmPessoa) {
		Pessoa bean = new Pessoa();
		bean.setTpIdentificacao(new DomainValue("CNPJ"));
		bean.setNrIdentificacao(nrIdentificacao.replaceAll("\\D", ""));
		bean.setNmPessoa(nmPessoa);
		bean.setTpPessoa(new DomainValue("J"));
		return bean;
	}

	protected static RotaColetaEntregaSuggestDTO buildRotaColetaEntregaSuggestDTO(RotaColetaEntrega bean) {
		RotaColetaEntregaSuggestDTO dto = new RotaColetaEntregaSuggestDTO();
		dto.setId(bean.getIdRotaColetaEntrega());
		dto.setNrRota(bean.getNrRota());
		dto.setDsRota(bean.getDsRota());
		dto.setNrKm(bean.getNrKm());
		return dto;
	}

}
