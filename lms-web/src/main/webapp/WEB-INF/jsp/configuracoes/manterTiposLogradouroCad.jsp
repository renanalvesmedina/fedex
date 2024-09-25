<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.configuracoes.tipoLogradouroService">
	<adsm:form action="/configuracoes/manterTiposLogradouro" idProperty="idTipoLogradouro">
		<adsm:textbox dataType="text" property="dsTipoLogradouro" label="descricao" width="85%" maxLength="60" size="60" required="true"/>
		<adsm:combobox property="tpSituacao" label="situacao" required="true" domain="DM_STATUS"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>