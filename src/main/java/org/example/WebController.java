package org.example;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final MessageJpaRepository messageJpaRepository;

    @GetMapping("/")
    public String homePageGet(Model model) {
        return "home";
    }

    @PostMapping("/")
    public String homePagePost(@RequestParam String userName, Model model, HttpSession session) {
        session.setAttribute("userName", userName);
        List<Message> messages = messageJpaRepository.findAll();
        session.setAttribute("messages", messages);
        return "redirect:messager";
    }

    @GetMapping("/messager")
    public String workplaceGet(Model model, HttpSession session) {
        return "messager";
    }

    @PostMapping("/addMessage")
    public String addSale(@RequestParam String text, Model model, HttpSession session) {
        Message message = new Message();
        message.setText(text);
        message.setSender((String) session.getAttribute("userName"));
        messageJpaRepository.save(message);
        String sender = (String) session.getAttribute("filter");
        List<Message> messages;
        if (sender == null || sender.equals("")) {
            messages = messageJpaRepository.findAll();
        } else {
            messages = messageJpaRepository.findAll().stream()
                    .filter(m -> m.getSender().equals(sender)).toList();
        }
        session.setAttribute("messages", messages);
        return "redirect:messager";
    }

    @PostMapping("/filterMessages")
    public String filterMessages(@RequestParam String sender, Model model, HttpSession session) {
        session.setAttribute("filter", sender);
        List<Message> messages;
        if (sender.equals("")) {
            messages = messageJpaRepository.findAll();
        } else {
            messages = messageJpaRepository.findAll().stream()
                    .filter(message -> message.getSender().equals(sender)).toList();
        }
        session.setAttribute("messages", messages);
        return "redirect:messager";
    }

//    @PostMapping("/showAllMessages")
//    public String showAllMessages(Model model, HttpSession session) {
//        List<Message> messages = messageJpaRepository.findAll();
//        session.setAttribute("messages", messages);
//        return "redirect:messager";
//    }
}
