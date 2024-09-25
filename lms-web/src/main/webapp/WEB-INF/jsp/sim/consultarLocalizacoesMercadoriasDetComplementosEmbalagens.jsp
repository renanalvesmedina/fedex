<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sim.consultarLocalizacoesMercadoriasAction">
	<adsm:form  action="/sim/consultarLocalizacoesMercadorias" height="210" idProperty="idDoctoServico">
			<adsm:grid idProperty="idServicoEmbalagem" property="servicoEmbalagem" onRowClick="returnFalse" service="lms.sim.consultarLocalizacoesMercadoriasAction.findPaginatedComplEmbalagens" selectionMode="none" showPagging="false" showGotoBox="false" unique="true" gridHeight="100" >
				<adsm:gridColumn width="32%" title="descricao" property="dsEmbalagem"/>
				<adsm:gridColumn width="17%" title="altura" property="nrAltura" align="right" unit="cm"/>
				<adsm:gridColumn width="17%" title="largura" property="nrLargura" align="right" unit="cm"/>
				<adsm:gridColumn width="17%" title="comprimento" property="nrComprimento" align="right" unit="cm"/>
				<adsm:gridColumn width="17%" title="quantidade" property="nrQuantidade" align="right"/>
			</adsm:grid>
	</adsm:form>
</adsm:window>   
<script>
	function returnFalse(){
		return false;
	}
	function findPaginatedComplEmbalagens(){	
	    var idDoctoServico = parent.document.getElementById("idDoctoServico").value;
	   	var data = new Array();
		setNestedBeanPropertyValue(data,"idDoctoServico",idDoctoServico);
		servicoEmbalagemGridDef.executeSearch(data);
	}
</script>