<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarAliquotaIssMunicipioServLogAction">
	<adsm:form action="/tributos/consultarAliquotaIssMunicipioServLog" >
		
		<%-- INSERIR FILTROS AQUI! --%>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idAliquotaIssMunicipioServLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="aliquotaIssMunicipioServ.idAliquotaIssMunicipioServ" title="aliquotaIssMunicipioServ.idAliquotaIssMunicipioServ" dataType="integer"/>
		<adsm:gridColumn property="issMunicipioServico.idIssMunicipioServico" title="issMunicipioServico.idIssMunicipioServico" dataType="integer"/>
		<adsm:gridColumn property="pcAliquota" title="pcAliquota" dataType="currency"/>
		<adsm:gridColumn property="pcEmbute" title="pcEmbute" dataType="currency"/>
		<adsm:gridColumn property="dtVigenciaInicial" title="dtVigenciaInicial" dataType="JTDate"/>
		<adsm:gridColumn property="blEmiteNfServico" title="blEmiteNfServico" renderMode="image-check"/>
		<adsm:gridColumn property="dtVigenciaFinal" title="dtVigenciaFinal" dataType="JTDate"/>
		<adsm:gridColumn property="blRetencaoTomadorServico" title="blRetencaoTomadorServico" renderMode="image-check"/>
		<adsm:gridColumn property="obAliquotaIssMunicipioServ" title="obAliquotaIssMunicipioServ"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>