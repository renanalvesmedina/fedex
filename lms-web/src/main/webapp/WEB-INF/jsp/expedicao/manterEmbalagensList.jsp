<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.embalagemService">
	<adsm:form action="/expedicao/manterEmbalagens" idProperty="idEmbalagem">
		<adsm:textbox dataType="text" property="dsEmbalagem" label="embalagem" maxLength="60" size="23" />
		<adsm:combobox property="blPrecificada" domain="DM_SIM_NAO" label="precificada" />
        <adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" />
        
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="embalagem"/>
			<adsm:resetButton />
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idEmbalagem" property="embalagem" gridHeight="200" defaultOrder="dsEmbalagem" unique="true" rows="13">
		<adsm:gridColumn title="embalagem" property="dsEmbalagem" width="35%" />
		<adsm:gridColumn title="precificada" property="blPrecificada" renderMode="image-check" width="12%" />
		<adsm:gridColumn title="largura" unit="cm" property="nrLargura" dataType="integer" width="11%" />
		<adsm:gridColumn title="altura" unit="cm" property="nrAltura" dataType="integer" width="11%" />
		<adsm:gridColumn title="comprimento" unit="cm" property="nrComprimento" dataType="integer" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="12%" />
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>