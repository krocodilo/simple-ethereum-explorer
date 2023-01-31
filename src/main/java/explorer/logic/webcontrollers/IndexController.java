package explorer.logic.webcontrollers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public ModelAndView index() {

        ModelAndView mav = new ModelAndView("index");   // index.html

        mav.addObject("pagetitle", "Ethereum Explorer");

        return mav;
    }
}
