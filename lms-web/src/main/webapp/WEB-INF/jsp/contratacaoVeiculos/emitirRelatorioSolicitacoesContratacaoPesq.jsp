<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contratacaoVeiculos.emitirRelatorioSolicitacoesContratacaoAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/contratacaoVeiculos/emitirRelatorioSolicitacoesContratacao">
		<adsm:i18nLabels>
			<adsm:include key="LMS-26097"/>
		</adsm:i18nLabels>

		<adsm:lookup
			action="/municipios/manterFiliais" 
			service="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction.findLookupFilial"
			label="filial"
			width="43%"
			labelWidth="17%"
			property="filial"
			idProperty="idFilial"
			criteriaProperty="sgFilial"
			maxLength="3" dataType="text" size="3"
			exactMatch="true">
			<adsm:textbox property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="true" dataType="text"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
		</adsm:lookup>

		<adsm:lookup property="usuario" label="usuarioSolicitante"
			idProperty="idUsuario" 	criteriaProperty="nrMatricula"
			action="/configuracoes/consultarFuncionariosView"
			service="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction.findLookupUsuario"
			labelWidth="17%" width="43%"
			size="16" maxLength="16" dataType="text"
			exactMatch="true">
			<adsm:propertyMapping relatedProperty="usuario.nmUsuario" modelProperty="nmUsuario"/>
			<adsm:textbox property="usuario.nmUsuario" size="30" disabled="true" dataType="text"/>
		</adsm:lookup>

		<adsm:combobox property="tpSolicitacaoContratacao" domain="DM_TIPO_SOLICITACAO_CONTRATACAO"
			label="tipoSolicitacao" labelWidth="17%" width="43%" disabled="true" defaultValue="V"/>

		<adsm:combobox property="tpSituacaoContratacao" domain="DM_SITUACAO_SOLICITACAO_CONTRATACAO"
			label="situacao" labelWidth="17%" width="43%"/>

		<adsm:combobox property="tipoMeioTransporte.idTipoMeioTransporte"
			optionProperty="idTipoMeioTransporte"
			optionLabelProperty="dsTipoMeioTransporte"
			autoLoad="true"
			service="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction.findComboTpMeioTransporte"
			label="tipoMeioTransporte"
			labelWidth="17%" width="43%"
		/>

		<adsm:combobox property="tpVinculoContratacao" domain="DM_TIPO_VINCULO_VEICULO"
			label="tipoVinculo" labelWidth="17%" width="47%"/>

		<adsm:combobox property="tpRotaSolicitacao" domain="DM_TIPO_ROTA_SOLICITACAO"
			label="tipoRota" labelWidth="17%" width="47%" defaultValue="EV"
			onchange="tpRotaChange()"
			/>

		<adsm:listbox property="rotaEventual.filiaisRota" 
				optionProperty="sgFilial" optionLabelProperty="dsRota"
				label="rotaEventual" size="4" labelWidth="17%" width="33%" 
				showOrderControls="false" boxWidth="180" showIndex="false" serializable="true" allowMultiple="true">
			<adsm:lookup property="filialRota" idProperty="idFilial" criteriaProperty="sgFilial" serializable="false" 
					dataType="text" size="3" maxLength="3" 
					service="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction.findLookupFilial" 
					action="/municipios/manterFiliais"
					exactMatch="false" minLengthForAutoPopUpSearch="3">
				<adsm:propertyMapping relatedProperty="rotaEventual.filiaisRota_nmFilial" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="nmFilial" disabled="true" serializable="false"/>
			</adsm:lookup>   
		</adsm:listbox>

		

		<adsm:lookup
			label="rotaExpressa"
			property="rotaIdaVolta"
			idProperty="idRotaIdaVolta"
			criteriaProperty="nrRota"
			service="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction.findLookupRotaExpressa"
			action="/municipios/consultarRotas"
			dataType="integer"
			cellStyle="vertical-align:top;"
			labelStyle="vertical-align:top;"
			size="4"
			maxLength="4"
			exactMatch="false"
			labelWidth="17%"
			width="33%" mask="0000"
			cmd="idaVolta"
			disabled="true"
		>
			<adsm:propertyMapping relatedProperty="rotaIdaVolta.dsRota" modelProperty="rota.dsRota"/>
			<adsm:textbox dataType="text" property="rotaIdaVolta.dsRota" size="30" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:range label="dataSolicitacao" labelWidth="17%" width="57%" maxInterval="31">
			<adsm:textbox dataType="JTDate" property="dtSolicitacaoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtSolicitacaoFinal" required="true"/>
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	function pageLoad_cb(data,error){
		onPageLoad_cb(data,error);
		var data = new Object();
		var sdo = createServiceDataObject("lms.contratacaoveiculos.emitirRelatorioSolicitacoesContratacaoAction.findBasicData","findBasicData",data);
		xmit({serviceDataObjects:[sdo]});	
	}
	
	function findBasicData_cb(data,error){
		if(error!=undefined){
			alert(error);
			return;
		}

		setElementValue("dtSolicitacaoInicial",data.dtSolicitacaoInicial);
		setElementValue("dtSolicitacaoFinal",data.dtSolicitacaoFinal);
	}

	function tpRotaChange(){
		var tpRota = getElementValue("tpRotaSolicitacao");

		if (tpRota.length > 0 && !confirmI18nMessage("LMS-26097")){
			return;
		}

		setupTpRotaEventual(tpRota == "EV");
		setupTpRotaExpressa(tpRota == "EX");

		/**
		if (tpRota == "EV"){
			setupTpRotaEventual(true);
			setupTpRotaExpressa(false);
		}else if (tpRota == "EX"){
			setupTpRotaEventual(false);
			setupTpRotaExpressa(true);
		}else{
			setupTpRotaEventual(false);
			setupTpRotaExpressa(false);
		}*/
	}
	
	function setupTpRotaEventual(isEventual){
		setDisabled("rotaEventual.filiaisRota_filialRota.idFilial",!isEventual);
		setDisabled("rotaEventual.filiaisRota",!isEventual);
		if (!isEventual){
			document.getElementById("rotaEventual.filiaisRota").innerHTML = "";
			setElementValue("rotaEventual.filiaisRota_filialRota.idFilial","");
			setElementValue("rotaEventual.filiaisRota_filialRota.sgFilial","");
			setElementValue("rotaEventual.filiaisRota_nmFilial","");
		}
	}

	function setupTpRotaExpressa(isExpressa){
		setDisabled("rotaIdaVolta.idRotaIdaVolta",!isExpressa);
		if (!isExpressa){
			setElementValue("rotaIdaVolta.idRotaIdaVolta","");
			setElementValue("rotaIdaVolta.nrRota","");
			setElementValue("rotaIdaVolta.dsRota","");
		}
	}	
	
</script>