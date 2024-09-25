package com.mercurio.lms.rest.sgr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mercurio.lms.municipios.model.Filial;
import com.mercurio.lms.rest.municipios.FilialSuggestDTO;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaDTO;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaImpedidoDTO;
import com.mercurio.lms.rest.sgr.dto.FornecedorEscoltaSuggestDTO;
import com.mercurio.lms.rest.sgr.dto.FranquiaFornecedorEscoltaDTO;
import com.mercurio.lms.rest.sgr.dto.RankingFornecedorEscoltaDTO;
import com.mercurio.lms.sgr.model.FornecEscoltaRankingItem;
import com.mercurio.lms.sgr.model.FornecedorEscolta;
import com.mercurio.lms.sgr.model.FornecedorEscoltaImpedido;
import com.mercurio.lms.sgr.model.FornecedorEscoltaRanking;
import com.mercurio.lms.sgr.model.FranquiaFornecedorEscolta;
import com.mercurio.lms.sgr.model.FranquiaFornecedorFilialAtendimento;
import com.mercurio.lms.util.FormatUtils;
import com.mercurio.lms.vendas.model.Cliente;

public final class FornecedorEscoltaDTOBuilder extends BaseDTOBuilder {

	public static List<FornecedorEscoltaDTO> convertFornecedorEscoltaDTO(List<FornecedorEscolta> list) {
		List<FornecedorEscoltaDTO> dtos = new ArrayList<FornecedorEscoltaDTO>();
		for (FornecedorEscolta bean : list) {
			dtos.add(buildFornecedorEscoltaDTO(bean));
		}
		return dtos;
	}

	public static FornecedorEscoltaDTO buildFornecedorEscoltaDTO(FornecedorEscolta bean) {
		FornecedorEscoltaDTO dto = new FornecedorEscoltaDTO();
		dto.setId(bean.getIdFornecedorEscolta());
		dto.setNrIdentificacao(FormatUtils.formatIdentificacao(bean.getPessoa()));
		dto.setNmPessoa(bean.getPessoa().getNmPessoa());
		dto.setDtVigenciaInicial(bean.getDtVigenciaInicial());
		dto.setDtVigenciaFinal(bean.getDtVigenciaFinal());
		dto.setDsEmailFornecedor(bean.getDsEmailFornecedor());
		dto.setDsTelefone1(bean.getDsTelefone1());
		dto.setDsTelefone2(bean.getDsTelefone2());
		dto.setDsTelefone3(bean.getDsTelefone3());
		return dto;
	}

	public static FornecedorEscolta buildFornecedorEscolta(FornecedorEscoltaDTO dto) {
		FornecedorEscolta bean = new FornecedorEscolta();
		bean.setIdFornecedorEscolta(dto.getId());
		bean.setPessoa(buildPessoa(dto.getNrIdentificacao(), dto.getNmPessoa()));
		bean.setDtVigenciaInicial(dto.getDtVigenciaInicial());
		bean.setDtVigenciaFinal(dto.getDtVigenciaFinal());
		bean.setDsEmailFornecedor(dto.getDsEmailFornecedor());
		bean.setDsTelefone1(dto.getDsTelefone1());
		bean.setDsTelefone2(dto.getDsTelefone2());
		bean.setDsTelefone3(dto.getDsTelefone3());
		return bean;
	}

	public static FornecedorEscolta buildFornecedorEscolta(FornecedorEscoltaSuggestDTO dto) {
		FornecedorEscolta bean = new FornecedorEscolta();
		bean.setIdFornecedorEscolta(dto.getId());
		bean.setPessoa(buildPessoa(dto.getNrIdentificacao(), dto.getNmPessoa()));
		return bean;
	}

	public static FornecedorEscolta buildFornecedorEscolta(Long id) {
		FornecedorEscolta bean = new FornecedorEscolta();
		bean.setIdFornecedorEscolta(id);
		return bean;
	}

	public static List<FornecedorEscoltaSuggestDTO> convertFornecedorEscoltaSuggestDTO(List<FornecedorEscolta> list) {
		List<FornecedorEscoltaSuggestDTO> dtos = new ArrayList<FornecedorEscoltaSuggestDTO>();
		for (FornecedorEscolta fornecedor : list) {
			dtos.add(buildFornecedorEscoltaSuggestDTO(fornecedor));
		}
		return dtos;
	}

	public static FornecedorEscoltaSuggestDTO buildFornecedorEscoltaSuggestDTO(FornecedorEscolta bean) {
		FornecedorEscoltaSuggestDTO dto = new FornecedorEscoltaSuggestDTO();
		dto.setId(bean.getIdFornecedorEscolta());
		dto.setNrIdentificacao(FormatUtils.formatIdentificacao(bean.getPessoa()));
		dto.setNmPessoa(bean.getPessoa().getNmPessoa());
		return dto;
	}

	public static List<FranquiaFornecedorEscoltaDTO> convertFranquiaFornecedorEscoltaDTO(List<FranquiaFornecedorEscolta> list) {
		List<FranquiaFornecedorEscoltaDTO> dtos = new ArrayList<FranquiaFornecedorEscoltaDTO>();
		for (FranquiaFornecedorEscolta bean : list) {
			bean.setFiliaisAtendimento(null);
			dtos.add(buildFranquiaFornecedorEscoltaDTO(bean));
		}
		return dtos;
	}

	public static FranquiaFornecedorEscoltaDTO buildFranquiaFornecedorEscoltaDTO(FranquiaFornecedorEscolta bean) {
		FranquiaFornecedorEscoltaDTO dto = new FranquiaFornecedorEscoltaDTO();
		dto.setId(bean.getIdFranquiaFornecedorEscolta());
		dto.setIdFornecedorEscolta(bean.getFornecedorEscolta().getIdFornecedorEscolta());
		dto.setTpEscolta(bean.getTpEscolta());
		dto.setNrQuilometragem(bean.getNrQuilometragem());
		dto.setNrTempoViagemMinutos(bean.getNrTempoViagemMinutos());
		dto.setVlFranquia(bean.getVlFranquia());
		dto.setDtVigenciaInicial(bean.getDtVigenciaInicial());
		dto.setDtVigenciaFinal(bean.getDtVigenciaFinal());
		dto.setVlHoraExcedente(bean.getVlHoraExcedente());
		dto.setVlQuilometroExcedente(bean.getVlQuilometroExcedente());
		if (bean.getFiliaisAtendimento() != null) {
			List<FilialSuggestDTO> filiaisAtendimento = new ArrayList<FilialSuggestDTO>();
			for (FranquiaFornecedorFilialAtendimento atendimento : bean.getFiliaisAtendimento()) {
				filiaisAtendimento.add(buildFilialSuggestDTO(atendimento.getFilial()));
			}
			dto.setFiliaisAtendimento(filiaisAtendimento);
		}
		if (bean.getFilialOrigem() != null) {
			dto.setFilialOrigem(buildFilialSuggestDTO(bean.getFilialOrigem()));
		}
		if (bean.getFilialDestino() != null) {
			dto.setFilialDestino(buildFilialSuggestDTO(bean.getFilialDestino()));
		}
		return dto;
	}

	public static FranquiaFornecedorEscolta buildFranquiaFornecedorEscolta(FranquiaFornecedorEscoltaDTO dto) {
		FranquiaFornecedorEscolta bean = new FranquiaFornecedorEscolta();
		bean.setIdFranquiaFornecedorEscolta(dto.getId());
		bean.setFornecedorEscolta(buildFornecedorEscolta(dto.getIdFornecedorEscolta()));
		bean.setTpEscolta(dto.getTpEscolta());
		bean.setNrQuilometragem(dto.getNrQuilometragem());
		bean.setNrTempoViagemMinutos(dto.getNrTempoViagemMinutos());
		bean.setVlFranquia(dto.getVlFranquia());
		bean.setDtVigenciaInicial(dto.getDtVigenciaInicial());
		bean.setDtVigenciaFinal(dto.getDtVigenciaFinal());
		bean.setVlHoraExcedente(dto.getVlHoraExcedente());
		bean.setVlQuilometroExcedente(dto.getVlQuilometroExcedente());
		List<FranquiaFornecedorFilialAtendimento> filiaisAtendimento = new ArrayList<FranquiaFornecedorFilialAtendimento>();
		for (FilialSuggestDTO filial : dto.getFiliaisAtendimento()) {
			FranquiaFornecedorFilialAtendimento atendimento = new FranquiaFornecedorFilialAtendimento();
			atendimento.setFranquiaFornecedorEscolta(bean);
			atendimento.setFilial(new Filial(filial.getId()));
			filiaisAtendimento.add(atendimento);
		}
		bean.setFiliaisAtendimento(filiaisAtendimento);
		if (dto.getFilialOrigem() != null) {
			bean.setFilialOrigem(new Filial(dto.getFilialOrigem().getId()));
		}
		if (dto.getFilialDestino() != null) {
			bean.setFilialDestino(new Filial(dto.getFilialDestino().getId()));
		}
		return bean;
	}

	public static List<FornecedorEscoltaImpedidoDTO> convertFornecedorEscoltaImpedidoDTO(List<FornecedorEscoltaImpedido> list) {
		List<FornecedorEscoltaImpedidoDTO> dtos = new ArrayList<FornecedorEscoltaImpedidoDTO>();
		for (FornecedorEscoltaImpedido bean : list) {
			dtos.add(buildFornecedorEscoltaImpedidoDTO(bean));
		}
		return dtos;
	}

	public static FornecedorEscoltaImpedidoDTO buildFornecedorEscoltaImpedidoDTO(FornecedorEscoltaImpedido bean) {
		FornecedorEscoltaImpedidoDTO dto = new FornecedorEscoltaImpedidoDTO();
		dto.setId(bean.getIdFornecedorEscoltaImpedido());
		dto.setIdFornecedorEscolta(bean.getFornecedorEscolta().getIdFornecedorEscolta());
		dto.setCliente(buildClienteSuggestDTO(bean.getCliente()));
		dto.setDtVigenciaInicial(bean.getDtVigenciaInicial());
		dto.setDtVigenciaFinal(bean.getDtVigenciaFinal());
		return dto;
	}

	public static FornecedorEscoltaImpedido buildFornecedorEscoltaImpedido(FornecedorEscoltaImpedidoDTO dto) {
		FornecedorEscoltaImpedido bean = new FornecedorEscoltaImpedido();
		bean.setIdFornecedorEscoltaImpedido(dto.getId());
		FornecedorEscolta fornecedor = new FornecedorEscolta();
		fornecedor.setIdFornecedorEscolta(dto.getIdFornecedorEscolta());
		bean.setFornecedorEscolta(fornecedor);
		bean.setCliente(new Cliente(dto.getCliente().getId()));
		bean.setDtVigenciaInicial(dto.getDtVigenciaInicial());
		bean.setDtVigenciaFinal(dto.getDtVigenciaFinal());
		return bean;
	}

	public static List<RankingFornecedorEscoltaDTO> convertRankingFornecedorEscoltaDTO(List<FornecedorEscoltaRanking> list) {
		List<RankingFornecedorEscoltaDTO> dtos = new ArrayList<RankingFornecedorEscoltaDTO>();
		for (FornecedorEscoltaRanking bean : list) {
			dtos.add(buildRankingFornecedorEscoltaTableDTO(bean));
		}
		return dtos;
	}

	public static RankingFornecedorEscoltaDTO buildRankingFornecedorEscoltaDTO(FornecedorEscoltaRanking bean) {
		RankingFornecedorEscoltaDTO dto = buildRankingFornecedorEscoltaTableDTO(bean);
		if (bean.getFornecedoresEscoltaRankingItem() != null) {
			dto.setFornecedoresEscolta(convertFornecEscoltaRankingItem(bean.getFornecedoresEscoltaRankingItem()));
		}
		return dto;
	}
	
	public static RankingFornecedorEscoltaDTO buildRankingFornecedorEscoltaTableDTO(FornecedorEscoltaRanking bean) {
		RankingFornecedorEscoltaDTO dto = new RankingFornecedorEscoltaDTO();
		dto.setId(bean.getIdFornecedorEscoltaRanking());
		if (bean.getCliente() != null) {
			dto.setCliente(buildClienteSuggestDTO(bean.getCliente()));
		}
		if (bean.getFilial() != null) {
			dto.setFilial(buildFilialSuggestDTO(bean.getFilial()));
		}
		dto.setDtVigenciaInicial(bean.getDtVigenciaInicial());
		dto.setDtVigenciaFinal(bean.getDtVigenciaFinal());
		return dto;
	}

	public static List<FornecedorEscoltaSuggestDTO> convertFornecEscoltaRankingItem(List<FornecEscoltaRankingItem> list) {
		List<FornecedorEscoltaSuggestDTO> dtos = new ArrayList<FornecedorEscoltaSuggestDTO>();
		for (FornecEscoltaRankingItem bean : list) {
			dtos.add(buildFornecedorEscoltaSuggestDTO(bean.getFornecedorEscolta()));
		}
		return dtos;
	}

	public static FornecedorEscoltaRanking buildFornecedorEscoltaRanking(RankingFornecedorEscoltaDTO dto) {
		FornecedorEscoltaRanking bean = new FornecedorEscoltaRanking();
		bean.setIdFornecedorEscoltaRanking(dto.getId());
		if (dto.getCliente() != null) {
			bean.setCliente(new Cliente(dto.getCliente().getIdCliente()));
		}
		if (dto.getFilial() != null) {
			bean.setFilial(new Filial(dto.getFilial().getId()));
		}
		bean.setDtVigenciaInicial(dto.getDtVigenciaInicial());
		bean.setDtVigenciaFinal(dto.getDtVigenciaFinal());
		if (!CollectionUtils.isEmpty(dto.getFornecedoresEscolta())) {
			bean.setFornecedoresEscoltaRankingItem(convertFornecEscoltaRankingItem(dto.getFornecedoresEscolta(), bean));
		}
		return bean;
	}

	private static List<FornecEscoltaRankingItem> convertFornecEscoltaRankingItem(
			List<FornecedorEscoltaSuggestDTO> list, FornecedorEscoltaRanking bean) {
		List<FornecEscoltaRankingItem> fornecedores = new ArrayList<FornecEscoltaRankingItem>();
		Long ordem = 0L;
		for (FornecedorEscoltaSuggestDTO dto : list) {
			FornecEscoltaRankingItem item = new FornecEscoltaRankingItem();
			FornecedorEscolta fornecedorEscolta = buildFornecedorEscolta(dto);
			item.setFornecedorEscolta(fornecedorEscolta);
			item.setFornecedorEscoltaRanking(bean);
			item.setNrOrdem(++ordem);
			fornecedores.add(item);
		}
		return fornecedores;
	}

	private FornecedorEscoltaDTOBuilder() {
		throw new AssertionError();
	}

}
