package ua.com.hedgehogsoft.oculus.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.hedgehogsoft.oculus.data.TableHeader;
import ua.com.hedgehogsoft.oculus.model.Constructor;
import ua.com.hedgehogsoft.oculus.model.Order;
import ua.com.hedgehogsoft.oculus.print.PrintType;
import ua.com.hedgehogsoft.oculus.print.Printer;
import ua.com.hedgehogsoft.oculus.repository.ConstructorRepository;
import ua.com.hedgehogsoft.oculus.repository.OrderRepository;
import ua.com.hedgehogsoft.oculus.service.FileRepository;
import ua.com.hedgehogsoft.oculus.validator.ConstructorValidator;
import ua.com.hedgehogsoft.oculus.validator.OrderValidator;

import java.io.*;
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
    @Autowired
    private Printer reportPrinter;
    @Autowired
    private FileRepository fileRepository;

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

    @RequestMapping(value = {"/constructor"}, method = RequestMethod.GET)
    public String oculus(@RequestParam("id") long id, Model model) {
        Constructor constructor = constructorRepository.findOne(id);
        Map<Constructor, List<Order>> constructorWithNotArchiveOrders =
                constructor.getOrders().stream().filter(order -> !order.isArchive()).sorted(comparing(Order::getPlannedDate))
                        .collect(groupingBy(Order::getConstructor));
        if (constructorWithNotArchiveOrders.isEmpty()) {
            constructorWithNotArchiveOrders.put(constructor, new ArrayList<>(0));
        }
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
        long id = constructorRepository.save(constructor).getId();
        return "redirect:/conord?id=" + id;
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
    public String addOrd(@RequestParam("con") long conId, @ModelAttribute("order") Order order, BindingResult bindingResult) {
        orderValidator.validate(order, bindingResult);
        if (bindingResult.hasErrors()) {
            return "addord";
        }
        Order _order = new Order();
        Constructor constructor = constructorRepository.findOne(conId);
        _order.setOrderNumber(order.getOrderNumber());
        _order.setPlannedDate(order.getPlannedDate());
        _order.setActualDate(order.getActualDate());
        _order.setCipher(order.getCipher());
        _order.setProductName(order.getProductName());
        _order.setConstructor(constructor);
        orderRepository.save(_order);
        return "redirect:/conord?id=" + conId;
    }

    @RequestMapping(value = {"/updord"}, method = RequestMethod.GET)
    public String updOrd(@RequestParam("id") long id, Model model) {
        Order order = orderRepository.findOne(id);
        model.addAttribute("order", order);
        return "updord";
    }

    @RequestMapping(value = {"/updord"}, method = RequestMethod.POST, params = "update")
    public String updOrd(@RequestParam("ord") long ordId, @ModelAttribute("order") Order order, BindingResult bindingResult) {
        orderValidator.validate(order, bindingResult);
        if (bindingResult.hasErrors()) {
            return "updord";
        }
        Order _order = orderRepository.findOne(ordId);
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
    public String delOrd(@RequestParam("ord") long ordId) {
        Order order = orderRepository.findOne(ordId);
        Constructor constructor = order.getConstructor();
        orderRepository.delete(ordId);
        return "redirect:/conord?id=" + constructor.getId();
    }

    @RequestMapping(value = {"/delcon"}, method = RequestMethod.POST)
    public String delCon(@RequestParam("id") long id) {
        constructorRepository.delete(id);
        return "redirect:/conordlist";
    }

    @RequestMapping(value = "/all_print", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getFullReport() throws FileNotFoundException {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorWithNotArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(order -> !order.isArchive())
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));
        ByteArrayOutputStream os =  reportPrinter.print(constructorWithNotArchiveOrders, PrintType.WORKBOOK);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return getResponse(input, getHeaders());
    }

    @RequestMapping(value = "/all_arch_print", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getFullArchReport() throws FileNotFoundException {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(Order::isArchive)
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));
        ByteArrayOutputStream os =  reportPrinter.print(constructorArchiveOrders, PrintType.ARCHIVE);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return getResponse(input, getHeaders());
    }

    @RequestMapping(value = "/arch_print", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getArchReport(@RequestParam("id") long id) throws FileNotFoundException {
        Constructor constructor = constructorRepository.findOne(id);
        Map<Constructor, List<Order>> constructorArchiveOrders =
                constructor.getOrders().stream().filter(Order::isArchive).sorted(comparing(Order::getPlannedDate))
                        .collect(groupingBy(Order::getConstructor));
        if (constructorArchiveOrders.isEmpty()) {
            constructorArchiveOrders.put(constructor, new ArrayList<>(0));
        }
        ByteArrayOutputStream os =  reportPrinter.print(constructorArchiveOrders, PrintType.ARCHIVE);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return getResponse(input, getHeaders());
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getReport(@RequestParam("id") long id) throws FileNotFoundException {
        Constructor constructor = constructorRepository.findOne(id);
        Map<Constructor, List<Order>> constructorWithNotArchiveOrders =
                constructor.getOrders().stream().filter(order -> !order.isArchive()).sorted(comparing(Order::getPlannedDate))
                        .collect(groupingBy(Order::getConstructor));
        if (constructorWithNotArchiveOrders.isEmpty()) {
            constructorWithNotArchiveOrders.put(constructor, new ArrayList<>(0));
        }
        ByteArrayOutputStream os =  reportPrinter.print(constructorWithNotArchiveOrders, PrintType.WORKBOOK);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return getResponse(input, getHeaders());
    }

    private ResponseEntity<InputStreamResource> getResponse(InputStream input, HttpHeaders headers){
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(input));
    }

    private HttpHeaders getHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }

    @RequestMapping(value = {"/report"}, method = RequestMethod.GET)
    public String report() {
        return "report";
    }

    @RequestMapping(value = "/print_report", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getMonthReport() throws FileNotFoundException {
        List<Constructor> constructors = constructorRepository.findAll();
        Map<Constructor, List<Order>> constructorArchiveOrders =
                constructors.stream().sorted(comparing(Constructor::getName)).collect(toMap(Function.identity(),
                        (constructor) -> constructor.getOrders().stream().filter(Order::isArchive)
                                .sorted(comparing(Order::getPlannedDate)).collect(toList()), (o1, o2) -> o2, LinkedHashMap::new));
        ByteArrayOutputStream os =  reportPrinter.print(constructorArchiveOrders, PrintType.REPORT);
        InputStream input = new ByteArrayInputStream(os.toByteArray());
        return getResponse(input, getHeaders());
    }

    @RequestMapping(value = {"/reportlist"}, method = RequestMethod.GET)
    public String getListReport(Model model) throws IOException {
        List<String> names = fileRepository.getFileNames();
        model.addAttribute("names", names);
        return "reportlist";
    }

    @RequestMapping(value = {"/reports"}, method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> getReport(@RequestParam("name") String name) throws IOException {
        File file = fileRepository.getFileByNme(name);
        HttpHeaders headers = getHeaders();
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    /*@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<InputStreamResource> downloadPDFFile()
            throws IOException {

        ClassPathResource pdfFile = new ClassPathResource("pdf-sample.pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(pdfFile.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(pdfFile.getInputStream()));
    }*/
}
