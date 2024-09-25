<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.carregamento.cargoOperacionalService" >
	<adsm:form action="/carregamento/manterCargosOperacionais" idProperty="idCargoOperacional" >
		<adsm:textbox dataType="text" property="dsCargo" label="nomeCargo" maxLength="60" size="60" width="85%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>