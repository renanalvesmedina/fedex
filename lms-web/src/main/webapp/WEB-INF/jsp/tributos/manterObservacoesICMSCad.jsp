<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.manterObservacoesICMSAction" onPageLoadCallBack="myPageLoadCallBack">
	<adsm:form action="/tributos/manterObservacoesICMS" idProperty="idDescricaoTributacaoIcms"
	newService="lms.tributos.manterObservacoesICMSAction.newMaster" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:combobox  
			service="lms.tributos.manterObservacoesICMSAction.findUfAtivo" 
			property="unidadeFederativa.idUnidadeFederativa" 
			optionLabelProperty="siglaDescricao" 
			optionProperty="idUnidadeFederativa" onlyActiveValues="true"
			style="width:222px"
			label="ufOrigem" required="true">
		</adsm:combobox>
		
		<adsm:hidden property="idTipoTributacaoIcms" serializable="false" />
        
        <adsm:combobox property="tipoTributacaoIcms.idTipoTributacaoIcms" 
        			   optionLabelProperty="dsTipoTributacaoIcms" 
        			   optionProperty="idTipoTributacaoIcms" 
        			   service="lms.tributos.manterObservacoesICMSAction.findTipoTributacao" 
        			   label="tipoTributacao" 
        			   required="true" 
        			   boxWidth="220" 
        			   autoLoad="false"/> 
        
		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:button caption="limpar" onclick="limpar()" disabled="false" id="btnLimpar"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>


	function initWindow(eventObj){
	
		//desabilita os campos da tela quando vier da grid ou depois de salvar	
		if (eventObj.name == "gridRow_click" || eventObj.name == "storeButton" || getElementValue("idDescricaoTributacaoIcms") != ""  ){
			desabilitaTodosCampos();
			setDisabled('btnLimpar', false);
			setFocus('btnLimpar',true,true);			
		}else{
			desabilitaTodosCampos(false);
			setDisabled('btnLimpar', false);
			setFocusOnFirstFocusableField(document);
		}		
		
		// Caso seja tabClick
		if (eventObj.name == "tab_click"){
			
			var idTipoTributacaoIcms = getElementValue("idTipoTributacaoIcms");
			
			// Caso o idTipoTributacaoIcms esteja setado
			if (idTipoTributacaoIcms != ""){
				setElementValue("tipoTributacaoIcms.idTipoTributacaoIcms", idTipoTributacaoIcms);
				setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms", true);
				document.getElementById("tipoTributacaoIcms.idTipoTributacaoIcms").masterLink = "true";
			}
			
		}
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
		setDisabled("unidadeFederativa.idUnidadeFederativa",val);
		setDisabled("tipoTributacaoIcms.idTipoTributacaoIcms",val);
	}

	/**
 	* Function que limpa os campos da tela e desabilita todos os campos.
 	*
 	* chamado por: botão limpar
 	*/
	function limpar(){
		newButtonScript(document, true, {name:"newButton_click"});
		desabilitaTodosCampos(false);
		setFocusOnFirstFocusableField();
	}
	
	/** 
	  * Call back do form
	  */ 
	function myOnDataLoad_cb(data, erro, o){
		onDataLoad_cb(data);
		
		// Caso não seja masterLink (não veio da tela de tipoTributacaoIcms)
		//if(getElement("tipoTributacaoIcms.idTipoTributacaoIcms").masterLink != "true" && data.tipoTributacaoIcms != undefined){
			//setElementValue("idTipoTributacaoIcms", data.tipoTributacaoIcms.idTipoTributacaoIcms);
			//populateComboTipoTributacaoIcms("tipoTributacaoCallBack");
		//}
		
	}
	
	/** Call back do find da combo de tipo de tributação */
	function tipoTributacaoCallBack_cb(data, error){
		
		tipoTributacaoIcms_idTipoTributacaoIcms_cb(data);
		
		var idTipoTributacaoIcms = getElementValue("idTipoTributacaoIcms");
		if (idTipoTributacaoIcms != ""){
			setElementValue("tipoTributacaoIcms.idTipoTributacaoIcms", idTipoTributacaoIcms);
		}
		
	}
	
	/**
	  * Call back da página
	  */
	function myPageLoadCallBack_cb(data, erro){
		
		onPageLoad_cb(data, erro);
		
		// Popula a combo de tipoTributacaoIcms
		populateComboTipoTributacaoIcms("tipoTributacaoIcms.idTipoTributacaoIcms");
		
	}
	
	/**
	  * Xmit para popular a combo de tipoTributacaoIcms
	  */
	function populateComboTipoTributacaoIcms(callBack){
	
		var dados = new Array();
        setNestedBeanPropertyValue(dados, "tipoTributacaoIcms.idTipoTributacaoIcms", getElementValue("idTipoTributacaoIcms"));
		
		_serviceDataObjects = new Array();
        addServiceDataObject(createServiceDataObject("lms.tributos.manterObservacoesICMSAction.findTipoTributacao",
                                             callBack, dados));
        xmit(false);
        	
	}
</script>