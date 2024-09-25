<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm"%>
<adsm:window service="lms.configuracoes.servicoAdicionalService">
	<adsm:form action="/configuracoes/manterServicosAdicionais" idProperty="idServicoAdicional">
		<adsm:textbox dataType="text" property="dsServicoAdicional" label="descricao" size="70" maxLength="60" required="true" width="85%"/>
        <adsm:combobox label="servicoOficial" property="servicoOficialTributo.idServicoOficialTributo" 
           service="lms.tributos.servicoOficialTributoService.find" onlyActiveValues="true"
           optionLabelProperty="nrServicoTributoDsServicoTributo" optionProperty="idServicoOficialTributo"
           required="true" boxWidth="630" width="85%"/>
        <adsm:range label="vigencia" width="85%">
			<adsm:textbox dataType="JTDate" property="dtVigenciaInicial" required="true"/>
			<adsm:textbox dataType="JTDate" property="dtVigenciaFinal"/>
		</adsm:range>
		<adsm:textbox label="codigoEDI" dataType="text" property="cdServicoEDI"  maxLength="3" required="true" size="4" />
		<adsm:buttonBar>

            <adsm:button caption="tipoPrecificacaoServicoAdicional" action="/configuracoes/manterTipoPrecificacaoServicosAdicionais" cmd="main" boxWidth="200" >
            	<adsm:linkProperty src="idServicoAdicional" target="servicoAdicional.idServicoAdicional"/>
            	<adsm:linkProperty src="idServicoAdicional" target="idServicoAdicionalTmp"/>
            </adsm:button>
            <adsm:button caption="composicaoServicoAdicional" action="/configuracoes/manterComposicaoServicosAdicionais" cmd="main" boxWidth="200">
                <adsm:linkProperty src="idServicoAdicional" target="servicoAdicional.idServicoAdicional"/>
                <adsm:linkProperty src="dsServicoAdicional" target="servicoAdicional.dsServicoAdicional"/>
            </adsm:button>
			<adsm:storeButton/>
     		<adsm:newButton/>
			<adsm:removeButton/>
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>