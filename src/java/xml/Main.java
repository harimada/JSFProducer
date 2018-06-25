package xml;
 
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Writer;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.math.BigDecimal;
import javax.xml.transform.sax.SAXResult;
import xsd.generated.ExpenseT;
import xsd.generated.ItemListT;
import xsd.generated.ItemT;
import xsd.generated.ObjectFactory;
import xsd.generated.UserT;
 
public class Main
 
{
    public static void main(String[] args) throws JAXBException, IOException
    {
 
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
 
        JAXBContext context = JAXBContext.newInstance("xsd.generated");
        JAXBElement<ExpenseT> element = factory.createExpenseReport(expense);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(element, baos);
        
        System.out.println("elementtttt----------------------->  "+baos.toString());
        baos.close();
        
    }
 
}