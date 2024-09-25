package com.mercurio.lms.expedicao.util;

import com.mercurio.lms.layoutNfse.model.cancelamento.Cancelamento;
import com.mercurio.lms.layoutNfse.model.impressao.ImpressaoControlada;
import com.mercurio.lms.layoutNfse.model.impressao.ImpressaoControladaNfse;
import com.mercurio.lms.layoutNfse.model.impressao.ImpressaoControladaRps;
import com.mercurio.lms.layoutNfse.model.impressao.Nfse;
import com.mercurio.lms.layoutNfse.model.retorno.Dados;
import com.mercurio.lms.layoutNfse.model.retorno.IntegrationXml;
import com.mercurio.lms.layoutNfse.model.retorno.MensagemRetorno;
import com.mercurio.lms.layoutNfse.model.retorno.Param;
import com.mercurio.lms.layoutNfse.model.rps.CampoAdicional;
import com.mercurio.lms.layoutNfse.model.rps.Deducao;
import com.mercurio.lms.layoutNfse.model.rps.Email;
import com.mercurio.lms.layoutNfse.model.rps.InfRps;
import com.mercurio.lms.layoutNfse.model.rps.Item;
import com.mercurio.lms.layoutNfse.model.rps.ListaDeducoes;
import com.mercurio.lms.layoutNfse.model.rps.ListaItens;
import com.mercurio.lms.layoutNfse.model.rps.ReceiverToList;
import com.mercurio.lms.layoutNfse.model.rps.Rps;
import com.thoughtworks.xstream.XStream;

/**
 * Classe utilitária responsavel por criar os alias para os XML's que são enviados e retornados para/de NDDigital 
 * @author lucianos
 *
 */
public class AliasLayoutNfse {

	public static XStream createAliasEnvio(XStream xstream){
		
        xstream.alias("Rps", Rps.class);
        xstream.alias("InfRps", InfRps.class);
        xstream.alias("CampoAdicional", CampoAdicional.class);  
        xstream.alias("Item", Item.class);
        xstream.alias("Deducao", Deducao.class);
        xstream.alias("ReceiverToList", ReceiverToList.class);
        xstream.alias("ReceiverTo", String.class);
        
      //remove duplicaçao de tag quando é inserido uma list na tag.
        xstream.addImplicitCollection(ListaItens.class, "Item");
        xstream.addImplicitCollection(ListaDeducoes.class, "Deducao");
        xstream.addImplicitCollection(Email.class, "ReceiverToList");
        
        xstream.useAttributeFor("Id", String.class);
        xstream.useAttributeFor("Versao", String.class);
        
        return xstream;	
	}
	
	public static XStream createAliasImpressaoControlada(XStream xStream){
		
		xStream.alias("ImpressaoControlada", ImpressaoControlada.class);
		xStream.alias("Rps", com.mercurio.lms.layoutNfse.model.impressao.Rps.class);
		xStream.alias("Nfse", Nfse.class);
		
		xStream.addImplicitCollection(ImpressaoControladaRps.class, "rps");
		xStream.addImplicitCollection(ImpressaoControladaNfse.class, "nfse");
		
		return xStream;
	}
	
	
	public static XStream createAliasRetorno(XStream xStream){
		
		xStream.alias("IntegrationXml", IntegrationXml.class);
		 
		xStream.alias("Param", Param.class);
		xStream.addImplicitCollection(Dados.class, "param");

		xStream.aliasField("nome", Param.class, "nome");
		xStream.useAttributeFor("nome", String.class);
	     
		xStream.aliasField("valor", Param.class, "valor");
		xStream.useAttributeFor("valor", String.class);
		
		xStream.alias("MensagemRetorno", MensagemRetorno.class);
		
		return xStream;
	}
	
	public static XStream createAliasCancelamento(XStream xStream){
		
		xStream.alias("Cancelamento", Cancelamento.class);
		
		xStream.useAttributeFor("xsd", String.class);
		xStream.useAttributeFor("xsi", String.class);
		xStream.useAttributeFor("Id", String.class);
		xStream.useAttributeFor("Versao", String.class);
		xStream.aliasAttribute("xmlns:xsd", "xsd");
		xStream.aliasAttribute("xmlns:xsi", "xsi");

		return xStream;
	}
}
