<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.municipios.regiaoGeograficaService" >
	<adsm:form idProperty="idRegiaoGeografica" action="/municipios/manterRegiaoGeograficas">
		<adsm:textbox dataType="text" required="true" property="dsRegiaoGeografica" label="descricao" maxLength="60" size="60" width="85%"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" required="true" label="situacao"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>