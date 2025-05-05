package ism.lab02_ism.config;

import ism.lab02_ism.entity.*;
import ism.lab02_ism.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public DataInitializer(
            UserRepository userRepository,
            MenuItemRepository menuItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            createUsers();
        }

        if (menuItemRepository.count() == 0) {
            createMenuItems();
        }

        createSampleCart();

        createSampleOrders();
    }

    private void createUsers() {
        User admin = new User();
        admin.setUserId("admin1");
        admin.setName("Admin User");
        admin.setEmail("admin@sushizen.com");
        admin.setPassword("admin123");
        admin.setPhoneNumber("123-456-7890");
        admin.setRole(User.UserRole.valueOf("ADMIN"));
        userRepository.save(admin);

        User kitchenStaff = new User();
        kitchenStaff.setUserId("kitchen1");
        kitchenStaff.setName("Kitchen User");
        kitchenStaff.setEmail("kitchen@sushizen.com");
        kitchenStaff.setPassword("kitchen123");
        kitchenStaff.setPhoneNumber("123-456-7891");
        kitchenStaff.setRole(User.UserRole.valueOf("KITCHEN_STAFF"));
        userRepository.save(kitchenStaff);

        User customer = new User();
        customer.setUserId("1");
        customer.setName("Test Customer");
        customer.setEmail("customer@sushizen.com");
        customer.setPassword("customer123");
        customer.setPhoneNumber("123-456-7892");
        customer.setRole(User.UserRole.valueOf("CUSTOMER"));
        userRepository.save(customer);

        User customer2 = new User();
        customer2.setUserId("2");
        customer2.setName("Another Customer");
        customer2.setEmail("customer2@sushizen.com");
        customer2.setPassword("customer456");
        customer2.setPhoneNumber("123-456-7893");
        customer2.setRole(User.UserRole.valueOf("CUSTOMER"));
        userRepository.save(customer2);
    }

    private void createMenuItems() {
        createMenuItem(
                "item1",
                "California Roll",
                "Crab, avocado and cucumber roll",
                8.99f,
                "https://img.freepik.com/free-photo/closeup-shot-sushi-roll-black-stone-plate_181624-28308.jpg?t=st=1746432461~exp=1746436061~hmac=b09c45c630b8d23a0358901c58df8a5721d65809027b48f1f3d7a806c8806acc&w=1380",
                true);

        createMenuItem(
                "item2",
                "Salmon Nigiri",
                "Fresh salmon over pressed vinegared rice",
                6.99f,
                "https://img.freepik.com/free-photo/close-up-sticks-holding-tasty-sushi_23-2148862957.jpg?t=st=1746432565~exp=1746436165~hmac=1039f38cecf00fa2d74e5623ff80a44f6bd334548fb467520473ddce70e7989b&w=826",
                true);

        createMenuItem(
                "item3",
                "Dragon Roll",
                "Eel and cucumber inside, avocado and eel sauce on top",
                12.99f,
                "https://img.freepik.com/free-photo/sushi-set-table_140725-6829.jpg?t=st=1746432636~exp=1746436236~hmac=bf705a0cc5f448b75125c5f7bbcc5f396ec9cbc24d98d3b54c06bc8df1d3b4b9&w=740",
                true);

        createMenuItem(
                "item4",
                "Spicy Tuna Roll",
                "Spicy tuna and cucumber",
                9.99f,
                "https://img.freepik.com/free-photo/closeup-shot-sushi-roll-black-stone-plate_181624-22537.jpg?t=st=1746432690~exp=1746436290~hmac=b58552b1e82a2aa8f9b48e27bb8be1b00c4a14b1e8b74aae723e8df88491f81d&w=1380",
                true);

        createMenuItem(
                "item5",
                "Vegetable Tempura",
                "Assorted vegetables fried in tempura batter",
                7.99f,
                "https://img.freepik.com/free-photo/fried-shrimps-with-greens-garnish-sauce-served-white-flat-tray_181624-32192.jpg?t=st=1746432724~exp=1746436324~hmac=ff9b838370b9399cd56828126114c2d92b8412273862a6d342fa6d984577736c&w=1380",
                false);
    }

    private void createMenuItem(
            String itemId,
            String name,
            String description,
            float price,
            String pictureUrl,
            boolean available) {

        if (menuItemRepository.existsByItemId(itemId)) {
            return;
        }

        MenuItem item = new MenuItem();
        item.setItemId(itemId);
        item.setItemName(name);
        item.setItemDescription(description);
        item.setItemPrice(price);
        item.setItemPicture(pictureUrl);
        item.setAvailable(available);
        menuItemRepository.save(item);
    }

    private void createSampleCart() {
        Optional<User> customerOpt = userRepository.findByUserId("1");
        if (!customerOpt.isPresent()) {
            return;
        }

        User customer = customerOpt.get();

        Optional<Cart> existingCart = cartRepository.findByUser(customer);
        if (existingCart.isPresent()) {
            return;
        }

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID().toString());
        cart.setUser(customer);
        cart.setTotalPrice(0);
        cart = cartRepository.save(cart);

        Optional<MenuItem> item1 = menuItemRepository.findByItemId("item1");
        Optional<MenuItem> item2 = menuItemRepository.findByItemId("item2");

        if (item1.isPresent() && item2.isPresent()) {
            CartItem cartItem1 = new CartItem();
            cartItem1.setCart(cart);
            cartItem1.setMenuItem(item1.get());
            cartItem1.setQuantity(2);
            cartItem1.setTotalPrice(item1.get().getItemPrice() * 2);
            cartItemRepository.save(cartItem1);

            CartItem cartItem2 = new CartItem();
            cartItem2.setCart(cart);
            cartItem2.setMenuItem(item2.get());
            cartItem2.setQuantity(3);
            cartItem2.setTotalPrice(item2.get().getItemPrice() * 3);
            cartItemRepository.save(cartItem2);

            float cartTotal = cartItem1.getTotalPrice() + cartItem2.getTotalPrice();
            cart.setTotalPrice(cartTotal);
            cartRepository.save(cart);
        }
    }

    private void createSampleOrders() {
        Optional<User> customerOpt = userRepository.findByUserId("1");
        if (!customerOpt.isPresent()) {
            return;
        }

        User customer = customerOpt.get();

        createOrder(customer, "order1", "PENDING", "PAID", Arrays.asList(
                createOrderItem("item1", 2),
                createOrderItem("item3", 1)));

        createOrder(customer, "order2", "PREPARING", "PAID", Arrays.asList(
                createOrderItem("item2", 4),
                createOrderItem("item4", 1)));

        createOrder(customer, "order3", "DELIVERED", "PAID", Arrays.asList(
                createOrderItem("item1", 1),
                createOrderItem("item5", 2)));
    }

    private void createOrder(User user, String orderId, String status, String paymentStatus,
            List<Map<String, Object>> items) {
        if (orderRepository.findByOrderId(orderId).isPresent()) {
            return;
        }

        Order order = new Order();
        order.setOrderId(orderId);
        order.setUser(user);
        order.setStatus(Order.OrderStatus.valueOf(status));
        order.setPaymentStatus(Order.PaymentStatus.valueOf(paymentStatus));
        order.setCreatedAt(LocalDateTime.now());

        float totalPrice = 0;

        order = orderRepository.save(order);

        for (Map<String, Object> itemInfo : items) {
            String menuItemId = (String) itemInfo.get("itemId");
            int quantity = (int) itemInfo.get("quantity");

            Optional<MenuItem> menuItemOpt = menuItemRepository.findByItemId(menuItemId);
            if (menuItemOpt.isPresent()) {
                MenuItem menuItem = menuItemOpt.get();

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setMenuItem(menuItem);
                orderItem.setQuantity(quantity);

                float itemTotal = menuItem.getItemPrice() * quantity;
                orderItem.setTotalPrice(BigDecimal.valueOf(itemTotal));

                orderItemRepository.save(orderItem);

                totalPrice += itemTotal;
            }
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
    }

    private Map<String, Object> createOrderItem(String itemId, int quantity) {
        Map<String, Object> item = new HashMap<>();
        item.put("itemId", itemId);
        item.put("quantity", quantity);
        return item;
    }
}