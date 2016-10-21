package ua.com.hedgehogsoft.oculus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.com.hedgehogsoft.oculus.data.TableHeader;
import ua.com.hedgehogsoft.oculus.model.Constructor;
import ua.com.hedgehogsoft.oculus.model.Order;
import ua.com.hedgehogsoft.oculus.repository.ConstructorRepository;
import ua.com.hedgehogsoft.oculus.repository.OrderRepository;
import ua.com.hedgehogsoft.oculus.validator.ConstructorValidator;
import ua.com.hedgehogsoft.oculus.validator.OrderValidator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Controller
public class OculusController {

    @Autowired
    private ConstructorRepository constructorRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ConstructorValidator constructorValidator;
    @Autowired
    private OrderValidator orderValidator;

    @RequestMapping(value = {"/"}, method = RequestMethod.GET)
    public String oculus(Model model) {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorWithNotArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(order -> !order.isArchive())
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));
        model.addAttribute("con_ord_map", constructorWithNotArchiveOrders);
        model.addAttribute("tableHeaders", TableHeader.values());
        return "oculus";
    }

    @RequestMapping(value = {"/archive"}, method = RequestMethod.GET)
    public String archive(@RequestParam("id") long id, Model model) {
        Constructor constructor = constructorRepository.findOne(id);
        Map<Constructor, List<Order>> constructorArchiveOrders =
                constructor.getOrders().stream().filter(Order::isArchive).sorted(comparing(Order::getPlannedDate))
                        .collect(groupingBy(Order::getConstructor));
        if (constructorArchiveOrders.isEmpty()) {
            constructorArchiveOrders.put(constructor, new ArrayList<>(0));
        }
        model.addAttribute("con_ord_map", constructorArchiveOrders);
        model.addAttribute("tableHeaders", TableHeader.values());
        return "archive";
    }

    @RequestMapping(value = {"/all_archive"}, method = RequestMethod.GET)
    public String allArchive(Model model) {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(Order::isArchive)
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));
        model.addAttribute("con_ord_map", constructorArchiveOrders);
        model.addAttribute("tableHeaders", TableHeader.values());
        return "archive";
    }

    @RequestMapping(value = {"/addcon"}, method = RequestMethod.GET)
    public String addConstructor(Model model) {
        model.addAttribute("constructor", new Constructor());
        return "addcon";
    }

    @RequestMapping(value = {"/addcon"}, method = RequestMethod.POST)
    public String addConstructor(@ModelAttribute("constructor") Constructor constructor, BindingResult bindingResult) {
        constructorValidator.validate(constructor, bindingResult);
        if (bindingResult.hasErrors()) {
            return "addcon";
        }
        constructorRepository.save(constructor);
        return "redirect:/console";
    }

    @RequestMapping(value = {"/conordlist"}, method = RequestMethod.GET)
    public String conOrdList(Model model) {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorWithNotArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(order -> !order.isArchive())
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));
        model.addAttribute("con_ord_map", constructorWithNotArchiveOrders);
        model.addAttribute("tableHeaders", TableHeader.values());
        return "conordlist";
    }

    @RequestMapping(value = {"/conord"}, method = RequestMethod.GET)
    public String conOrd(@RequestParam("id") long id, Model model) {
        Constructor constructor = constructorRepository.findOne(id);
        Map<Constructor, List<Order>> constructorWithNotArchiveOrders =
                constructor.getOrders().stream().filter(order -> !order.isArchive()).sorted(comparing(Order::getPlannedDate))
                        .collect(groupingBy(Order::getConstructor));
        if (constructorWithNotArchiveOrders.isEmpty()) {
            constructorWithNotArchiveOrders.put(constructor, new ArrayList<>(0));
        }
        model.addAttribute("con_ord_map", constructorWithNotArchiveOrders);
        model.addAttribute("tableHeaders", TableHeader.values());
        return "conord";
    }

    @RequestMapping(value = {"/addord"}, method = RequestMethod.GET)
    public String addOrd(@RequestParam("id") long id, Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("constructor_id", id);
        return "addord";
    }

    @RequestMapping(value = {"/addord"}, method = RequestMethod.POST)
    public String addOrd(@RequestParam("id") long id, @ModelAttribute("order") Order order, BindingResult bindingResult) {
        orderValidator.validate(order, bindingResult);
        if (bindingResult.hasErrors()) {
            return "addord";
        }
        Constructor constructor = constructorRepository.findOne(id);
        order.setConstructor(constructor);
        orderRepository.save(order);
        return "redirect:/conord?id=" + id;
    }

    @RequestMapping(value = {"/updord"}, method = RequestMethod.GET)
    public String updOrd(@RequestParam("id") long id, Model model) {
        Order order = orderRepository.findOne(id);
        model.addAttribute("order", order);
        return "updord";
    }

    @RequestMapping(value = {"/updord"}, method = RequestMethod.POST, params = "update")
    public String updOrd(@RequestParam("id") long id, @ModelAttribute("order") Order order, BindingResult bindingResult) {
        orderValidator.validate(order, bindingResult);
        if (bindingResult.hasErrors()) {
            return "updord";
        }
        Order _order = orderRepository.findOne(id);
        Constructor constructor = _order.getConstructor();
        _order.setOrderNumber(order.getOrderNumber());
        _order.setPlannedDate(order.getPlannedDate());
        _order.setActualDate(order.getActualDate());
        _order.setCipher(order.getCipher());
        _order.setProductName(order.getProductName());
        orderRepository.save(_order);
        return "redirect:/conord?id=" + constructor.getId();
    }

    @RequestMapping(value = {"/updord"}, method = RequestMethod.POST, params = "delete")
    public String delOrd(@RequestParam("id") long id, BindingResult bindingResult) {
        Order order = orderRepository.findOne(id);
        Constructor constructor = order.getConstructor();
        orderRepository.delete(id);
        return "redirect:/conord?id=" + constructor.getId();
    }
}
