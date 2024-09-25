package com.mercurio.lms.franqueados.model.service.calculo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.joda.time.YearMonthDay;

import com.mercurio.adsm.framework.util.TypedFlatMap;
import com.mercurio.lms.franqueados.ConstantesFranqueado;
import com.mercurio.lms.franqueados.model.ContaContabilFranqueado;
import com.mercurio.lms.franqueados.model.DescontoFranqueado;
import com.mercurio.lms.franqueados.model.DistanciaColetaEntregaFranqueado;
import com.mercurio.lms.franqueados.model.DistanciaTransferenciaFranqueado;
import com.mercurio.lms.franqueados.model.FixoFranqueado;
import com.mercurio.lms.franqueados.model.FreteLocalFranqueado;
import com.mercurio.lms.franqueados.model.LimiteParticipacaoFranqueado;
import com.mercurio.lms.franqueados.model.MunicipioNaoAtendidoFranqueado;
import com.mercurio.lms.franqueados.model.ReembarqueFranqueado;
import com.mercurio.lms.franqueados.model.RepasseFranqueado;
import com.mercurio.lms.franqueados.model.ServicoAdicionalFranqueado;
import com.mercurio.lms.municipios.model.MunicipioFilial;
import com.mercurio.lms.util.JTVigenciaUtils;

import br.com.tntbrasil.integracao.domains.franqueados.TipoCalculoFranqueado;

public class CalculoFranqueadoParametros {

	private Competencia competencia;

	private RepasseFranqueado repasseFranqueado;

	private ReembarqueFranqueado reembarqueFranqueado;

	private ContaContabilFranqueado contaContabilFranqueado;

	private List<DescontoFranqueado> descontosFranqueados;

	private FreteLocalFranqueado freteLocalFranqueado;

	private List<FixoFranqueado> parametrosFranquia;

	private Map<ParcelaKey, List<TypedFlatMap>> vlParcelaPcParticipacaoList;

	private Map<Long, MunicipioFilial> municipioFilial;

	private Map<Long, MunicipioNaoAtendidoFranqueado> municipiosNaoAtendidoFranqueado;

	private List<DistanciaTransferenciaFranqueado> listDistanciaTransferencia;

	private List<DistanciaColetaEntregaFranqueado> lstDistanciaColetaEntrega;

	private BigDecimal nrKmMinimoColetaEntrega;

	private List<LimiteParticipacaoFranqueado> lstLimiteParticipacao;

	private Map<Long, ServicoAdicionalFranqueado> servicoAdicionalFranqueado;

	private Map<ParcelaKey, List<TypedFlatMap>> vlParcelaParticipacaoList;

	private Boolean existePercentualTerra;

	private Long psMinFreteCarreteiro;
	private Long idFranquia;
	private Long idFranquiaBaseSimulacao;
	private List<Long> idsMunicipioSimulacao;
	private TipoCalculoFranqueado tipoCalculo;

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(YearMonthDay dtBaseCompetenciaInicio, YearMonthDay dtBaseCompetenciaFim) {
		this.competencia = new Competencia(dtBaseCompetenciaInicio, dtBaseCompetenciaFim);
	}

	public void setCompetencia(YearMonthDay dtBaseCompetencia) {
		this.competencia = new Competencia(dtBaseCompetencia);
	}

	public RepasseFranqueado getRepasseFranqueado() {
		return repasseFranqueado;
	}

	public void setRepasseFranqueado(RepasseFranqueado repasseFranqueado) {
		this.repasseFranqueado = repasseFranqueado;
	}

	public DescontoFranqueado getDescontosFranqueados(Long idMotivoDesconto, String tpFrete) {
		if (idMotivoDesconto == null || tpFrete == null) {
			return null;
		}
		for (DescontoFranqueado desconto : descontosFranqueados) {
			if (tpFrete.equals(desconto.getTpFrete().getValue())
					&& idMotivoDesconto.equals(desconto.getMotivoDesconto().getIdMotivoDesconto())) {
				return desconto;
			}
		}
		return null;
	}

	public void setDescontosFranqueados(List<DescontoFranqueado> descontosFranqueados) {
		this.descontosFranqueados = descontosFranqueados;
	}

	public FreteLocalFranqueado getFreteLocalFranqueado() {
		return freteLocalFranqueado;
	}

	public void setFreteLocalFranqueado(FreteLocalFranqueado freteLocalFranqueado) {
		this.freteLocalFranqueado = freteLocalFranqueado;
	}

	public List<FixoFranqueado> getParametrosFranquia() {
		return parametrosFranquia;
	}

	public void setParametrosFranquia(List<FixoFranqueado> parametrosFranquia) {
		this.parametrosFranquia = parametrosFranquia;
	}

	public List<TypedFlatMap> getVlParcelaPcParticipacaoList(String tpFrete, Long idDoctoServico) {
		return vlParcelaPcParticipacaoList.get(new ParcelaKey(tpFrete, idDoctoServico));
	}

	public void setVlParcelaPcParticipacaoList(List<TypedFlatMap> vlParcelaPcParticipacaoList) {
		this.vlParcelaPcParticipacaoList = new HashMap<CalculoFranqueadoParametros.ParcelaKey, List<TypedFlatMap>>();
		for (TypedFlatMap map : vlParcelaPcParticipacaoList) {
			ParcelaKey key = new ParcelaKey(map.getString("tpFrete"), map.getLong("idDoctoServico"));
			if (!this.vlParcelaPcParticipacaoList.containsKey(key)) {
				this.vlParcelaPcParticipacaoList.put(key, new LinkedList<TypedFlatMap>());
			}
			this.vlParcelaPcParticipacaoList.get(key).add(map);
		}
	}

	public MunicipioFilial getMunicipioFilial(Long idMunicipio) {
		return municipioFilial.get(idMunicipio);
	}

	public void setMunicipiosFiliais(List<MunicipioFilial> municipiosFiliais) {
		this.municipioFilial = new HashMap<Long, MunicipioFilial>();
		if (CollectionUtils.isNotEmpty(municipiosFiliais)) {
			for (MunicipioFilial municipio : municipiosFiliais) {
				this.municipioFilial.put(municipio.getMunicipio().getIdMunicipio(), municipio);
			}
		}
	}

	public MunicipioNaoAtendidoFranqueado getMunicipioNaoAtendidoFranqueado(Long idMunicipio) {
		return municipiosNaoAtendidoFranqueado.get(idMunicipio);
	}

	public void setMunicipiosNaoAtendidoFranqueado(
			List<MunicipioNaoAtendidoFranqueado> municipioNaoAtendidoFranqueado) {
		this.municipiosNaoAtendidoFranqueado = new HashMap<Long, MunicipioNaoAtendidoFranqueado>();
		if (CollectionUtils.isNotEmpty(municipioNaoAtendidoFranqueado)) {
			for (MunicipioNaoAtendidoFranqueado municipio : municipioNaoAtendidoFranqueado) {
				this.municipiosNaoAtendidoFranqueado.put(municipio.getMunicipio().getIdMunicipio(), municipio);
			}
		}
	}

	public void setLstDistanciaTransferencia(List<DistanciaTransferenciaFranqueado> lstDistanciaTransferencia) {
		listDistanciaTransferencia = lstDistanciaTransferencia;
	}

	public List<DistanciaColetaEntregaFranqueado> getLstDistanciaColetaEntrega() {
		return lstDistanciaColetaEntrega;
	}

	public void setLstDistanciaColetaEntrega(List<DistanciaColetaEntregaFranqueado> lstDistanciaColetaEntrega) {
		this.lstDistanciaColetaEntrega = lstDistanciaColetaEntrega;
		setPercentualTerra();
	}

	public BigDecimal getNrKmMinimoColetaEntrega() {
		return nrKmMinimoColetaEntrega;
	}

	public void setNrKmMinimoColetaEntrega(BigDecimal nrKmMinimoColetaEntrega) {
		this.nrKmMinimoColetaEntrega = nrKmMinimoColetaEntrega;
	}

	public List<LimiteParticipacaoFranqueado> getLstLimiteParticipacao() {
		return lstLimiteParticipacao;
	}

	public void setLstLimiteParticipacao(List<LimiteParticipacaoFranqueado> lstLimiteParticipacao) {
		this.lstLimiteParticipacao = lstLimiteParticipacao;
	}

	public ServicoAdicionalFranqueado getServicoAdicionalFranqueado(Long idServicoAdicional) {
		return servicoAdicionalFranqueado.get(idServicoAdicional);
	}

	public void setServicoAdicionalFranqueado(List<ServicoAdicionalFranqueado> servicoAdicionalFranqueado) {
		this.servicoAdicionalFranqueado = new HashMap<Long, ServicoAdicionalFranqueado>();
		if (CollectionUtils.isNotEmpty(servicoAdicionalFranqueado)) {
			for (ServicoAdicionalFranqueado servico : servicoAdicionalFranqueado) {
				this.servicoAdicionalFranqueado.put(servico.getServicoAdicional().getIdServicoAdicional(), servico);
			}
		}
	}

	public List<TypedFlatMap> getVlParcelaParticipacaoList(String tpFrete, Long idDoctoServico) {
		return vlParcelaParticipacaoList.get(new ParcelaKey(tpFrete, idDoctoServico));
	}

	public void setVlParcelaParticipacaoList(List<TypedFlatMap> vlParcelaParticipacaoList) {
		this.vlParcelaParticipacaoList = new HashMap<CalculoFranqueadoParametros.ParcelaKey, List<TypedFlatMap>>();
		if (CollectionUtils.isNotEmpty(vlParcelaParticipacaoList)) {
			for (TypedFlatMap map : vlParcelaParticipacaoList) {
				ParcelaKey key = new ParcelaKey(map.getString("tpFrete"), map.getLong("idDoctoServico"));
				if (!this.vlParcelaParticipacaoList.containsKey(key)) {
					this.vlParcelaParticipacaoList.put(key, new LinkedList<TypedFlatMap>());
				}
				this.vlParcelaParticipacaoList.get(key).add(map);
			}
		}
	}

	public List<FixoFranqueado> findFixosFranqueadoVigentes(YearMonthDay competenciaRecalculo) {
		List<FixoFranqueado> result = new ArrayList<FixoFranqueado>();
		YearMonthDay dtCompetencia = competenciaRecalculo;
		if (dtCompetencia == null) {
			dtCompetencia = competencia.getInicio();
		}
		if (CollectionUtils.isNotEmpty(parametrosFranquia)) {
			for (FixoFranqueado fixoFranqueado : parametrosFranquia) {
				if (JTVigenciaUtils.isVigente(fixoFranqueado, dtCompetencia)) {
					result.add(fixoFranqueado);
				}
			}
		}
		return result;
	}

	public FixoFranqueado findFixoFranqueado(Long idCliente, Long idMunicipio, YearMonthDay competenciaRecalculo) {

		// Faz a busca por regra específica para o cliente
		for (FixoFranqueado fixoFranqueado : findFixosFranqueadoVigentes(competenciaRecalculo)) {
			if (fixoFranqueado.getCliente() != null) {
				if (fixoFranqueado.getCliente().getIdCliente().equals(idCliente)) {
					return fixoFranqueado;
				}
			} else if (fixoFranqueado.getMunicipio() != null) {
				if (fixoFranqueado.getMunicipio().getIdMunicipio().equals(idMunicipio)) {
					return fixoFranqueado;
				}
			} else {
				return fixoFranqueado;
			}
		}
		throw new IllegalArgumentException("Fixo Franquia não definido na tabela FIXO_FRQ.");
	}

	private void setPercentualTerra() {
		if (CollectionUtils.isNotEmpty(lstDistanciaColetaEntrega)) {
			for (DistanciaColetaEntregaFranqueado distanciaColetaEntregaFranqueado : lstDistanciaColetaEntrega) {
				if (distanciaColetaEntregaFranqueado.getTpPavimento().getValue()
						.equals(ConstantesFranqueado.TP_PAVIMENTO_TERRA)
						&& !distanciaColetaEntregaFranqueado.getPcDistancia().equals(BigDecimal.ZERO)) {
					existePercentualTerra = true;
					break;
				}
			}
		}
		existePercentualTerra = false;
	}

	public Boolean isPercentualTerra() {
		return existePercentualTerra;
	}

	public DistanciaTransferenciaFranqueado getDistanciaTransferenciaFranqueado(String tpFrete, Integer nrKm) {
		if (CollectionUtils.isNotEmpty(listDistanciaTransferencia)) {
			for (DistanciaTransferenciaFranqueado o : listDistanciaTransferencia) {
				if (tpFrete.equals(o.getTpFrete().getValue()) && nrKm <= o.getNrKm()) {
					return o;
				}
			}
		}
		return null;
	}

	public void clearParametros() {
		parametrosFranquia = null;
		municipioFilial = null;
		municipiosNaoAtendidoFranqueado = null;
	}

	class DistanciaTransferenciaKey {
		private String tpFrete;
		private Integer nrKm;

		public DistanciaTransferenciaKey(String tpFrete, Integer nrKm) {
			super();
			this.tpFrete = tpFrete;
			this.nrKm = nrKm;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DistanciaTransferenciaKey) {
				DistanciaTransferenciaKey other = (DistanciaTransferenciaKey) obj;
				if (this.tpFrete.equals(other.tpFrete) && this.nrKm <= other.nrKm) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

	}

	class DescontoFraqueadoKey {
		private String tpFrete;
		private Long idMotivoDesconto;

		public DescontoFraqueadoKey(String tpFrete, Long idMotivoDesconto) {
			super();
			this.tpFrete = tpFrete;
			this.idMotivoDesconto = idMotivoDesconto;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DescontoFraqueadoKey) {
				DescontoFraqueadoKey other = (DescontoFraqueadoKey) obj;
				if (this.tpFrete.equals(other.tpFrete) && this.idMotivoDesconto == other.idMotivoDesconto) {
					return true;
				}
			}
			return false;
		}
		
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

	}

	class ParcelaKey {
		private String tpFrete;
		private Long id;

		public ParcelaKey(String tpFrete, Long id) {
			super();
			this.tpFrete = tpFrete;
			this.id = id;
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ParcelaKey) {
				ParcelaKey other = (ParcelaKey) obj;
				return this.tpFrete.equals(other.tpFrete) && this.id.equals(other.id);
			}
			return false;
		}

	}

	public ReembarqueFranqueado getReembarqueFranqueado() {
		return reembarqueFranqueado;
	}

	public void setReembarqueFranqueado(ReembarqueFranqueado reembarqueFranqueado) {
		this.reembarqueFranqueado = reembarqueFranqueado;
	}

	public void setContaContabilFranqueado(ContaContabilFranqueado contaContabilFranqueado) {
		this.contaContabilFranqueado = contaContabilFranqueado;
	}

	public ContaContabilFranqueado getContaContabilFranqueado() {
		return contaContabilFranqueado;
	}

	public Boolean isMensal() {
		return TipoCalculoFranqueado.MENSAL.equals(tipoCalculo);
	}

	public Boolean isSimulacao() {
		return TipoCalculoFranqueado.SIMULACAO.equals(tipoCalculo);
	}

	public Long getPsMinFreteCarreteiro() {
		return psMinFreteCarreteiro;
	}

	public void setPsMinFreteCarreteiro(Long psMinFreteCarreteiro) {
		this.psMinFreteCarreteiro = psMinFreteCarreteiro;
	}

	public Long getIdFranquia() {
		return idFranquia;
	}

	public void setIdFranquia(Long idFranquia) {
		this.idFranquia = idFranquia;
	}

	public Long getIdFranquiaBaseSimulacao() {
		return idFranquiaBaseSimulacao;
	}

	public void setIdFranquiaBaseSimulacao(Long idFranquiaBaseSimulacao) {
		this.idFranquiaBaseSimulacao = idFranquiaBaseSimulacao;
	}

	public List<Long> getIdsMunicipioSimulacao() {
		return idsMunicipioSimulacao;
	}

	public void setIdsMunicipioSimulacao(List<Long> idsMunicipioSimulacao) {
		this.idsMunicipioSimulacao = idsMunicipioSimulacao;
	}
	
	public TipoCalculoFranqueado getTipoCalculo() {
		return tipoCalculo;
	}

	public void setTipoCalculo(TipoCalculoFranqueado tipoCalculo) {
		this.tipoCalculo = tipoCalculo;
	}

	static public class Competencia {
		private YearMonthDay inicio;
		private YearMonthDay fim;

		public Competencia(YearMonthDay dtBaseCompetencia) {
			initDateMonth(dtBaseCompetencia);
		}

		public Competencia(YearMonthDay dtBaseCompetenciaInicio, YearMonthDay dtBaseCompetenciaFim) {
			initDateDaily(dtBaseCompetenciaInicio, dtBaseCompetenciaFim);
		}

		private void initDateMonth(YearMonthDay date) {
			Integer firstDay = date.dayOfMonth().getMinimumValue();
			Integer lastDay = date.dayOfMonth().getMaximumValue();
			inicio = date.withDayOfMonth(firstDay);
			fim = date.withDayOfMonth(lastDay);
		}

		private void initDateDaily(YearMonthDay dateStart, YearMonthDay dateEnd) {
			inicio = dateStart;
			fim = dateEnd;
		}

		public YearMonthDay getInicio() {
			return inicio;
		}

		public YearMonthDay getFim() {
			return fim;
		}

	}
}