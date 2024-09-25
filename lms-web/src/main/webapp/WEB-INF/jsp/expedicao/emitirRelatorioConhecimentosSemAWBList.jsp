<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
	    onPageLoad(); 
        var data = new Array();	
		var sdo = createServiceDataObject("lms.expedicao.emitirRelatorioConhecimentosSemAWBAction.getBasicData", "dataSession", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
</script>
<adsm:window onPageLoad="loadDataObjects">
	<adsm:form action="/expedicao/emitirRelatorioConhecimentosSemAWB">

		<adsm:hidden property="tpEmp" value="M" serializable="false" />
		<adsm:lookup dataType="text" 
        		property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.expedicao.emitirRelatorioConhecimentosSemAWBAction.montaLookupFilial"
				action="/municipios/manterFiliais"
				label="filialOrigem" labelWidth="15%" width="33%" size="3" maxLength="3" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="false" size="30" />
		</adsm:lookup>

		<adsm:lookup dataType="text" 
        		property="filialDestino" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.expedicao.emitirRelatorioConhecimentosSemAWBAction.montaLookupFilial"
				action="/municipios/manterFiliais"
				label="filialDestino" labelWidth="15%" width="33%" size="3" maxLength="3" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filialDestino.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />
			<adsm:textbox dataType="text" property="filialDestino.pessoa.nmFantasia" disabled="true" serializable="false" size="30" />
		</adsm:lookup>
		
		<adsm:range
		label="periodo"
		labelWidth="15%"
		required="true"
		width="32%">
			<adsm:textbox dataType="JTDate" 
						  property="dtInicial" 
						  label="horarioInicial" 
						  required="false"
						  labelWidth="16%" 
						  width="37%"/>
			<adsm:textbox dataType="JTDate" 
						  property="dtFinal" 
						  label="horarioFinal" 
						  labelWidth="14%" 
						  width="31%"/>
		</adsm:range>        

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.expedicao.emitirRelatorioConhecimentosSemAWBAction"/>
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
			
	/**
	*Copia dados para da sessao recebidos para variáveis globais da tela
	*/
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
