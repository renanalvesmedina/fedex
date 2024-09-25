<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.pendencia.manterFaseOcorrenciaAction" onPageLoadCallBack="pageLoad">
	<adsm:form action="/pendencia/manterFaseOcorrencia" idProperty="idFaseOcorrencia" service="lms.pendencia.manterFaseOcorrenciaAction.findByIdCustom">
		
		<adsm:lookup dataType="text" property="faseProcesso" idProperty="idFaseProcesso" criteriaProperty="dsFase"
					 service="lms.sim.faseProcessoService.findLookup" action="/sim/manterFaseProcesso"
					 label="faseProcesso" maxLength="50" width="55%" size="45%" exactMatch="false" minLengthForAutoPopUpSearch="4"/>
					 
		<adsm:lookup dataType="text" property="ocorrenciaPendencia" idProperty="idOcorrenciaPendencia" criteriaProperty="dsOcorrencia"
					 service="lms.pendencia.ocorrenciaPendenciaService.findLookup" action="/pendencia/manterOcorrenciasPendencia"
					 label="ocorrenciaPendencia" maxLength="50" width="55%" size="45%" exactMatch="false" minLengthForAutoPopUpSearch="4"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script>
	function pageLoad_cb(data) {
		onPageLoad_cb(data);
		if(getElementValue("ocorrenciaPendencia.idOcorrenciaPendencia") != "") {
	    	setDisabled("ocorrenciaPendencia.idOcorrenciaPendencia", true);
		}
	    return false;
	}
</script>