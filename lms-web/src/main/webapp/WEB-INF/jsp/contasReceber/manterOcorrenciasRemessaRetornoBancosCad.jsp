<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.manterOcorrenciasRemessaRetornoBancosAction">

	<adsm:form action="/contasReceber/manterOcorrenciasRemessaRetornoBancos" 
		idProperty="idOcorrenciaBanco" 
		onDataLoadCallBack="myOnDataLoad">
	
		<adsm:hidden property="banco.tpSituacao" value="A"/>
     	<adsm:lookup service="lms.configuracoes.bancoService.findLookup" 
					 idProperty="idBanco"
					 dataType="integer" 
					 property="banco" 					 
					 criteriaProperty="nrBanco"					 
					 label="banco" 
					 size="5" 
					 maxLength="3" 
					 width="30%" required="true"
					 action="/configuracoes/manterBancos"
					 exactMatch="false" minLengthForAutoPopUpSearch="1">
			<adsm:propertyMapping modelProperty="nmBanco" relatedProperty="banco.nmBanco"/>				
			<adsm:propertyMapping modelProperty="tpSituacao" criteriaProperty="banco.tpSituacao"/>						
			<adsm:textbox dataType="text" property="banco.nmBanco" disabled="true" />
		</adsm:lookup>		

		<adsm:combobox 
			label="tipo" 
			property="tpOcorrenciaBanco" 
			domain="DM_TIPO_OCOR_BANCO"
			width="85%" 
			labelWidth="15%"
			required="true"/>

		<adsm:textbox 
			dataType="integer" 
			property="nrOcorrenciaBanco" 
			labelWidth="15%" 
			width="85%" 
			label="numero" 
			size="3" 
			maxLength="3"
			required="true"/>

		<adsm:textbox 
			dataType="text" 
			property="dsOcorrenciaBanco" 
			label="descricao" 
			size="60" 
			maxLength="60" 
			width="85%" 
			labelWidth="15%"
			required="true"/>		
			
		<adsm:buttonBar>
			<adsm:button caption="motivoOcorrencia" action="/contasReceber/manterMotivosOcorrenciasRemessaRetornoBancos" 
				cmd="main" id="motivoOcorrenciaButton">
				<adsm:linkProperty src="idOcorrenciaBanco" target="ocorrenciaBanco.idOcorrenciaBanco"/>
				<adsm:linkProperty src="nrOcorrenciaBanco" target="ocorrenciaBanco.nrOcorrenciaBanco"/>
				<adsm:linkProperty src="dsOcorrenciaBanco" target="ocorrenciaBanco.dsOcorrenciaBanco"/>
			</adsm:button>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function validateTipo(){
		var tipo = getElementValue("tpOcorrenciaBanco");
		if (tipo == "REM") {
				setDisabled("motivoOcorrenciaButton", true);
	    } else {
				setDisabled("motivoOcorrenciaButton", false);    	
	   	}
    
    }

	function myOnDataLoad_cb(data, errors){
		if (errors != undefined){
			alert(''+errors);
			return false;
		}
		onDataLoad_cb(data, errors);
		validateTipo();
	}
	
	function initWindow(eventObj){		
		if(eventObj.name == "storeButton"){
			validateTipo();
		}		
	}
	
	
</script>