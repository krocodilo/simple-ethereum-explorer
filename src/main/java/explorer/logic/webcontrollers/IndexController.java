package explorer.logic.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String index(Model model) {

        model.addAttribute("pagetitle", "Ethereum Explorer");

        return "index";
    }

}
