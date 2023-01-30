package explorer.logic.webcontrollers;

import explorer.logic.Explorer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/balance")
public class BalanceController {


    @PostMapping
    public String balancePOST(
            @RequestParam String address,
            @RequestParam String date) {
        // Receives the POST request from the Homepage form
        return "redirect:/balance/" + address + "?date=" + date;
    }

    @GetMapping({"/{address}"})
    public String getBalanceAtDate(
            @PathVariable String address,
            @RequestParam(name="date", required=false, defaultValue="2022-12-01") String date,
            Model model ) throws Exception {

        model.addAttribute("balance",
                Explorer.getBalance(address, "2022-12-01") + " ETH"
        );

        model.addAttribute("pagetitle", address + " balance");

        return "balance";  // Returns view balance.html
    }
}
