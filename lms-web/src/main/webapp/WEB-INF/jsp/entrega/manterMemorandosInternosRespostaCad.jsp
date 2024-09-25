<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterMemorandosInternosRespostaAction" >
	<adsm:form action="/entrega/manterMemorandosInternosResposta" idProperty="idMir"
			service="lms.entrega.manterMemorandosInternosRespostaAction.findByIdCustom"
			onDataLoadCallBack="dataLoadCustom"
			newService="lms.entrega.manterMemorandosInternosRespostaAction.newMaster" >
		
		<adsm:i18nLabels>
			<adsm:include key="LMS-09024" />
			<adsm:include key="LMS-09089" />
			<adsm:include key="LMS-09091" /> 
		</adsm:i18nLabels>
		
		<%-- Campos masterLink para atribuir valores do recibo reembolso --%>
		<adsm:hidden property="reciboReembolso.filial.idFilial" serializable="false" />
		<adsm:hidden property="reciboReembolso.filial.sgFilial" serializable="false" />
		<adsm:hidden property="reciboReembolso.filial.pessoa.nmFantasia" serializable="false" />
		<adsm:hidden property="reciboReembolso.idDoctoServico" serializable="false" />
		<adsm:hidden property="reciboReembolso.nrReciboReembolso" serializable="false" />
		
		<adsm:textbox dataType="text" property="nrMir" 
				label="numeroMIR" maxLength="8" size="14" labelWidth="20%" width="30%" disabled="true"/>

		<adsm:combobox property="tpDocumentoMir" domain="DM_TIPO_DOCUMENTO_MIR" renderOptions="true"
				label="tipoDocumento" labelWidth="20%" width="30%" required="true"
				onchange="onTpDocumentoChange(this);"/>
		<adsm:hidden property="tpDocumentoMirOld" value="" serializable="false" />
 
		<adsm:combobox property="tpMir" domain="DM_TIPO_MIR" renderOptions="true"
				label="tipoMir" labelWidth="20%" width="80%" required="true" 
				onchange="onTpMirChange(this);" >
			<%--  adsm:propertyMapping modelProperty="description" relatedProperty="tpMir.description" / --%>
		</adsm:combobox>
		<adsm:hidden property="tpMir.description" />
		<adsm:hidden property="tpMirOld" value="" serializable="false" />
		<adsm:hidden property="tpMirDescriptionOld" value="" serializable="false" />
		
		
<%-- -- Origem -- --%>
		<adsm:section caption="origem" />
			<adsm:hidden property="filialByIdFilialOrigem.idFilial" />
			<adsm:textbox dataType="text" property="filialByIdFilialOrigem.sgFilial"
	    			label="filial" size="3" labelWidth="20%" width="80%" disabled="true" required="true" serializable="false" >
	         	<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia"
	         			size="30" disabled="true" serializable="false" />
	         	<adsm:hidden property="filialByIdFilialOrigem.siglaNomeFilial" />
		    </adsm:textbox>
		    
			<adsm:hidden property="usuarioByIdUsuarioCriacao.idUsuario" />
			<adsm:textbox dataType="text" property="usuarioByIdUsuarioCriacao.nrMatricula"
	    			label="funcionario" size="16" labelWidth="20%" width="80%" disabled="true" required="true" serializable="false" >
	         	<adsm:textbox dataType="text" property="usuarioByIdUsuarioCriacao.nmUsuario"
	         			size="30" disabled="true" serializable="false" />
		    </adsm:textbox>
	        
<%-- -- Destino -- --%>
		<adsm:section caption="destino" />
			<adsm:hidden property="tpEmpresaMercurio" value="M" serializable="false" />
		
			<adsm:lookup dataType="text" property="filialByIdFilialDestino"
					idProperty="idFilial" criteriaProperty="sgFilial"
	    			service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupFilial"
	    			action="/municipios/manterFiliais"
	    			onDataLoadCallBack="filialDestinoDataLoad" onPopupSetValue="filialDestinoPopup"
	    			onchange="return filialDestinoChange(this);"
	    			label="filial" size="3" maxLength="3" labelWidth="20%" width="80%" exactMatch="true" required="true" >
	    		<adsm:propertyMapping criteriaProperty="tpEmpresaMercurio" modelProperty="empresa.tpEmpresa" />
	    	
	         	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
	         	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.siglaNomeFilial" modelProperty="siglaNomeFilial" />
	         	<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia"
	         			size="30" disabled="true" serializable="false" />
	         	<adsm:hidden property="filialByIdFilialDestino.siglaNomeFilial" />
		    </adsm:lookup>
			<adsm:hidden property="sgFilialDestinoOld" value="" serializable="false" />
			
			<adsm:lookup dataType="text" property="usuarioByIdUsuarioRecebimento" idProperty="idUsuario" criteriaProperty="nrMatricula"
				     service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView"
				     label="funcionario" size="16" maxLength="16" labelWidth="20%" width="80%" >				
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" 
						modelProperty="filial.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" 
						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
				
				<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioRecebimento.nmUsuario" modelProperty="nmUsuario"/>				
				<adsm:textbox dataType="text" property="usuarioByIdUsuarioRecebimento.nmUsuario" 
						size="30" maxLength="45" disabled="true" serializable="false"/>
			</adsm:lookup>

<%-- -- Rastreamento -- --%>
		<adsm:section caption="rastreamento" />

			<adsm:textbox dataType="JTDateTimeZone" property="dhEmissao" 
					label="dataEmissao" labelWidth="20%" width="80%" disabled="true" />
			<adsm:textbox dataType="JTDateTimeZone" property="dhEnvio" 
					label="dataEnvio" labelWidth="20%" width="80%" disabled="true" />
			<adsm:textbox dataType="JTDateTimeZone" property="dhRecebimento" 
					label="dataRecebimento" labelWidth="20%" width="80%" disabled="true" />
		
		<adsm:hidden property="blBloqueiaFilhos" serializable="false" />
				
		<adsm:buttonBar>
			<adsm:button id="btnEmitir" caption="emitir" onclick="onEmitirClick();" />
			<adsm:button id="btnEnvio" caption="confirmarEnvio" onclick="onEnvioClick();" />
			<adsm:button id="btnConfirmar" caption="confirmarRecebimento" onclick="onRecebimentoClick();" />
			<adsm:storeButton id="storeButton" callbackProperty="afterStore" />
			<adsm:newButton id="newButton" />
			<adsm:removeButton id="removeButton" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script type="text/javascript">
<!--

	document.getElementById('filialByIdFilialOrigem.sgFilial').serializable = true;
	document.getElementById('tpEmpresaMercurio').masterLink = true;

	function initWindow(eventObj) {
		var blDetalhamento = eventObj.name == "gridRow_click" || eventObj.name == "storeButton"
				|| (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq");

		if (!blDetalhamento) {
			setElementValue("blBloqueiaFilhos","false");
			
			setElementValue("filialByIdFilialOrigem.idFilial",getPesqValue("idFilialSessao"));
			setElementValue("filialByIdFilialOrigem.sgFilial",getPesqValue("sgFilialSessao"));
			setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia",getPesqValue("nmFilialSessao"));
			setElementValue("filialByIdFilialOrigem.siglaNomeFilial",getPesqValue("siglaNomeFilialSessao"));
			
			setElementValue("usuarioByIdUsuarioCriacao.idUsuario",getPesqValue("idUsuarioSessao"));
			setElementValue("usuarioByIdUsuarioCriacao.nrMatricula",getPesqValue("nrMatriculaSessao"));
			setElementValue("usuarioByIdUsuarioCriacao.nmUsuario",getPesqValue("nmUsuarioSessao"));
			
			setDisabledTabCustom("reem",true);
			setDisabledTabCustom("comp",true);
			
			comportamentoDetalhe();
			
		} else if (eventObj.name == "tab_click" && eventObj.src.tabGroup.oldSelectedTab.properties.id != "pesq") {
			comportamentoDetalhe();
		}
	}

	function dataLoadCustom_cb(data,error) {
		onDataLoad_cb(data,error);
		if (data != undefined) {
			// Habilita aba dos filhos de acordo com o tipo do documento.
			var tpDocumentoMir = getNestedBeanPropertyValue(data,"tpDocumentoMir.value");
			setDisabledTabCustom("reem",tpDocumentoMir != 'R');
			setDisabledTabCustom("comp",tpDocumentoMir != 'C');
		
			comportamentoDetalhe();
			
			var tpMirDescription = getNestedBeanPropertyValue(data,"tpMir.description");
			setElementValue("tpMir.description",tpMirDescription);
			
			setElementValue("tpDocumentoMirOld",tpDocumentoMir);
			setElementValue("tpMirOld",getNestedBeanPropertyValue(data,"tpMir.value"));
			setElementValue("tpMirDescriptionOld",tpMirDescription);
			setElementValue("sgFilialDestinoOld",getNestedBeanPropertyValue(data,"filialByIdFilialDestino.sgFilial"));
		}
	}

	function comportamentoDetalhe(evento) {
		// Estado padrão dos botões.
		// Desbilita envio se filial de origem é diferente do usuário logado.
		setDisabled("btnEnvio",getPesqValue("idFilialSessao") != getElementValue("filialByIdFilialOrigem.idFilial"));
		if (getElementValue("idMir") != "")
			setDisabled("removeButton",false);
		setDisabled("btnConfirmar",true);
		
		// Se data de Emissão for preenchida:
		var dhEmissao = getElementValue("dhEmissao");
		if (dhEmissao != undefined && dhEmissao != "") {
			setElementValue("blBloqueiaFilhos","true");
						
			// Se data de Envio for preenchida:
			var dhEnvio = getElementValue("dhEnvio");
			if (dhEnvio != undefined && dhEnvio != "") {
				setDisabled("btnEnvio",true);
				setDisabled("removeButton",true);
				// Se data de Recebimento for preenchida:
				var dhRecebimento = getElementValue("dhRecebimento");
				if (dhRecebimento == undefined || dhRecebimento == "") {
					// Desbilita recebimento se filial de destino é diferente do usuário logado.
					setDisabled("btnConfirmar",getPesqValue("idFilialSessao") != getElementValue("filialByIdFilialDestino.idFilial"));
				}
				
			}
			
		} else {
			setElementValue("blBloqueiaFilhos","false");
			setDisabled("btnEnvio",true);
		}
		
		// Desabilita campos se MIR já foi emitido:
		var blEmitido = dhEmissao != '';
		setDisabled("tpMir",blEmitido);
		setDisabled("tpDocumentoMir",blEmitido);
		setDisabled("usuarioByIdUsuarioRecebimento.idUsuario",blEmitido);
		setDisabled("filialByIdFilialDestino.idFilial",getElementValue("tpMir") != "DO" || blEmitido);
		setDisabled("storeButton",blEmitido);

		if (evento != undefined && evento == "store") {
			setFocusOnNewButton();
		} else {
			if (blEmitido) {
				setFocusOnNewButton();
			} else {
				setFocusOnFirstFocusableField();
			}
		}
	}

	function afterStore_cb(data,error,key) {
		store_cb(data,error,key);
		
		if(error != undefined) {
		
			var tabGroup = getTabGroup(this.document);
			if (key == "LMS-09010")
				tabGroup.selectTab('comp',{name:'tab_click'});
			else if (key == "LMS-09011")
				tabGroup.selectTab('reem',{name:'tab_click'});
			else
				tabGroup.selectTab('cad',{name:'tab_click'});
				
		} else if (data != undefined) {
			comportamentoDetalhe("store");
		}
	}
	

		
	function onTpMirChange(elem) {
		comboboxChange({e:elem});
		
		if (elem.selectedIndex > 0)
			setElementValue("tpMir.description", elem.options[elem.selectedIndex].text);
		else
			resetValue("tpMir.description");
		
		if (getElementValue("tpDocumentoMir") != "") {
			validateTpDocumentoMir2("validateTpDocumentoForTpMir");
		} else {
			setDisabledTabCustom("reem",true);
			setDisabledTabCustom("comp",true);
			onChangeTpMirDefault();
		}
	}
	
	function onTpDocumentoChange(elem) {
		comboboxChange({e:elem});
		validateTpDocumentoMir(elem.value);
	}
	
	/**
	 * Utilizado no change do campo tpMir.
	 */
	function validateTpDocumentoMir(value) {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",getElementValue("idMir"));
		setNestedBeanPropertyValue(data,"tpDocumentoMir",value);
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.validateTpDocumentoMir",
				"validateTpDocumentoMir",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function validateTpDocumentoMir_cb(data,error) {
		if (error != undefined) {
			setElementValue("tpDocumentoMir",getElementValue("tpDocumentoMirOld"));
			alert(error);
		} else {
			var newValue = getElementValue("tpDocumentoMir");
			setElementValue("tpDocumentoMirOld",getElementValue("tpDocumentoMir"));
						
			// Valida se tipo de mir e filial destino estão preenchidos:
			var camposPreenchidos = getElementValue("filialByIdFilialDestino.idFilial") != "" &&
					getElementValue("tpMir") != "";
			
			setDisabledTabCustom("reem",!(newValue == 'R' && camposPreenchidos));
			setDisabledTabCustom("comp",!(newValue == 'C' && camposPreenchidos));
		}
	}
		
	/**
	 * Utilizado no change dos campos tpMir e filial destino.
	 */	
	function validateTpDocumentoMir2(callBack) {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",getElementValue("idMir"));
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.validateTpDocumentoMir",
				callBack,data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function filialDestinoDataLoad_cb(data) {
		filialByIdFilialDestino_sgFilial_exactMatch_cb(data);
		
		if (data != undefined && data.length > 0) {
			validateTpDocumentoMir2("validateTpDocumentoForFilial");
		} else {
			setDisabledTabCustom("reem",true);
			setDisabledTabCustom("comp",true);
		}
	}
	
	function filialDestinoPopup(data) {
		if (data != undefined) {
			validateTpDocumentoMir2("validateTpDocumentoForFilial");
		}
	}
	
	function filialDestinoChange(elem) {
		if (elem.value == "") {
			validateTpDocumentoMir2("validateTpDocumentoForFilial");
		}
		return filialByIdFilialDestino_sgFilialOnChangeHandler();
	}
	
	function validateTpDocumentoForFilial_cb(data,error) {
		if (error != undefined) {
			var sgFilialOld = getElementValue("sgFilialDestinoOld");
			var sgFilialNow = getElementValue("filialByIdFilialDestino.sgFilial");
						
			if (sgFilialOld != sgFilialNow) {
				alert(i18NLabel.getLabel("LMS-09089"));
				setElementValue("filialByIdFilialDestino.sgFilial",getElementValue("sgFilialDestinoOld"));
				lookupChange({e:document.forms[0].elements["filialByIdFilialDestino.idFilial"],forceChange:true});
			}
		} else {
			var newValue = getElementValue("filialByIdFilialDestino.sgFilial");
			setElementValue("sgFilialDestinoOld",newValue);
			
			// Valida se tipo de mir e filial destino estão preenchidos:
			var camposPreenchidos = getElementValue("filialByIdFilialDestino.idFilial") != "" &&
					getElementValue("tpMir") != "";
			
			var tpDocumentoMir = getElementValue("tpDocumentoMir");			
			setDisabledTabCustom("reem",!(tpDocumentoMir == 'R' && camposPreenchidos));
			setDisabledTabCustom("comp",!(tpDocumentoMir == 'C' && camposPreenchidos));
		}
	}
	
	function validateTpDocumentoForTpMir_cb(data,error) {
		if (error != undefined) {
			setElementValue("tpMir",getElementValue("tpMirOld"));
			setElementValue("tpMir.description",getElementValue("tpMirDescriptionOld"));
			
			alert(i18NLabel.getLabel("LMS-09091"));
		} else {
			onChangeTpMirDefault();
			
			var newValue = getElementValue("tpDocumentoMir");
			
			// Valida se tipo de mir e filial destino estão preenchidos:
			var camposPreenchidos = getElementValue("filialByIdFilialDestino.idFilial") != "" &&
					getElementValue("tpMir") != "";
			
			setDisabledTabCustom("reem",!(newValue == 'R' && camposPreenchidos));
			setDisabledTabCustom("comp",!(newValue == 'C' && camposPreenchidos));
		}
	}
	
	function onChangeTpMirDefault() {
		var newValue = getElementValue("tpMir");
		setElementValue("tpMirOld",newValue);
		
		var newValue2 = getElementValue("tpMir.description");
		setElementValue("tpMirDescriptionOld",newValue2);
		
		var isEntregaAdm = (newValue == 'AE' || newValue == 'EA');
		
		if (isEntregaAdm) {		
			setElementValue("filialByIdFilialDestino.idFilial",getPesqValue("idFilialSessao"));
			setElementValue("filialByIdFilialDestino.sgFilial",getPesqValue("sgFilialSessao"));
			setElementValue("filialByIdFilialDestino.pessoa.nmFantasia",getPesqValue("nmFilialSessao"));
			setElementValue("filialByIdFilialDestino.siglaNomeFilial",getPesqValue("siglaNomeFilialSessao"));
		} else { 
			resetValue("filialByIdFilialDestino.idFilial");
			resetValue("filialByIdFilialDestino.pessoa.nmFantasia");
		}
		
		setDisabled("filialByIdFilialDestino.idFilial",isEntregaAdm);
	}
	
	function onEmitirClick() {
		if (getElementValue("dhEmissao") == "") {
			var data = new Array();
			setNestedBeanPropertyValue(data,"idMir",getElementValue("idMir"));
			var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.updateConfirmarEmissao",
					"updateConfirmarEmissao",data);
			xmit({serviceDataObjects:[sdo]});
		} else {
			emitirMIR(true);	
		}		
	}
	
	function onEnvioClick() {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",getElementValue("idMir"));
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.updateConfirmarEnvio",
				"updateConfirmarEnvio",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function onRecebimentoClick() {
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",getElementValue("idMir"));
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.updateConfirmarRecebimento",
				"updateConfirmarRecebimento",data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function updateConfirmarEnvio_cb(data,error) {
		onDataLoad_cb(data,error);
		
		//setDisabled("btnEnvio",true);
		//setDisabled("removeButton",true);
		//setDisabled("btnConfirmar",false);
		comportamentoDetalhe();
	}
	
	function updateConfirmarEmissao_cb(data,error) {
		onDataLoad_cb(data,error);
		// bloqueia os campos das abas filhas.
		setElementValue("blBloqueiaFilhos","true");
		
		//setDisabled("btnEnvio",false);
		//setDisabled("removeButton",false);
		//setDisabled("btnConfirmar",true);
		
		comportamentoDetalhe();
		emitirMIR(false);
	}
	
	function updateConfirmarRecebimento_cb(data,error) {
		onDataLoad_cb(data,error);
		
		//setDisabled("btnEnvio",true);
		//setDisabled("removeButton",true);
		//setDisabled("btnConfirmar",true);
		comportamentoDetalhe();
		if (getElementValue("tpMir") == "DO") {
			if (confirm(i18NLabel.getLabel("LMS-09024"))) {
				showModalDialog('entrega/manterMemorandosInternosResposta.do?cmd=receb',window,
					'unardorned:no;scroll:no;resizable:no;status:no;center=yes;help:no;dialogWidth:600px;dialogHeight:140px;');
			}
		}
	}
	 
	
	function getPesqValue(propertyName) {
		var tabGroup = getTabGroup(this.document);
		return tabGroup.getTab("pesq").getFormProperty(propertyName);
	}
	
	function setDisabledTabCustom(tabName,disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab(tabName, disabled);
	}
	
	function emitirMIR(blReemissao){
		var data = new Array();
		setNestedBeanPropertyValue(data,"idMir",getElementValue("idMir"));
		setNestedBeanPropertyValue(data,"tpDocumentoMir",getElementValue("tpDocumentoMir"));
		setNestedBeanPropertyValue(data,"blReemissao",blReemissao);
		var sdo = createServiceDataObject("lms.entrega.emitirMIRAction","openPdf",data);
		executeReportWindowed(sdo, 'pdf');
	}
	
//-->
</script>