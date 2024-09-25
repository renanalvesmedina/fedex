<%@ taglib uri="/WEB-INF/adsm.tld" prefix="adsm" %>
<script>
function carregaDadosPagina_cb(data, error) {

	onDataLoad_cb(data);
	if(getElementValue("btnPreAlertaDisabled") == "true")
		setDisabled("pedidoColetaButton", true);
	else
		setDisabled("pedidoColetaButton", false);

	if(getElementValue("btnsAwbDisabled") == "true") {
		setDisabled("btnCtrcs", true);
		setDisabled("btnDimensoes", true);
		setDisabled("btnNotasFiscais", true);
	} else {
		setDisabled("btnCtrcs", false);
		setDisabled("btnDimensoes", false);
		setDisabled("btnNotasFiscais", false);
	}
	
	var tpLocalizacao = getElementValue("awb.tpLocalizacao"); 
	var vlTpStatusAwb = getElementValue("vlTpStatusAwb");
	if((tpLocalizacao !== null || tpLocalizacao !== '') && tpLocalizacao === "AE" && vlTpStatusAwb === "E") {
		setDisabled("btnCancelar", false);		
	} else {
		setDisabled("btnCancelar", true);		
	}	
}

</script>
<adsm:window
	service="lms.expedicao.consultarAWBsAction">
	
	<adsm:form 
		action="/expedicao/consultarAWBs" 
		idProperty="idAwb"
		height="370"
		onDataLoadCallBack="carregaDadosPagina">

		<adsm:i18nLabels>
			<adsm:include key="LMS-04100"/>
			<adsm:include key="LMS-04101"/>
			<adsm:include key="LMS-04195"/>
		</adsm:i18nLabels>
		
		<adsm:hidden property="btnPreAlertaDisabled" />
		<adsm:hidden property="btnsAwbDisabled" />
		
		<%-----------------------------------------%>
		<%-- UTILIZADO PELA POPUP DE OBSERVACOES --%>
		<%-----------------------------------------%>
		<adsm:hidden property="obAwb"/>
		
		<%----------------------------%>
		<%-- UTILIZADO PELAS POPUPS --%>
		<%----------------------------%>
		<adsm:hidden property="awb.idAwb" />
		
		<%----------------------------------------------%>
		<%-- UTILIZADO PELA POPUP DE CALCULO DE FRETE --%>
		<%----------------------------------------------%>
		<adsm:hidden property="vlFretePeso" />
		<adsm:hidden property="vlTaxaTerrestre" />
		<adsm:hidden property="vlTaxaCombustivel" />
		<adsm:hidden property="pcAliquotaICMS" />
		<adsm:hidden property="vlICMS" />
		
		<%--------------------------------------%>
		<%-- UTILIZADO PELA TELA DE HISTORICO --%>
		<%--------------------------------------%>
		<adsm:hidden property="vlTpStatusAwb" />

		<%------------------------------%>
		<%-- CIA FILIAL MERCURIO TEXT --%>
		<%------------------------------%>
        <adsm:textbox 
        	dataType="text" 
        	property="ciaFilialMercurio.empresa.pessoa.nmPessoa" 
        	size="23" 
        	label="ciaAerea"
        	maxLength="30" 
        	labelWidth="21%" 
        	width="30%"
        	disabled="true"/>
        	
        <%-----------------%>
		<%-- NR AWB TEXT --%>
		<%-----------------%>
		<adsm:textbox 
			dataType="text" 
			label="numeroAWB" 
			property="nrAwb" 
			size="20" 
			maxLength="20" 
			disabled="true"
			labelWidth="18%" 
			width="31%" >
		</adsm:textbox>
		
		<%---------------------------------%>
		<%-- AEROPORTO ORIGEM COMPLEMENT --%>
		<%---------------------------------%>
		<adsm:complement
			label="aeroportoDeOrigem"
			labelWidth="21%" 
			width="30%">
			
			<adsm:textbox 
				dataType="text"
				property="aeroportoByIdAeroportoOrigem.sgAeroporto"
				size="3"
				maxLength="3"
				disabled="true"/>
			
			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoOrigem.pessoa.nmPessoa"
				size="25"
				maxLength="45"
				disabled="true"/>
				
		</adsm:complement>
		
		<%----------------------------------%>
		<%-- AEROPORTO DESTINO COMPLEMENT --%>
		<%----------------------------------%>
		<adsm:complement
			label="aeroportoDestino"
			labelWidth="18%" 
			width="31%">
			
			<adsm:textbox 
				dataType="text"
				property="aeroportoByIdAeroportoDestino.sgAeroporto"
				size="3"
				maxLength="3"
				disabled="true"/>
			
			<adsm:textbox
				dataType="text"
				property="aeroportoByIdAeroportoDestino.pessoa.nmPessoa"
				size="25"
				maxLength="45"
				disabled="true"/>
				
		</adsm:complement>
		
		<%---------------------------------%>
		<%-- FILIAL ORIGEM COMPLEMENT --%>
		<%---------------------------------%>
		<adsm:complement
			label="filialOrigem2"
			labelWidth="21%" 
			width="30%">
			
			<adsm:textbox 
				dataType="text"
				property="filialByIdFilialOrigem.sgFilial"
				size="3"
				maxLength="3"
				disabled="true"/>
			
			<adsm:textbox
				dataType="text"
				property="filialByIdFilialOrigem.pessoa.nmFantasia"
				size="25"
				maxLength="45"
				disabled="true"/>
				
		</adsm:complement>
		
		<%-------------------------------%>
		<%-- FILIAL DESTINO COMPLEMENT --%>
		<%-------------------------------%>
		<adsm:complement
			label="filialDestino"
			labelWidth="18%" 
			width="31%">
			
			<adsm:textbox 
				dataType="text"
				property="filialByIdFilialDestino.sgFilial"
				size="3"
				maxLength="3"
				disabled="true"/>
			
			<adsm:textbox
				dataType="text"
				property="filialByIdFilialDestino.pessoa.nmFantasia"
				size="25"
				maxLength="45"
				disabled="true"/>
				
		</adsm:complement>
		
		<%-----------------------------%>
		<%-- NR PRESTACAO CONTA TEXT --%>
		<%-----------------------------%>
		<adsm:textbox 
			dataType="text"
			property="prestacaoConta.nrPrestacaoConta"
			size="15"
			maxLength="15"
			disabled="true"
			label="numeroPrestacaoContas"
			labelWidth="21%"
			width="30%"/>
		
		<adsm:textbox 
			dataType="text"
			property="dsTpLocalizacao"
			size="34"
			maxLength="34"
			disabled="true"
			label="localizacao"
			labelWidth="18%"
			width="31%"/>
		
		<%-----------------------%>
		<%-- DH DIGITACAO TEXT --%>
		<%-----------------------%>
		<adsm:textbox 
			property="dhDigitacao" 
			label="dataHoraDigitacao" 
			dataType="JTDateTimeZone" 
			labelWidth="21%" 
			width="30%" 
			disabled="true"
			picker="false"/>
			
		<adsm:textbox 
			dataType="text"
			property="nmUsuarioInclusao"
			size="34"
			maxLength="34"
			disabled="true"
			label="usuarioInclusao"
			labelWidth="18%"
			width="31%"/>		
		
		<%---------------------%>
		<%-- DH EMISSAO TEXT --%>
		<%---------------------%>
		<adsm:textbox 
			property="dhEmissao" 
			label="dataHoraDeEmissao" 
			dataType="JTDateTimeZone" 
			labelWidth="21%" 
			width="30%"
			disabled="true"
			picker="false"/>
		
		<%------------------------%>
		<%-- LOCAL EMISSAO TEXT --%>
		<%------------------------%>
		<adsm:textbox 
			property="tpLocalEmissao" 
			label="localEmissao" 
			dataType="text" 
			labelWidth="18%"
			width="31%"
			disabled="true"/>
			
		<%------------------------%>
		<%-- CONFERIDO CHECK --%>
		<%------------------------%>
		<adsm:checkbox 
			property="blConferido" 
			label="conferido" 
			labelWidth="21%" 
			width="30%"
			disabled="true"/>
			
		<%-- Finalidade TEXT --%>
		<adsm:textbox 
			property="tpAwb" 
			label="finalidade" 
			dataType="text" 
			labelWidth="18%"
			width="31%"
			disabled="true"/>

		<%-------------------%>
		<%-- SITUACAO TEXT --%>
		<%-------------------%>
		<adsm:textbox 
			dataType="text"
			property="tpStatusAwb" 
			label="situacao" 
			labelWidth="21%" 
			width="30%" 
			disabled="true"/>
		
		<%-------------------------------%>
		<%-- VALOR DO FRETE COMPLEMENT --%>
		<%-------------------------------%>
		<adsm:complement 
			label="valorFrete"
			labelWidth="18%"
			width="31%">
			
			<adsm:textbox 
				dataType="text"
				property="moeda.siglaDescricao"
				size="7"
				disabled="true"/>
			
			<adsm:textbox 
				property="vlFrete" 
				dataType="currency" 
				disabled="true" 
				size="15" 
				maxLength="18" />
				
		</adsm:complement>
			
		<%-------------------------%>
		<%-- NR AWB COMPLEMENTADO --%>
		<%-------------------------%>		
		<adsm:textbox 
			dataType="text" 
			label="nrAwbComplementado" 
			property="numeroAWBComplementado" 
			size="20" 
			maxLength="20" 
			disabled="true"
			labelWidth="21%" 
			width="30%" >
		</adsm:textbox>
					
		<%---------------------------------------%>
		<%-- CODIGO LIBERACAO AWB COMPLEMENTAR --%>
		<%---------------------------------------%>
		<adsm:textbox 
			labelWidth="18%" 
			width="31%" 
			maxLength="8"
			dataType="text" 
			property="cdLiberacaoAWBCompl" 
			label="codigoLiberacao"
			disabled="true" />
			
		<adsm:textbox 
			property="dhCancelamento" 
			label="dataHoraCancelamento" 
			dataType="JTDateTimeZone" 
			labelWidth="21%" 
			width="30%" 
			disabled="true"
			picker="false"/>
			
		<adsm:textbox 
			dataType="text"
			property="nmUsuarioCancelamento"
			size="34"
			maxLength="34"
			disabled="true"
			label="usuarioCancelamento"
			labelWidth="18%"
			width="31%"/>
		
		<%--------------------------------%>
		<%-- DADOS DO REMETENTE SECTION --%>
		<%--------------------------------%>
		<adsm:section caption="dadosRemetente"/>
		
		<adsm:complement 
			label="remetente"
			labelWidth="21%"
			width="45%">
			
			<adsm:textbox 
				dataType="text"
				property="clienteByIdClienteExpedidor.pessoa.nrIdentificacao"
				size="20"
				disabled="true" />
				
			<adsm:textbox 
				dataType="text"
				property="clienteByIdClienteExpedidor.pessoa.nmPessoa"
				size="30"
				disabled="true"/>			
			
		</adsm:complement>
		
		<%----------------------------------------%>
		<%-- NRINSCRICAOESTADUAL EXPEDIDOR TEXT --%>
		<%----------------------------------------%>
		<adsm:textbox
			dataType="text"
			size="20"
			maxLength="20"
			property="inscricaoEstadualExpedidor.nrInscricaoEstadual"
			label="ie"
			width="19%"
			labelWidth="13%"
			disabled="true"/>
		
		<%--------------------------------%>
		<%-- DS ENDERECO EXPEDIDOR TEXT --%>
		<%--------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteExpedidor.pessoa.endereco.dsEndereco" 
			label="endereco" 
			dataType="text" 
			size="33" 
			maxLength="100" 
			labelWidth="21%" 
			width="28%" 
			disabled="true"/>
		
		<%--------------------------------%>
		<%-- NR ENDERECO EXPEDIDOR TEXT --%>
		<%--------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteExpedidor.pessoa.endereco.nrEndereco" 
			label="numero" 
			dataType="text" 
			size="5" 
			maxLength="6" 
			labelWidth="10%" 
			width="10%" 
			disabled="true"/>
			
		<%-----------------------------------%>
		<%-- DS COMPLEMENTO EXPEDIDOR TEXT --%>
		<%-----------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteExpedidor.pessoa.endereco.dsComplemento" 
			label="complemento" 
			dataType="text" 
			size="15" 
			maxLength="40" 
			labelWidth="13%" 
			width="18%" 
			disabled="true"/>

		<%-----------------------------------%>
		<%-- DS COMPLEMENTO EXPEDIDOR TEXT --%>
		<%-----------------------------------%>		
		<adsm:textbox 
			property="clienteByIdClienteExpedidor.pessoa.endereco.nmMunicipio" 
			label="municipio" 
			dataType="text" 
			maxLength="50" 
			disabled="true" 
			labelWidth="21%" 
			width="28%" />
		
		<%-----------------------%>
		<%-- UF EXPEDIDOR TEXT --%>
		<%-----------------------%>	
		<adsm:textbox
			dataType="text"
			size="5"
			property="clienteByIdClienteExpedidor.pessoa.endereco.sgUnidadeFederativa" 
			label="uf" 
			disabled="true" 
			labelWidth="10%" 
			width="10%"/>
		
		<%---------------------------%>
		<%-- NR CEP EXPEDIDOR TEXT --%>
		<%---------------------------%>	
		<adsm:textbox 
			property="clienteByIdClienteExpedidor.pessoa.endereco.nrCep" 
			label="cep" 
			dataType="text" 
			maxLength="50" 
			size="10" 
			disabled="true" 
			labelWidth="13%" 
			width="18%"/>
			
		<%-----------------------------------%>
		<%-- DADOS DO DESTINATARIO SECTION --%>
		<%-----------------------------------%>
		<adsm:section caption="dadosDestinatario"/>
		
		<adsm:complement 
			label="destinatario"
			labelWidth="21%"
			width="45%">
			
			<adsm:textbox 
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nrIdentificacao"
				size="20"
				disabled="true" />
				
			<adsm:textbox 
				dataType="text"
				property="clienteByIdClienteDestinatario.pessoa.nmPessoa"
				size="30"
				disabled="true"/>			
			
		</adsm:complement>
		
		<%-------------------------------------------%>
		<%-- NRINSCRICAOESTADUAL DESTINATARIO TEXT --%>
		<%-------------------------------------------%>
		<adsm:textbox
			dataType="text"
			size="20"
			maxLength="20"
			property="inscricaoEstadualDestinatario.nrInscricaoEstadual"
			label="ie"
			width="19%"
			labelWidth="13%"
			disabled="true"/>
		
		<%-----------------------------------%>
		<%-- DS ENDERECO DESTINATARIO TEXT --%>
		<%-----------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteDestinatario.pessoa.endereco.dsEndereco" 
			label="endereco" 
			dataType="text" 
			size="33" 
			maxLength="100" 
			labelWidth="21%" 
			width="28%" 
			disabled="true"/>
		
		<%-----------------------------------%>
		<%-- NR ENDERECO DESTINATARIO TEXT --%>
		<%-----------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteDestinatario.pessoa.endereco.nrEndereco" 
			label="numero" 
			dataType="text" 
			size="5" 
			maxLength="6" 
			labelWidth="10%" 
			width="10%" 
			disabled="true"/>
			
		<%--------------------------------------%>
		<%-- DS COMPLEMENTO DESTINATARIO TEXT --%>
		<%--------------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteDestinatario.pessoa.endereco.dsComplemento" 
			label="complemento" 
			dataType="text" 
			size="15" 
			maxLength="40" 
			labelWidth="13%" 
			width="18%" 
			disabled="true"/>

		<%--------------------------------------%>
		<%-- DS COMPLEMENTO DESTINATARIO TEXT --%>
		<%--------------------------------------%>		
		<adsm:textbox 
			property="clienteByIdClienteDestinatario.pessoa.endereco.nmMunicipio" 
			label="municipio" 
			dataType="text" 
			maxLength="50" 
			disabled="true" 
			labelWidth="21%" 
			width="28%" />
		
		<%--------------------------%>
		<%-- UF DESTINATARIO TEXT --%>
		<%--------------------------%>	
		<adsm:textbox
			dataType="text"
			size="5"
			property="clienteByIdClienteDestinatario.pessoa.endereco.sgUnidadeFederativa" 
			label="uf" 
			disabled="true" 
			labelWidth="10%" 
			width="10%"/>
		
		<%------------------------------%>
		<%-- NR CEP DESTINATARIO TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteDestinatario.pessoa.endereco.nrCep" 
			label="cep" 
			dataType="text" 
			maxLength="50" 
			size="10" 
			disabled="true" 
			labelWidth="13%" 
			width="18%"/>
		
		<%------------------------------%>
		<%-- DADOS DO TOMADOR SECTION --%>
		<%------------------------------%>
		<adsm:section caption="dadosTomadorServico"/>
		
		<adsm:complement 
			label="tomador"
			labelWidth="21%"
			width="45%">
			
			<adsm:textbox 
				dataType="text"
				property="clienteByIdClienteTomador.pessoa.nrIdentificacao"
				size="20"
				disabled="true" />
				
			<adsm:textbox 
				dataType="text"
				property="clienteByIdClienteTomador.pessoa.nmPessoa"
				size="30"
				disabled="true"/>			
			
		</adsm:complement>
		
		<%--------------------------------------%>
		<%-- NRINSCRICAOESTADUAL TOMADOR TEXT --%>
		<%--------------------------------------%>
		<adsm:textbox
			dataType="text"
			size="20"
			maxLength="20"
			property="inscricaoEstadualTomador.nrInscricaoEstadual"
			label="ie"
			width="19%"
			labelWidth="13%"
			disabled="true"/>
		
		<%------------------------------%>
		<%-- DS ENDERECO TOMADOR TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteTomador.pessoa.endereco.dsEndereco" 
			label="endereco" 
			dataType="text" 
			size="33" 
			maxLength="100" 
			labelWidth="21%" 
			width="28%" 
			disabled="true"/>
		
		<%------------------------------%>
		<%-- NR ENDERECO TOMADOR TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteTomador.pessoa.endereco.nrEndereco" 
			label="numero" 
			dataType="text" 
			size="5" 
			maxLength="6" 
			labelWidth="10%" 
			width="10%" 
			disabled="true"/>
			
		<%---------------------------------%>
		<%-- DS COMPLEMENTO TOMADOR TEXT --%>
		<%---------------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteTomador.pessoa.endereco.dsComplemento" 
			label="complemento" 
			dataType="text" 
			size="15" 
			maxLength="40" 
			labelWidth="13%" 
			width="18%" 
			disabled="true"/>

		<%---------------------------------%>
		<%-- DS COMPLEMENTO TOMADOR TEXT --%>
		<%---------------------------------%>		
		<adsm:textbox 
			property="clienteByIdClienteTomador.pessoa.endereco.nmMunicipio" 
			label="municipio" 
			dataType="text" 
			maxLength="50" 
			disabled="true" 
			labelWidth="21%" 
			width="28%" />
		
		<%---------------------%>
		<%-- UF TOMADOR TEXT --%>
		<%---------------------%>	
		<adsm:textbox
			dataType="text"
			size="5"
			property="clienteByIdClienteTomador.pessoa.endereco.sgUnidadeFederativa" 
			label="uf" 
			disabled="true" 
			labelWidth="10%" 
			width="10%"/>
		
		<%-------------------------%>
		<%-- NR CEP TOMADOR TEXT --%>
		<%-------------------------%>
		<adsm:textbox 
			property="clienteByIdClienteTomador.pessoa.endereco.nrCep" 
			label="cep" 
			dataType="text" 
			maxLength="50" 
			size="10" 
			disabled="true" 
			labelWidth="13%" 
			width="18%"/>
			
		<adsm:label key="espacoBranco" width="56%" />
		<adsm:textbox dataType="integer" property="nrContaCorrente" label="contaCiaAerea" 
			maxLength="11" size="12" labelWidth="20%" width="15%" disabled="true" />				
		
		<%--------------------------%>
		<%-- DADOS DO AWB SECTION --%>
		<%--------------------------%>
		<adsm:section caption="dadosAWB"/>
		
		<%-----------------------%>
		<%-- DS EMBALAGEM TEXT --%>
		<%-----------------------%>
		<adsm:textbox 
			property="embalagem.dsEmbalagem" 
			label="embalagem" 
			dataType="text" 
			size="30"
			maxLength="60" 
			labelWidth="21%" 
			width="47%" 
			disabled="true"/>
		
		
		<adsm:textbox 
			property="fatura.numeroFatura" 
			label="numeroFatura" 
			dataType="text" 
			size="15"
			maxLength="60" 
			labelWidth="16%" 
			width="15%" 
			disabled="true"/>
		

		<%-------------------------------%>
		<%-- NR TARIFA ESPECIFICA TEXT --%>
		<%-------------------------------%>
		<adsm:textbox
			dataType="text"
			property="produtoEspecifico.nrTarifaEspecifica" 
			label="tarifaEspecifica" 
			labelWidth="21%" 
			width="13%" 
			size="5"
			disabled="true"/>
		
		<%----------------------%>
		<%-- TARIFA SPOT TEXT --%>
		<%----------------------%>
		<adsm:textbox 
			property="tarifaSpot.dsSenha" 
			label="tarifaSPOT" 
			dataType="text" 
			size="10" 
			maxLength="18" 
			labelWidth="17%" 
			width="13%" 
			disabled="true"/>
			
		<%-------------------------%>
		<%-- AUTORIZADA POR TEXT --%>
		<%-------------------------%>
		<adsm:textbox 
			label="autorizadaPor" 
			property="tarifaSpot.usuarioByIdUsuarioLiberador.nmUsuario" 
			size="42" 
			dataType="text" 
			labelWidth="21%" 
			width="47%" 
			disabled="true"/>
		
		<%-------------------------%>
		<%-- DT AUTORIZACAO TEXT --%>
		<%-------------------------%>
		<adsm:textbox 
			property="tarifaSpot.dtLiberacao" 
			label="dataAutorizacao" 
			dataType="JTDate" 
			labelWidth="16%" 
			width="16%" 
			disabled="true" 
			picker="false"/>
		
		<%-------------------%>
		<%-- TP FRETE TEXT --%>
		<%-------------------%>
		<adsm:textbox 
			property="tpFrete" 
			label="tipoFrete" 
			dataType="text"
			size="7"
			labelWidth="21%" 
			width="13%" 
			disabled="true"/>
		
		<%------------------------------%>
		<%-- DS NATUREZA PRODUTO TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			property="naturezaProduto.dsNaturezaProduto" 
			label="naturezaProduto" 
			dataType="text"
			labelWidth="17%" 
			width="17%" 
			disabled="true"/>
		
		<%---------------------%>
		<%-- QT VOLUMES TEXT --%>
		<%---------------------%>
		<adsm:textbox 
			property="qtVolumes" 
			label="qtdeVolumes" 
			size="10" 
			dataType="integer" 
			maxLength="18" 
			labelWidth="16%" 
			width="16%" 
			disabled="true"/>
		
		
		<%--------------------%>
		<%-- VALOR TOTAL DOCUMENTOS TEXT --%>
		<%--------------------%>
		<adsm:textbox 
			property="vlTotalDocumentos" 
			label="receitaTotalDocumentos"
			dataType="currency" 
			labelWidth="21%" 
			width="13%" 
			size="10"
			disabled="true"/>		
		
		<%------------------%>
		<%-- PS REAL TEXT --%>
		<%------------------%>
		<adsm:textbox 
			property="psTotal" 
			label="pesoReal" 
			dataType="decimal" 
			mask="#,###,###,###,##0.000"
			unit="kg" 
			size="10" 
			labelWidth="17%" 
			width="17%" 
			disabled="true"/>
		
		<%--------------------%>
		<%-- PS CUBADO TEXT --%>
		<%--------------------%>
		<adsm:textbox 
			property="psCubado" 
			label="pesoCubado" 
			dataType="decimal" 
			mask="#,###,###,###,##0.000"
			unit="kg" 
			size="10" 
			labelWidth="16%" 
			width="16%" 
			disabled="true"/>
		
		<%--------------------------%>
		<%-- NR VOO PREVISTO TEXT --%>
		<%--------------------------%>
		<adsm:textbox 
			property="dsVooPrevisto" 
			label="numeroVooPrevisto" 
			dataType="integer" 
			size="10" 
			labelWidth="21%" 
			width="30%" 
			disabled="true"/>
		
		<%----------------------------%>
		<%-- DH PREVISTA SAIDA TEXT --%>
		<%----------------------------%>
		<adsm:textbox 
			property="dhPrevistaSaida" 
			label="dataHoraDeSaidaPrevista" 
			dataType="JTDateTimeZone" 
			size="10" 
			labelWidth="25%" 
			width="24%" 
			disabled="true" 
			picker="false"/>
		
		<%------------------------------%>
		<%-- SG AEROPORTO ESCALA TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			property="aeroportoByIdAeroportoEscala.sgAeroporto" 
			label="via" 
			dataType="text" 
			size="5" 
			labelWidth="21%" 
			width="30%" 
			disabled="true"/>
		
		<%------------------------------%>
		<%-- DH PREVISTA CHEGADA TEXT --%>
		<%------------------------------%>
		<adsm:textbox 
			property="dhPrevistaChegada" 
			label="dataHoraChegadaPrevista" 
			dataType="JTDateTimeZone" 
			size="10" 
			labelWidth="25%" 
			width="24%" 
			disabled="true" 
			picker="false"/> 

		<adsm:hidden property="cliente.nrIdentificacao" serializable="false"/>
		<adsm:hidden property="origem" value="consultarAWB" serializable="false"/>
		<adsm:hidden property="idServicoAereoNacConv" serializable="false"/>
		<adsm:hidden property="naturezaProduto.idNaturezaProduto" serializable="false"/>
		<adsm:hidden property="aeroportoByIdAeroportoDestino.pessoa.enderecoPessoa.municipio.nmMunicipio" serializable="false"/>
		<adsm:hidden property="moeda.idMoeda" serializable="false"/>
		<adsm:hidden property="moeda.dsSimbolo" serializable="false"/>
		<adsm:hidden property="awb.nrAwb" serializable="false"/>
		<adsm:hidden property="awb.ctoAwbs.vlMercadoria" serializable="false"/>		
		<adsm:hidden property="filialByIdFilialOrigem.idFilial" serializable="false"/>
		<adsm:hidden property="filialByIdFilialDestino.idFilial" serializable="false"/>
		<adsm:hidden property="ciaFilialMercurio.empresa.idEmpresa" serializable="false"/>
		<adsm:hidden property="pesoTotal" serializable="false"/>
		<adsm:hidden property="awb.tpLocalizacao" serializable="false"/>
				
		<adsm:buttonBar>
		
			<adsm:button id="pedidoColetaButton" caption="pedidoColeta" action="/coleta/cadastrarPedidoColeta" cmd="main" disabled="false" boxWidth="100" >
				<adsm:linkProperty src="cliente.nrIdentificacao" target="cliente.pessoa.nrIdentificacao"/>			
				<adsm:linkProperty src="origem" target="origem"/>
				<adsm:linkProperty src="idServicoAereoNacConv" target="servico.idServico"/>
				<adsm:linkProperty src="naturezaProduto.idNaturezaProduto" target="naturezaProduto.idNaturezaProduto"/>
				<adsm:linkProperty src="tpFrete" target="tpFrete"/>
				<adsm:linkProperty src="aeroportoByIdAeroportoDestino.pessoa.enderecoPessoa.municipio.nmMunicipio" target="municipioPessoa.nmMunicipio"/>
				<adsm:linkProperty src="moeda.idMoeda" target="moeda.idMoeda"/>
				<adsm:linkProperty src="moeda.dsSimbolo" target="siglaSimbolo"/>
				<adsm:linkProperty src="qtVolumes" target="qtTotalVolumesInformado"/>
				<adsm:linkProperty src="pesoTotal" target="psTotalInformado"/>
				<adsm:linkProperty src="awb.idAwb" target="awb.idAwb"/>
				<adsm:linkProperty src="awb.nrAwb" target="awb.nrAwb"/>
				<adsm:linkProperty src="awb.ctoAwbs.vlMercadoria" target="vlTotalInformado"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.idFilial" target="filialByIdFilialResponsavel.idFilial"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.sgFilial" target="filialSigla"/>
				<adsm:linkProperty src="filialByIdFilialOrigem.pessoa.nmFantasia" target="filialNome"/>
				<adsm:linkProperty src="filialByIdFilialDestino.sgFilial" target="filial.sgFilial"/>
				<adsm:linkProperty src="filialByIdFilialDestino.idFilial" target="filial.idFilial"/>		
				<adsm:linkProperty src="filialByIdFilialDestino.pessoa.nmFantasia" target="filial.pessoa.nmFantasia"/>
				<adsm:linkProperty src="ciaFilialMercurio.empresa.idEmpresa" target="awb.ciaFilialMercurio.empresa.idEmpresa"/>				
			</adsm:button>
		
			<adsm:button			
 				id="btnEmitirEspelho"
 				caption="emitir"
 				buttonType="reportViewerButton"
 				boxWidth="80"
 				onclick="emitirEspelho();"/>
		
			<adsm:button			
				id="btnObservacoes"
				caption="observacoes" 
				boxWidth="90" 
				onclick="showObservacoes();"/>
				
			<adsm:button 
				id="btnCtrcs"
				caption="ctrcs" 
				onclick="showCtrcs();"/>
			
			<adsm:button 
				id="btnDimensoes"
				caption="dimensoes" 
				boxWidth="80" 
				onclick="showDimensoes();"/>
			
			<adsm:button 
				id="btnNotasFiscais"
				caption="notasFiscais" 
				boxWidth="90" 
				onclick="showNotasFiscais();"/>
			
			<adsm:button 
				id="btnCalculoFrete"
				caption="calculoFrete" 
				boxWidth="100" 
				onclick="showCalculoFrete();" />
				
			<adsm:button
				caption="cancelar"
				id="btnCancelar"
				onclick="showCancelarAwb();"
			/>
			
		</adsm:buttonBar>
	</adsm:form>
</adsm:window>

<script src="../lib/expedicao.js"></script>
<script type="text/javascript">

function initWindow(eventObj) {
	changeButtonsStatus(false);
}

function emitirEspelho(){
	executeReportWithCallback('lms.expedicao.consultarAWBsAction', 'emitirEspelho', document.forms[0]);
}

function emitirEspelho_cb(strFile, error){
	if (error){
		alert(error);
		return false;
	} 
    openReportWithLocator(strFile._value, error);
}

function showObservacoes() {
	showModalDialog('expedicao/consultarAWBsObservacoes.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:530px;dialogHeight:200px;');
}

function showCtrcs() {
	showModalDialog('expedicao/consultarAWBsCTRCs.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:820px;dialogHeight:290px;');
}

function showDimensoes() {
	showModalDialog('expedicao/consultarAWBsDimensoes.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:500px;dialogHeight:320px;');
}

function showNotasFiscais() {
	showModalDialog('expedicao/consultarAWBsNFs.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:450px;dialogHeight:290px;');
}

function showCalculoFrete() {
	showModalDialog('expedicao/consultarAWBsCalcularFrete.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:420px;dialogHeight:300px;');
}

function showCancelarAwb() {
	showModalDialog('expedicao/cancelarAWB.do?cmd=main',window,'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:680px;dialogHeight:200px;');
	populaForm();
}

//Método que chama o serviço da action para buscar o elemento selecionado na grid
function populaForm() {
	var sdo = createServiceDataObject("lms.expedicao.consultarAWBsAction.findById", "findId", {idAwb:getElementValue("awb.idAwb")} );
	xmit({serviceDataObjects:[sdo]});
}

// Callback que popula os campos da tela após o usuário 
// selecionar uma linha da grid
function findId_cb(data,erros){
	if (erros!=undefined){
		alert(erros)
		return false;
	}
	
	onDataLoad_cb(data);
// 	carregaDadosPagina_cb(data,erros);
}

function changeButtonsStatus(status) {
	setDisabled("btnObservacoes", status);
	setDisabled("btnCtrcs", status);
	setDisabled("btnDimensoes", status);
	setDisabled("btnNotasFiscais", status);
	setDisabled("btnCalculoFrete", status);
}

/*****************************/
/* ENVIA DOCTO P/ IMPRESSORA */
/*****************************/
function printAWB(data) {
	if((data == undefined) || (data == "")) {
		alert(i18NLabel.getLabel("LMS-04100"));
		return;
	}

	var printer = window.top[0].document.getElementById("printer");
	printer.print(data);
	alertI18nMessage("LMS-04101", "1", false);
}

</script>