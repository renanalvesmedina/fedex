<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.seguros.situacaoReembolsoService">

	<adsm:form action="/seguros/manterSituacoesReembolso" idProperty="idSituacaoReembolso" height="390">
	
		<adsm:textbox dataType="text" property="dsSituacaoReembolso" label="descricao" maxLength="20" size="80" width="85%" required="true"/>
	
		<adsm:combobox property="blStatus" label="status" domain="DM_STATUS" width="85%" renderOptions="true" required="true" />
			
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	
	</adsm:form>

</adsm:window>