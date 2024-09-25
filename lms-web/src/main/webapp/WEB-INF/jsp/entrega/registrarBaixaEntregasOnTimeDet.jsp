<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<script type="text/javascript">
<!--
	var isValidRemetente = true;
	var isValidDestinatario = true;
	//FUNÇÔES RELACIONADAS AO DOCTO SERVICO
	function changeTpDoctoServico(field) {
		resetValue('doctoServico.idDoctoServico');
		var flag = changeDocumentWidgetType(
					{documentTypeElement:field, 
					 filialElement:document.getElementById('doctoServico.filialByIdFilialOrigem.idFilial'), 
					 documentNumberElement:document.getElementById('doctoServico.idDoctoServico'), 
					 parentQualifier:'doctoServico',
					 documentGroup:'DOCTOSERVICE',
					 actionService:'lms.entrega.registrarBaixaEntregasOnTimeAction'}
				  );
		document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").service = "lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupFilial";
		var pms = document.getElementById("doctoServico.idDoctoServico").propertyMappings;
		pms[pms.length] = {modelProperty:"idDoctoServico", relatedProperty:"idDoctoServico"};
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("doctoServico.idDoctoServico",true);
		return flag;
	}
	function changeFilialDoctoServico() {
		resetValue('doctoServico.idDoctoServico');
		var flag = changeDocumentWidgetFilial(
					{filialElement:document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial"), 
					 documentNumberElement:document.getElementById("doctoServico.idDoctoServico")}
			   );
		setDisabled("doctoServico.filialByIdFilialOrigem.idFilial",true);
		setDisabled("doctoServico.idDoctoServico",true);
		return flag;
	}
	
	function manifestoEntrega_filialChange(elem) {
		setDisabled("manifestoEntrega.idManifestoEntrega",elem.value == "");
		return manifestoEntrega_filial_sgFilialOnChangeHandler();
	}
	
	function dataLoadOcorrenciaEntrega_cb(data) {
		ocorrenciaEntrega_cdOcorrenciaEntrega_exactMatch_cb(data);
		setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
		document.getElementById("ocorrenciaEntrega.idOcorrenciaEntrega").callBack = "ocorrenciaEntrega.cdOcorrenciaEntrega_exactMatch";
	}
	    
	function confirmBaixa() {
		if (!validateTabScript(document.forms['Lazy']))
			return;

		var data = buildFormBeanFromForm(document.forms['Lazy']);
		setNestedBeanPropertyValue(data,"isValidR",isValidRemetente);
		setNestedBeanPropertyValue(data,"isValidD",isValidDestinatario);		
		
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasOnTimeAction.executeConfirmation","dataLoadVoid",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function dataLoadVoid_cb(data,error,key) {
		
		if(data != undefined && getNestedBeanPropertyValue(data,"LMS09143") == "true"){
			alert("LMS-09143 -" + i18NLabel.getLabel("LMS-09143"));
		}
		
		if (key != undefined && key.substr(0,3) == "PCE") {
			var explode = key.split("_");
			if (explode[1] == "R")
				isValidRemetente = false;
			else
				isValidDestinatario = false;
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[2] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');
		}else if (key == "LMS-01097") {
			var explode = error.split("-");
			showModalDialog('vendas/alertaPce.do?idVersaoDescritivoPce=' + explode[3] + '&cmd=pop',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:310px;');			
		}else if (data != undefined && getNestedBeanPropertyValue(data,"warning") != "" && getNestedBeanPropertyValue(data,"warning") != undefined) {
			alert(getNestedBeanPropertyValue(data,"warning"));
			dialogArguments.window.ManifestoEntregaDocumentoGridDef.executeLastSearch();
			self.close();
		}else{
			if (error)
				alert(error);
			else{
				dialogArguments.window.ManifestoEntregaDocumentoGridDef.executeLastSearch();
				self.close();
			} 
		}
	}
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasOnTimeAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
		var url = new URL(document.location.href);
		for(var key in url.parameters) {
			if (key != "cmd") {
				setElementValue(key,setFormat(document.getElementById(key),url.parameters[key]));
				if (key == "doctoServico.tpDocumentoServico")
					changeTpDoctoServico(document.getElementById("doctoServico.tpDocumentoServico"));
				if (key == "doctoServico.filialByIdFilialOrigem.idFilial")
					changeFilialDoctoServico();
			}
		}
		setElementValue("idDoctoServico",url.parameters['doctoServico.idDoctoServico']);
		//Feito propositalmente!!!!
		document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").value = "001";
		setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
		document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").serializable = "true";
		lookupChange({e:document.forms[0].elements["ocorrenciaEntrega.idOcorrenciaEntrega"],forceChange:true});
		
		validateTpEntregaParcial();
		
	}
	//PREENCHENDO USUARIO E DHATUAL
	var nmUsuario = null;
	var dhAtual = null;
	
	function dataSession_cb(data) {
		nmUsuario = getNestedBeanPropertyValue(data,"usuario.nmUsuario");
		dhAtual = getNestedBeanPropertyValue(data,"dhAtual");
		writeDataSession();
	}
	
	function writeDataSession() {
		setElementValue("usuario.nmUsuario",nmUsuario);
		setElementValue("dhOcorrencia",setFormat(document.getElementById("dhOcorrencia"),dhAtual));
	}
	
	function alertPCE_cb() {
		if (isValidRemetente == false || isValidDestinatario == false)
			confirmBaixa();
	}
	
	function validateTpEntregaParcial() {
		if (getElementValue("ocorrenciaEntrega.cdOcorrenciaEntrega") == 1) {
			setDisabled("tpEntregaParcial", false);
		} else {
			setDisabled("tpEntregaParcial", true);
			resetValue("tpEntregaParcial");
		}
	}
	
	function setPopUpOcorrencia(data) {
		if (getNestedBeanPropertyValue(data,"cdOcorrenciaEntrega") == 1) { // Entrega realizada
			setDisabled("tpEntregaParcial",false);
		} else {
			setDisabled("tpEntregaParcial",true);
			resetValue("tpEntregaParcial");
		}
		
		return true;
	}
	
	function changeOcorrencia() {
		var flag = ocorrenciaEntrega_cdOcorrenciaEntregaOnChangeHandler();

		validateTpEntregaParcial();
				
		return flag;
	}
	
//-->
</script>
<adsm:window service="lms.entrega.registrarBaixaEntregasOnTimeAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/entrega/registrarBaixaEntregasOnTime">
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>

   		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" labelWidth="19%"
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupFilial" action="/municipios/manterFiliais" disabled="true" width="81%"
					 popupLabel="pesquisarFilial" label="controleCargas" size="3" maxLength="3" picker="false" serializable="false" criteriaSerializable="true">
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" disabled="true"  
						 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 popupLabel="pesquisarControleCarga" maxLength="8" size="8" mask="00000000" criteriaSerializable="true">
			</adsm:lookup>
		</adsm:lookup>

   		<adsm:lookup dataType="text" 
   				property="manifestoEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
   				service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupFilial"
   				action="/entrega/consultarManifestosEntrega" cmd="list"
   				onchange="return manifestoEntrega_filialChange(this);"
   				label="manifestoEntrega" size="3" maxLength="3" disabled="true"
   				labelWidth="19%" width="81%" exactMatch="true" picker="false" >
   			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.pessoa.nmFantasia"
   						modelProperty="pessoa.nmFantasia" />
   			
   			<adsm:lookup dataType="integer" action="/entrega/consultarManifestosEntrega" cmd="lookup" exactMatch="true" maxLength="8"
	   				property="manifestoEntrega" idProperty="idManifestoEntrega" criteriaProperty="nrManifestoEntrega" size="7" disabled="true"
	   				service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupManifestoEntrega" mask="00000000">
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.idFilial" modelProperty="filial.idFilial" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.sgFilial"
   						modelProperty="filial.sgFilial" inlineQuery="false" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.pessoa.nmFantasia"
   						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
   			</adsm:lookup>
   			
   			<adsm:hidden property="manifestoEntrega.filial.pessoa.nmFantasia" serializable="false" />
   		</adsm:lookup>

		<adsm:combobox property="doctoServico.tpDocumentoServico" label="documentoServico" labelWidth="19%" optionLabelProperty="description"
					   service="lms.entrega.registrarBaixaEntregasOnTimeAction.findTpDoctoServico" optionProperty="value" width="81%"
					   onchange="return changeTpDoctoServico(this);" disabled="true">
			<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" action="" size="3" maxLength="3" picker="false" onchange="return changeFilialDoctoServico();" disabled="true">
			</adsm:lookup>
			
			<adsm:lookup dataType="integer" property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" action="" size="12" maxLength="8" mask="00000000" serializable="true" disabled="true"/>
			<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			<adsm:hidden property="idDoctoServico"/>
		</adsm:combobox>


		<adsm:lookup service="lms.entrega.registrarBaixaEntregasOnTimeAction.findLookupOcorrenciaEntrega" dataType="integer" property="ocorrenciaEntrega" 
	 		criteriaProperty="cdOcorrenciaEntrega" idProperty="idOcorrenciaEntrega" label="ocorrencia" labelWidth="19%" width="81%" required="true"
	 		exactMatch="true" minLengthForAutoPopUpSearch="3" size="3" maxLength="3" action="/entrega/manterOcorrenciasEntrega" onDataLoadCallBack="dataLoadOcorrenciaEntrega"
	 		onPopupSetValue="setPopUpOcorrencia" onchange="return changeOcorrencia();">
			<adsm:propertyMapping modelProperty="dsOcorrenciaEntrega" relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>						
			<adsm:textbox dataType="text" size="70" maxLength="60" property="ocorrenciaEntrega.dsOcorrenciaEntrega" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpEntregaParcial" 
			label="entrega" labelWidth="19%" width="81%"
			boxWidth="200" 
			domain="DM_TIPO_ENTREGA_PARCIAL"
			renderOptions="true"
			/>
		
		<adsm:textbox dataType="text" property="recebedor" size="35" width="31%" maxLength="60" label="recebedor" labelWidth="19%"/>		
		<adsm:textbox dataType="text" property="rg" label="rg" size="35" width="31%" maxLength="60" labelWidth="19%" width="81%"/>

		<adsm:textarea property="obManifesto" label="observacao" maxLength="400" rows="2" columns="97"  labelWidth="19%" width="81%"/>
		<adsm:textbox dataType="JTDateTimeZone" property="dhOcorrencia" width="31%" label="dataHoraOcorrencia" labelWidth="19%" disabled="true" required="true" serializable="false"/>
		<adsm:textbox dataType="text" property="usuario.nmUsuario" size="35" width="31%" label="usuario" disabled="true" labelWidth="19%" required="true" serializable="false"/>
		
		<adsm:combobox property="complementoBaixaSPP"
			label="complementoBaixa" labelWidth="19%" width="31%" 
			service="lms.expedicao.dadosComplementoService.findBaixaSPP" 
			optionProperty="value" optionLabelProperty="description"/>			
		
		<adsm:buttonBar>
			<adsm:button caption="confirmar" disabled="false" onclick="confirmBaixa()"/>
			<adsm:button caption="fechar" onclick="self.close();" disabled="false"/>
		</adsm:buttonBar>
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-09143"/>
		</adsm:i18nLabels>
		
	</adsm:form>
	
</adsm:window>
