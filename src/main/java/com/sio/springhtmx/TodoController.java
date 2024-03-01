package com.sio.springhtmx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

record Todo(int id, String title, String status) {
}

@Controller
public class TodoController {

    static List<Todo> STORE = new ArrayList<>();

    static {
        IntStream.range(1, 50).forEach(
                it -> {
                    var todo = new Todo(it, "Todo - " + it, it % 2 == 0 ? "Active" : "Inactive");
                    STORE.add(todo);
                });
    }

    @GetMapping()
    public String list(Model model) {
        model.addAttribute("todos", STORE);
        return "todo/todo-list";
    }

    @GetMapping("/filters")
    public String todoList(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            Model model) {

        var list = STORE.stream()
                .filter(it -> title.isEmpty() || it.title().toLowerCase().startsWith(title.toLowerCase()))
                .filter(it -> status.isEmpty() || it.status().toLowerCase().startsWith(status.toLowerCase()))
                .toList();

        model.addAttribute("todos", list);
        return "todo/filtered-todo";
    }

}
