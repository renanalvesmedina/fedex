<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>

<script type="text/javascript">
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
	    onPageLoad(); 
        var data = new Array();	
		var sdo = createServiceDataObject("lms.expedicao.emitirConhecimentosCanceladosAction.getBasicData", "dataSession", data);
    	xmit({serviceDataObjects:[sdo]});
	}
</script>
<adsm:window onPageLoad="loadDataObjects">
	<adsm:form action="/expedicao/emitirConhecimentosCancelados">
		
       <adsm:hidden property="tpEmp" value="M" serializable="false" />
       <adsm:lookup dataType="text" 
        		property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.expedicao.emitirConhecimentosCanceladosAction.montaLookupFilial"
				
				action="/municipios/manterFiliais"
				label="filial" labelWidth="15%" width="33%" size="3" maxLength="3" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />     
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="false" size="30" />
		</adsm:lookup>

		<adsm:range
		label="periodo"
		labelWidth="15%"
		width="32%">

			<adsm:textbox dataType="JTDate" 
						  property="dtInicial" 
						  label="horarioInicial" 
						  required="true"
						  labelWidth="16%" 
						  width="37%"/>
						  
			<adsm:textbox dataType="JTDate" 
						  property="dtFinal" 
						  label="horarioFinal" 
						  required="true"
						  labelWidth="14%" 
						  width="31%"/>
				  
		</adsm:range>

		<adsm:combobox property="tpAbrangencia" 
					   label="abrangencia" 
					   domain="DM_ABRANGENCIA"
					   labelWidth="15%" 
					   width="28%"
		/>
					   
		<adsm:combobox
			property="motivoCancelamento.idMotivoCancelamento"
			optionLabelProperty="dsMotivoCancelamento"
			labelWidth="20%"
			width="30%"
			optionProperty="idMotivoCancelamento"
			onlyActiveValues="true"
			service="lms.expedicao.emitirConhecimentosCanceladosAction.findMotivoCancelamento"
			required="false"
			label="motivoCancelamento"
			boxWidth="220"
		/>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.expedicao.emitirConhecimentosCanceladosAction"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script type="text/javascript">

	var idFilial = null;
	var sgFilial = null;
	var nmFilial = null;
	
	var dtInicial 	= null;
	var dtFinal 	= null;
	
// Copia dados para da sessao recebidos para variáveis globais da tela
	function dataSession_cb(data) {
		idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
		sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
		nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");
		
		dtInicial = getNestedBeanPropertyValue(data, "dataInicial");
		dtFinal = getNestedBeanPropertyValue(data, "dataFinal");
		
		writeDataSession();
	}
	
	function initWindow(event) {
		if(event.name == "cleanButton_click"){
			writeDataSession();
		}
	}
	
	/**
	 * Preenche os dados basicos da tela
	 */
	function writeDataSession() {
		setElementValue("filial.idFilial", idFilial);
		setElementValue("filial.sgFilial", sgFilial);
		setElementValue("filial.pessoa.nmFantasia", nmFilial);
		
		setElementValue("dtInicial", dtInicial);
		setElementValue("dtFinal", dtFinal);		
	}
</script>