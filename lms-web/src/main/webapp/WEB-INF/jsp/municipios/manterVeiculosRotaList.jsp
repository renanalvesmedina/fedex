<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
	
	function changeMeioTransporte() {
		var r = true;
		if(getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador") == "")   {
			resetMeioTransporte();
		} else {
			r = meioTransporteRodoviario_meioTransporte_nrIdentificadorOnChangeHandler();
		}	
		return r;
	}
	
	function changeMeioTransporte2() {
		var r = true;
		if(getElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota") == "")   {
			resetMeioTransporte();
		} else {
			r = meioTransporteRodoviario2_meioTransporte_nrFrotaOnChangeHandler();
		}	
		return r;
	}
	
	function resetMeioTransporte(){
		resetValue("meioTransporteRodoviario.meioTransporte.nrIdentificador");
		resetValue("meioTransporteRodoviario.idMeioTransporte");
		resetValue("meioTransporteRodoviario2.idMeioTransporte");
		resetValue("meioTransporteRodoviario2.meioTransporte.nrFrota");
		resetValue("proprietario.nmPessoa");
	}
	
		/**
	* Função chamada pela PopupSetValue da Lookup de Meios de Transporte. Carrega o proprietário
	*/
	function meioTranspPropPopup(data) {
	data = (data[0] != undefined) ? data[0] : data;
	if (data!=undefined){
		  var sdo = createServiceDataObject("lms.municipios.manterVeiculosRotaAction.findProprietarioByMeioTransporte", "propComp", {idMeioTransporteRodoviario:data.idMeioTransporte});
          xmit({serviceDataObjects:[sdo]});
		  return true;
		}
	
	}
	
	/**
	* Função CallBack da Lookup de Meios de Transporte. Carrega o proprietário
	*/
	function meioTranspProp_cb(data) {
		meioTransporteRodoviario_meioTransporte_nrIdentificador_exactMatch_cb(data);
		meioTranspPropPopup(data);
	}

	
	function propComp_cb(data) {
		if (data!=undefined){
	         setElementValue("proprietario.nmPessoa",data.nmPessoa);
	        
		}
		else {
			 setElementValue("proprietario.nmPessoa","");
			
			 }
	}
					
</script>
<adsm:window title="consultarVeiculosRota" service="lms.municipios.manterVeiculosRotaAction">
	   <adsm:form action="/municipios/manterVeiculosRota" idProperty="idRotaMeioTransporteRodov">
       
       <adsm:hidden property="rotaTipoMeioTransporte.idRotaTipoMeioTransporte" />
       
       <adsm:complement label="filial" labelWidth="20%" width="80%">
	       <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.filial.sgFilial" size="5"   serializable="false" disabled="true"/>
	   	   <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
	   </adsm:complement>

       <adsm:complement label="numeroRota" labelWidth="20%" width="30%">
       		    <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.nrRota" serializable="false" size="5" disabled="true" />
       		    <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.dsRota" serializable="false" size="30" disabled="true" />
       </adsm:complement>
       
       <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" serializable="false" size="20" disabled="true" width="31%" labelWidth="19%" label="tipoMeioTransporte"/>
                  
       <adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" maxLength="6"
				service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte"  labelWidth="20%" width="8%" size="8" serializable="false"
				onDataLoadCallBack="meioTranspProp" onchange="return changeMeioTransporte2();" onPopupSetValue="meioTranspPropPopup" >

			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
 				  	modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
		
		
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" maxLength="25"
					service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="true"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					onDataLoadCallBack="meioTranspProp" onchange="return changeMeioTransporte();" onPopupSetValue="meioTranspPropPopup" width="22%" size="20" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
 						  modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
				
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
			</adsm:lookup>
			
			<adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		</adsm:lookup>
		
		
	    
	    <adsm:textbox dataType="text" property="proprietario.nmPessoa" size="20" disabled="true" serializable="false" width="31%" labelWidth="19%" label="proprietario"/>
		<adsm:hidden property="idProprietario" />	     	    	    	 

        <adsm:range label="vigencia" labelWidth="20%" width="80%" required="false" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="rotaMeioTransporteRodov"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idRotaMeioTransporteRodov" property="rotaMeioTransporteRodov" selectionMode="check" gridHeight="220" unique="true" rows="11" >
		<adsm:gridColumnGroup separatorType="FILIAL">
			<adsm:gridColumn title="filial" property="sgFilial" width="100" />
			<adsm:gridColumn title="" property="pessoaFilial"  width="50"/>
		</adsm:gridColumnGroup>
		
		<adsm:gridColumn title="meioTransporte" property="nrFrota" width="65" />
		<adsm:gridColumn title="" property="nrIdentificador" width="75"/>
		
		<adsm:gridColumn title="proprietario" property="proprietario" width="180" />
		<adsm:gridColumn title="vigenciaInicial" property="dtVigenciaInicial" dataType="JTDate" width="100" />
		<adsm:gridColumn title="vigenciaFinal" property="dtVigenciaFinal" dataType="JTDate" width="100" />	
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
