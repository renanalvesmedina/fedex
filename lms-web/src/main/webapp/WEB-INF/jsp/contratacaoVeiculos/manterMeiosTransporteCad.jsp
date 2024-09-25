<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	// variável para manter nrFrota no caso de usuário tentar trocar para próprio.
	var nrFrotaAlteracao;

	/**
	 * Como são usados divs, é necessário a função para gerar 100 colunas dentro da table no div.
	 */
	function geraColunas() {
		colunas = '<table class="Form" cellpadding="0" cellspacing="0" width="98%><tr>';
		for (i = 0 ; i < 33 ; i++) {
			colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td>';
			colunas += '<td><img src="lms/images/spacer.gif" width="8px" height="1px"></td><td><img src="lms/images/spacer.gif" width="8px" height="1px"></td>';
		}
		colunas += '<td><img src="lms/images/spacer.gif" width="7px" height="1px"></td></tr>';

		return colunas;
	}
</script>
<adsm:window service="lms.contratacaoveiculos.manterMeiosTransporteAction" onPageLoadCallBack="meioTransporte">
	<adsm:i18nLabels>
		<adsm:include key="bloquear"/>
		<adsm:include key="liberar"/>
		<adsm:include key="bloqueado"/>
		<adsm:include key="liberado"/>
		<adsm:include key="meioTransporte"/>
	</adsm:i18nLabels>
	
	<adsm:form
		idProperty="idMeioTransporte"
		onDataLoadCallBack="meioTransporteLoad"
		service="lms.contratacaoveiculos.manterMeiosTransporteAction.findByIdCustom"
		action="/contratacaoVeiculos/manterMeiosTransporte"
		height="368"
	>

	<adsm:hidden property="meioTransporteRodoviario.idMeioTransporte" serializable="true" />
	<adsm:hidden property="tpSituacaoPendencia" serializable="true" />
	<adsm:hidden property="idProcessoWorkflow" serializable="false"/>
	<adsm:hidden property="desabilitaCad" serializable="false"/>

	<td colspan="100" >

	<div id="principal" style="display:;border:none;">
	<script>
		document.write(geraColunas()); 
	</script>

		<adsm:hidden property="tipoEmpresaFilial" value="M" serializable="false" />
		<adsm:lookup
			property="filial"
			idProperty="idFilial"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupFilial"
			criteriaProperty="sgFilial" label="filial" size="3" maxLength="3" required="true"
			action="/municipios/manterFiliais" labelWidth="20%" width="80%" exactMatch="true"
			dataType="text"
			cellStyle="vertical-align:bottom;"
		>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tipoEmpresaFilial" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="26" maxLength="50" disabled="true" serializable="false" cellStyle="vertical-align:bottom;" />
		</adsm:lookup>

		<adsm:combobox
			property="tpVinculo" label="tipoVinculo" domain="DM_TIPO_VINCULO_VEICULO" required="true"
			labelWidth="20%" width="30%" onchange="tpVinculoChange(this);" cellStyle="vertical-align:bottom;"/>
			
		<adsm:combobox property="tpOperacao" domain="DM_TIPO_OPERACAO_PROPRIETARIO" label="tipoOperacao"  labelWidth="20%" width="30%" required="true" />

		<adsm:lookup
			dataType="text"
			property="filialAgregadoCe"
			idProperty="idFilial"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupFilial"
			criteriaProperty="sgFilial"
			label="agregadoColetaEntregaFilial"
			size="3"
			maxLength="3"
			action="/municipios/manterFiliais"
			labelWidth="20%"
			width="30%"
			exactMatch="true"
			cellStyle="vertical-align:bottom;"
			disabled="true"
		>
			<adsm:propertyMapping relatedProperty="filialAgregadoCe.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tipoEmpresaFilial" modelProperty="empresa.tpEmpresa"/>
			<adsm:textbox dataType="text" property="filialAgregadoCe.pessoa.nmFantasia" size="26" maxLength="50" disabled="true" serializable="false" cellStyle="vertical-align:bottom;" />
		</adsm:lookup>

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" required="true" labelWidth="20%" width="30%" />

		<%-- Combo 'Tipo meio transporte' é montada dinamicamente pela combo 'meio transporte' --%>
		<adsm:combobox property="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" domain="DM_TIPO_MEIO_TRANSPORTE"
			onchange="meioTransporteChange(this);" required="true"
			label="modalidade" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;" serializable="false"/>

		<adsm:textbox dataType="text" property="nrFrota" label="meioTransporte"
				maxLength="6" size="6" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;" mask="999999">
			<adsm:textbox dataType="text" property="nrIdentificador" required="true"
				maxLength="25" size="25" cellStyle="vertical-align:bottom;"
				onchange="return onNrIdentificadorChange(this);" />
		</adsm:textbox>

		<adsm:hidden property="modeloMeioTransporte.tpMeioTransporte" />
		<adsm:combobox property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
			optionProperty="idTipoMeioTransporte" optionLabelProperty="dsTipoMeioTransporte"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboTipoMeioTransporteAtivo" onlyActiveValues="true" required="true"
			onDataLoadCallBack="comboTipoMeioTransporte" onchange="tipoMeioTransporteChange(this);"
			label="tipoMeioTransporte" labelWidth="20%" width="30%" boxWidth="172" cellStyle="vertical-align:bottom;"
			serializable="true"
		>
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="dsTipoMeioTransporteTemp" modelProperty="dsTipoMeioTransporte" />
		</adsm:combobox>
		<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" serializable="false" />
		<adsm:hidden property="dsTipoMeioTransporteTemp" serializable="false" />
		<adsm:hidden property="nrCodigoBarraTemp" serializable="false" />

		<%-- Combo 'Modelo meio transporte' é montada dinamicamente pelas combos
				'Tipo meio transporte' e 'Marca meio transporte' --%>
		<adsm:combobox property="modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte"
			optionProperty="idMarcaMeioTransporte" optionLabelProperty="dsMarcaMeioTransporte"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboMarcaMeioTransporte"
			onlyActiveValues="true" required="true"
			onDataLoadCallBack="comboMarcaMeioTransporte" onchange="marcaAndTipoChange(this);"
			label="marca" labelWidth="20%" width="30%" boxWidth="172" serializable="false"
		>
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" modelProperty="tpMeioTransporte" />	
		</adsm:combobox>
		<adsm:combobox property="modeloMeioTransporte.idModeloMeioTransporte"
			optionProperty="idModeloMeioTransporte" optionLabelProperty="dsModeloMeioTransporte"
			service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboModeloMeioTransporte" onlyActiveValues="true" required="true"
			onDataLoadCallBack="comboModeloMeioTransporte"
			onchange="modeloMeioTransporteChange(this);"
			label="modelo" labelWidth="20%" width="30%" boxWidth="172"
		>
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" modelProperty="tipoMeioTransporte.idTipoMeioTransporte" />	
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte" modelProperty="marcaMeioTransporte.idMarcaMeioTransporte" />
		</adsm:combobox>

		<adsm:textbox dataType="integer" property="nrAnoFabricao" label="anoFabricacao" required="true"
			maxLength="4" size="8" labelWidth="20%" width="30%" />
		<adsm:textbox dataType="integer" property="qtPortas" label="quantidadePortas" required="true"
			maxLength="3" size="8" labelWidth="20%" width="30%" />

		<adsm:textbox dataType="decimal" property="nrCapacidadeKg" label="capacidade" unit="kg" required="true"
			labelWidth="20%" width="30%" mask="#,###,###,###,###,##0.00" />
		<adsm:textbox dataType="decimal" property="nrCapacidadeM3" label="capacidade" unit="m3" required="true"
			labelWidth="20%" width="30%" mask="###,###,##0.00" onchange="return nrCapacidadeChange();" />

		<adsm:combobox property="tipoCombustivel.idTipoCombustivel"
			optionProperty="idTipoCombustivel" optionLabelProperty="dsTipoCombustivel"			
			label="tipoCombustivel" labelWidth="20%" width="30%" cellStyle="vertical-align:bottom;"
			serializable="true">
		</adsm:combobox>

		<adsm:hidden property="idFotoMeioTransporte"/>
		
		<adsm:textbox dataType="picture"
					  property="imMeioTransporte"
					  blobColumnName="IM_MEIO_TRANSPORTE"
					  tableName="FOTO_MEIO_TRANSPORTE"
					  primaryKeyColumnName="ID_FOTO_MEIO_TRANSPORTE"
					  primaryKeyValueProperty="idFotoMeioTransporte"
					  label="foto"
					  labelWidth="20%"
					  width="30%" />
		
		<adsm:textbox dataType="integer" property="preNrCodigoBarra" label="nrCodigoBarra"
				maxLength="2" size="2" labelWidth="20%" width="5%" cellStyle="vertical-align:bottom;" disabled="true">
			<adsm:textbox dataType="integer" property="nrCodigoBarra"
				maxLength="10" size="10" width="15%" />
		</adsm:textbox>	

		<adsm:combobox property="tpSituacao" domain="DM_STATUS_PESSOA" required="true"	label="situacao" labelWidth="20%" width="80%" />
		
		<adsm:complement width="78%" labelWidth="20%" label="alteradoPor">
			<adsm:textbox dataType="text" property="usuarioAtualizacao.nrMatricula" width="20%" size="10" disabled="true"/>
			<adsm:textbox dataType="text" property="usuarioAtualizacao.nmUsuario" size="50" width="50%" disabled="true"/>
		</adsm:complement>

		<adsm:textbox dataType="JTDate" property="dtAtualizacao" disabled="true" picker="false" label="dataAtualizacao" size="20" maxLength="20" labelWidth="20%" width="30%"/>
		
		<adsm:textbox dataType="text" property="dsPendencia" size="30" maxLength="60" label="situacaoPendencia" labelWidth="20%" width="30%" disabled="true"/>

		<adsm:textbox dataType="integer" property="nrAntt" size="30" maxLength="17" label="antt" labelWidth="20%" width="30%" required="true"/>	

		<adsm:combobox property="blAlugado" domain="DM_SIM_NAO" label="veiculoAlugado" labelWidth="20%" width="20%" required="true"/>
	</table>	
	</div>

<%--agrupamento Características gerais--%>
	<div id="caracGerais" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="caracteristicasGerais"/>

		<adsm:checkbox property="meioTransporteRodoviario.blControleTag" label="controleTAG" width="30%" labelWidth="20%"
				onclick="controleTagClick(this);" />
		<adsm:textbox dataType="integer" property="meioTransporteRodoviario.nrTag"
				label="numeroTag" maxLength="7" size="8" labelWidth="20%" width="30%" disabled="true" >
			<span id="meioTransporteRodoviario_nrTag_req"><font size="2" color="red">*</font></span>
		</adsm:textbox>

		<adsm:checkbox property="meioTransporteRodoviario.blPossuiPlataforma" label="possuiPlataforma" width="80%" labelWidth="20%"/>

		<adsm:textbox dataType="decimal" property="meioTransporteRodoviario.vlAlturaBau" onchange="onAlturaBauChange(this);"
				label="alturaBau" labelWidth="20%" width="30%" mask="##0.00" unit="m" />
		<adsm:textbox dataType="decimal" property="meioTransporteRodoviario.vlLarguraBau" onchange="onLarguraBauChange(this);"
				label="larguraBau" labelWidth="20%" width="30%" mask="##0.00" unit="m" />
		<adsm:textbox dataType="decimal" property="meioTransporteRodoviario.vlProfundidadeBau" onchange="onProfundidadeBauChange(this);"
				label="profundidadeBau" labelWidth="20%" width="80%" mask="##0.00" unit="m" />

		<adsm:combobox property="meioTransporteRodoviario.eixosTipoMeioTransporte.idEixosTipoMeioTransporte"
				optionLabelProperty="qtEixos" optionProperty="idEixosTipoMeioTransporte"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboEixosTipoMeioTransporte"
				label="quantidadeEixos" labelWidth="20%" width="80%" boxWidth="180" required="true"
				onDataLoadCallBack="eixosTipoMeioTransporteLoad">
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
					modelProperty="idTipoMeioTransporte"/>
		</adsm:combobox>

		<adsm:combobox property="meioTransporteRodoviario.operadoraMct.idOperadoraMct"
				optionLabelProperty="pessoa.nmPessoa" optionProperty="idOperadoraMct"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findComboOperadoraMct" onlyActiveValues="true"
				label="operadoraMCT" labelWidth="20%" width="30%" onchange="operadoraMctChange();" boxWidth="172" />

		<adsm:textbox dataType="integer" property="meioTransporteRodoviario.nrRastreador"
				label="rastreadorNum" maxLength="10" size="20" width="18%" labelWidth="11%" onchange="nrRastreadorChange(this)">
			<span id="meioTransporteRodoviario_nrRastreador_req"><font size="2" color="red">*</font></span>
		</adsm:textbox>
		<adsm:checkbox property="meioTransporteRodoviario.blMonitorado" label="rastreadorAtivo" width="8%" labelWidth="8%" />

		<adsm:listbox size="3" boxWidth="200" 
			label="rastreadorPerifericos"
			labelWidth="20%"
			width="33%"
			property="meioTransportePerifericos"
			optionProperty="idPerifRast"
			orderProperty="dsPerifericoRastreador"
			required="true"
		>
			<adsm:combobox property="meioTransportePerifericos.idPerifericoRastreador"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findPerifericoRastreador" 
				optionProperty="idPerifericoRastreador" optionLabelProperty="dsPerifericoRastreador"
				boxWidth="150">
			</adsm:combobox>
		</adsm:listbox>

		<adsm:textbox dataType="decimal" property="meioTransporteRodoviario.psTara" label="tara"
				width="30%" labelWidth="20%" mask="###,###,###,###,##0.000" />

		<adsm:complement labelWidth="20%" width="60%" label="celular">
			<adsm:textbox dataType="integer" property="meioTransporteRodoviario.nrDddCelular" size="4" maxLength="5"/>
			<adsm:textbox dataType="integer" property="meioTransporteRodoviario.nrCelular" size="10" maxLength="10"/>
		</adsm:complement>

		<adsm:hidden property="tpSituacaoAtivo" value="A" />
		<adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupRodo" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="cavaloMecanico" labelWidth="20%" width="80%" size="8" serializable="false" maxLength="6" >
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" 
					modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte"/>
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
					modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrAnoFabricao"
					modelProperty="meioTransporte.nrAnoFabricao" />
			<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo"
					modelProperty="meioTransporte.tpSituacao" />

			<adsm:lookup dataType="text" property="meioTransporteRodoviario.meioTransporteRodoviario" idProperty="idMeioTransporte"
					service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupRodo" picker="true"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					size="25" maxLength="25" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"
						modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrAnoFabricao"
						modelProperty="meioTransporte.nrAnoFabricao" />
				<adsm:propertyMapping criteriaProperty="tpSituacaoAtivo"
						modelProperty="meioTransporte.tpSituacao" />
			</adsm:lookup>					

		</adsm:lookup>

		<adsm:hidden property="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrAnoFabricao" serializable="false" />

	</table>	
	</div>	

<%--Agrupamento Informações documentação--%>
	<div id="infDoc" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="informacoesDocumentacao"/>

		<adsm:textbox dataType="integer" property="meioTransporteRodoviario.cdRenavam"
				label="codigoRENAVAM" width="30%" labelWidth="20%" size="20" maxLength="10" required="true" />
		<adsm:textbox dataType="integer" property="meioTransporteRodoviario.nrCertificado"
				label="certificado" width="30%" labelWidth="20%" size="20" maxLength="11" required="true" />
		<adsm:textbox dataType="JTDate" property="meioTransporteRodoviario.dtEmissao"
				label="dataEmissaoDocumento" width="60%" labelWidth="20%" size="20" required="true" />
		<adsm:textbox dataType="text" property="meioTransporteRodoviario.nrChassi"
				label="chassi" width="60%" labelWidth="20%" maxLength="18" size="20" required="true" />

		<adsm:lookup dataType="text" property="meioTransporteRodoviario.municipio" idProperty="idMunicipio"
				service="lms.contratacaoveiculos.manterMeiosTransporteAction.findLookupMunicipio" criteriaProperty="nmMunicipio"
				width="30%" labelWidth="20%" label="municipioEmplacamento" size="30" required="true"
				action="/municipios/manterMunicipios" minLengthForAutoPopUpSearch="3" exactMatch="false" maxLength="60"
			>
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.municipio.unidadeFederativa.nmUnidadeFederativa"
				modelProperty="unidadeFederativa.nmUnidadeFederativa" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.municipio.unidadeFederativa.pais.nmPais"
				modelProperty="unidadeFederativa.pais.nmPais" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.municipio.unidadeFederativa.pais.sgPais"
				modelProperty="unidadeFederativa.pais.sgPais" />
		</adsm:lookup>			

		<adsm:textbox dataType="text" property="meioTransporteRodoviario.municipio.unidadeFederativa.nmUnidadeFederativa" serializable="false"
			label="ufEmplacamento" width="30%" labelWidth="20%" size="20" disabled="true" />
		<adsm:textbox dataType="text" property="meioTransporteRodoviario.municipio.unidadeFederativa.pais.nmPais" serializable="false"
			label="paisEmplacamento" width="30%" labelWidth="20%" size="20" disabled="true" />
		<adsm:hidden property="meioTransporteRodoviario.municipio.unidadeFederativa.pais.sgPais" serializable="true" />	

		<adsm:textbox dataType="text" property="meioTransporteRodoviario.nrBilheteSeguro"
			label="bilheteSeguro" width="30%" labelWidth="20%" maxLength="11" size="20" required="true" />
		<adsm:textbox dataType="JTDate" property="meioTransporteRodoviario.dtVencimentoSeguro"
			label="vencimentoSeguro" width="30%" labelWidth="20%" size="20" required="true" />

	</table>
	</div>

<%--Agrupamento Bloqueio Liberação--%>

	<div id="infBloqLib" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

		<adsm:section caption="informacoesBloqueioLiberacao"/>
		<adsm:textbox dataType="text" property="tpStatus" label="situacao" labelWidth="20%" width="80%" disabled="true" />

	</table>
	</div>

<%--Outras informações é montado dinamicamente--%>

	<div id="outrasInf" style="display:;border:none;">
	<script>
		document.write(geraColunas());
	</script>

	<adsm:section caption="outrasInformacoes"/>

	</table>
	</div>

		<adsm:buttonBar lines="2">
			<adsm:button caption="eventos" action="/contratacaoVeiculos/consultarEventosMeiosTransporte" cmd="main" >
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporte.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporte2.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="nrFrota" target="meioTransporte.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="meioTransporte2.nrIdentificador" disabled="true" />
			</adsm:button>
			<adsm:button caption="proprietarios" action="/contratacaoVeiculos/manterMeiosTransporteProprietarios" cmd="main" >
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporte.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporte2.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="nrFrota" target="meioTransporte2.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="meioTransporte.nrIdentificador" disabled="true" />
			</adsm:button>
			<adsm:button id="motoristas" caption="motoristas" action="/contratacaoVeiculos/manterMeiosTransporteRodoviariosMotorista" cmd="main" >
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporteRodoviario.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporteRodoviario2.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="nrFrota" target="meioTransporteRodoviario2.meioTransporte.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="meioTransporteRodoviario.meioTransporte.nrIdentificador" disabled="true" />
				<adsm:linkProperty src="tpVinculo" target="meioTransporteRodoviario.meioTransporte.tpVinculo" />
			</adsm:button>
			<adsm:button caption="quilometragem" action="/portaria/manterQuilometragensSaidaChegada" cmd="main" breakBefore="true" >
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporteRodoviario.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporteRodoviario2.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="nrFrota" target="meioTransporteRodoviario2.meioTransporte.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="meioTransporteRodoviario.meioTransporte.nrIdentificador" disabled="true" />
			</adsm:button>

			<adsm:button id="btnSolicitacoes" caption="solicitacoesContratacao"
					action="/contratacaoVeiculos/manterSolicitacoesContratacao" cmd="main" boxWidth="172" >
				<adsm:linkProperty src="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
						target="idTipoMeioTransporteTemp" disabled="true" />
				<adsm:linkProperty src="dsTipoMeioTransporteTemp" 
						target="dsTipoMeioTransporteTemp" disabled="true" />
				<adsm:linkProperty src="modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte" 
						target="tipoMeioTransporte.tpMeioTransporte" disabled="true" />

				<adsm:linkProperty src="nrFrota" target="nrIdentificacaoMeioTransp.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="nrIdentificacaoMeioTransp.nrPlaca" disabled="true" />
				<adsm:linkProperty src="nrAnoFabricao" target="nrAnoFabricacaoMeioTransporte" disabled="true" />

				<adsm:linkProperty src="meioTransporteRodoviario2.meioTransporte.nrFrota" 
						target="nrIdentificacaoSemiReboque.nrFrota" disabled="true" />
				<adsm:linkProperty src="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrIdentificador" 
						target="nrIdentificacaoSemiReboque.nrPlaca" disabled="true" />
				<adsm:linkProperty src="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrAnoFabricao" 
						target="nrAnoFabricacaoMeioTransporteSemiReboque" disabled="true" />

				<adsm:linkProperty src="idMeioTransporte" target="meioTransporteRodoviario.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporteRodoviario2.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="nrFrota" target="meioTransporteRodoviario2.meioTransporte.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="meioTransporteRodoviario.meioTransporte.nrIdentificador" disabled="true" />

				<adsm:linkProperty src="meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte"
						target="meioTransporteRodoviarioSemiReboque.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte"
						target="meioTransporteRodoviarioSemiReboque2.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="meioTransporteRodoviario2.meioTransporte.nrFrota" 
						target="meioTransporteRodoviarioSemiReboque2.meioTransporte.nrFrota" disabled="true" />
				<adsm:linkProperty src="meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrIdentificador" 
						target="meioTransporteRodoviarioSemiReboque.meioTransporte.nrIdentificador" disabled="true" />		
			</adsm:button>

			<adsm:button caption="consultarBloqueioLiberacao" action="contratacaoVeiculos/manterBloqueiosMotoristaProprietario" cmd="main" >
				<adsm:linkProperty src="idMeioTransporte" target="meioTransporte.idMeioTransporte" disabled="true" />
				<adsm:linkProperty src="nrFrota" target="meioTransporte.nrFrota" disabled="true" />
				<adsm:linkProperty src="nrIdentificador" target="meioTransporte.nrIdentificador" disabled="true" />
			</adsm:button>
			<adsm:button caption="bloquear" id="bloquear" onclick="onBloquearButtonClick();" disabled="false" />
			<adsm:storeButton service="lms.contratacaoveiculos.manterMeiosTransporteAction.storeCustom" callbackProperty="afterStore" />
			<adsm:newButton />
			<adsm:removeButton service="lms.contratacaoveiculos.manterMeiosTransporteAction.removeByIdCustom" />
		</adsm:buttonBar>

	</td></tr></table>

	</adsm:form>
</adsm:window>
<script type="text/javascript" src="../lib/expedicao.js"></script>
<script>


function meioTransporte_cb(data){
	onPageLoad_cb(data);
	if (getElementValue("idProcessoWorkflow") != "") {
		var form = document.forms[0];
		var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{idMeioTransporte:getElementValue("idProcessoWorkflow")});
		xmit({serviceDataObjects:[sdo]});
		getTabGroup(this.document).setDisabledTab("cad",false);
		getTabGroup(this.document).selectTab(1);
		getTabGroup(this.document).setDisabledTab("pesq",true);
	}
}


	document.getElementById("tpSituacaoAtivo").masterLink = "true";	
	document.getElementById("nrIdentificador").label = i18NLabel.getLabel("meioTransporte");
	var _idMarcaMeioTransporte;
	var _idTipoCombustivel;

	/**
	 * Ao carregar dados na tela, há algumas regras de comportamento.
	 */
	function meioTransporteLoad_cb(data, error) {
		// Senão o número da frota não será carregado se possuir letras.
		obj_nrFrota.mask = "";
		onDataLoad_cb(data,error);
		obj_nrFrota.mask = "999999";
		
		poupulateCamposMeioTransporte(data);
		var tpVinculo = getNestedBeanPropertyValue(data,"tpVinculo.value");
		var tpMeioTransporte = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value");
		setDisabled("motoristas",!(tpMeioTransporte == 'R' && tpVinculo != 'P'));
		setDisabled("btnSolicitacoes",!(tpMeioTransporte == 'R' && (tpVinculo == 'E' || tpVinculo == 'A')));

		var tpVinculo = document.getElementById("tpVinculo");
		if(tpVinculo.value != 'E'){
			setDisabled("nrCodigoBarra",false);
			document.getElementById("preNrCodigoBarra").value = "11";
		}else{
			setDisabled("nrCodigoBarra",true);
		}
		
		var idMeioTransporte = getNestedBeanPropertyValue(data, "idMeioTransporte");
		
		if (idMeioTransporte && idMeioTransporte != undefined){
			disableTabAnexos(false);
		} else {
			disableTabAnexos(true);
		}
		
		nrRastreadorChange();
		loadComboMarcas(data, error);
		loadComboTipoCombustivel(data, error);
		setFocusOnFirstFocusableField();
		
		defineDisabledButtons(
				getElementValue("idProcessoWorkflow"), 
				getNestedBeanPropertyValue(data, "desabilitaCad"));

		
		disableTpVinculoBySituacaoPendencia();
	}
	
	function disableTpVinculoBySituacaoPendencia(){
		setDisabled("tpVinculo",false);
		var tpSituacaoPendencia = getElementValue("tpSituacaoPendencia");
		if(tpSituacaoPendencia != undefined && tpSituacaoPendencia != ""){
			var filialSessao = getFilialSessao();
			if (filialSessao != undefined && "MTZ" == filialSessao.sgFilial){
				if (tpSituacaoPendencia == "C"){
					setDisabled("tpVinculo",true);
				}
			}else{
				setDisabled("tpVinculo",true);
			}
		}
		
	}
	
	function getFilialSessao(){
		var data = new Object();
		
		var tabGroup = getTabGroup(this.document);
		var tabPesq = tabGroup.getTab("pesq");
		data.idFilial = tabPesq.getFormProperty("idFilialSessao");
		data.sgFilial = tabPesq.getFormProperty("sgFilialSessao");
		
		return data;
	}
	
	
	function loadComboMarcas(data, error) {
		if (error == undefined) {
			_idMarcaMeioTransporte = data.modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte;
			var service = "lms.contratacaoveiculos.manterMeiosTransporteAction.findComboMarcaMeioTransporte";
			var criteria = {
				tpMeioTransporte : getElementValue("modeloMeioTransporte.tpMeioTransporte")
			};
			var sdo = createServiceDataObject(service, "loadComboMarcas", criteria);
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function loadComboMarcas_cb(data) {
		comboMarcaMeioTransporte_cb(data);
		setElementValue("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte", _idMarcaMeioTransporte);
	}
	
	function loadComboTipoCombustivel(data, error) {
		if (error == undefined) {
			
			if(data.tipoCombustivel != null){
				_idTipoCombustivel = data.tipoCombustivel.idTipoCombustivel;				
			} else {
				_idTipoCombustivel = "";
			}
			
			var service = "lms.contratacaoveiculos.manterMeiosTransporteAction.findTipoCombustivelByTpMeioTransporte";
			var criteria = {id : getElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")};
			var sdo = createServiceDataObject(service, "loadComboTipoCombustivel", criteria);
			
			xmit({serviceDataObjects:[sdo]});
		}
	}
	
	function loadComboTipoCombustivel_cb(data) {
		validateTipoCombustivel(data);
		setElementValue("tipoCombustivel.idTipoCombustivel", _idTipoCombustivel);
	}
	
	function poupulateCamposMeioTransporte(data,dispareChangeTipo) {
		if (data != undefined) {
			var idFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte");
			var nrFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporteRodoviario.meioTransporte.nrFrota");

			// É necessário preencher via js a segunda lookup de 'Cavalo Mecânico'.				
			if (idFrota != undefined) {
				document.getElementById("meioTransporteRodoviario2.idMeioTransporte").value = idFrota;
				document.getElementById("meioTransporteRodoviario2.meioTransporte.nrFrota").value = nrFrota;
			}

			var tpVinculo = getNestedBeanPropertyValue(data,"tpVinculo.value");
			if (tpVinculo == 'P')
				document.getElementById("nrFrota").required = "true";
			else
				document.getElementById("nrFrota").required = "false";

			doSetDisabledAgregadoFilial(tpVinculo);

			// Se o meio de transporte for Rodoviário, deve mostrar seus agrupamentos.		
			var tpMeioTransporte = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value");
			document.getElementById("modeloMeioTransporte.tpMeioTransporte").value = tpMeioTransporte;
			showCaracsRodoviario(tpMeioTransporte == "R");

			manipulaNrFrota(data);

			var idMeioTransporte = getNestedBeanPropertyValue(data,"idMeioTransporte");
			var idModelo = getNestedBeanPropertyValue(data,"modeloMeioTransporte.idModeloMeioTransporte");
			var idTipo = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
			if (idModelo != undefined) {
				setDisabled("modeloMeioTransporte.idModeloMeioTransporte",false);
				findAtributosByModelo(idTipo,idMeioTransporte);
			}
			// Seta caption do botão bloquear de acordo com seu estado de bloqueio.
			var captionBloquear = getNestedBeanPropertyValue(data,'captionBloquear');
			if (captionBloquear != undefined)
				document.getElementById("bloquear").value = captionBloquear;

			controleTagClick(document.getElementById("meioTransporteRodoviario.blControleTag"));

			if (dispareChangeTipo == undefined || dispareChangeTipo != "true") {
				var idComposto = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
				if (idComposto != undefined && idComposto != "") {
					setDisabledCavalo(false);
				} else {
					setDisabledCavalo(true);			
				}
			}
			populateTipoMeioTransporte(data,dispareChangeTipo);			
		}
	}
	
	function defineDisabledButtons(idProcessoWorkflow, desabilitaCad){
		if ((idProcessoWorkflow != undefined && idProcessoWorkflow != "") || desabilitaCad === 'true') {	
			var elements = document.getElementById('form_idMeioTransporte').getElementsByTagName('input');
			
			for (var i = 0; i < elements.length; i++) {
				var button = elements[i];
				if(button.type == 'button'){
					button.disabled = true;
				}
			}
		}
	}

	function populateTipoMeioTransporte(data,dispareChangeTipo) {
		var id = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
		var descricao = getNestedBeanPropertyValue(data,"dsTipoMeioTransporteTemp");
		setComboBoxElementValue(document.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"), id, id, descricao);

		if (id != undefined && id != "" && dispareChangeTipo != undefined && dispareChangeTipo == "true") {
			comboboxChange({e:document.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte")});
			tipoMeioTransporteChange(document.getElementById("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"));
		}
	}

	/**
	 * Após salvar, é preenchido o número da frota na tela, quando o mesmo for gerado automaticamente.
	 */
	function afterStore_cb(data,exception,key) {
		store_cb(data,exception,key);
		displayWarnings(data);

		if (exception == undefined) {
			if (document.getElementById("nrFrota").value == "") {
				var nrFrotaGerado = getNestedBeanPropertyValue(data,"nrFrota");
				if (nrFrotaGerado != undefined) {
					document.getElementById("nrFrota").value = nrFrotaGerado;
				}
			}

			var tpVinculo = document.getElementById("tpVinculo");
			if(tpVinculo.value != 'E'){
				setDisabled("nrCodigoBarra",false);
				document.getElementById("preNrCodigoBarra").value = "11";
			}else{
				setDisabled("nrCodigoBarra",true);
			}

			var idMeioTransporteRodoviario = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.idMeioTransporte");
			if (idMeioTransporteRodoviario != undefined) {
				document.getElementById("meioTransporteRodoviario.idMeioTransporte").value = idMeioTransporteRodoviario;
				disableTabAnexos(false);
			} else {
				disableTabAnexos(true);
			}

			setDisabled("nrIdentificador",true);
			
			defineDisabledButtons(
				getElementValue("idProcessoWorkflow"), 
				getNestedBeanPropertyValue(data, "desabilitaCad"));
		}
		setFocus("__buttonBar:0.newButton");
	}

	/**
	 * Desabilita nrFrota quando tipo de vínculo não for próprio.
	 * É necessário uma variável global para tratar o comportamento do onchange de tpVinculo.
	 */
	function manipulaNrFrota(data) {
		var tpVinculo = getNestedBeanPropertyValue(data,"tpVinculo.value");
		if (tpVinculo != "P")
			nrFrotaAlteracao = getNestedBeanPropertyValue(data,"nrFrota");
		else
			nrFrotaAlteracao = "";
		setDisableFrota(tpVinculo != "P");
	}

	function disableTabAnexos(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("anexo", disabled);
	}
	/**
	 * tratamento dos eventos da initWindow para <tab_click>, 
	 * <gridRow_click>, <newButton_click> e/ou <removeButton_click> 
	 */
	function initWindow(eventObj) {
		if (eventObj.name == "gridRow_click") {
			// No onDataLoadCallBack, é desabilitado campos de acordo com seus valores.
			showAllSections(false);
			setDisabled("nrIdentificador",true);
		} else if (eventObj.name != "storeButton") {
			if(eventObj.name == "newButton_click"){
				estadoNovo();
				nrFrotaAlteracao = "";
				populateFilial();
				doSetDisabledAgregadoFilial(tpVinculo);
				disableTabAnexos(true);
			}
			
			else if(eventObj.src.tabGroup && eventObj.src.tabGroup.oldSelectedTab.properties.id == "pesq"){					
				estadoNovo();
				nrFrotaAlteracao = "";
				populateFilial();
				doSetDisabledAgregadoFilial(tpVinculo);
				disableTabAnexos(true);
			}
		} else { // storeButton
			var tpMeioTransporte = getElementValue("modeloMeioTransporte.tpMeioTransporte");
			var tpVinculo = getElementValue("tpVinculo");
			setDisabled("motoristas",!(tpMeioTransporte == 'R' && tpVinculo != 'P'));
			setDisabled("btnSolicitacoes",!(tpMeioTransporte == 'R' && (tpVinculo == 'E' || tpVinculo == 'A')));
		}
		setDisabled("modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte",
				(eventObj.name == "gridRow_click" || eventObj.name == "storeButton"));
		nrRastreadorChange();
		if (eventObj.name != "storeButton") {
			setFocusOnFirstFocusableField();
		}
		
		if(eventObj.name == "tab_click" || eventObj.name == "newButton_click"){
			findComboTipoCombustivel();	
		}
		
		defineDisabledButtons(getElementValue("idProcessoWorkflow"), getElementValue("desabilitaCad"));	
	}

	/**
	 * OnChange do campo Tipo de vínculo. 
	 * Desabilita campo nrFrota quanto tipo de vínculo for Próprio (P).
	 */
	function tpVinculoChange(e) {
		if (e.value != 'P') {
			setDisableFrota(true);
			document.getElementById("nrFrota").required = "false";
			if (nrFrotaAlteracao != undefined)
				document.getElementById("nrFrota").value = nrFrotaAlteracao;
		} else {
			setDisableFrota(false);
			resetValue("nrFrota");
			document.getElementById("nrFrota").required = "true";
		}

		var codigoBarra = document.getElementById("nrCodigoBarra").value;
		
		if(e.value != 'E'){
			setDisabled("nrCodigoBarra",false);
			document.getElementById("preNrCodigoBarra").value = "11";
		}else{
			resetValue("nrCodigoBarra");
			resetValue("preNrCodigoBarra");
			setDisabled("nrCodigoBarra",true);
		}
		
		if (e.value != 'A') {
			resetValue("filialAgregadoCe.idFilial");
		}
		doSetDisabledAgregadoFilial(e.value);
	}

	function populateFilial() {
		var tabGroup = getTabGroup(this.document);
		var tabPesq = tabGroup.getTab("pesq");
		setElementValue("filial.idFilial",tabPesq.getFormProperty("idFilialSessao"));
		setElementValue("filial.sgFilial",tabPesq.getFormProperty("sgFilialSessao"));
		setElementValue("filial.pessoa.nmFantasia",tabPesq.getFormProperty("nmFilialSessao"));
	}

	/**
	 * Estado da tela ao clicar no botão novo.
	 */
	function estadoNovo() {
		// Esconde todos agrupamentos.
		showAllSections(false);	

		// Desabilita alguns campos contidos na especificação.
		setDisabled("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte",true);
		setDisabled("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",true);
		setDisabled("modeloMeioTransporte.idModeloMeioTransporte",true);
		setDisableFrota(true);
		setDisabled("nrIdentificador",false);		

		// Desabilita lookup de Cavalo mecânico
		setDisabled("meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte",true);
		setDisabled("meioTransporteRodoviario2.idMeioTransporte",true);

		doSetDisabledAgregadoFilial(getElementValue("tpVinculo"));
	}

	function setDisableFrota(value) {
		setDisabled("nrFrota",value);
	}

	/**
	 * Altera visibilidade dos agrupamentos relacionados ao transporte rodoviário.
	 * param true torna visível
	 * param false torna invisível
	 * 
	 * Quando é invisível, os campos não são mais obrigatórios.
	 */
	function showCaracsRodoviario(valueBoolean) {
		var value = (valueBoolean) ? "" : "none";

		document.getElementById("caracGerais").style.display = value;
		document.getElementById("infDoc").style.display = value;

		document.getElementById("meioTransporteRodoviario.eixosTipoMeioTransporte.idEixosTipoMeioTransporte").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.cdRenavam").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.nrCertificado").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.dtEmissao").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.nrChassi").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.nrBilheteSeguro").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.dtVencimentoSeguro").required = new String(valueBoolean);
		document.getElementById("meioTransporteRodoviario.municipio.nmMunicipio").required = new String(valueBoolean);

		var checked = document.getElementById("meioTransporteRodoviario.blControleTag").checked;
		document.getElementById("meioTransporteRodoviario_nrTag_req").style.visibility = (checked && valueBoolean) ? "visible" : "hidden";
		document.getElementById("meioTransporteRodoviario.nrTag").required = (checked && valueBoolean) ? "true" : "false";

		var valueMct = getElementValue("meioTransporteRodoviario.operadoraMct.idOperadoraMct");
		document.getElementById("meioTransporteRodoviario_nrRastreador_req").style.visibility =
				(valueMct != undefined && valueMct != "" && valueBoolean) ? "visible" : "hidden";
		document.getElementById("meioTransporteRodoviario.nrRastreador").required = 
				(valueMct != undefined && valueMct != "" && valueBoolean) ? "true" : "false";
	}

	/**
	 * Altera visibilidade do agrupamento 'Outras Informações'.
	 * Remove todos os campos que foram gerados anteriormente.
	 * param true torna visível
	 * param false torna invisível
	 */
	function showOutrasInformacaoes(valueBoolean) {
		var value = (valueBoolean) ? "" : "none";

		document.getElementById("outrasInf").style.display = value;
		if (!valueBoolean)
			document.getElementById("outrasInf").innerHTML = "";
	}

	/**
	 * Altera visibilidade de todos agrupamentos.
	 * param true torna visível
	 * param false torna invisível
	 */
	function showAllSections(valueBoolean) {
		var value = (value) ? "" : "none";

		showCaracsRodoviario(valueBoolean);
		showOutrasInformacaoes(valueBoolean);
	}	

	/**
	 * Desabilita/Habilita lookup de Cavalo mecânico
	 * Limpa cavalo quando desabilita-o.
	 */	
	function setDisabledCavalo(value) {
		setDisabled("meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte",value);
		setDisabled("meioTransporteRodoviario2.idMeioTransporte",value);
		if (value) {
			resetValue(document.getElementById("meioTransporteRodoviario.meioTransporteRodoviario.idMeioTransporte"));
			resetValue(document.getElementById("meioTransporteRodoviario2.idMeioTransporte"));
		}
	}

	/**
	 * CallBack de modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte
	 * Habilita Tipo de Meio Transporte quando vazio.
	 * Cria lista com os ids dos tipos de meios de transporte compostos.
	 */
	function comboTipoMeioTransporte_cb(data) {
		setDisabled("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",false);
		setDisabled("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte",false);

		modeloMeioTransporte_tipoMeioTransporte_idTipoMeioTransporte_cb(data);

		if (DATA_TIPO != undefined) {
			var data = DATA_TIPO;
			DATA_TIPO = undefined;
			poupulateCamposMeioTransporte(data,"true");
		}
	}

	/**
	 * CallBack de modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte
	 * Habilita Marca de Meio Transporte quando vazio.
	 */
	function comboMarcaMeioTransporte_cb(data) {
		setDisabled("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",false);
		setDisabled("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte",false);
		modeloMeioTransporte_marcaMeioTransporte_idMarcaMeioTransporte_cb(data);
	}

	/**
	 * CallBack de modeloMeioTransporte.idModeloMeioTransporte
	 * Habilita Modelo de Meio Transporte quando vazio.
	 */
	function comboModeloMeioTransporte_cb(data) {
		var idMarca = getElementValue("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte");
		var idTipoMeio = getElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
		setDisabled("modeloMeioTransporte.idModeloMeioTransporte", 
			!((data != null && data.length > 0) &&
			  (idMarca != "" && idTipoMeio != ""))
		);
		modeloMeioTransporte_idModeloMeioTransporte_cb(data);
	}

	/**
	 * Onchange de Onchange de modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte
	 * Trata comportamento da tag de Cavalo mecânico.
	 */
	function tipoMeioTransporteChange(e) {
		// Invoca pnChange padrão para Marca e Tipo de meio de transporte.
		marcaAndTipoChange(e);
		findAtributosByModelo(e.value,document.getElementById("idMeioTransporte").value);

		var id = e.value;
		if (id != undefined && id != "") {
			validateTipoMeioTransporte(id);
		} else {	
			setDisabledCavalo(true);
			findComboTipoCombustivel();
		}
	}

	function validateTipoMeioTransporte(id) {
		var data = new Array();
		setNestedBeanPropertyValue(data,"id",id);
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.validateTipoMeioTransporte",
				"validateTipoMeioTransporte",data);
		xmit({serviceDataObjects:[sdo]});
		
		findComboTipoCombustivelByTpMeioTransporte(data);
	}

	function validateTipoMeioTransporte_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}

		var idComposto = getNestedBeanPropertyValue(data,"idComposto");
		if (idComposto != undefined && idComposto != "") {
			setDisabledCavalo(false);
			setElementValue("modeloMeioTransporte.tipoMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",
					idComposto);
		} else {
			setDisabledCavalo(true);			
		}
	}

	function findComboTipoCombustivelByTpMeioTransporte(data){
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.findTipoCombustivelByTpMeioTransporte",
				"findComboTipoCombustivelByTpMeioTransporte",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findComboTipoCombustivelByTpMeioTransporte_cb(data, error){
		if(error != undefined){
			alert(error);
			return false;
		}
		
		validateTipoCombustivel(data);
	}
	
	function validateTipoCombustivel(data){
		if(data != null && data != ""){
			setDisabled("tipoCombustivel.idTipoCombustivel", false);			
			tipoCombustivel_idTipoCombustivel_cb(data);
		} else {
			setDisabled("tipoCombustivel.idTipoCombustivel", true);
			setElementValue("tipoCombustivel.idTipoCombustivel", "");			
		}
	}
	
	function findComboTipoCombustivel(){
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.findComboTipoCombustivel",
				"findComboTipoCombustivel", null);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findComboTipoCombustivel_cb(data, error){
		if(error != undefined){
			alert(error);
			return false;
		}
		
		validateTipoCombustivel(data);
	}

	/**
	 * Onchange de tpMeioTransporte
	 * 
	 * Desabilita tipo e marca quando combo meio transporte for vazia.
	 */
	function meioTransporteChange(e) {
		document.getElementById("modeloMeioTransporte.tpMeioTransporte").value = e.value;
		if (e.value == "") {
			setDisabled("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte",true);
			setDisabled("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",true);
			setDisabled("modeloMeioTransporte.idModeloMeioTransporte",true);
			showCaracsRodoviario(false);
		} else if (e.value == 'R') {
			showCaracsRodoviario(true);
		} else {
			showCaracsRodoviario(false);
		}
		showOutrasInformacaoes(true);
		findAtributosByModelo(e.value,document.getElementById("idMeioTransporte").value);
		comboboxChange({e:e});
		findComboTipoCombustivel();
	}

	/**
	 * Onchange de modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte
	 * Onchange de modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte
	 *
	 * Desabilita Modelo de Meio de transporte quando alguma das combos for vazia.
	 */
	function marcaAndTipoChange(e) {
		if (e.value == "") {
			setDisabled("modeloMeioTransporte.idModeloMeioTransporte",true);
		}
		comboboxChange({e:e});
	}

	/**
	 * xmit que encontrará componentes de um modelo de meio de transporte.
	 */
	function findAtributosByModelo(idModelo,idMeioTransporte) {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idModelo",idModelo);
		setNestedBeanPropertyValue(data,"idMeioTransporte",idMeioTransporte);

		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.findAtributosByModelo",
				"findAtributosByModeloVolta",data);
		xmit({serviceDataObjects:[sdo]});
	}

	/**
	 * Onchange de modeloMeioTransporte
	 */
	function modeloMeioTransporteChange(e) {
	}

	function findAtributosByModeloVolta_cb(data,exception) {
		if (exception) {
			alert(exception);
			return false;
		}
		gerarOutrasInformaoes(data);
		return true;
	}

//#################################################
// FUNÇÕES JS PARA GERAR OS COMPONENTES NA TELA.
//#################################################

	function getLabelHTML(caption,width) {
		return '<td style="text-overflow : ellipsis; overflow : hidden;" class="FmLbRequerido" colspan="' 
				+ width + '">' + caption + ':</td>';
	}

	function getTextBoxHTML(property,width,size,datatype,required,value,charsInteiro,charsDecimal) {
		var textBox = "";

		textBox += '<td class="FmLbRequeridoFont" colspan="' + width + '" valign="top">';
		textBox += '<input type="text" name="' + property + '" size="' + size + '" id="' + property + '" this.select(); ';

		if (required || required == "true")
			textBox += ' required="true" ';

		textBox += 'onkeyup="return blockLength(this,event,false,true);" onkeypress="return setMask(this, event);" ';
		textBox += 'onchange="return validate(this);" onfocus="unformat(this); this.select();" ';
		textBox += 'onbeforedeactivate="format(this); return onBeforeDeactivateElement(this, undefined)" ';

		if (datatype == "date") {
			textBox += ' mask="dd/MM/yyyy" maxChars="10" dataType="JTDate" ';
			if (value != undefined)
				textBox += ' value="' + formatDate(value,"yyyy-MM-dd","dd/MM/yyyy") + '" ';
		} else {
			textBox+= " style=\"text-align:right\"";

			var mascara = geraMask(charsInteiro,charsDecimal);

			textBox += ' mask="' + mascara + '" maxChars="' + (parseInt(charsInteiro) + parseInt(charsDecimal)) + '" ';

			if (value != undefined) {
				if (datatype == "integer")
					textBox += ' value="' + formatNumber(value.replace('.',decimal),mascara) + '" ';
				else
					textBox += ' value="' + value + '" ';
			}
		}

		if (datatype == "integer") {
			textBox += ' dataType="' + ((charsDecimal != 0 && charsDecimal != undefined && charsDecimal != "") ? "currency" : "integer") +'" ';
		}

		textBox += 'serializable="true" />'; 

		if (datatype == "date") {
			textBox += ' <a href="#" border="0" onclick=\'openCalendar(document.getElementById("' + property + '"));\'>';
			textBox += '<img src="../images/small_cal.gif" width="15" height="15" border="0" align="absmiddle"/></a>';
		}

		if (required || required == "true")
			textBox += '&nbsp;<font size="2" color="red">*</font>';

		textBox += '</td>';
		return textBox;
	}

	function geraMask(inteiro,decimal) {
		var retorno = "";
		var t;
		if (decimal == null || decimal == 0 || decimal == "" || decimal == undefined)
			decimal = 0;
		for ( t = 0 ; t < inteiro -1 - decimal ; t++ ) {
			retorno += "#";
		}
		for(x = retorno.length; x > 0; x--) {
			x-= 2;
			if (x > 0)
				retorno = retorno.substring(0,x) + "," + retorno.substring(x);
		}
		if (decimal != 0) {
			retorno += "0.";
			for ( t = 0 ; t <= decimal -1 ; t++ ) {
				retorno += "0";
			}
		}else
			retorno+= "#";

		return retorno;
	}

	function getComboBoxHTML(property,width,required,value) {
		var comboBox = "";
		comboBox += '<td class="FmLbRequeridoFont" colspan="' + width + '" valign="top">';
		comboBox += '<select name="' + property + '" id="' + property + '" serializable="true" ';
		if (required || required == "true")
			comboBox += ' required="true" ';
		if (value != undefined)
			comboBox += 'value="' + value + '"';
		comboBox += 'onkeyup="return blockLength(this,event,true,true);" onchange="comboboxChange({e:this});" >';
		comboBox += '<option>Selecione...</option>';
		comboBox += '</select>';
		if (required || required == "true")
			comboBox += '&nbsp;<font size="2" color="red">*</font>';
		comboBox += '</td>';

		return comboBox;
	}

	function getCheckBoxHTML(property,width,value) {
		var checkBox = "";
		checkBox += '<td class="FmLbRequeridoFont" colspan="' + width + '" valign="top">';
		checkBox += '<input type="checkbox" name="' + property + '" id="' + property + '" ';
		checkBox += 'style="border: 0px;" serializable="true" ';
		if (value != undefined) {
			checkBox += ' value="' + value + '" ';
			if (value == "true")
				checkBox += ' checked ';
		}
		checkBox += 'onclick="setCheckboxCommonValue(this);" />';
		checkBox += '</td>';

		return checkBox;
	}

//##############################################################################
// FUNÇÕES JS PARA GERAR OS COMPONENTES EXCLUSIVOS DA TELA MEIO DE TRANSPORTE.
//##############################################################################
//	function geraComponenteArea(caption,property1,property2,required);	
//	function geraComponenteVolume(caption,property1,property2,property3,required);	
//	function geraComponenteCaixaTexto(caption,property,required);	
//	function geraComponenteComboBox(caption,property,required,listIds,listTexts);	
//	function geraComponenteCheckBox(caption,property);

	function geraComponenteArea(caption,property1,property2,value1,value2,required,charsInteiro,charsDecimal) {
		comp = '<tr>';
		comp += getLabelHTML(caption,20);
		comp += getTextBoxHTML(property1,(required) ? 18 : (18 - 1),21,"integer",required,value1,charsInteiro,charsDecimal);
		comp += getTextBoxHTML(property2,(required) ? (80 - 18) : ((80 - 18) + 2),21,"integer",required,value2,charsInteiro,charsDecimal);
		comp += '</tr>';
		return comp;
	}

	function geraComponenteVolume(caption,property1,property2,property3,value1,value2,value3,required,charsInteiro,charsDecimal) {
		comp = '<tr>';
		comp += getLabelHTML(caption,20);
		comp += getTextBoxHTML(property1,(required) ? 18 : (18 - 1),21,"integer",required,value1,charsInteiro,charsDecimal);
		comp += getTextBoxHTML(property2,(required) ? 18 : (18 - 1),21,"integer",required,value2,charsInteiro,charsDecimal);
		comp += getTextBoxHTML(property3,(required) ? (80 - (18 * 2)) : ((80 - (18 * 2)) + 2),21,"integer",required,value3,charsInteiro,charsDecimal);
		comp += '</tr>';
		return comp;
	}

	function geraComponenteCaixaTexto(caption,property,required,datatype,value,size,charsInteiro,charsDecimal) {
		comp = '<tr>';
		comp += getLabelHTML(caption,20);
		comp += getTextBoxHTML(property,80,size,datatype,required,value,charsInteiro,charsDecimal);
		comp += '</tr>';
		return comp;
	}

	function geraComponenteComboBox(caption,property,required,value) {
		comp = '<tr>';
		comp += getLabelHTML(caption,20);
		comp += getComboBoxHTML(property,80,required,value);
		comp += '</tr>';
		return comp;
	}

	function geraComponenteCheckBox(caption,property,value) {
		comp = '<tr>';
		comp += getLabelHTML(caption,20);
		comp += getCheckBoxHTML(property,80,value);
		comp += '</tr>';
		return comp;
	}

	/**
	 * Recebe um data que contém dados da tabela ModeloMeioTranspAtributo.
	 * Renderiza (data.length + 1) elementos na tela.
	 */
	function gerarOutrasInformaoes(data) {
		// Primeiramente esconde e reseta o agrupamento Outras Informações
		showOutrasInformacaoes(false);
		var conteudo = "";

		if (data != undefined && data.length > 0) {
			conteudo += '<adsm:section caption="outrasInformacoes" />';
			conteudo += geraColunas();

			var tpComponente = "";
			var tpInformacao = "";
			var caption = "";
			var required = "";
			var property = "";
			var conteudoAtrib = "";
			var valoresAtribs = "";
			var decimais = "";
			var tamanho = "";

			var listIds = "";
			var listTexts = "";

			var combos = new Array();
			var dataCombos = new Array();
			var valuesCombos = new Array();

			var i;
			// Percorre pelo data para gerar todos os componentes
			for (i = 0 ; i < data.length ; i++) {
				tpComponente = getNestedBeanPropertyValue(data,":" + i + ".atributoMeioTransporte.tpComponente.value");
				caption = getNestedBeanPropertyValue(data,":" + i + ".atributoMeioTransporte.dsAtributoMeioTransporte");
				required = getNestedBeanPropertyValue(data,":" + i + ".blOpcional") == "false";
				property = getNestedBeanPropertyValue(data,":" + i + ".idModeloMeioTranspAtributo");
				valoresAtribs = getNestedBeanPropertyValue(data,":" + i + ".meioTranspConteudoAtribs");
				tamanho = getNestedBeanPropertyValue(data,":" + i + ".atributoMeioTransporte.nrTamanho");
				decimais = getNestedBeanPropertyValue(data,":" + i + ".atributoMeioTransporte.nrDecimais");

				if (tpComponente != "M")
					property = "atrib.elem" + property;
				// É componente tipo Área?
				if (tpComponente == "A") {
					conteudo += geraComponenteArea(caption,property + '.v1',property + '.v2',
							(valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":0.dsConteudo"),
							(valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":1.dsConteudo"),
							required,tamanho,decimais);
				// É componente tipo Volume?
				} else if (tpComponente == "V") {
					conteudo += geraComponenteVolume(caption,property + '.v1',property + '.v2',property + '.v3',
							(valoresAtribs == undefined) ? "" : getNestedBeanPropertyValue(valoresAtribs,":0.dsConteudo"),
							(valoresAtribs == undefined) ? "" : getNestedBeanPropertyValue(valoresAtribs,":1.dsConteudo"),
							(valoresAtribs == undefined) ? "" : getNestedBeanPropertyValue(valoresAtribs,":2.dsConteudo"),
							required,tamanho,decimais);
				// É componente tipo Multi-seleção?
				} else if (tpComponente == "M") {
					conteudoAtrib = getNestedBeanPropertyValue(data,":" + i + ".conteudoAtributoModelos");
					combos.push("atrib.comb" + property);
					dataCombos.push(conteudoAtrib);
					valuesCombos.push((valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":0.idConteudoAtributoModelo"));
					conteudo += geraComponenteComboBox(caption,"atrib.comb" + property,required);
				// É componente tipo Sim/Não?
				} else if (tpComponente == "S") {
					conteudo += geraComponenteCheckBox(caption,property,
							(valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":0.dsConteudo"));
				// Então é tipo Caixa de Texto!
				} else {
					tpInformacao = getNestedBeanPropertyValue(data,":" + i + ".atributoMeioTransporte.tpInformacao.value");
					if (tpInformacao == "N") {
						conteudo += geraComponenteCaixaTexto(caption,property,required,"integer",
								(valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":0.dsConteudo"),25,
								tamanho,decimais);
					} else if (tpInformacao == "D") {
						conteudo += geraComponenteCaixaTexto(caption,property,required,"date",
								(valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":0.dsConteudo"),10,
								0,0);
					} else {
						conteudo += geraComponenteCaixaTexto(caption,property,required,"text",
								(valoresAtribs == undefined) ? "" :getNestedBeanPropertyValue(valoresAtribs,":0.dsConteudo"),25,
								tamanho,0);
					}
				}
			}

			conteudo += '</table>';

			// Por fim é apresentado os componentes:
			document.getElementById("outrasInf").innerHTML = conteudo;

			// Populando as combos:
			for (i = 0; i < combos.length ; i++) {
				document.getElementById(combos[i]).data = dataCombos[i];
				document.getElementById(combos[i]).propertyMappings = [];
				if (dataCombos[i] != undefined) {
					for (j = 0 ; j < dataCombos[i].length ; j++)
						document.getElementById(combos[i]).options[j+1] = new Option(
								getNestedBeanPropertyValue(dataCombos[i],":" + j + ".dsConteudoAtributoModelo"),
								getNestedBeanPropertyValue(dataCombos[i],":" + j + ".idConteudoAtributoModelo")
					);
					if (valuesCombos[i] != undefined && valuesCombos[i] != "")
						document.getElementById(combos[i]).value = valuesCombos[i];
				}
			}

			for (i = 0 ; i < data.length ; i++) {
				caption = getNestedBeanPropertyValue(data,":" + i + ".atributoMeioTransporte.dsAtributoMeioTransporte");
				property = getNestedBeanPropertyValue(data,":" + i + ".idModeloMeioTranspAtributo");

				if (tpComponente != "M") {
					property = "atrib.elem" + property;
				} else {
					property = "atrib.comb" + property;
				}

				document.getElementById(property).label = caption;
			}

			showOutrasInformacaoes(true);

			/** Registra os eventos de onclick dos elementos criados */
			tab_registerFormFieldsEventListeners(this.document);
		}
	}

	function onAlturaBauChange(elem) {
		calculaCapacidade();
	}

	function onLarguraBauChange(elem) {
		calculaCapacidade();
	}

	function onProfundidadeBauChange(elem) {
		calculaCapacidade();
	}

	function calculaCapacidade() {
		var altura = getElementValue("meioTransporteRodoviario.vlAlturaBau");
		var largura = getElementValue("meioTransporteRodoviario.vlLarguraBau");
		var profundidade = getElementValue("meioTransporteRodoviario.vlProfundidadeBau");

		if (altura == "" || largura == "" || profundidade == "")
			resetValue("nrCapacidadeM3");
		else {
			var data = new Array();
			setNestedBeanPropertyValue(data,"vlAlturaBau",altura);
			setNestedBeanPropertyValue(data,"vlLarguraBau",largura);
			setNestedBeanPropertyValue(data,"vlProfundidadeBau",profundidade);

			var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.calculaCapacidade",
					"calculaCapacidade",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	function calculaCapacidade_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		if (data != undefined) {
			var capacidade = getNestedBeanPropertyValue(data,"_value");
			setElementValue("nrCapacidadeM3", capacidade);
		}
	}

	function nrCapacidadeChange() {
		resetValue("meioTransporteRodoviario.vlAlturaBau");
		resetValue("meioTransporteRodoviario.vlLarguraBau");
		resetValue("meioTransporteRodoviario.vlProfundidadeBau");
		return true;
	}

	document.getElementById("meioTransporteRodoviario_nrTag_req").style.visibility = "hidden";
	function controleTagClick(elem) {
		var checked = elem.checked;
		if (checked == undefined){
			checked = document.getElementById("meioTransporteRodoviario.blControleTag").checked;
		}

		setDisabled("meioTransporteRodoviario.nrTag",!checked);
		if (!checked) {
			resetValue("meioTransporteRodoviario.nrTag");
		}

		document.getElementById("meioTransporteRodoviario_nrTag_req").style.visibility = (checked) ? "visible" : "hidden";
		document.getElementById("meioTransporteRodoviario.nrTag").required = (checked) ? "true" : "false";
	}

	document.getElementById("meioTransporteRodoviario_nrRastreador_req").style.visibility = "hidden";
	function operadoraMctChange() {
		comboboxChange({e:document.getElementById("meioTransporteRodoviario.operadoraMct.idOperadoraMct")});
		var value = getElementValue("meioTransporteRodoviario.operadoraMct.idOperadoraMct");

		document.getElementById("meioTransporteRodoviario_nrRastreador_req").style.visibility =
				(value != undefined && value != "") ? "visible" : "hidden";
		document.getElementById("meioTransporteRodoviario.nrRastreador").required = 
				(value != undefined && value != "") ? "true" : "false";
	}

	function onNrIdentificadorChange(elem) {
		elem.value = elem.value.toUpperCase();
		validateInfoMeioTransporteByNrPlaca();
		return validate(elem);
	}
	
	// ############################################################
	// trata o clique no botão bloquear
	// ############################################################
	function onBloquearButtonClick() {
		showModalDialog('contratacaoVeiculos/popupBloqueios.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:250px;');
		verificaBloqueiosVigentes();
	}

	// ############################################################
	// verifica se há bloqueios vigentes para o proprietario
	// ############################################################
	function verificaBloqueiosVigentes() {
		var data = new Array();
		setNestedBeanPropertyValue(data, "idMeioTransporte", getElementValue("idMeioTransporte"));
		var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.validateBloqueiosVigentes","verificaBloqueiosVigentes",data);
		xmit({serviceDataObjects:[sdo]});
	}

	function verificaBloqueiosVigentes_cb (data, exception) {
		if (exception != undefined) {
			alert(exception);
			return false;
		} else if (data.valorBotaoBloqueio != undefined) {
			if (data.valorBotaoBloqueio == "desbloquear") {
				document.getElementById("bloquear").value = i18NLabel.getLabel("liberar");
				setElementValue('tpStatus', i18NLabel.getLabel("bloqueado"));
			} else {
				document.getElementById("bloquear").value = i18NLabel.getLabel("bloquear");		
				setElementValue('tpStatus', i18NLabel.getLabel("liberado"));				
			}
			return true;
		}
	}

	function nrRastreadorChange(e) {
		if (getElementValue("meioTransporteRodoviario.nrRastreador") == "") {
			setDisabled("meioTransporteRodoviario.blMonitorado", true);
			if (e != undefined) {
				setElementValue("meioTransporteRodoviario.blMonitorado", "false");
			}
			while (document.getElementById("meioTransportePerifericos").length > 0) {
				document.getElementById("meioTransportePerifericos")[0] = null;
			}
			meioTransportePerifericosListboxDef.cleanRelateds();
			document.getElementById("meioTransportePerifericos").required = "false";
			setRowVisibility("meioTransportePerifericos", false);
			if (e != null && e.name == "meioTransporteRodoviario.nrRastreador") {
				setFocus("meioTransporteRodoviario.psTara");
			}
		} else {
			if (e != undefined) {
				setElementValue("meioTransporteRodoviario.blMonitorado", "true");
			}
			setDisabled("meioTransporteRodoviario.blMonitorado", false);
			document.getElementById("meioTransportePerifericos").required = "true";
			setRowVisibility("meioTransportePerifericos", true);
			if (e != null && e.name == "meioTransporteRodoviario.nrRastreador") {
				setFocus("meioTransportePerifericos_meioTransportePerifericos.idPerifericoRastreador");
			}
			
		}
	}

	function doSetDisabledAgregadoFilial(tpVinculo) {
		var disable = ( (tpVinculo != 'A') || (getElementValue("idMeioTransporte") == "") );
		setDisabled("filialAgregadoCe.idFilial", disable);
	}

	function eixosTipoMeioTransporteLoad_cb(data, exception) {
		meioTransporteRodoviario_eixosTipoMeioTransporte_idEixosTipoMeioTransporte_cb(data);
		if (data.length == 1) {
			document.getElementById("meioTransporteRodoviario.eixosTipoMeioTransporte.idEixosTipoMeioTransporte").selectedIndex = 1;
		}
	}

	function validateInfoMeioTransporteByNrPlaca() {
		var nrPlaca = getElementValue("nrIdentificador");
		if (nrPlaca != undefined && nrPlaca != '') {
			var data = new Array();
			setNestedBeanPropertyValue(data, "tpVinculo", getElementValue("tpVinculo"));
			setNestedBeanPropertyValue(data, "nrIdentificador", nrPlaca);
			setNestedBeanPropertyValue(data, "idTipoMeioTransporte",
					getElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte"));
			var sdo = createServiceDataObject("lms.contratacaoveiculos.manterMeiosTransporteAction.validateInfoMeioTransporteByNrPlaca",
					"validateInfoMeioTransporteByNrPlaca",data);
			xmit({serviceDataObjects:[sdo]});
		}
	}

	var DATA_TIPO = undefined;
	function validateInfoMeioTransporteByNrPlaca_cb(data,exception) {
		if (exception != undefined) {
			alert(exception);
			fillFormWithFormBeanData(document.forms[0].tabIndex, data);
			setFocus("nrIdentificador");
		} else if (data != undefined) {
			if (getNestedBeanPropertyValue(data,"flag") == undefined) {
				var oldTp = getElementValue("modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte");
				var actualTp = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte.value");
				fillFormWithFormBeanData(document.forms[0].tabIndex, data);

				var idTipo = getNestedBeanPropertyValue(data,"modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte");
				if (idTipo != undefined && getElementValue("modeloMeioTransporte.marcaMeioTransporte.idMarcaMeioTransporte") != "") {
					setDisabled("modeloMeioTransporte.idModeloMeioTransporte",false);
				}

				if (actualTp != undefined && oldTp != actualTp) {
					DATA_TIPO = data;
					notifyElementListeners({e:document.getElementById("modeloMeioTransporte.tipoMeioTransporte.tpMeioTransporte")});
				} else {
					poupulateCamposMeioTransporte(data,"true");
				}
			}
		}
	}

</script>