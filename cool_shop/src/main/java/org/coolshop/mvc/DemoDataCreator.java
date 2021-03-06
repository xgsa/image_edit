package org.coolshop.mvc;

import org.apache.log4j.Logger;
import org.coolshop.domain.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Repository
public class DemoDataCreator {

    private static final Logger LOG = Logger.getLogger(DemoDataCreator.class);

    @Autowired
    private SessionFactory sessionFactory;

    private Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    private Session session;

    private User customerUser;
    private List<Upc> upcsToOrder = new ArrayList<>();


    //@PostConstruct
    void initDB() {
        LOG.info("Create DB & and fill with demo data...");

        session = sessionFactory.openSession();
        session.beginTransaction();
        try {

            createUsers();
            createProducts();
            createOrders();

            session.getTransaction().commit();
        } catch (Exception e) {
            LOG.error("ERROR during DB creation & and filling it with demo data!", e);
            session.getTransaction().rollback();
        }
        finally {
            session.close();
        }
    }

    // Just for convenient notation
    private String encode(String password) {
        return passwordEncoder.encodePassword(password, null);
    }

    private void createUsers() {
        session.save(new User("root", encode("111"), "Global Administrator", User.Role.Admin));
        session.save(new User("admin", encode("111"), "Another-One-Administrator", User.Role.Admin));
        session.save(new User("petr", encode("111"), "Petr I", User.Role.Manager));
        customerUser = new User("vasia", encode("111"), "Vasiliy Petrovich", User.Role.User);
        session.save(customerUser);
    }

    void createProducts() {
        Attribute
            mark = new Attribute("mark"),
            color = new Attribute("color"),
            buttons = new Attribute("buttons"),
            wheel = new Attribute("wheel");

        Product product;
        Upc upc;
        Store store;

        store = new Store("Mobilochka", "Kharkov, Lenina av. 77");
        session.save(store);

        product = new Product("Sony Xperia Z2 D6502", "Sony Xperia Z2 D6502.png");
        product.getAttributes().add(new AttributeValue(mark, "LG"));
        upc = new Upc(product, (long) 99);
        upc.addStore(store);
        upc.getAttributes().add(new AttributeValue(color, "black"));
        session.save(upc);
        upc = new Upc(product, (long) 101);
        upc.addStore(store);
        upc.getAttributes().add(new AttributeValue(color, "white"));
        session.save(upc);

        store = new Store("PC Shop", "Kharkov, Pravdy av. 42");
        session.save(store);

        product = new Product("HP ProBook 470 G1", "HP ProBook 470 G1.png");
        product.getAttributes().add(new AttributeValue(mark, "HP"));
        upc = new Upc(product, (long) 999);
        upc.addStore(store);
        upc.getAttributes().add(new AttributeValue(color, "Grey"));
        session.save(upc);
        upcsToOrder.add(upc);

        product = new Product("Samsung S22C200B", "Samsung S22C200B.png");
        product.getAttributes().add(new AttributeValue(mark, "Samsung"));
        upc = new Upc(product, (long) 399);
        upc.addStore(store);
        session.save(upc);
        upcsToOrder.add(upc);

        product = new Product("Logitech Gaming Mouse G300", "Logitech Gaming Mouse G300.png");
        product.getAttributes().add(new AttributeValue(mark, "Logitech"));
        upc = new Upc(product, (long) 29);
        upc.addStore(store);
        upc.getAttributes().add(new AttributeValue(buttons, "3"));
        upc.getAttributes().add(new AttributeValue(wheel, "Yes"));
        session.save(upc);
        upcsToOrder.add(upc);
    }

    void createOrders() {
        Order order;
        order = new Order(customerUser);
        order.getUpcs().addAll(upcsToOrder);
        order.setStatus(Order.Status.Submitted);
        session.save(order);
    }
}
