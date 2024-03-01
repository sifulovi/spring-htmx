package com.sio.springhtmx;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

record Todo(int id, String title, String status, LocalDate date) {
}

@Controller
public class TodoController {

    static List<Todo> STORE = new ArrayList<>();

    static {
        IntStream.range(1, 50).forEach(
                it -> {
                    var todo = new Todo(it, "Todo - " + it, it % 2 == 0 ? "Active" : "Inactive",
                            LocalDate.now().plusDays(it));
                    STORE.add(todo);
                });
    }

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("todos", STORE);
        model.addAttribute("startDate", LocalDate.now());
        model.addAttribute("endDate", LocalDate.now().plusDays(7));
        return "todo/todo-list";
    }

    @GetMapping("/filters")
    public String todoList(
            @RequestParam(required = false, defaultValue = "") String title,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Model model) {

        var list = STORE.stream()
                .filter(it -> title.isEmpty() || it.title().toLowerCase().startsWith(title.toLowerCase()))
                .filter(it -> status.isEmpty() || it.status().toLowerCase().startsWith(status.toLowerCase()))
                .filter(it -> (startDate == null || endDate == null)
                        || (Objects.requireNonNullElse(it.date(), LocalDate.MIN)
                                .isAfter(Objects.requireNonNullElse(startDate, LocalDate.MIN).minusDays(1))
                                && Objects.requireNonNullElse(it.date(), LocalDate.MAX)
                                        .isBefore(Objects.requireNonNullElse(endDate, LocalDate.MAX).plusDays(1))))
                .toList();

        model.addAttribute("todos", list);
        return "todo/filtered-todo";
    }

}
