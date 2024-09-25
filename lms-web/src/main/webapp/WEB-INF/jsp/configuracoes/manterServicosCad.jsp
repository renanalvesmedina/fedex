<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.servicoService">

	<adsm:form action="/configuracoes/manterServicos" idProperty="idServico">

		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL" required="true"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA" required="true"/>
		<adsm:combobox property="tipoServico.idTipoServico" label="tipoServico" optionLabelProperty="dsTipoServico" optionProperty="idTipoServico" 
			service="lms.configuracoes.tipoServicoService.find" required="true" style="width:250px" onlyActiveValues="true"/>
		<adsm:textbox dataType="text" property="dsServico" label="descricao" size="40" maxLength="60" required="true"/>
		<adsm:textbox dataType="text" property="sgServico" label="sigla" size="5" maxLength="5" required="true"/>
		<adsm:checkbox property="blGeraMcd" label="geraMCD"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS" required="true"/>

		<adsm:buttonBar>
			<adsm:storeButton/>
			<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>
