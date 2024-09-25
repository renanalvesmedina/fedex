<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.municipios.manterFiliaisAction"
	onPageLoad="myOnPageLoad" onPageLoadCallBack="pageLoad">
<script language="javascript" type="text/javascript">
<!--
	function myOnPageLoad() {
		onPageLoad();

		initPessoaWidget( {
			tpTipoElement:document.getElementById("pessoa.tpPessoa"),
			tpIdentificacaoElement:document.getElementById("pessoa.tpIdentificacao"),
			numberElement:document.getElementById("pessoa.idPessoa")
		});
	}

	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		changeTypePessoaWidget({tpTipoElement:getElement("pessoa.tpPessoa"), tpIdentificacaoElement:getElement("pessoa.tpIdentificacao"), numberElement:getElement("pessoa.nrIdentificacao"), tabCmd:'cad'})

		if (getElementValue("idProcessoWorkflow") != "") {
			var form = document.forms[0];
			var sdo = createServiceDataObject(form.service,form.onDataLoadCallBack,{id:getElementValue("idProcessoWorkflow")});
			xmit({serviceDataObjects:[sdo]});
			getTabGroup(this.document).selectTab(1);
			getTabGroup(this.document).setDisabledTab("pesq", true);
		}
		getElement("pessoa.tpIdentificacao").required = "false";
		getElement("pessoa.nrIdentificacao").required = "false";
		getElement("pessoa.idPessoa").required = "false";
	}

	function changeFuncionario(property) {
		resetValue(property + ".idUsuario");
		resetValue(property + ".nrMatricula");
	}
//-->
</script>
	<adsm:i18nLabels>
		<adsm:include key="requiredField" />
		<adsm:include key="filial" />
		<adsm:include key="LMS-29104" />
		<adsm:include key="LMS-29146" />
	</adsm:i18nLabels>

	<adsm:form action="/municipios/manterFiliais" id="form"
		idProperty="idFilial" height="345" onDataLoadCallBack="populaGrid">
		<adsm:hidden property="flagResp" serializable="false" value="R" />
		<adsm:hidden property="flagCia" serializable="false" value="C" />
		<adsm:hidden property="tpSituacao" serializable="false" value="A" />
		<adsm:hidden property="flag" serializable="false" value="telaFilial" />
		<adsm:hidden property="empresa.tpEmpresa" />
		<adsm:hidden property="empresa.tpEmpresaF" serializable="false" value="F" />

		<adsm:hidden property="idProcessoWorkflow" />
		<adsm:hidden property="blDisablePrevisaoOperacaoInicial" serializable="false" />
		<adsm:hidden property="pessoa.tpPessoa" value="J" serializable="true" />

		<adsm:hidden property="nomeConcatenado" serializable="false" />
		<adsm:lookup label="empresa" property="empresa" idProperty="idEmpresa"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.municipios.empresaService.findLookupFilial"
			action="/municipios/manterEmpresas" labelWidth="21%" dataType="text"
			size="20" maxLength="20" width="19%" required="true"
			onDataLoadCallBack="customizado"
			onPopupSetValue="verificaTipoEmpresa"
			onchange="return changeEmpresa();">
			<adsm:propertyMapping criteriaProperty="flag" modelProperty="flag"
				inlineQuery="false" />
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"
				relatedProperty="empresa.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpEmpresa.value"
				relatedProperty="empresa.tpEmpresa" />
			<adsm:propertyMapping modelProperty="tpSituacao"
				criteriaProperty="tpSituacao" />
			<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa"
				size="50" disabled="true" serializable="false" width="40%" />
		</adsm:lookup>

		<adsm:combobox label="tipoFilial"
			property="lastHistoricoFilial.tpFilial" domain="DM_TIPO_FILIAL"
			required="true" labelWidth="21%" width="79%"
			onchange="return verificaTipoFilial();" />

		<adsm:combobox label="tipoSistema"
			property="tpSistema" domain="DM_TP_SISTEMA"
			required="false" labelWidth="21%" width="79%"
		/>

		<adsm:complement labelWidth="21%" width="79%" label="identificacao">
			<adsm:combobox definition="TIPO_IDENTIFICACAO_PESSOA.cad"
				required="false" />
			<adsm:lookup definition="IDENTIFICACAO_PESSOA" required="false"
				service="lms.municipios.filialService.validateIdentificacao"
				onDataLoadCallBack="lookupIdentificacao" />
		</adsm:complement>

		<adsm:textbox label="codigoFilial" maxLength="3" size="2" dataType="integer" 
			property="codigoFilial" labelWidth="21%" width="29%" />

		<adsm:textbox dataType="text" onchange="return concatenaNome(this);"
			property="sgFilial" label="sigla" maxLength="3" size="2"
			labelWidth="21%" width="79%" required="true" />

		<adsm:textbox dataType="text" property="pessoa.nmPessoa"
			onchange="return concatenaNome(this);" label="nome" maxLength="50"
			size="35" required="true" labelWidth="21%" width="29%" />
		<adsm:textbox dataType="text" property="pessoa.nmFantasia"
			label="nomeFantasia" maxLength="60" size="35" labelWidth="21%"
			width="29%" required="true" />

		<adsm:lookup label="franqueado" property="franqueado"
			idProperty="idEmpresa" criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			service="lms.municipios.empresaService.findLookupFilial"
			action="/municipios/manterEmpresas" labelWidth="21%" dataType="text"
			size="20" maxLength="20" width="19%">
			<adsm:propertyMapping modelProperty="pessoa.nmPessoa"
				relatedProperty="franqueado.pessoa.nmPessoa" />
			<adsm:propertyMapping modelProperty="tpEmpresa"
				criteriaProperty="empresa.tpEmpresaF" />
			<adsm:propertyMapping modelProperty="tpSituacao"
				criteriaProperty="tpSituacao" />
			<adsm:textbox dataType="text" property="franqueado.pessoa.nmPessoa"
				size="50" disabled="true" serializable="false" width="40%" />
		</adsm:lookup>

		<adsm:textbox dataType="text" property="lastRegional.sgRegional"
			size="2" label="regional" labelWidth="21%" disabled="true" width="4%"
			serializable="false" />
		<adsm:textbox dataType="text" property="lastRegional.dsRegional"
			size="20" disabled="true" width="74%" serializable="false" />

		<adsm:textbox label="nrDdr" maxLength="5" size="7" dataType="integer" 
			property="nrDdr" labelWidth="21%" width="29%" />		
			
		<adsm:textbox
			label="agenda"
			property="nrDddAgenda"
			dataType="integer"
			maxLength="3"
			size="3"
			labelWidth="21%"
			width="5%"
		/>	
		<adsm:textbox
			property="nrTelefoneAgenda"
			dataType="integer"
			maxLength="10"
			size="15"
			width="24%"
		/>

		<adsm:textbox property="dtImplantacaoLMS" label="dataImplantaçãoLMS"
			dataType="JTDate" labelWidth="21%" width="29%" disabled="true"/>
		<adsm:combobox property="dsTimezone" label="fusoHorario"
			labelWidth="21%" width="29%"
			service="lms.municipios.manterFiliaisAction.findTimeZones"
			optionLabelProperty="value" optionProperty="value" required="true" />

		<adsm:lookup label="gerente" property="gerente" idProperty="idUsuario"
			criteriaProperty="nrMatricula"
			service="lms.municipios.manterFiliaisAction.findLookupUsuarioFuncionario"
			action="/configuracoes/consultarFuncionariosView" dataType="text"
			size="16" maxLength="16" labelWidth="21%" width="80%">
			<adsm:propertyMapping relatedProperty="gerente.nome"
				modelProperty="nmUsuario" />
			<adsm:textbox dataType="text" property="gerente.nome" size="30"
				maxLength="45" disabled="true"
				onchange="return changeFuncionario('gerente');" />
			<adsm:hidden property="gerente.idContato" />
		</adsm:lookup>

		<adsm:range label="dataPrevista" labelWidth="21%" width="79%">
			<adsm:textbox dataType="JTDate"
				property="lastHistoricoFilial.dtPrevisaoOperacaoInicial" />
			<adsm:textbox dataType="JTDate"
				property="lastHistoricoFilial.dtPrevisaoOperacaoFinal" />
		</adsm:range>
		<adsm:range label="dataReal" labelWidth="21%" width="79%">
			<adsm:textbox dataType="JTDate"
				property="lastHistoricoFilial.dtRealOperacaoInicial" disabled="true" />
			<adsm:textbox dataType="JTDate"
				property="lastHistoricoFilial.dtRealOperacaoFinal" disabled="true" />
		</adsm:range>
		<adsm:textarea property="obAprovacao" disabled="true"
			label="observacoesImplantacao" maxLength="800" rows="2"
			labelWidth="21%" width="79%" columns="60" />

		<adsm:textbox dataType="text" property="dsHomepage" label="homepage"
			maxLength="120" size="25" labelWidth="21%" width="29%" />
		<adsm:textbox dataType="email" property="pessoa.dsEmail" label="email"
			maxLength="50" size="25" labelWidth="21%" width="29%" />

		<adsm:textarea property="obFilial" label="observacoesGerais"
			maxLength="120" rows="2" labelWidth="21%" width="79%" columns="60" />

		<adsm:textbox
				dataType="picture"
				property="imFilial"
				label="foto"
				blobColumnName="IM_FILIAL"
				tableName="FOTO_FILIAL"
				primaryKeyColumnName="ID_FOTO_FILIAL"
				primaryKeyValueProperty="idFilial"
				maxLength="12" size="30"
				labelWidth="21%" width="79%" />

		<adsm:checkbox property="blSorter" serializable="true" 
					label="possuiSorter" labelWidth="21%" width="14%"
					cellStyle="vertical-align:bottom" />

		<adsm:checkbox property="blRestrEntrOutrasFiliais" serializable="true" 
					label="restringeEntregaOutrasFiliais" labelWidth="25%" width="15%"
					cellStyle="vertical-align:bottom" />

		<adsm:hidden property="filtroGE" serializable="false" value="GE" />
		<adsm:hidden property="filtroCA" serializable="false" value="CA" />
		<adsm:hidden property="filtroCO" serializable="false" value="CO" />
		<adsm:hidden property="filtroCC" serializable="false" value="CC" />

		<adsm:section caption="contatos" />

		<adsm:lookup label="operacional" property="operacional"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.municipios.manterFiliaisAction.findLookupUsuarioFuncionario"
			action="/configuracoes/consultarFuncionariosView" dataType="text"
			size="16" maxLength="16" labelWidth="21%" width="80%">
			<adsm:propertyMapping relatedProperty="operacional.nome"
				modelProperty="nmUsuario" />
			<adsm:textbox dataType="text" property="operacional.nome" size="30"
				maxLength="45" disabled="true"
				onchange="return changeFuncionario('operacional');" />
			<adsm:hidden property="operacional.idContato" />
		</adsm:lookup>

		<adsm:lookup label="comercial" property="comercial"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.municipios.manterFiliaisAction.findLookupUsuarioFuncionario"
			action="/configuracoes/consultarFuncionariosView" dataType="text"
			size="16" maxLength="16" labelWidth="21%" width="80%">
			<adsm:propertyMapping relatedProperty="comercial.nome"
				modelProperty="nmUsuario" />
			<adsm:textbox dataType="text" property="comercial.nome" size="30"
				maxLength="45" disabled="true"
				onchange="return changeFuncionario('comercial');" />
			<adsm:hidden property="comercial.idContato" />
		</adsm:lookup>

		<adsm:lookup label="administrativo" property="administrativo"
			idProperty="idUsuario" criteriaProperty="nrMatricula"
			service="lms.municipios.manterFiliaisAction.findLookupUsuarioFuncionario"
			action="/configuracoes/consultarFuncionariosView" dataType="text"
			size="16" maxLength="16" labelWidth="21%" width="80%">
			<adsm:propertyMapping relatedProperty="administrativo.nome"
				modelProperty="nmUsuario" />
			<adsm:textbox dataType="text" property="administrativo.nome"
				size="30" maxLength="45" disabled="true"
				onchange="return changeFuncionario('administrativo');" />
			<adsm:hidden property="administrativo.idContato" />
		</adsm:lookup>

		</tr>
		<tr>
			<td colspan="100">
			<div id="mercurio" style="display: inline; border: none;">
			<table>
				<adsm:section caption="expedicao" />
				<adsm:lookup label="aeroportoAtendimento" property="aeroporto"
					idProperty="idAeroporto" criteriaProperty="sgAeroporto"
					service="lms.municipios.aeroportoService.findLookup"
					action="municipios/manterAeroportos" dataType="text"
					labelWidth="21%" width="50%" size="3" maxLength="3">
					<adsm:propertyMapping relatedProperty="aeroporto.pessoa.nmPessoa"
						modelProperty="pessoa.nmPessoa" />
					<adsm:propertyMapping criteriaProperty="tpSituacao"
						modelProperty="tpSituacao" />
					<adsm:textbox property="aeroporto.pessoa.nmPessoa" dataType="text"
						size="40" maxLength="30" disabled="true" />
				</adsm:lookup>
					
				<adsm:textbox label="horarioCorte" property="hrCorte"
					labelWidth="13%" width="15%" picker="true"
					dataType="JTTime"/>
					
		<adsm:lookup
			action="/contratacaoVeiculos/manterPostoConveniado"
			label="postoConveniado"
			idProperty="idPostoConveniado"
			service="lms.contratacaoveiculos.manterPostoConveniadoAction.findLookup"
			dataType="text" property="postoConveniado"
			criteriaProperty="pessoa.nrIdentificacao"
			relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			size="20"
			maxLength="20"
			labelWidth="21%" width="60%" 
			exactMatch="false" minLengthForAutoPopUpSearch="5" minLength="5" >
			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="postoConveniado.pessoa.nmPessoa"/>
			<adsm:textbox
				dataType="text"
				property="postoConveniado.pessoa.nmPessoa"
				size="40"
				maxLength="40" 
				disabled="true" />
		</adsm:lookup>
					
					
				<adsm:checkbox property="blColetorDadoScan"
					label="possuiColetorDadosScan" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />	
					
				<adsm:checkbox property="blConfereDoctoDescarga"
					label="confereDoctoDescarga" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />
					
				<adsm:checkbox property="blLimitaNotaFiscalForm" serializable="true" 
					label="limitaForm" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />
					
				<adsm:section caption="carregamento" />
				<adsm:checkbox property="blValidaLocalVeiculo" serializable="true"
					label="validaLocalVeiculo" labelWidth="21%" width="29%" 
					cellStyle="vertical-align:bottom" />
				<adsm:checkbox property="blRestringeCCVinculo" serializable="true"
					label="restringeCCVinculo" labelWidth="21%" width="29%" 
					cellStyle="vertical-align:bottom" />
				<adsm:checkbox property="blRncAutomaticaCarregamento" serializable="true"
					label="rncAutomatica" labelWidth="21%" width="29%" 
					cellStyle="vertical-align:bottom" />
					
				<adsm:section caption="descarga" />
				<adsm:checkbox property="blRncAutomaticaDescarga" serializable="true"
					label="rncAutomatica" labelWidth="21%" width="29%" 
					cellStyle="vertical-align:bottom" />
				<adsm:checkbox property="blRncAutomaticaDescargaMww" serializable="true"
					label="rncAutomaticaMww" labelWidth="21%" width="29%" 
					cellStyle="vertical-align:bottom" />
					
				<adsm:section caption="espacoFisicoFilial" />
				<adsm:textbox dataType="decimal" property="nrAreaTotal"
					label="areaTotal" mask="#,###,###,###,###,##0.00" minValue="0.01"
					size="24" labelWidth="21%" width="29%" />
				<adsm:textbox dataType="decimal" property="nrAreaArmazenagem"
					label="areaArmazenagem" mask="#,###,###,###,###,##0.00"
					minValue="0.01" size="24" labelWidth="21%" width="29%" />
				<%
					// Não achei a procedencia
				%>
				<adsm:textbox dataType="text" property="numeroDocas"
					label="numeroDocas" maxLength="3" size="18" disabled="true"
					labelWidth="21%" width="29%" />
				<adsm:textbox dataType="text" property="numeroBoxes"
					label="numeroBoxes" maxLength="3" size="18" disabled="true"
					labelWidth="21%" width="29%" />
				<%
					//fimmm
				%>
				<adsm:section caption="coletaEntrega" />
				<adsm:textbox dataType="text" property="meioTransporteProprioCount"
					label="veiculosProprios" maxLength="4" size="18" disabled="true"
					labelWidth="21%" width="29%" />
				<adsm:textbox dataType="text" property="meioTransporteAgregadoCount"
					label="veiculosAgregados" maxLength="4" size="18" disabled="true"
					labelWidth="21%" width="29%" />

				<adsm:checkbox property="blRecebeVeiculosSemColeta"
					label="recebeVeiculosSColetaOnTimeInf" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />
				<adsm:checkbox property="blInformaKmPortaria"
					label="obrigaInformacaoKm" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />

				<adsm:checkbox property="blOrdenaEntregaValor"
					label="ordenaEntregaValor" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />
				<adsm:checkbox property="blObrigaBaixaEntregaOrdem"
					label="obrigaBaixaEntregaOrdem" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />

				<adsm:textbox dataType="integer" property="nrFranquiaKm"
					label="franquiaKm" maxLength="6" size="18" labelWidth="21%"
					width="29%" required="true" />
				<adsm:textbox dataType="integer" property="nrFranquiaPeso"
					label="franquiaPesoPorcentagem" maxLength="6" size="18"
					labelWidth="21%" width="29%" required="true" />
				<adsm:checkbox property="blPagaDiariaExcedente"
					label="pgDiariaExcedente" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />
					
				<adsm:checkbox property="blWorkflowKm"
					label="gerarWorkflowKm" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />	
					
				<adsm:textbox dataType="integer" property="nrHrColeta"
					label="nrHrColeta" maxLength="4" size="18" labelWidth="21%"
					width="29%" />	
					

				<adsm:section caption="veiculosOnLine" />
				<adsm:combobox property="tpOrdemDoc" domain="DM_TP_ORDEM_DOC"
					label="ordemExibicaoDocumentos" labelWidth="21%" width="33%" renderOptions="true"/>				

			    <adsm:section caption="transferencia" />			
				<adsm:checkbox property="blGeraContratacaoRetornoVazio"
					label="contratacaoRetornoVazio" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />	

				<adsm:section caption="dadosFinanceiros" />

				<adsm:hidden property="filialReponsavel.empresa.tpEmpresa"
					serializable="false" value="M" />

				<adsm:lookup label="filialResponsavel"
					property="filialByIdFilialResponsavel" idProperty="idFilial"
					criteriaProperty="sgFilial"
					service="lms.municipios.filialService.findLookupFilial"
					action="/municipios/manterFiliais" labelWidth="21%" dataType="text"
					size="2" maxLength="3" width="79%">
					<adsm:propertyMapping criteriaProperty="flagResp"
						modelProperty="flag" />
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
						relatedProperty="filialByIdFilialResponsavel.pessoa.nmFantasia" />
					<adsm:propertyMapping
						criteriaProperty="lastHistoricoFilial.tpFilial"
						modelProperty="flagType" />
					<adsm:propertyMapping
						criteriaProperty="filialReponsavel.empresa.tpEmpresa"
						modelProperty="empresa.tpEmpresa" />
					<adsm:textbox dataType="text"
						property="filialByIdFilialResponsavel.pessoa.nmFantasia" size="50"
						disabled="true" serializable="false" />
				</adsm:lookup>

				<adsm:lookup label="filialCiaAerea"
					property="filialByIdFilialResponsavalAwb" idProperty="idFilial"
					criteriaProperty="sgFilial"
					service="lms.municipios.filialService.findLookupFilial"
					action="/municipios/manterFiliais" labelWidth="21%" dataType="text"
					size="2" maxLength="3" width="79%"
					cellStyle="vertical-align:bottom">
					<adsm:propertyMapping criteriaProperty="flagCia"
						modelProperty="flag" />
					<adsm:propertyMapping
						criteriaProperty="lastHistoricoFilial.tpFilial"
						modelProperty="flagType" />
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia"
						relatedProperty="filialByIdFilialResponsavalAwb.pessoa.nmFantasia" />
					<adsm:propertyMapping
						criteriaProperty="filialReponsavel.empresa.tpEmpresa"
						modelProperty="empresa.tpEmpresa" />
					<adsm:textbox dataType="text"
						property="filialByIdFilialResponsavalAwb.pessoa.nmFantasia"
						size="50" disabled="true" required="false" serializable="false"
						cellStyle="vertical-align:bottom" />
				</adsm:lookup>

				<adsm:combobox property="cedenteByIdCedenteBloqueto.idCedente"
					optionProperty="idCedente" optionLabelProperty="comboText"
					service="lms.contasreceber.cedenteService.findComboByActiveValues"
					label="cedenteBoleto" width="29%" labelWidth="21%" boxWidth="150" />
				<adsm:combobox property="cedenteByIdCedente.idCedente"
					optionProperty="idCedente" optionLabelProperty="comboText"
					service="lms.contasreceber.cedenteService.findComboByActiveValues"
					label="cedenteAereoFOB" width="29%" labelWidth="21%" boxWidth="150" />

				<adsm:textbox label="centroCusto" property="nrCentroCusto"
					dataType="integer" labelWidth="21%" width="29%" maxLength="4"
					size="18" />
				<adsm:textbox label="prazoCobranca2" property="nrPrazoCobranca"
					dataType="integer" labelWidth="21%" width="29%" maxLength="2"
					size="18" required="true" />

				<adsm:combobox label="moeda" property="moeda.idMoeda"
					optionProperty="idMoeda" optionLabelProperty="siglaSimbolo"
					service="lms.configuracoes.moedaService.find" labelWidth="21%"
					width="79%" required="true" />

				<adsm:textbox dataType="decimal" property="pcJuroDiario"
					label="percentualJurosDiarios" mask="##0.00" minValue="0.01"
					size="18" labelWidth="21%" width="29%"
					cellStyle="vertical-align:bottom" />
				<adsm:textbox dataType="decimal" property="pcFreteCarreteiro"
					label="percentualAdiamentoFreteCarreteiro" mask="##0.00"
					minValue="0.01" size="18" labelWidth="21%" width="29%"
					required="true" cellStyle="vertical-align:bottom" />

				<adsm:checkbox property="blEmiteBoletoFaturamento"
					label="emiteBoletoFaturamento" labelWidth="21%" width="29%" />
				<adsm:checkbox property="blEmiteBoletoEntrega"
					label="emiteBoletoEntrega" labelWidth="21%" width="29%" />

				<adsm:checkbox property="blEmiteReciboFrete"
					label="emiteReciboFrete" labelWidth="21%" width="29%" />
				<adsm:checkbox property="blLiberaFobAereo"
					label="emiteFreteFOBAereoParceira" labelWidth="21%" width="29%" />

				<adsm:hidden property="pendencia.idPendencia" />

				<adsm:hidden property="blEnableReabrirFilial" serializable="false" />

			</table>
			</div>
			</td>
			<adsm:section caption="historicoFilial" />
			<adsm:grid property="historicoFilial" idProperty="idHistoricoFilial"
				selectionMode="none" gridHeight="220" unique="false" rows="9"
				showPagging="false" showGotoBox="true" onRowClick="rowClick();">
				<adsm:gridColumn title="tipoFilial" property="dsTpFilial"
					width="20%" />
				<adsm:gridColumn title="dataPrevistaAbertura"
					property="dtPrevisaoOperacaoInicial" dataType="JTDate" width="20%" />
				<adsm:gridColumn title="dataPrevistaFechamento"
					property="dtPrevisaoOperacaoFinal" dataType="JTDate" width="20%" />
				<adsm:gridColumn title="dataRealAbertura"
					property="dtRealOperacaoInicial" dataType="JTDate" width="20%" />
				<adsm:gridColumn title="dataRealFechamento"
					property="dtRealOperacaoFinal" dataType="JTDate" width="20%" />
			</adsm:grid>
			<adsm:buttonBar lines="3">
				<adsm:button id="horariosButton" caption="horariosFuncionamento"
					action="municipios/manterHorariosAtendimentoFilial" boxWidth="170"
					cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="servicosButton" caption="servicos"
					action="municipios/manterServicosFilial" cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="gruposButton" caption="gruposClassificacao"
					action="municipios/manterGruposClassificacaoFilial" boxWidth="145"
					cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="ciaButton" caption="ciasAereasUtilizadasFilial"
					action="municipios/manterCiasAereasFiliais" boxWidth="238"
					cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="postosButton" caption="postosAvancados"
					action="municipios/manterPostosAvancadosFiliais" boxWidth="125"
					cmd="main" breakBefore="true">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="regionalButton" caption="regional"
					action="municipios/manterRegionalFilial" cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="regioesButton" caption="regioesFilial"
					action="municipios/manterRegioesColetaEntregaFiliais"
					boxWidth="100" cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="municipiosButton" caption="municipiosAtendidos"
					action="municipios/manterMunicipiosAtendidos" boxWidth="130"
					cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
					<adsm:linkProperty src="empresa.idEmpresa"
						target="filial.empresa.idEmpresa" disabled="true" />
					<adsm:linkProperty src="empresa.pessoa.nrIdentificacao"
						target="filial.empresa.pessoa.nrIdentificacao" disabled="true" />
					<adsm:linkProperty src="empresa.pessoa.nmPessoa"
						target="filial.empresa.pessoa.nmPessoa" disabled="true" />
				</adsm:button>
				<adsm:button id="enderecosButton"
					onclick="parent.parent.redirectPage('configuracoes/manterEnderecoPessoa.do?cmd=main' + buildLinkPropertiesQueryString_default());"
					caption="enderecos" boxWidth="70" />
				<adsm:button id="inscricaoEstadual" caption="inscricaoEstadual"
					onclick="parent.parent.redirectPage('configuracoes/manterInscricoesEstaduais.do?cmd=main' + buildLinkPropertiesQueryString_default());"
					boxWidth="115" />
				<adsm:button id="bancariosButton"
					onclick="parent.parent.redirectPage('configuracoes/manterDadosBancariosPessoa.do?cmd=main' + buildLinkPropertiesQueryString_dadosBancarios());"
					caption="dadosBancarios" boxWidth="100" />
				<adsm:button id="contatosButton" caption="contatos"
					onclick="parent.parent.redirectPage('configuracoes/manterContatos.do?cmd=main' + buildLinkPropertiesQueryString_default());"
					boxWidth="70" />
				<adsm:button id="docasButton" caption="terminais" breakBefore="true"
					action="portaria/manterTerminais" boxWidth="75" cmd="main">
					<adsm:linkProperty src="idFilial" target="filial.idFilial"
						disabled="true" />
					<adsm:linkProperty src="sgFilial" target="filial.sgFilial"
						disabled="true" />
					<adsm:linkProperty src="pessoa.nmFantasia"
						target="filial.pessoa.nmFantasia" disabled="true" />
				</adsm:button>
				<adsm:button id="reabrirFilial" caption="reabrirFilial"
					onclick="reabrirFilialClick();" />
				<adsm:button id="salvarButton" caption="salvar"
					onclick="storeCustomizado()" disabled="false" />
				<adsm:newButton id="novoButton" />
				<adsm:button id="__buttonBar:0.removeButton" caption="excluir"
					onclick="removeCustomizado()" />
				<adsm:hidden property="labelPessoa" />
			</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script language="javascript" type="text/javascript">
	function reabrirFilialClick() {
		showModalDialog('municipios/manterFiliais.do?id=' + getElementValue("idFilial") + '&cmd=reabrir',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:350px;dialogHeight:170px;');			
	}

	function buildLinkPropertiesQueryString_default() {
		//deve ser desta forma pois getElementValue("pessoa.nrIdentificacao") nao retorna formatado
		var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
		var qs = "&pessoa.idPessoa=" + getElementValue("idFilial");
			qs += "&pessoa.nrIdentificacao=" + nrIdentificacao;
			qs += "&pessoa.nmFantasia=" + getElementValue("pessoa.nmFantasia");
			qs += "&pessoa.nmPessoa=" + getElementValue("pessoa.nmFantasia");
			qs += "&labelPessoaTemp=" + getI18nMessage("filial");
		return qs;
	}

	function buildLinkPropertiesQueryString_dadosBancarios() {
		var qs = null;
		//deve ser desta forma pois getElementValue("pessoa.nrIdentificacao") nao retorna formatado
		var nrIdentificacao = getElementValue("pessoa.nrIdentificacao");
		if (getElementValue("franqueado.idEmpresa") == "") {
			qs = "&pessoa.idPessoa=" + getElementValue("idFilial");
				qs += "&pessoa.nrIdentificacao=" + nrIdentificacao;
				qs += "&pessoa.nmFantasia=" + getElementValue("pessoa.nmFantasia");
				qs += "&pessoa.nmPessoa=" + getElementValue("pessoa.nmFantasia");
		} else {
			qs = "&pessoa.idPessoa=" + getElementValue("franqueado.idEmpresa");
				qs += "&pessoa.nrIdentificacao=" + getElementValue("franqueado.pessoa.nrIdentificacao");
				qs += "&pessoa.nmFantasia=" + getElementValue("franqueado.pessoa.nmPessoa");
				qs += "&pessoa.nmPessoa=" + getElementValue("franqueado.pessoa.nmPessoa");
		}
		qs += "&labelPessoaTemp=" + getI18nMessage("filial");
		return qs;
	}

	getElement("pessoa.nrIdentificacao").serializable = true;
	var tpFilial = null;
	var tpFilialSalva = null;
	var vlDates = new Array(4);
	function initWindow(evento) {
		if ("tab_click" == evento.name || "newButton_click" == evento.name) {
			verificaTipoEmpresa();
			stateNew();
		}else if ("gridRow_click" == evento.name || "storeButton_click" == evento.name) {
			disableContatos();
			setDisabled("pessoa.idPessoa", true);
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("sgFilial", true);
			setDisabled("reabrirFilial", true);
		}
	}

	/** arqui esta a função de identificação**/
	function lookupIdentificacao_cb(data,exception) {
		if (exception) {
			alert(exception);
			resetValue("pessoa.nrIdentificacao");
			setFocus("pessoa.nrIdentificacao");
			return;
		}
		var flag = pessoa_nrIdentificacao_exactMatch_cb(data);
		concatenaNome();
		return flag;
	}

	function concatenaNome(field) {
		setElementValue("sgFilial", getElementValue("sgFilial").toUpperCase());
		setElementValue("nomeConcatenado", getElementValue("sgFilial") + " - " + getElementValue("pessoa.nmFantasia"));
		if (field != undefined)
			return validate(field);
	}

	function storeCustomizado() {
		var tabGroup = getTabGroup(this.document);
		tab	= tabGroup.getTab("cad");

		var valid = false;
		if (tab != null)
			valid = tab.validate({name:"storeButton_click"});
		if (valid == false)
			return false;

		if (getElementValue("lastHistoricoFilial.tpFilial") != "LO" && getElementValue("pessoa.nrIdentificacao") == "") {
			alertRequiredField("pessoa.nrIdentificacao");
			return false;
		}

		if (getElementValue("idProcessoWorkflow") == "") {
			if (getElementValue("empresa.tpEmpresa") == "M" && getElementValue("lastHistoricoFilial.tpFilial") != "MA" &&
				(tpFilial != getElementValue("lastHistoricoFilial.tpFilial") || dtPrevisao != getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial") ||
				dtPrevisaoFinal != getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal"))) {
				if (confirmI18nMessage("LMS-29104"))
					storeButtonScript('lms.municipios.manterFiliaisAction.store', 'storeDataLoad', document.Lazy);
			} else
				storeButtonScript('lms.municipios.manterFiliaisAction.store', 'storeDataLoad', document.Lazy);
		} else
			storeButtonScript('lms.municipios.manterFiliaisAction.store', 'storeDataLoad', document.Lazy);
	}

	function verificaTipoFilial() {
		var tpFilialNew = getElementValue("lastHistoricoFilial.tpFilial");
		var tpEmpresa = getElementValue("empresa.tpEmpresa");
		var isTpFilialChanged = false;

		if (tpFilialNew) {
			if( (tpFilial != null) && (tpFilial != tpFilialNew) ) {
				if(confirmI18nMessage("LMS-29146")) {
					isTpFilialChanged = true;
				} else {
					setElementValue("lastHistoricoFilial.tpFilial", tpFilial);
					return;
				}
			}
			comboboxChange({e:tpFilialNew});
		}
		setDisabled("franqueado.pessoa.nmPessoa", true);
		if (tpFilialNew == "FR") {
			setRowVisibility("franqueado.idEmpresa", true);
			getElement("franqueado.pessoa.nrIdentificacao").required = "true";
		} else {
			setRowVisibility("franqueado.idEmpresa", false);
			setElementValue("franqueado.idEmpresa", "");
			setElementValue("franqueado.pessoa.nrIdentificacao", "");
			setElementValue("franqueado.pessoa.nmPessoa", "");
			getElement("franqueado.pessoa.nrIdentificacao").required = "false";
		}

		if(tpEmpresa == "M") {
			if(tpFilialNew == "LO" || tpFilialNew == "OP") {
				getElement("filialByIdFilialResponsavel.sgFilial").required = "true";
			} else {
				getElement("filialByIdFilialResponsavel.sgFilial").required = "false";
			}
		}

		if (tpFilial) {
			if (tpFilial != tpFilialNew) {
					// Se tpFilial anteriror igual a Loja deve habilitar os campos
				setDisabled("pessoa.tpIdentificacao", (tpFilialSalva != "LO"));
				setDisabled("pessoa.nrIdentificacao", (tpFilialSalva != "LO"));

				if (vlDates[0] == undefined && vlDates[1] == undefined && vlDates[2] == undefined && vlDates[3] == undefined) {
					vlDates[0] = getElementValue("lastHistoricoFilial.dtRealOperacaoInicial");
					vlDates[1] = getElementValue("lastHistoricoFilial.dtRealOperacaoFinal");
					vlDates[2] = getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal");
					vlDates[3] = getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial");
				}
				resetValue("lastHistoricoFilial.dtRealOperacaoInicial");
				resetValue("lastHistoricoFilial.dtRealOperacaoFinal");
				resetValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal");
				resetValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial");
			} else {
				if (vlDates[0] != undefined && vlDates[1] != undefined && vlDates[2] != undefined && vlDates[3] != undefined) {
					setElementValue("lastHistoricoFilial.dtRealOperacaoInicial", vlDates[0]);
					setElementValue("lastHistoricoFilial.dtRealOperacaoFinal", vlDates[1]);
					setElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal", vlDates[2]);
					setElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial", vlDates[3]);
				}
			}
		}
		if (tpEmpresa != "P") {
			if (getElementValue("idFilial") != "") {
				setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", (getElementValue("lastHistoricoFilial.dtRealOperacaoInicial") == "" || (getElementValue("lastHistoricoFilial.dtRealOperacaoFinal") != "" && getElementValue("idProcessoWorkFlow") == "")));
				setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial") != "");
				setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", true);
				setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", false);
			}
		}

		setDisabled("filialByIdFilialResponsavel.idFilial", false);
		setDisabled("filialByIdFilialResponsavalAwb.idFilial", false);

		if (tpFilialNew == "MA" || tpFilialNew == "") {
			setDisabled("filialByIdFilialResponsavel.idFilial", true);
			setDisabled("filialByIdFilialResponsavalAwb.idFilial", true);
			resetValue("filialByIdFilialResponsavel.idFilial");
			resetValue("filialByIdFilialResponsavalAwb.idFilial");
		}
		if(isTpFilialChanged) {
			tpFilial = tpFilialNew;
		}
		disableAndResetContatos();
	}

	function stateNew() {
		setDisabled("empresa.idEmpresa", false);
		setDisabled("filialByIdFilialResponsavel.idFilial", true);
		setDisabled("filialByIdFilialResponsavalAwb.idFilial", true);
		setDisabled("salvarButton", false);
		setDisabled("pessoa.tpIdentificacao", false);
		setDisabled("pessoa.nrIdentificacao", true);
		setDisabled("lastHistoricoFilial.tpFilial", false);
		setDisabled("sgFilial", false);
		setDisabled("pessoa.nmPessoa", false);
		setDisabled("pessoa.nmFantasia", false);
		setDisabled("franqueado.idEmpresa", false);
		setDisabled("dtImplantacaoLMS", true);
		setDisabled("dsTimezone", false);
		setDisabled("dsHomepage", false);
		setDisabled("pessoa.dsEmail", false);
		setDisabled("obFilial", false);
		setDisabled("imFilial", false);
		setDisabled("aeroporto.idAeroporto", false);
		setDisabled("postoConveniado.idPostoConveniado", false);
		setDisabled("nrAreaTotal", false);
		setDisabled("nrAreaArmazenagem", false);
		setDisabled("blRecebeVeiculosSemColeta", false);
		setDisabled("blInformaKmPortaria", false);
		setDisabled("blOrdenaEntregaValor", false);
		setDisabled("blObrigaBaixaEntregaOrdem", false);
		setDisabled("nrFranquiaKm", false);
		setDisabled("nrFranquiaPeso", false);
		setDisabled("nrHrColeta", false);
		setDisabled("blPagaDiariaExcedente", false);		
		setDisabled("cedenteByIdCedenteBloqueto.idCedente", false);
		setDisabled("cedenteByIdCedente.idCedente", false);
		setDisabled("nrCentroCusto", false);
		setDisabled("nrPrazoCobranca", false);
		setDisabled("moeda.idMoeda", false);
		setDisabled("pcJuroDiario", false);
		setDisabled("pcFreteCarreteiro", false);
		setDisabled("blEmiteBoletoFaturamento", false);
		setDisabled("blEmiteBoletoEntrega", false);
		setDisabled("blEmiteReciboFrete", false);
		setDisabled("blColetorDadoScan", false);
		setDisabled("blLiberaFobAereo", false);
		setDisabled("reabrirFilial", true);
		setDisabled("blSorter", false);
		
		setElementValue("blRestrEntrOutrasFiliais", true);
		
		//Reseta as variaveis.
		tpFilial = undefined;
		dtPrevisaoFinal = undefined;
		dtPrevisao = undefined;

		disableContatos();
		historicoFilialGridDef.resetGrid();
		setFocusOnFirstFocusableField();
	}

	var dtPrevisao = null;
	var dtPrevisaoFinal = null;
	function populaGrid_cb(data, error) {
		vlDates[0] = undefined;
		vlDates[1] = undefined;
		vlDates[2] = undefined;
		vlDates[3] = undefined;

		onDataLoadCallbackPessoaWidget({tpIdentificacaoElement:getElement("pessoa.tpIdentificacao"), 
									numberElement:getElement("pessoa.nrIdentificacao")});

		setElementValue("empresa.tpEmpresa",getNestedBeanPropertyValue(data,"empresa.tpEmpresa"));

		tpFilial = getNestedBeanPropertyValue(data,"lastHistoricoFilial.tpFilial");
		setElementValue("lastHistoricoFilial.tpFilial", tpFilial);
		tpFilialSalva = tpFilial;

		setElementValue("lastHistoricoFilial.dtRealOperacaoInicial",
			setFormat(getElement("lastHistoricoFilial.dtRealOperacaoInicial"),
			getNestedBeanPropertyValue(data,"lastHistoricoFilial.dtRealOperacaoInicial")));

		setElementValue("lastHistoricoFilial.dtRealOperacaoFinal",
			setFormat(getElement("lastHistoricoFilial.dtRealOperacaoFinal"),
			getNestedBeanPropertyValue(data,"lastHistoricoFilial.dtRealOperacaoFinal")));

		setElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal",
			setFormat(getElement("lastHistoricoFilial.dtPrevisaoOperacaoFinal"),
			getNestedBeanPropertyValue(data,"lastHistoricoFilial.dtPrevisaoOperacaoFinal")));

		setElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial",
			setFormat(getElement("lastHistoricoFilial.dtPrevisaoOperacaoInicial"),
			getNestedBeanPropertyValue(data,"lastHistoricoFilial.dtPrevisaoOperacaoInicial")));

		verificaTipoEmpresa(undefined);

		onDataLoad_cb(data, error);
		setElementValue("pessoa.nmPessoa", getNestedBeanPropertyValue(data, "pessoa.nmPessoa"));

		var blEnableReabrirFilial = getNestedBeanPropertyValue(data, "blEnableReabrirFilial");
		setElementValue("blEnableReabrirFilial", blEnableReabrirFilial);

		
		if (blEnableReabrirFilial == "true") {
			setDisabled(document, true);
			setDisabled("reabrirFilial", false);
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("novoButton", false);
			setFocus("novoButton", false);
		} else {
			setDisabled("reabrirFilial", true);
			setDisabled("pessoa.idPessoa", true);
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("sgFilial", true);

			var disable = getElementValue("empresa.tpEmpresa") == "P";
			setDisabled("servicosButton", disable);
			setDisabled("gruposButton", disable);
			setDisabled("ciaButton", disable);
			setDisabled("postosButton", disable);
			setDisabled("regioesButton", disable);
			setDisabled("regionalButton", disable);
			setDisabled("docasButton", disable);
			dtPrevisao = getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial");
			dtPrevisaoFinal = getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal");

			if(getElementValue("empresa.tpEmpresa") == "M") {
				setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", (getElementValue("blDisablePrevisaoOperacaoInicial") == "true"));
				setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", (getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal") != "" || getElementValue("lastHistoricoFilial.dtRealOperacaoInicial") == ""));
			}

			if (getElementValue("idProcessoWorkflow") != "") {
				setDisabled(document, true);
				setDisabled("salvarButton", false);
				if (getElementValue("lastHistoricoFilial.dtRealOperacaoInicial") != "") {
					setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", false);
					setFocus("lastHistoricoFilial.dtPrevisaoOperacaoFinal");
				} else {
					setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", false);
					setFocus("lastHistoricoFilial.dtPrevisaoOperacaoInicial");
				}
			}
			setDisabled("__buttonBar:0.removeButton", false);

			setFocusOnFirstFocusableField();
		}

		if (getElementValue("idFilial") != "") {
			//historicoFilialGridDef.executeSearch({filial:{idFilial:getElementValue("idFilial")}});
			historicoFilialGridDef.prepareSearch();
			historicoFilialGridDef.onDataLoad_cb(data.historicosFilial);
		}

		setFocus("pessoa.nmPessoa");
		setDisabled("empresa.idEmpresa", true);
	}

	function customizado_cb(data) {
		empresa_pessoa_nrIdentificacao_exactMatch_cb(data);
		verificaTipoEmpresa(data);
	}

	function verificaTipoEmpresa(data) {
		if (data != undefined)
			setElementValue("empresa.tpEmpresa", getNestedBeanPropertyValue(data, ((data.length > 0) ? ":0." : "") + "tpEmpresa.value"));

		var sub = "";

		if(getElementValue("empresa.tpEmpresa") != "P" && getElementValue("empresa.tpEmpresa") != "M"){
			getElement("mercurio").style.display = 'none';
		}
		else{
			getElement("mercurio").style.display = 'inline';
		}
		
		if (getElementValue("empresa.tpEmpresa") == "P") {

			getElement("lastHistoricoFilial.dtPrevisaoOperacaoInicial").required = "false";
			setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", true);
			resetValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial");

			setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", true);
			resetValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal");

			getElement("lastHistoricoFilial.dtRealOperacaoInicial").required = "true";
			setDisabled("lastHistoricoFilial.dtRealOperacaoInicial", false);
			resetValue("lastHistoricoFilial.dtRealOperacaoInicial");

			setDisabled("lastHistoricoFilial.dtRealOperacaoFinal", false);
			resetValue("lastHistoricoFilial.dtRealOperacaoFinal");

			getElement("nrPrazoCobranca").required = "false";
			getElement("pcFreteCarreteiro").required = "false";
			getElement("moeda.idMoeda").required = "false";
			setDisabled("servicosButton", true);
			setDisabled("gruposButton", true);
			setDisabled("ciaButton", true);
			setDisabled("postosButton", true);
			setDisabled("regioesButton", true);
			setDisabled("docasButton", true);

			if (data != undefined) {
				disableAndResetContatos();
			}
			resetValue("nrAreaTotal");
			resetValue("nrAreaArmazenagem");
			resetValue("blSorter");
			resetValue("blEmiteBoletoEntrega");
			resetValue("blEmiteReciboFrete");
			resetValue("blLiberaFobAereo");
			resetValue("blRecebeVeiculosSemColeta");
			resetValue("blInformaKmPortaria");
			resetValue("blOrdenaEntregaValor");
			resetValue("blObrigaBaixaEntregaOrdem");
			resetValue("nrFranquiaKm");
			resetValue("nrFranquiaPeso");
			resetValue("nrHrColeta");
			resetValue("blPagaDiariaExcedente");
			resetValue("filialByIdFilialResponsavel.idFilial");
			resetValue("filialByIdFilialResponsavalAwb.idFilial");
			resetValue("cedenteByIdCedenteBloqueto.idCedente");
			resetValue("cedenteByIdCedente.idCedente");
			resetValue("nrCentroCusto");
			resetValue("nrPrazoCobranca");
			resetValue("pcFreteCarreteiro");
			resetValue("moeda.idMoeda");
			resetValue("pcJuroDiario");
			resetValue("blEmiteBoletoFaturamento");
		} else {

			getElement("lastHistoricoFilial.dtPrevisaoOperacaoInicial").required = "true";
			setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", false);
			setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", getElement("lastHistoricoFilial.dtRealOperacaoInicial") == "");

			getElement("lastHistoricoFilial.dtRealOperacaoInicial").required = "false";

			getElement("nrPrazoCobranca").required = "true";
			getElement("pcFreteCarreteiro").required = "true";
			getElement("lastHistoricoFilial.dtPrevisaoOperacaoInicial").required = "true";
			getElement("moeda.idMoeda").required = "true";

			if (data != undefined) {
				disableAndResetContatos();
			}
		}

		verificaTipoFilial();

		return true;
	}

	function disableContatos(option) {
		var blDesabilita;
		if (option == undefined || option != true) {
			blDesabilita = getElementValue("empresa.tpEmpresa") != "M";
		} else {
			blDesabilita = true;
		}

		setDisabled("administrativo.idUsuario", blDesabilita);
		setDisabled("operacional.idUsuario", blDesabilita);
		setDisabled("gerente.idUsuario", blDesabilita);
		setDisabled("comercial.idUsuario", blDesabilita);

		if (option == undefined || option != true) {
			blDesabilita = getElementValue("empresa.tpEmpresa") != "P";
		} else {
			blDesabilita = true;
		}
		setDisabled("administrativo.nome", blDesabilita);
		setDisabled("operacional.nome", blDesabilita);
		setDisabled("gerente.nome", blDesabilita);
		setDisabled("comercial.nome", blDesabilita);
	}

	function disableAndResetContatos(option) {
		disableContatos(option);
		resetValue("administrativo.nrMatricula");
		resetValue("operacional.nrMatricula");
		resetValue("gerente.nrMatricula");
		resetValue("comercial.nrMatricula");
		resetValue("administrativo.nome");
		resetValue("operacional.nome");
		resetValue("gerente.nome");
		resetValue("comercial.nome");
	}

	function storeDataLoad_cb(data,error,key) {
		if (error) {
			alert(error);
			if (key == "LMS-29001")
				setFocus("lastHistoricoFilial.tpFilial");
			return false;
		}
		if (data != undefined) {
			setDisabled("__buttonBar:0.removeButton", false);
			setElementValue("idFilial", getNestedBeanPropertyValue(data, "idFilial"));	

			tpFilial = getElementValue("lastHistoricoFilial.tpFilial");
			tpFilialSalva = getElementValue("lastHistoricoFilial.tpFilial");
			
			dtPrevisao = getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial");
			dtPrevisaoFinal = getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal");

			//historicoFilialGridDef.executeSearch({filial:{idFilial:getElementValue("idFilial")}});

			setElementValue("obAprovacao", getNestedBeanPropertyValue(data, "obAprovacao"));
			setElementValue("pessoa.idPessoa", getNestedBeanPropertyValue(data, "pessoa.idPessoa"));

			if (getNestedBeanPropertyValue(data,"pendencia.idPendencia") != undefined)
				setElementValue("pendencia.idPendencia", getNestedBeanPropertyValue(data, "pendencia.idPendencia"));
		}
		store_cb(data, error);
		setFocus("novoButton", false);
		if (data != undefined) {
			setDisabled("pessoa.idPessoa", true);
			setDisabled("pessoa.nrIdentificacao", true);
			setDisabled("pessoa.tpIdentificacao", true);
			setDisabled("empresa.idEmpresa", true);
			setDisabled("sgFilial", true);
			if (getElementValue("idProcessoWorkflow") != "") {
				setDisabled(document, true);
				setDisabled("salvarButton", false);
				if (getElementValue("empresa.tpEmpresa") == "M") {
					if (getElementValue("lastHistoricoFilial.dtRealOperacaoInicial") != "") {
						setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", false);
						setFocus("lastHistoricoFilial.dtPrevisaoOperacaoFinal");
					}
				}
			} else {
				if (getElementValue("empresa.tpEmpresa") == "M") {
					setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoFinal", (getElementValue("lastHistoricoFilial.dtRealOperacaoInicial") == "" || getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoFinal") != ""));
					setDisabled("lastHistoricoFilial.dtPrevisaoOperacaoInicial", getElementValue("lastHistoricoFilial.dtPrevisaoOperacaoInicial") != "");
				}
				var disable = getElementValue("empresa.tpEmpresa") == "P";
				setDisabled("servicosButton", disable);
				setDisabled("gruposButton", disable);
				setDisabled("ciaButton", disable);
				setDisabled("postosButton", disable);
				setDisabled("regioesButton", disable);
				setDisabled("regionalButton", disable);
				setDisabled("docasButton", disable);
			}
		}
		setDisabled("reabrirFilial", getElementValue("blEnableReabrirFilial") != "true");
		setFocus("novoButton", false);
	}

	function rowClick() {
		return false;
	}

	function changeEmpresa() {
		if (getElementValue("empresa.pessoa.nrIdentificacao") == "") {
			disableAndResetContatos(true);
			setDisabled("administrativo.nome", true);
			setDisabled("operacional.nome", true);
			setDisabled("gerente.nome", true);
			setDisabled("comercial.nome", true);
			resetValue("administrativo.nome");
			resetValue("operacional.nome");
			resetValue("gerente.nome");
			resetValue("comercial.nome");
		}
		return empresa_pessoa_nrIdentificacaoOnChangeHandler();
	}

	function removeCustomizado() {
		removeButtonScript('lms.municipios.filialService.removeFilialById', 'removeCustomizado', 'idFilial', this.document);
	}

	function removeCustomizado_cb(data,exception) {
		removeById_cb(data,exception);
		if (exception == undefined)
			stateNew();
	}
//-->
</script>