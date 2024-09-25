<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.consultarImpressoraLogAction">
	<adsm:form action="/expedicao/consultarImpressoraLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idImpressoraLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="impressora.idImpressora" title="impressora.idImpressora" dataType="integer"/>
		<adsm:gridColumn property="filial.idFilial" title="filial.idFilial" dataType="integer"/>
		<adsm:gridColumn property="dsCheckIn" title="dsCheckIn"/>
		<adsm:gridColumn property="dsLocalizacao" title="dsLocalizacao"/>
		<adsm:gridColumn property="tpImpressora" title="tpImpressora" isDomain="true"/>
		<adsm:gridColumn property="dsModelo" title="dsModelo"/>
		<adsm:gridColumn property="dsFabricante" title="dsFabricante"/>
		<adsm:gridColumn property="dsMac" title="dsMac"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>