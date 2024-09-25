<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterMotivosTransferenciasAction">
	<adsm:form action="/contasReceber/manterMotivosTransferencias" idProperty="idMotivoTransferencia">
		<adsm:textbox dataType="text" property="dsMotivoTransferencia" size="60" maxLength="60" required="true" label="descricao" width="85%"/>
		<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS" required="true" defaultValue="A"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>