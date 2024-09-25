<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.manterMemorandosInternosRespostaAction" onPageLoadCallBack="pageLoadCustom" >
	<adsm:i18nLabels>
		<adsm:include key="LMS-09026" />
		<adsm:include key="LMS-09099" />
		<adsm:include key="LMS-00013" />
	</adsm:i18nLabels>
	  
	<adsm:form action="/entrega/manterMemorandosInternosResposta" idProperty="idMir" height="120" >
	
		<adsm:hidden property="idFilialSessao" serializable="false" />
		<adsm:hidden property="sgFilialSessao" serializable="false" />
		<adsm:hidden property="nmFilialSessao" serializable="false" />
		<adsm:hidden property="siglaNomeFilialSessao" serializable="false" />
		<adsm:hidden property="tpFilialSessao" serializable="false" />
		<adsm:hidden property="idUsuarioSessao" serializable="false" />
		<adsm:hidden property="nrMatriculaSessao" serializable="false" />
		<adsm:hidden property="nmUsuarioSessao" serializable="false" /> 
	
		<adsm:hidden property="tpEmpresaMercurio" value="M" serializable="false" />
	
		<adsm:lookup dataType="text" property="filialByIdFilialOrigem"
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupFilial"
    			action="/municipios/manterFiliais"
    			onDataLoadCallBack="filialOrigemDataLoad"
    			afterPopupSetValue="filialOrigemPopup"    			
    			label="filialOrigem" size="3" maxLength="3" labelWidth="14%" width="40%" exactMatch="true" >
    		<adsm:propertyMapping criteriaProperty="tpEmpresaMercurio" modelProperty="empresa.tpEmpresa" />
    			
         	<adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialByIdFilialOrigem.pessoa.nmFantasia"
         			size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
	    
	    <adsm:lookup dataType="text" property="filialByIdFilialDestino"
				idProperty="idFilial" criteriaProperty="sgFilial"
    			service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupFilial"
    			action="/municipios/manterFiliais"
    			label="filialDestino" size="3" maxLength="3" labelWidth="14%" width="32%" exactMatch="true" >
    		<adsm:propertyMapping criteriaProperty="tpEmpresaMercurio" modelProperty="empresa.tpEmpresa" />
    		
         	<adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia" />
         	<adsm:textbox dataType="text" property="filialByIdFilialDestino.pessoa.nmFantasia"
         			size="30" disabled="true" serializable="false" />
	    </adsm:lookup>
		
		<adsm:textbox dataType="integer" property="nrMir" label="numeroMIR" maxLength="8" size="7" labelWidth="14%" width="9%" mask="00000000"/>

		<adsm:combobox property="tpMir" domain="DM_TIPO_MIR" label="tipoMir" labelWidth="9%" width="22%" 
					   onchange="onTpMirChange(this);" renderOptions="true" />

		<adsm:combobox property="tpDocumentoMir" domain="DM_TIPO_DOCUMENTO_MIR" label="tipoDocumento" 
		               labelWidth="14%" width="32%" onchange="onTpDocumentoMirChange(this);" boxWidth="150"
		               renderOptions="true"/>

		<adsm:textbox dataType="text"
				property="reciboReembolso.tpDocumentoServico.description"
				label="numeroRecibo" labelWidth="14%" width="86%" size="5" disabled="true">

			<adsm:lookup dataType="text"
					property="reciboReembolso.filial"
					idProperty="idFilial" criteriaProperty="sgFilial"
					service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupFilial"
					action="/municipios/manterFiliais" size="3" maxLength="3"
					picker="false" onDataLoadCallBack="doctoServicoDataLoad"
					onPopupSetValue="filialDoctoServicoPopup" 
					onchange="return onChangeFilialDocumentoServico(this);" >
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.pessoa.nmFantasia"
						modelProperty="pessoa.nmFantasia" />
			</adsm:lookup>

			<adsm:lookup dataType="integer" property="reciboReembolso" 
					idProperty="idDoctoServico" criteriaProperty="nrReciboReembolso"
					service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupServiceDocumentNumberRRE" 
					action="/entrega/manterReembolsos" size="10" onPopupSetValue="reciboReembolsoPopup"
					maxLength="8" serializable="true" mask="00000000" >
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filial.idFilial"
						modelProperty="filialByIdFilialOrigem.idFilial" />
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filial.sgFilial"
						modelProperty="filialByIdFilialOrigem.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="reciboReembolso.filial.pessoa.nmFantasia"
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" inlineQuery="false" />
						
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.idFilial"
						modelProperty="filialByIdFilialOrigem.idFilial" blankFill="false" />
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.sgFilial"
						modelProperty="filialByIdFilialOrigem.sgFilial" blankFill="false" />
				<adsm:propertyMapping relatedProperty="reciboReembolso.filial.pessoa.nmFantasia"
						modelProperty="filialByIdFilialOrigem.pessoa.nmFantasia" blankFill="false" />
						
				<adsm:propertyMapping criteriaProperty="tpChequesRecolhidos" modelProperty="tpSituacaoRecibo" />
			</adsm:lookup>
			<adsm:hidden property="tpChequesRecolhidos" value="CR" serializable="false" />
			<adsm:hidden property="reciboReembolso.filial.pessoa.nmFantasia" />
		</adsm:textbox>
		

		<adsm:range label="periodoEmissao" labelWidth="14%" width="40%" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="dhEmisssaoInicial" />
			<adsm:textbox dataType="JTDate" property="dhEmisssaoFinal" />
		</adsm:range>
		<adsm:range label="periodoEnvio" labelWidth="14%" width="32%" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="dhEnvioInicial" />
			<adsm:textbox dataType="JTDate" property="dhEnvioFinal" />
		</adsm:range>
		<adsm:range label="periodoRecebimento2" labelWidth="14%" width="86%" maxInterval="30" >
			<adsm:textbox dataType="JTDate" property="dhRecebimentoInicial" cellStyle="vertical-align:bottom" />
			<adsm:textbox dataType="JTDate" property="dhRecebimentoFinal" cellStyle="vertical-align:bottom" />
		</adsm:range>
<%-- -- Origem -- --%>

		<adsm:section caption="origem" />
			<adsm:lookup dataType="text" property="usuarioByIdUsuarioCriacao" idProperty="idUsuario" criteriaProperty="nrMatricula"
				     service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView"
				     label="funcionario" size="16" maxLength="16" labelWidth="14%" width="86%" >
							
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.idFilial" modelProperty="filial.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.sgFilial" 
						modelProperty="filial.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialOrigem.pessoa.nmFantasia" 
						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
				
				<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioCriacao.nmUsuario" modelProperty="nmUsuario"/>				
				<adsm:textbox dataType="text" property="usuarioByIdUsuarioCriacao.nmUsuario" 
						size="35" maxLength="45" disabled="true" serializable="false"/>
			</adsm:lookup>
<%-- -- Destino -- --%>
		<adsm:section caption="destino" />
		   	<adsm:lookup dataType="text" property="usuarioByIdUsuarioRecebimento" idProperty="idUsuario" criteriaProperty="nrMatricula"
				     service="lms.entrega.manterMemorandosInternosRespostaAction.findLookupUsuarioFuncionario" 
				     action="/configuracoes/consultarFuncionariosView"
				     label="funcionario" size="16" maxLength="16" labelWidth="14%" width="86%">
							
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.idFilial" modelProperty="filial.idFilial" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.sgFilial" 
						modelProperty="filial.sgFilial" inlineQuery="false" />
				<adsm:propertyMapping criteriaProperty="filialByIdFilialDestino.pessoa.nmFantasia" 
						modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
				
				<adsm:propertyMapping relatedProperty="usuarioByIdUsuarioRecebimento.nmUsuario" modelProperty="nmUsuario"/>				
				<adsm:textbox dataType="text" property="usuarioByIdUsuarioRecebimento.nmUsuario" 
						size="35" maxLength="45" disabled="true" serializable="false"/>
			</adsm:lookup>

		<adsm:buttonBar freeLayout="true" >
			<adsm:findButton callbackProperty="mir" />
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid property="mir" idProperty="idMir" rows="8" scrollBars="horizontal" gridHeight="160"
			service="lms.entrega.manterMemorandosInternosRespostaAction.findPaginatedCustom"
			rowCountService="lms.entrega.manterMemorandosInternosRespostaAction.getRowCountCustom" >
		
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="numeroMIR" property="filialByIdFilialOrigem.sgFilial" width="50" />
			<adsm:gridColumn title="" property="nrMir" width="50" mask="00000000" dataType="integer" />
		</adsm:gridColumnGroup> 
		
		<adsm:gridColumnGroup separatorType="FILIAL" >
			<adsm:gridColumn title="filialDestino" property="filialByIdFilialDestino.sgFilial" width="90" />
			<adsm:gridColumn title="" property="filialByIdFilialDestino.pessoa.nmFantasia" width="90" />
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn width="150" title="tipoMir" property="tpMir.description" />
		<adsm:gridColumn width="140" title="tipoDocumento" property="tpDocumento.description" />
		
		<adsm:gridColumn width="120" title="dataEmissao" property="dhEmissao" dataType="JTDateTimeZone" />
		<adsm:gridColumn width="120" title="dataEnvio" property="dhEnvio" dataType="JTDateTimeZone" />
		<adsm:gridColumn width="135" title="dataRecebimento" property="dhRecebimento" dataType="JTDateTimeZone" />
		
		<adsm:buttonBar>
			<adsm:removeButton /> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script type="text/javascript">
<!--
 
	document.getElementById("idFilialSessao").masterLink = true;
	document.getElementById("sgFilialSessao").masterLink = true;
	document.getElementById("nmFilialSessao").masterLink = true;
	document.getElementById("siglaNomeFilialSessao").masterLink = true;
	document.getElementById("tpFilialSessao").masterLink = true;
	document.getElementById("idUsuarioSessao").masterLink = true;
	document.getElementById("nrMatriculaSessao").masterLink = true;
	document.getElementById("nmUsuarioSessao").masterLink = true;
	document.getElementById("reciboReembolso.tpDocumentoServico.description").masterLink = true;
	document.getElementById('tpEmpresaMercurio').masterLink = true;
	
	function pageLoadCustom_cb(data,error) {
		onPageLoad_cb(data,error);
		setElementValue("reciboReembolso.tpDocumentoServico.description", "RRE");
		setRowVisibility("reciboReembolso.tpDocumentoServico.description", false);
		if (document.getElementById("tpDocumentoMir").masterLink && getElementValue("tpDocumentoMir") == 'R') {
			setRowVisibility("reciboReembolso.tpDocumentoServico.description", true);
		}
		
		setDisabled("reciboReembolso.idDoctoServico",false);
		setDisabled("reciboReembolso.nrReciboReembolso",true);
		
		findInfoUsuarioLogado();
	}

	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.entrega.manterMemorandosInternosRespostaAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		if (error != undefined) {
			alert(error);
			return false;
		}
		
		setElementValue("idFilialSessao",getNestedBeanPropertyValue(data,"filial.idFilial"));
		setElementValue("sgFilialSessao",getNestedBeanPropertyValue(data,"filial.sgFilial"));
		setElementValue("nmFilialSessao",getNestedBeanPropertyValue(data,"filial.pessoa.nmFantasia"));
		setElementValue("siglaNomeFilialSessao",getNestedBeanPropertyValue(data,"filial.siglaNomeFilial"));
		setElementValue("tpFilialSessao",getNestedBeanPropertyValue(data,"filial.tpFilial"));
		
		setElementValue("idUsuarioSessao",getNestedBeanPropertyValue(data,"usuario.idUsuario"));
		setElementValue("nrMatriculaSessao",getNestedBeanPropertyValue(data,"usuario.nrMatricula"));
		setElementValue("nmUsuarioSessao",getNestedBeanPropertyValue(data,"usuario.nmUsuario"));
	}


	function onTpMirChange(elem) {
		comboboxChange({e:elem});
		var isEntregaAdm = (elem.value == 'AE' || elem.value == 'EA');
		
		if (isEntregaAdm) {
			//setElementValue("filialByIdFilialOrigem.idFilial",getElementValue("idFilialSessao"));
			//setElementValue("filialByIdFilialOrigem.sgFilial",getElementValue("sgFilialSessao"));
			//setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia",getElementValue("nmFilialSessao"));
			var idFilial = getElementValue("filialByIdFilialOrigem.idFilial");
			if (idFilial != undefined && idFilial != "") {
				setElementValue("filialByIdFilialDestino.idFilial",idFilial);
				setElementValue("filialByIdFilialDestino.sgFilial",getElementValue("filialByIdFilialOrigem.sgFilial"));
				setElementValue("filialByIdFilialDestino.pessoa.nmFantasia",getElementValue("filialByIdFilialOrigem.pessoa.nmFantasia"));
			}
		}
		
		setDisabled("filialByIdFilialDestino.idFilial",isEntregaAdm);
	}
	
	function validateTab() {
		var blFilialMatriz = (getElementValue("tpFilialSessao") == "MA");

		var blReciboLink = document.getElementById("reciboReembolso.idDoctoServico").masterLink;

		if (blReciboLink != "true") {
			if (!blFilialMatriz) {
				var idOrigem = getElementValue("filialByIdFilialOrigem.idFilial");
				var idDestino = getElementValue("filialByIdFilialDestino.idFilial");
				var idSessao = getElementValue("idFilialSessao");
				
				if (idOrigem != idSessao && idDestino != idSessao) {
					alert(i18NLabel.getLabel("LMS-09026"));
					setFocusOnFirstFocusableField();
					return false;
				}
			}
	
			var idOrigem = getElementValue("filialByIdFilialOrigem.idFilial");		
			var blNrMirPreenchido = getElementValue("nrMir") != ''
			if (idOrigem == '' && blNrMirPreenchido) {
				alert(i18NLabel.getLabel("LMS-09099"));
				return false;
			}
			
			var rangeEmissao = (getElementValue("dhEmisssaoInicial") != '' && getElementValue("dhEmisssaoFinal") != '');
			var rangeEnvio = (getElementValue("dhEnvioInicial") != '' && getElementValue("dhEnvioFinal") != '');
			var rangeRecebimento = (getElementValue("dhRecebimentoInicial") != '' && getElementValue("dhRecebimentoFinal") != '');
			var blReembolsoPreenchido = (getElementValue("reciboReembolso.idDoctoServico") != '');
			if (!blNrMirPreenchido && !rangeEmissao && !rangeEnvio && !rangeRecebimento && !blReembolsoPreenchido) {
				alert(i18NLabel.getLabel("LMS-00013")
						+ document.getElementById("nrMir").label + ', ' 
						+ document.getElementById("reciboReembolso.idDoctoServico").label + ', ' 
						+ document.getElementById("dhEmisssaoInicial").label + ', ' 
						+ document.getElementById("dhEnvioInicial").label + ', ' 
						+ document.getElementById("dhRecebimentoInicial").label + ".");
				return false;
			}
		}
				
		return true;	
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "tab_click") {
			setDisabledTabsFilhas(true);
		} else {
			setDisabled("filialByIdFilialOrigem.idFilial",false);
			setDisabled("filialByIdFilialDestino.idFilial",false);
			
			setDisabled("reciboReembolso.idDoctoServico",false);
			setDisabled("reciboReembolso.nrReciboReembolso",true);
			
			setRowVisibility("reciboReembolso.tpDocumentoServico.description",false);
			
			setFocusOnFirstFocusableField();
		}
	}
	
	function setDisabledTabsFilhas(disabled) {
		var tabGroup = getTabGroup(this.document);
		tabGroup.setDisabledTab("comp", disabled);
		tabGroup.setDisabledTab("reem", disabled);
	}
	
	var isPopup = false;
	function doctoServicoDataLoad_cb(data) {
		isPopup = false;
		var blRetorno = reciboReembolso_filial_sgFilial_exactMatch_cb(data);
		if (!isPopup) {
			setDisabled("reciboReembolso.nrReciboReembolso", !blRetorno);
			if (blRetorno) {
				setFocus(document.getElementById("reciboReembolso.nrReciboReembolso"));
			}
		}
		return blRetorno;
	}
	
	function onChangeFilialDocumentoServico(elem) {
		setDisabled("reciboReembolso.nrReciboReembolso",elem.value == "");
		
		return reciboReembolso_filial_sgFilialOnChangeHandler();
	}
	
	
	function onTpDocumentoMirChange(elem) {
		comboboxChange({e:elem});		
		setRowVisibility("reciboReembolso.tpDocumentoServico.description", elem.value == 'R');
	}
	
	function filialDoctoServicoPopup(data) {
		isPopup = true;
		setDisabled("reciboReembolso.nrReciboReembolso",false);
		//setFocus("reciboReembolso.nrReciboReembolso");
	}
	
	function reciboReembolsoPopup(data) {
		setDisabled("reciboReembolso.nrReciboReembolso",false);
	}
	
	
	// PopupSetValue da lookup de filial de origem
	function filialOrigemPopup(data) {
		onTpMirChange(document.getElementById("tpMir"));
		return true;
	}
	
	function filialOrigemDataLoad_cb(data) {
		var retorno = filialByIdFilialOrigem_sgFilial_exactMatch_cb(data);
		onTpMirChange(document.getElementById("tpMir"));
		return retorno;
	}
//-->
</script>