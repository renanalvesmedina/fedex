<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.manterGruposFrotasAction" >
	<adsm:form  action="/vol/manterGruposFrotas" idProperty="idGruVeic" service="lms.vol.manterGruposFrotasAction.findByIdMeioTransporte">
	    
	    <adsm:masterLink showSaveAll="true" idProperty="idGrupoFrota" >
			<adsm:masterLinkItem property="dsNome"	label="grupo" />
		</adsm:masterLink>
	
		<adsm:hidden property="filial.idFilial" />
		<adsm:hidden property="filial.sgFilial" />
		<adsm:hidden property="filial.pessoa.nmFantasia" />
		
		<adsm:lookup label="meioTransporte" labelWidth="15%" width="85%" picker="false"
		             property="meioTransporte"
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrFrota"
		             action="/contratacaoVeiculos/manterMeiosTransporte"
		             service="lms.vol.manterGruposFrotasAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="8" 
		             maxLength="6"
		             exactMatch="true"
		             required="true"
		             >
		             
		        <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			    <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			    <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			
		    <adsm:propertyMapping relatedProperty="meioTransporte2.nrIdentificador"
                       modelProperty="nrIdentificador" />
        
             <!--  Criteria por nrIdentificador -->        
             <adsm:lookup 
		             property="meioTransporte2"
		             idProperty="idMeioTransporte"
		             criteriaProperty="nrIdentificador"
		             action="/contratacaoVeiculos/manterMeiosTransporte"
		             service="lms.vol.manterGruposFrotasAction.findLookupMeioTransporte" 
		             dataType="text"
		             size="30" 
		             maxLength="25"
		             exactMatch="false"
		             minLengthForAutoPopUpSearch="5"
		             >
		             
		         <adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial"/>
			     <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial"/>
			     <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia" modelProperty="filial.pessoa.nmFantasia" />
			    
		         <adsm:propertyMapping relatedProperty="meioTransporte.nrFrota"
                        modelProperty="nrFrota" />
                 <adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
                            modelProperty="idMeioTransporte" />      
                        
         	 </adsm:lookup>
        </adsm:lookup>
		<adsm:buttonBar freeLayout="true">
			<adsm:storeButton caption="salvarMeioTransporte" service="lms.vol.manterGruposFrotasAction.storeMeioTransporte" callbackProperty="storeMeioTransporte" />
			<adsm:newButton/>
		</adsm:buttonBar>
		
	</adsm:form>

	<adsm:grid property="frotasGrupos" idProperty="idGruVeic" selectionMode="check" 
			   rows="11" gridHeight="150" onRowClick="disableClick"
			   service="lms.vol.manterGruposFrotasAction.findPaginatedGruposFrotaMeiosTransporte"
			   rowCountService="lms.vol.manterGruposFrotasAction.getRowCountGruposFrotaMeiosTransporte"
			   unique="true"
			   autoSearch="false" showGotoBox="true" showPagging="true" detailFrameName="frotas">
		<adsm:gridColumn title="meioTransporte"  property="meioTransporte.nrFrota" width="100"  />
		<adsm:gridColumn title="" property="meioTransporte.nrIdentificador" width="200" align="left" />
		<adsm:gridColumn title="telefone" property="meioTransporte.volEquipamentos.dsNumero" dataType="text"/>
				
		<adsm:buttonBar> 
			<adsm:removeButton service="lms.vol.manterGruposFrotasAction.removeByIdsGruposFrotaMeiosTransporte"/>
		</adsm:buttonBar>
	</adsm:grid>
    <adsm:i18nLabels>
		<adsm:include key="LMS-41001"/>
	</adsm:i18nLabels>
</adsm:window>

<script>
	
	function disableClick() {
		return false;
	}
	
	function tabShowFrotas() {
	   var tabGroup = getTabGroup(this.document);
	   var tabDet = tabGroup.getTab("pesq");
	   var idFilial = tabDet.getFormProperty("filial.idFilial");
	   var sgFilial = tabDet.getFormProperty("filial.sgFilial");
	   var nmFantasia = tabDet.getFormProperty("filial.pessoa.nmFantasia");
		
	   setElementValue("filial.idFilial",idFilial);
	   setElementValue("filial.sgFilial",sgFilial);
	   setElementValue("filial.pessoa.nmFantasia",nmFantasia);
	   
	   document.getElementById("filial.idFilial").masterLink = "true";
	   document.getElementById("filial.sgFilial").masterLink = "true";
	   document.getElementById("filial.pessoa.nmFantasia").masterLink = "true";
	   
	}
	
	/* 
	 * CallBack para testar se deseja transferir meio de transporte para outra frota 
	*/
	function storeMeioTransporte_cb(data,error) {
	  storeItem_cb(data, error);
	  if (data.chave != undefined) {
	     if (confirm(i18NLabel.getLabel("LMS-41001"))) {
	         var sdo = createServiceDataObject("lms.vol.manterGruposFrotasAction.storeMeioTransporteTransferencia", "storeMeioTransporteTransferencia", data);
    	     xmit({serviceDataObjects:[sdo]});
	     }
	  }
	}
	
	function storeMeioTransporteTransferencia_cb(data,error) {
	   storeItem_cb(data, error);
	   if (error != undefined) {
	     alert(error);
	   }
	}
</script>