<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
	
<adsm:window service="lms.contasreceber.manterAlineasDevolucoesChequesAction">

	<adsm:form action="/contasReceber/manterAlineasDevolucoesCheques">
	
        <adsm:textbox dataType="integer" property="cdAlinea" size="2" maxLength="2" width="30%" label="codigo" minValue="1"/>
        
		<adsm:textbox dataType="text" property="dsAlinea" size="50" maxLength="50" width="40%" label="descricao"/>
		
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>

		<adsm:buttonBar freeLayout="true">
		
			<adsm:findButton callbackProperty="alinea"/>
			
			<adsm:resetButton/>
			
		</adsm:buttonBar>        
		
	</adsm:form>

	<adsm:grid property="alinea"
			   idProperty="idAlinea"  
			   gridHeight="200"  
	           rows="13"
	           defaultOrder="dsAlinea">
	
		<adsm:gridColumn title="descricao" property="dsAlinea" width="80%" />
		
		<adsm:gridColumn title="codigo" property="cdAlinea" width="10%" align="right"/>
		
		<adsm:gridColumn title="situacao" property="tpSituacao" width="10%" isDomain="true"/>
        
		<adsm:buttonBar>
		
			<adsm:removeButton/>
			
		</adsm:buttonBar>
		
	</adsm:grid>

</adsm:window>