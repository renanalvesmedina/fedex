<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %> 
<adsm:window>
	<adsm:form action="/entrega/consultarManifestosEntrega" height="390">
		<adsm:textbox dataType="text" size="10" property="PREVISAO" label="entregaPrevista" labelWidth="21%" width="70%" disabled="true"/>
		<adsm:textbox dataType="text" size="10" property="ENTRAGA" label="entregaRealizada" labelWidth="21%" width="70%" disabled="true"/>
		<adsm:textbox dataType="text" property="motivoNaoEntrega" label="motivoNaoEntrega" size="25" labelWidth="21%" width="70%" disabled="true"/>
		<adsm:textbox dataType="text" property="localizacaoMercadoria" label="localizacaoMercadoria" size="25" labelWidth="21%" width="70%" disabled="true" />
		<adsm:section caption="dadosDocumentoServico"/>

		<adsm:combobox property="servico" label="servico" prototypeValue="" service="" optionLabelProperty="" optionProperty="" labelWidth="21%" width="79%" cellStyle="vertical-align:bottom;" disabled="true" />	


		<adsm:combobox property="tpDocumentoServico" label="tipoDocumentoServico" prototypeValue="CTRC|CRT|NFS|MDA|RRE" service="" optionLabelProperty="" optionProperty="" labelWidth="21%" width="79%" cellStyle="vertical-align:bottom;" disabled="true" />	

		<adsm:textbox label="numero" labelWidth="21%" width="8%" size="6" maxLength="3" dataType="text" property="filialOrigem" disabled="true"/>
        <adsm:textbox width="10%" size="10" dataType="text" property="numeroDocumento" disabled="true"/>
        <adsm:label key="hifen" width="2%" style="border:none;text-align:center"/>
        <adsm:textbox dataType="text" property="numeroDocumentoCompl"  width="57%" size="5" style="width: 15px;" disabled="true"/>


		<adsm:textbox dataType="text" property="emissao" label="emissao" size="25" labelWidth="21%" width="29%" disabled="true" />
		<adsm:textbox dataType="text" property="notaFiscal" label="notaFiscal" size="25" labelWidth="21%" width="29%" disabled="true" />
		<adsm:textbox dataType="text" property="filialDestino" label="filialDestino" size="25" labelWidth="21%" width="79%" disabled="true" />

		<adsm:textbox label="remetente" labelWidth="21%" width="11%" size="10" maxLength="3" dataType="text" property="filialOrigem" disabled="true"/>
        <adsm:textbox width="60%" size="30" dataType="text" property="numeroDocumento" disabled="true"/>
		<adsm:textbox label="destinatario" labelWidth="21%" width="11%" size="10" maxLength="3" dataType="text" property="filialOrigem" disabled="true" />
        <adsm:textbox width="60%" size="30" dataType="text" property="numeroDocumento" disabled="true"/>

		<adsm:textbox dataType="text" property="municipioDestino" label="municipioDestino" size="25" labelWidth="21%" width="70%" disabled="true" />
		<adsm:textbox dataType="text" property="enderecoEntrega" label="enderecoEntrega" size="25" labelWidth="21%" width="70%" disabled="true" />
		<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" rows="9">
			<adsm:gridColumn width="20%" title="notaFiscal" property="NOTA" align="left" />
			<adsm:gridColumn width="20%" title="dataEmissao" property="EMISSAO" align="center"/>
			<adsm:gridColumn width="20%" title="volumes" property="VOLUMES" align="right"/>
			<adsm:gridColumn width="20%" title="peso" property="PESO" align="right" unit="kg"/>
		</adsm:grid>
	</adsm:form>
		<adsm:buttonBar>
			<adsm:button caption="contatoFilialOrigem"  onClick="showModal:/sim/consultarLocalizacaoDetalhadaContatos.do?cmd=main"/>
			<adsm:button caption="contatoFilialDestino" onClick="showModal:/sim/consultarLocalizacaoDetalhadaContatos.do?cmd=main"/>
			<adsm:button caption="voltar"/>
		</adsm:buttonBar>
</adsm:window>