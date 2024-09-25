<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window service="lms.contasreceber.manterMotivosDescontosAction">
	<adsm:form action="/contasReceber/manterMotivosDescontos" idProperty="idMotivoDesconto">

		<adsm:textbox 	dataType="text" 
						property="dsMotivoDesconto" 
						required="true" 
						size="70" 
						maxLength="60" 
						width="85%" 
						label="descricao"/>
		
		<adsm:textbox 	dataType="text" 
						property="cdMotivoDesconto" 
						size="3" 
						maxLength="3" 
						labelWidth="15%" 
						required="true"
						width="15%" 
						label="codigo"/>
						
		<adsm:combobox 	property="tpMotivoDesconto" 
						label="setorCausadorAbatimento" 
						labelWidth="22%"
						width="38%"
						domain="DM_SETOR_CAUSADOR" 
						required="true"/>
		
		<adsm:combobox 	property="tpSituacao" 
						label="situacao" 
						domain="DM_STATUS" 
						required="true"/>

		<adsm:buttonBar>
				<adsm:storeButton/>
				<adsm:newButton/>
				<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>