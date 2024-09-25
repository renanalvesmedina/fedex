<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterGrupoJobAction">
	<adsm:form action="/configuracoes/manterGrupoJob" idProperty="idGrupoJob">
		
		<adsm:textbox property="nmGrupo" dataType="text" 
					  label="nomeGrupo" maxLength="80" size="50" width="85%" required="true"/>
					  
		<adsm:textbox property="dsGrupo" dataType="text" 
					  label="descricao" maxLength="256" size="100" width="85%"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>