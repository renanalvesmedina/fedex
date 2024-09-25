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
					 actionService:'lms.entrega.registrarBaixaEntregasAction'}
				  );
		document.getElementById("doctoServico.filialByIdFilialOrigem.idFilial").service = "lms.entrega.registrarBaixaEntregasAction.findLookupFilial";
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
	
	function manifestoViagem_filialChange(elem) {
		setDisabled("manifestoViagem.idManifestoViagem",elem.value == "");
		return manifestoViagem_filial_sgFilialOnChangeHandler();
	}
	    
	function confirmBaixa() {
		if (!validateTabScript(document.forms['Lazy']))
			return;

		var data = buildFormBeanFromForm(document.forms['Lazy']);
		setNestedBeanPropertyValue(data,"isValidR",isValidRemetente);
		setNestedBeanPropertyValue(data,"isValidD",isValidDestinatario);		
		
		var DocumentoFisico = true;
		if((document.getElementById("ordem").value*1)<0)
			DocumentoFisico = false;

		setNestedBeanPropertyValue(data,"isDocFisico",DocumentoFisico);

		if(document.getElementById("idsManifestoEntregaDocumento") != null
				&& document.getElementById("idsManifestoEntregaDocumento").value != "" ){
			setNestedBeanPropertyValue(data,"idsManifestoEntregaDocumento", document.getElementById("idsManifestoEntregaDocumento").value);
			//alert("BaixaVols");return;
			var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasAction.storeOcorrenciaOnManifestoEntregaVolume","dataLoadVoid",data);
			xmit({serviceDataObjects:[sdo]});
		}else{
			//alert("BaixaDoc");return;
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasAction.executeConfirmation","dataLoadVoid",data);
		xmit({serviceDataObjects:[sdo]});
	}
	}
	
	function dataLoadVoid_cb(data,error,key) {
		
		if(data != undefined && getNestedBeanPropertyValue(data,"LMS09143") == "true" ){
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
		}else{
			if (error)
				alert(error);
			else{
				
				if(data != undefined && getNestedBeanPropertyValue(data,"abrirRegistrarPorNotaFiscal") == "true" ){
					var idDoctoServico = getNestedBeanPropertyValue(data,"idDoctoServico")
					var idManifesto    = getNestedBeanPropertyValue(data,"idManifesto")
					var tpManifesto    = getNestedBeanPropertyValue(data,"tpManifesto")
					
					var url = 'view/swt/index#/app/entrega/registrarBaixaEntregaPorNotaFiscal/detalhe/'+ idDoctoServico + '&' + idManifesto + '&' + tpManifesto + '&F';
					var wProperties = 'unardorned:no;scroll:yes;resizable:yes;status:no;center=yes;help:no;dialogWidth:1500px;dialogHeight:780px;';
					showModalDialog(url, window, wProperties);
				}
				
				//getTabGroup(dialogArguments.window).getTab("pesq").ManifestoEntregaDocumentoGridDef.executeLastSearch(true);
				//dialogArguments.window.ManifestoEntregaDocumentoGridDef.executeLastSearch(true);
				self.close();
			} 
		}
	}
	var isBaixaDocumento;
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		var sdo = createServiceDataObject("lms.entrega.registrarBaixaEntregasAction.findDataSession","dataSession",null);
		xmit({serviceDataObjects:[sdo]});
		var url = new URL(document.location.href);
		for(var key in url.parameters) {
			if (key != "cmd") {
				setElementValue(key,setFormat(document.getElementById(key),url.parameters[key]));
				if (key == "doctoServico.tpDocumentoServico") {
					changeTpDoctoServico(document.getElementById("doctoServico.tpDocumentoServico"));
				}
				else if (key == "doctoServico.filialByIdFilialOrigem.idFilial") {
					changeFilialDoctoServico();
				}
				else if (key == "tpManifesto") {
					var exibeManEntrega = url.parameters["tpManifesto"] == "E";
					setRowVisibility("manifestoEntrega.idManifestoEntrega", exibeManEntrega);
					setRowVisibility("manifestoViagem.idManifestoViagem", !exibeManEntrega);
				}
				}
			}
		setElementValue("idDoctoServico",url.parameters['doctoServico.idDoctoServico']);
		setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
		document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").serializable = "true";
		
		isBaixaDocumento = (url.parameters['idsManifestoEntregaDocumento'] == null 
								|| url.parameters['idsManifestoEntregaDocumento'] == "");

    	if(!isBaixaDocumento){
			setVisibility('recebedor', false);
			setVisibility('obManifesto', false);
			setVisibility('complementoBaixaSPP', false);
		}else{
			setVisibility('nrSequencias', false);
		}
		
		if((document.getElementById("ordem").value * 1) < 0){
			setDisabled("recebedor", true);
			setDisabled("obManifesto", true);
			setDisabled("complementoBaixaSPP", true);
		}

		//Feito propositalmente!!!!
		document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").value = "001";
		setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
		lookupChange({e:document.forms[0].elements["ocorrenciaEntrega.idOcorrenciaEntrega"],forceChange:true});
		document.getElementById("ocorrenciaEntrega.cdOcorrenciaEntrega").serializable = "true";
		
		
		validateTpEntregaParcial();
	}
	
	function dataLoadOcorrenciaEntrega_cb(data) {
		ocorrenciaEntrega_cdOcorrenciaEntrega_exactMatch_cb(data);
		setFocus("ocorrenciaEntrega.cdOcorrenciaEntrega");
		document.getElementById("ocorrenciaEntrega.idOcorrenciaEntrega").callBack = "ocorrenciaEntrega.cdOcorrenciaEntrega_exactMatch";
	}
	
	//PREENCHENDO USUARIO E DHATUAL
	var nmUsuario = null;
	
	function dataSession_cb(data) {
		nmUsuario = getNestedBeanPropertyValue(data,"usuario.nmUsuario");
		writeDataSession();
	}
	
	function writeDataSession() {
		setElementValue("usuario.nmUsuario",nmUsuario);
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
<adsm:window service="lms.entrega.registrarBaixaEntregasAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/entrega/registrarBaixaEntregas">
	
		<adsm:hidden property="tpManifesto" />
		<adsm:hidden property="ordem" serializable="false" />
		<adsm:hidden property="idsManifestoEntregaDocumento" serializable="true" />
	
		<adsm:hidden property="tpSituacao" serializable="false" value="A"/>
		
		<!-- MANIFESTO ENTREGA -->
   		<adsm:lookup dataType="text" 
   				property="manifestoEntrega.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
   				service="lms.entrega.registrarBaixaEntregasAction.findLookupFilial"
   				action="/entrega/consultarManifestosEntrega" cmd="list"
   				onchange="return manifestoEntrega_filialChange(this);"
   				label="manifestoEntrega" size="3" maxLength="3" disabled="true"
   				labelWidth="19%" width="81%" exactMatch="true" picker="false" >
   			<adsm:propertyMapping relatedProperty="manifestoEntrega.filial.pessoa.nmFantasia"
   						modelProperty="pessoa.nmFantasia" />
   			
   			<adsm:lookup dataType="integer" action="/entrega/consultarManifestosEntrega" cmd="lookup" exactMatch="true" maxLength="8"
	   				property="manifestoEntrega" idProperty="idManifestoEntrega" criteriaProperty="nrManifestoEntrega" size="7" disabled="true"
	   				service="lms.entrega.registrarBaixaEntregasAction.findLookupManifestoEntrega" mask="00000000">
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.idFilial" modelProperty="filial.idFilial" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.sgFilial"
   						modelProperty="filial.sgFilial" inlineQuery="false" />
   				<adsm:propertyMapping criteriaProperty="manifestoEntrega.filial.pessoa.nmFantasia"
   						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
   			</adsm:lookup>
   			
   			<adsm:hidden property="manifestoEntrega.filial.pessoa.nmFantasia" serializable="false" />
   		</adsm:lookup>
   		<!-- /MANIFESTO ENTREGA -->
   		
   		<!-- MANIFESTO VIAGEM -->
   		<adsm:lookup dataType="text" 
   				property="manifestoViagem.filial" idProperty="idFilial" criteriaProperty="sgFilial" 
   				service="lms.entrega.registrarBaixaEntregasAction.findLookupFilial"
   				action="/entrega/consultarManifestosEntrega" cmd="list"
   				onchange="return manifestoViagem_filialChange(this);"
   				label="manifestoViagem" size="3" maxLength="3" disabled="true"
   				labelWidth="19%" width="81%" exactMatch="true" picker="false" >
   			<adsm:propertyMapping relatedProperty="manifestoViagem.filial.pessoa.nmFantasia"
   						modelProperty="pessoa.nmFantasia" />
   			
   			<adsm:lookup dataType="integer" action="" cmd="lookup" exactMatch="true" maxLength="8"
	   				property="manifestoViagem" idProperty="idManifestoViagem" criteriaProperty="nrManifestoViagem" size="7" disabled="true"
	   				service="" mask="00000000">
   				<adsm:propertyMapping criteriaProperty="manifestoViagem.filial.idFilial" modelProperty="filial.idFilial" />
   				<adsm:propertyMapping criteriaProperty="manifestoViagem.filial.sgFilial"
   						modelProperty="filial.sgFilial" inlineQuery="false" />
   				<adsm:propertyMapping criteriaProperty="manifestoViagem.filial.pessoa.nmFantasia"
   						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
   			</adsm:lookup>
   			
   			<adsm:hidden property="manifestoViagem.filial.pessoa.nmFantasia" serializable="false" />
   		</adsm:lookup>
   		<!-- /MANIFESTO VIAGEM -->
   		
   		<!-- CONTROLE CARGA -->
   		<adsm:lookup dataType="text" property="controleCarga.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" labelWidth="19%"
					 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupFilial" action="/municipios/manterFiliais" disabled="true" width="81%"
					 popupLabel="pesquisarFilial" label="controleCargas" size="3" maxLength="3" picker="false" serializable="false" criteriaSerializable="true">
			<adsm:lookup dataType="integer" property="controleCarga" idProperty="idControleCarga" criteriaProperty="nrControleCarga" disabled="true"  
						 service="lms.seguros.emitirRelatorioProcessoSinistroAction.findLookupControleCarga" action="/carregamento/manterControleCargas" cmd="list"
						 popupLabel="pesquisarControleCarga" maxLength="8" size="8" mask="00000000" criteriaSerializable="true">
			</adsm:lookup>
		</adsm:lookup>		
		<!-- /CONTROLE CARGA -->

		<!-- DOCTO SERVICO -->
		<adsm:combobox property="doctoServico.tpDocumentoServico" label="documentoServico" labelWidth="19%" optionLabelProperty="description"
					   service="lms.entrega.registrarBaixaEntregasAction.findTpDoctoServico" optionProperty="value" width="31%"
					   onchange="return changeTpDoctoServico(this);" disabled="true">
			<adsm:lookup dataType="text" property="doctoServico.filialByIdFilialOrigem" idProperty="idFilial" criteriaProperty="sgFilial" 
						 service="" action="" size="3" maxLength="3" picker="false" onchange="return changeFilialDoctoServico();" disabled="true">
			</adsm:lookup>
			<adsm:hidden property="doctoServico.filialByIdFilialOrigem.pessoa.nmFantasia" serializable="false"/>
			<adsm:lookup dataType="integer" property="doctoServico" idProperty="idDoctoServico" criteriaProperty="nrDoctoServico" 
						 service="" action="" size="12" maxLength="8" mask="00000000" serializable="true" disabled="true"/>
			<adsm:hidden property="idDoctoServico"/>
		</adsm:combobox>
		<!-- /DOCTO SERVICO -->

		<adsm:textbox dataType="text" property="nrSequencias" size="35" width="35%" label="volumes" disabled="true" labelWidth="15%" required="false" serializable="false"/>

		
		<!-- OCORRENCIA ENTREGA -->
		<adsm:lookup service="lms.entrega.registrarBaixaEntregasAction.findLookupOcorrenciaEntrega" dataType="integer" property="ocorrenciaEntrega" 
	 		criteriaProperty="cdOcorrenciaEntrega" idProperty="idOcorrenciaEntrega" label="ocorrencia" labelWidth="19%" width="81%" required="true"
	 		exactMatch="true" minLengthForAutoPopUpSearch="3" size="3" maxLength="3" action="/entrega/manterOcorrenciasEntrega" onDataLoadCallBack="dataLoadOcorrenciaEntrega"
	 		onPopupSetValue="setPopUpOcorrencia" onchange="return changeOcorrencia();">
			<adsm:propertyMapping modelProperty="dsOcorrenciaEntrega" relatedProperty="ocorrenciaEntrega.dsOcorrenciaEntrega"/>
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="tpSituacao"/>						
			<adsm:textbox dataType="text" size="70" maxLength="60" property="ocorrenciaEntrega.dsOcorrenciaEntrega" disabled="true" serializable="false"/>
		</adsm:lookup>
		<!-- /OCORRENCIA ENTREGA -->
		
		<!-- Entrega -->
		<adsm:combobox property="tpEntregaParcial" 
			label="entrega" labelWidth="19%" width="81%"
			boxWidth="200" 
			domain="DM_TIPO_ENTREGA_PARCIAL"
			renderOptions="true"
			/>
		<!--  -->
		
		<!-- DH OCORRENCIA -->
		<adsm:textbox dataType="JTDateTimeZone" property="dhOcorrencia" width="31%" label="dataHoraOcorrencia" labelWidth="19%" required="true"/>
		
		<!-- NM USUARIO -->
		<adsm:textbox dataType="text" property="usuario.nmUsuario" size="35" width="31%" label="usuario" disabled="true" labelWidth="19%" required="true" serializable="false"/>
		
		<!-- RECEBEDOR -->
		<adsm:textbox dataType="text" property="recebedor" size="35" width="31%" maxLength="60" label="recebedor" labelWidth="19%"/>
		
		<!-- RG -->
		<adsm:textbox dataType="text" property="rg" label="rg" size="35" width="31%" maxLength="60" labelWidth="19%" width="81%"/>
		
		<!-- OB MANIFESTO -->
		<adsm:textarea property="obManifesto" label="observacao" maxLength="400" rows="2" columns="97"  labelWidth="19%" width="81%"/>
		
		<!-- COMPLEMENTO BAIXA SPP -->
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
