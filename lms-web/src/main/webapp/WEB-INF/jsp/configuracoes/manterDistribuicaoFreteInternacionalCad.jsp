<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction" onPageLoadCallBack="myOnPageLoadCallBack">

	<adsm:form action="/configuracoes/manterDistribuicaoFreteInternacional" idProperty="idDistrFreteInternacional" 
			   newService="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.newMaster">

		<adsm:hidden property="dataAtual" serializable="false"/>

	 	<adsm:lookup label="filialOrigem" 
	 				 property="filialOrigem" 
	 				 idProperty="idFilial" 
	 				 criteriaProperty="sgFilial"
	 				 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupBySgFilialVigenteEm" 
	 				 dataType="text" 
	 				 size="3" 
	 				 maxLength="3" 
	 				 action="/municipios/manterFiliais" 
	 				 required="true" 
	 				 onPopupSetValue="filialOrigemPopUpSetValue"
	 				 onDataLoadCallBack="filialOrigem">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialOrigem.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="siglaNomeFilial" formProperty="filialOrigem.siglaNomeFilial"/>
			<adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>
			<adsm:textbox dataType="text" property="filialOrigem.pessoa.nmFantasia" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:hidden property="filialOrigem.siglaNomeFilial" serializable="false"/>

		<adsm:textbox label="percentualFreteOrigem" property="pcFreteOrigem" dataType="percent" 
			size="6" maxLength="6" labelWidth="25%" width="25%" required="true" minValue="1" maxValue="100"/>

	 	<adsm:lookup label="filialDestino" 
	 				 property="filialDestino" 
	 				 idProperty="idFilial" 
	 				 criteriaProperty="sgFilial"
	 				 service="lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findLookupBySgFilialVigenteEm" 
	 				 dataType="text" 
	 				 size="3" 
	 				 maxLength="3" 
	 				 action="/municipios/manterFiliais" 
	 				 required="true" 
	 				 onPopupSetValue="filialDestinoPopUpSetValue"
	 				 onDataLoadCallBack="filialDestino">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialDestino.pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="siglaNomeFilial" formProperty="filialDestino.siglaNomeFilial"/>
			<adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" disabled="true" serializable="false"/>
		</adsm:lookup>
		
		<adsm:hidden property="filialDestino.siglaNomeFilial" serializable="false"/>

		<adsm:textbox label="percentualFreteDestino" property="pcFreteDestino" dataType="percent" 
			size="6" maxLength="6" labelWidth="25%" width="25%" required="true" minValue="1" maxValue="100"/>

		<adsm:textbox label="permisso" property="cdPermisso" dataType="integer" size="8" maxLength="4" required="true" minValue="1"/>

		<adsm:textbox label="percentualFreteExterno" property="pcFreteExterno" dataType="percent" size="6" 
			maxLength="6" labelWidth="25%" width="25%" required="true"/>

		<adsm:textbox label="tempo" property="nrTempoViagem" dataType="currency" size="10" maxLength="8" mask="##0.00"/>

		<adsm:textbox label="distancia" property="distanciaKm" dataType="integer" labelWidth="25%" width="25%" 
			size="6" maxLength="6" required="true" unit="km2"/>

		<adsm:buttonBar>
			<adsm:storeButton id="storeButton"/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	
	</adsm:form>
</adsm:window>
<script>

	/**
	  * CallBack  da lookup de filialOrigem
	  */
	function filialOrigem_cb(data, erro){
		if(filialOrigem_sgFilial_exactMatch_cb(data)){
			validaFiliais("origem");
		}
	}
	
	/**
	  * CallBack  da lookup de filialDestino
	  */
	function filialDestino_cb(data, erro){
		if (filialDestino_sgFilial_exactMatch_cb(data)){
			validaFiliais("destino");
		}
	}
	
	/**
	  * Valida a filialOrigem e a filialDestino, pois as mesmas não podem ser iguais
	  */
	function validaFiliais(tpFilial){
		if (getElementValue("filialOrigem.idFilial") == getElementValue("filialDestino.idFilial")){
				alert(parent.i18NLabel.getLabel("LMS-00044"));
				
				if(tpFilial == "origem"){
					resetValue(document.getElementById("filialOrigem.idFilial"));
					setFocus(document.getElementById("filialOrigem.sgFilial"));
				}else{
					resetValue(document.getElementById("filialDestino.idFilial"));
					setFocus(document.getElementById("filialDestino.sgFilial"));
				}
				return false;
		} else {
			return true;
		}
	}
	
	/**
	  * PageLoadCallBack
	  */
	function myOnPageLoadCallBack_cb(data, error){
		onPageLoad_cb(data,error);
		
		var dados = new Array();
        var sdo = createServiceDataObject("lms.configuracoes.manterDistribuicaoFreteInternacionalAction.findDataAtual",
                                          "setaDataAtual",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	}
	
	function setaDataAtual_cb(data,error){
		setElementValue("dataAtual",data._value);
		document.getElementById("dataAtual").masterLink = 'true';
	}
	
	/**
	  * OnPopUpSetValue lookup de filialOrigem
	  */
	function filialOrigemPopUpSetValue(data){
		
		setElementValue("filialOrigem.idFilial", data.idFilial);
		setElementValue("filialOrigem.sgFilial", data.sgFilial);
		setElementValue("filialOrigem.pessoa.nmFantasia", data.pessoa.nmFantasia);
		setElementValue("filialOrigem.siglaNomeFilial", data.sgFilial + " - " + data.pessoa.nmFantasia);	
	
		return validaFiliais("origem");
		
	}
	
	/**
	  * OnPopUpSetValue lookup de filialDestino
	  */
	function filialDestinoPopUpSetValue(data){
		
		setElementValue("filialDestino.idFilial", data.idFilial);
		setElementValue("filialDestino.sgFilial", data.sgFilial);
		setElementValue("filialDestino.pessoa.nmFantasia", data.pessoa.nmFantasia);
		setElementValue("filialDestino.siglaNomeFilial", data.sgFilial + " - " + data.pessoa.nmFantasia);	
	
		return validaFiliais("destino");
		
	}
	
</script>