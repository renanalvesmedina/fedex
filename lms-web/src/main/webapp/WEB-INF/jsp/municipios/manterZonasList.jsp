<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.zonaService">
	<adsm:form action="/municipios/manterZonas" idProperty="idZona">
		<adsm:textbox dataType="text" property="dsZona" label="descricao" maxLength="60" size="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="zona"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idZona" property="zona" selectionMode="check"  gridHeight="200" unique="true" defaultOrder="dsZona" rows="13">
		<adsm:gridColumn title="descricao" property="dsZona" width="90%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true" />
		<adsm:buttonBar>
			<adsm:removeButton/> 
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>
