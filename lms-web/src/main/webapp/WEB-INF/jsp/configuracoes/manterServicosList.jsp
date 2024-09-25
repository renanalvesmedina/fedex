<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.servicoService">
	<adsm:form action="/configuracoes/manterServicos" idProperty="idServico">
		<adsm:combobox property="tpModal" label="modal" domain="DM_MODAL"/>
		<adsm:combobox property="tpAbrangencia" label="abrangencia" domain="DM_ABRANGENCIA"/>
		<adsm:combobox property="tipoServico.idTipoServico" label="tipoServico" service="lms.configuracoes.tipoServicoService.find"
			optionLabelProperty="dsTipoServico" optionProperty="idTipoServico" style="width:250px"/>
		<adsm:textbox dataType="text" property="dsServico" label="descricao" size="40" maxLength="60"/>

		<adsm:textbox dataType="text" property="sgServico" label="sigla" size="5" maxLength="5"/>
		<adsm:combobox property="tpSituacao" label="situacao" domain="DM_STATUS"/>
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="servicos"/>
			<adsm:resetButton/>
		</adsm:buttonBar>
	</adsm:form>
	<adsm:grid idProperty="idServico" gridHeight="200" defaultOrder="tpModal, tpAbrangencia, tipoServico_.dsTipoServico, dsServico"
		 property="servicos" rows="12">
		<adsm:gridColumn width="20%" title="modal" property="tpModal" isDomain="true"/>
		<adsm:gridColumn width="15%" title="abrangencia" property="tpAbrangencia" isDomain="true"/>
		<adsm:gridColumn width="15%" title="tipoServico" property="tipoServico.dsTipoServico"/>
		<adsm:gridColumn width="30%" title="descricao" property="dsServico" />
		<adsm:gridColumn width="10%" title="sigla" property="sgServico" />
		<adsm:gridColumn width="10%" title="situacao" property="tpSituacao" isDomain="true"/>
		<adsm:buttonBar> 
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:grid>

</adsm:window>
