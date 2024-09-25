<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.contasreceber.consultarItemRedecoLogAction">
	<adsm:form action="/contasreceber/consultarItemRedecoLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idItemRedecoLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="itemRedeco.idItemRedeco" title="itemRedeco.idItemRedeco" dataType="integer"/>
		<adsm:gridColumn property="redeco.idRedeco" title="redeco.idRedeco" dataType="integer"/>
		<adsm:gridColumn property="fatura.idFatura" title="fatura.idFatura" dataType="integer"/>
		<adsm:gridColumn property="vlTarifa" title="vlTarifa" dataType="currency"/>
		<adsm:gridColumn property="vlJuros" title="vlJuros" dataType="currency"/>
		<adsm:gridColumn property="recibo.idRecibo" title="recibo.idRecibo" dataType="integer"/>
		<adsm:gridColumn property="obItemRedeco" title="obItemRedeco"/>
		<adsm:gridColumn property="nrVersao" title="nrVersao" dataType="integer"/>
		<adsm:gridColumn property="vlDiferencaCambialCotacao" title="vlDiferencaCambialCotacao" dataType="currency"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>