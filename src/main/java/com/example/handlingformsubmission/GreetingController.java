package com.example.handlingformsubmission;

import jxl.write.WriteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class GreetingController {
    private final DynamoDBEnhanced dde;
    private final WriteExcel writeExcel;
    private final EmailService emailService;

    @Autowired
    GreetingController(
            DynamoDBEnhanced dde, WriteExcel writeExcel, EmailService emailService
    ) {
        this.dde = dde;
        this.writeExcel = writeExcel;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String greetingForm(Model model) {
        model.addAttribute("EnhancedLogItem", new EnhancedLogItem());
        return "greeting";
    }

    @PostMapping("/Search")
    public String adminSubmit(@ModelAttribute SearchItem search, Model model) throws WriteException, IOException {

        System.out.println("Search Time Period: " + search.getTimePeriodDays());
        System.out.println("Search H-Number: " + search.getHNumber());
        System.out.println("Search Email: " + search.getEmail());
        System.out.println("Search Device: " + search.getDevice());

        List<EnhancedLogItem> items;
        if (search.getHNumber() != "") {
            items = dde.scanFilterHNumber(search);
        } else if (search.getDevice() != "") {
            items = dde.getItemsByDevice(search);
        } else {
            items = dde.getAllItems(search);
        }

        if (search.getEmail() != null && search.getEmail() != ""){
            model.addAttribute("SearchItem" , search);
            InputStream attachment = writeExcel.write(items);
            emailService.sendReport(attachment, search);
            return "Email";
        } else {
            model.addAttribute("items", items);
            return "Display";
        }
    }

    @GetMapping("/admin")
    public String adminForm(Model model){
        model.addAttribute("search", new SearchItem());
        return "Search";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute EnhancedLogItem logItem, Model model) {
        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter Dateformatter
                = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter Timeformatter
                = DateTimeFormatter.ofPattern("HH:mm:ss");

        String dateString = now.format(Dateformatter);
        String timeString = now.format(Timeformatter);

        logItem.setDate(dateString);
        logItem.setTime(timeString);
        logItem.setDTS(now.toString());
        logItem.setHNumber(logItem.getHNumber().toUpperCase());

        dde.injectDynamoItem(logItem);
        model.addAttribute("name", logItem.getName().split(" ")[0]);

        return "result";
    }
}