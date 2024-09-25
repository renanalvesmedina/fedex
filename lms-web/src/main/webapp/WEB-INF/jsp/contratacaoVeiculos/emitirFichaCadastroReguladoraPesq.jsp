<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
<!--
   //seta os dados na lookup de proprietario
//-->

	function dadosProprietario(data){
		if(getNestedBeanPropertyValue(data,"outrasInfo")!= null){
			var idProprietario = getNestedBeanPropertyValue(data,"outrasInfo.idProprietario");
			var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data,"outrasInfo.nrIdentificacaoFormatado");
			var nmPessoa = getNestedBeanPropertyValue(data,"outrasInfo.nmPessoa");
			setNestedBeanPropertyValue(data, "proprietario.idProprietario", idProprietario);
			setNestedBeanPropertyValue(data, "proprietario.pessoa.nrIdentificacao", nrIdentificacaoFormatado);
			setNestedBeanPropertyValue(data, "proprietarioNrIdentificacao", nrIdentificacaoFormatado);
			setNestedBeanPropertyValue(data, "proprietario.pessoa.nmPessoa", nmPessoa);
		}
		var idTipoMeioComposto = getNestedBeanPropertyValue(data,"idTipoMeioComposto");	
		setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",getNestedBeanPropertyValue(data,"idTipoMeioComposto"));
		habilitaDesabilitaSemiReboque(idTipoMeioComposto);
		findMotoristaByMeioTransporte(getNestedBeanPropertyValue(data,"idMeioTransporte"));
	}
	
	function findMotoristaByMeioTransporte(idMeioTransporte) {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findMotoristaByMeioTransporte",
				   "writeMotorista",{idMeioTransporte:idMeioTransporte});
	    xmit({serviceDataObjects:[sdo]}); 
	}
	function writeMotorista_cb(data) {
		if (data != undefined)
			fillFormWithFormBeanData(document.forms[0].tabIndex, data);
	}
	function dadosProprietarioSemi(data){
		 if(getNestedBeanPropertyValue(data,"outrasInfo") != null){
		 	var idProprietario = getNestedBeanPropertyValue(data,"outrasInfo.idProprietario");
			var nrIdentificacaoFormatado = getNestedBeanPropertyValue(data,"outrasInfo.nrIdentificacaoFormatado");
			var nmPessoa = getNestedBeanPropertyValue(data,"outrasInfo.nmPessoa"); 
			setNestedBeanPropertyValue(data, "proprietario.idProprietario", idProprietario);
			setNestedBeanPropertyValue(data, "proprietario.pessoa.nrIdentificacao", nrIdentificacaoFormatado);
			setNestedBeanPropertyValue(data, "proprietarioNrIdentificacao", nrIdentificacaoFormatado);
			setNestedBeanPropertyValue(data, "proprietario.pessoa.nmPessoa", nmPessoa);
		 }
	}
	
		
	function habilitaDesabilitaMotorista_cb(data){
		meioTransporteRodoviario1_meioTransporte_nrFrota_exactMatch_cb(data);
	    var idTipoMeioComposto = getNestedBeanPropertyValue(data,":0.idTipoMeioComposto");
	    findMotoristaByMeioTransporte(getNestedBeanPropertyValue(data,":0.idMeioTransporte"));
	    setElementValue("modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte",getNestedBeanPropertyValue(data,":0.idTipoMeioComposto"));
	    habilitaDesabilitaSemiReboque(idTipoMeioComposto);	
	  
	}
	 
	
	function habilitaDesabilitaSemiReboque(data){
	    if(data!= null){
	     	setDisabled(document.getElementById("meioTransporteRodoviario3.idMeioTransporte"), false);
	     	setDisabled(document.getElementById("meioTransporteRodoviario2.idMeioTransporte"), false);
	    }else { 
	    	setDisabled(document.getElementById("meioTransporteRodoviario3.idMeioTransporte"), true);	 	
	    	setDisabled(document.getElementById("meioTransporteRodoviario2.idMeioTransporte"), true);
	    }		
	}


	function initWindow(eventObj) {
		setDisabled("qtEmpresa",true);
		setDisabled("qtAcidente",true);
		setDisabled("qtRoubo",true);
		document.getElementById("qtEmpresa").required = "false";
		document.getElementById("qtAcidente").required = "false";
		document.getElementById("qtRoubo").required = "false";
		
		
		populateInfoUsuarioLogado();
	}

	function limpaCamposByButton(){
		newButtonScript();
		setDisabled(document.getElementById("botaoLimpar"), false);
		setDisabled(document.getElementById("meioTransporteRodoviario3.idMeioTransporte"), true);
		setDisabled(document.getElementById("meioTransporteRodoviario2.idMeioTransporte"), true);
	}
	
	// ######################################################################
	// reset na identificação do meio de transporte
	// ######################################################################
	function resetMeioTransporte() {
		if (getElementValue("meioTransporteRodoviario1.meioTransporte.nrFrota") == "" ){
			getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador") = "";
		}
		return meioTransporteRodoviario_meioTransporte_nrIdentificadorOnChangeHandler();
	}

	function desabilitaMotorista1(){
		if(getElementValue("meioTransporteRodoviario1.meioTransporte.nrFrota")== ''){
			resetValue("meioTransporteRodoviario3.idMeioTransporte");
			resetValue("meioTransporteRodoviario2.idMeioTransporte");
			setDisabled(document.getElementById("meioTransporteRodoviario3.idMeioTransporte"), true);
			setDisabled(document.getElementById("meioTransporteRodoviario2.idMeioTransporte"), true);
			
		}	
        return meioTransporteRodoviario1_meioTransporte_nrFrotaOnChangeHandler(); 
		
	}
	
	function desabilitaMotorista2(){
		if(getElementValue("meioTransporteRodoviario.meioTransporte.nrIdentificador")== ''){
			resetValue("meioTransporteRodoviario3.idMeioTransporte");
			resetValue("meioTransporteRodoviario2.idMeioTransporte");
			setDisabled(document.getElementById("meioTransporteRodoviario3.idMeioTransporte"), true);
			setDisabled(document.getElementById("meioTransporteRodoviario2.idMeioTransporte"), true);
		}	
        return meioTransporteRodoviario_meioTransporte_nrIdentificadorOnChangeHandler();
		
	}
	
	
	function desabilitaHabilitaQuantasVezes(field) {
		var name = "qt" + field.name.substring(2);
		resetValue(name);
		setDisabled(name,!field.checked);
		document.getElementById(name).required = new String(field.checked);
	}

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		setDisabled(document.getElementById("botaoLimpar"), false);
		findInfoUsuarioLogado();
	}

	var infoUsuario = undefined;
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		if (error != undefined) {
			alert(error);
		} else {
			infoUsuario = data;
			populateInfoUsuarioLogado();
		}
	}

	function populateInfoUsuarioLogado() {
		fillFormWithFormBeanData(document.forms[0].tabIndex, infoUsuario);
	}

</script>
<adsm:window title="emitirFichaCadastroReguladora" service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction" onPageLoadCallBack="pageLoadCustom">
	<adsm:form action="/contratacaoVeiculos/emitirFichaCadastroReguladora">
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>
	    <adsm:hidden property="isCalledByLookup" value="true" />
	    <adsm:hidden property="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
		<adsm:lookup 
			onchange="return desabilitaMotorista1()"
			onPopupSetValue="dadosProprietario"
			onDataLoadCallBack="habilitaDesabilitaMotorista"
			picker="false"
		    serializable="false"
			property="meioTransporteRodoviario1" 
			criteriaProperty="meioTransporte.nrFrota" 
			idProperty="idMeioTransporte" 
			service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupMeioTransporteRodoviario" 
			label="meioTransporte" labelWidth="20%"
			dataType="text" size="8" maxLength="6" 
			width="80%" 
			cmd="rodo" required="true"
			action="/contratacaoVeiculos/manterMeiosTransporte" exactMatch="true">
			<adsm:propertyMapping criteriaProperty="isCalledByLookup" modelProperty="isCalledByLookup"/>
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
            <adsm:propertyMapping relatedProperty="meioTransporteRodoviario.idMeioTransporte" modelProperty="idMeioTransporte" />      
            <adsm:propertyMapping relatedProperty="meioTransporteRodoviario.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
            <adsm:propertyMapping relatedProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
            <adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
            <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa"/>          
            <adsm:propertyMapping relatedProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
            <adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty=""/>
            <adsm:propertyMapping relatedProperty="motorista.pessoa.nrIdentificacao" modelProperty=""/>
             <adsm:propertyMapping relatedProperty="motorista.idMotorista" modelProperty=""/>
            <adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />
			
			<adsm:lookup
		 		onchange="return desabilitaMotorista2()"
				picker="true"
				onPopupSetValue="dadosProprietario"
				onDataLoadCallBack="habilitaDesabilitaMotorista"
				property="meioTransporteRodoviario" 
				criteriaProperty="meioTransporte.nrIdentificador" 
				idProperty="idMeioTransporte" 
				service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupMeioTransporteRodoviario" 
				dataType="text"  
				size="20" maxLength="25" 
				cmd="rodo"
				action="/contratacaoVeiculos/manterMeiosTransporte"
				exactMatch="false"
				minLengthForAutoPopUpSearch="3">
				<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario1.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping criteriaProperty="isCalledByLookup" modelProperty="isCalledByLookup"/>
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario1.idMeioTransporte" modelProperty="idMeioTransporte" /> 
				<adsm:propertyMapping relatedProperty="meioTransporteRodoviario1.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
				<adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
				<adsm:propertyMapping relatedProperty="proprietario.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
	            <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa"/>   
	            <adsm:propertyMapping relatedProperty="proprietario.idProprietario" modelProperty="proprietario.idProprietario"/>
	            <adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty=""/>
	            <adsm:propertyMapping relatedProperty="motorista.pessoa.nrIdentificacao" modelProperty=""/>
	            <adsm:propertyMapping relatedProperty="motorista.idMotorista" modelProperty=""/>
	            <adsm:propertyMapping relatedProperty="meioTransporteNrFrota" modelProperty="meioTransporte.nrFrota" />
	            <adsm:propertyMapping relatedProperty="identificacaoMeioTransporte" modelProperty="meioTransporte.nrIdentificador" />
	            
           		<adsm:hidden property="identificacaoMeioTransporte" serializable="true"/>
             	<adsm:hidden property="meioTransporteNrFrota" serializable="true"/>
			</adsm:lookup>
	</adsm:lookup>		
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>
	
	
	<!-- lookup PROPRIETARIO -->
	   <adsm:lookup 
		   service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupProprietario" 
		   dataType="text" 
		   property="proprietario" 
		   idProperty="idProprietario"
		   criteriaProperty="pessoa.nrIdentificacao" 
		   relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		   label="proprietario" size="30" maxLength="20"  
		   action="/contratacaoVeiculos/manterProprietarios" labelWidth="20%" width="80%" required="true" disabled="true">
                  <adsm:propertyMapping relatedProperty="proprietario.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
                  <adsm:propertyMapping relatedProperty="proprietarioNrIdentificacao" modelProperty="pessoa.nrIdentificacao" />
                  
                  <adsm:textbox dataType="text" property="proprietario.pessoa.nmPessoa" size="60" disabled="true" />
                  
        </adsm:lookup>
        <adsm:hidden property="proprietarioNrIdentificacao" serializable="true"/>
	
	
	
	
<%--Lookup SEMI-REBOQUE--------------------------------------------------------------------------------------------------------------------%>		
	 <adsm:lookup 
        	onPopupSetValue="dadosProprietarioSemi"
		    picker="false"
		    serializable="true"
		   	property="meioTransporteRodoviario3" 
			criteriaProperty="meioTransporte.nrFrota" 
			idProperty="idMeioTransporte" 
			service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupSemiReboque" 
			label="semiReboque" labelWidth="20%"
			dataType="text" size="8" maxLength="6"
			width="80%" 
			cmd="rodo"
			action="/contratacaoVeiculos/manterMeiosTransporte"
			disabled="true" >
			<adsm:propertyMapping criteriaProperty="isCalledByLookup" modelProperty="isCalledByLookup"/>
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
            
            <adsm:propertyMapping relatedProperty="meioTransporteRodoviario3.idMeioTransporte" modelProperty="meioTransporte.idMeioTransporte" />      
  
            <adsm:propertyMapping relatedProperty="meioTransporteRodoviario2.meioTransporte.nrIdentificador" modelProperty="meioTransporte.nrIdentificador" />
            
            <adsm:propertyMapping relatedProperty="identificacaoSemiReboque" modelProperty="meioTransporte.nrIdentificador" />
            <adsm:propertyMapping relatedProperty="semiReboqueNrFrota" modelProperty="meioTransporte.nrFrota" />
            
            <adsm:propertyMapping relatedProperty="proprietarioSemiNrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
			<adsm:propertyMapping relatedProperty="proprietarioSemi.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
	        <adsm:propertyMapping relatedProperty="proprietarioSemi.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa"/>   
	        <adsm:propertyMapping relatedProperty="proprietarioSemi.idProprietario" modelProperty="proprietario.idProprietario"/>
            
	 <adsm:lookup
			picker="true"
			onPopupSetValue="dadosProprietarioSemi"
			property="meioTransporteRodoviario2" 
			criteriaProperty="meioTransporte.nrIdentificador" 
			idProperty="idMeioTransporte" 
			service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupSemiReboque" 
			dataType="text"  
			size="20" maxLength="25" 
			cmd="rodo" 
			action="/contratacaoVeiculos/manterMeiosTransporte"
			exactMatch="false"
			minLengthForAutoPopUpSearch="3"
			disabled="true">
			<adsm:propertyMapping criteriaProperty="isCalledByLookup" modelProperty="isCalledByLookup"/>
			<adsm:propertyMapping criteriaProperty="meioTransporteRodoviario3.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			<adsm:propertyMapping criteriaProperty="modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" modelProperty="meioTransporte.modeloMeioTransporte.tipoMeioTransporte.idTipoMeioTransporte" />
			
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario3.idMeioTransporte" modelProperty="meioTransporte.idMeioTransporte" /> 
			<adsm:propertyMapping relatedProperty="meioTransporteRodoviario3.meioTransporte.nrFrota" modelProperty="meioTransporte.nrFrota" />
			
			<adsm:propertyMapping relatedProperty="identificacaoSemiReboque" modelProperty="meioTransporte.nrIdentificador" />
			<adsm:propertyMapping relatedProperty="semiReboqueNrFrota" modelProperty="meioTransporte.nrFrota" />
			
			<adsm:propertyMapping relatedProperty="proprietarioSemiNrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
			<adsm:propertyMapping relatedProperty="proprietarioSemi.pessoa.nrIdentificacao" modelProperty="proprietario.pessoa.nrIdentificacao"/>          
	        <adsm:propertyMapping relatedProperty="proprietarioSemi.pessoa.nmPessoa" modelProperty="proprietario.pessoa.nmPessoa"/>   
	        <adsm:propertyMapping relatedProperty="proprietarioSemi.idProprietario" modelProperty="proprietario.idProprietario"/>
			
	        <adsm:hidden property="identificacaoSemiReboque" serializable="true"/>
	        <adsm:hidden property="semiReboqueNrFrota" serializable="true"/>
		</adsm:lookup>	
	</adsm:lookup>		
<%--Lookup SEMI-REBOQUE--------------------------------------------------------------------------------------------------------------------%>				
			
			
	<adsm:lookup 
		   service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupProprietario" 
		   dataType="text" labelWidth="20%" width="80%" disabled="true" property="proprietarioSemi" 
		   idProperty="idProprietario" criteriaProperty="pessoa.nrIdentificacao" relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
		   label="proprietario" size="30" maxLength="20"  action="/contratacaoVeiculos/manterProprietarios">
                  <adsm:propertyMapping relatedProperty="proprietarioSemi.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa" />
                  <adsm:propertyMapping relatedProperty="proprietarioSemiNrIdentificacao" modelProperty="pessoa.nrIdentificacao" />
                  
                  <adsm:textbox dataType="text" property="proprietarioSemi.pessoa.nmPessoa" size="60" disabled="true" />
                  
        </adsm:lookup>
        <adsm:hidden property="proprietarioSemiNrIdentificacao" serializable="true"/>
        
        

        <!-- lookup MOTORISTA -->
        <adsm:hidden property="tpSituacaoMotorista" value="A" serializable="false" />
        <adsm:hidden property="blBloqueadoMotorista" value="N" serializable="false" />
	    <adsm:lookup service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupMotorista"
		    	dataType="text" 
			    property="motorista" 
			    idProperty="idMotorista"
			    criteriaProperty="pessoa.nrIdentificacao" 
			    relatedCriteriaProperty="pessoa.nrIdentificacaoFormatado"
			    label="motorista" size="30" maxLength="20"  
			    action="/contratacaoVeiculos/manterMotoristas" labelWidth="20%" width="80%"  required="true">
		    <adsm:propertyMapping criteriaProperty="tpSituacaoMotorista" modelProperty="tpSituacao"/>
		    <adsm:propertyMapping criteriaProperty="blBloqueadoMotorista" modelProperty="blBloqueado"/>
		    
 			<adsm:propertyMapping relatedProperty="motorista.pessoa.nmPessoa" modelProperty="pessoa.nmPessoa"/>
           	<adsm:textbox dataType="text" property="motorista.pessoa.nmPessoa" size="60" disabled="true" />
        </adsm:lookup>
        
        <adsm:section caption="dadosCarga"/>
        
        <adsm:lookup label="filialOrigem" labelWidth="20%" dataType="text" size="3" maxLength="3" width="80%"
				     service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupFilial" property="filialO" idProperty="idFilial"
					 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true" criteriaSerializable="true">
					<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialO.pessoa.nmFantasia"/>
					<adsm:textbox dataType="text" property="filialO.pessoa.nmFantasia" size="35" disabled="true" serializable="true"/>
		</adsm:lookup>

		<adsm:hidden property="tpAcesso" value="A" />
        <adsm:lookup label="filialDestino" labelWidth="20%" dataType="text" size="3" maxLength="3" width="80%"
			     service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findLookupFilial" property="filialD" idProperty="idFilial"
				 criteriaProperty="sgFilial" action="/municipios/manterFiliais" required="true" criteriaSerializable="true">
			<adsm:propertyMapping criteriaProperty="tpAcesso" modelProperty="tpAcesso" />
			<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filialD.pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filialD.pessoa.nmFantasia" size="35" disabled="true" serializable="true"/> 
		</adsm:lookup>
		
		
		<adsm:combobox property="segmentoMercado.idSegmentoMercado" onlyActiveValues="true" optionLabelProperty="dsSegmentoMercado" 
			optionProperty="idSegmentoMercado" service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction.findComboSegmentoMercado" 
			label="tipoCarga" labelWidth="20%" width="80%" boxWidth="240" required="true">
			<adsm:propertyMapping relatedProperty="segmentoMercado.dsSegmentoMercado" modelProperty="dsSegmentoMercado"/>
			<adsm:hidden property="segmentoMercado.dsSegmentoMercado" serializable="true"/>
		</adsm:combobox>
			
        <adsm:textbox label="valor" labelWidth="20%" width="80%" property="vlValor" dataType="currency" required="true"/>
        <adsm:checkbox property="blRoubo" label="jaFoiVitimaRoubo" labelWidth="20%" width="30%" onclick="desabilitaHabilitaQuantasVezes(document.getElementById('blRoubo'));"/>
        <adsm:textbox label="quantasVezes" labelWidth="20%" width="30%" property="qtRoubo" dataType="integer" maxLength="5" disabled="true"/>
        <adsm:checkbox property="blAcidente" label="jaSeEnvolveuEmAcidente" labelWidth="20%" width="30%" onclick="desabilitaHabilitaQuantasVezes(document.getElementById('blAcidente'))"/>
        <adsm:textbox label="quantasVezes" labelWidth="20%" width="30%" property="qtAcidente" dataType="integer" maxLength="5" disabled="true"/>
        <adsm:checkbox property="blEmpresa" label="jaTransportouParaEstaEmpresa" labelWidth="20%" width="30%" onclick="desabilitaHabilitaQuantasVezes(document.getElementById('blEmpresa'))"/>
        <adsm:textbox label="quantasVezes" labelWidth="20%" width="30%" property="qtEmpresa" dataType="integer" maxLength="5" disabled="true"/>
        
    	<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.contratacaoveiculos.emitirFichaCadastroReguladoraAction"/>
			<adsm:button caption="limpar" onclick="limpaCamposByButton()" disabled="false" id="botaoLimpar"/>
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>
<script>

	document.getElementById("motorista.pessoa.nrIdentificacao").serializable = true;
	document.getElementById("tpSituacaoMotorista").masterLink = "true";
	document.getElementById("blBloqueadoMotorista").masterLink = "true";

</script>