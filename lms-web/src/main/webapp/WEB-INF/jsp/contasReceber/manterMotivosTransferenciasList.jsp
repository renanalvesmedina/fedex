<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.contasreceber.manterMotivosTransferenciasAction">
	<adsm:form action="/contasReceber/manterMotivosTransferencias" >

	<adsm:textbox dataType="text" property="dsMotivoTransferencia" size="60" maxLength="60" label="descricao" width="85%"/>
	<adsm:combobox label="situacao" property="tpSituacao" domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="motivoTransferencia"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid idProperty="idMotivoTransferencia" property="motivoTransferencia" defaultOrder="dsMotivoTransferencia" rows="13">
		<adsm:gridColumn title="descricao" property="dsMotivoTransferencia" width="80%" />
		<adsm:gridColumn title="situacao" property="tpSituacao" isDomain="true" width="20%" />
		<adsm:buttonBar>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>