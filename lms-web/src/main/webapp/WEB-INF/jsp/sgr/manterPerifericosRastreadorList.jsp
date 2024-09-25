<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.sgr.manterPerifericosRastreadorAction">
	<adsm:form action="/sgr/manterPerifericosRastreador" idProperty="idPerifericoRastreador" >

		<adsm:textbox  label="nomePeriferico" property="dsPerifericoRastreador" dataType="text" maxLength="60" size="60" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" width="85%" renderOptions="true"/>		

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="perifericos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	
	<adsm:grid idProperty="idPerifericoRastreador" 
			   property="perifericos" 
			   selectionMode="check" 
			   gridHeight="130" 
			   unique="true"
			   scrollBars="none"
			   rows="13"
			   service="lms.sgr.manterPerifericosRastreadorAction.findPaginatedCustom"
			   rowCountService="lms.sgr.manterPerifericosRastreadorAction.getRowCountCustom">

		<adsm:gridColumn width="60%" property="dsPerifericoRastreador" title="nomePeriferico" align="left"/>
		<adsm:gridColumn width="40%" property="tpSituacao" title="situacao" dataType="text" align="left"/>		
		
		<adsm:buttonBar freeLayout="false">
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>

<script>

</script>