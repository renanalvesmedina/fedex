<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.manterGrupoJobAction">
	<adsm:form action="/configuracoes/manterGrupoJob">

		<adsm:textbox property="nmGrupo" dataType="text" 
					  label="nomeGrupo" maxLength="80" size="50" width="85%"/>

		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="grupoJob"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>

	<adsm:grid property="grupoJob" idProperty="idGrupoJob">	
		<adsm:gridColumn property="nmGrupo" title="nome" width="50%" />
		<adsm:gridColumn property="dsGrupo" title="descricao" width="50%" />
        <adsm:buttonBar> 
		   <adsm:removeButton/>
    	</adsm:buttonBar>
	</adsm:grid>

</adsm:window>