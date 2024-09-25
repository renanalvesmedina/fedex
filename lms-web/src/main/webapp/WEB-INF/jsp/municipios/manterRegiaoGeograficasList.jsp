<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.regiaoGeograficaService" >
	<adsm:form action="/municipios/manterRegiaoGeograficas" idProperty="idRegiaoGeografica">
		<adsm:textbox dataType="text" property="dsRegiaoGeografica" label="descricao" size="60" maxLength="60" labelWidth="15%" width="75%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" labelWidth="15%" width="75%"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="regiaoGeografica"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid defaultOrder="dsRegiaoGeografica" selectionMode="check" idProperty="idRegiaoGeografica" property="regiaoGeografica" unique="true" rows="13" >
		<adsm:gridColumn title="descricao" property="dsRegiaoGeografica" width="90%"/>
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="10%"/>
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
