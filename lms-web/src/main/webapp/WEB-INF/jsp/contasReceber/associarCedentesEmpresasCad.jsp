<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.associarCedentesEmpresasAction" onPageLoadCallBack="myPageLoadCallBack">
	
	<adsm:form action="/contasReceber/associarCedentesEmpresas" idProperty="idComplementoEmpresaCedente" onDataLoadCallBack="myOnDataLoad">
	
		<adsm:hidden property="idCedente" serializable="false" />
		
		<adsm:combobox 
			service="lms.contasreceber.vincularCedenteModalAbrangenciaAction.findActiveCedentes"
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			labelWidth="18%"
			width="82%"
			required="true"
			autoLoad="false"
			label="cedente"> 
		</adsm:combobox>
	
		<adsm:hidden property="tpSituacao" value="A"/>
		<adsm:lookup label="empresa" 
					 labelWidth="18%" 
					 dataType="text" 
					 size="20" 
					 maxLength="20" 
					 width="82%"
				     service="lms.contasreceber.associarCedentesEmpresasAction.findLookupEmpresa" 
				     property="empresa" 
				     idProperty="idEmpresa"
					 criteriaProperty="pessoa.nrIdentificacao" 
					 relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
					 action="/municipios/manterEmpresas" 
					 required="true" 
					 onDataLoadCallBack="customizado">
					
					<adsm:propertyMapping modelProperty="pessoa.nmPessoa" relatedProperty="empresa.pessoa.nmPessoa"/>
					<adsm:propertyMapping criteriaProperty="tpSituacao" modelProperty="tpSituacao"/>
					<adsm:textbox dataType="text" property="empresa.pessoa.nmPessoa" size="50" disabled="true" serializable="false" />
		</adsm:lookup>
		
		<adsm:range label="intervaloBoletos" labelWidth="18%" width="82%" required="true">
			<adsm:textbox dataType="integer" property="nrIntervaloInicialBoleto" size="15" maxLength="13"/>
			<adsm:textbox dataType="integer" property="nrIntervaloFinalBoleto" size="15" maxLength="13"/> 
		</adsm:range>
		
		<adsm:textbox dataType="integer" required="true" property="nrUltimoBoleto" label="ultimoBoleto" size="15" maxLength="13" labelWidth="18%" width="82%"/>
		
		<adsm:buttonBar>
			<adsm:button caption="salvar" buttonType="storeButton" onclick="beforeStore(this.form)" />
			<adsm:button caption="limpar" buttonType="newButton" onclick="carregaComboAposLimpar(this.document);"/>
			<adsm:removeButton/>
		</adsm:buttonBar>
		
		<script>
			var LMS_36001 = "<adsm:label key="LMS-36001"/>";
		</script>
		
	</adsm:form>
	
</adsm:window>

<script>


	function beforeStore(formulario){
		/** Valida se o ultimo boleto impresso é maior ou igual ao intervalo boleto inicial e menor que o intervalo boleto final, para realizar o store */
		var intervalo = Number(getElementValue("nrIntervaloInicialBoleto") - 1);
		if((Number(getElementValue("nrUltimoBoleto")) >= intervalo) &&
				 (Number(getElementValue("nrUltimoBoleto")) <= Number(getElementValue("nrIntervaloFinalBoleto")))){
				 
			storeButtonScript('lms.contasreceber.associarCedentesEmpresasAction.store', 'store', formulario);
		}else{
			/** Valida a tab, para depois validar a regra de boletos */
			if(validateTabScript(formulario)){
				/** Valida os tres campos de boletos para emitir o alerta LMS_36001 */
				if(getElementValue("nrUltimoBoleto") != "" &&
						getElementValue("nrIntervaloInicialBoleto") != "" &&
							getElementValue("nrIntervaloFinalBoleto") != ""){
					alert(LMS_36001);
				}
			}
		}
		
	}

	function myPageLoadCallBack_cb(data,erro){
		
		onPageLoad_cb(data,erro);
        var dados = new Array();
        setNestedBeanPropertyValue(dados, "cedente.idCedente", getElementValue("idCedente"));
        var sdo = createServiceDataObject("lms.contasreceber.associarCedentesEmpresasAction.findActiveCedentes",
                                             "cedente_idCedente",
                                             dados);
         xmit({serviceDataObjects:[sdo]});
		
	}
	
	/** Função chamada no click da grid(callBack do form) popular a combo de cedentes a partir da tela de 
	 *  vinculo entre cedente e empresa
	 */ 
	function myOnDataLoad_cb(data, erro, o){
		
		if(data.cedente != undefined){
			setElementValue("idCedente", data.cedente.idCedente);
		}
		
		onDataLoad_cb(data);
		if(getElementValue("idCedente") != ""){
			
			var dados = new Array();
	        setNestedBeanPropertyValue(dados, "cedente.idCedente", getElementValue("idCedente"));
	        var sdo = createServiceDataObject("lms.contasreceber.associarCedentesEmpresasAction.findActiveCedentes",
	                                             "myCedenteCallBack",
	                                             dados);
	        xmit({serviceDataObjects:[sdo]});
        }
        
       
	}
	
	/** Função chamada fala enfileirar a função cedente_idCedente_cb após o xmit */
	function myCedenteCallBack_cb(data){
		cedente_idCedente_cb(data);
		var idCedente = getElementValue("idCedente");
		if (idCedente != ""){
			setElementValue("cedente.idCedente", idCedente);
		}
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
    
    function changeEmpresa() {
		return empresa_pessoa_nrIdentificacaoOnChangeHandler();
	}
	
	function customizado_cb(data) {
		empresa_pessoa_nrIdentificacao_exactMatch_cb(data);
	}
	
	

</script>