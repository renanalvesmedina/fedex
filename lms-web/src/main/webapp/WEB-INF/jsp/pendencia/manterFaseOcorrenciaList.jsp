<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterFaseOcorrenciaAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/pendencia/manterFaseOcorrencia">	
		
		<adsm:lookup dataType="text" property="faseProcesso" idProperty="idFaseProcesso" criteriaProperty="dsFase"
					 service="lms.sim.faseProcessoService.findLookup" action="/sim/manterFaseProcesso"
					 label="faseProcesso" maxLength="50" width="55%" size="45%" exactMatch="false" minLengthForAutoPopUpSearch="4"/>
					 
		<adsm:lookup dataType="text" property="ocorrenciaPendencia" idProperty="idOcorrenciaPendencia" criteriaProperty="dsOcorrencia"
					 service="lms.pendencia.ocorrenciaPendenciaService.findLookup" action="/pendencia/manterOcorrenciasPendencia"
					 label="ocorrenciaPendencia" maxLength="50" width="55%" size="45%" exactMatch="false" minLengthForAutoPopUpSearch="4"/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="faseOcorrencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="faseOcorrencia" idProperty="idFaseOcorrencia" gridHeight="200" unique="true">
		<adsm:gridColumn property="faseProcesso.cdFase" title="codigo" dataType="integer"  width="20%" />
		<adsm:gridColumn property="faseProcesso.dsFase" title="descricao" width="40%" />
		<adsm:gridColumn property="ocorrenciaPendencia.dsOcorrencia" title="ocorrenciaPendencia" width="40%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
<script>
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		povoaGrid();
	}
	
	function povoaGrid() {
		if(getElementValue("ocorrenciaPendencia.idOcorrenciaPendencia") != "") {
			var filtro = new Array();
	    	setNestedBeanPropertyValue(filtro, "ocorrenciaPendencia.idOcorrenciaPendencia", getElementValue("ocorrenciaPendencia.idOcorrenciaPendencia"));
	    	faseOcorrenciaGridDef.executeSearch(filtro, true);
	    	setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia", true);
		}
	    return false;
	}
</script>