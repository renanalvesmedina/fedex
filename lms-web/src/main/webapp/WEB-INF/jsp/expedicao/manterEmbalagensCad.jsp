<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.expedicao.embalagemService">
	<adsm:form action="/expedicao/manterEmbalagens" idProperty="idEmbalagem">
		<adsm:textbox dataType="text" property="dsEmbalagem" label="embalagem" maxLength="60" size="23" required="true"/>
		<adsm:checkbox  property="blPrecificada" label="precificada" />
		<adsm:textbox unit="cm" dataType="integer" minValue="1" property="nrLargura" label="largura" maxLength="5" size="5" required="true"/>
		<adsm:textbox unit="cm" dataType="integer" minValue="1"  property="nrAltura" label="altura" maxLength="5" size="5" required="true"/>
		<adsm:textbox unit="cm" dataType="integer" minValue="1" property="nrComprimento" label="comprimento" maxLength="5" size="5" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>
		<adsm:buttonBar>
			<adsm:storeButton />
			<adsm:newButton />
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
