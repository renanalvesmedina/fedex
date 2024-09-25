<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<adsm:window title="emitirEficienciaEntregas" service="lms.entrega.emitirEficienciaEntregasAction"
		onPageLoadCallBack="pageLoadCustom" >
	<adsm:form action="/entrega/emitirEficienciaEntregas" idProperty="id" >
				
		<adsm:lookup dataType="text" property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.entrega.emitirEficienciaEntregasAction.findLookupFilial" required="true"
				action="/municipios/manterFiliais" criteriaSerializable="true" onchange="return filialOnChange(this)"
				onDataLoadCallBack="filialDataLoad" onPopupSetValue="filialPopup"
				label="filialEntrega" labelWidth="18%" width="82%" size="3" maxLength="3" exactMatch="true" >		
			<adsm:propertyMapping criteriaProperty="empresa.tpEmpresa" modelProperty="empresa.tpEmpresa"/>
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="true" size="30" />
		</adsm:lookup>
		
		<adsm:hidden property="empresa.tpEmpresa" value="M" serializable="false"/>
		
		<adsm:combobox label="opcao" property="tpOpcao"	domain="DM_TIPO_RELATORIO_EFICIENCIA" required="true" labelWidth="18%" width="82%" />
                
   		<adsm:lookup dataType="text" property="meioTransporte2" idProperty="idMeioTransporte"
				service="lms.entrega.emitirEficienciaEntregasAction.findLookupMeioTransporte" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" criteriaProperty="nrFrota"
				label="meioTransporte" labelWidth="18%" criteriaSerializable="true"
				width="82%" size="8" serializable="false" maxLength="6" cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="meioTransporte.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="meioTransporte.nrIdentificador"
					modelProperty="nrIdentificador" />
					
			<adsm:lookup dataType="text" property="meioTransporte" idProperty="idMeioTransporte"
					service="lms.entrega.emitirEficienciaEntregasAction.findLookupMeioTransporte"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" 
					criteriaProperty="nrIdentificador" criteriaSerializable="true"
					picker="true" maxLength="25" size="20" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
			</adsm:lookup>
					
		</adsm:lookup>
        
        <adsm:range label="periodoFechamento" labelWidth="18%" width="82%" required="true" maxInterval="30">
			<adsm:textbox dataType="JTDate" property="dtFechamentoInicial"/>
			<adsm:textbox dataType="JTDate" property="dtFechamentoFinal"/>
		</adsm:range>
       
        <adsm:combobox label="formatoRelatorio" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" 
        		defaultValue="pdf" required="true" labelWidth="18%" width="82%" />
        
        <adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirEficienciaEntregasAction" />
			<adsm:resetButton />
		</adsm:buttonBar>	
	</adsm:form>
</adsm:window>

<script type="text/javascript">
<!--

	function pageLoadCustom_cb(data) {
		onPageLoad_cb(data);
		findInfoUsuarioLogado();
	}
	
	function initWindow(eventObj) {
		if (eventObj.name == "cleanButton_click") {
			populateInfoUsuarioLogado();
		}
	}

	var infoUsuario = undefined;
	
	function findInfoUsuarioLogado() {
		var sdo = createServiceDataObject("lms.entrega.emitirEficienciaEntregasAction.findInfoUsuarioLogado",
				"findInfoUsuarioLogado",undefined);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findInfoUsuarioLogado_cb(data,error) {
		infoUsuario = data;
		populateInfoUsuarioLogado();
	}

	function populateInfoUsuarioLogado() {
		fillFormWithFormBeanData(document.forms[0].tabIndex, infoUsuario);
	}
	
	
	function filialOnChange(obj){
		var retorno = filial_sgFilialOnChangeHandler();
		
		if (obj.value == ''){
			resetCampos();			
		}
		
		return retorno;
	}
	
	function filialDataLoad_cb(data){
		filial_sgFilial_exactMatch_cb(data);
		
		if (data != undefined && data.length == 1){
			resetCampos();
		}
	}
	
	function filialPopup(data){
		resetCampos();
		return true;
	}
	
	function resetCampos(){
		resetValue("tpOpcao");
		resetValue("meioTransporte2.idMeioTransporte");
		resetValue("meioTransporte.idMeioTransporte");
		resetValue("dtFechamentoInicial");
		resetValue("dtFechamentoFinal");
		resetValue("tpFormatoRelatorio");
	}

//-->
</script>
