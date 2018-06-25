/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package obe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import xsd.generated.ExpenseT;
import xsd.generated.ItemListT;
import xsd.generated.ItemT;
import xsd.generated.ObjectFactory;
import xsd.generated.UserT;

/**
 *
 * @author Harikrishna
 */
@Named(value = "messageProducerBean")
@RequestScoped
public class MessageProducerBean {

    @Resource(mappedName = "jms/myQueue")
    private Queue myQueue;

    @Inject
    @JMSConnectionFactory("jms/myQueueFactory")
    private JMSContext context;

    private String message;
    
    /**
     * Creates a new instance of MessageProducerBean
     */
    public MessageProducerBean() {
    }

    
    public void send() throws JAXBException, IOException{
        
               
        
        ObjectFactory factory = new ObjectFactory();
 
        UserT user = factory.createUserT();
        user.setUserName("Sanaulla");
        ItemT item = factory.createItemT();
        item.setItemName("Seagate External HDD");
        item.setPurchasedOn("August 24, 2010");
        item.setAmount(new BigDecimal("6776.5"));
 
        ItemListT itemList = factory.createItemListT();
        itemList.getItem().add(item);
 
        ExpenseT expense = factory.createExpenseT();
        expense.setUser(user);
        expense.setItems(itemList);
 
        JAXBContext jxbcontext = JAXBContext.newInstance("xsd.generated");
        JAXBElement<ExpenseT> element = factory.createExpenseReport(expense);
        Marshaller marshaller = jxbcontext.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        marshaller.marshal(element, baos);
        
        System.out.println("elementtttt----------------------->  "+baos.toString());
        
        FacesContext facesContext = FacesContext.getCurrentInstance();
        message = baos.toString();
        sendJMSMessageToMyQueue(message);
        FacesMessage facesMessage = new FacesMessage("Message sent: " + message);
        facesMessage.setSeverity(FacesMessage.SEVERITY_INFO);
        facesContext.addMessage(null, facesMessage);
        baos.flush();
        baos.close();
        
    }
    
    
    
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private void sendJMSMessageToMyQueue(String messageData) {
        
        context.createProducer().send(myQueue, messageData);
    }
    
    
    
}
