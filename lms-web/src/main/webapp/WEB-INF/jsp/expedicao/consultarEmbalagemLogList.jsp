<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.consultarEmbalagemLogAction">
	<adsm:form action="/expedicao/consultarEmbalagemLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idEmbalagemLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="embalagem.idEmbalagem" title="embalagem.idEmbalagem" dataType="integer"/>
		<adsm:gridColumn property="nrAltura" title="nrAltura" dataType="integer"/>
		<adsm:gridColumn property="nrLargura" title="nrLargura" dataType="integer"/>
		<adsm:gridColumn property="nrComprimento" title="nrComprimento" dataType="integer"/>
		<adsm:gridColumn property="dsEmbalagem" title="dsEmbalagem"/>
		<adsm:gridColumn property="blPrecificada" title="blPrecificada" renderMode="image-check"/>
		<adsm:gridColumn property="tpSituacao" title="tpSituacao" isDomain="true"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>