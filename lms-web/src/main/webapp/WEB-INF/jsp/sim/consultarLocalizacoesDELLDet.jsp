<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<adsm:window>
	<adsm:form action="/entrega/consultarManifestosEntrega" height="390">
		<adsm:section caption="localizacaoMercadorias"/>
			<adsm:textbox dataType="text" size="25" property="status" label="statusAtual" labelWidth="21%" width="29%" disabled="true"/>

			<adsm:textbox dataType="text" property="para" label="para" maxLength="50" disabled="true" size="10" labelWidth="21%" width="11%" />
			<adsm:textbox dataType="text" property="paraNome" size="17" maxLength="50" disabled="true" width="18%"/>

			<adsm:textbox dataType="date" size="25" property="previsaoEntrega" label="previsaoEntrega" labelWidth="21%" width="29%" disabled="true"/>
			<adsm:textbox dataType="date" size="25" property="dataEntrega" label="dataEntrega" labelWidth="21%" width="29%" disabled="true"/>	
			<adsm:textbox dataType="text" size="75" property="motivo" label="motivoNaoEntrega" labelWidth="21%" width="79%" disabled="true"/>
		<adsm:section caption="dadosDocumentoServico"/>
			<adsm:textbox dataType="text" size="25" property="TIPO" label="tipoDocumento" labelWidth="21%" width="29%" disabled="true"/>
			<adsm:textbox label="documentoServico" labelWidth="21%" width="8%" size="6" maxLength="3" dataType="text" property="DOCUMENTOSERVICO" disabled="true" />
	        <adsm:textbox width="10%" size="10" dataType="text" property="numeroDocumento" disabled="true" />
	        <adsm:label key="hifen" width="2%" style="border:none;text-align:center"/>
	        <adsm:textbox dataType="text" width="9%" size="5" property="numeroDocumentoCompl" style="width:15px;" disabled="true" />

			<adsm:textbox dataType="date" size="25" property="dataEmissao" label="dataEmissao" labelWidth="21%" width="29%" disabled="true"/>	
			<adsm:textbox dataType="text" size="25" property="notaFiscal" label="notaFiscal" labelWidth="21%" width="29%" disabled="true"/>
			<adsm:textbox dataType="text" size="25" property="destino" label="destino" labelWidth="21%" width="29%" disabled="true"/>	

			<adsm:textbox dataType="text" property="remetente" label="remetente" maxLength="50" disabled="true" size="10" labelWidth="21%" width="11%" />
			<adsm:textbox dataType="text" property="remetenteNome" size="17" maxLength="50" disabled="true" width="18%"/>
	
			<adsm:textbox dataType="text" property="destinatario" label="destinatario" maxLength="50" disabled="true" size="10" labelWidth="21%" width="11%" />
			<adsm:textbox dataType="text" property="destinatarioNome" size="17" maxLength="50" disabled="true" width="68%"/>

			<adsm:textbox dataType="text" size="25" property="paisDestino" label="paisDestino" labelWidth="21%" width="29%" disabled="true"/>
			<adsm:textbox dataType="text" size="25" property="ufDestino" label="ufDestino" labelWidth="21%" width="29%" disabled="true"/>
			<adsm:textbox dataType="text" size="25" property="municipioDestino" label="municipioDestino" labelWidth="21%" width="79%" disabled="true"/>
			<adsm:textbox dataType="text" size="75" property="enderecoEntrega" label="enderecoEntrega" labelWidth="21%" width="79%" disabled="true"/>

			<adsm:textbox dataType="text" size="25" property="volume" label="volume" labelWidth="21%" width="29%" disabled="true"/>
			<adsm:textbox dataType="text" size="25" property="peso" label="peso" labelWidth="21%" width="29%" disabled="true" unit="kg"/>
			<adsm:textbox dataType="text" size="25" property="valorMercadoria" label="valorMercadoria" labelWidth="21%" width="29%" disabled="true"/>

		<adsm:section caption="notasFiscaisConhecimento"/>
			<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" rows="7">
				<adsm:gridColumn width="20%" title="notaFiscal" property="nota" align="left"/>
				<adsm:gridColumn width="20%" title="dataEmissao" property="emissao" align="center"/>
				<adsm:gridColumn width="20%" title="volumes" property="volume" align="right"/>
				<adsm:gridColumn width="15%" title="peso" property="peso" align="right" unit="kg"/>
				<adsm:gridColumn width="25%" title="valorMercadoria" property="valor" align="right"/>
			</adsm:grid>
		<adsm:section caption="eventosTransporte"/>
			<adsm:grid paramId="id" paramProperty="id" showCheckbox="false" unique="false" rows="7" scrollBars="horizontal">
				<adsm:gridColumn width="150" title="tipoDocumento" property="tipoD"/>
				<adsm:gridColumn width="150" title="numeroDocumento" property="numeroD"/>
				<adsm:gridColumn width="150" title="eventoPrevisto" property="eventoP"/>
				<adsm:gridColumn width="150" title="dataHoraPrevista" property="dataP" align="center"/>
				<adsm:gridColumn width="150" title="eventoRealizado" property="eventoR"/>
				<adsm:gridColumn width="150" title="dataHoraRealizado" property="dataR" align="center"/>
				<adsm:gridColumn width="150" title="ocorrencia" property="ocorrencia"/>
			</adsm:grid>
	</adsm:form>
	<adsm:buttonBar>
		<adsm:button caption="contatoFilialOrigem"  onClick="showModal:/sim/consultarLocalizacaoDetalhadaContatos.do?cmd=main"/>
		<adsm:button caption="contatoFilialDestino" onClick="showModal:/sim/consultarLocalizacaoDetalhadaContatos.do?cmd=main"/>
		<adsm:button caption="voltar"/>
	</adsm:buttonBar>
</adsm:window>