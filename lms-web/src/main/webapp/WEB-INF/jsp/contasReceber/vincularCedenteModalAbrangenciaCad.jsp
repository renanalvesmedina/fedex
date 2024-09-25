<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.contasreceber.vincularCedenteModalAbrangenciaAction" onPageLoadCallBack="myPageLoadCallBack">
	
	<adsm:form action="/contasReceber/vincularCedenteModalAbrangencia" idProperty="idCedenteModalAbrangencia" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:hidden property="idCedente" serializable="false" />
	
		<adsm:combobox 
			service="lms.contasreceber.vincularCedenteModalAbrangenciaAction.findActiveCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			required="true"
			autoLoad="false"
			boxWidth="200"
			label="cedente"> 
		</adsm:combobox>
		
		<adsm:combobox label="modal" property="tpModal" domain="DM_MODAL" width="35%" required="true" labelWidth="15%"/>

		<adsm:combobox label="abrangencia" property="tpAbrangencia" domain="DM_ABRANGENCIA" width="35%" required="true" labelWidth="15%"/>
		
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:button caption="limpar" buttonType="newButton" onclick="carregaComboAposLimpar(this.document);"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>

	function myPageLoadCallBack_cb(data,erro){
		
		onPageLoad_cb(data,erro);
        var dados = new Array();
        setNestedBeanPropertyValue(dados, "cedente.idCedente", getElementValue("idCedente"));
        var sdo = createServiceDataObject("lms.contasreceber.vincularCedenteModalAbrangenciaAction.findActiveCedentes",
                                             "cedente_idCedente",
                                             dados);
         xmit({serviceDataObjects:[sdo]});
		
	}
	
	/** Função chamada no click da grid(callBack do form) popular a combo de cedentes a partir da tela de 
	 *  vinculo entre cedente e filial
	 */ 
	function myOnDataLoad_cb(data, erro, o){
		setElementValue("idCedente", data.cedente.idCedente);
		onDataLoad_cb(data);
		if(getElementValue("idCedente") != ""){
			
			var dados = new Array();
	        setNestedBeanPropertyValue(dados, "cedente.idCedente", getElementValue("idCedente"));
	        var sdo = createServiceDataObject("lms.contasreceber.vincularCedenteModalAbrangenciaAction.findActiveCedentes",
	                                             "myCedenteCallBack",
	                                             dados);
	        xmit({serviceDataObjects:[sdo]});
        }
        
       
	}
	
	/** Função chamada fala enfileirar a função cedente_idCedente_cb após o xmit */
	function myCedenteCallBack_cb(data){
		var retorno = cedente_idCedente_cb(data);
		var idCedente = getElementValue("idCedente");
		if (idCedente != ""){
			setElementValue("cedente.idCedente", idCedente);
		}
		return retorno;
	}
	
	/**
	  * Função chamada a cada vez que a tela em questão é iniciada, para setar o cedente como masterLink(caso esteja vindo do cadastro de cedente)
	  */  
    function initWindow(eventObj){  
    	if (eventObj.name == "tab_click"){
			var idCedente = getElementValue("idCedente");
			if (idCedente != ""){
				setElementValue("cedente.idCedente", idCedente);
				setDisabled("cedente.idCedente", true);
				document.getElementById("cedente.idCedente").masterLink = "true";
			}else{
				var dados = new Array();
		        setNestedBeanPropertyValue(dados, "cedente.idCedente", "");
		        var sdo = createServiceDataObject("lms.contasreceber.vincularCedenteModalAbrangenciaAction.findActiveCedentes",
		                                             "cedente_idCedente",
		                                             dados);
		        xmit({serviceDataObjects:[sdo]});
			
			}
		}
    }
    
    /** 
      *	Função responsável por carregar somente os cedentes ativos(caso o cedente em questão não estiver setado como masterLink) após o 
      * botão limpar ser clicado 
      */
    function carregaComboAposLimpar(doc){
    	newButtonScript(doc, true, {name:'newButton_click'});
    	
    	if(document.getElementById("cedente.idCedente").masterLink != "true"){
    		document.getElementById("cedente.idCedente").length = 1;
    		var dados = new Array();
	        setNestedBeanPropertyValue(dados, "cedente.idCedente", "");
	        var sdo = createServiceDataObject("lms.contasreceber.vincularCedenteModalAbrangenciaAction.findActiveCedentes",
	                                             "cedente_idCedente",
	                                             dados);
	         xmit({serviceDataObjects:[sdo]});
	    }
    }
	
</script>
