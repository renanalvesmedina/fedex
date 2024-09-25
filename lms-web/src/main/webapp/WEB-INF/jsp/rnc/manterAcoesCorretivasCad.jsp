<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.rnc.acaoCorretivaService" >
	<adsm:form action="/rnc/manterAcoesCorretivas" idProperty="idAcaoCorretiva">
		<adsm:textbox dataType="text" property="dsAcaoCorretiva" label="descricao" size="60" maxLength="60" width="85%" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" width="85%" required="true" renderOptions="true"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>			
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>