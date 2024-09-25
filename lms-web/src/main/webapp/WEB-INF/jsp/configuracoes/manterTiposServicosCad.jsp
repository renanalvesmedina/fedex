<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.tipoServicoService">
	<adsm:form action="/configuracoes/manterTiposServicos" idProperty="idTipoServico">
		<adsm:textbox property="dsTipoServico" dataType="text" label="descricao" size="40" maxLength="60" required="true"/>
		<adsm:checkbox property="blPriorizar" label="priorizar"/>
		<adsm:combobox property="tpSituacao" domain="DM_STATUS" label="situacao" required="true" width="85%"/>
		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>