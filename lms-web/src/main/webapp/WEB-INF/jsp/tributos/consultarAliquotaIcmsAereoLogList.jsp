<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.tributos.consultarAliquotaIcmsAereoLogAction">
	<adsm:form action="/tributos/consultarAliquotaIcmsAereoLog" >
		
		<adsm:combobox
			service="lms.tributos.consultarAliquotaIcmsAereoLogAction.findUnidadeFederativa" 
			property="unidadeFederativa.idUnidadeFederativa"
			optionProperty="idUnidadeFederativa"
			boxWidth="120"
			optionLabelProperty="siglaDescricao"
			label="ufOrigem"
		/>
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="gridLog"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid 
			property="gridLog" 
			idProperty="idAliquotaIcmsAereoLog"
			selectionMode="none"
			width="10000"
			scrollBars="horizontal"
  	>
		<adsm:gridColumn property="aliquotaIcmsAereo.idAliquotaIcmsAereo" title="aliquotaIcmsAereo.idAliquotaIcmsAereo" dataType="integer"/>
		<adsm:gridColumn property="unidadeFederativa.idUnidadeFederativa" title="unidadeFederativa.idUnidadeFederativa" dataType="integer"/>
		<adsm:gridColumn property="dtInicioVigencia" title="dtInicioVigencia" dataType="JTDate"/>
		<adsm:gridColumn property="pcAliquotaInterna" title="pcAliquotaInterna" dataType="currency"/>
		<adsm:gridColumn property="pcEmbuteInterno" title="pcEmbuteInterno" dataType="currency"/>
		<adsm:gridColumn property="pcAliquotaInterestadual" title="pcAliquotaInterestadual" dataType="currency"/>
		<adsm:gridColumn property="pcEmbuteInterestadual" title="pcEmbuteInterestadual" dataType="currency"/>
		<adsm:gridColumn property="obInterno" title="obInterno"/>
		<adsm:gridColumn property="obInterestadual" title="obInterestadual"/>
		<adsm:gridColumn property="pcAliquotaDestNc" title="pcAliquotaDestNc" dataType="currency"/>
		<adsm:gridColumn property="pcEmbuteDestNc" title="pcEmbuteDestNc" dataType="currency"/>
		<adsm:gridColumn property="obDestNc" title="obDestNc"/>
		<adsm:gridColumn property="tpOrigemLog" title="tpOrigemLog" isDomain="true"/>
		<adsm:gridColumn property="loginLog" title="loginLog"/>
		<adsm:gridColumn property="dhLog" title="dhLog" dataType="JTDateTimeZone"/>
		<adsm:gridColumn property="opLog" title="opLog" isDomain="true"/>
	</adsm:grid>
	
	<adsm:buttonBar/>
</adsm:window>