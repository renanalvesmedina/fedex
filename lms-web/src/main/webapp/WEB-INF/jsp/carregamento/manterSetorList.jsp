<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.carregamento.manterSetorAction">
	<adsm:form action="/carregamento/manterSetor" idProperty="idSetor" >

		<adsm:textbox label="codigoRH" property="cdSetorRh" dataType="integer" maxLength="2" size="2" width="85%"/>
		<adsm:textbox label="nomeSetor" property="dsSetor" dataType="text" maxLength="60" size="60" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" width="85%" renderOptions="true"/>		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="setores"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idSetor" 
			   property="setores" 
			   selectionMode="check" 
			   gridHeight="130" 
			   unique="true"
			   scrollBars="none"
			   rows="12"
			   service="lms.carregamento.manterSetorAction.findPaginatedCustom"
			   rowCountService="lms.carregamento.manterSetorAction.getRowCountCustom">

		<adsm:gridColumn width="40%" property="dsSetor" title="nomeSetor" dataType="text" align="left"/>
		<adsm:gridColumn width="40%" property="cdSetorRh" title="codigoRH" dataType="integer" align="right"/>
		<adsm:gridColumn width="20%" property="tpSituacao" title="situacao" dataType="text" align="left"/>		
		
		<adsm:buttonBar freeLayout="false">
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

</script>