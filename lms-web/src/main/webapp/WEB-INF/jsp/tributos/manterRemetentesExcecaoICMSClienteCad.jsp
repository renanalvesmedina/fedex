<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.tributos.manterRemetentesExcecaoICMSCienteAction">

	<adsm:form  action="/tributos/manterRemetentesExcecaoICMSCliente" 
				idProperty="idRemetenteExcecaoICMSCli" 
				onDataLoadCallBack="myOnDataLoad">
	
  	    <adsm:hidden property="excecaoICMSCliente.idExcecaoICMSCliente" serializable="true"/>
  	    
  	    <adsm:hidden property="excecaoICMSCliente.unidadeFederativa.idUnidadeFederativa" serializable="true"/>
  	    <adsm:textbox label="uf" labelWidth="20%" size="5" property="excecaoICMSCliente.unidadeFederativa.sgUnidadeFederativa" 
			  	    	dataType="text" disabled="true" width="6%"/>
		
		<adsm:hidden property="unidadeFederativa.siglaDescricao" serializable="false"/>			
		
		<adsm:textbox
					dataType="text" 
					property="excecaoICMSCliente.unidadeFederativa.nmUnidadeFederativa" 
					disabled="true"
					required="true"
					width="24%" 
					size="20" />
					  
		<adsm:hidden property="excecaoICMSCliente.tipoTributacaoIcms.idTipoTributacaoIcms" serializable="true"/>	
		<adsm:textbox
					label="tipoTributacao" 
					dataType="text" 
					property="excecaoICMSCliente.tipoTributacaoIcms.dsTipoTributacaoIcms" 
					disabled="true"
					labelWidth="20%" 
					width="30%" 
					size="20" />						  

		<adsm:textbox 
					property="excecaoICMSCliente.nrCNPJParcialDev" 
					maxLength="14"
					serializable="false"
					required="true"
					dataType="text" 
					label="nrCNPJParcialDev"
					labelWidth="20%"
					width="30%"/>
					
					
		<adsm:textbox 
					property="nmDevedor" 
					dataType="text" 
					serializable="false"
					label="nomeDevedor"
					labelWidth="20%"
					disabled="true"
					size="38%"
					width="30%"/>					
					
					
		<adsm:combobox 
					property="excecaoICMSCliente.tpFrete" 
					label="tipoFrete" 
					required="true"
					domain="DM_TIPO_FRETE"
					labelWidth="20%" 
					width="30%"/>
					
		
		<adsm:range label="vigencia" labelWidth="20%" width="30%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial"  required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal" />
		</adsm:range>
		
										
					
		<adsm:combobox  property="tipoCnpj" 
						domain="DM_TIPO_CNPJ"
						labelWidth="20%" 
						defaultValue="P"
						onchange="onChangeComboTipoCnpj(this);"
						width="10%" 
						label="nrCNPJParcialRem">
		</adsm:combobox>
		<adsm:textbox   property="nrCnpjParcialRem" 
						maxLength="8"
						size="14"
						mask="00000000"
						onchange="validateCNPJ(this)"
						required="true"
						dataType="integer"
						width="20%">
				
		</adsm:textbox>								

		<adsm:textbox 
					property="nomeRemetente" 
					dataType="text" 
					serializable="false"
					label="nomeRemetente"
					labelWidth="20%"
					disabled="true"
					size="38%"
					width="30%"/>
		
		
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>		
			<adsm:removeButton/>
		</adsm:buttonBar>

	</adsm:form>
	
</adsm:window>

<script>
	getElement("tipoCnpj").required = true;
	getElement("nrCnpjParcialRem").label = "CNPJ";
	
	/**
		Função chamada na inicialiação da tela
	*/
	function initWindow(eventObj){
		if (eventObj.name == "tab_click" || eventObj.name == "removeButton" ){
			limpar();
		}
	
		setDisabled('btnLimpar', false);
		
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton" ){
			desabilitaTodosCampos();
			validateDtVigenciaFinal(getElementValue("dtVigenciaFinal"));
		} else {
			setDisabled("dtVigenciaFinal", false);
			setMaskCnpj("P");
		}
		
		if(eventObj.name == "storeButton"){
            setFocus('btnLimpar',true,true);
        }
	}	

	
	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript();
		desabilitaTodosCampos(false);
		
	}
	
	
	/**
 	* Function que desabilita todos os campos da tela e seta os valores default
 	*
 	* chamado por: limpar, initWindow
 	*/	
	function desabilitaTodosCampos(val){
		if (val == undefined){ 
			val = true;	
		}
		setDisabled("nrCnpjParcialRem",val);
		setDisabled("tipoCnpj", val);
		 
		var documento = getCurrentDocument("dtVigenciaInicial", null);
        var obj = getElement("dtVigenciaInicial", documento);
        setDisabledCalendar(obj, val, documento);

		setFocusOnFirstFocusableField();				
	}
	
	/**
	 * Função responsável por validar o número parcial do CNPJ
	 */
	function validateCNPJ(cnpj){
	
		if( cnpj.value != "" && cnpj.value.length > 0 ){
			_serviceDataObjects = new Array(); 
			var data = buildFormBeanFromForm(document.forms[0]);
			
			addServiceDataObject(createServiceDataObject("lms.tributos.manterRemetentesExcecaoICMSCienteAction.findNrCNPJParcialRemEqualNrIdentificacaoPessoa", 
			"validateCNPJ", { nrCNPJParcial:cnpj.value, tipoCnpj:getElementValue("tipoCnpj") })); 
			xmit(false);
		} else{
			resetValue("nomeRemetente");
		}
	}
	
	function validateCNPJ_cb(data, errors){
		if(errors != undefined && errors != ""){
			setElementValue("nrCNPJParcialRem", "");
			setElementValue("nomeRemetente", "");
			setFocus("nrCNPJParcialRem");
			alert( errors );
		}else{
			setElementValue("nomeRemetente", data.nomeRemetente);
		}
	
	}
	
	function onChangeComboTipoCnpj(elem){
		comboboxChange({e:elem});
		setMaskCnpj(elem.options[elem.selectedIndex].value);
		setElementValue("nrCNPJParcialRem", "");
		setElementValue("nomeRemetente", "");
		
	}
	
	/**
	  * Modifica a mascara do cnpj de acordo com o tipo de cnpj.
	  */ 
	function setMaskCnpj(tpCnpj) {
		if (tpCnpj == "P") {
			getElement("nrCNPJParcialRem").mask = "00000000";
			getElement("nrCNPJParcialRem").maxLength = 8;
		} else {
			getElement("nrCNPJParcialRem").mask = "00000000000000";
			getElement("nrCNPJParcialRem").maxLength = 14;
		}
	}
	
	function myOnDataLoad_cb(data, error) {
		onDataLoad_cb(data, error);
		if (data.nrCnpjParcialRem.length == 14) {
			setMaskCnpj("C");
			setElementValue("tipoCnpj", "C");
		} else {
			setMaskCnpj("P");
			setElementValue("tipoCnpj", "P");
		}
		validateDtVigenciaFinal(data.dtVigenciaFinal);			
	}

	function validateDtVigenciaFinal(dtVigenciaFinal){
		if (dtVigenciaFinal == undefined || 
				dtVigenciaFinal == "") {
			setDisabled("dtVigenciaFinal", false);
			return;
		}	
		_serviceDataObjects = new Array(); 
		var data = buildFormBeanFromForm(document.forms[0]);
			
		addServiceDataObject(createServiceDataObject("lms.tributos.manterRemetentesExcecaoICMSCienteAction.validateDtVigenciaFinal", 
		"validateDtVigenciaFinal", { dtVigenciaFinal: getElementValue("dtVigenciaFinal") })); 
		xmit(false);
	}
	
	function validateDtVigenciaFinal_cb(data, error){
		if (error != undefined) alert(error);
		if (data.dtVigenciaFinalMaiorIgualDtAtual != undefined &&
				data.dtVigenciaFinalMaiorIgualDtAtual == "false") {
			setDisabled("dtVigenciaFinal", true);
		}
	}	
</script>