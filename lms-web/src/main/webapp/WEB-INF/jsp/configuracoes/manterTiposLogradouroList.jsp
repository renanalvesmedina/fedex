<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.tipoLogradouroService">
	<adsm:form action="/configuracoes/manterTiposLogradouro" idProperty="idTipoLogradouro">

		<adsm:textbox dataType="text" property="dsTipoLogradouro" label="descricao" width="85%" maxLength="60" size="60"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="tipoLogradouro"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idTipoLogradouro" property="tipoLogradouro" defaultOrder="dsTipoLogradouro" rows="13">	
		<adsm:gridColumn width="85%" title="descricao" property="dsTipoLogradouro"/>
		<adsm:gridColumn width="15%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>