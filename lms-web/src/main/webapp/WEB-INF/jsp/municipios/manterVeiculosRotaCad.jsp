<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window title="consultarVeiculosRota" service="lms.municipios.manterVeiculosRotaAction">
	<adsm:form action="/municipios/manterVeiculosRota" service="lms.municipios.manterVeiculosRotaAction.findByIdDetalhamento" idProperty="idRotaMeioTransporteRodov" onDataLoadCallBack="vigenciaLoad">       
       <adsm:hidden property="rotaTipoMeioTransporte.idRotaTipoMeioTransporte" />
       
       <adsm:complement label="filial" labelWidth="20%" width="80%">
	       <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.filial.sgFilial" size="5"   serializable="false" disabled="true"/>
	   	   <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.filial.pessoa.nmFantasia" size="30" disabled="true" serializable="false" />
	   </adsm:complement>
       
       <adsm:complement label="numeroRota" labelWidth="20%" width="30%">
   		    <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.nrRota" size="5" disabled="true" />
   		    <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.rotaColetaEntrega.dsRota" size="30" disabled="true" />
       </adsm:complement>
       
       <adsm:textbox dataType="text" property="rotaTipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte" size="20" disabled="true" width="31%" labelWidth="19%" label="tipoMeioTransporte"/>
       <adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
            
       <adsm:lookup dataType="text" property="meioTransporteRodoviario2" idProperty="idMeioTransporte" maxLength="6"
				service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrFrota"
				label="meioTransporte" labelWidth="20%" width="30%" size="8" serializable="false" required="true"
				onDataLoadCallBack="meioTranspProp" onchange="return changeMeioTransporte2();" onPopupSetValue="meioTranspPropPopup" >
				
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
                    modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			<adsm:propertyMapping criteriaProperty="tpSituacaoMeioTransporte"
					modelProperty="meioTransporte.tpSituacao" />
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte"
					modelProperty="idMeioTransporte" />		
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador"
					modelProperty="meioTransporte.nrIdentificador" />				
		
			<adsm:lookup dataType="text" property="meioTransporteRodoviario" idProperty="idMeioTransporte" maxLength="25"
					service="lms.municipios.manterVeiculosRotaAction.findLookupMeioTransporteRodov" picker="true"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="rodo" criteriaProperty="meioTransporte.nrIdentificador"
					onDataLoadCallBack="meioTranspProp" onchange="return changeMeioTransporte();" onPopupSetValue="meioTranspPropPopup" size="20" >
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" 
 						  modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
				<adsm:propertyMapping criteriaProperty="tpSituacaoMeioTransporte"
						modelProperty="meioTransporte.tpSituacao" />
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.idMeioTransporte"
						modelProperty="idMeioTransporte" />	
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrFrota"
						modelProperty="meioTransporte.nrFrota" />		
			</adsm:lookup>
			
	    </adsm:lookup>
	    
   		<adsm:hidden property="tpSituacaoMeioTransporte" serializable="false" value="A" />
	    <adsm:textbox dataType="text" property="proprietario.nmPessoa" size="20" disabled="true" width="31%" labelWidth="19%" label="proprietario"/>
	     	    	    	 
        <adsm:range label="vigencia" labelWidth="20%" width="80%" required="false" >
             <adsm:textbox dataType="JTDate" property="dtVigenciaInicial" picker="true" required="true" />
             <adsm:textbox dataType="JTDate" property="dtVigenciaFinal" picker="true"/>
        </adsm:range>

	<adsm:buttonBar>
			<adsm:storeButton id="salvar" callbackProperty="afterStore" service="lms.municipios.manterVeiculosRotaAction.storeMap" />
			<adsm:newButton id="novo" />
			<adsm:removeButton id="excluir" />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>   
<script>

	function changeMeioTransporte() {
		var r = meioTransporteRodoviario_meioTransporte_nrIdentificadorOnChangeHandler();
		
		if(getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador") == "")   {
			resetMeioTransporte();
		} 
		
		return r;
	}
	
	function changeMeioTransporte2() {
		var r = meioTransporteRodoviario2_meioTransporte_nrFrotaOnChangeHandler();
		if(getElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota") == "")   {
			resetMeioTransporte();
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
	* Função PopupSetValue da Lookup de Meios de Transporte. Carrega o proprietário
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
	
	/**
	* CallBack do método chamado na função anterior, seta o nome do proprietário no campo
	*/
	function propComp_cb(data) {
		if (data!=undefined){
			  setElementValue("proprietario.nmPessoa",data.nmPessoa);
		}
		else
			 setElementValue("proprietario.nmPessoa","");
	}
	
	function limpaProprietario() {
		if (getElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota")==undefined){
			  setElementValue("proprietario.nmPessoa","");
		}
		else{
			var r = meioTransporteRodoviario_nrFrotaOnChangeHandler();
			return r;
		}
		if (getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador")==undefined){
			  setElementValue("proprietario.nmPessoa","");
		}
		else{
			var x = meioTransporteRodoviario2_nrIdentificadorOnChangeHandler();
			return x;
		}
		
	}
	
	
	
	/**
	* Retorna estado dos campos como foram carregados na página.
	*/
	function estadoNovo() {
		setDisabled(document,false);
		setDisabled("rotaTipoMeioTransporte.rotaColetaEntrega.nrRota",true);
		setDisabled("rotaTipoMeioTransporte.rotaColetaEntrega.dsRota",true);
		setDisabled("rotaTipoMeioTransporte.tipoMeioTransporte.dsTipoMeioTransporte",true);
		setDisabled("proprietario.nmPessoa",true);
		setDisabled("excluir",true);
	}
	
	
	/**
    * Habilitar campos se o registro estiver vigente.
	*/
	function habilitarCampos() {
		setDisabled("dtVigenciaFinal",false);
	}
			
			
	/**
	 * Ao carregar os dados, é tratado o retorno da validação de vigência no detalhamento:
	 */
	function vigenciaLoad_cb(data,exception,errorCode, eventObj) {
		
				onDataLoad_cb(data,exception,errorCode, eventObj);
				
				data = (data[0] != undefined) ? data[0] : data;
				if (data != undefined) {
					var idFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.idMeioTransporte");
					var nrFrota = getNestedBeanPropertyValue(data,"meioTransporteRodoviario.meioTransporte.nrFrota");
					
					// É necessário preencher via js a segunda lookup de Meio de Transporte.				
					if (idFrota != undefined) {
						setElementValue("meioTransporteRodoviario2.idMeioTransporte",idFrota);
						setElementValue("meioTransporteRodoviario2.meioTransporte.nrFrota",nrFrota);
												
						var sdo = createServiceDataObject("lms.municipios.manterVeiculosRotaAction.findProprietarioByMeioTransporte", "propComp", {idMeioTransporteRodoviario:idFrota});
		          		xmit({serviceDataObjects:[sdo]});
						
					}
				}
				var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
				validaAcaoVigencia(acaoVigenciaAtual, null);
				
				
			}
			
			function validaAcaoVigencia(acaoVigenciaAtual, tipoEvento){
				if (acaoVigenciaAtual == 0) {
					estadoNovo();
					setDisabled("excluir",false);
					if(tipoEvento == "" ||  tipoEvento == null)
     					setFocusOnFirstFocusableField(document);
				    else
				        setFocusOnNewButton(document);
				} else if (acaoVigenciaAtual == 1) {
					setDisabled(document,true);
					setDisabled("novo",false);
					setDisabled("salvar",false);
					habilitarCampos();
					setDisabled("excluir",true);
					if(tipoEvento == "" ||  tipoEvento == null)
     					setFocusOnFirstFocusableField(document);
				    else
				        setFocusOnNewButton(document);
				} else if (acaoVigenciaAtual == 2) {
					setDisabled(document,true);
					setDisabled("novo",false);
					setDisabled("excluir",true);
					setFocusOnNewButton(document);
				
				}
			}
			
			
			function afterStore_cb(data,exception,key) {
		    	store_cb(data,exception,key);
				if (exception == undefined) {
					var acaoVigenciaAtual = getNestedBeanPropertyValue(data, "acaoVigenciaAtual");
					var store= "true";
					validaAcaoVigencia(acaoVigenciaAtual, store);	
				}
		    }
		
			function initWindow(eventObj) {
				if (eventObj.name != "gridRow_click" && eventObj.name != "storeButton"){
					estadoNovo();
					setFocusOnFirstFocusableField();
				}
				
			}
					
</script>