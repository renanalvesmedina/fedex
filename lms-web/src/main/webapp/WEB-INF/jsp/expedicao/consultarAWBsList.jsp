<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window
	onPageLoadCallBack="onPageLoadCallBack"
	service="lms.expedicao.consultarAWBsAction">

	<adsm:form
		action="/expedicao/consultarAWBs">

		<adsm:hidden property="statusAwbCancelado"/>
 		<adsm:hidden property="ciaFilialMercurio.idCiaFilialMercurio" serializable="false"/>
 		<adsm:hidden property="tpStatusAwb" serializable="false"/>

		<%---------------------%>
		<%-- CIA AEREA COMBO --%>
		<%---------------------%>
		<adsm:combobox 
			property="ciaFilialMercurio.empresa.idEmpresa" 
			optionLabelProperty="pessoa.nmPessoa" 
 			optionProperty="idEmpresa" 
 			service="lms.expedicao.consultarAWBsAction.findCiaAerea" 
 			label="ciaAerea"
 			required="true"
 			boxWidth="190"
 			width="32%"
 			labelWidth="19%"
 			serializable="true" />

		<%----------------%>
		<%-- NRAWB TEXT --%>
		<%----------------%>
	
		<adsm:combobox 
			property="tpAwb" 
			label="preAwbAwb" 
			labelWidth="19%" 
			domain="DM_LOOKUP_AWB"
			width="30%" defaultValue="E"
			onchange="resetDataEmissao();">

			<adsm:textbox 
				dataType="integer" 
				property="nrAwb" 
				size="12" 
				maxLength="11"  />
		</adsm:combobox>
		<%-----------------------------%>
		<%-- AEROPORTO ORIGEM LOOKUP --%>
		<%-----------------------------%>
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.consultarAWBsAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoOrigem"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeOrigem"
			size="3"
			maxLength="3"
			labelWidth="19%"
			width="32%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa" 
				serializable="false"
				size="26"
				maxLength="50"
				disabled="true"/>

		</adsm:lookup>

		<%------------------------------%>
		<%-- AEROPORTO DESTINO LOOKUP --%>
		<%------------------------------%>
		<adsm:lookup
			action="/municipios/manterAeroportos"
			service="lms.expedicao.consultarAWBsAction.findAeroporto"
			dataType="text"
			property="aeroportoByIdAeroportoDestino"
			idProperty="idAeroporto"
			criteriaProperty="sgAeroporto"
			label="aeroportoDeDestino"
			size="3"
			maxLength="3"
			labelWidth="19%"
			width="30%">

			<adsm:propertyMapping
				modelProperty="pessoa.nmPessoa"
				relatedProperty="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"/>

			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa" 
				serializable="false"
				size="26"
				maxLength="50"
				disabled="true"/>

		</adsm:lookup>

		<%-----------------------------%>
		<%-- PERIODO DE EMISSAO TEXT --%>
		<%-----------------------------%>
		<adsm:range label="periodoEmissao" labelWidth="19%" width="32%">
			<adsm:textbox 
				dataType="JTDate" 
				property="dataInicial"/>

			<adsm:textbox 
				dataType="JTDate" 
				property="dataFinal"/>
		</adsm:range>
			
		<%-------------------------%>
		<%-- TIPO SITUACAO COMBO --%>
		<%-------------------------%>
		<adsm:combobox 
			property="tpSituacaoAwb" 
			label="situacao" 
			labelWidth="19%" 
			domain="DM_STATUS_AWB"
			width="30%" />		
			
		<%-------------------------%>
		<%-- CONFERIDO COMBO --%>
		<%-------------------------%>
		<adsm:combobox 
			property="blConferido" 
			label="conferido" 
			labelWidth="19%" 
			domain="DM_SIM_NAO"
			width="32%" />

		<%-------------------------%>
		<%-- CODIGO LIBERACAO AWB COMPLEMENTAR --%>
		<%-------------------------%>
		<adsm:textbox labelWidth="19%" width="30%" maxLength="8"
			dataType="integer" 
			property="cdLiberacaoAWBCompl" 
			label="codigoLiberacao"/>
			
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="awbList" />
			<adsm:resetButton />
		</adsm:buttonBar>

	</adsm:form>
	<adsm:grid 
		idProperty="idAwb"
		property="awbList"
		gridHeight="189"
		unique="true"
		rows="9"
		onRowClick="awbRowClick"
		selectionMode="none"
		scrollBars="horizontal">

		<adsm:gridColumn 
			dataType="text"
			title="preAwbAwb" 
			property="nrAwbFormatado" 
			width="120" />

		<adsm:gridColumn 
			isDomain="true"
			title="situacao" 
			property="tpStatusAwb" 
			width="75" />
			
		<adsm:gridColumn 
			isDomain="true"
			title="finalidade" 
			property="tpAwb" 
			width="80" />

		<adsm:gridColumn 
			title="aeroportoOrigem" 
			property="aeroportoByIdAeroportoOrigem.sgAeroporto" 
			width="106" />

		<adsm:gridColumn 
			title="aeroportoDestino" 
			property="aeroportoByIdAeroportoDestino.sgAeroporto" 
			width="106" />

		<adsm:gridColumn 
			dataType="JTDateTimeZone"
			title="dataHoraEmissao" 
			property="dhEmissao" 
			width="110" 
			align="center"/>

		<adsm:gridColumnGroup customSeparator=" ">

			<adsm:gridColumn 
				property="moeda.sgMoeda" 
				dataType="text" 
				title="valorFrete" 
				width="30"/>

			<adsm:gridColumn 
				property="moeda.dsSimbolo" 
				dataType="text" 
				title="" 
				width="30"/>

		</adsm:gridColumnGroup>

		<adsm:gridColumn 
			dataType="currency"
			title="" 
			property="vlFrete" 
			width="55"/>
 
		<adsm:buttonBar>
			<adsm:button
				caption="fechar"
				id="btnFechar"
				onclick="self.close();"
				buttonType="closeButton"
				style="display:none"
			/>
		</adsm:buttonBar> 

	</adsm:grid>
</adsm:window>
<script language="javascript" type="text/javascript">
<!--
function onPageLoadCallBack_cb(data, error) {
	onPageLoad_cb(data, error);
	var isLookup = window.dialogArguments && window.dialogArguments.window;
	if(isLookup) {
		getElement("btnFechar").style.display = "inline";
		setDisabled('btnFechar',false);
	} else {
		getElement("btnFechar").style.display = "none";
	}
	getDadosDefault();
}

function initWindow(eventObj) {
	changeCadTabStatus(true);
	if(eventObj.name == "cleanButton_click") {
		getDadosDefault();
	}
}

/***********************/
/* ONROWCLICK AWB GRID */
/***********************/
function awbRowClick() {
	changeCadTabStatus(false);
}

/**********************/
/* FUNCOES AUXILIARES */
/**********************/

function changeCadTabStatus(status) {
	var tabGroup = getTabGroup(this.document);
	if(tabGroup != undefined) {
		tabGroup.setDisabledTab("cad", status);
		tabGroup.setDisabledTab("hist", status);
		tabGroup.setDisabledTab("rnc", status);
	}
}

function getDadosDefault() {
	var service = "lms.expedicao.consultarAWBsAction.getDadosDefault";
	var sdo = createServiceDataObject(service, "getDadosDefault");
	xmit({serviceDataObjects:[sdo]});
}

function getDadosDefault_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	setElementValue("dataInicial", setFormat("dataInicial", data.dataInicial));
	setElementValue("dataFinal", setFormat("dataFinal", data.dataFinal));
	
	tpStatusAwb = getElementValue("tpStatusAwb");
	
	if(tpStatusAwb !== null && tpStatusAwb !== '') {
		setElementValue("tpAwb", tpStatusAwb);	
		setDisabled("tpAwb", true);
		
		if(getElementValue("statusAwbCancelado")){
			setElementValue("tpSituacaoAwb", "C");
		}else{
			setElementValue("tpSituacaoAwb", tpStatusAwb);
		}
		setDisabled("tpSituacaoAwb", true);
	}
}

function resetDataEmissao(){
	if(getElementValue("tpAwb") == "P"){
		setElementValue("dataInicial", "");
		setElementValue("dataFinal", "");
	}else{
		var service = "lms.expedicao.consultarAWBsAction.getDadosDefault";
		var sdo = createServiceDataObject(service, "setDataEmissao");
		xmit({serviceDataObjects:[sdo]});
	}
}

function setDataEmissao_cb(data, error) {
	if(error != undefined) {
		alert(error);
		return;
	}
	setElementValue("dataInicial", setFormat("dataInicial", data.dataInicial));
	setElementValue("dataFinal", setFormat("dataFinal", data.dataFinal));
}
-->
</script>