<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window service="lms.entrega.consultarManifestosEntregaAction" >
	<adsm:form action="/entrega/consultarManifestosEntrega" idProperty="idManifestoEntrega" >
        <adsm:hidden property="manifesto.controleCarga.idControleCarga"/>
        <adsm:hidden property="tpAcessoFilial" serializable="true" value="A"/>
        <adsm:lookup dataType="text" 
        		property="filial" idProperty="idFilial" criteriaProperty="sgFilial"  
				service="lms.entrega.consultarManifestosEntregaAction.findLookupFilial"
				action="/municipios/manterFiliais"
				label="filial" labelWidth="17%" width="33%" size="3" maxLength="3" exactMatch="true" >
			<adsm:propertyMapping relatedProperty="filial.pessoa.nmFantasia" modelProperty="pessoa.nmFantasia"/>
			<adsm:propertyMapping criteriaProperty="tpAcessoFilial" modelProperty="tpAcesso" />
			<adsm:textbox dataType="text" property="filial.pessoa.nmFantasia" disabled="true" serializable="false" size="30" />
		</adsm:lookup>

		<adsm:textbox dataType="integer" property="nrManifestoEntrega"
				label="numero" labelWidth="17%" width="33%" maxLength="8" size="8" mask="00000000" />

		<adsm:lookup dataType="integer" property="manifesto.controleCarga.rotaColetaEntrega" 
				idProperty="idRotaColetaEntrega" criteriaProperty="nrRota"
				service="lms.entrega.consultarManifestosEntregaAction.findLookupRotaColetaEntrega"
				action="/municipios/manterRotaColetaEntrega"
				label="rotaColetaEntrega" size="3" maxLength="3" mask="000" labelWidth="17%" width="33%"
				cellStyle="vertical-Align:bottom" >
			<adsm:propertyMapping criteriaProperty="filial.idFilial" modelProperty="filial.idFilial" />
            <adsm:propertyMapping criteriaProperty="filial.sgFilial" modelProperty="filial.sgFilial" inlineQuery="false" />
            <adsm:propertyMapping criteriaProperty="filial.pessoa.nmFantasia"
            		modelProperty="filial.pessoa.nmFantasia" inlineQuery="false" />
            
            <adsm:propertyMapping relatedProperty="rotaColetaEntrega.dsRota" modelProperty="dsRota" />
	        <adsm:textbox dataType="text" property="rotaColetaEntrega.dsRota" size="30" disabled="true" serializable="false" />            
        </adsm:lookup>

<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>
		<adsm:lookup dataType="text"
				property="meioTransporte2" 
				idProperty="idMeioTransporte" criteriaProperty="nrFrota"
				service="lms.entrega.consultarManifestosEntregaAction.findLookupMeioTransporte" picker="false"
				action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list"
				label="meioTransporte" labelWidth="17%"
				width="33%" size="8" maxLength="6" cellStyle="vertical-Align:bottom" serializable="false" >
			<adsm:propertyMapping criteriaProperty="manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador"
					modelProperty="nrIdentificador" />
			<adsm:propertyMapping relatedProperty="manifesto.controleCarga.meioTransporteByIdTransportado.idMeioTransporte"
					modelProperty="idMeioTransporte" />
			<adsm:propertyMapping relatedProperty="manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador"
					modelProperty="nrIdentificador" />
					
			<adsm:lookup dataType="text" property="manifesto.controleCarga.meioTransporteByIdTransportado"
					idProperty="idMeioTransporte" criteriaProperty="nrIdentificador"
					service="lms.entrega.consultarManifestosEntregaAction.findLookupMeioTransporte"
					action="/contratacaoVeiculos/manterMeiosTransporte" cmd="list" 
					picker="true" maxLength="25" size="20" cellStyle="vertical-Align:bottom" >
				<adsm:propertyMapping criteriaProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.idMeioTransporte"
						modelProperty="idMeioTransporte" />
				<adsm:propertyMapping relatedProperty="meioTransporte2.nrFrota"
						modelProperty="nrFrota" />
			</adsm:lookup>
					
		</adsm:lookup>		
<%--Lookup Meio de Transporte--------------------------------------------------------------------------------------------------------------------%>		

		<adsm:range label="periodoEmissao" labelWidth="17%" width="83%" >
			<adsm:textbox dataType="JTDate" property="manifesto.dhEmisssaoManifestoInicial" />
			<adsm:textbox dataType="JTDate" property="manifesto.dhEmisssaoManifestoFinal" />
		</adsm:range>
		
		<adsm:combobox property="manifesto.tpManifestoEntrega" domain="DM_TIPO_MANIFESTO_ENTREGA"
				label="tipoManifesto" labelWidth="17%" width="33%" />
		<adsm:combobox property="manifesto.tpStatusManifesto" domain="DM_STATUS_MANIFESTO"
				label="situacaoManifesto" labelWidth="17%" width="33%" />
		
		<adsm:buttonBar freeLayout="true">
			<adsm:findButton callbackProperty="manifestoEntrega" />
			<adsm:resetButton />
		</adsm:buttonBar> 
	</adsm:form>
	
	<adsm:grid property="manifestoEntrega" idProperty="idManifesto" 
			service="lms.entrega.consultarManifestosEntregaAction.findPaginatedLookup"
			rowCountService="lms.entrega.consultarManifestosEntregaAction.getRowCountLookup"
			selectionMode="none" unique="true" rows="10" >
			
		<adsm:gridColumn title="tipoManifesto" property="manifesto.tpManifestoEntrega" width="100" isDomain="true" />		
		 
		<adsm:gridColumnGroup customSeparator=" " >
			<adsm:gridColumn title="manifesto" property="filial.sgFilial" width="60" />
			<adsm:gridColumn title="" property="nrManifestoEntrega" dataType="integer" mask="00000000" width="30" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="dataEmissao" property="manifesto.dhEmissaoManifesto" width="140" dataType="JTDateTimeZone" />
		
		<adsm:gridColumnGroup customSeparator=" - ">
			<adsm:gridColumn title="rota" property="manifesto.controleCarga.rotaColetaEntrega.nrRota"
					dataType="integer" mask="000" width="100" align="left" />
			<adsm:gridColumn title="" property="manifesto.controleCarga.rotaColetaEntrega.dsRota" width="100" />
		</adsm:gridColumnGroup>

		<adsm:gridColumn title="meioTransporte" property="manifesto.controleCarga.meioTransporteByIdTransportado.nrFrota" width="50" />
		<adsm:gridColumn title="" property="manifesto.controleCarga.meioTransporteByIdTransportado.nrIdentificador" width="60" />

		<adsm:gridColumn title="situacao" property="manifesto.tpStatusManifesto" isDomain="true" />
		
		<adsm:buttonBar>
			<adsm:removeButton />
		</adsm:buttonBar>
	</adsm:grid>
</adsm:window>