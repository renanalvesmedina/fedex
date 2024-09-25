<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	
	/**
	 * Carrega dados do usuario
	 */
	function loadDataObjects() {	
	    onPageLoad(); 
        var data = new Array();	
		var sdo = createServiceDataObject("lms.expedicao.resumoDiarioPesoEmbarcadoManifestoAction.getDataSugerida", "dataSession", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
</script>

<adsm:window onPageLoad="loadDataObjects">
	<adsm:form action="/expedicao/resumoDiarioPesoEmbarcadoManifesto">

		<adsm:hidden property="tpEmp" value="M" serializable="false" />

		<adsm:lookup label="filialOrigem2"
				 property="filialByIdFilialOrigem"
				 idProperty="idFilial"
                 criteriaProperty="sgFilial"
				 action="/municipios/manterFiliais"
	        	 service="lms.vendas.emitirTabelasClienteAction.findLookupFilial" 
				 dataType="text"
				 exactMatch="true"
				 minLengthForAutoPopUpSearch="3"
				 labelWidth="28%"
				 width="9%"
				 size="3" maxLength="3">
				 <adsm:propertyMapping relatedProperty="filialByIdFilialOrigem.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
				<adsm:textbox dataType="text" width="27%"
				 property="filialByIdFilialOrigem.pessoa.nmFantasia" size="30" disabled="true" />
 	 	    	 <adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />	    	
		</adsm:lookup>
        
		<adsm:lookup label="filialDestino2"
				 property="filialByIdFilialDestino"
				 idProperty="idFilial"
                 criteriaProperty="sgFilial"
				 action="/municipios/manterFiliais"
	        	 service="lms.vendas.emitirTabelasClienteAction.findLookupFilial" 
				 dataType="text"
				 exactMatch="true"
				 minLengthForAutoPopUpSearch="3"
				 labelWidth="28%"
				 width="9%"
				 size="3" maxLength="3">
				 <adsm:propertyMapping relatedProperty="filialByIdFilialDestino.pessoa.nmFantasia"
				modelProperty="pessoa.nmFantasia" />
				<adsm:textbox dataType="text" width="27%"
				 property="filialByIdFilialDestino.pessoa.nmFantasia" size="30" disabled="true" />
			    <adsm:propertyMapping modelProperty="empresa.tpEmpresa" criteriaProperty="tpEmp" />	    	
		</adsm:lookup>
		
		
		<adsm:range label="periodoEmissaoManifesto" width="72%" labelWidth="28%" >
			<adsm:textbox dataType="JTDate" property="dataInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" required="true"/>		
		</adsm:range>

		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.expedicao.resumoDiarioPesoEmbarcadoService"/>
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
	
		setElementValue("filialByIdFilialOrigem.idFilial", idFilial);
		setElementValue("filialByIdFilialOrigem.sgFilial", sgFilial);
		setElementValue("filialByIdFilialOrigem.pessoa.nmFantasia", nmFilial);
			
		setElementValue("dataInicial", dtInicial);
		setElementValue("dataFinal", dtFinal);
		
	}
</script>