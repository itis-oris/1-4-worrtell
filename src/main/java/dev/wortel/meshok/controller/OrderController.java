package dev.wortel.meshok.controller;

import dev.wortel.meshok.dto.ItemDisplayDto;
import dev.wortel.meshok.dto.OrderDto;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.entity.Order;
import dev.wortel.meshok.entity.PaymentMethod;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.service.CartService;
import dev.wortel.meshok.service.OrderService;
import dev.wortel.meshok.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final ItemMapper itemMapper;
    private final PictureHelper pictureHelper;
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String viewOrders(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long userId = getUserId(principal);
        List<Order> orders = orderService.getUserOrders(userId);

        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/owner")
    public String viewAllOrders(Model model) {
        List<Order> orders = orderService.getAllOrders();

        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/{id}")
    public String viewOrderDetails(@PathVariable Long id, Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Order order = orderService.getOrderById(id);

        List<ItemDisplayDto> itemDtos = order.getItems().stream()
                .map(item -> itemMapper.toItemDisplayDto(item, pictureHelper))
                .toList();

        model.addAttribute("order", order);
        model.addAttribute("itemDtos", itemDtos);
        return "orders/details";
    }

    @PostMapping("/create")
    public String createOrder(
            @ModelAttribute OrderDto orderDTO,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long userId = getUserId(principal);
        log.info("Order created: {} for {}", orderDTO, userId);

        try {
            Order order = orderService.createOrderFromCart(userId, orderDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Заказ успешно создан!");
            return "redirect:/orders/" + order.getId();
        } catch (Exception e) {
            log.error("Error creating order", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при создании заказа: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        Long userId = getUserId(principal);
        List<Item> cartItems = cartService.getCartItems(userId);

        if (cartItems.isEmpty()) {
            model.addAttribute("errorMessage", "Корзина пуста, невозможно оформить заказ");
            return "redirect:/cart";
        }

        List<ItemDisplayDto> displayDtos = cartItems.stream()
                .map(item -> itemMapper.toItemDisplayDto(item, pictureHelper))
                .toList();

        Double totalPrice =cartItems.stream()
                .mapToDouble(item -> Double.parseDouble(item.getPrice()))
                .sum();

        model.addAttribute("cartItems", displayDtos);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderDTO", new OrderDto());
        model.addAttribute("paymentMethods", PaymentMethod.values());

        return "orders/checkout";
    }

    private Long getUserId(Principal principal) {
        return userService.findByEmail(principal.getName()).getId();
    }
}