<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window onPageLoadCallBack="emitirEntregasEfetuadasPageLoad">

	<adsm:form action="/entrega/emitirEntregasEfetuadasOrigem">
			
		<adsm:lookup service="lms.entrega.emitirEntregasEfetuadasOrigemAction.findLookupFilial" dataType="text"
				property="filial" criteriaProperty="sgFilial" label="filialResponsavelEntrega" size="3" maxLength="3"
				width="79%" labelWidth="21%" action="/municipios/manterFiliais" idProperty="idFilial" criteriaSerializable="true"
				 exactMatch="true" required="true">
				
        	<adsm:propertyMapping modelProperty="pessoa.nmFantasia" relatedProperty="filial.pessoa.nmFantasia"/>
        	<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" size="30" disabled="true" serializable="true"/>
        </adsm:lookup>
		
		<adsm:listbox property="filiais" label="filiaisDeOrigem" 
					  labelWidth="21%" size="4" boxWidth="180"  showIndex="false"
					  optionProperty="idFilial" optionLabelProperty="sgFilial" > 
			<adsm:lookup service="lms.entrega.emitirEntregasEfetuadasOrigemAction.findLookupFilial" 
						 dataType="text" property="filial" idProperty="idFilial" 
						 criteriaProperty="sgFilial"  size="3" maxLength="10" 
						 action="/municipios/manterFiliais" cmd="list">
			 	<adsm:propertyMapping relatedProperty="filiais_filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
				<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="false"/>
	        </adsm:lookup>
        </adsm:listbox>
				
		<adsm:combobox property="servico.idServico" optionProperty="idServico" optionLabelProperty="dsServico" 
						label="servico" service="lms.entrega.emitirEntregasEfetuadasOrigemAction.findServico" onlyActiveValues="false" 
						width="79%" labelWidth="21%" boxWidth="200">
				<adsm:propertyMapping relatedProperty="servico.dsServico" modelProperty="dsServico"/>
		</adsm:combobox>
		
		<adsm:hidden property="servico.dsServico"/>
		
		<adsm:range label="periodoEntrega" labelWidth="21%" width="79%" required="true" maxInterval="15">
			<adsm:textbox dataType="JTDate" property="dtPeriodoInicial"/> 
			<adsm:textbox dataType="JTDate" property="dtPeriodoFinal"/>
		</adsm:range>
		
		<adsm:combobox label="formatoRelatorio" labelWidth="21%" required="true" property="tpFormatoRelatorio" domain="DM_FORMATO_RELATORIO" defaultValue="pdf"/>
				
		<adsm:buttonBar>
			<adsm:reportViewerButton service="lms.entrega.emitirEntregasEfetuadasOrigemAction"/><%--/entrega/emitirEntregasEfetuadasOrigem.jasper --%> 
			<adsm:resetButton/>
		</adsm:buttonBar>
		
	</adsm:form>
</adsm:window>
<script>

	function initWindow(event) {

		if (event.name == 'cleanButton_click') {	
			setFilialValues();
			setFocusOnFirstFocusableField();
		} 
	}

	function emitirEntregasEfetuadasPageLoad_cb(){
		onPageLoad_cb();
		findFilialUsuario();
	}
	
	var idFilial;
	var sgFilial;
	var nmFilial;
	
	function setFilialValues() {		
		if (idFilial != undefined &&
			sgFilial != undefined &&
			nmFilial != undefined) {
			setElementValue("filial.idFilial", idFilial);
			setElementValue("filial.sgFilial", sgFilial);
			setElementValue("filial.pessoa.nmFantasia", nmFilial);
		}
	}
	
	function findFilialUsuario() {
		var data = new Array();
		var sdo = createServiceDataObject("lms.entrega.emitirEntregasEfetuadasOrigemAction.findFilialUsuario", "findFilialUsuario", data);
		xmit({serviceDataObjects:[sdo]});
	}
	
	function findFilialUsuario_cb(data, exception) {
		if (exception == null){			
			idFilial = getNestedBeanPropertyValue(data, "filial.idFilial");
			sgFilial = getNestedBeanPropertyValue(data, "filial.sgFilial");
			nmFilial = getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia");			
		}
		setFilialValues();
	}


</script>