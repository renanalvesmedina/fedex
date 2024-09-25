package com.mercurio.lms.tracking.util;

import com.mercurio.lms.tracking.Consignment;
import com.mercurio.lms.tracking.DeliveryLocation;
import com.mercurio.lms.tracking.Depot;
import com.mercurio.lms.tracking.Depots;
import com.mercurio.lms.tracking.Element;
import com.mercurio.lms.tracking.Event;
import com.mercurio.lms.tracking.Events;
import com.mercurio.lms.tracking.Invoice;
import com.mercurio.lms.tracking.Invoices;
import com.mercurio.lms.tracking.Scheduling;
import com.mercurio.lms.tracking.Schedulings;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class AliasTracking{
	
	public static XStream createAlias(){
		
		/**
		 * alias para search tracking		
		 */
		//remove os uderscore duplos 
		XStream xstream = new XStream(new XppDriver(new XmlFriendlyReplacer("_-", "_")));
	
        xstream.alias("element", Element.class);
        xstream.alias("invoice", Invoice.class);
        xstream.alias("event", Event.class);
        xstream.alias("scheduling", Scheduling.class);
        xstream.alias("delivery_location", DeliveryLocation.class);
        
        xstream.aliasField("estimated_date_delivery", Consignment.class, "estimatedDateDelivery");
        xstream.aliasField("pickup_date", Consignment.class, "pickupDate");
        xstream.aliasField("delivery_date", Consignment.class, "deliveryDate");
        
        xstream.aliasField("issued_date", Invoice.class, "issuedDate");
        
        xstream.aliasField("depot_code", Event.class, "depotCode");
        
        xstream.aliasField("contactDate", Scheduling.class, "contactDate");
        xstream.aliasField("depot_code", Scheduling.class, "depotCode");
        xstream.aliasField("depot_name", Scheduling.class, "depotName");
        xstream.aliasField("scheduling_date", Scheduling.class, "schedulingDate");
        
        xstream.aliasField("delivery_location", Element.class, "deliveryLocation");
        
        //remove duplicidade de tag quando uma lista � adicionada no XML
        xstream.addImplicitCollection(Invoices.class, "invoices");
        xstream.addImplicitCollection(Events.class, "events"); 
        xstream.addImplicitCollection(Schedulings.class, "schedulings");
        
		return xstream;
	}

}
