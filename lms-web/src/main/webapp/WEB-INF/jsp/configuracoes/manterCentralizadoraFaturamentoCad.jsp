<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.centralizadoraFaturamentoService" onPageLoadCallBack="myOnPageLoadCallBack"> 
	<adsm:form action="/configuracoes/manterCentralizadoraFaturamento" idProperty="idCentralizadoraFaturamento">	
	
		<adsm:hidden property="dataAtual" serializable="false"/>

	 	<adsm:lookup label="filialCentralizadora" 
				 	 labelWidth="15%"
				 	 width="8%"
	 				 property="filialByIdFilialCentralizadora" 
	 				 idProperty="idFilial" 
	 				 criteriaProperty="sgFilial"
	 				 service="lms.configuracoes.manterCentralizadoraFaturamentoAction.findLookupBySgFilialVigenteEm" 
	 				 dataType="text" 
	 				 size="3" 
	 				 maxLength="3" 
	 				 action="/municipios/manterFiliais" 
	 				 required="true" 
	 				 onPopupSetValue="filialOrigemPopUpSetValue">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialByIdFilialCentralizadora.pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>
			<adsm:textbox dataType="text" size="30" maxLength="30" property="filialByIdFilialCentralizadora.pessoa.nmPessoa" disabled="true" width="27%" required="true"/>
		</adsm:lookup>
			
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" required="true"/>
		
		<adsm:lookup label="filialCentralizada" 
				 	 labelWidth="15%"
				 	 width="8%"
	 				 property="filialByIdFilialCentralizada" 
	 				 idProperty="idFilial" 
	 				 criteriaProperty="sgFilial"
	 				 service="lms.configuracoes.manterCentralizadoraFaturamentoAction.findLookupBySgFilialVigenteEm" 
	 				 dataType="text" 
	 				 size="3" 
	 				 maxLength="3" 
	 				 action="/municipios/manterFiliais" 
	 				 required="true" 
	 				 onPopupSetValue="filialDestinoPopUpSetValue">
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" formProperty="filialByIdFilialCentralizada.pessoa.nmPessoa"/>
			<adsm:propertyMapping criteriaProperty="dataAtual" modelProperty="historicoFiliais.vigenteEm"/>
			<adsm:textbox dataType="text" size="30" maxLength="30" property="filialByIdFilialCentralizada.pessoa.nmPessoa" disabled="true" width="27%" required="true"/>
		</adsm:lookup>
		
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" required="true"/>			
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
<script>
	/**
	 * OnPopUpSetValue lookup de filialOrigem
	 */
	function filialOrigemPopUpSetValue(data){
		setElementValue("filialByIdFilialCentralizadora.idFilial", data.idFilial);
		setElementValue("filialByIdFilialCentralizadora.sgFilial", data.sgFilial);
		setElementValue("filialByIdFilialCentralizadora.pessoa.nmPessoa", data.pessoa.nmFantasia);
	}
	
	/**
	  * OnPopUpSetValue lookup de filialDestino
	  */
	function filialDestinoPopUpSetValue(data){
		setElementValue("filialByIdFilialCentralizada.idFilial", data.idFilial);
		setElementValue("filialByIdFilialCentralizada.sgFilial", data.sgFilial);
		setElementValue("filialByIdFilialCentralizada.pessoa.nmPessoa", data.pessoa.nmFantasia);		
	}
	
	/**
	  * PageLoadCallBack
	  */
	function myOnPageLoadCallBack_cb(data, error){
		onPageLoad_cb(data,error);
		
		var dados = new Array();
        var sdo = createServiceDataObject("lms.configuracoes.manterCentralizadoraFaturamentoAction.findDataAtual",
                                          "setaDataAtual",
                                          dados);
        xmit({serviceDataObjects:[sdo]});
	}
	
	function setaDataAtual_cb(data,error){
		setElementValue("dataAtual",data._value);
		document.getElementById("dataAtual").masterLink = 'true';
	}
</script>