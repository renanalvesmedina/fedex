<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>

<adsm:window service="lms.contasreceber.manterAlineasDevolucoesChequesAction">

	<adsm:form action="/contasReceber/manterAlineasDevolucoesCheques" idProperty="idAlinea">
	
        <adsm:textbox dataType="integer" property="cdAlinea" size="2" maxLength="2" width="100%" label="codigo" required="true" minValue="1"/>
        
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="100%" required="true"/>

		<adsm:textarea property="dsAlinea" maxLength="500" required="true" label="descricao" width="100%" columns="80" rows="4"/>

		<adsm:buttonBar>
		
			<adsm:storeButton/>
			
			<adsm:newButton/>
						
			<adsm:removeButton/>	
			
		</adsm:buttonBar>
		
	</adsm:form>

</adsm:window>