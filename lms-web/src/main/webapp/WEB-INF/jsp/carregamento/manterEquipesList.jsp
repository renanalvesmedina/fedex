<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script type="text/javascript">
	function loadPageData_cb(data) {
		onPageLoad_cb(data);
		if (document.getElementById("filial.idFilial").masterLink != "true") {
			loadDataUsuario();
		}
	}

	function loadDataUsuario() {
    	var data = new Array();
		var sdo = createServiceDataObject("lms.carregamento.equipeService.getDataUsuario", "loadDataUsuario", data);
    	xmit({serviceDataObjects:[sdo]});
	}
	
	function loadDataUsuario_cb(data, error) {
		setElementValue("filial.idFilial", getNestedBeanPropertyValue(data, "filial.idFilial"));
		setElementValue("filial.sgFilial", getNestedBeanPropertyValue(data, "filial.sgFilial"));
		setElementValue("filial.pessoa.nmFantasia", getNestedBeanPropertyValue(data, "filial.pessoa.nmFantasia"));
	}
	
	function limpaUsuario(document) {
		if (document.getElementById("filial.idFilial").masterLink != "true") {
			loadDataUsuario();		
		}
		newButtonScript(document);
	}
	
</script>
<adsm:window service="lms.carregamento.equipeService" onPageLoadCallBack="loadPageData">
	<adsm:form action="/carregamento/manterEquipes">
		<adsm:hidden property="filial.idFilial" />
     	<adsm:textbox property="filial.sgFilial" label="filial" dataType="text" size="3" width="85%" disabled="true" serializable="false" >
			<adsm:textbox property="filial.pessoa.nmFantasia" dataType="text" size="30" disabled="true" serializable="false" />
		</adsm:textbox>
		
		<adsm:combobox width="85%" label="setor" 
					    property="setor.idSetor"
					    service="lms.configuracoes.setorService.findSetorOrderByDsSetor" 
					    optionProperty="idSetor" 
					    optionLabelProperty="dsSetor" 
		/>		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" renderOptions="true" />
		<adsm:textbox property="dsEquipe" label="descricao" dataType="text" size="57" width="85%" maxLength="50" />
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="equipes"/>
			<adsm:button caption="limpar" id="limpar" buttonType="resetButton" onclick="limpaUsuario(this.document)" disabled="false"/>			
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid property="equipes" defaultOrder="filial_.sgFilial, setor_.dsSetor, dsEquipe" idProperty="idEquipe" selectionMode="check" gridHeight="200" unique="true" rows="11">
		<adsm:gridColumn property="filial.sgFilial" title="filial" width="23%" />
		<adsm:gridColumn property="setor.dsSetor" title="setor" width="33%" />
		<adsm:gridColumn property="dsEquipe" title="descricao" width="34%" />
		<adsm:gridColumn property="tpSituacao" title="situacao" isDomain="true" width="10%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
