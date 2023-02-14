package com.example.manifold;

import lombok.extern.slf4j.Slf4j;
import manifold.ext.rt.api.Jailbreak;
import manifold.science.measures.Length;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static manifold.collections.api.range.RangeFun.step;
import static manifold.collections.api.range.RangeFun.to;
import static manifold.science.util.UnitConstants.km;
import static manifold.science.util.UnitConstants.mi;

@Slf4j
@SpringBootApplication
public class ManifoldApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManifoldApplication.class, args);
    }

    @Bean
    ApplicationRunner ready() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {

                @Jailbreak
                Cart cart = new Cart(new Customer("Josh", new Address("777 Spring St.", "88888")));
                log.info("the total is {}", cart.total.get());
                cart.total.set(100D);
                log.info("the total is {}", cart.total.get());
                cart.recalc();
                log.info("the total is {}", cart.total.get());

                var anonymousTuple = (1, 2, 3);
                log.info("item1: {}", anonymousTuple.item1);

                var namedTuple = (name:"Josh", twitter:"starbuxman", youtube:"https://youtube.com/@coffeesoftware");
                log.info("name tuple youtube: {}", namedTuple.youtube);


                var distanceInKm = (Length) (4 * 2km);
                log.info("distance in Kilometers: " + distanceInKm);

                var distanceInMiles = (Length) (5 * 2mi);
                log.info("distance in Miles: {}", distanceInMiles);

                for (var i : 2to 10step 2) {
                    log.info("ranged value: {}", i);
                }

                cart += new LineItem("spam", 42.7f);
                cart += new LineItem("eggs", 777.42f);
                cart += new LineItem("java", 8888.88f);
                log.info("total: {}", cart.total.get());

                var data = new ArrayList<Number>(List.of(1, 1, 2, 3, 4, 5, 5));
                log.info("chaos! {}", data.chaos());

                log.info(cart.customer.address.street);
            }
        };
    }
}


class Cart {

    private final List<LineItem> lineItems = new ArrayList<>();

    private final AtomicReference<Double> total = new AtomicReference<>();

    private final Customer customer;

    Cart(Customer customer) {
        this.customer = customer;
    }


    public Cart plus(LineItem lineItem) {
        this.lineItems.add(lineItem);
        this.recalc();
        return this;
    }

    private void recalc() {
        synchronized (this.lineItems) {
            this.total.set(this.lineItems.stream()
                    .map(LineItem::price)
                    .mapToDouble(Float::doubleValue)
                    .sum());
        }
    }

}

record LineItem(String product, float price) {
}

record Address(String street, String zipcode) {
}

record Customer(String name, Address address) {
}