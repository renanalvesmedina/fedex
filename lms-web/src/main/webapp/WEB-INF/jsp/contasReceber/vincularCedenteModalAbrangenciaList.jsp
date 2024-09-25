<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.vincularCedenteModalAbrangenciaAction" onPageLoadCallBack="myPageLoadCallBack">
	
	<adsm:form action="/contasReceber/vincularCedenteModalAbrangencia">
	
		<adsm:hidden property="idCedente" serializable="false" />
		
		<adsm:combobox 
			service="lms.contasreceber.vincularCedenteModalAbrangenciaAction.findCedentes" 
			optionLabelProperty="comboText" 
			optionProperty="idCedente" 
			property="cedente.idCedente" 
			autoLoad="false"
			boxWidth="200"
			label="cedente"> 
		</adsm:combobox>
		
		<adsm:combobox label="modal" property="tpModal" domain="DM_MODAL" width="35%" labelWidth="15%"/>

		<adsm:combobox label="abrangencia" property="tpAbrangencia" domain="DM_ABRANGENCIA" width="35%" labelWidth="15%"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:button 
				disabled="false"
				buttonType="findButton" 
				caption="consultar" 
				onclick="validateCedenteModalAbrangencia(this.form)"/>
				
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
	
	<adsm:grid 
		selectionMode="check" 
		idProperty="idCedenteModalAbrangencia" 
		property="cedenteModalAbrangencia" 
		service="lms.contasreceber.vincularCedenteModalAbrangenciaAction.findPaginatedByCedenteModalAbrangencia"
		rowCountService="lms.contasreceber.vincularCedenteModalAbrangenciaAction.getRowCountByCedenteModalAbrangencia"
		gridHeight="200" 
		unique="true" 
		rows="13">
		
		<adsm:gridColumn 
			width="45%" 
			title="cedente" 
			property="dsCedente"/>
			
		<adsm:gridColumn 
			width="15%" 
			title="conta" 
			property="nrContaCorrente" 
			align="right"/>
			
		<adsm:gridColumn 
			width="15%" 
			title="modal" 
			property="tpModal" 
			isDomain="true"/>
			
		<adsm:gridColumn 
			width="25%" 
			title="abrangencia" 
			property="tpAbrangencia" 
			isDomain="true"/>
		
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	
	</adsm:grid>

</adsm:window>

<script>

	function validateCedenteModalAbrangencia(form){
		findButtonScript('cedenteModalAbrangencia', form);
	}
	
	function myPageLoadCallBack_cb(data,erro){
		
		onPageLoad_cb(data,erro);
        var dados = new Array();
        setNestedBeanPropertyValue(dados, "cedente.idCedente", getElementValue("idCedente"));
        var sdo = createServiceDataObject("lms.contasreceber.vincularCedenteModalAbrangenciaAction.findCedentes",
                                             "cedente",
                                             dados);
         xmit({serviceDataObjects:[sdo]});
		
	}
	
	function cedente_cb(data){
		comboboxLoadOptions({e:document.getElementById("cedente.idCedente"), data:data});
		var idCedente = getElementValue("idCedente");
		if (idCedente != ""){
			setElementValue("cedente.idCedente", idCedente);
			setDisabled("cedente.idCedente", true);
			document.getElementById("cedente.idCedente").masterLink = "true";
		}
	}

</script>