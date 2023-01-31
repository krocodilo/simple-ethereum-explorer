package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/balance")
public class BalanceController {


    @PostMapping
    public String balancePOST(
            @RequestParam String address,
            @RequestParam String date
    ) {
        // Receives the POST request from the Homepage form
        return "redirect:/balance/" + address + "?date=" + date;
    }

    @GetMapping({"/{address}"})
    public ModelAndView getBalanceAtDate(
            @PathVariable String address,
            @RequestParam(name="date", required=false) String date
    ) throws Exception {

        ModelAndView mav = new ModelAndView("balance"); // balance.html

        // Validate address
        if( ! Explorer.isValidAddress(address) )
            throw new Exception("Invalid address.");

        mav.addObject("balance",
                Explorer.getBalance(address, date) + " ETH"
        );

        mav.addObject("pagetitle", address + " balance");

        return mav;  // Returns view balance.html
    }
}
