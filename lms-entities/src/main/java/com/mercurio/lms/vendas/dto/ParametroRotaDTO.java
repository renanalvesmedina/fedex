package com.mercurio.lms.vendas.dto;

import java.math.BigDecimal;
import java.util.Arrays;

import com.mercurio.adsm.framework.model.DomainValue;
import com.mercurio.adsm.framework.model.hibernate.VarcharI18n;

/**
 * LMS-4526 - Data Transfer Object para ordenamento do relatório
 * "Tabela Frete Mínimo Peso Excedente". Considera do cliente a UF e a filial
 * que atende comercial. Considera dos parâmetros do cliente as UF's de
 * origem/destino, as filiais de origem/destino, os tipos de localização dos
 * municípios de origem/destino e os grupos região de origem/destino.
 * 
 * @author FabianoP
 */
public class ParametroRotaDTO implements Comparable<ParametroRotaDTO> {

	private static final String TIPO_LOCALIZACAO_MUNICIPIO_CAPITAL = "Capital";

	private Long idUnidadeFederativaReferencia;
	private Long idFilialReferencia;
	private Long idParametroReferencia;
	private String grupo;
	private Long idUnidadeFederativaOrigem;
	private String sgUnidadeFederativaOrigem;
	private Long idUnidadeFederativaDestino;
	private String sgUnidadeFederativaDestino;
	private Long idFilialOrigem;
	private String sgFilialOrigem;
	private Long idFilialDestino;
	private String sgFilialDestino;
	private String dsTipoLocalizacaoMunicipioOrigem;
	private String dsTipoLocalizacaoMunicipioDestino;
	private String dsGrupoRegiaoOrigem;
	private String dsGrupoRegiaoDestino;
	private Object[] parametroClienteAttrs;

	/**
	 * Especialização do construtor para Data Transfer Object de parâmetro do
	 * cliente de rota para o relatório "Tabela Frete Mínimo Peso Excedente".
	 * Inclui dos parâmetros do cliente os valores de peso mínimo do frete
	 * (vlMinFretePeso), peso do frete (vlFretePeso) e advalorem (vlAdvalorem).
	 * 
	 * @param idUnidadeFederativaReferencia
	 * @param idFilialReferencia
	 * @param idParametroReferencia
	 * @param idUnidadeFederativaOrigem
	 * @param sgUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param sgUnidadeFederativaDestino
	 * @param idFilialOrigem
	 * @param sgFilialOrigem
	 * @param idFilialDestino
	 * @param sgFilialDestino
	 * @param dsTipoLocalizacaoMunicipioOrigem
	 * @param dsTipoLocalizacaoMunicipioDestino
	 * @param dsGrupoRegiaoOrigem
	 * @param dsGrupoRegiaoDestino
	 * @param vlMinFretePeso
	 * @param vlFretePeso
	 * @param vlAdvalorem
	 */
	public ParametroRotaDTO(
			Long idUnidadeFederativaReferencia,
			Long idFilialReferencia,
			Long idParametroReferencia,
			Long idUnidadeFederativaOrigem,
			String sgUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino,
			String sgUnidadeFederativaDestino,
			Long idFilialOrigem,
			String sgFilialOrigem,
			Long idFilialDestino,
			String sgFilialDestino,
			VarcharI18n dsTipoLocalizacaoMunicipioOrigem,
			VarcharI18n dsTipoLocalizacaoMunicipioDestino,
			String dsGrupoRegiaoOrigem,
			String dsGrupoRegiaoDestino,
			BigDecimal vlMinFretePeso,
			BigDecimal vlFretePeso,
			BigDecimal vlAdvalorem) {
		this(
				idUnidadeFederativaReferencia,
				idFilialReferencia,
				idParametroReferencia,
				idUnidadeFederativaOrigem,
				sgUnidadeFederativaOrigem,
				idUnidadeFederativaDestino,
				sgUnidadeFederativaDestino,
				idFilialOrigem,
				sgFilialOrigem,
				idFilialDestino,
				sgFilialDestino,
				dsTipoLocalizacaoMunicipioOrigem,
				dsTipoLocalizacaoMunicipioDestino,
				dsGrupoRegiaoOrigem,
				dsGrupoRegiaoDestino);
		this.parametroClienteAttrs = new Object[] {
				vlMinFretePeso,
				vlFretePeso,
				vlAdvalorem
		};
	}

	/**
	 * Especialização do construtor para Data Transfer Object de parâmetro do
	 * cliente de rota para o relatório "Tabela Frete Percentual". Inclui dos
	 * parâmetros do cliente os valores de percentual sobre nota fiscal
	 * (pcFretePercentual), valor frete mínimo (vlMinimoFretePercentual), peso
	 * mínimo (psFretePercentual), valor frete tonelada
	 * (vlToneladaFretePercentual).
	 * 
	 * @param idUnidadeFederativaReferencia
	 * @param idFilialReferencia
	 * @param idParametroReferencia
	 * @param idUnidadeFederativaOrigem
	 * @param sgUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param sgUnidadeFederativaDestino
	 * @param idFilialOrigem
	 * @param sgFilialOrigem
	 * @param idFilialDestino
	 * @param sgFilialDestino
	 * @param dsTipoLocalizacaoMunicipioOrigem
	 * @param dsTipoLocalizacaoMunicipioDestino
	 * @param dsGrupoRegiaoOrigem
	 * @param dsGrupoRegiaoDestino
	 * @param pcFretePercentual
	 * @param vlMinimoFretePercentual
	 * @param psFretePercentual
	 * @param vlToneladaFretePercentual
	 */
	public ParametroRotaDTO(
			Long idUnidadeFederativaReferencia,
			Long idFilialReferencia,
			Long idParametroReferencia,
			Long idUnidadeFederativaOrigem,
			String sgUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino,
			String sgUnidadeFederativaDestino,
			Long idFilialOrigem,
			String sgFilialOrigem,
			Long idFilialDestino,
			String sgFilialDestino,
			VarcharI18n dsTipoLocalizacaoMunicipioOrigem,
			VarcharI18n dsTipoLocalizacaoMunicipioDestino,
			String dsGrupoRegiaoOrigem,
			String dsGrupoRegiaoDestino,
			BigDecimal pcFretePercentual,
			BigDecimal vlMinimoFretePercentual,
			BigDecimal psFretePercentual,
			BigDecimal vlToneladaFretePercentual) {
		this(
				idUnidadeFederativaReferencia,
				idFilialReferencia,
				idParametroReferencia,
				idUnidadeFederativaOrigem,
				sgUnidadeFederativaOrigem,
				idUnidadeFederativaDestino,
				sgUnidadeFederativaDestino,
				idFilialOrigem,
				sgFilialOrigem,
				idFilialDestino,
				sgFilialDestino,
				dsTipoLocalizacaoMunicipioOrigem,
				dsTipoLocalizacaoMunicipioDestino,
				dsGrupoRegiaoOrigem,
				dsGrupoRegiaoDestino);
		this.parametroClienteAttrs = new Object[] {
				pcFretePercentual,
				vlMinimoFretePercentual,
				psFretePercentual,
				vlToneladaFretePercentual
		};
	}
	
	/**
	 * 
	 * @param idUnidadeFederativaReferencia
	 * @param idFilialReferencia
	 * @param idParametroReferencia
	 * @param idUnidadeFederativaOrigem
	 * @param sgUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param sgUnidadeFederativaDestino
	 * @param idFilialOrigem
	 * @param sgFilialOrigem
	 * @param idFilialDestino
	 * @param sgFilialDestino
	 * @param dsTipoLocalizacaoMunicipioOrigem
	 * @param dsTipoLocalizacaoMunicipioDestino
	 * @param dsGrupoRegiaoOrigem
	 * @param dsGrupoRegiaoDestino
	 * @param tpIndicadorAdvalorem
	 * @param tpIndicadorFretePeso
	 * @param tpIndicadorMinFretePeso
	 * @param tpIndicadorPercMinimoProgr
	 * @param vlAdvalorem
	 * @param vlFretePeso
	 * @param vlMinFretePeso
	 * @param vlMinimoFretePercentual
	 * @param vlMinimoFreteQuilo
	 * @param vlPercMinimoProgr
	 */
	public ParametroRotaDTO(
			Long idUnidadeFederativaReferencia,
			Long idFilialReferencia,
			Long idParametroReferencia,
			Long idUnidadeFederativaOrigem,
			String sgUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino,
			String sgUnidadeFederativaDestino,
			Long idFilialOrigem,
			String sgFilialOrigem,
			Long idFilialDestino,
			String sgFilialDestino,
			VarcharI18n dsTipoLocalizacaoMunicipioOrigem,
			VarcharI18n dsTipoLocalizacaoMunicipioDestino,
			String dsGrupoRegiaoOrigem,
			String dsGrupoRegiaoDestino,
			DomainValue tpIndicadorAdvalorem,        
			DomainValue tpIndicadorFretePeso,       
			DomainValue tpIndicadorMinFretePeso,
			DomainValue tpIndicadorPercMinimoProgr,
			BigDecimal vlAdvalorem,                  
			BigDecimal vlFretePeso,                 
			BigDecimal vlMinFretePeso,             
			BigDecimal vlMinimoFretePercentual,
			BigDecimal vlMinimoFreteQuilo,         
			BigDecimal vlPercMinimoProgr) {
		this(
				idUnidadeFederativaReferencia,
				idFilialReferencia,
				idParametroReferencia,
				idUnidadeFederativaOrigem,
				sgUnidadeFederativaOrigem,
				idUnidadeFederativaDestino,
				sgUnidadeFederativaDestino,
				idFilialOrigem,
				sgFilialOrigem,
				idFilialDestino,
				sgFilialDestino,
				dsTipoLocalizacaoMunicipioOrigem,
				dsTipoLocalizacaoMunicipioDestino,
				dsGrupoRegiaoOrigem,
				dsGrupoRegiaoDestino);
		this.parametroClienteAttrs = new Object[] {
				tpIndicadorAdvalorem != null ? tpIndicadorAdvalorem.getValue() : null,
				tpIndicadorFretePeso != null ? tpIndicadorFretePeso.getValue() : null,
				tpIndicadorMinFretePeso != null ? tpIndicadorMinFretePeso.getValue() : null,
				tpIndicadorPercMinimoProgr != null ? tpIndicadorPercMinimoProgr.getValue() : null,
				vlAdvalorem,                  
				vlFretePeso,                 
				vlMinFretePeso,             
				vlMinimoFretePercentual,
				vlMinimoFreteQuilo,         
				vlPercMinimoProgr
		};
	}

	/**
	 * Construtor base para Data Transfer Object de parâmetro do cliente de
	 * rota. Utilizado para ordenamento e verificação de duplicidade
	 * oridem/destino nos relatórios de tabelas de preços.
	 * 
	 * @param idUnidadeFederativaReferencia
	 * @param idFilialReferencia
	 * @param idParametroReferencia
	 * @param idUnidadeFederativaOrigem
	 * @param sgUnidadeFederativaOrigem
	 * @param idUnidadeFederativaDestino
	 * @param sgUnidadeFederativaDestino
	 * @param idFilialOrigem
	 * @param sgFilialOrigem
	 * @param idFilialDestino
	 * @param sgFilialDestino
	 * @param dsTipoLocalizacaoMunicipioOrigem
	 * @param dsTipoLocalizacaoMunicipioDestino
	 * @param dsGrupoRegiaoOrigem
	 * @param dsGrupoRegiaoDestino
	 */
	public ParametroRotaDTO(
			Long idUnidadeFederativaReferencia,
			Long idFilialReferencia,
			Long idParametroReferencia,
			Long idUnidadeFederativaOrigem,
			String sgUnidadeFederativaOrigem,
			Long idUnidadeFederativaDestino,
			String sgUnidadeFederativaDestino,
			Long idFilialOrigem,
			String sgFilialOrigem,
			Long idFilialDestino,
			String sgFilialDestino,
			VarcharI18n dsTipoLocalizacaoMunicipioOrigem,
			VarcharI18n dsTipoLocalizacaoMunicipioDestino,
			String dsGrupoRegiaoOrigem,
			String dsGrupoRegiaoDestino) {
		this();
		this.idUnidadeFederativaReferencia = idUnidadeFederativaReferencia;
		this.idFilialReferencia = idFilialReferencia;
		this.idParametroReferencia = idParametroReferencia;
		this.idUnidadeFederativaOrigem = idUnidadeFederativaOrigem;
		this.sgUnidadeFederativaOrigem = sgUnidadeFederativaOrigem;
		this.idUnidadeFederativaDestino = idUnidadeFederativaDestino;
		this.sgUnidadeFederativaDestino = sgUnidadeFederativaDestino;
		this.idFilialOrigem = idFilialOrigem;
		this.sgFilialOrigem = sgFilialOrigem;
		this.idFilialDestino = idFilialDestino;
		this.sgFilialDestino = sgFilialDestino;
		this.dsTipoLocalizacaoMunicipioOrigem = dsTipoLocalizacaoMunicipioOrigem != null ? dsTipoLocalizacaoMunicipioOrigem
				.getValue() : null;
		this.dsTipoLocalizacaoMunicipioDestino = dsTipoLocalizacaoMunicipioDestino != null ? dsTipoLocalizacaoMunicipioDestino
				.getValue() : null;
		this.dsGrupoRegiaoOrigem = dsGrupoRegiaoOrigem;
		this.dsGrupoRegiaoDestino = dsGrupoRegiaoDestino;
	}

	/**
	 * Construtor base para Data Transfer Object de parâmetro da rota. Utilizado
	 * para ordenamento e verificação de duplicidade oridem/destino nos
	 * relatórios de tabelas de preços.
	 */
	public ParametroRotaDTO() {
		super();
	}

	public Long getIdUnidadeFederativaReferencia() {
		return idUnidadeFederativaReferencia;
	}

	public void setIdUnidadeFederativaReferencia(Long idUnidadeFederativaReferencia) {
		this.idUnidadeFederativaReferencia = idUnidadeFederativaReferencia;
	}

	public Long getIdFilialReferencia() {
		return idFilialReferencia;
	}

	public void setIdFilialReferencia(Long idFilialReferencia) {
		this.idFilialReferencia = idFilialReferencia;
	}

	public Long getIdParametroReferencia() {
		return idParametroReferencia;
	}

	public void setIdParametroReferencia(Long idParametroReferencia) {
		this.idParametroReferencia = idParametroReferencia;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public Long getIdUnidadeFederativaOrigem() {
		return idUnidadeFederativaOrigem;
	}

	public void setIdUnidadeFederativaOrigem(Long idUnidadeFederativaOrigem) {
		this.idUnidadeFederativaOrigem = idUnidadeFederativaOrigem;
	}

	public String getSgUnidadeFederativaOrigem() {
		return sgUnidadeFederativaOrigem;
	}

	public void setSgUnidadeFederativaOrigem(String sgUnidadeFederativaOrigem) {
		this.sgUnidadeFederativaOrigem = sgUnidadeFederativaOrigem;
	}

	public Long getIdUnidadeFederativaDestino() {
		return idUnidadeFederativaDestino;
	}

	public void setIdUnidadeFederativaDestino(Long idUnidadeFederativaDestino) {
		this.idUnidadeFederativaDestino = idUnidadeFederativaDestino;
	}

	public String getSgUnidadeFederativaDestino() {
		return sgUnidadeFederativaDestino;
	}

	public void setSgUnidadeFederativaDestino(String sgUnidadeFederativaDestino) {
		this.sgUnidadeFederativaDestino = sgUnidadeFederativaDestino;
	}

	public Long getIdFilialOrigem() {
		return idFilialOrigem;
	}

	public void setIdFilialOrigem(Long idFilialOrigem) {
		this.idFilialOrigem = idFilialOrigem;
	}

	public String getSgFilialOrigem() {
		return sgFilialOrigem;
	}

	public void setSgFilialOrigem(String sgFilialOrigem) {
		this.sgFilialOrigem = sgFilialOrigem;
	}

	public Long getIdFilialDestino() {
		return idFilialDestino;
	}

	public void setIdFilialDestino(Long idFilialDestino) {
		this.idFilialDestino = idFilialDestino;
	}

	public String getSgFilialDestino() {
		return sgFilialDestino;
	}

	public void setSgFilialDestino(String sgFilialDestino) {
		this.sgFilialDestino = sgFilialDestino;
	}

	public String getDsTipoLocalizacaoMunicipioOrigem() {
		return dsTipoLocalizacaoMunicipioOrigem;
	}

	public void setDsTipoLocalizacaoMunicipioOrigem(String dsTipoLocalizacaoMunicipioOrigem) {
		this.dsTipoLocalizacaoMunicipioOrigem = dsTipoLocalizacaoMunicipioOrigem;
	}

	public String getDsTipoLocalizacaoMunicipioDestino() {
		return dsTipoLocalizacaoMunicipioDestino;
	}

	public void setDsTipoLocalizacaoMunicipioDestino(String dsTipoLocalizacaoMunicipioDestino) {
		this.dsTipoLocalizacaoMunicipioDestino = dsTipoLocalizacaoMunicipioDestino;
	}

	public String getDsGrupoRegiaoOrigem() {
		return dsGrupoRegiaoOrigem;
	}

	public void setDsGrupoRegiaoOrigem(String dsGrupoRegiaoOrigem) {
		this.dsGrupoRegiaoOrigem = dsGrupoRegiaoOrigem;
	}

	public String getDsGrupoRegiaoDestino() {
		return dsGrupoRegiaoDestino;
	}

	public void setDsGrupoRegiaoDestino(String dsGrupoRegiaoDestino) {
		this.dsGrupoRegiaoDestino = dsGrupoRegiaoDestino;
	}

	public Object[] getParametroClienteAttrs() {
		return parametroClienteAttrs;
	}

	public void setParametroClienteAttrs(Object[] parametroClienteAttrs) {
		this.parametroClienteAttrs = parametroClienteAttrs;
	}

	/**
	 * Retorna um código hash para o Data Transfer Object
	 * <tt>ParametroClienteRotaDTO</tt>. De acordo com as recomendações este
	 * método é consistente com o método {@link #equals(Object)}, ou seja, se
	 * dois objetos são "iguais" então o código hash deverá sera o mesmo para
	 * ambos objetos.
	 * 
	 * Na produção do código hash os atributos referentes à origem/destino são
	 * considerados equivalentes, ou seja, se alternados produzirá o mesmo
	 * resultado.
	 * 
	 * @see java.lang.Object#hashCode()
	 * 
	 * @return código hash para o Data Transfer Object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idUnidadeFederativaReferencia.hashCode();
		result = prime * result + idFilialReferencia.hashCode();
		if (grupo != null) {
			result = prime * result + grupo.hashCode();
		}
		int hashOrigem = Arrays.hashCode(new Object[] {
				this.idUnidadeFederativaOrigem,
				this.idFilialOrigem,
				this.dsTipoLocalizacaoMunicipioOrigem,
				this.dsGrupoRegiaoOrigem
		});
		int hashDestino = Arrays.hashCode(new Object[] {
				this.idUnidadeFederativaDestino,
				this.idFilialDestino,
				this.dsTipoLocalizacaoMunicipioDestino,
				this.dsGrupoRegiaoDestino
		});
		if (hashOrigem < hashDestino) {
			result = prime * result + hashOrigem;
			result = prime * result + hashDestino;
		} else {
			result = prime * result + hashDestino;
			result = prime * result + hashOrigem;
		}
		result = prime * result + Arrays.hashCode(parametroClienteAttrs);
		return result;
	}

	/**
	 * Determina se algum objeto é "igual" a este. De acordo com as
	 * recomendações este método é consistente com o método {@link #hashCode()},
	 * ou seja, dois objetos considerados "iguais" terão o mesmo código hash.
	 * 
	 * Na verificação de igualdade os atributos referentes à origem/destino são
	 * considerados equivalentes, ou seja, se alternados produzirá o mesmo
	 * resultado.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param object
	 *            referência a objeto para comparação
	 * @return <tt>true</tt> se os objetos forem iguais
	 */
	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		ParametroRotaDTO that = (ParametroRotaDTO) object;
		// Compara grupos, se houver
		if (this.grupo != null && !this.grupo.equals(that.grupo)
				|| that.grupo != null && !that.grupo.equals(this.grupo)) {
			return false;
		}
		// Compara por UF e filial da Referencia
		if (!this.idUnidadeFederativaReferencia.equals(that.idUnidadeFederativaReferencia)
				|| !this.idFilialReferencia.equals(that.idFilialReferencia)) {
			return false;
		}
		// Compara pontos das rotas de forma direta e alternada
		Object[] origem1 = new Object[] {
				this.idUnidadeFederativaOrigem,
				this.idFilialOrigem,
				this.dsTipoLocalizacaoMunicipioOrigem,
				this.dsGrupoRegiaoOrigem
		};
		Object[] destino1 = new Object[] {
				this.idUnidadeFederativaDestino,
				this.idFilialDestino,
				this.dsTipoLocalizacaoMunicipioDestino,
				this.dsGrupoRegiaoDestino
		};
		Object[] origem2 = new Object[] {
				that.idUnidadeFederativaOrigem,
				that.idFilialOrigem,
				that.dsTipoLocalizacaoMunicipioOrigem,
				that.dsGrupoRegiaoOrigem
		};
		Object[] destino2 = new Object[] {
				that.idUnidadeFederativaDestino,
				that.idFilialDestino,
				that.dsTipoLocalizacaoMunicipioDestino,
				that.dsGrupoRegiaoDestino
		};
		if ((!Arrays.equals(origem1, origem2) || !Arrays.equals(destino1, destino2))
				&& (!Arrays.equals(origem1, destino2) || !Arrays.equals(destino1, origem2))) {
			return false;
		}
		// Compara atributos do parâmetro da Referencia
		return Arrays.equals(this.parametroClienteAttrs, that.parametroClienteAttrs);
	}

	/**
	 * As rotas devem ser ordenadas da seguinte forma:
	 * 1. Rotas originadas na UF e filial da Referencia;
	 * 2. Rotas originadas na UF da Referencia sem indicação de filial;
	 * 3. Demais rotas, ordenadas por UF (3a), primeiro capital e depois
	 *    interior (3b) ou por filial se esta for informada (3c) ou
	 *    GRUPO_REGIAO.NR_ORDEM_VISUALIZACAO caso grupo região tenha sido
	 *    informado (3d) (atenção: atributo GRUPO_REGIAO.NR_ORDEM_VISUALIZACAO
	 *    inexistente; considerar GRUPO_REGIAO.DS_GRUPO_REGIAO para regra 3d).
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * 
	 * @param that
	 *            objeto <tt>ParametroRotaDTO</tt> para comparação
	 * @return -1, zero ou 1 se este objeto for menor, igual ou maior que o
	 *         objeto especificado
	 */
	@Override
	public int compareTo(ParametroRotaDTO that) {
		// Compara grupos, se houver
		int c = compare(this.grupo, that.grupo);
		if (c != 0) {
			return c;
		}
		// Compara origens das rotas
		c = compare(
				this.idUnidadeFederativaOrigem,
				this.sgUnidadeFederativaOrigem,
				this.idFilialOrigem,
				this.sgFilialOrigem,
				this.dsTipoLocalizacaoMunicipioOrigem,
				this.dsGrupoRegiaoOrigem,
				that.idUnidadeFederativaOrigem,
				that.sgUnidadeFederativaOrigem,
				that.idFilialOrigem,
				that.sgFilialOrigem,
				that.dsTipoLocalizacaoMunicipioOrigem,
				that.dsGrupoRegiaoOrigem);
		if (c != 0) {
			return c;
		}
		// Se mesma origem, compara destinos das rotas
		c = compare(
				this.idUnidadeFederativaDestino,
				this.sgUnidadeFederativaDestino,
				this.idFilialDestino,
				this.sgFilialDestino,
				this.dsTipoLocalizacaoMunicipioDestino,
				this.dsGrupoRegiaoDestino,
				that.idUnidadeFederativaDestino,
				that.sgUnidadeFederativaDestino,
				that.idFilialDestino,
				that.sgFilialDestino,
				that.dsTipoLocalizacaoMunicipioDestino,
				that.dsGrupoRegiaoDestino);
		return c;
	}

	/**
	 * Comparação parcial de parâmetros, considerando isoladamente
	 * origem/destino de cada rota.
	 * 
	 * @param idUnidadeFederativa1
	 * @param sgUnidadeFederativa1
	 * @param idFilial1
	 * @param sgFilial1
	 * @param dsTipoLocalizacaoMunicipio1
	 * @param dsGrupoRegiao1
	 * @param idUnidadeFederativa2
	 * @param sgUnidadeFederativa2
	 * @param idFilial2
	 * @param sgFilial2
	 * @param dsTipoLocalizacaoMunicipio2
	 * @param dsGrupoRegiao2
	 * @return -1, zero ou 1 na comparação de origem ou destino
	 */
	private int compare(
			Long idUnidadeFederativa1,
			String sgUnidadeFederativa1,
			Long idFilial1,
			String sgFilial1,
			String dsTipoLocalizacaoMunicipio1,
			String dsGrupoRegiao1,
			Long idUnidadeFederativa2,
			String sgUnidadeFederativa2,
			Long idFilial2,
			String sgFilial2,
			String dsTipoLocalizacaoMunicipio2,
			String dsGrupoRegiao2) {
		// Compara por UF e filial para regras 1 e 2
		int c = compare(
				idUnidadeFederativa1, idFilial1,
				idUnidadeFederativa2, idFilial2);
		if (c != 0) {
			return c;
		}
		c = compare(
				sgUnidadeFederativa1, dsTipoLocalizacaoMunicipio1, sgFilial1, dsGrupoRegiao1,
				sgUnidadeFederativa2, dsTipoLocalizacaoMunicipio2, sgFilial2, dsGrupoRegiao2);
		return c;
	}

	/**
	 * Compara rotas considerando UF e filial, de acordo com as regras 1 e 2
	 * definidas em {@link #compareTo(ParametroRotaDTO)}.
	 * 
	 * @param idUnidadeFederativa1
	 * @param idFilial1
	 * @param idUnidadeFederativa2
	 * @param idFilial2
	 * @return -1, zero ou 1 na comparação pelas regras 1 e 2
	 */
	private int compare(
			Long idUnidadeFederativa1, Long idFilial1,
			Long idUnidadeFederativa2, Long idFilial2) {
		// Regra 1 - rota originada na UF e filial da Referencia
		boolean b1 = isUnidadeFederativaReferenciaFilialReferencia(idUnidadeFederativa1, idFilial1);
		boolean b2 = isUnidadeFederativaReferenciaFilialReferencia(idUnidadeFederativa2, idFilial2);
		if (b1 != b2) {
			return b1 ? -1 : 1;
		}
		// Regra 2 - rota originada na UF da Referencia sem indicação de filial
		b1 = isUnidadeFederativaReferenciaFilialNull(idUnidadeFederativa1, idFilial1);
		b2 = isUnidadeFederativaReferenciaFilialNull(idUnidadeFederativa2, idFilial2);
		if (b1 != b2) {
			return b1 ? -1 : 1;
		}
		return 0;
	}

	/**
	 * Compara rotas considerando siglas da UF's, tipos de localização
	 * (capital/interior), sigla das filiais e grupo região, de acordo com a
	 * regra 3 definida em {@link #compareTo(ParametroRotaDTO)}.
	 * 
	 * @param sgUnidadeFederativa1
	 * @param dsTipoLocalizacaoMunicipio1
	 * @param sgFilial1
	 * @param dsGrupoRegiao1
	 * @param sgUnidadeFederativa2
	 * @param dsTipoLocalizacaoMunicipio2
	 * @param sgFilial2
	 * @param dsGrupoRegiao2
	 * @return -1, zero ou 1 na comparação pela regra 3
	 */
	private int compare(
			String sgUnidadeFederativa1, String dsTipoLocalizacaoMunicipio1, String sgFilial1, String dsGrupoRegiao1,
			String sgUnidadeFederativa2, String dsTipoLocalizacaoMunicipio2, String sgFilial2, String dsGrupoRegiao2) {
		// Regra 3a - ordenar por UF
		int c = compare(sgUnidadeFederativa1, sgUnidadeFederativa2);
		if (c != 0) {
			return c;
		}
		// Regra 3b - ordenar por capital/interior
		if (dsTipoLocalizacaoMunicipio1 != null && dsTipoLocalizacaoMunicipio2 != null) {
			boolean b1 = isTipoLocalizacaoMunicipioCapital(dsTipoLocalizacaoMunicipio1);
			boolean b2 = isTipoLocalizacaoMunicipioCapital(dsTipoLocalizacaoMunicipio2);
			if (b1 != b2) {
				return b1 ? -1 : 1;
			}
		} else if (dsTipoLocalizacaoMunicipio1 != null) {
			return -1;
		} else if (dsTipoLocalizacaoMunicipio2 != null) {
			return 1;
		}
		// Regra 3c - ordenar por filial
		c = compare(sgFilial1, sgFilial2);
		if (c != 0) {
			return c;
		}
		// Regra 3d - ordenar por grupo região
		c = compare(dsGrupoRegiao1, dsGrupoRegiao2);
		if (c != 0) {
			return c;
		}
		return 0;
	}

	/**
	 * Compara duas strings tratando <tt>null</tt>.
	 * 
	 * @param string1
	 * @param string2
	 * @return -1, zero ou 1 na comparação de duas string
	 */
	private int compare(String string1, String string2) {
		if (string1 != null && string2 != null) {
			return string1.compareTo(string2);
		} else if (string1 != null) {
			return -1;
		} else if (string2 != null) {
			return 1;
		}
		return 0;
	}

	/**
	 * Verifica se UF e filial estão especificadas e coincidem com UF e filial
	 * da Referencia, de acordo com a regra 1 definida em
	 * {@link #compareTo(ParametroRotaDTO)}.
	 * 
	 * @param idUnidadeFederativa
	 * @param idFilial
	 * @return <tt>true</tt> se origem/destino da rota está na mesma UF e filial
	 *         da Referencia
	 */
	private boolean isUnidadeFederativaReferenciaFilialReferencia(Long idUnidadeFederativa, Long idFilial) {
		return idUnidadeFederativaReferencia != null && idUnidadeFederativaReferencia.equals(idUnidadeFederativa)
				&& idFilialReferencia != null && idFilialReferencia.equals(idFilial);
	}

	/**
	 * Verifica se UF está especificada e coincide com UF da Referencia; e também
	 * se tem filial indeterminada, de acordo com a regra 2 definida em
	 * {@link #compareTo(ParametroRotaDTO)}.
	 * 
	 * @param idUnidadeFederativa
	 * @param idFilial
	 * @return <tt>true</tt> se origem/destino da rota está na mesma UF da
	 *         Referencia e tem filial indeterminada
	 */
	private boolean isUnidadeFederativaReferenciaFilialNull(Long idUnidadeFederativa, Long idFilial) {
		return idUnidadeFederativaReferencia != null && idUnidadeFederativaReferencia.equals(idUnidadeFederativa)
				&& idFilial == null;
	}

	/**
	 * Verifica se tipo de localização é considerado "Capital" ou não, de acordo
	 * com a regra 3 definida em {@link #compareTo(ParametroRotaDTO)}.
	 * 
	 * @param dsTipoLocalizacaoMunicipio1
	 * @return <tt>true</tt> se origem/destino
	 */
	private boolean isTipoLocalizacaoMunicipioCapital(String dsTipoLocalizacaoMunicipio1) {
		return dsTipoLocalizacaoMunicipio1 != null
				&& dsTipoLocalizacaoMunicipio1.indexOf(TIPO_LOCALIZACAO_MUNICIPIO_CAPITAL) != -1;
	}

	/**
	 * Retorna uma representação string do Data Transfer Object
	 * <tt>ParametroReferenciaRota</tt> listando seus atributos.
	 * 
	 * @see java.lang.Object#toString()
	 * 
	 * @return representação string do Data Transfer Object
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder()
				.append("[")
				.append(idUnidadeFederativaReferencia).append(", ")
				.append(idFilialReferencia).append(", ")
				.append(idParametroReferencia).append(", ")
				.append(grupo).append(", ")
				.append(idUnidadeFederativaOrigem).append(", ")
				.append(sgUnidadeFederativaOrigem).append(", ")
				.append(idUnidadeFederativaDestino).append(", ")
				.append(sgUnidadeFederativaDestino).append(", ")
				.append(idFilialOrigem).append(", ")
				.append(sgFilialOrigem).append(", ")
				.append(idFilialDestino).append(", ")
				.append(sgFilialDestino).append(", ")
				.append(dsTipoLocalizacaoMunicipioOrigem).append(", ")
				.append(dsTipoLocalizacaoMunicipioDestino).append(", ")
				.append(dsGrupoRegiaoOrigem).append(", ")
				.append(dsGrupoRegiaoDestino);
		for (Object parametroClienteAttr : parametroClienteAttrs) {
			sb.append(", ").append(parametroClienteAttr);
		}
		sb.append("]");
		return sb.toString();
	}

}
