package explorer.logic.webcontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Serves the root of the website
 */
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
