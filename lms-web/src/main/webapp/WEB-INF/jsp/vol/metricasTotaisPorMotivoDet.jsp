<%-- @ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" import="org.apache.commons.beanutils.*" --%>
<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.vol.metricasTotaisPorMotivoAction">
	<adsm:form  action="/vol/metricasTotaisPorMotivo">
		
		<adsm:lookup label="filial" width="8%" labelWidth="10%" 
				     property="filial"
		             idProperty="idFilial"
		             criteriaProperty="sgFilial"
		             action="/municipios/manterFiliais" 
		             service="lms.vol.metricasTotaisPorMotivoAction.findLookupFilial" 
		             dataType="text"
		             size="3" 
		             maxLength="3" disabled="true">		             
        	<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" 
        						  modelProperty="pessoa.nmFantasia" />
            <adsm:textbox dataType="text" 
            			  property="filial.pessoa.nmFantasia" 
            			  serializable="false" 
            			  size="50" 
            			  maxLength="50" disabled="true" width="50%" disabled="true"/>
        </adsm:lookup>

		<adsm:range label="periodo" width="70%" labelWidth="10%">
			<adsm:textbox dataType="JTDate" property="dataInicial" disabled="true"/>
			<adsm:textbox dataType="JTDate" property="dataFinal" disabled="true"/>
		</adsm:range>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="detalhe"/>
			<adsm:resetButton/>
		</adsm:buttonBar>		
		
	</adsm:form>

	<adsm:grid property="detalhe" idProperty="idDetalhe" selectionMode="none"  disableMarkAll="true"
			   rows="14" gridHeight="150" unique="true" onRowClick="onClick"
			   service="lms.vol.metricasTotaisPorMotivoAction.findPaginatedDetalhe"
			   rowCountService="lms.vol.metricasTotaisPorMotivoAction.getRowCountDetalhe">
		<adsm:gridColumn property="frota" title="frota" width="9%" />
		<adsm:gridColumn property="manifesto" title="manifesto" width="9%" />
		<adsm:gridColumn property="endereco" title="endereco" width="30%" />
		<adsm:gridColumn property="cliente" title="cliente" width="25%" />
		<adsm:gridColumn property="volumes" title="volumes" width="8%" />
		<adsm:gridColumn property="baixa" title="baixa" width="10%" />
		<adsm:gridColumn property="dpe" title="dpe" width="9%" />		
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>

<script>

	function onClick() {
		return false;
	}

	function initWindow(eventObj) {	
		findButtonScript('detalhe', document.forms[0]);
				
		var tabGroup = getTabGroup(this.document);
		var tab = tabGroup.getTab("pesq");			
		setElementValue('filial.sgFilial', tab.getElementById('filial.sgFilial'));
		setElementValue('filial.pessoa.nmFantasia', tab.getElementById('filial.pessoa.nmFantasia'));
		setElementValue('dataInicial', tab.getElementById('dataInicial'));
		setElementValue('dataFinal', tab.getElementById('dataFinal'));
	}
</script>